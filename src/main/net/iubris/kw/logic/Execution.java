/*******************************************************************************
 * Massimiliano Leone - maximilianus@gmail.com - 2009, GPL license
 ******************************************************************************/
package net.iubris.kw.logic;
/* 
 * Massimiliano Leone - maximilianus@gmail.com - 2009, GPL license
 * 
 * 	enum for Execution return type, in execute method, in any class
 *  which extends KWActionLogic
 *  
 *	OK: if all is ok :)
 *  
 *  INPUT: if we want go back at input page (o another page?)
 *    
 *  ERROR: if we want go in some error page 
 *  	(generic? global? which handles some exceptions? as we like)
 *  
 */
public enum Execution {
	OK, ERROR, INPUT	
}
