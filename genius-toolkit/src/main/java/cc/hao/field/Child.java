package cc.hao.field;

import cc.hao.core.Entity;
import cc.hao.data.Meta;

public class Child<T extends Entity> extends Reference<T>{
	public Child() {
	}
	
	public Child(Long id){
		super(id);
	}
	
	public Child(String s){
		super(s);
	}
	
	@SuppressWarnings("unchecked")
	public T get() throws Exception{
		Entity e = super.get();
		if(e == null){
			return null;
		}
		if(e.getParent() == null){
			e.setParent(entity);
		}
		return (T) e;
	}
	
	public void set(T value){
		if(unchanged(value)){
			return;
		}
		toDelete(id);
		toSave();
		this.value = value;
		if(value != null){
			updateId();
			value.setParent(entity);
			toSave(value);
		}
	}

	private void updateId() {
		if(value.getId() == 0){
			value.setId(Meta.generateId());
		}
		id = value.getId();
	}
	
}
