package cc.hao.server;
import static java.net.URLDecoder.*;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cc.hao.util.Cfg;
import cc.hao.util.LoadUtil;
import cc.hao.util.ReflectionUtil;
import cc.hao.util.StringKit;

public class Request {
	
	private static final String FAVICON = "favicon.ico";
	private static final String RESOURCES = "resources";
	
	
	private String head;
	private String body;
	private String path;
	private String[] parts;
	private String entity;
	private Session session;
	private Class<?> clazz;
	private Method method;
	private Map<String,String> params;
	
	public Request(String s) throws Exception {
		String[] in = s.split("(?:\n\n)|(?:\r\n\r\n)");
		head = in[0];
		path = decode(head.split(" ", 3)[1].substring(1), Cfg.charset());
		body = in.length >= 2 ? decode(in[1],Cfg.charset()):null;
		init();
	}
	
	

	public Request(String head, String path) throws Exception {
		this.head = head;
		this.path = decode(path,Cfg.charset());
		this.body = null;
		this.params = null;
		init();
	}



	private void init() {
		parts = path.split("/");
		session = SessionUtil.getSession(head);
		entity = parts[0].isEmpty() ? Cfg.defaultEntity():parts[0];
		if(entity.equals(FAVICON)){
			entity = RESOURCES;
			path = RESOURCES + "/" + path;
		}
	}
	
	public List<Cookie> cookies(){
		return CookieUtil.cookies(head);
	}
	
	public String head(){
		return head;
	}
	
	public Session session(){
		return session;
	}
	
	public Class<?> clazz() throws Exception{
		if(clazz == null){
			clazz = LoadUtil.load(entity);
		}
		return clazz;
	}
	
	public boolean isResources(){
		return entity.equals(RESOURCES);
	}
	
	public Method method() throws Exception{
		if(method == null){
			method = ReflectionUtil.getMethod(clazz(), action());
			return method == null ? ReflectionUtil.getMethod(clazz(),"toView"):method; 
		}
		return method;
	}
	
	public String action(){
		return parts.length >= 2 ? parts[1]:Cfg.defaultAction();
	}

	public Field[] fields() throws Exception{
		return clazz().getDeclaredFields();
	}
	
	public Map<String,String> params(){
		if(params == null){
			params = new HashMap<>();
			return body == null ? fromPath(): fromBody();
		}
		return params;
	}
	
	private Map<String,String> fromBody() {
		for (int i = 2; i < parts.length; i += 2) {
			params.put(parts[i], parts[i + 1]);
		}
		return params;
	}


	private Map<String,String> fromPath() {
		List<String> pairs = StringKit.split(body, '&');
		for (String string : pairs) {
			List<String> list = StringKit.split(string, '=');
			params.put(list.get(0),list.get(1));
		}
		return params;
	}


	public String path(){
		return path;
	}
}
