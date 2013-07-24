/**
 * Copyright (c) 2013 Daniele Pantaleone, Mathias Van Malderen
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 * 
 * @author      Mathias Van Malderen
 * @version     1.0
 * @copyright   Mathias Van Malderen, 23 July, 2013
 * @package     com.orion.utility
 **/
package com.orion.utility;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.google.common.collect.Ordering;

/**
 * The purpose of this class is to provide a convenient way to perform certain List operations.
 * 
 * @author Mathias Van Malderen
 */
public class ListUtil {
    
	/**
	 * Insert a value in an ordered List while preserving the ordering criteria by which that list is ordered.
	 * This method assumes that the specified list is ordered according to the order defined by the specified comparator.
	 * 
	 * @param list the list in which the specified value is to be inserted
	 * @param value	value to be inserted
	 * @param comparator the comparator by which the list is ordered
	 * @throws NullPointerException if any of the specified arguments is null
	 */
    public static <V> void insertInOrder(final List<V> list, final V value, final Comparator<? super V> comparator) {
        
        checkNotNull(list);
        checkNotNull(value);
        checkNotNull(comparator);
        
        final int I = Collections.binarySearch(list, value, comparator);
        final int insertPos = ((I < 0) ? (-I - 1) : (I + 1));
        
        list.add(insertPos, value);
        
    }
    
    
	/**
	 * Insert a value in an ordered List while preserving the ordering criteria by which that list is ordered.
	 * This method assumes that the list is ordered according to the natural order of the specified value.
	 * 
	 * @param list the list in which the specified value is to be inserted
	 * @param value	value to be inserted
	 * @throws NullPointerException if any of the specified arguments is null
	 */
    public static <V extends Comparable<V>> void insertInOrder(final List<V> list, final V value) {
        insertInOrder(list, value, Ordering.natural());
    }
    
}