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
 * @copyright   Daniele Pantaleone, 30 January, 2013
 * @package     com.orion.control
 **/

package com.orion.control;

import java.sql.SQLException;
import java.util.List;

import org.apache.commons.logging.Log;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import com.orion.bot.Orion;
import com.orion.dao.AliasDao;
import com.orion.dao.AliasDaoMySql;
import com.orion.domain.Alias;
import com.orion.domain.Client;

public class AliasCtl {
        
    private final Log log;
    private final DateTimeZone timezone;
	private final AliasDao dao;
    
	
    /**
     * Object constructor
     * 
     * @author Daniele Pantaleone
     * @param  orion <tt>Orion</tt> object reference
     **/
    public AliasCtl(Orion orion) {
    	this.log = orion.log;
    	this.timezone = orion.timezone;
        this.dao = new AliasDaoMySql(orion);
    }
    
    
    /**
     * Return a collection of <tt>Alias</tt> objects matching the given <tt>Client</tt>
     * 
     * @author Daniele Pantaleone
     * @param  client The <tt>Client</tt> object on which to perform the search
     * @throws ClassNotFoundException If the JDBC driver fails in being loaded
     * @throws SQLException If the load query fails somehow
     * @return A collection of <tt>Alias</tt> objects matching the given <tt>Client</tt>
     **/
    public List<Alias> getByClient(Client client) throws ClassNotFoundException, SQLException {
        return this.dao.loadByClient(client);
    }
    
    
    /**
     * Return an <tt>Alias</tt> object matching the given <tt>Client</tt> id and name
     * 
     * @author Daniele Pantaleone
     * @param  client The <tt>Client</tt> object on which to perform the search
     * @throws ClassNotFoundException If the JDBC driver fails in being loaded
     * @throws SQLException If the load query fails somehow
     * @return An <tt>Alias</tt> object matching the given <tt>Client</tt> id and name or <tt>null</tt> if we have no match
     **/
    public Alias getByClientName(Client client) throws ClassNotFoundException, SQLException {
    	return this.dao.loadByClientName(client);
    }
    
    
    /**
     * Create a new entry in the database for the current object.
     * 
     * @author Daniele Pantaleone
     * @param  alias The <tt>Alias</tt> object whose informations needs to be stored
     * @throws ClassNotFoundException If the JDBC driver fails in being loaded
     * @throws SQLException If the insert query fails somehow
     **/
    public void insert(Alias alias) throws ClassNotFoundException, SQLException {
        
        if (!alias.getClient().isBot()) {
        	DateTime date = new DateTime(this.timezone);
            alias.setTimeAdd(date);
            alias.setTimeEdit(date);
            this.log.trace("[SQL] INSERT `aliases`: " + alias.toString());
            this.dao.insert(alias);
        }
        
    }
    
    
    /**
     * Update domain object in the database.
     * 
     * @author Daniele Pantaleone
     * @param  alias The <tt>Alias</tt> object whose informations needs to be updated
     * @throws ClassNotFoundException If the JDBC driver fails in being loaded
     * @throws SQLException If the update query fails somehow
     **/
    public void update(Alias alias) throws ClassNotFoundException, SQLException {

        if (!alias.getClient().isBot()) {
            alias.setTimeEdit(new DateTime(this.timezone));
            this.log.trace("[SQL] UPDATE `aliases`: " + alias.toString());
            this.dao.update(alias);
        }
        
    }
    
    /**
     * Delete domain object from the database.
     * 
     * @author Daniele Pantaleone
     * @param  alias The <tt>Alias</tt> object whose informations needs to be deleted
     * @throws ClassNotFoundException If the JDBC driver fails in being loaded
     * @throws SQLException If the delete query fails somehow
     **/
    public void delete(Alias alias) throws ClassNotFoundException, SQLException {
        
    	if (!alias.getClient().isBot()) {
    		this.log.trace("[SQL] DELETE `aliases`: " + alias.toString());
    		this.dao.delete(alias);
    	}
    	
    }
    
    
    /**
     * Save the <tt>Alias</tt> object in the database
     *
     * @author Daniele Pantaleone
     * @param  alias The <tt>Alias</tt> object to be saved in the database
     * @throws ClassNotFoundException If the JDBC driver fails in being loaded
     * @throws SQLException If the load query fails somehow
     **/
    public void save(Alias alias) throws ClassNotFoundException, SQLException { 
        if (alias.getId() > 0) { this.update(alias); } 
        else { this.insert(alias); }
    }
    
}