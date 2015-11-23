package cc.hao.data;

import java.lang.reflect.Field;

import cc.hao.core.Entity;
import cc.hao.util.Wrapper;

public class FieldVisitor {
	
	public static interface Getable{
		public Object get(Field f) throws Exception;
	}
	
	public static interface Operator{
		public void operator(Field f) throws Exception;
	}
	
	public static boolean eachField(final Entity entity,Class<?> upper,final Getable getable) throws Exception{
		final Wrapper<Boolean> wrapper = new Wrapper<>(false);
		Operator operator = new Operator() {
			
			@Override
			public void operator(Field f) throws Exception {
				Object obj = getable.get(f);
				if(obj == null){
					return;
				}
				f.set(entity,obj);
			}
		};
		eachField(entity.getClass(), upper, operator);
		return wrapper.getValue();
	}

	public static void eachField(Class<?> clazz, Class<?> upper, Operator operator) throws Exception {
		while(!clazz.equals(upper)){
			for(Field f : clazz.getDeclaredFields()){
				f.setAccessible(true);
				operator.operator(f);
			}
			clazz = clazz.getSuperclass();
		}
	}

}
