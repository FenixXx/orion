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

import com.orion.domain.Client;
import com.orion.exception.ParserException;
import com.orion.utility.BooleanParser;

public class Command {
    
    private Client  client;
    private Prefix  prefix;
    private String  handle;
    private String  params;
    private boolean force;
    
    
    /**
     * Object constructor
     * 
     * @author Daniele Pantaleone
     * @param  client The <tt>Client</tt> tho issued the <tt>Command</tt>
     * @param  prefix The <tt>Prefix</tt> associated to this <tt>Command</tt>
     * @param  handle The command name or alias
     * @param  params Extra command parameters
     **/
    public Command(Client client, Prefix prefix, String handle, String params) {
        this(client, prefix, handle, params, false);
    }
    
    
    /**
     * Object constructor
     * 
     * @author Daniele Pantaleone
     * @param  client The <tt>Client</tt> tho issued the <tt>Command</tt>
     * @param  prefix The <tt>Prefix</tt> associated to this <tt>Command</tt>
     * @param  handle The command name or alias
     * @param  params Extra command parameters
     * @param  force  <tt>true</tt> if this command needs to be executed no matter
     *                the <tt>Client</tt> group level, <tt>false</tt> otherwise
     **/
    public Command(Client client, Prefix prefix, String handle, String params, boolean force) {
        setClient(client);
        setPrefix(prefix);
        setHandle(handle);
        setParams(params);
        setForce(force);
    }
    
    
    /**
     * Return number of tokens extracted from the parameters <tt>String</tt>
     * 
     * @author Daniele Pantaleone
     * @return The number of tokens extracted from the parameters <tt>String</tt>
     **/
    public int getParamNum() {
        
        // Don't bother if null or empty string
        if (((this.params == null)) || (this.params.isEmpty()))
            return 0;
        
        return this.params.split("\\s").length;
        
    }
    
    
    /**
     * Return the command argument at the given index as a <tt>Boolean</tt>
     * 
     * @author Daniele Pantaleone
     * @param  index The argument index
     * @throws ParserException If the string cannot be parsed as a <tt>Boolean</tt>
     * @return The command argument at the given index or <tt>null</tt> if there is no match
     **/
    public Boolean getParamBoolean(int index) throws ParserException {
        String token = this.getParamString(index);
        if (token != null) return BooleanParser.valueOf(token);
        return null;
    }
    
    
    /**
     * Return the command argument at the given index as a <tt>Double</tt>
     * 
     * @author Daniele Pantaleone
     * @param  index The argument index
     * @throws NumberFormatException If the string cannot be parsed as a <tt>Double</tt>
     * @return The command argument at the given index or <tt>null</tt> if there is no match
     **/
    public Double getParamDouble(int index) throws NumberFormatException {
        String token = this.getParamString(index);
        if (token != null) return Double.valueOf(token);
        return null;
    }
    

    /**
     * Return the command argument at the given index as a <tt>Float</tt>
     * 
     * @author Daniele Pantaleone
     * @param  index The argument index
     * @throws NumberFormatException If the string cannot be parsed as a <tt>Float</tt>
     * @return The command argument at the given index or <tt>null> if there is no match
     **/
    public Float getParamFloat(int index) throws NumberFormatException {
        String token = this.getParamString(index);
        if (token != null) return Float.valueOf(token);
        return null;
    }
    
    
    /**
     * Return the command argument at the given index as an <tt>Integer</tt>
     * 
     * @author Daniele Pantaleone
     * @param  index The argument index
     * @throws NumberFormatException If the string cannot be parsed as an <tt>Integer</tt>
     * @return The command argument at the given index or <tt>null</tt> if there is no match
     **/
    public Integer getParamInt(int index) throws NumberFormatException {
        String token = this.getParamString(index);
        if (token != null) return Integer.valueOf(token);
        return null;
    }
    
    
    /**
     * Return the command argument at the given index as a <tt>Long</tt>
     * 
     * @author Daniele Pantaleone
     * @param  index The argument index
     * @throws NumberFormatException If the string cannot be parsed as a <tt>Long</tt>
     * @return The command argument at the given index or <tt>null</tt> if there is no match
     **/
    public Long getParamLong(int index) throws NumberFormatException {
        String token = this.getParamString(index);
        if (token != null) return Long.valueOf(token);
        return null;
    }
    
    
    /**
     * Return the command argument at the given index as a <tt>Short</tt>
     * 
     * @author Daniele Pantaleone
     * @param  index The argument index
     * @throws NumberFormatException If the string cannot be parsed as a <tt>Short</tt>
     * @return The command argument at the given index or <tt>null</tt> if there is no match
     **/
    public Short getParamShort(int index) throws NumberFormatException {
        String token = this.getParamString(index);
        if (token != null) return Short.valueOf(token);
        return null;
    }
    
    
    /**
     * Return the command argument at the given index as a <tt>String</tt>
     * 
     * @author Daniele Pantaleone
     * @param  index The argument index
     * @return The command argument at the given index or <tt>null</tt> if there is no match
     **/
    public String getParamString(int index) {
        
        // No params specified
        if (this.params == null)
            return null;
        
        // Tokenizing the string
        String params[] = this.params.split("\\s");
        
        // Index out of bounds
        if ((index < 0) || (index > params.length - 1))
            return null;
        
        return params[index];
        
    }
    
    
    /**
     * Return the command argument at the given index as a <tt>String</tt><br>
     * It will split the param string into tokens and return concatenated
     * tokens according to the given index<br>
     * 
     * <pre>
     * Command cmd = new Command('!', "example", "This is an example string");
     * String result = command.getParamStringConcact(2);
     * System.out.println(result); -> "an example string"
     * </pre>
     * 
     * @author Daniele Pantaleone
     * @param  index The argument index
     * @return The command argument at the given index or <tt>null</tt> if there is no match
     **/
    public String getParamStringConcat(int index) {
        
        // No params specified
        if (this.params == null)
            return null;
        
        // Tokenizing the string
        String params[] = this.params.split("\\s", index + 1);
        
        // Index out of bounds
        if ((index < 0) || (index > params.length - 1))
            return null;
        
        return params[index];
        
    }

    
    private void setClient(Client client) {
        if (client == null) throw new NullPointerException();
        this.client = client;
    }


    private void setPrefix(Prefix prefix) {
        if (prefix == null) throw new NullPointerException();
        this.prefix = prefix;
    }


    private void setHandle(String handle) {
        if (handle == null) throw new NullPointerException();
        if (handle.trim().isEmpty()) throw new IllegalArgumentException();
        
        this.handle = handle;
    }


    private void setParams(String params) {
        if (params == null) throw new NullPointerException();
        this.params = params;
    }


    private void setForce(boolean force) {
        this.force = force;
    }
    

    public Client getClient() {
        return client;
    }

    
    public Prefix getPrefix() {
        return prefix;
    }
    
    
    public String getHandle() {
        return handle;
    }

    
    public String getParams() {
        return params;
    }
    
    
    public boolean isForce() {
        return force;
    }
    
}
