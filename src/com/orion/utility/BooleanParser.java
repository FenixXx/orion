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