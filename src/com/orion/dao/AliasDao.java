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
 * @package     com.orion.dao
 **/

package com.orion.dao;

import java.sql.SQLException;
import java.util.List;

import com.orion.domain.Alias;
import com.orion.domain.Client;

public interface AliasDao {
    
   
    /**
     * Return a collection of <tt>Alias</tt> objects matching the given <tt>Client</tt>
     * 
     * @author Daniele Pantaleone
     * @param  client The <tt>Client</tt> object on which to perform the search
     * @throws ClassNotFoundException If the JDBC driver fails in being loaded
     * @throws SQLException If the load query fails somehow
     * @return A collection of <tt>Alias</tt> objects matching the given <tt>Client</tt>
     **/
    public abstract List<Alias> loadByClient(Client client) throws ClassNotFoundException, SQLException;
    
    
    /**
     * Return an <tt>Alias</tt> object matching the given <tt>Client</tt> id and name
     * 
     * @author Daniele Pantaleone
     * @param  client The <tt>Client</tt> object on which to perform the search
     * @throws ClassNotFoundException If the JDBC driver fails in being loaded
     * @throws SQLException If the load query fails somehow
     * @return An <tt>Alias</tt> object matching the given <tt>Client</tt> id and name or <tt>null</tt> if we have no match
     **/
    public abstract Alias loadByClientName(Client client) throws ClassNotFoundException, SQLException;
        
    
    /**
     * Create a new entry in the database for the current object.
     * 
     * @author Daniele Pantaleone
     * @param  alias The <tt>Alias</tt> object whose informations needs to be stored
     * @throws ClassNotFoundException If the JDBC driver fails in being loaded
     * @throws SQLException If the insert query fails somehow
     **/
    public abstract void insert(Alias alias) throws ClassNotFoundException, SQLException;
    
    
    /**
     * Update domain object in the database.
     * 
     * @author Daniele Pantaleone
     * @param  alias The <tt>Alias</tt> object whose informations needs to be updated
     * @throws ClassNotFoundException If the JDBC driver fails in being loaded
     * @throws SQLException If the update query fails somehow
     **/
    public abstract void update(Alias alias) throws ClassNotFoundException, SQLException;
    
    
    /**
     * Delete domain object from the database.
     * 
     * @author Daniele Pantaleone
     * @param  alias The <tt>Alias</tt> object whose informations needs to be deleted
     * @throws ClassNotFoundException If the JDBC driver fails in being loaded
     * @throws SQLException If the delete query fails somehow
     **/
    public abstract void delete(Alias alias) throws ClassNotFoundException, SQLException;

}
