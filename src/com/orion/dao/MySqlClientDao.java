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
 * @copyright   Daniele Pantaleone, 05 October, 2012
 * @package     com.orion.dao
 **/

package com.orion.dao;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import com.orion.bot.Orion;
import com.orion.domain.Client;
import com.orion.domain.Group;
import com.orion.storage.DataSourceManager;

public class MySqlClientDao implements ClientDao {
    
    private final DateTimeZone timezone;
    private final DataSourceManager storage;
    
    private PreparedStatement statement;
    private ResultSet resultset;
    
    private static final String LOAD_BY_ID = "SELECT `cl`.`id` AS `cl_id`, " +
                                             "`cl`.`name` AS `cl_name`, " +
                                             "`cl`.`connections` AS `cl_connections`, " +
                                             "`cl`.`ip` AS `cl_ip`, " +
                                             "`cl`.`guid` AS `cl_guid`, " +
                                             "`cl`.`auth` AS `cl_auth`, " +
                                             "`cl`.`time_add` AS `cl_time_add`, " +
                                             "`cl`.`time_edit` AS `cl_time_edit`, " +
                                             "`gr`.`id` AS `gr_id`, " +
                                             "`gr`.`name` AS `gr_name`, " +
                                             "`gr`.`keyword` AS `gr_keyword`, " +
                                             "`gr`.`level` AS `gr_level` " +
                                             "FROM `clients` AS `cl` INNER JOIN `groups` AS `gr` " +
                                             "ON `cl`.`group_id` = `gr`.`id` WHERE `cl`.`id` = ?";
    
    
    private static final String LOAD_BY_GUID = "SELECT `cl`.`id` AS `cl_id`, " +
                                               "`cl`.`name` AS `cl_name`, " +
                                               "`cl`.`connections` AS `cl_connections`, " +
                                               "`cl`.`ip` AS `cl_ip`, " +
                                               "`cl`.`guid` AS `cl_guid`, " +
                                               "`cl`.`auth` AS `cl_auth`, " +
                                               "`cl`.`time_add` AS `cl_time_add`, " +
                                               "`cl`.`time_edit` AS `cl_time_edit`, " +
                                               "`gr`.`id` AS `gr_id`, " +
                                               "`gr`.`name` AS `gr_name`, " +
                                               "`gr`.`keyword` AS `gr_keyword`, " +
                                               "`gr`.`level` AS `gr_level` " +
                                               "FROM `clients` AS `cl` INNER JOIN `groups` AS `gr` " +
                                               "ON `cl`.`group_id` = `gr`.`id` WHERE `cl`.`guid` = ?";

    
    private static final String LOAD_BY_AUTH = "SELECT `cl`.`id` AS `cl_id`, " +
                                               "`cl`.`name` AS `cl_name`, " +
                                               "`cl`.`connections` AS `cl_connections`, " +
                                               "`cl`.`ip` AS `cl_ip`, " +
                                               "`cl`.`guid` AS `cl_guid`, " +
                                               "`cl`.`auth` AS `cl_auth`, " +
                                               "`cl`.`time_add` AS `cl_time_add`, " +
                                               "`cl`.`time_edit` AS `cl_time_edit`, " +
                                               "`gr`.`id` AS `gr_id`, " +
                                               "`gr`.`name` AS `gr_name`, " +
                                               "`gr`.`keyword` AS `gr_keyword`, " +
                                               "`gr`.`level` AS `gr_level` " +
                                               "FROM `clients` AS `cl` INNER JOIN `groups` AS `gr` " +
                                               "ON `cl`.`group_id` = `gr`.`id` WHERE `cl`.`auth` = ?";
    
    
    private static final String LOAD_BY_GROUP_KEYWORD = "SELECT `cl`.`id` AS `cl_id`, " +
                                                        "`cl`.`name` AS `cl_name`, " +
                                                        "`cl`.`connections` AS `cl_connections`, " +
                                                        "`cl`.`ip` AS `cl_ip`, " +
                                                        "`cl`.`guid` AS `cl_guid`, " +
                                                        "`cl`.`auth` AS `cl_auth`, " +
                                                        "`cl`.`time_add` AS `cl_time_add`, " +
                                                        "`cl`.`time_edit` AS `cl_time_edit`, " +
                                                        "`gr`.`id` AS `gr_id`, " +
                                                        "`gr`.`name` AS `gr_name`, " +
                                                        "`gr`.`keyword` AS `gr_keyword`, " +
                                                        "`gr`.`level` AS `gr_level` " +
                                                        "FROM `clients` AS `cl` INNER JOIN `groups` AS `gr` " +
                                                        "ON `cl`.`group_id` = `gr`.`id` WHERE `gr`.`keyword` = ?";
    
    
    private static final String LOAD_BY_GROUP_LEVEL = "SELECT `cl`.`id` AS `cl_id`, " +
                                                      "`cl`.`name` AS `cl_name`, " +
                                                      "`cl`.`connections` AS `cl_connections`, " +
                                                      "`cl`.`ip` AS `cl_ip`, " +
                                                      "`cl`.`guid` AS `cl_guid`, " +
                                                      "`cl`.`auth` AS `cl_auth`, " +
                                                      "`cl`.`time_add` AS `cl_time_add`, " +
                                                      "`cl`.`time_edit` AS `cl_time_edit`, " +
                                                      "`gr`.`id` AS `gr_id`, " +
                                                      "`gr`.`name` AS `gr_name`, " +
                                                      "`gr`.`keyword` AS `gr_keyword`, " +
                                                      "`gr`.`level` AS `gr_level` " +
                                                      "FROM `clients` AS `cl` INNER JOIN `groups` AS `gr` " +
                                                      "ON `cl`.`group_id` = `gr`.`id` WHERE `gr`.`level` = ?";
    
    
    private static final String INSERT = "INSERT INTO `clients` (`group_id`, `name`, `ip`, `guid`, `auth`, `time_add`, `time_edit`) VALUES (?,?,?,?,?,?,?)";
    private static final String UPDATE = "UPDATE `clients` SET `group_id` = ?, `name` = ?, `connections` = ?, `ip` = ?, `guid` = ?, `auth` = ?, `time_edit`= ? WHERE `id` = ?";
    private static final String DELETE = "DELETE FROM `clients` WHERE `id` = ?";
        
    
    /**
     * Object constructor
     * 
     * @author Daniele Pantaleone
     * @param  orion <tt>Orion</tt> object reference
     **/
    public MySqlClientDao(Orion orion) {
        this.timezone = orion.timezone;
        this.storage = orion.storage;
    }
    
    
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
    public Client loadById(int id) throws ClassNotFoundException, SQLException, UnknownHostException {
        
        this.statement = this.storage.getConnection().prepareStatement(LOAD_BY_ID);
        this.statement.setInt(1, id);
        this.resultset = this.statement.executeQuery();
        
        if (!this.resultset.next()) {    
            this.resultset.close();
            this.statement.close();
            return null;
        }
        
        // Retrieving the client object from the ResultSet tuple
        Client client = this.getObjectFromCursor(resultset);
        
        this.resultset.close();
        this.statement.close();
        
        return client;
            
    }
    
    
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
    public Client loadByGuid(String guid) throws ClassNotFoundException, SQLException, UnknownHostException {
        
        this.statement = this.storage.getConnection().prepareStatement(LOAD_BY_GUID);
        this.statement.setString(1, guid);
        this.resultset = statement.executeQuery();

        if (!this.resultset.next()) {    
            this.resultset.close();
            this.statement.close();
            return null;
        }
        
        // Retrieving the client object from the ResultSet tuple
        Client client = this.getObjectFromCursor(resultset);
        
        this.resultset.close();
        this.statement.close();
        
        return client;
        
    }
    
    
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
    public Client loadByAuth(String auth) throws ClassNotFoundException, SQLException, UnknownHostException {
        
        this.statement = this.storage.getConnection().prepareStatement(LOAD_BY_AUTH);
        this.statement.setString(1, auth);
        this.resultset = statement.executeQuery();

        if (!this.resultset.next()) {    
            this.resultset.close();
            this.statement.close();
            return null;
        }
        
        // Retrieving the client object from the ResultSet tuple
        Client client = this.getObjectFromCursor(resultset);
        
        this.resultset.close();
        this.statement.close();
        
        return client;
        
    }
    
    
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
    public List<Client> loadByGroup(int level) throws ClassNotFoundException, SQLException, UnknownHostException {
        
        this.statement = this.storage.getConnection().prepareStatement(LOAD_BY_GROUP_LEVEL);
        this.statement.setInt(1, level);
        this.resultset = statement.executeQuery();
        List<Client> collection = this.getCollectionFromResultSet(this.resultset);
        this.resultset.close();
        this.statement.close();
        
        return collection;
        
    }
    
    
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
    public List<Client> loadByGroup(String keyword) throws ClassNotFoundException, SQLException, UnknownHostException {
        
        this.statement = this.storage.getConnection().prepareStatement(LOAD_BY_GROUP_KEYWORD);
        this.statement.setString(1, keyword);
        this.resultset = statement.executeQuery();
        List<Client> collection = this.getCollectionFromResultSet(this.resultset);
        this.resultset.close();
        this.statement.close();
        
        return collection;
        
    }
    
    
    /**
     * Create a new entry in the database for the current object
     * 
     * @author Daniele Pantaleone
     * @param  client The <tt>Client</tt> whose informations needs to be stored
     * @throws ClassNotFoundException If the JDBC driver fails in being loaded
     * @throws SQLException If the insert query fails somehow
     **/
    public void insert(Client client) throws ClassNotFoundException, SQLException { 
        
        this.statement = this.storage.getConnection().prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS);
        this.statement.setInt(1, client.group.id);
        this.statement.setString(2, client.name);
        this.statement.setString(3, client.ip.getHostAddress());
        this.statement.setString(4, client.guid);
        if (client.auth != null) this.statement.setString(5, client.auth);
        else this.statement.setNull(5, Types.VARCHAR);
        this.statement.setLong(6, client.time_add.getMillis());
        this.statement.setLong(7, client.time_edit.getMillis());
        
