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

import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.logging.Log;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.orion.exception.ParserException;

public class XmlConfiguration implements Configuration {
        
    private static final String SECTION_XPATH = "/configuration/section[@name='%s']/set";
    private static final String SECTION_NODE_XPATH = SECTION_XPATH + "[@name='%s']";
    
    private Log log = null;
    private DocumentBuilder builder = null;
    private Document document = null;
    private XPath xpath = null;
    
    
    /**
     * Object constructor
     * 
     * @author Mathias Van Malderen
     * @param  config The configuration file path
     * @throws ParserException If there is an error while initializing the parser
     **/
    public XmlConfiguration(String config) throws ParserException {
        this(new File(config));        
    }
    
    
    /**
     * Object constructor
     * 
     * @author Mathias Van Malderen
     * @param  config The configuration file path
     * @param  log Main logger reference
     * @throws ParserException If there is an error while initializing the parser
     **/
    public XmlConfiguration(String config, Log log) throws ParserException {
        this(new File(config), log);
    }
    
    
    /**
     * Object constructor
     * 
     * @author Mathias Van Malderen
     * @param  config The configuration <tt>File</tt>
     * @throws ParserException If there is an error while initializing the parser
     **/
    public XmlConfiguration(File config) throws ParserException {
        
        try {
            
            this.builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            this.xpath = XPathFactory.newInstance().newXPath();
            this.document = this.builder.parse(config);
        
        } catch (IOException | SAXException | ParserConfigurationException e) {
            // Rethrowing our custom exception
            throw new ParserException("Unable to initialize XML configuration parser [ path : " + config.getAbsolutePath() + " ]", e);
        }
        
    }
    
    
    /**
     * Object constructor
     * 
     * @author Mathias Van Malderen
     * @param  config The configuration <tt>File</tt>
     * @param  log Main logger reference
     * @throws ParserException If there is an error while initializing the parser
     **/
    public XmlConfiguration(File config, Log log) throws ParserException {
        
        try {
            
            this.log = log;
            this.builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            this.xpath = XPathFactory.newInstance().newXPath();
            this.document = this.builder.parse(config);
        
        } catch (IOException | SAXException | ParserConfigurationException e) {
            // Rethrowing our custom exception
            throw new ParserException("Unable to initialize XML configuration parser [ path : " + config.getAbsolutePath() + " ]", e);
        }
        
    }
     
    
    /**
     * Return the section lookup XPath expression with section filled in
     * 
     * @author Mathias Van Malderen
     * @param  section The section from which to build the XPath expression
     * @return The section lookup XPath expression with section filled in
     **/
    private String getSectionXPath(String section) {
        return String.format(SECTION_XPATH, section);
    }
    
    
    /**
     * Return the option value lookup XPath expression with section and option filled in
     * 
     * @author Mathias Van Malderen
     * @param  section The section from which to build the XPath expression
     * @param  option The option from which to build the XPath expression
     * @return The option value lookup XPath expression with section and option filled in
     **/
    private String getSectionNodeXPath(String section, String option) {
        return String.format(SECTION_NODE_XPATH, section, option);
    }
    
    
    /**
     * Read the option value of a configuration section as a <tt>boolean</tt>
     * 
     * @author Mathias Van Malderen, Daniele Pantaleone
     * @param  section The name of the section from where to retrieve the value
     * @param  option The name of the option from where to retrieve the value
     * @throws ParserException If an error occurs while retrieving the value from the XML config file
     * @return The value retrieved from the XML configuration file parsed as a <tt>Boolean</tt>
     **/
    public boolean getBoolean(String section, String option) throws ParserException {
        
        try {
            
            // Converting the value retrieved from the getString() method
            return BooleanParser.parseBoolean(this.getString(section, option));
        
        } catch (ParserException e) {
            // Rethrowing our custom exception
            throw new ParserException("Unable to parse value as a Boolean [ section : " + section + " | option : " + option + " ]", e);
        }
        
    }
    
    
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
    public boolean getBoolean(String section, String option, boolean defaultValue) {
        
        try {
            
            // Converting the value retrieved from the getString() method
            return BooleanParser.parseBoolean(this.getString(section, option));
        
        } catch (ParserException e) {
            // Logging the Exception and returning the provided default value
            if (this.log != null) this.log.debug("Unable to parse value as a Boolean. Using default value [ section : " + section + " | option : " + option + " | default : " + defaultValue + " ]", e);
            return defaultValue;
        }
        
    }
    
    
    /**
     * Read the option value of a configuration section as a <tt>double<tt>
     * 
     * @author Mathias Van Malderen, Daniele Pantaleone
     * @param  section The name of the section from where to retrieve the value
     * @param  option The name of the option from where to retrieve the value
     * @throws ParserException If an error occurs while retrieving the value from the XML config file
     * @return The value retrieved from the XML configuration file parsed as a <tt>Double</tt>
     **/
    public double getDouble(String section, String option) throws ParserException {
        
        try {
            
            // Converting the value retrieved from the getString() method
            return Double.parseDouble(this.getString(section, option));
        
        } catch (NumberFormatException e) {
            // Rethrowing our custom exception
            throw new ParserException("Unable to parse value as a Double [ section : " + section + " | option : " + option + " ]", e);
        }

    }
    
    
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
    public double getDouble(String section, String option, double defaultValue) {
        
        try {
            
            // Converting the value retrieved from the getString() method
            return Double.parseDouble(this.getString(section, option));
        
        } catch (NumberFormatException | ParserException e) {
            // Logging the Exception and returning the provided default value
            if (this.log != null) this.log.debug("Unable to parse value as a Double. Using default value [ section : " + section + " | option : " + option + " | default : " + defaultValue + " ]", e);
            return defaultValue;
        }
        
    }
    
    
    /**
     * Read the option value of a configuration section as a <tt>float</tt>
     * 
     * @author Mathias Van Malderen, Daniele Pantaleone
     * @param  section The name of the section from where to retrieve the value
     * @param  option The name of the option from where to retrieve the value
     * @throws ParserException If an error occurs while retrieving the value from the XML config file
     * @return The value retrieved from the XML configuration file parsed as a <tt>Float</tt>
     **/
    public float getFloat(String section, String option) throws ParserException {
        
        try {
            
            // Converting the value retrieved from the getString() method
            return Float.parseFloat(this.getString(section, option));
        
        } catch (NumberFormatException e) {
            // Rethrowing our custom exception
            throw new ParserException("Unable to parse value as a Float [ section : " + section + " | option : " + option + " ]", e);
        }
        
    }
    
    
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
    public float getFloat(String section, String option, float defaultValue) {
        
        try {
            
            // Converting the value retrieved from the getString() method
            return Float.parseFloat(this.getString(section, option));
        
        } catch (NumberFormatException | ParserException e) {
            // Logging the Exception and returning the provided default value
            if (this.log != null) this.log.debug("Unable to parse value as a Float. Using default value [ section : " + section + " | option : " + option + " | default : " + defaultValue + " ]", e);
            return defaultValue;
        }
        
    }
    
    
    /**
     * Read the option value of a configuration section as an <tt>int</tt>
     * 
     * @author Mathias Van Malderen, Daniele Pantaleone
     * @param  section The name of the section from where to retrieve the value
     * @param  option The name of the option from where to retrieve the value
     * @throws ParserException If an error occurs while retrieving the value from the XML config file
     * @return The value retrieved from the XML configuration file parsed as an <tt>Integer</tt>
     **/
    public int getInt(String section, String option) throws ParserException {

        try {
            
            // Converting the value retrieved from the getString() method
            return Integer.parseInt(this.getString(section, option));
        
        } catch (NumberFormatException e) {
            // Rethrowing our custom exception
            throw new ParserException("Unable to parse value as an Integer [ section : " + section + " | option : " + option + " ]", e);
        }
        
    }
    
    
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
    public int getInt(String section, String option, int defaultValue) {
        
        try {
            
            // Converting the value retrieved from the getString() method
            return Integer.parseInt(this.getString(section, option));
        
        } catch (NumberFormatException | ParserException e) {
            // Logging the Exception and returning the provided default value
            if (this.log != null) this.log.debug("Unable to parse value as an Integer. Using default value [ section : " + section + " | option : " + option + " | default : " + defaultValue + " ]", e);
            return defaultValue;
        }
        
    }
    

