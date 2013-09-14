/*******************************************************************************
 * Copyleft 2013 Massimiliano Leone - maximilianus@gmail.com .
 * 
 * Execution.java is part of 'kw'.
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
