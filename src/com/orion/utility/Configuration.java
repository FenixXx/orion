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
 * @author      Daniele Pantaleone, Mathia Van Malderen
 * @version     1.0
 * @copyright   Mathias Van Malderen, 07 February, 2013
 * @package     com.orion.utility
 **/

package com.orion.utility;

import java.util.List;
import java.util.Map;

import com.orion.exception.ParserException;

public interface Configuration {
    
    
    /**
     * Read the option value of a configuration section as a <tt>boolean</tt>
     * 
     * @author Mathias Van Malderen, Daniele Pantaleone
     * @param  section The name of the section from where to retrieve the value
     * @param  option The name of the option from where to retrieve the value
     * @throws ParserException If an error occurs while retrieving the value from the XML config file
     * @return The value retrieved from the XML configuration file parsed as a <tt>Boolean</tt>
     **/
    public abstract boolean getBoolean(String section, String option) throws ParserException;
    
    
    /**
     * Read the option value of a configuration section as a <tt>boolean</tt><br>
     * If an Exception occurrs the provided default value will be returned
     * 
     * @author Mathias Van Malderen, Daniele Pantaleone
     * @param  section The name of the section from where to retrieve the value
     * @param  option The name of the option from where to retrieve the value
     * @param  defaultValue The default value to be returned if there is a failure while parsing the configuration file
     * @return The value retrieved from the XML configuration file parsed as a <tt>Boolean</tt>
     **/
    public abstract boolean getBoolean(String section, String option, boolean defaultValue);
    
    
    /**
     * Read the option value of a configuration section as a <tt>double<tt>
     * 
     * @author Mathias Van Malderen, Daniele Pantaleone
     * @param  section The name of the section from where to retrieve the value
     * @param  option The name of the option from where to retrieve the value
     * @throws ParserException If an error occurs while retrieving the value from the XML config file
     * @return The value retrieved from the XML configuration file parsed as a <tt>Double</tt>
     **/
    public abstract double getDouble(String section, String option) throws ParserException;
    
    
    /**
     * Read the option value of a configuration section as a <tt>double</tt><br>
     * If an Exception occurrs the provided default value will be returned
     * 
     * @author Mathias Van Malderen, Daniele Pantaleone
     * @param  section The name of the section from where to retrieve the value
     * @param  option The name of the option from where to retrieve the value
     * @param  defaultValue The default value to be returned if there is a failure while parsing the configuration file
     * @return The value retrieved from the XML configuration file parsed as a <tt>Double</tt>
     **/
    public abstract double getDouble(String section, String option, double defaultValue);
    
    
    /**
     * Read the option value of a configuration section as a <tt>float</tt>
     * 
     * @author Mathias Van Malderen, Daniele Pantaleone
     * @param  section The name of the section from where to retrieve the value
     * @param  option The name of the option from where to retrieve the value
     * @throws ParserException If an error occurs while retrieving the value from the XML config file
     * @return The value retrieved from the XML configuration file parsed as a <tt>Float</tt>
     **/
    public abstract float getFloat(String section, String option) throws ParserException;
    
    
    /**
     * Read the option value of a configuration section as a <tt>float</tt><br>
     * If an Exception occurrs the provided default value will be returned
     * 
     * @author Mathias Van Malderen, Daniele Pantaleone
     * @param  section The name of the section from where to retrieve the value
     * @param  option The name of the option from where to retrieve the value
     * @param  defaultValue The default value to be returned if there is a failure while parsing the configuration file
     * @return The value retrieved from the XML configuration file parsed as a <tt>Float</tt>
     **/
    public abstract float getFloat(String section, String option, float defaultValue);
    
    
    /**
     * Read the option value of a configuration section as an <tt>int</tt>
     * 
     * @author Mathias Van Malderen, Daniele Pantaleone
     * @param  section The name of the section from where to retrieve the value
     * @param  option The name of the option from where to retrieve the value
     * @throws ParserException If an error occurs while retrieving the value from the XML config file
     * @return The value retrieved from the XML configuration file parsed as an <tt>Integer</tt>
     **/
    public abstract int getInt(String section, String option) throws ParserException;
    
    
    /**
     * Read the option value of a configuration section as an <tt>int</tt><br>
     * If an Exception occurrs the provided default value will be returned
     * 
     * @author Mathias Van Malderen, Daniele Pantaleone
     * @param  section The name of the section from where to retrieve the value
     * @param  option The name of the option from where to retrieve the value
     * @param  defaultValue The default value to be returned if there is a failure while parsing the configuration file
     * @return The value retrieved from the XML configuration file parsed as an <tt>Integer</tt>
     **/
    public abstract int getInt(String section, String option, int defaultValue);
    

