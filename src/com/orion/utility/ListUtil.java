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
 **/
package com.orion.utility;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.google.common.collect.Ordering;

public class ListUtil {
    
	public static <V> void insertInOrder(final List<V> list, final V value, final Comparator<? super V> comparator) {
		
        checkNotNull(list);
        checkNotNull(value);
        checkNotNull(comparator);
        
        final int I = Collections.binarySearch(list, value, comparator);
        final int insertPos = ((I < 0) ? (-I - 1) : (I + 1));
        
        list.add(insertPos, value);
        
    }
    
    
    public static <V extends Comparable<V>> void insertInOrder(final List<V> list, final V value) {
        insertInOrder(list, value, Ordering.natural());
    }
    
}