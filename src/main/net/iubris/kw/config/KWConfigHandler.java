/*
 * Massimiliano Leone - maximilianus@gmail.com - 2009, GPL license
 * 
 * A dummy Handler for sax parsing exception during xml validation by schema
 */
package net.iubris.kw.config;

import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;


public class KWConfigHandler extends DefaultHandler {

	//private boolean foundErrors = false;
	
	@Override
	public void error(SAXParseException e) {
		//foundErrors = true;
		System.out.println("\nline: "+e.getLineNumber() +",column: "+e.getColumnNumber() +"\n"+ e.getMessage()+"\n");
		//e.printStackTrace();
	}
	/*
	 public boolean success() {		 
         return foundErrors;
	 }*/	
}
