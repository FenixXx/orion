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
 * @copyright   Daniele Pantaleone, 30 January, 2013
 * @package     com.orion.control
 **/

package com.orion.control;

import java.net.UnknownHostException;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.logging.Log;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import com.orion.bot.Orion;
import com.orion.dao.IpAliasDao;
import com.orion.dao.MySqlIpAliasDao;
import com.orion.domain.Client;
import com.orion.domain.IpAlias;

public class IpAliasCtl {
        
    private final Log log;
    private final DateTimeZone timezone;
    private final IpAliasDao dao;
    
    
    /**
     * Object constructor.
     * 
     * @author Daniele Pantaleone
     * @param  orion <tt>Orion</tt> object reference
     **/
    public IpAliasCtl(Orion orion) {
        this.log = orion.log;
        this.timezone = orion.timezone;
        this.dao = new MySqlIpAliasDao(orion);
    }
    
    
    /**
     * Return a collection of <tt>IpAlias</tt> objects matching the given <tt>Client</tt>
     * 
     * @author Daniele Pantaleone
     * @param  client The <tt>Client</tt> object on which to perform the search
     * @throws ClassNotFoundException If the JDBC driver fails in being loaded
     * @throws SQLException If the load query fails somehow
     * @throws UnknownHostException If we can't generate an <tt>InetAddress</tt> object using a <tt>Client</tt> IP address
     * @return A collection of </tt>IpAlias</tt> objects matching the given <tt>Client</tt>
     **/
    public List<IpAlias> getByClient(Client client) throws ClassNotFoundException, SQLException, UnknownHostException {
        return this.dao.loadByClient(client);
    }
    
    
    /**
     * Return an <tt>IpAlias</tt> object matching the given <tt>Client</tt> id and IP address
     * 
     * @author Daniele Pantaleone
     * @param  client The <tt>Client</tt> object on which to perform the search
     * @throws ClassNotFoundException If the JDBC driver fails in being loaded
     * @throws SQLException If the load query fails somehow
     * @throws UnknownHostException If we can't generate an <tt>InetAddress</tt> object using the <tt>Client</tt> IP address
     * @return An <tt>IpAlias</tt> object matching the given <tt>Client</tt> id and IP address or <tt>null</tt> if we have no match
     **/
    public IpAlias getByClientIp(Client client) throws ClassNotFoundException, SQLException, UnknownHostException {
        return this.dao.loadByClientIp(client);
    }
    
    
    /**
     * Create a new entry in the database for the current object
     * 
     * @author Daniele Pantaleone
     * @param  ipalias The <tt>IpAlias</tt> object whose informations needs to be stored
     * @throws ClassNotFoundException If the JDBC driver fails in being loaded
     * @throws SQLException If the insert query fails somehow
     **/
    public void insert(IpAlias ipalias) throws ClassNotFoundException, SQLException {
        
        if (!ipalias.getClient().isBot()) {
            DateTime date = new DateTime(this.timezone);
            ipalias.setTimeAdd(date);
            ipalias.setTimeEdit(date);
            this.log.trace("[SQL] INSERT `ipaliases`: " + ipalias.toString());
            this.dao.insert(ipalias);
        }
        
    }
    
    
    /**
     * Update domain object in the database
     * 
     * @author Daniele Pantaleone
     * @param  ipalias The <tt>IpAlias</tt> object whose informations needs to be updated
     * @throws ClassNotFoundException If the JDBC driver fails in being loaded
     * @throws SQLException If the update query fails somehow
     **/
    public void update(IpAlias ipalias) throws ClassNotFoundException, SQLException {
        
        if (!ipalias.getClient().isBot()) {
            ipalias.setTimeEdit(new DateTime(this.timezone));
            this.log.trace("[SQL] UPDATE `ipaliases`: " + ipalias.toString());
            this.dao.update(ipalias);
        }
        
    }
    
    
    /**
     * Delete domain object from the database.
     * 
     * @author Daniele Pantaleone
     * @param  ipalias The <tt>IpAlias</tt> object whose informations needs to be deleted
     * @throws ClassNotFoundException If the JDBC driver fails in being loaded
     * @throws SQLException If the delete query fails somehow
     **/
    public void delete(IpAlias ipalias) throws ClassNotFoundException, SQLException {
        
        if (!ipalias.getClient().isBot()) {
            this.log.trace("[SQL] DELETE `ipaliases`: " + ipalias.toString());
            this.dao.delete(ipalias);
        }
        
    }
    
    
    /**
     * Save the <tt>IpAlias</tt> object in the database
     *
     * @author Daniele Pantaleone
     * @param  alias The <tt>IpAlias</tt> object to be saved in the database
     * @throws ClassNotFoundException If the JDBC driver fails in being loaded
     * @throws SQLException If the load query fails somehow
     **/
    public void save(IpAlias ipalias) throws ClassNotFoundException, SQLException { 
        if (ipalias.getId() > 0) { this.update(ipalias); } 
        else { this.insert(ipalias); }
    }
    
}