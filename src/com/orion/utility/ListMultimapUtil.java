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

import static com.google.common.base.Preconditions.*;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Ordering;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


// FIXME: Needs refactoring !!!
public class ListMultimapUtil
{
    public static <K, V> void sort(final ListMultimap<K, V> listMultimap, final K key, final Comparator<? super V> comparator)
    {
        checkNotNull(listMultimap);
        checkNotNull(key);
        checkNotNull(comparator);
        
        Collections.sort(listMultimap.get(key), comparator);
    }
    
    
    public static <K, V extends Comparable> void sort(final ListMultimap<K, V> listMultimap, final K key)
    {
        sort(listMultimap, key, Ordering.natural());
    }
    
    
    public static <K, V> void insertInOrder(final ListMultimap<K, V> listMultimap, final K key, final V value, final Comparator<? super V> comparator)
    {
        checkNotNull(listMultimap);
        checkNotNull(key);
        checkNotNull(value);
        checkNotNull(comparator);
        
        final List<V> list = listMultimap.get(key);
        final int I = Collections.binarySearch(list, value, comparator);
        final int insertPos = ((I < 0) ? (-I - 1) : (I + 1));
        
        list.add(insertPos, value);
    }
    
    
    public static <K, V extends Comparable> void insertInOrder(final ListMultimap<K, V> listMultimap, final K key, final V value)
    {
        insertInOrder(listMultimap, key, value, Ordering.natural());
    }
}