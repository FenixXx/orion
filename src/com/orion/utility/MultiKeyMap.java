/**
 * This file is part of Orion source code
 * 
 * Copyright (C) 2012 [Gore]Clan - http://www.goreclan.net
 *
 * Orion is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Lesser Public
 * License as published by the Free Software Foundation; either
 * version 2 of the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Orion. If not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA
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
