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