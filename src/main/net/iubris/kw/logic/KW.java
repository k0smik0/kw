/*******************************************************************************
 * Copyleft 2013 Massimiliano Leone - maximilianus@gmail.com .
 * 
 * KW.java is part of 'kw'.
 * 
 * 'kw' is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 * 
 * 'kw' is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with 'kw'; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301 USA
 ******************************************************************************/
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
