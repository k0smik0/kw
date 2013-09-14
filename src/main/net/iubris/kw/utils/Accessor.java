/*******************************************************************************
 * Copyleft 2013 Massimiliano Leone - massimiliano.leone@iubris.net .
 * 
 * Accessor.java is part of 'kw'.
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
package net.iubris.kw.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

public enum Accessor {
	
	INSTANCE;
	public enum AccessorType { GET,SET; }

	
	public String getAccessorNameForField(Field field, AccessorType accessorType) {
		String n = field.getName();
		String f = n.substring(0, 1); 
		n = n.replaceFirst(f, f.toUpperCase() );

		if (accessorType.equals(AccessorType.SET)) {
			String methodName = "set"+n;
			return methodName;
		}
		if (accessorType.equals(AccessorType.GET)) {
			String methodName = "get"+n;
			return methodName;
		}
		return null;
	}
	/*
	public Method getSetterForField(String methodName, Object obj) {
		Method method = null;
				
		for (Class<?> clazz: primitiveClassTypes ) {
			try {
				return obj.getClass().getMethod(methodName,clazz);
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			}
		}
		
		try { //try with string 
			return obj.getClass().getMethod(methodName,String.class);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}

		return method;
	}*/
	
	public Method getSetterAccessorForField(String methodName, Field field, Object obj) {
		
		//try to search a setter with a type parameter equal to relative field
		// i.e. int myField -> we search for setMyField(int aInt)
		try {			
			return obj.getClass().getMethod(methodName, new Class[] { field.getType() });			
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {			
			// :/ - ok, we iterate on all setter method - we want almost ones!
			for (Method m: obj.getClass().getDeclaredMethods()) {
				if ( m.getName().equals(methodName)) {
					for (Type t: m.getParameterTypes()) {
						if (Converter.INSTANCE.getTypesSupported().contains(t)) return m;
					}					
				}
			}
			try {
				return obj.getClass().getMethod(methodName, String.class);
			} catch (SecurityException e1) {
				e1.printStackTrace();
			} catch (NoSuchMethodException e1) {
				e1.printStackTrace();
			}				
		}		
		return null;
	}
		
	/*
	public void invokeSetterForField(Method setter, Field field, String parameter) {
		for (Class<?> clazz: Converter.INSTANCE.getTypesSupported()) {
			if (field.getType().equals(clazz)) {
				
			} 
		}		
	}
	*/
}
