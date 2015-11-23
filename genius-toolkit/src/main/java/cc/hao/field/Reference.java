package cc.hao.field;

import cc.hao.core.Entity;

public class Reference<T extends Entity> extends OMField<T> {

	protected long id;
	
	public Reference() {
	}

	
	
	public Reference(long id) {
		this.id = id;
	}

	

	public Reference(String s) {
		this.id = Long.parseLong(s);
		
	}

	public long getId() {
		return id;
	}
	
	public T get() throws Exception{
		doGet();
		return super.get();
	}
	
	@SuppressWarnings("unchecked")
	private void doGet() throws Exception {
		if(super.get() == null && id > 0){
			value = (T)entity.get(id);
			value.setTransaction(getTransaction());
		}
	}
	
	public void set(T value){
		super.set(value);
		if(value == null){
			return;
		}
		id = value.getId();
	}


	@Override
	public String toString() {
		return String.valueOf(id);
	}

	

}