    /**
     * Read the option value of a configuration section as a <tt>long</tt><br>
     * 
     * @author Mathias Van Malderen, Daniele Pantaleone
     * @param  section The name of the section from where to retrieve the value
     * @param  option The name of the option from where to retrieve the value
     * @throws ParserException If an error occurs while retrieving the value from the XML config file
     * @return The value retrieved from the XML configuration file parsed as a <tt>Long</tt>
     **/
    public abstract long getLong(String section, String option) throws ParserException;
    
    
    /**
     * Read the option value of a configuration section as a <tt>long</tt><br>
     * If an Exception occurrs the provided default value will be returned
     * 
     * @author Mathias Van Malderen, Daniele Pantaleone
     * @param  section The name of the section from where to retrieve the value
     * @param  option The name of the option from where to retrieve the value
     * @param  defaultValue The default value to be returned if there is a failure while parsing the configuration file
     * @return The value retrieved from the XML configuration file parsed as a <tt>Long</tt>
     **/
    public abstract long getLong(String section, String option, long defaultValue);
    
    
    /**
     * Read the option value of a configuration section as a <tt>short</tt>
     * 
     * @author Mathias Van Malderen, Daniele Pantaleone
     * @param  section The name of the section from where to retrieve the value
     * @param  option The name of the option from where to retrieve the value
     * @throws ParserException If an error occurs while retrieving the value from the XML config file
     * @return The value retrieved from the XML configuration file parsed as a <tt>Short</tt>
     **/
    public abstract short getShort(String section, String option) throws ParserException;
    
    
    /**
     * Read the option value of a configuration section as a <tt>short</tt><br>
     * If an Exception occurrs the provided default value will be returned
     * 
     * @author Mathias Van Malderen, Daniele Pantaleone
     * @param  section The name of the section from where to retrieve the value
     * @param  option The name of the option from where to retrieve the value
     * @param  defaultValue The default value to be returned if there is a failure while parsing the configuration file
     * @return The value retrieved from the XML configuration file parsed as a <tt>Short</tt>
     **/
    public abstract short getShort(String section, String option, short defaultValue);
    
    
    /**
     * Read the option value of a configuration section as a <tt>String</tt>
     * 
     * @author Mathias Van Malderen, Daniele Pantaleone
     * @param  section The name of the section from where to retrieve the value
     * @param  option The name of the option from where to retrieve the value
     * @throws ParserException If an error occurs while retrieving the value from the XML config file
     * @return The value retrieved from the XML configuration file parsed as a <tt>String</tt>
     **/
    public abstract String getString(String section, String option) throws ParserException;
    
    
    /**
     * Read the option value of a configuration section as a <tt>String</tt><br>
     * If an Exception occurrs the provided default value will be returned
     * 
     * @author Mathias Van Malderen, Daniele Pantaleone
     * @param  section The name of the section from where to retrieve the value
     * @param  option The name of the option from where to retrieve the value
     * @param  defaultValue The default value to be returned if there is a failure while parsing the configuration file
     * @return The value retrieved from the XML configuration file parsed as a <tt>String</tt>
     **/
    public abstract String getString(String section, String option, String defaultValue);
    
    
    /**
     * Read a human readable time notation from the configuration file and return
     * the correspondent amount of milliseconds as a <tt>long</tt><br>
     * 
     * @author Mathias Van Malderen, Daniele Pantaleone
     * @param  section The name of the section from where to retrieve the value
     * @param  option The name of the option from where to retrieve the value
     * @throws ParserException If an error occurs while retrieving the value from the XML config file
     * @return The amount of time expressed in milliseconds as a <tt>long</tt><br>
     **/
    public abstract long getTime(String section, String option);
    
    
    /**
     * Read a human readable time notation from the configuration file and return
     * the correspondent amount of milliseconds as a <tt>long</tt><br>
     * If an Exception occurrs the provided default value will be returned
     * 
     * @author Mathias Van Malderen, Daniele Pantaleone
     * @param  section The name of the section from where to retrieve the value
     * @param  option The name of the option from where to retrieve the value
     * @param  defaultValue The default value to be returned if there is a failure while parsing the configuration file
     * @return The amount of time expressed in milliseconds as a <tt>long</tt><br>
     **/
    public abstract long getTime(String section, String option, long defaultValue);
    
    
    /**
     * Read all setting names and setting values from a section
     * 
     * @author Mathias Van Malderen, Daniele Pantaleone
     * @param  section The name of the section to retrieve
     * @throws ParserException If there is an error while fetching data
     * @return A <tt>List<String></tt> containing all the values of the specified section
     **/
    public abstract List<String> getList(String section) throws ParserException;
        
   
    /**
     * Read all setting names and setting values from a section
     * 
     * @author Mathias Van Malderen, Daniele Pantaleone
     * @param  section The name of the section to retrieve
     * @throws ParserException If there is an error while fetching data
     * @return A <tt>Map<String, String></tt> with section option value stored with the format <tt>key|value</tt>
     **/
    public abstract Map<String, String> getMap(String section) throws ParserException;

}
