package cc.hao.data;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

import cc.hao.core.Entity;

public class DataUtil {
	
	private static Set<Long> locks = new ConcurrentSkipListSet<>(); 
	
	private static Cache cache = new Cache();

	public static Entity get(long id, boolean forUpdate) {
		if(forUpdate){
			while(locks.contains(id)){
				
			}
			locks.add(id);
		}
		Entity entity = cache.get(id);
		return entity == null ? loadEntity(id):entity;
	}

	private static Entity loadEntity(long id) {
		
		return null;
	}

	public static synchronized void save(Entity entity) {
		
	}

	public static void delete(long id) {
		
	}

	public static void release(long id) {
		
	}

	public static Entity get(String string, Class<?> clazz, boolean b) {
		return null;
	}

	public static void loadFields(Entity entity, Map<String, String> params, boolean b) {
		
	}

	public static Entity get(Class<?> clazz) {
		// TODO Auto-generated method stub
		return null;
	}

}
