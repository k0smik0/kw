/*
 * Massimiliano Leone - maximilianus@gmail.com - 2009, GPL license
 * 
 * This is KWAction, a simple class which map 
 * <action>...</action> node, in kw.xml
 * 
 *  name: name to use in action form (in html), 
 *  	or in any action chain (action chain is still under develop)
 *  
 *  classWithPath: a full qualified class with package path, as java.lang.String
 *  	KW framework use this to instance a class via reflection
 *  
 *  ok: a full qualified jsp page for "OK" execution, as /some/path/okpage.jsp
 *  
 *  input: a full qualified jsp page for "INPUT" execution, as /some/path/inputpage.jsp
 *   
 *  error: a full qualified jsp page for "ERROR" execution, as /some/path/errorpage.jsp
 *  
 *  for OK,INPUT,ERROR see @Execution 
 *  
 */

package net.iubris.kw.logic;

public class KWAction {
	
	private String name;
	private String classWithPath;
	private String ok;
	private String input;
	private String error;
	
	public KWAction() {}
	
	public KWAction(String name, String classWithPath) {
		this.name = name;
		this.classWithPath = classWithPath;
	}
	
	public KWAction(String name, String classWithPath, String ok) {
		this.name = name;
		this.classWithPath = classWithPath;
		this.ok = ok;
	}

	public KWAction(String name, String classWithPath,String ok, String input) {
		this.name = name;
		this.classWithPath = classWithPath;
		this.ok = ok;
		this.input = input;
	}
	
	public KWAction(String name, String classWithPath,String ok, String input, String error) {
		this.name = name;
		this.classWithPath = classWithPath;
		this.ok = ok;
		this.input = input;
		this.error = error;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getClassWithPath() {
		return classWithPath;
	}
	public void setClassWithPath(String classWithPath) {
		this.classWithPath = classWithPath;
	}
	public String getOk() {
		return ok;
	}
	public void setOk(String ok) {
		this.ok = ok;
	}
	public String getInput() {
		return input;
	}
	public void setInput(String input) {
		this.input = input;
	}
	public String getError() {
		return error;
	}
	public void setError(String error) {
		this.error = error;
	}
	
	public String toString() {
		String out = "";
		out += "name: "+name;
		out += "\n";
		out += "class: "+classWithPath;
		out += "\n";
		out += "ok: "+ok;
		out += "\n";
		out += "input: "+input;
		out += "\n";
		out += "error: "+error;
		
		return out;
	}
	

}
