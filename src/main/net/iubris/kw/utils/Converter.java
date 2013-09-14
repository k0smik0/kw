/*******************************************************************************
 * Copyleft 2013 Massimiliano Leone - maximilianus@gmail.com .
 * 
 * Converter.java is part of 'kw'.
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
 * A simple Converter from String to any type requested
 * For now, just int/Integer, float/Float, double/Double 
 * are supported.
 * 
 * Simply, the conversion work by parseSOMETHING
 *  
 */

package net.iubris.kw.utils;

import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public enum Converter {
	
	INSTANCE;
		 
	public enum ConverterMethods {
		INSTANCE;
		
		public int parsePrimitiveInt(int i) {
			return i;
		}
		public int parsePrimitiveInt(Integer i) {
			return i.intValue();
		}
		public int parsePrimitiveInt(String par) {
			return Integer.parseInt(par);
		}
		public Integer parseInteger(String par) {
			return new Integer(par);
		}
		public float parsePrimitiveFloat(String par) {
			return Float.parseFloat(par);
		}
		public Float parseFloat(String par) {
			return new Float(par);
		}
		public double parsePrimitiveDouble(String par) {
			return Double.parseDouble(par);
		}
		public Double parseDouble(String par) {
			return new Double(par);
		}
		public boolean parsePrimitiveBoolean(String par) {
			return Boolean.parseBoolean(par);
		}
		public Boolean parseBoolean(String par) {
			return new Boolean(par);
		}
		public String parseString(String par) {
			return par;
		}
	}
	private Map<Class<?>,Method> typeConverterMap = new HashMap<Class<?>,Method>();
		
	private Converter() {
		
		try {
			typeConverterMap.put(int.class, 		ConverterMethods.INSTANCE.getClass().getDeclaredMethod("parsePrimitiveInt", String.class));
			typeConverterMap.put(Integer.class, 	ConverterMethods.INSTANCE.getClass().getDeclaredMethod("parseInteger", String.class));
			typeConverterMap.put(float.class, 		ConverterMethods.INSTANCE.getClass().getDeclaredMethod("parsePrimitiveFloat", String.class));
			typeConverterMap.put(Float.class, 		ConverterMethods.INSTANCE.getClass().getDeclaredMethod("parseFloat", String.class));
			typeConverterMap.put(double.class, 		ConverterMethods.INSTANCE.getClass().getDeclaredMethod("parsePrimitiveDouble", String.class));
			typeConverterMap.put(Double.class, 		ConverterMethods.INSTANCE.getClass().getDeclaredMethod("parseDouble", String.class));
			typeConverterMap.put(boolean.class, 	ConverterMethods.INSTANCE.getClass().getDeclaredMethod("parsePrimitiveBoolean", String.class));
			typeConverterMap.put(Boolean.class, 	ConverterMethods.INSTANCE.getClass().getDeclaredMethod("parseBoolean", String.class));
			typeConverterMap.put(String.class,	 	ConverterMethods.INSTANCE.getClass().getDeclaredMethod("parseString", String.class));
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}		
	}
	
	public Set<Class<?>> getTypesSupported() {
		//Map<Class<?>,Method> 
		return this.typeConverterMap.keySet();
	}
	
	
	/*
	 * @param string 	a string which contains some data (int, float, etc)
	 * @param type 		type (Type) which in we would convert string
	 * @return 			some object type which of is specified in input	
	 * 
	 */	
	public Object convert(String string, Type type) {		
		
		Method m = Converter.INSTANCE.typeConverterMap.get(type);
		try {
			return m.invoke(ConverterMethods.INSTANCE, string);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	
		return null;
	}	
		
}
