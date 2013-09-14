/*******************************************************************************
 * Copyleft 2013 Massimiliano Leone - maximilianus@gmail.com .
 * 
 * KWActionMapper.java is part of 'kw'.
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
 * This is mapper for KWAction 
 * It's instantiated with a KWActionLogic concrete class 
 * 
 * And it provides a map method which populate 
 * @KW compliant fields found in KWActionLogic 
 * with request parameters (via names reflection) 
 * 
 */
package net.iubris.kw.mapper;


import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;
import net.iubris.kw.logic.KW;
import net.iubris.kw.logic.KWActionLogic;
import net.iubris.kw.utils.Accessor;
import net.iubris.kw.utils.Converter;
import net.iubris.kw.utils.Accessor.AccessorType;

public class KWActionMapper {

	private KWActionLogic kwActionLogic = null;
	
	/*
	 * @param classWithPath a full qualified class name with package name, 
	 * 						used to instantiate a KWActionLogic concrete class 
	 */
	@SuppressWarnings("unchecked")
	public KWActionMapper(String classWithPath) {
		Class<KWActionLogic> clazz = null;
		try {
			clazz = (Class<KWActionLogic>) Class.forName(classWithPath);
			this.kwActionLogic = clazz.newInstance();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}				
	}
	
	/*
	 * @param request a HttpServletRequest
	 * @return kwActionLogic given in constructor, with pojo(s)/field(s) populated with request parameters
	 */	
	public KWActionLogic map(HttpServletRequest request) throws IllegalArgumentException, IllegalAccessException, InstantiationException, ClassNotFoundException, InvocationTargetException {
				
		for (Field field: this.kwActionLogic.getClass().getDeclaredFields()) {
			
			String par = request.getParameter( field.getName() );

			if (par != null) { //directly
				setValueIntoField(par, field, this.kwActionLogic);
			}
			else { // we hope to have a field/pojo-kind which can map request
				KW annotation = field.getAnnotation(KW.class);
				if (annotation!=null && annotation.inject()) { // found a compliant KW pojo to populate
					setRequestParametersIntoKWAnnotatedPojoAsField(request, field);				
				}
			}	
		}		
		return this.kwActionLogic;		
	}
	
	/*
	 * Here we assign every request parameter in per-Field, using set accessors 
	 * 
	 * @param parameter a request String parameter from html form or similar
	 * @param field		a Field which map parameter, typized as developper wants (int,String,float...) 
	 */
	private void setValueIntoField(String parameter, Field field, Object target) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		String setterMethodName = Accessor.INSTANCE.getAccessorNameForField(field,AccessorType.SET);
		Method setter = null;
 
		// try an enhanced set, invoking a typized setter accessor			
		setter = Accessor.INSTANCE.getSetterAccessorForField(setterMethodName, field, target);
		
		setter.invoke(target, Converter.INSTANCE.convert(parameter, field.getType()) );
	}
	
	/*
	private void setValueIntoFieldOLD(String parameter, Field field, Object target) {
		String setterMethodName = Accessor.INSTANCE.getAccessorNameForField(field,AccessorType.SET);
		Method setter = null;
		try { // try an enhanced set, invoking a typized setter accessor			
			setter = Accessor.INSTANCE.getSetterAccessorForField(setterMethodName, field, target); 

			setter.invoke(target, Converter.INSTANCE.convert(parameter, field.getType()) );
		} catch (IllegalArgumentException e) {					
			try { // try for method which accept string, honoring parameter naturally
				setter = target.getClass().getDeclaredMethod(setterMethodName,String.class);
				setter.invoke(target, parameter);
			} catch (SecurityException e1) {
				e1.printStackTrace();
			} catch (NoSuchMethodException e1) {
				e1.printStackTrace();
			} catch (IllegalArgumentException e1) {
				e.printStackTrace();
			} catch (IllegalAccessException e1) {
				e.printStackTrace();
			} catch (InvocationTargetException e1) {
				e.printStackTrace();
			}
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();		
		}
	}
	*/
	private void setValueIntoField(Object value, Field field, Object target) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		String setterMethodName = Accessor.INSTANCE.getAccessorNameForField(field,AccessorType.SET);
		Method setter = null;

		setter = Accessor.INSTANCE.getSetterAccessorForField(setterMethodName, field, target);
		setter.invoke(target, value);	
	}

	/*
	 * Here, we have a complex field than java native as "int a" or "String s" 
	 * with relative set accessors. There is a custom class which boxes java 
	 * native field (always with set accessors): and then we populate these
	 * pojo/field's fields with request parameters.      
	 * 
	 * @param request	HttpServletRequest request which from we get parameters 
	 * @param pojo		a KW annotated pojo where we store request parameters
	 */
	private void setRequestParametersIntoKWAnnotatedPojoAsField(HttpServletRequest request, Field pojo) throws IllegalArgumentException, IllegalAccessException, InstantiationException, ClassNotFoundException, InvocationTargetException {		
		populatePojo(pojo, pojo.getType(), request);	
	}

	/*
	 * First:
	 *  an object with type specified in "Class<T> t" has been 
	 *  instanced, using object fields name to retrieve value
	 *  (yeah, we love reflection ;D) 
	 * 
	 * Second: 
	 *  object will be stored in pojo/field given   
	 *  
	 * @param fieldPojo pojo given as Field type
	 * @param t			generic class type (pojo's one)
	 * @param request 	a HttpServletRequest
	 *  
	 */
	private <T> void populatePojo(Field pojoAsKWActionField, Class<T> t, HttpServletRequest request) throws IllegalArgumentException, IllegalAccessException, InstantiationException, ClassNotFoundException, InvocationTargetException  {		
				
		T pojo = t.newInstance();
				
		for (Field pf: pojo.getClass().getDeclaredFields()) {			
			String par = request.getParameter( pf.getName() );
			
			if (par!=null) {
				setValueIntoField(par, pf, pojo);
			}
			
			/*
			// a basically conversion for some type (string,int,float,double)
			Object toSet = Converter.INSTANCE.convert(att,pf.getType());
			
			pf.setAccessible(true);
			pf.set(pojo, toSet );
			*/
		}
				
		setValueIntoField(pojo, pojoAsKWActionField, this.kwActionLogic);
		/*pojoAsKWActionField.setAccessible(true);
		pojoAsKWActionField.set(this.kwActionLogic, pojo);
		pojoAsKWActionField.setAccessible(false);*/
	}//populatePojo		
	
}
