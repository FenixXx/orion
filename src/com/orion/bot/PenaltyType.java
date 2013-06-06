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