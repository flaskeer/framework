package cc.hao.server;

import java.util.HashMap;
import java.util.Map;

public class Session {
	
	private String id;
	private long touched = System.currentTimeMillis();
	private Map<String,Object> map = new HashMap<>();
	
	public Session(String id) {
		this.id = id;
	}
	
	@Override
	public String toString() {
		return "Set-Cookie: session=" + id + ";Path=/";
	}
	
	public void touch(){
		touched = System.currentTimeMillis();
	}
	
	public long touched(){
		return touched;
	}
	
	public void set(String key,Object value){
		map.put(key, value);
	}
	
	public Object get(String key){
		return map.get(key);
	}

}
