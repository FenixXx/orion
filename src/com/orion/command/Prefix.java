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
 * @copyright   Daniele Pantaleone, 15 February, 2013
 * @package     com.orion.command
 **/

package com.orion.command;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum Prefix {
    
    NORMAL ('!'),
    LOUD   ('@'),
    BIG    ('&');
    
    private static final Map<Character,Prefix> prefixByChar = new HashMap<Character,Prefix>();
    public final Character name;
    
    
    static {  
        for (Prefix p : EnumSet.allOf(Prefix.class)) {  
            Prefix.prefixByChar.put(p.name, p);  
        }  
    }  
    
    
    /**
     * Object constructor
     * 
     * @author Daniele Pantaleone
     * @param  identifier The <tt>Prefix</tt> identifier
     **/
    private Prefix(Character identifier) {
        this.name = identifier;
    }
    
    
    /**
     * Return the Prefix associated to the given identifier
     * 
     * @author Daniele Pantaleone
     * @param  name The <tt>Prefix</tt> identifier
     * @throws IndexOutOfBoundsException If the given <tt>Prefix</tt> is not mapped
     * @return The <tt>Prefix</tt> associated to the given identifier
     **/
    public static Prefix getByChar(Character name) throws IndexOutOfBoundsException {
        
        if (!prefixByChar.containsKey(name)) 
            throw new IndexOutOfBoundsException("Unable to match prefix identifier: " + name);
        
        return prefixByChar.get(name);
        
    }
 
}
