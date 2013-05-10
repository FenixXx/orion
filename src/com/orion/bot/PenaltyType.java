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
 * @copyright   Daniele Pantaleone, 08 March, 2013
 * @package     com.orion.bot
 **/

package com.orion.bot;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum PenaltyType {
    
    WARNING("WARNING"),
    KICK("KICK"),
    BAN("BAN");
    
    private static final Map<String, PenaltyType> penaltyByName = new HashMap<String, PenaltyType>();
    private String handle;
    
    static {
        for (PenaltyType p : EnumSet.allOf(PenaltyType.class)) {
            PenaltyType.penaltyByName.put(p.handle, p);
        }
    }
    
    /**
     * Object constructor
     * 
     * @author Daniele Pantaleone
     * @param  handle The name of the <tt>Penalty</tt>
     **/
    private PenaltyType(String handle) {
        this.handle = handle;
    }
    
    
    /**
     * Return a <tt>PenaltyType</tt> object by matching the given name
     * 
     * @author Daniele Pantaleone
     * @param  name The name of the <tt>Penalty</tt>
     * @throws IndexOutOfBoundsException If the given <tt>Penalty</tt> name is not mapped in the Enum
     * @return The <tt>PenaltyType</tt> object matching the given name
     */
    public static PenaltyType getByName(String name) throws IndexOutOfBoundsException {
        
        if (!penaltyByName.containsKey(name.toUpperCase())) 
            throw new IndexOutOfBoundsException("Unable to match penalty name: " + name);
     
        return penaltyByName.get(name);
    }
    
}