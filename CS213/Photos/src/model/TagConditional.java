/**
 * TagConditional.java
 *
 * Copyright (c) 2019 Gemuele Aludino, Patrick Nogaj. 
 * All rights reserved.
 *
 * Rutgers University: School of Arts and Sciences
 * 01:198:213 Software Methodology, Spring 2019
 * Professor Seshadri Venugopal
 */

package model;

/**
 * @version Apr 16, 2019
 * @author gemuelealudino
 *
 */

public class TagConditional {
	
	private Tag tag1;
	private Tag tag2;

	private String conditional;

	/**
	 * Default constructor of a TagConditional object.
	 * @param tag1: Tag object
	 * @param tag2: Tag object
	 * @param conditional: String which contains AND/OR/NOT.
	 */
	public TagConditional(Tag tag1, Tag tag2, String conditional) {
		this.tag1 = tag1;
		this.tag2 = tag2;

		switch (conditional) {
		case "AND":
		case "OR":
		case "NOT":
		case "searchNOconditional":
			this.conditional = conditional;
			break;
		default:
			break;
		}
	}
	
	/**
	 * Checks to see if the current condition is "AND"
	 * @return true | false based on condition.
	 */
	public boolean isAnd() {
		return conditional.equals("AND");
	}
	
	/**
	 * Checks to see if the current condition is "OR"
	 * @return true | false based on condition.
	 */
	public boolean isOr() {
		return conditional.equals("OR");
	}
	
	/**
	 * Checks to see if the current condition is "NOT"
	 * @return true | false based on condition.
	 */
	public boolean isNot() {
		return conditional.equals("NOT");
	}
	
	/**
	 * Checks to see if there is no current condition.
	 * @return true | false based on condition.
	 */
	public boolean isSearchNoConditional() {
		return conditional.equals("searchNOconditional");
	}
	
	/**
	 * Gets the first Tag object.
	 * @return Tag
	 */
	public Tag getTag1() {
		return tag1;
	}
	
	/**
	 * Gets the second Tag object.
	 * @return Tag
	 */
	public Tag getTag2() {
		return tag2;
	}
	
	@Override
	public boolean equals(Object o) {
		if (o != null && o instanceof TagConditional) {
			TagConditional tc = (TagConditional)(o);
			
			return tag1.equals(tc.tag2) && conditional.equals(tc.conditional); 
		} else {
			return false;
		}
	}
	
	@Override
	public String toString() {
		return String.format("%s %s %s", tag1, conditional, tag2);
	}
}
