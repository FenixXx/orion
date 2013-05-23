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
 * @version     1.0
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
import com.orion.dao.CallvoteDao;
import com.orion.dao.MySqlCallvoteDao;
import com.orion.domain.Callvote;
import com.orion.domain.Client;

public class CallvoteC {
        
    private final Log log;
    private final DateTimeZone timezone;
    private final CallvoteDao dao;
    
    
    /**
     * Object constructor.
     * 
     * @author Daniele Pantaleone
     * @param  orion <tt>Orion</tt> object reference
     **/
    public CallvoteC(Orion orion) {
        this.log = orion.log;
        this.timezone = orion.timezone;
        this.dao = new MySqlCallvoteDao(orion);
    }
    
    
    /**
     * Return a collection of <tt>Callvote</tt> objects matching the given query limit
     * 
     * @author Daniele Pantaleone
     * @param  limit The number of </tt>Callvote</tt> objects to retrieve
     * @throws ClassNotFoundException If the JDBC driver fails in being loaded
     * @throws SQLException If the load query fails somehow
     * @throws UnknownHostException If we can't generate an <tt>InetAddress</tt> object using a <tt>Client</tt> IP address
     * @return A collection of </tt>Callvote</tt> objects matching the given query limit
     **/
    public List<Callvote> getByLimit(int limit) throws ClassNotFoundException, SQLException, UnknownHostException {
        return this.dao.loadByLimit(limit);
    }
    
    
    /**
     * Return a collection of <tt>Callvote</tt> objects matching the given <tt>Client</tt>
     * 
     * @author Daniele Pantaleone
     * @param  client The <tt>Client</tt> object on which to perform the search
     * @throws ClassNotFoundException If the JDBC driver fails in being loaded
     * @throws SQLException If the load query fails somehow
     * @return A collection of <tt>Callvote</tt> objects matching the given <tt>Client</tt>
     **/
    public List<Callvote> getByClient(Client client) throws ClassNotFoundException, SQLException, UnknownHostException {
        return this.dao.loadByClient(client);
    }
    
    
    /**
     * Return a collection of <tt>Callvote</tt> objects matching the given <tt>Client</tt> and the given query limit
     * 
     * @author Daniele Pantaleone
     * @param  client The <tt>Client</tt> object on which to perform the search
     * @param  limit The number of <tt>Callvote</tt> objects to retrieve
     * @throws ClassNotFoundException If the JDBC driver fails in being loaded
     * @throws SQLException If the load query fails somehow
     * @return A collection of <tt>Callvote</tt> objects matching the given <tt>Client</tt> and the given query limit
     **/
    public List<Callvote> getByClientLimit(Client client, int limit) throws ClassNotFoundException, SQLException, UnknownHostException {
        return this.dao.loadByClientLimit(client, limit);
    }
    
    
    /**
     * Create a new entry in the database for the current object.
     * 
     * @author Daniele Pantaleone
     * @param  callvote The <tt>Callvote</tt> object whose informations needs to be stored
     * @throws ClassNotFoundException If the JDBC driver fails in being loaded
     * @throws SQLException If the insert query fails somehow
     **/
    public void insert(Callvote callvote) throws ClassNotFoundException, SQLException {
        
        if (!callvote.client.bot) {
            DateTime date = new DateTime(this.timezone);
            callvote.time_add = date;
            callvote.time_edit = date;
            this.log.trace("[SQL] INSERT `callvotes`: " + callvote.toString());
            this.dao.insert(callvote);
        }
        
    }
    
    
    /**
     * Update domain object in the database
     * 
     * @author Daniele Pantaleone
     * @param  callvote The <tt>Callvote</tt> object whose informations needs to be updated
     * @throws ClassNotFoundException If the JDBC driver fails in being loaded
     * @throws SQLException If the update query fails somehow
     **/
    public void update(Callvote callvote) throws ClassNotFoundException, SQLException {
        
        if (!callvote.client.bot) {
            callvote.time_edit = new DateTime(this.timezone);
            this.log.trace("[SQL] UPDATE `callvotes`: " + callvote.toString());
            this.dao.update(callvote);
        }
        
    }
    
    
    /**
     * Delete domain object from the database
     * 
     * @author Daniele Pantaleone
     * @param  callvote The <tt>Callvote</tt> object whose informations needs to be deleted
     * @throws ClassNotFoundException If the JDBC driver fails in being loaded
     * @throws SQLException If the delete query fails somehow
     **/
    public void delete(Callvote callvote) throws ClassNotFoundException, SQLException {
        
        if (!callvote.client.bot) {
            this.log.trace("[SQL] DELETE `callvotes`: " + callvote.toString());
            this.dao.delete(callvote);
        }
        
    }
    
    
    /**
     * Save the <tt>Callvote</tt> object in the database
     *
     * @author Daniele Pantaleone
     * @param  client The <tt>Callvote</tt> object to be saved in the database
     * @throws ClassNotFoundException If the JDBC driver fails in being loaded
     * @throws SQLException If the load query fails somehow
     **/
    public void save(Callvote callvote) throws ClassNotFoundException, SQLException { 
        if (callvote.id > 0) { this.update(callvote); } 
        else { this.insert(callvote); }
    }

    
}