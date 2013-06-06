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

import com.orion.bot.PenaltyType;
import com.orion.domain.Client;
import com.orion.domain.Penalty;

public interface PenaltyDao {
    
    
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
    public abstract List<Penalty> loadByLimit(int limit) throws ClassNotFoundException, SQLException, UnknownHostException;
    
    
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
    public abstract List<Penalty> loadByLimit(PenaltyType type, int limit) throws ClassNotFoundException, SQLException, UnknownHostException;
    
    
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
    public abstract List<Penalty> loadByClient(Client client) throws ClassNotFoundException, SQLException, UnknownHostException;
    
    
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
    public abstract List<Penalty> loadByClient(Client client, PenaltyType type) throws ClassNotFoundException, SQLException, UnknownHostException;
    
    
    /**
     * Create a new entry in the database for the current object
     * 
     * @author Daniele Pantaleone
     * @param  penalty The <tt>Penalty</tt> object whose informations needs to be stored
     * @throws ClassNotFoundException If the JDBC driver fails in being loaded
     * @throws SQLException If the insert query fails somehow
     **/
    public abstract void insert(Penalty penalty) throws ClassNotFoundException, SQLException;
    
    
    /**
     * Update domain object in the database
     * 
     * @author Daniele Pantaleone
     * @param  penalty The <tt>Penalty</tt> object whose informations needs to be updated
     * @throws ClassNotFoundException If the JDBC driver fails in being loaded
     * @throws SQLException If the update query fails somehow
     **/
    public abstract void update(Penalty penalty) throws ClassNotFoundException, SQLException;
    
    
    /**
     * Delete domain object from the database
     * 
     * @author Daniele Pantaleone
     * @param  penalty The <tt>Penalty</tt> object whose informations needs to be deleted
     * @throws ClassNotFoundException If the JDBC driver fails in being loaded
     * @throws SQLException If the delete query fails somehow
     **/
    public abstract void delete(Penalty penalty) throws ClassNotFoundException, SQLException;
    
    
}