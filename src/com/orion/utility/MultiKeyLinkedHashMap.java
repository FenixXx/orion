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

import java.util.LinkedHashMap;
import java.util.Map;

public class MultiKeyLinkedHashMap<K1, K2, V> implements MultiKeyMap<K1, K2, V> {
    
    private Map<K1, V> map1;
    private Map<K2, V> map2;
    
    
    /**
     * Object constructor.
     * 
     * @author Daniele Pantaleone
     **/
    public MultiKeyLinkedHashMap() {
        this.map1 = new LinkedHashMap<K1, V>();
        this.map2 = new LinkedHashMap<K2, V>();
    }
    
    
    /**
     * Remove all of the mappings from this map.
     * The map will be empty after this call returns.
     * 
     * @author Daniele Pantaleone
     **/
    public void clear() {
        this.map1.clear();
        this.map2.clear();
    }
    
    
    /**
     * Return <tt>true</tt> if this map contains a mapping for the specified key.
     * 
     * @author Daniele Pantaleone
     * @param  key The key on which perform the search
     * @return <tt>true</tt> if the key is mapped, <tt>false</tt> otherwise
     **/
    public boolean containsKey(Object key) {
        if (key == null) return false;
        return (this.map1.containsKey(key) || this.map2.containsKey(key));
    }
    
    
    /**
     * Return <tt>true</tt> if this map contains a mapping for the specified keys.
     * 
     * @author Daniele Pantaleone
     * @param  key The key on which perform the search
     * @return <tt>true</tt> if the keys are mapped, <tt>false</tt> otherwise
     **/
    public boolean containsKey(Object key1, Object key2) {
        
        if (key1 == null || !this.map1.containsKey(key1))
            return false;
        
        if (key2 == null || !this.map2.containsKey(key2))
            return false;
        
        V val1 = this.map1.get(key1);
        V val2 = this.map2.get(key2);
        
        return (val1 == val2) ? true : false;
        
    }
    
    
    /**
     * Return <tt>true</tt> if this map maps one or more keys to the
     * specified value.
     * 
     * @author Daniele Pantaleone
     * @param  key The key on which perform the search
     * @return <tt>true</tt> if this map maps one or more keys to the 
     *         specified value, <tt>false</tt> otherwise
     **/
    public boolean containsValue(Object value) {
        return this.map1.containsValue(value);
    }
    
    
    /**
     * Return the value to which the specified key is mapped or 
     * <tt>null</tt> if this map contains no mapping for the key.
     *
     * @author Daniele Pantaleone
     * @param  key The key from which to get the value
     * @return The value to which the specified key is mapped or
     *         <tt>null</tt> if this map contains no mapping for the key
     **/
    public V get(Object key) {
        
        if (key == null)
            return null;
        
        if (this.map1.containsKey(key))
            return this.map1.get(key);
        
        if (this.map2.containsKey(key))
            return this.map2.get(key);
        
        return null;
        
    }
    
    
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
    public V get(Object key1, Object key2) {
        
        if (key1 == null || !this.map1.containsKey(key1))
            return null;
        
        if (key2 == null || !this.map2.containsKey(key2))
            return null;
        
        V val1 = this.map1.get(key1);
        V val2 = this.map2.get(key2);
        
        return (val1 == val2) ? val1 : null;

    }
    
    
    /**
     * Return a Map with all the values of the MultiKeyHashMap 
     * associated with the first key of the MultiKeyHashMap.
     * 
     * @author Daniele Pantaleone
     * @return 
     **/
    public Map<K1, V> getMap1() {
        return this.map1;
    }
    
    
    /**
     * Return a Map with all the values of the MultiKeyHashMap
     * associated with the second key of the MultiKeyHashMap.
     * 
     * @author Daniele Pantaleone
     * @return 
     **/
    public Map<K2, V> getMap2() {
        return this.map2;
    }
    
    
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
    public V put(K1 key1, K2 key2, V val) {
        
        if (key1 == null) return null;
        V val1 = this.map1.put(key1, val);
        
        if (key2 == null) return val1;
        this.map2.put(key2, val);
        
        return val1;
    
    }
    
    
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
    public V remove(Object key) {
        
        if (this.map1.containsKey(key)) {
            
            V val1 = this.map1.remove(key);
            
            if (this.map2.containsValue(val1)) {
                for (Map.Entry<K2, V> entry : this.map2.entrySet()) {
                    if (entry.getValue() == val1) {
                        this.map2.remove(entry.getKey());
                        break;
                    }    
                } 
            }
            
            return val1;
            
            
        } else if (this.map2.containsKey(key)) {
            
            V val2 = this.map2.remove(key);
            
            if (this.map2.containsValue(val2)) {
                for (Map.Entry<K1, V> entry : this.map1.entrySet()) {
                    if (entry.getValue() == val2) {
                        this.map1.remove(entry.getKey());
                        break;
                    }    
                } 
            }
            
            return val2;
            
        }
        
        return null;
        
    }
    
    
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
    public V remove(Object key1, Object key2) {
        
        if (this.containsKey(key1, key2)) {
            this.map2.remove(key2);
            return this.map1.remove(key1);
        }
        
        return null;
        
    }

}
