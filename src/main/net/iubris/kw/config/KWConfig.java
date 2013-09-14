/*******************************************************************************
 * Copyleft 2013 Massimiliano Leone - maximilianus@gmail.com .
 * 
 * KWConfig.java is part of 'kw'.
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
 * This is a mapper from xml to oop world.
 * It map every <logic> stanza in kw.xml configuration file
 * and populate a map: key are same <name> values
 * 
 *  here a sample:
 *  <logic>
		<name>VoloLogic</name>
		<class>net.iubris.flights.VoloLogic</class>
		<ok>/ok.jsp</ok>
		<input>/input_page.jsp</input>
		<error>/error_page.jsp</error>		
	</logic>	
 * 
 * 
 */
package net.iubris.kw.config;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import net.iubris.kw.logic.KWAction;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class KWConfig {
	
	private Map<String,KWAction> map = new HashMap<String,KWAction>();
	private Document document = null;
	
	
	public KWConfig(String path) {
		init(path);
		parse();
	}	
		
	private boolean init(String path) {		
		File kw = new File(path);

		//File schema = new File(path.replace("xml", "xsd"));		
		String dirPath = "classes"+File.separator+this.getClass().getPackage().getName().replace(".", File.separator);
		File schema = new File(path.replaceFirst("kw.xml", dirPath+File.separator+"kw.xsd"));
	
		DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = null;		
		
		try {
			if (schema.exists()) {
				domFactory.setNamespaceAware(true);
				domFactory.setValidating(true);
				domFactory.setAttribute(
	                    "http://java.sun.com/xml/jaxp/properties/schemaLanguage",
	                    "http://www.w3.org/2001/XMLSchema");
	            domFactory.setAttribute(
	                    "http://java.sun.com/xml/jaxp/properties/schemaSource",
	                    schema);
	            builder = domFactory.newDocumentBuilder();
	            builder.setErrorHandler(new KWConfigHandler());
			}
			else {
				builder = domFactory.newDocumentBuilder();
			}						
			this.document = builder.parse( kw );			
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return true;
	}
	
	private void parse() {
		NodeList nl = this.document.getElementsByTagName("logic");		
		for (int i=0; i<nl.getLength();i++){
			Element e = (Element) nl.item(i);		
			KWAction kwAction = new KWAction();

			kwAction.setName(
					e.getElementsByTagName("name").item(0).getTextContent()
				);			
			kwAction.setClassWithPath(
					e.getElementsByTagName("class").item(0).getTextContent()
				);			
			
			Node okNode = e.getElementsByTagName("ok").item(0);					
			if (okNode!=null) {				 
				kwAction.setOk( okNode.getTextContent() );
			}
			
			Node inputNode = e.getElementsByTagName("input").item(0);					
			if (inputNode!=null) {				 
				kwAction.setInput( inputNode.getTextContent() );
			}
			
			Node errorNode = e.getElementsByTagName("error").item(0);					
			if (errorNode!=null) {							 
				kwAction.setError( errorNode.getTextContent() );
			}
						
			
			map.put(kwAction.getName(), kwAction);			
		}
	}
	
	/*
	 * @return map a Map<String,KWAction>
	 */
	public Map<String,KWAction> getActionsMap() {
		return this.map;
	}
	
	public String toString() {
		String a = "";
		for (KWAction kwa: this.map.values()) {
			a += kwa.toString();
			a += "\n\n";
		}
		return a;
	}
}
