package cc.hao.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ReflectionUtil {

	public static interface FieldSetter{
		public void set(Field field) throws Exception;
	}
	
	public static boolean isSubClass(Class<?> clazz,Class<?> superClass){
		clazz = clazz.getSuperclass();
		if(clazz == null){
			return false;
		}
		while(!clazz.equals(Object.class)){
			if(clazz.equals(superClass)){
				return true;
			}
			clazz = clazz.getSuperclass();
		}
		return false;
	}
	
	public static Object get(Object obj,String name) throws Exception{
		String methodName = "get" + StringKit.upperFirst(name);
		Method method = getMethod(obj.getClass(), methodName);
		Object value = method == null ? getField(obj, name):method.invoke(obj);
		return value == null ?"":value;
	}
	
	public static Object getField(Object obj,String name) throws Exception{
		return doGetField(obj,obj.getClass(),name);
	}
	
	private static Object doGetField(Object obj, Class<?> clazz, String name) throws Exception{
		for(Field field:clazz.getDeclaredFields()){
			if(field.getName().equals(name)){
				field.setAccessible(true);
				return field.get(obj);
			}
		}
		Class<?> superclass = clazz.getSuperclass();
		if(superclass.getSimpleName().equals("Object")){
			return null;
		}
		return doGetField(obj, superclass, name);
	}

	public static Method getMethod(Class<?> clazz,String name){
		for(Method method:clazz.getMethods()){
			if(method.getParameterTypes().length == 0 && name.equals(method.getName())){
				return method;
			}
		}
		return null;
	}

	public static void setField(final Object obj,String name,final Object value) throws Exception{
		FieldSetter setter = new FieldSetter() {
			
			@Override
			public void set(Field field) throws Exception {
				field.set(obj, value);
			}
		};
		doSetField(obj.getClass(),name,setter);
	}
	
	public static void setField(Class<?> clazz,String name,final String value) throws Exception{
		FieldSetter setter = new FieldSetter() {
			@Override
			public void set(Field field) throws Exception {
				field.set(null, parseField(field.getType(),value));
			}
		};
		doSetField(clazz,name,setter);
	}
	
	private static Object parseField(Class<?> clazz, String value) {
		String type = clazz.getName();
		if(type.equals("int")){
			return Integer.parseInt(value);
		}
		else if(type.equals("long")){
			return Long.parseLong(type);
		}
		else if(type.equals("boolean")){
			return true;
		}
		else{
			return value;
		}
	}

	private static void doSetField(Class<?> clazz, String name, FieldSetter setter) throws Exception {
		for(Field field:clazz.getDeclaredFields()){
			if(field.getName().equals(name)){
				field.setAccessible(true);
				setter.set(field);
				break;
			}
		}
		Class<?> superClass = clazz.getSuperclass();
		if(superClass.getSimpleName().equals("Object")){
			return;
		}
		doSetField(superClass, name, setter);
	}
	
}
