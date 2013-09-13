/*
 * Massimiliano Leone - maximilianus@gmail.com - 2009, GPL license
 * 
 * This is annotation for any class which extend KWLogicAction
 * 
 * Every instance member (in specified classes as above) must have
 * @KW annotation in order to be used as pojo in some request or similar
 * 
 *     When a field with @KW annotation is found, framework uses it
 *     via reflection, using its fields request parameters (by name)
 *     
 *     Again, if a target (at least one) is specified, same field 
 * 		will be used as request bean, in one (or more) target jsp page 
 * 
 */
package net.iubris.kw.logic;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
@Documented
public @interface KW {	
	
	/*
	 * When a field with @KW inject=true annotation is found, framework uses it
	 * via reflection, using its fields request parameters (by name)
	 */		
	public boolean inject() default false;	
	
	/*
	 * if a target (at least one) is specified, same field 
	 * will be used as request bean, in one (or more) target jsp page 
	 */
	public Execution[] target() default {};	
}
