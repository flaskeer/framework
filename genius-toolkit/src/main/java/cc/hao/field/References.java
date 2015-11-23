package cc.hao.field;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import cc.hao.core.Entity;
import cc.hao.util.ArrayKit;

public class References<T extends Entity> extends
                 OMField<Map<Long, Reference<T>>> implements Iterable<Reference<T>>{

	
	private class RefComp implements Comparator<Reference<T>>{

		@Override
		public int compare(Reference<T> o1, Reference<T> o2) {
			try {
				long id1 = o1.getId();
				long id2 = o2.getId();
				return id1 == id2 ? 0 : (id1 > id2 ? -1 : 1);
			} catch (Exception e) {
				
				return 0;
			}
		}
		
	}
	
	public References() {
		value = new HashMap<Long,Reference<T>>();
	}
	
	public References(String s){
		value = new HashMap<Long,Reference<T>>();
		for(String idString:s.split(",")){
			Long id = Long.parseLong(idString);
			value.put(id, new Reference<T>(id));
		}
	}
	
	public void setEntity(Entity entity){
		super.setEntity(entity);
		for(Reference<?> r: value.values()){
			r.setEntity(entity);
		}
	}
	
	
	public Iterator<Reference<T>> iterator() {
		return value.values().iterator();
	}

	public void add(T e) {
		Reference<T> reference = new Reference<>();
		reference.setEntity(e);
		reference.set(e);
		value.put(entity.getId(),reference);
	}

	public void remove(long id) {
		value.remove(id);
		toSave();
	}
	
	public Reference<T> get(long id){
		return value.get(id);
	}
	
	public List<Reference<T>> toList(){
		return ArrayKit.toList(value.values(), new RefComp());
	}
	
	@Override
	public String toString() {
		return ArrayKit.toString(value.values());
	}
	
	public List<Reference<T>> toList(int pageSize,int pageIndex){
		return ArrayKit.toList(value.values(),new RefComp(), pageIndex, pageSize);
	}

}
