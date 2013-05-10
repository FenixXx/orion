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

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import com.orion.bot.Orion;
import com.orion.domain.Alias;
import com.orion.domain.Client;
import com.orion.storage.DataSourceManager;

public class MySqlAliasDao implements AliasDao {
    
    private final DateTimeZone timezone;
    private final DataSourceManager storage;
    
    private PreparedStatement statement;
    private ResultSet resultset;
    
    private static final String LOAD_BY_CLIENT = "SELECT `id`, `name`, `num_used`, `time_add`, `time_edit` FROM `aliases` WHERE `client_id` = ? ORDER BY `num_used` DESC, `time_edit` DESC";
    private static final String LOAD_BY_CLIENT_NAME = "SELECT `id`, `name`, `num_used`, `time_add`, `time_edit` FROM `aliases` WHERE `client_id` = ? AND `name` = ?";    
    private static final String INSERT = "INSERT INTO `aliases` (`client_id`, `name`, `time_add`, `time_edit`) VALUES (?,?,?,?)";
    private static final String UPDATE = "UPDATE `aliases` SET `client_id` = ?, `name` = ?, `num_used` = ?, `time_edit` = ? WHERE `id` = ?";
    private static final String DELETE = "DELETE FROM `aliases` WHERE `id` = ?";
        
    
    /**
     * Object constructor.
     * 
     * @author Daniele Pantaleone
     * @param  orion <tt>Orion</tt> object reference
     **/
    public MySqlAliasDao(Orion orion) {
        this.timezone = orion.timezone;
        this.storage = orion.storage;
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
    public List<Alias> loadByClient(Client client) throws ClassNotFoundException, SQLException {
        
        this.statement = this.storage.getConnection().prepareStatement(LOAD_BY_CLIENT);
        this.statement.setInt(1, client.id);
        this.resultset = this.statement.executeQuery();
        List<Alias> collection = this.getCollectionFromResultSet(this.resultset, client);
        this.resultset.close();
        this.statement.close();
        
        return collection;
            
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
    public Alias loadByClientName(Client client) throws ClassNotFoundException, SQLException {
        
        this.statement = this.storage.getConnection().prepareStatement(LOAD_BY_CLIENT_NAME);
        this.statement.setInt(1, client.id);
        this.statement.setString(2, client.name);
        this.resultset = this.statement.executeQuery();
        
        if (!this.resultset.next()) {    
            this.resultset.close();
            this.statement.close();
            return null;
        }
        
        Alias alias = this.getObjectFromCursor(this.resultset, client);
        this.resultset.close();
        this.statement.close();
        
        return alias;
            
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
        
        this.statement = this.storage.getConnection().prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS);
        this.statement.setInt(1, alias.client.id);
        this.statement.setString(2, alias.name);
        this.statement.setLong(3, alias.time_add.getMillis());
        this.statement.setLong(4, alias.time_edit.getMillis());
        
        // Executing the statement
        this.statement.executeUpdate();
        
        // Retrieving the generated primary key
        this.resultset = this.statement.getGeneratedKeys();
        if (!this.resultset.next()) throw new SQLException("Unable to retrieve generated primary key from `aliases` table");
        
        // Storing the new generated client id
        alias.id = this.resultset.getInt(1);
        
        this.resultset.close();
        this.statement.close();
        
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
        
        this.statement = this.storage.getConnection().prepareStatement(UPDATE);
        this.statement.setInt(1, alias.client.id);
        this.statement.setString(2, alias.name);
        this.statement.setInt(3, alias.num_used);
        this.statement.setLong(4, alias.time_edit.getMillis());
        this.statement.setInt(5, alias.id);
        
        // Executing the statement.
        this.statement.executeUpdate();
        this.statement.close();
        
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
        
        this.statement = this.storage.getConnection().prepareStatement(DELETE);
        statement.setInt(1, alias.id);
        
        // Executing the statement.
        statement.executeUpdate();
        statement.close();
        
    }
    
    
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////// AUXILIARY METHODS /////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
    
    /**
     * Return a collection of <tt>Alias</tt> objects accessible through the <tt>List</tt> interface
     * 
     * @author Daniele Pantaleone
     * @param  resultset The <tt>ResultSet</tt> object from which to fetch data
     * @param  client The <tt>Client</tt> who those <tt>Alias</tt> objects belong to
     * @throws SQLException If one of the <tt>ResultSet</tt> objects is not consistent
     * @return A collection of <tt>Alias</tt> objects matching the given <tt>ResultSet</tt>
     **/
    private List<Alias> getCollectionFromResultSet(ResultSet resultset, Client client) throws SQLException {
        
        List<Alias> collection = new LinkedList<Alias>();
        
        while (resultset.next()) { 
            // Appending all the objects to the collection.
            Alias alias = this.getObjectFromCursor(resultset, client);
            collection.add(alias);
        }       
        
        return Collections.unmodifiableList(collection);

    }
    
    
    /**
     * Return an <tt>Alias</tt> object matching the given <tt>ResultSet</tt> tuple
     * 
     * @author Daniele Pantaleone
     * @param  resultset The <tt>ResultSet</tt> object from which to build the <tt>Alias</tt> object
     * @param  client The <tt>Client</tt> who this <tt>Alias</tt> belongs to
     * @throws SQLException If the <tt>ResultSet</tt> object is not consistent
     * @return An <tt>Alias</tt> object matching the given <tt>ResultSet</tt> tuple
     **/
    private Alias getObjectFromCursor(ResultSet resultset, Client client) throws SQLException {
        
        return new Alias(resultset.getInt("id"), 
                         client, 
                         resultset.getString("name"), 
                         resultset.getInt("num_used"), 
                         new DateTime(resultset.getLong("time_add"), this.timezone),  
                         new DateTime(resultset.getLong("time_edit"), this.timezone));
        
    }

    
}