    /**
     * Read the option value of a configuration section as a <tt>long</tt><br>
     * 
     * @author Mathias Van Malderen, Daniele Pantaleone
     * @param  section The name of the section from where to retrieve the value
     * @param  option The name of the option from where to retrieve the value
     * @throws ParserException If an error occurs while retrieving the value from the XML config file
     * @return The value retrieved from the XML configuration file parsed as a <tt>Long</tt>
     **/
    public long getLong(String section, String option) throws ParserException {
        
        try {
            
            // Converting the value retrieved from the getString() method
            return Long.parseLong(this.getString(section, option));
        
        } catch (NumberFormatException e) {
            // Rethrowing our custom exception
            throw new ParserException("Unable to parse value as a Long [ section : " + section + " | option : " + option + " ]", e);
        }
        
    }
    
    
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
    public long getLong(String section, String option, long defaultValue) {
        
        try {
            
            // Converting the value retrieved from the getString() method
            return Long.parseLong(this.getString(section, option));
        
        } catch (NumberFormatException | ParserException e) {
            // Logging the Exception and returning the provided default value
            if (this.log != null) this.log.debug("Unable to parse value as a Long. Using default value [ section : " + section + " | option : " + option + " | default : " + defaultValue + " ]", e);
            return defaultValue;
        }
        
    }
    
    
    /**
     * Read the option value of a configuration section as a <tt>short</tt>
     * 
     * @author Mathias Van Malderen, Daniele Pantaleone
     * @param  section The name of the section from where to retrieve the value
     * @param  option The name of the option from where to retrieve the value
     * @throws ParserException If an error occurs while retrieving the value from the XML config file
     * @return The value retrieved from the XML configuration file parsed as a <tt>Short</tt>
     **/
    public short getShort(String section, String option) throws ParserException {
        
        try {
            
            // Converting the value retrieved from the getString() method
            return Short.parseShort(this.getString(section, option));
        
        } catch (NumberFormatException e) {
            // Rethrowing our custom exception
            throw new ParserException("Unable to parse value as a Short [ section : " + section + " | option : " + option + " ]", e);
        }
        
    }
    
    
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
    public short getShort(String section, String option, short defaultValue) {
        
        try {
            
            // Converting the value retrieved from the getString() method
            return Short.parseShort(this.getString(section, option));
        
        } catch (NumberFormatException | ParserException e) {
            // Logging the Exception and returning the provided default value
            if (this.log != null) this.log.debug("Unable to parse value as a Short. Using default value [ section : " + section + " | option : " + option + " | default : " + defaultValue + " ]", e);
            return defaultValue;
        }
        
    }
    
    
    /**
     * Read the option value of a configuration section as a <tt>String</tt>
     * 
     * @author Mathias Van Malderen, Daniele Pantaleone
     * @param  section The name of the section from where to retrieve the value
     * @param  option The name of the option from where to retrieve the value
     * @throws ParserException If an error occurs while retrieving the value from the XML config file
     * @return The value retrieved from the XML configuration file parsed as a <tt>String</tt>
     **/
    public String getString(String section, String option) throws ParserException {
        
        try {
            
            // Fetching the result from the XML configuration file
            String result = this.xpath.evaluate(this.getSectionNodeXPath(section, option), this.document);
            
            if (result.isEmpty()) {
                // No value at specified section/option. Throwing an Exception so top level layers will notice
                throw new ParserException("The value retrieved from the XML configuration file is empty [ section : " + section + " | option : " + option + " ]");
            }
            
            return result;
        
        } catch (XPathExpressionException e) {
            // Rethrowing our custom exception
            throw new ParserException("Unable to retrieve value from the XML configuration file [ section : " + section + " | option : " + option + " ]", e);
        }
        
    }
    
    
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
    public String getString(String section, String option, String defaultValue) {
        
        try {
            
            // Fetching the result from the XML configuration file
            String result = this.xpath.evaluate(this.getSectionNodeXPath(section, option), this.document);
            
            if (result.isEmpty()) {
               
                // Returning the provided default value
                if (this.log != null) 
                    this.log.debug("The value retrieved from the XML configuration file is empty. Using default value [ section : " + section + " | option : " + option + " | default : " + defaultValue + " ]");
                
                return defaultValue;
            
            }
            
            return result;
        
        } catch (XPathExpressionException e) {
            
            // Logging the Exception and returning the provided default value
            if (this.log != null) 
                this.log.error("Unable to retrieve value from the XML configuration file. Using default value [ section : " + section + " | option : " + option + " | default : " + defaultValue + " ]", e);
            
            return defaultValue;
        
        }
        
    }
    
    
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
    public long getTime(String section, String option) {
        
         try {
             
             // Converting the value retrieved from the getString() method
             return TimeParser.parseTime(this.getString(section, option));
         
         } catch (ParserException e) {
             // Rethrowing our custom exception
             throw new ParserException("Unable to parse string as human readable time format [ section : " + section + " | option : " + option + " ]", e);
         }
    }
    
    
    
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
    public long getTime(String section, String option, long defaultValue) {
        
        try {
            
            // Converting the value retrieved from the getString() method
            return TimeParser.parseTime(this.getString(section, option));
        
        } catch (ParserException e) {
            // Logging the Exception and returning the provided default value
            if (this.log != null) this.log.debug("Unable to parse string as human readable time format. Using default value [ section : " + section + " | option : " + option + " | default : " + defaultValue + " ]", e);
            return defaultValue;
        }

    }
    
    
    /**
     * Read all setting names and setting values from a section
     * 
     * @author Mathias Van Malderen, Daniele Pantaleone
     * @param  section The name of the section to retrieve
     * @throws ParserException If there is an error while fetching data
     * @return A <tt>List<String></tt> containing all the values of the specified section
     **/
    public List<String> getList(String section) throws ParserException {
        
        try {
            
            NodeList nodes = (NodeList)this.xpath.evaluate(this.getSectionXPath(section), this.document, XPathConstants.NODESET);
            List<String> result = new LinkedList<String>();
            for (int i = 0; i < nodes.getLength(); i++) {
                String value = nodes.item(i).getChildNodes().item(0).getNodeValue();
                result.add(value);
            }
            
            return result;
        
        } catch (XPathExpressionException e) {
            throw new ParserException("Unable to retrieve section from the XML configuration file [ section : " + section + " ]", e);
        }
        
    }
    
    
    /**
     * Read all setting names and setting values from a section
     * 
     * @author Mathias Van Malderen, Daniele Pantaleone
     * @param  section The name of the section to retrieve
     * @throws ParserException If there is an error while fetching data
     * @return A <tt>Map<String, String></tt> with section option value stored with the format <tt>key|value</tt>
     **/
    public Map<String, String> getMap(String section) throws ParserException {
        
        try {
            
            NodeList nodes = (NodeList)this.xpath.evaluate(this.getSectionXPath(section), this.document, XPathConstants.NODESET);
            Map<String, String> result = new LinkedHashMap<String, String>();
            for (int i = 0; i < nodes.getLength(); i++) {
                String settingName  = nodes.item(i).getAttributes().getNamedItem("name").getNodeValue();
                String settingValue = nodes.item(i).getChildNodes().item(0).getNodeValue();
                result.put(settingName, settingValue);
            }
            
            return result;
        
        } catch (XPathExpressionException e) {
            throw new ParserException("Unable to retrieve section from the XML configuration file [ section : " + section + " ]", e);
        }
        
    }

}

