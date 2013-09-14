/*******************************************************************************
 * Copyleft 2013 Massimiliano Leone - massimiliano.leone@iubris.net .
 * 
 * KWConfigHandler.java is part of 'kw'.
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
