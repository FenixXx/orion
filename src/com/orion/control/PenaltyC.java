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
import com.orion.bot.PenaltyType;
import com.orion.dao.MySqlPenaltyDao;
import com.orion.dao.PenaltyDao;
import com.orion.domain.Client;
import com.orion.domain.Penalty;

public class PenaltyC {
        
    private final Log log;
    private final DateTimeZone timezone;
    private final PenaltyDao dao;
    
    
    /**
     * Object constructor.
     * 
     * @author Daniele Pantaleone
     * @param  orion <tt>Orion</tt> object reference
     **/
    public PenaltyC(Orion orion) {
        this.log = orion.log;
        this.timezone = orion.timezone;
        this.dao = new MySqlPenaltyDao(orion);
    }
       
    
    /**
     * Return a collection of <tt>Penalty</tt> objects matching the given query limit<br>
     * The list is ordered by <tt>TIME_ADD DESC</tt>
     * 
     * @author Daniele Pantaleone
     * @param  limit The number of </tt>Penalty</tt> objects to retrieve
     * @throws ClassNotFoundException If the JDBC driver fails in being loaded
     * @throws SQLException If the load query fails somehow
     * @throws UnknownHostException If we can't generate an <tt>InetAddress</tt> object using a <tt>Client</tt> IP address
     * @return A collection of active </tt>Penalty</tt> objects matching the given query limit
     **/
    public List<Penalty> getByLimit(int limit) throws ClassNotFoundException, SQLException, UnknownHostException {
        return this.dao.loadByLimit(limit);
    }
    
    
    /**
     * Return a collection of <tt>Penalty</tt> objects matching the given query limit
     * and the given <tt>PenaltyType</tt><br>
     * The list is ordered by <tt>TIME_ADD DESC</tt>
     * 
     * @author Daniele Pantaleone
     * @param  type The <tt>PenaltyType</tt> object to match in the query
     * @param  limit The number of </tt>Penalty</tt> objects to retrieve
     * @throws ClassNotFoundException If the JDBC driver fails in being loaded
     * @throws SQLException If the load query fails somehow
     * @throws UnknownHostException If we can't generate an <tt>InetAddress</tt> object using a <tt>Client</tt> IP address
     * @return A collection of active </tt>Penalty</tt> objects matching the given query limit
     **/
    public List<Penalty> loadByLimit(PenaltyType type, int limit) throws ClassNotFoundException, SQLException, UnknownHostException {
    	return this.dao.loadByLimit(type, limit);
    }
    
    
    /**
     * Return a collection of active <tt>Penalty</tt> objects matching the given <tt>Client</tt><br>
     * The list is ordered by <tt>TIME_ADD DESC</tt>
     * 
     * @author Daniele Pantaleone
     * @param  client The <tt>Client</tt> object on which to perform the search
     * @throws ClassNotFoundException If the JDBC driver fails in being loaded
     * @throws SQLException If the load query fails somehow
     * @throws UnknownHostException If we can't generate an <tt>InetAddress</tt> object using a <tt>Client</tt> IP address
     * @return A collection of active <tt>Penalty</tt> objects matching the given <tt>Client</tt>
     **/
    public List<Penalty> getByClient(Client client) throws ClassNotFoundException, SQLException, UnknownHostException {
        return this.dao.loadByClient(client);
    }
    
    
    /**
     * Return a collection of active <tt>Penalty</tt> objects matching the given <tt>Client</tt>
     * and the given <tt>PenaltyType</tt><br>
     * The list is ordered by <tt>TIME_ADD DESC</tt>
     * 
     * @author Daniele Pantaleone
     * @param  client The <tt>Client</tt> object on which to perform the search
     * @param  type The <tt>PenaltyType</tt> object to match in the query
     * @throws ClassNotFoundException If the JDBC driver fails in being loaded
     * @throws SQLException If the load query fails somehow
     * @throws UnknownHostException If we can't generate an <tt>InetAddress</tt> object using a <tt>Client</tt> IP address
     * @return A collection of active <tt>Penalty</tt> objects matching the given <tt>Client</tt>
     **/
    public List<Penalty> getByClient(Client client, PenaltyType type) throws ClassNotFoundException, SQLException, UnknownHostException {
    	return this.dao.loadByClient(client, type);
    }
    
    
    /**
     * Create a new entry in the database for the current object
     * 
     * @author Daniele Pantaleone
     * @param  penalty The <tt>Penalty</tt> object whose informations needs to be stored
     * @throws ClassNotFoundException If the JDBC driver fails in being loaded
     * @throws SQLException If the insert query fails somehow
     **/
    public void insert(Penalty penalty) throws ClassNotFoundException, SQLException {
        
        if (!penalty.getClient().isBot()) {
        	DateTime date = new DateTime(this.timezone);
            penalty.setTimeAdd(date);
            penalty.setTimeEdit(date);
            this.log.trace("[SQL] INSERT `penalties`: " + penalty.toString());
            this.dao.insert(penalty);
        }
        
    }
    
    
    /**
     * Update domain object in the database
     * 
     * @author Daniele Pantaleone
     * @param  penalty The <tt>Penalty</tt> object whose informations needs to be updated
     * @throws ClassNotFoundException If the JDBC driver fails in being loaded
     * @throws SQLException If the update query fails somehow
     **/
    public void update(Penalty penalty) throws ClassNotFoundException, SQLException {
        
        if (!penalty.getClient().isBot()) {
            penalty.setTimeEdit(new DateTime(this.timezone));
            this.log.trace("[SQL] UPDATE `penalties`: " + penalty.toString());
            this.dao.update(penalty);
        }
        
    }
    
    
    /**
     * Delete domain object from the database
     * 
     * @author Daniele Pantaleone
     * @param  penalty The <tt>Penalty</tt> object whose informations needs to be deleted
     * @throws ClassNotFoundException If the JDBC driver fails in being loaded
     * @throws SQLException If the delete query fails somehow
     **/
    public void delete(Penalty penalty) throws ClassNotFoundException, SQLException {
        
        if (!penalty.getClient().isBot()) {
            this.log.trace("[SQL] DELETE `penalties`: " + penalty.toString());
            this.dao.delete(penalty);
        }
    
    }
    
}