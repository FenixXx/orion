/**
 * Copyright (c) 2012 Daniele Pantaleone, Mathias Van Malderen
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
 * @author      Daniele Pantaleone
 * @version     1.0
 * @copyright   Daniele Pantaleone, 09 March, 2013
 * @package     com.orion.utility
 **/

package com.orion.utility;

import java.util.Map;

public interface MultiKeyMap<K1, K2, V> {
    
    
    /**
     * Remove all of the mappings from this map.
     * The map will be empty after this call returns.
     * 
     * @author Daniele Pantaleone
     **/
    public abstract void clear();
    
    
    /**
     * Return <tt>true</tt> if this map contains a mapping for the specified key.
     * 
     * @author Daniele Pantaleone
     * @param  key The key on which perform the search
     * @return <tt>true</tt> if the key is mapped, <tt>false</tt> otherwise
     **/
    public abstract boolean containsKey(Object key);
    
    
    /**
     * Return <tt>true</tt> if this map contains a mapping for the specified keys.
     * 
     * @author Daniele Pantaleone
     * @param  key The key on which perform the search
     * @return <tt>true</tt> if the keys are mapped, <tt>false</tt> otherwise
     **/
    public abstract boolean containsKey(Object key1, Object key2);
    
    
    /**
     * Return <tt>true</tt> if this map maps one or more keys to the
     * specified value.
     * 
     * @author Daniele Pantaleone
     * @param  key The key on which perform the search
     * @return <tt>true</tt> if this map maps one or more keys to the 
     *         specified value, <tt>false</tt> otherwise
     **/
    public abstract boolean containsValue(Object value);
    
    
    /**
     * Return the value to which the specified key is mapped or 
     * <tt>null</tt> if this map contains no mapping for the key.
     *
     * @author Daniele Pantaleone
     * @param  key The key from which to get the value
     * @return The value to which the specified key is mapped or
     *         <tt>null</tt> if this map contains no mapping for the key
     **/
    public abstract V get(Object key);
    
    
    /**
     * Return the value to which the specified keys are mapped or
     * <tt>null</tt> if this map contains no mapping for the keys.
     *
     * @author Daniele Pantaleone
     * @param  key1 The 1st key from which to get the value
     * @param  key2 The 2nd key from which to get the value
     * @return The value to which the specified keys are mapped or
     *         <tt>null</tt> if this map contains no mapping for the keys
     **/
    public abstract V get(Object key1, Object key2);
    
    
    /**
     * Return a Map with all the values of the MultiKeyHashMap 
     * associated with the first key of the MultiKeyHashMap.
     * 
     * @author Daniele Pantaleone
     * @return 
     **/
    public abstract Map<K1, V> getMap1();
    
    
    /**
     * Return a Map with all the values of the MultiKeyHashMap
     * associated with the second key of the MultiKeyHashMap.
     * 
     * @author Daniele Pantaleone
     * @return 
     **/
    public abstract Map<K2, V> getMap2();
    
    
    /**
     * Associates the specified value with the specified keys in this map.<br>
     * If the map previously contained a mapping for the keys, the old value is replaced.<br>
     * The 2nd key is optional and if <tt>null</tt> is given the mapping will be specified only
     * using the 1st key.
     *
     * @author Daniele Pantaleone
     * @param  key1 The 1st key with which the specified value is to be associated
     * @param  key2 The 2nd key with which the specified value is to be associated
     * @param  val The value to be associated with the specified keys
     * @return The previous value associated with <tt>key1, key2</tt>, or
     *         <tt>null</tt> if there was no mapping for <tt>key1, key2</tt>.
     *         (A <tt>null</tt> return can also indicate that the map
     *         previously associated <tt>null</tt> with <tt>key1, key2</tt>.)
     **/
    public abstract V put(K1 key1, K2 key2, V val);
    
    
    /**
     * Remove the mapping for the specified key from this map if present.
     *
     * @author Daniele Pantaleone
     * @param  key The key whose mapping is to be removed from the map
     * @return The previous value associated with <tt>key</tt>, or
     *         <tt>null</tt> if there was no mapping for <tt>key</tt>.
     *         (A <tt>null</tt> return can also indicate that the map
     *         previously associated <tt>null</tt> with <tt>key</tt>.)
     **/
    public abstract V remove(Object key);
    
    
    /**
     * Remove the mapping for the specified keys from this map if present.
     *
     * @author Daniele Pantaleone
     * @param  key1 The 1st key whose mapping is to be removed from the map
     * @param  key2 The 2nd key whose mapping is to be removed from the map
     * @return The previous value associated with <tt>key</tt>, or
     *         <tt>null</tt> if there was no mapping for <tt>key</tt>.
     *         (A <tt>null</tt> return can also indicate that the map
     *         previously associated <tt>null</tt> with <tt>key</tt>.)
     **/
    public abstract V remove(Object key1, Object key2);

}
