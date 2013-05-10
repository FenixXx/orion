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
 * @author      Mathias Van Malderen
 * @version     1.0
 * @copyright   Mathias Van Malderen 09 July, 2012
 * @package     com.orion.utility
 **/

package com.orion.utility;

import java.util.HashMap;
import java.util.Map;

import com.orion.exception.ParserException;

public class BooleanParser {
    
    private static final Map<String, Boolean> stringToBoolean;
    
    static {
    	
        stringToBoolean = new HashMap<String, Boolean>();
        
        stringToBoolean.put("true",  Boolean.TRUE);
        stringToBoolean.put("false", Boolean.FALSE);
        stringToBoolean.put("yes",   Boolean.TRUE);
        stringToBoolean.put("no",    Boolean.FALSE);
        stringToBoolean.put("on",    Boolean.TRUE);
        stringToBoolean.put("off",   Boolean.FALSE);
        stringToBoolean.put("1",     Boolean.TRUE);
        stringToBoolean.put("0",     Boolean.FALSE);
        stringToBoolean.put(null,    Boolean.FALSE);
    
    }
    
    
    /**
     * Convert a <tt>String</tt> into a <tt>boolean</tt> primitive
     * 
     * @author Mathias Van Malderen
     * @param  str The <tt>String</tt> to be parsed
     * @throws ParserException If the input <tt>String</tt> can't be converted
     **/
    public static boolean parseBoolean(String str) throws ParserException {
        Boolean value = stringToBoolean.get(str.toLowerCase());
        if (value == null) throw new ParserException("Unable to parse string as a boolean: " + str);
        return value;
    }
    
    
    /**
     * Convert a <tt>String</tt> into a </tt>Boolean</tt> object
     * 
     * @author Mathias Van Malderen
     * @param  str The <tt>String</tt> to be parsed
     * @throws ParserException If the input <tt>String</tt> can't be converted
     **/
    public static Boolean valueOf(String str) throws ParserException {
        Boolean value = stringToBoolean.get(str.toLowerCase());
        if (value == null) throw new ParserException("Unable to parse string as a Boolean: " + str);
        return value;
    }
    
}