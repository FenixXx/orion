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
