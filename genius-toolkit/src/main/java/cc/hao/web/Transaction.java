package cc.hao.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cc.hao.core.Entity;
import cc.hao.data.DataUtil;

public class Transaction {

	private boolean forUpdate;
	private Set<Entity> toSave = new HashSet<>();
	private List<Long> toDelete = new ArrayList<>();
	private Map<Long, Entity> cache = new HashMap<>();
	
	
	public Transaction(boolean forUpdate) {
		this.forUpdate = forUpdate;
	}
	
	public Entity get(long id){
		Entity entity = cache.get(id);
		if(entity == null){
			entity = (Entity)DataUtil.get(id,forUpdate);
			entity.setTransaction(this);
			cache(entity);
		}
		return entity;
	}

	public void cache(Entity entity) {
		cache.put(entity.getId(), entity);
	}
	
	public boolean forUpdate(){
		return forUpdate;
	}
	
	public void toSave(Entity entity){
		if(forUpdate && entity.getId() != 0){
			toSave.add(entity);
		}
	}
	
	public void toDelete(long id){
		if(forUpdate && id != 0){
			toDelete.add(id);
			cache.remove(id);
		}
	}
	
	public void commit(){
		try {
			for(Entity entity : toSave){
				DataUtil.save(entity);
			}
			for(long id:toDelete){
				DataUtil.delete(id);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void rockback(){
		clear();
	}

	private void clear() {
		toSave.clear();
		toDelete.clear();
		for(long id : cache.keySet()){
			DataUtil.release(id);
		}
		cache.clear();
	}
	
}
