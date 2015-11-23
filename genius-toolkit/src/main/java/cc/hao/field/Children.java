package cc.hao.field;

import java.util.concurrent.ConcurrentHashMap;

import cc.hao.core.Entity;

public class Children<T extends Entity> extends References<T> {

	public Children() {
		value = new ConcurrentHashMap<Long,Reference<T>>();
	}
	
	public Children(String s){
		value = new ConcurrentHashMap<>();
		for(String idString : s.split(",")){
			Long id = Long.parseLong(idString);
			value.put(id, new Child<T>(id));
		}
	}
}
