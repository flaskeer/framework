package cc.hao.core;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


public class Annotations {

	@Target(ElementType.TYPE)
	@Retention(RetentionPolicy.RUNTIME)
	public @interface Ignore{
		
	}
	
	@Retention(RetentionPolicy.RUNTIME)
	public @interface Role{
		String[] value();
	}
	
	@Target(ElementType.METHOD)
	@Retention(RetentionPolicy.RUNTIME)
	public @interface UpdateOptions{
		String[] toUpdate() default {};
		String[] allowEmpty() default {};
	}
	
}
