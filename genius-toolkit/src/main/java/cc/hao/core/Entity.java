package cc.hao.core;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;

import cc.hao.data.DataUtil;
import cc.hao.data.FieldVisitor;
import cc.hao.data.FieldVisitor.Operator;
import cc.hao.field.Child;
import cc.hao.field.Children;
import cc.hao.field.Reference;
import cc.hao.field.References;
import cc.hao.server.Request;
import cc.hao.util.CfgUtil;
import cc.hao.util.ReflectionUtil;
import cc.hao.util.StringKit;
import cc.hao.web.Controller;
import cc.hao.web.Transaction;
import cc.hao.web.View;

public abstract class Entity {
	
	private long id;
	
	private long created = System.currentTimeMillis();
	
	private Entity parent;
	
	private String view;
	
	private Request request;
	
	private Transaction transaction;
	
	private Map<String, String> cfg;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
	
	public long created(){
		return created;
	}
	
	public Entity getParent() {
		return parent;
	}
	
	public void setParent(Entity parent) {
		this.parent = parent;
	}
	
	public void setRequest(Request request) {
		this.request = request;
	}
	
	public Request getRequest() {
		return request;
	}
	
	public String cfg(String name,String ... args) throws Exception{
		if(cfg == null){
			String path = "cfg/" + getClass().getSimpleName();
			cfg = CfgUtil.load(path);
		}
		return String.format(cfg.get(name), (Object[])args);
	}
	
	public void clear(){
		transaction = null;
		request = null;
		cfg = null;
	}
	
	public boolean isRole(String role) throws Exception{
		Object session = request.session().get("role");
		return session == null ? false : session.equals(role);
	}

	public void setTransaction(Transaction transaction) {
		this.transaction = transaction;
		if(id != 0){
			transaction.cache(this);
		}
	}

	public Entity getDatabase(){
		return get(1);
	}
	
	public Entity get(long id) {
		
		return transaction.get(id);
	}

	public void setView(String view) {
		this.view = view;
	}
	
	public <E extends Entity> E get(Class<?> clazz){
		@SuppressWarnings("unchecked")
		E e = (E) DataUtil.get(clazz);
		e.setTransaction(transaction);
		return e;
	}
	
	public String save() throws Exception{
		doSave();
		return action("list");
	}
	
	public String delete() throws Exception{
		doDelete();
		return action("list");
	}

	

	public void doDelete() throws Exception {
		FieldVisitor.eachField(getClass(), Entity.class,new DeleteOp());
		doList().remove(id);
		
	}
	
	private class DeleteOp implements Operator{

		@Override
		public void operator(Field f) throws Exception {
			if(f.getType().equals(Child.class)){
				deleteChild((Child<?>)f.get(Entity.this));
			}else if(f.getType().equals(Children.class)){
//				for(Reference<?> child : (Children<?>)f.get(Entity.this)){
//					deleteChild(child);
//				}
			}
		}

		private void deleteChild(Reference<?> child) throws Exception {
			if(child != null){
				child.get().doDelete();
			}
		}
		
	}
	
	@Override
	public String toString() {
		return String.valueOf(id);
	}

	private String action(String action) throws Exception {
		if(action.startsWith("/")){
			Request req = new Request(request.head(), action.substring(1));
			transaction.commit();
			return new Controller(req).doRun();
		}else{
			view = action;
			Method method = this.getClass().getMethod(action);
			return (String) method.invoke(this);
		}
	}

	private void doSave() throws Exception {
		doList().add(this);
	}

	@SuppressWarnings("unchecked")
	private References<Entity> doList() throws Exception {
		String name = this.getClass().getSimpleName();
		name = StringKit.lowerFirst(name) + "List";
		return (References<Entity>) ReflectionUtil.getField(parent, name);
	}
	
	public String toView() throws Exception{
		return toView(this);
	}

	public String toView(Object model) throws Exception {
		return toView(model,view);
	}

	public String toView(String view) throws Exception{
		return toView(this, view);
	}
	
	public String toView(Object model, String view) throws Exception {
		if(view.charAt(0) != '/'){
			view = this.getClass().getSimpleName() + '/' + view;
		}
		return View.render(model, view, request);
	}

	public Transaction getTransaction() {
		return transaction;
	}
	

}
