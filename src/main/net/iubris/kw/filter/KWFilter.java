/*******************************************************************************
 * Copyleft 2013 Massimiliano Leone - maximilianus@gmail.com .
 * 
 * KWFilter.java is part of 'kw'.
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
 *  Massimiliano Leone - maximilianus@gmail.com - 2009, GPL license
 * 
 *  A very big factotum filter ;D
 * 
 *  This filter is responsible for any uri or another resource handling
 *  
 *  It analyze uri request, and try to map request parameters 
 *  in a KWActionLogic, as specified in action form, using KWActionLogic
 *  @KW compliant fields as valid pojos for request parameters value
 *  
 *  If uri request is not a KWAction class (so it is a static/dynamic resource), 
 *  filter call a RequestDispatcher for this resource, and that's all :)
 *  
 *  Obviously, if in KWActionLogic there are some fields specified as 
 *  @KW target (@see KWAction, Execution), filter set these fields in request
 *  
 *  
 *  
 *  
 */
package net.iubris.kw.filter;


import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.iubris.kw.config.KWConfig;
import net.iubris.kw.logic.Execution;
import net.iubris.kw.logic.KW;
import net.iubris.kw.logic.KWAction;
import net.iubris.kw.logic.KWActionLogic;
import net.iubris.kw.mapper.KWActionMapper;
import net.iubris.kw.utils.Accessor;
import net.iubris.kw.utils.Accessor.AccessorType;

/**
 * Servlet Filter implementation class KWFilter
 */
public class KWFilter implements Filter {

	private FilterConfig filterConfig;
	private Map<String, KWAction> kwActionsMap;
	private KWConfig kwConfig;
	
    /**
     * Default constructor.  
     */
    public KWFilter() {}
    
    /**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig filterConfig) throws ServletException {
		this.filterConfig = filterConfig;

		String path = filterConfig.getServletContext().getRealPath("WEB-INF"+File.separator+"kw.xml");
		this.kwConfig = new KWConfig(path);		
		this.kwActionsMap = this.kwConfig.getActionsMap();		
				
		// with singleton enum based 
		//KWConfigE.INSTANCE.setFileName(path);
		//KWConfigE.INSTANCE.parse();
	}
    
	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */	
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
		
