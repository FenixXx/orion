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
 * @copyright   Daniele Pantaleone, 05 October, 2012
 * @package     com.orion.domain
 **/

package com.orion.domain;

public class Group {
    
    private int id;
    private String name;
    private String keyword;
    private int level;
  
    
    /**
     * Object constructor
     * 
     * @author Daniele Pantaleone
     * @param  id The group id
     * @param  name The group name
     * @param  keyword The group keyword
     * @param  level The group level
     **/
    public Group(int id, String name, String keyword, int level) {
        this.setId(id);
        this.setName(name);
        this.setKeyword(keyword);
        this.setLevel(level);
    }

    
    /**
     * Return the group id
     * 
     * @author Daniele Pantaleone
     * @return The group id
     **/
    public int getId() {
        return this.id;
    }
    
    
    /**
     * Return the group name
     * 
     * @author Daniele Pantaleone
     * @return The group name
     **/
    public String getName() {
        return this.name;
    }
    
    
    /**
     * Return the group keyword
     * 
     * @author Daniele Pantaleone
     * @return The group keyword
     **/
    public String getKeyword() {
        return this.keyword;
    }
    
    
    /**
     * Return the group level
     * 
     * @author Daniele Pantaleone
     * @return The group level
     **/
    public int getLevel() {
        return this.level;
    }
    
    
    /**
     * Set the group id
     * 
     * @author Daniele Pantaleone
     * @param  id The group id
     **/
    public void setId(int id) {
        this.id = id;
    }

    
    /**
     * Set the group name
     * 
     * @author Daniele Pantaleone
     * @param  name The group name
     **/
    public void setName(String name) {
        this.name = name;
    }

    
    /**
     * Set the group keyword
     * 
     * @author Daniele Pantaleone
     * @param  keyword The group keyword
     **/
    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    
    /**
     * Set the group level
     * 
     * @author Daniele Pantaleone
     * @param  level The group level
     **/
    public void setLevel(int level) {
        this.level = level;
    }
    
    
    /**
     * String object representation
     * 
     * @author Daniele Pantaleone
     * @return A <tt>String</tt> representing this object
     **/
    public String toString() {
        return "[ id : " + this.getId() + " | name : " + this.getName() + " | keyword : " + this.getKeyword() + " | level : " + this.getLevel() + " ]";
    }
    
}