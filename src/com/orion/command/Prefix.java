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
