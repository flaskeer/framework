package cc.hao.data;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import cc.hao.core.Entity;
import cc.hao.core.OMThread;
import cc.hao.util.Cfg;

public class Cache extends OMThread{

	private Map<Long, Entity> map = new ConcurrentHashMap<>();
	
	public Entity get(long id){
		Entity entity = map.get(id);
		return entity == null ? null : null;
	}
	
	public void add(Entity entity){
		if(entity.getId() != 0){
			entity.clear();
		}
		map.put(entity.getId(), entity);
	}
	
	public void delete(long id){
		map.remove(id);
	}
	
	public void clear(){
		long deadTime = System.currentTimeMillis() - Cfg.cache();
		Iterator<Entity> iterator = map.values().iterator();
		while(iterator.hasNext()){
			if(iterator.next().created() > deadTime){
				iterator.remove();
			}
		}
	}
	
	@Override
	protected void doRun() throws Exception {
		Thread.sleep(Cfg.cache());
		while(true){
			Thread.sleep(Cfg.cache());
			clear();
		}
	}

}
