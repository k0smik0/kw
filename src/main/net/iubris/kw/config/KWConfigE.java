/*******************************************************************************
 * Massimiliano Leone - maximilianus@gmail.com - 2009, GPL license
 ******************************************************************************/
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
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public enum KWConfigE {
	
	INSTANCE;
	
	private Map<String,KWAction> map = new HashMap<String,KWAction>();
	private Document document = null;
	private String kwconfigPath = null;
	/*
	 * key(name) -> class 	-> nome classe con package
					ok 		-> pagina ok
					input	-> pagina input
					error	-> pagina error
	 * 
	 * 
	 */
	
	//private KWConfigE() {}
	
	public void setFileName(String s) {
		this.kwconfigPath = s;
	}
	
	/*
	public KWConfigE(String path) {
		init(path);
		parse();
	}	
	*/
		
	private boolean init() {
		
		File kw = new File(kwconfigPath);
		
		DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = null;
		try {
			builder = domFactory.newDocumentBuilder();
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
	
	public void parse() {
		
		init();
		
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
			kwAction.setOk(
					e.getElementsByTagName("ok").item(0).getTextContent()		
				);			
			kwAction.setInput(
					e.getElementsByTagName("input").item(0).getTextContent()		
				);
			kwAction.setError(
					e.getElementsByTagName("error").item(0).getTextContent()		
				);
			
			map.put(kwAction.getName(), kwAction);			
		}
	}
	
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