        // Executing the statement
        this.statement.executeUpdate();
         
        // Retrieving the generated primary key
        this.resultset = this.statement.getGeneratedKeys();
        if (!this.resultset.next()) throw new SQLException("Unable to retrieve generated primary key from `clients` table");
        
        // Storing the new generated client id
        client.id = this.resultset.getInt(1);
        
        this.resultset.close();
        this.statement.close();
        
    }
    
    
    /**
     * Update domain object in the database
     * 
     * @author Daniele Pantaleone
     * @param  client The <tt>Client</tt> whose informations needs to be updated
     * @throws ClassNotFoundException If the JDBC driver fails in being loaded
     * @throws SQLException If the update query fails somehow
     **/
    public void update(Client client) throws ClassNotFoundException, SQLException { 
        
        this.statement = this.storage.getConnection().prepareStatement(UPDATE);
        this.statement.setInt(1, client.group.id);
        this.statement.setString(2, client.name);
        this.statement.setInt(3, client.connections);
        this.statement.setString(4, client.ip.getHostAddress());
        this.statement.setString(5, client.guid);
        if (client.auth != null) this.statement.setString(6, client.auth);
        else this.statement.setNull(6, Types.VARCHAR);
        this.statement.setLong(7, client.time_edit.getMillis());
        this.statement.setInt(8, client.id);
        
        // Executing the statement.
        this.statement.executeUpdate();
        this.statement.close();
        
    }
    
    
    /**
     * Delete domain object from the database
     * 
     * @author Daniele Pantaleone
     * @param  client The <tt>Client</tt> whose informations needs to be deleted
     * @throws ClassNotFoundException If the JDBC driver fails in being loaded
     * @throws SQLException If the delete query fails somehow
     **/
    public void delete(Client client) throws ClassNotFoundException, SQLException { 
        
        this.statement = this.storage.getConnection().prepareStatement(DELETE);
        statement.setInt(1, client.id);
        
        // Executing the statement.
        statement.executeUpdate();
        statement.close();
        
    }
    
    
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////// AUXILIARY METHODS /////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
    
    /**
     * Return a collection of <tt>Client</tt> objects accessible through the <tt>List</tt> interface
     * 
     * @author Daniele Pantaleone
     * @param  resultset The <tt>ResultSet</tt> object from which to fetch data
     * @throws SQLException If one of the <tt>ResultSet</tt> objects is not consistent
     * @throws UnknownHostException If we can't generate an <tt>InetAddress</tt> object using a <tt>Client</tt> IP address
     * @return A collection of <tt>Client</tt> objects matching the given <tt>ResultSet</tt>
     **/
    private List<Client> getCollectionFromResultSet(ResultSet resultset) throws SQLException, UnknownHostException {
        
        List<Client> collection = new LinkedList<Client>();
        
        while (resultset.next()) { 
            // Appending all the objects to the collection.
            Client client = this.getObjectFromCursor(resultset);
            collection.add(client);
        }       
        
        return Collections.unmodifiableList(collection);

    }
    
    
    /**
     * Return a <tt>Client</tt> object matching the given <tt>ResultSet</tt> tuple
     * 
     * @author Daniele Pantaleone
     * @param  resultset The <tt>ResultSet</tt> object from which to build the <tt>Client</tt> object
     * @throws SQLException If the <tt>ResultSet</tt> object is not consistent
     * @throws UnknownHostException If we can't generate an <tt>InetAddress</tt> object using the <tt>Client</tt> IP address
     * @return A <tt>Client</tt> object matching the given ResultSet tuple
     **/
    private Client getObjectFromCursor(ResultSet resultset) throws SQLException, UnknownHostException {
        
        Group group = new Group(resultset.getInt("gr_id"), 
                                resultset.getString("gr_name"), 
                                resultset.getString("gr_keyword"), 
                                resultset.getInt("gr_level"));
        
        return new Client(resultset.getInt("cl_id"), 
                          group, 
                          resultset.getString("cl_name"), 
                          resultset.getInt("cl_connections"), 
                          InetAddress.getByName(resultset.getString("cl_ip")),
                          resultset.getString("cl_guid"), 
                          resultset.getString("cl_auth"), 
                          new DateTime(resultset.getLong("cl_time_add"), this.timezone), 
                          new DateTime(resultset.getLong("cl_time_edit"), this.timezone));
        

    
    }

    
}