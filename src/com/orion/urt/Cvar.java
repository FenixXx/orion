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
 * @version     1.1
 * @copyright   Daniele Pantaleone, 21 July, 2013
 * @package     com.orion.urt
 **/

package com.orion.urt;

import com.orion.exception.ParserException;
import com.orion.utility.BooleanParser;

public class Cvar {
    
    private String name;
    private String value;
    
    
    /**
     * Object constructor
     * 
     * @author Daniele Pantaleone
     * @param  name The CVAR name
     * @param  value The CVAR value as a <tt>String</tt>
     * @throws NullPointerException If the CVAR name is <tt>null</tt>
     **/
    public Cvar(String name, String value) throws NullPointerException {
        
        if (name == null) {
            // If the CVAR name is NULL, throw an Exception since
            // we cannot track it's value and it will be useless
            throw new NullPointerException("CVAR name can't be NULL");
        }
        
        this.name = name.toLowerCase();
        this.value = value;
    }
    
    
    /**
     * Object constructor
     * 
     * @author Daniele Pantaleone
     * @param  name The CVAR name
     * @param  value The CVAR value as a <tt>Integer</tt>
     * @throws NullPointerException If the CVAR name is <tt>null</tt>
     **/
    public Cvar(String name, Integer value) throws NullPointerException {
        this(name, String.valueOf(value));
    }
    
    
    /**
     * Object constructor
     * 
     * @author Daniele Pantaleone
     * @param  name The CVAR name
     * @param  value The CVAR value as a <tt>Float</tt>
     * @throws NullPointerException If the CVAR name is <tt>null</tt>
     **/
    public Cvar(String name, Float value) throws NullPointerException {
        this(name, String.valueOf(value));
    }
    
    
    /**
     * Object constructor
     * 
     * @author Daniele Pantaleone
     * @param  name The CVAR name
     * @param  value The CVAR value as a <tt>Double</tt>
     * @throws NullPointerException If the CVAR name is <tt>null</tt>
     **/
    public Cvar(String name, Double value) throws NullPointerException {
        this(name, String.valueOf(value));
    }
    
    
    /**
     * Object constructor
     * 
     * @author Daniele Pantaleone
     * @param  name The CVAR name
     * @param  value The CVAR value as a <tt>Short</tt>
     * @throws NullPointerException If the CVAR name is <tt>null</tt>
     **/
    public Cvar(String name, Short value) throws NullPointerException {
        this(name, String.valueOf(value));
    }
    
    
    /**
     * Object constructor
     * 
     * @author Daniele Pantaleone
     * @param  name The CVAR name
     * @param  value The CVAR value as a <tt>Long</tt>
     * @throws NullPointerException If the CVAR name is <tt>null</tt>
     **/
    public Cvar(String name, Long value) throws NullPointerException {
        this(name, String.valueOf(value));
    }
    
    
    /**
     * Object constructor
     * 
     * @author Daniele Pantaleone
     * @param  name The CVAR name
     * @param  value The CVAR value as a <tt>Boolean</tt>
     * @throws NullPointerException If the CVAR name is <tt>null</tt>
     **/
    public Cvar(String name, Boolean value) throws NullPointerException {
        this(name, String.valueOf(value));
    }
    
    
    /**
     * Return the CVAR name
     * 
     * @author Daniele Pantaleone
     * @return The CVAR name
     **/
    public String getName() {
        return this.name;
    }
    
    
    /**
     * Return the CVAR value as a <tt>String</tt>
     * 
     * @author Daniele Pantaleone
     * @return The CVAR value as a <tt>String</tt>
     **/
    public String getString() {
        return this.value;
    }
    
    
    /**
     * Return the CVAR value as a <tt>Integer</tt>
     * 
     * @author Daniele Pantaleone
     * @throws NumberFormatException If the CVAR value couldn't be converted
     * @return The CVAR value as a <tt>Integer</tt> or <tt>null</tt> 
     *         if the conversion could not be performed
     **/
    public Integer getInt() throws NumberFormatException {
        
        if (this.value != null) {
            // Converting the CVAR string
            return Integer.valueOf(this.value);
        }
        
        return null;
        
    }
    
    
    /**
     * Return the CVAR value as a <tt>Float</tt>
     * 
     * @author Daniele Pantaleone
     * @throws NumberFormatException If the CVAR value couldn't be converted
     * @return The CVAR value as a <tt>Float</tt> or <tt>null</tt> 
     *         if the conversion could not be performed
     **/
    public Float getFloat() throws NumberFormatException {
        
        if (this.value != null)
            return Float.valueOf(this.value);
        
        return null;
        
    }
    
    
    /**
     * Return the CVAR value as a <tt>Double</tt>
     * 
     * @author Daniele Pantaleone
     * @throws NumberFormatException If the CVAR value couldn't be converted
     * @return The CVAR value as a <tt>Double</tt> or <tt>null</tt> 
     *         if the conversion could not be performed
     **/
    public Double getDouble() throws NumberFormatException {
        
        if (this.value != null) 
            return Double.valueOf(this.value);
        
        return null;
        
    }
    
    
    /**
     * Return the CVAR value as a <tt>Short</tt>
     * 
     * @author Daniele Pantaleone
     * @throws NumberFormatException If the CVAR value couldn't be converted
     * @return The CVAR value as a <tt>Short</tt> or <tt>null</tt> 
     *         if the conversion could not be performed
     **/
    public Short getShort() throws NumberFormatException {
        
        if (this.value != null) 
            return Short.valueOf(this.value);
        
        return null;
        
    }
    
    
    /**
     * Return the CVAR value as a <tt>Long</tt>
     * 
     * @author Daniele Pantaleone
     * @throws NumberFormatException If the CVAR value couldn't be converted
     * @return The CVAR value as a <tt>Long</tt> or <tt>null</tt> 
     *         if the conversion could not be performed
     **/
    public Long getLong() throws NumberFormatException {
        
        if (this.value != null)
            return Long.valueOf(this.value);
        
        return null;
        
    }
    
    
    /**
     * Return the CVAR value as a <tt>Boolean</tt>
     * 
     * @author Daniele Pantaleone
     * @throws ParserException If the CVAR value couldn't be converted
     * @return The CVAR value as a <tt>Boolean</tt>
     **/
    public Boolean getBoolean() throws ParserException {
        
        if (this.value != null)
            return BooleanParser.valueOf(this.value);

        return null;
        
    }
    
}