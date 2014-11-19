/**
 * 
 */
package com.uvsq.Jung;

/**
 * @author Bruce GONG
 *
 */
public class TupleRDF {
	private Object Sujet;
	private Object Predicat;
	private Object Literal;
	private boolean hasSubNode;
	public TupleRDF(Object sujet, Object predicat, Object literal) {
		super();
		Sujet = sujet;
		Predicat = predicat;
		Literal = literal;
		this.hasSubNode = false;
	}
	/**
	 * @return the sujet
	 */
	public Object getSujet() {
		return Sujet;
	}
	/**
	 * @param sujet the sujet to set
	 */
	public void setSujet(Object sujet) {
		Sujet = sujet;
	}
	/**
	 * @return the predicat
	 */
	public Object getPredicat() {
		return Predicat;
	}
	/**
	 * @param predicat the predicat to set
	 */
	public void setPredicat(Object predicat) {
		Predicat = predicat;
	}
	/**
	 * @return the literal
	 */
	public Object getLiteral() {
		return Literal;
	}
	/**
	 * @param literal the literal to set
	 */
	public void setLiteral(Object literal) {
		Literal = literal;
	}
	/**
	 * @return the hasSubNode
	 */
	public boolean isHasSubNode() {
		return hasSubNode;
	}
	/**
	 * @param hasSubNode the hasSubNode to set
	 */
	public void setHasSubNode(boolean hasSubNode) {
		this.hasSubNode = hasSubNode;
	}
	
	
}
