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
 * @copyright   Daniele Pantaleone, 29 January, 2013
 * @package     com.orion.dao
 **/

package com.orion.dao;

import java.net.UnknownHostException;
import java.sql.SQLException;
import java.util.List;

import com.orion.domain.Client;

public interface ClientDao {
    
    
    /**
     * Return the <tt>Client</tt> object matching the specified <tt>Client</tt> id
     * 
     * @author Daniele Pantaleone
     * @param  id The <tt>Client</tt> object primary key
     * @throws ClassNotFoundException If the JDBC driver fails in being loaded
     * @throws SQLException If the load query fails somehow
     * @throws UnknownHostException If we can't generate an <tt>InetAddress</tt> object using the <tt>Client</tt> IP address
     * @return A <tt>Client</tt> object matching the given id or <tt>null</tt> if we have no match
     **/
    public abstract Client loadById(int id) throws ClassNotFoundException, SQLException, UnknownHostException;
    
    
    /**
     * Return the <tt>Client</tt> object matching the specified <tt>Client</tt> guid
     * 
     * @author Daniele Pantaleone
     * @param  guid The <tt>Client</tt> guid
     * @throws ClassNotFoundException If the JDBC driver fails in being loaded
     * @throws SQLException If the load query fails somehow
     * @throws UnknownHostException If we can't generate an <tt>InetAddress</tt> object using the <tt>Client</tt> IP address
     * @return A <tt>Client</tt> object matching the given guid or <tt>null</tt> if we have no match
     **/
    public abstract Client loadByGuid(String guid) throws ClassNotFoundException, SQLException, UnknownHostException;
    
    
    /**
     * Return the <tt>Client</tt> object matching the specified <tt>Client</tt> auth login
     * 
     * @author Daniele Pantaleone
     * @param  auth The <tt>Client</tt> auth login
     * @throws ClassNotFoundException If the JDBC driver fails in being loaded
     * @throws SQLException If the load query fails somehow
     * @throws UnknownHostException If we can't generate an <tt>InetAddress</tt> object using the <tt>Client</tt> IP address
     * @return A <tt>Client</tt> object matching the given auth login or <tt>null</tt> if we have no match
     **/
    public abstract Client loadByAuth(String auth) throws ClassNotFoundException, SQLException, UnknownHostException;
    
    
    /**
     * Return a <tt>List</tt> of <tt>Client</tt> object matching the specified <tt>Group</tt> level
     * 
     * @author Daniele Pantaleone
     * @param  level The <tt>Group</tt> level
     * @throws ClassNotFoundException If the JDBC driver fails in being loaded
     * @throws SQLException If the load query fails somehow
     * @throws UnknownHostException If we can't generate an <tt>InetAddress</tt> object using the <tt>Client</tt> IP address
     * @return A <tt>List</tt> of <tt>Client</tt> object matching the specified <tt>Group</tt> keyword
     **/
    public abstract List<Client> loadByGroup(int level) throws ClassNotFoundException, SQLException, UnknownHostException;
    
    
    /**
     * Return a <tt>List</tt> of <tt>Client</tt> object matching the specified <tt>Group</tt> keyword
     * 
     * @author Daniele Pantaleone
     * @param  keyword The <tt>Group</tt> keyword
     * @throws ClassNotFoundException If the JDBC driver fails in being loaded
     * @throws SQLException If the load query fails somehow
     * @throws UnknownHostException If we can't generate an <tt>InetAddress</tt> object using the <tt>Client</tt> IP address
     * @return A <tt>List</tt> of <tt>Client</tt> object matching the specified <tt>Group</tt> keyword
     **/
    public abstract List<Client> loadByGroup(String keyword) throws ClassNotFoundException, SQLException, UnknownHostException;
    
    
    /**
     * Create a new entry in the database for the current object.
     * 
     * @author Daniele Pantaleone
     * @param  client The <tt>Client</tt> whose informations needs to be stored
     * @throws ClassNotFoundException If the JDBC driver fails in being loaded
     * @throws SQLException If the insert query fails somehow
     **/
    public abstract void insert(Client client) throws ClassNotFoundException, SQLException;
    
    
    /**
     * Update domain object in the database.
     * 
     * @author Daniele Pantaleone
     * @param  client The <tt>Client</tt> whose informations needs to be updated
     * @throws ClassNotFoundException If the JDBC driver fails in being loaded
     * @throws SQLException If the update query fails somehow
     **/
    public abstract void update(Client client) throws ClassNotFoundException, SQLException;
    
    
    /**
     * Delete domain object from the database.
     * 
     * @author Daniele Pantaleone
     * @param  client The <tt>Client</tt> whose informations needs to be deleted
     * @throws ClassNotFoundException If the JDBC driver fails in being loaded
     * @throws SQLException If the delete query fails somehow
     **/
    public abstract void delete(Client client) throws ClassNotFoundException, SQLException;

}
