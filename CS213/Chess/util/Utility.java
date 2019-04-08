/**
 * Utility.java
 *
 * Copyright (c) 2019 Gemuele Aludino, Patrick Nogaj. 
 * All rights reserved.
 *
 * Rutgers University: School of Arts and Sciences
 * 01:198:213 Software Methodology, Spring 2019
 * Professor Seshadri Venugopal
 */
package util;

import java.util.*;
import java.util.function.*;

/**
 * This class is not to be instantiated.
 * Utility functions and static constants of a "miscellaneous" nature
 * will reside here.
 * 
 * @version Mar 3, 2019
 * @author gemuelealudino
 * @author patricknogaj
 */
public final class Utility {
	
	private Utility() { }
	
	/**
	 * Filter a List (evaluate each element for a condition)
	 * and return a new ArrayList
	 * 
	 * @param list	The list to traverse
	 * @param p		Functional interface with a test method
	 * @return		A new ArrayList; a filtered list
	 */
	public static <T> List<T> filterList(List<T> list, Predicate<T> p) {
		List<T> result = new ArrayList<T>();
		
		for (T t : list) {
			if (p.test(t)) {
				result.add(t);
			}
		}
		
		return result;
	}
	
	/**
	 * Map a List of type T and return a new ArrayList of type R
	 * 
	 * @param list	The list to traverse
	 * @param f		Functional interface with an apply method
	 * @return		A new ArrayList of type R, a filtered list type T
	 */
	public static <T, R> List<R> mapList(List<T> list, Function<T, R> f) {
		List<R> result = new ArrayList<R>();
		
		for (T t : list) {
			result.add(f.apply(t));
		}
		
		return result;
	}
	
	/**
	 * Consume a List of type T
	 * 
	 * @param list	The list to traverse
	 * @param cons	Functional interface with an accept method
	 */
	public static <T> void consumeList(List<T> list, Consumer<T> cons) {
		for (T t : list) {
			cons.accept(t);
		}
	}
}
