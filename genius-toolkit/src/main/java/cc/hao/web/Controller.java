package cc.hao.web;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import cc.hao.core.Entity;
import cc.hao.data.DataUtil;
import cc.hao.core.Annotations.UpdateOptions;
import cc.hao.server.Cookie;
import cc.hao.server.Request;
import cc.hao.util.Cfg;
import cc.hao.util.ReflectionUtil;

public class Controller {

	private Request request;
	
	private Entity entity;

	public Controller(Request request) {
		this.request = request;
	}
	
	public byte[] run() throws Exception{
		if(!AccessChecker.isAllow(request)){
			return toLion();
		}
		String res = doRun();
		List<Cookie> cookies = getCookies(entity);
		return response(res,cookies).getBytes();
	}

	private String response(String res, List<Cookie> cookies) {
		StringBuilder result = new StringBuilder();
		result.append("HTTP/1.1 200 OK\n");
		result.append(request.session() + "\n");
		if(cookies != null){
			for (Cookie cookie : cookies) {
				result.append(cookie + "\n");
			}
		}
		result.append(res + "\n");
		return result.toString();
	}

	@SuppressWarnings("unchecked")
	private List<Cookie> getCookies(Entity entity) throws Exception {
		
		return (List<Cookie>) ReflectionUtil.getField(entity, "cookies");
	}

	public String doRun() throws Exception {
		Method method = request.method();
		UpdateOptions op = method.getAnnotation(UpdateOptions.class);
		Transaction transaction = loadEntity(op);
		try {
			Object res = method.invoke(entity);
			transaction.commit();
			return (String) res;
		} catch (Exception e) {
			transaction.rockback();
			e.printStackTrace();
			return "";
		}
	}

	private Transaction loadEntity(UpdateOptions op) throws Exception {
		Map<String, String> params = request.params();
		entity = DataUtil.get(params.get("id"), request.clazz(),op != null);
		entity.setRequest(request);
		entity.setView(request.action());
		ReflectionUtil.setField(entity,"session", request.session());
		ReflectionUtil.setField(entity, "cookies", request.cookies());
		Transaction transaction = new Transaction(op != null);
		entity.setTransaction(transaction);
		DataUtil.loadFields(entity,params,op != null);
		return transaction;
	}

	private byte[] toLion() throws Exception {
		String path = Cfg.loginEntity() + '/' + Cfg.loginAction();
		Request req = new Request(request.head(), path);
		return new Controller(req).run();
	}
	
}
