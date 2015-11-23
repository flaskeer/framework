package cc.hao.web;

import cc.hao.core.Annotations.Role;
import cc.hao.core.Annotations.UpdateOptions;
import cc.hao.server.Request;
import cc.hao.server.Session;
import cc.hao.util.ArrayKit;

public class AccessChecker {

	public static boolean isAllow(Request request) throws Exception {
		String role = roleInSession(request.session());
		
		return isClzAllow(request, role) && isMethodAllow(request, role);
	}
	
	public static boolean isClzAllow(Request request,String role) throws Exception{
		Role r = request.clazz().getAnnotation(Role.class);
		return r == null || r != null && ArrayKit.contains(r.value(),role);
	}
	
	public static boolean isMethodAllow(Request request,String role) throws Exception{
		Role r = request.method().getAnnotation(Role.class);
		if(r != null){
			String[] value = r.value();
			if(!ArrayKit.contains(value, r)){
				return false;
			}
		}
		
		UpdateOptions ops = request.method().getAnnotation(UpdateOptions.class);
		if(ops != null && !checkOptions(request,ops)){
			return false;
		}
		
		return true;
	}

	private static boolean checkOptions(Request request, UpdateOptions ops) {
		
		return false;
	}

	public static String roleInSession(Session session){
		Object roleObj = session.get("role");
		if(roleObj == null){
			return null;
		}
		return (String) roleObj;
	}

}
