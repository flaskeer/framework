package cc.hao.field;

import cc.hao.core.Entity;
import cc.hao.web.Transaction;

public abstract class OMField<T> {

	protected T value;
	protected Entity entity;
	
	public T get() throws Exception{
		return value;
	}
	
	public void set(T value) {
		if(unchanged(value)){
			return;
		}
		this.value = value;
		toSave();
	}
	
	public void setEntity(Entity entity) {
		this.entity = entity;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof OMField<?>)){
			return false;
		}
		return value.equals(((OMField<?>)obj).value);
	}
	
	@Override
	public String toString() {
		return value == null ? "": value.toString();
	}
	
	protected void toDelete(long id){
		if(entity != null && getTransaction() != null){
			getTransaction().toDelete(id);
		}
	}
	
	public Transaction getTransaction(){
		return entity.getTransaction();
	}

	public void toSave() {
		toSave(entity);
	}

	public void toSave(Entity e) {
		if(entity != null && getTransaction() != null){
			getTransaction().toSave(e);
		}
	}

	public boolean unchanged(T v) {
		if(value == null){
			return v == null;
		}
		if(v == null){
			return false;
		}
		return value.equals(v);
	}
	
}