		HttpServletRequest  request  = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse)res;
		ServletContext cx = filterConfig.getServletContext();	
		String requestUri = this.getRequestURI(request);
		
		System.out.println("IP: "+request.getRemoteHost());
		
		//String actionUri = requestUri.replaceFirst("/", "");
		
		int pos = requestUri.split("/").length - 1;
		String[] uriSplitted = requestUri.split("/");
		String actionUri = uriSplitted[pos];		
		KWAction kwAction = kwActionsMap.get( actionUri );
		if (kwAction != null) {
//			System.out.println("KWFilter: 105 -> Acting! -> URI: "+actionUri);		
			act(kwAction,request,response,cx);
		}
		else {			
//			System.out.println("KWFilter: 105 -> Not acting -> URI: "+requestUri);			
			RequestDispatcher rd = cx.getRequestDispatcher(requestUri);
			rd.forward(request, response);
		}
	}

	/* This is the heart of the sun ;D
	 * 
	 * Here, a KWActionLogic is retrieved and used for a KWActionMapper,
	 * which try to map request parameter in a KWActionLogic subclass,
	 * honoring "action" declaration in html form. These values will be 
	 * stored into @KW compliant fields (instance member), and then 
	 * KWActionLogic will be returned. 
	 * Then, honoring the Command Pattern, the KWActionLogic execute method
	 * will be invoked, we'll get a OK or INPUT or ERROR Execution return.
	 * Finally, we obtain desired target, honoring above Execution and 
	 * we will build a dispatcher with this, and here we go ;D 
	 * 
	 * @param kwAction	kwAction which from we get class name to instance
	 * @param request	a HttpServletRequest request from doFilter, used in dispatching
	 * @param response	a HttpServletResponse response from doFilter, used in dispatching
	 * @param cx		a ServletContext from doFilter, used in dispatching
	 */
	private void act (KWAction kwAction, HttpServletRequest request, HttpServletResponse response, ServletContext cx) throws ServletException, IOException {
		
		// we want a mapper with this kwAction
		KWActionMapper mapper = new KWActionMapper( kwAction.getClassWithPath() );
		
		// we have a kwActionLogic with pojo(s) populated with request parameters 
		KWActionLogic kwActionLogic = null;
		try {
			kwActionLogic = mapper.map(request);
		} catch (IllegalArgumentException e) {
			System.out.println(e);
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			System.out.println(e);
			e.printStackTrace();
		} catch (InstantiationException e) {
			System.out.println(e);
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			System.out.println(e);
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			System.out.println(e);
			e.printStackTrace();
		}
		
		// we discover our destiny ;D
		// envelope A B or C ? (really OK INPUT or ERROR)
		// the queen wins, the jacks lose, ladies and gentlemen		
		Execution execution = kwActionLogic.execute();
		 
		// we introspect into kwActionLogic and retrieve 
		// @KW compliant fields.
		// Then, if any targets annotation are found, 
		// we build bean(s) and we set in request 
		for (Field pojo: kwActionLogic.getClass().getDeclaredFields()) {
			KW annotation = pojo.getAnnotation(KW.class);
			if (annotation != null && annotation.target().length>0) { // found the pojo
				List<Execution> targets = Arrays.asList(annotation.target());
				if (targets.contains(execution)) {
					pojo.setAccessible(true);
					request.setAttribute( 
							pojo.getName(),
							buildBean(kwActionLogic,pojo,pojo.getType())
						);
				}			 
			}
		}
		
		//finally, we obtain desired target and 
		//we get a dispatcher for this 
		RequestDispatcher rd = null;
		switch (execution) {
		case OK:
			rd = cx.getRequestDispatcher( kwAction.getOk() );
			break;
		case INPUT:
			rd = cx.getRequestDispatcher( kwAction.getInput() );			
			break;
		case ERROR:
			rd = cx.getRequestDispatcher( kwAction.getError() );
			break;		
		}
		
		//and then we go ;)
		rd.forward(request, response);
	}//act
	
	/*
	 * from struts2
	 * 
	 * a better getRequestURI than internal ones
	 */	 
	private String getRequestURI(HttpServletRequest request) {
        String servletPath = request.getServletPath();        
        String requestUri = request.getRequestURI();
        // Detecting other characters that the servlet container cut off (like anything after ';')
        if (requestUri != null && servletPath != null && !requestUri.endsWith(servletPath)) {
            int pos = requestUri.indexOf(servletPath);
            if (pos > -1) {
                servletPath = requestUri.substring(requestUri.indexOf(servletPath));
            }
        }        
        if (null != servletPath && !"".equals(servletPath)) {         
            return servletPath;
        }        
        int startIndex = request.getContextPath().equals("") ? 0 : request.getContextPath().length();
        int endIndex = request.getPathInfo() == null ? requestUri.length() : requestUri.lastIndexOf(request.getPathInfo());
        if (startIndex > endIndex) { // this should not happen
            endIndex = startIndex;
        }
        String ru = requestUri.substring(startIndex, endIndex);       
        return ru;
    }
	
	/*
	 * Here we instance a bean which type is provided using generics.
	 * 
	 * Then, value in (type Field) pojo (from KWActionLogic) 
   	 * will be extracted using get accessor. and stored in bean.
   	 * 
   	 * Finally this bean will be returned 
	 * 
	 * @param kwActionLogic a kwActionLogic which from pojo is extracted
	 * @param pojo 			kwActionLogic @KW compliant pojo, given as Field type
	 * @param t class 		which type bean will be instantied 
	 */	
	@SuppressWarnings("unchecked")
	private <T> T buildBean(KWActionLogic kwActionLogic, Field pojo, Class<T> t) {
		T bean = null;
		// using field directly
		/*try {
			bean = (T) pojo.get(kwActionLogic);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}*/		
		String getterName = Accessor.INSTANCE.getAccessorNameForField(pojo, AccessorType.GET);
		try {
			Method getter = kwActionLogic.getClass().getDeclaredMethod(getterName);
			bean = (T) getter.invoke(kwActionLogic);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}	
		
		return bean;		
	}	
}
