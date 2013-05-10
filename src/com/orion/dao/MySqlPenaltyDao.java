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

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import com.orion.bot.Orion;
import com.orion.bot.PenaltyType;
import com.orion.domain.Client;
import com.orion.domain.Group;
import com.orion.domain.Penalty;
import com.orion.storage.DataSourceManager;

public class MySqlPenaltyDao implements PenaltyDao {
    
    private final DateTimeZone timezone;
    private final DataSourceManager storage;
    
    private PreparedStatement statement;
    private ResultSet resultset;
        
    private static final String LOAD_BY_LIMIT = "SELECT `pn`.`id` AS `pn_id`, " +
                                                "`pn`.`type` AS `pn_type`, " +
                                                "`pn`.`active` AS `pn_active`, " +
                                                "`pn`.`reason` AS `pn_reason`, " +
                                                "`pn`.`time_add` AS `pn_time_add`, " +
                                                "`pn`.`time_edit` AS `pn_time_edit`, " +
                                                "`pn`.`time_expire` AS `pn_time_expire`, " +
                                                "`cl`.`id` AS `cl_id`, " +
                                                "`cl`.`name` AS `cl_name`, " +
                                                "`cl`.`connections` AS `cl_connections`, " +
                                                "`cl`.`ip` AS `cl_ip`, " +
                                                "`cl`.`guid` AS `cl_guid`, " +
                                                "`cl`.`auth` AS `cl_auth`, " +
                                                "`cl`.`time_add` AS `cl_time_add`, " +
                                                "`cl`.`time_edit` AS `cl_time_edit`, " +
                                                "`ad`.`id` AS `ad_id`, " +
                                                "`ad`.`name` AS `ad_name`, " +
                                                "`ad`.`connections` AS `ad_connections`, " +
                                                "`ad`.`ip` AS `ad_ip`, " +
                                                "`ad`.`guid` AS `ad_guid`, " +
                                                "`ad`.`auth` AS `ad_auth`, " +
                                                "`ad`.`time_add` AS `ad_time_add`, " +
                                                "`ad`.`time_edit` AS `ad_time_edit`, " +
                                                "`gr_cl`.`id` AS `gr_cl_id`, " +
                                                "`gr_cl`.`name` AS `gr_cl_name`, " +
                                                "`gr_cl`.`keyword` AS `gr_cl_keyword`, " +
                                                "`gr_cl`.`level` AS `gr_cl_level`, " +
                                                "`gr_ad`.`id` AS `gr_ad_id`, " +
                                                "`gr_ad`.`name` AS `gr_ad_name`, " +
                                                "`gr_ad`.`keyword` AS `gr_ad_keyword`, " +
                                                "`gr_ad`.`level` AS `gr_ad_level` " +
                                                "FROM `penalties` AS `pn` " +
                                                "INNER JOIN `clients` AS `cl` ON `pn`.`client_id` = `cl`.`id` " +
                                                "INNER JOIN `groups` AS `gr_cl` ON `cl`.`group_id` = `gr_cl`.`id` " +
                                                "LEFT OUTER JOIN `clients` AS `ad` ON `pn`.`admin_id` = `ad`.`id` " +
                                                "LEFT OUTER JOIN `groups` AS `gr_ad` ON `ad`.`group_id` = `gr_ad`.`id` " +
                                                "WHERE `pn`.`active` = 1 AND (`pn`.`time_expire` is NULL or `pn`.`time_expire` > ?) " +
                                                "ORDER BY `pn`.`time_add` DESC " +
                                                "LIMIT 0, ?";
    
    private static final String LOAD_BY_LIMIT_AND_TYPE = "SELECT `pn`.`id` AS `pn_id`, " +
                                                         "`pn`.`type` AS `pn_type`, " +
                                                         "`pn`.`active` AS `pn_active`, " +
                                                         "`pn`.`reason` AS `pn_reason`, " +
                                                         "`pn`.`time_add` AS `pn_time_add`, " +
                                                         "`pn`.`time_edit` AS `pn_time_edit`, " +
                                                         "`pn`.`time_expire` AS `pn_time_expire`, " +
                                                         "`cl`.`id` AS `cl_id`, " +
                                                         "`cl`.`name` AS `cl_name`, " +
                                                         "`cl`.`connections` AS `cl_connections`, " +
                                                         "`cl`.`ip` AS `cl_ip`, " +
                                                         "`cl`.`guid` AS `cl_guid`, " +
                                                         "`cl`.`auth` AS `cl_auth`, " +
                                                         "`cl`.`time_add` AS `cl_time_add`, " +
                                                         "`cl`.`time_edit` AS `cl_time_edit`, " +
                                                         "`ad`.`id` AS `ad_id`, " +
                                                         "`ad`.`name` AS `ad_name`, " +
                                                         "`ad`.`connections` AS `ad_connections`, " +
                                                         "`ad`.`ip` AS `ad_ip`, " +
                                                         "`ad`.`guid` AS `ad_guid`, " +
                                                         "`ad`.`auth` AS `ad_auth`, " +
                                                         "`ad`.`time_add` AS `ad_time_add`, " +
                                                         "`ad`.`time_edit` AS `ad_time_edit`, " +
                                                         "`gr_cl`.`id` AS `gr_cl_id`, " +
                                                         "`gr_cl`.`name` AS `gr_cl_name`, " +
                                                         "`gr_cl`.`keyword` AS `gr_cl_keyword`, " +
                                                         "`gr_cl`.`level` AS `gr_cl_level`, " +
                                                         "`gr_ad`.`id` AS `gr_ad_id`, " +
                                                         "`gr_ad`.`name` AS `gr_ad_name`, " +
                                                         "`gr_ad`.`keyword` AS `gr_ad_keyword`, " +
                                                         "`gr_ad`.`level` AS `gr_ad_level` " +
                                                         "FROM `penalties` AS `pn` " +
                                                         "INNER JOIN `clients` AS `cl` ON `pn`.`client_id` = `cl`.`id` " +
                                                         "INNER JOIN `groups` AS `gr_cl` ON `cl`.`group_id` = `gr_cl`.`id` " +
                                                         "LEFT OUTER JOIN `clients` AS `ad` ON `pn`.`admin_id` = `ad`.`id` " +
                                                         "LEFT OUTER JOIN `groups` AS `gr_ad` ON `ad`.`group_id` = `gr_ad`.`id` " +
                                                         "WHERE `pn`.`active` = 1 AND `pn`.`type` = ? AND (`pn`.`time_expire` is NULL or `pn`.`time_expire` > ?) " +
                                                         "ORDER BY `pn`.`time_add` DESC " +
                                                         "LIMIT 0, ?";
    
    private static final String LOAD_BY_CLIENT = "SELECT `pn`.`id` AS `pn_id`, " +
                                                 "`pn`.`type` AS `pn_type`, " +
                                                 "`pn`.`active` AS `pn_active`, " +
                                                 "`pn`.`reason` AS `pn_reason`, " +
                                                 "`pn`.`time_add` AS `pn_time_add`, " +
                                                 "`pn`.`time_edit` AS `pn_time_edit`, " +
                                                 "`pn`.`time_expire` AS `pn_time_expire`, " +
                                                 "`ad`.`id` AS `ad_id`, " +
                                                 "`ad`.`name` AS `ad_name`, " +
                                                 "`ad`.`connections` AS `ad_connections`, " +
                                                 "`ad`.`ip` AS `ad_ip`, " +
                                                 "`ad`.`guid` AS `ad_guid`, " +
                                                 "`ad`.`auth` AS `ad_auth`, " +
                                                 "`ad`.`time_add` AS `ad_time_add`, " +
                                                 "`ad`.`time_edit` AS `ad_time_edit`, " +
                                                 "`gr_ad`.`id` AS `gr_ad_id`, " +
                                                 "`gr_ad`.`name` AS `gr_ad_name`, " +
                                                 "`gr_ad`.`keyword` AS `gr_ad_keyword`, " +
                                                 "`gr_ad`.`level` AS `gr_ad_level` " +
                                                 "FROM `penalties` AS `pn` " +
                                                 "LEFT OUTER JOIN `clients` AS `ad` ON `pn`.`admin_id` = `ad`.`id` " +
                                                 "LEFT OUTER JOIN `groups` AS `gr_ad` ON `ad`.`group_id` = `gr_ad`.`id` " +
                                                 "WHERE `pn`.`client_id` = ? AND `pn`.`active` = 1 AND (`pn`.`time_expire` is NULL or `pn`.`time_expire` > ?) " +
                                                 "ORDER BY `pn`.`time_add` DESC";
    
    private static final String LOAD_BY_CLIENT_AND_TYPE = "SELECT `pn`.`id` AS `pn_id`, " +
                                                          "`pn`.`type` AS `pn_type`, " +
                                                          "`pn`.`active` AS `pn_active`, " +
                                                          "`pn`.`reason` AS `pn_reason`, " +
                                                          "`pn`.`time_add` AS `pn_time_add`, " +
                                                          "`pn`.`time_edit` AS `pn_time_edit`, " +
                                                          "`pn`.`time_expire` AS `pn_time_expire`, " +
                                                          "`ad`.`id` AS `ad_id`, " +
                                                          "`ad`.`name` AS `ad_name`, " +
                                                          "`ad`.`connections` AS `ad_connections`, " +
                                                          "`ad`.`ip` AS `ad_ip`, " +
                                                          "`ad`.`guid` AS `ad_guid`, " +
                                                          "`ad`.`auth` AS `ad_auth`, " +
                                                          "`ad`.`time_add` AS `ad_time_add`, " +
                                                          "`ad`.`time_edit` AS `ad_time_edit`, " +
                                                          "`gr_ad`.`id` AS `gr_ad_id`, " +
                                                          "`gr_ad`.`name` AS `gr_ad_name`, " +
                                                          "`gr_ad`.`keyword` AS `gr_ad_keyword`, " +
                                                          "`gr_ad`.`level` AS `gr_ad_level` " +
                                                          "FROM `penalties` AS `pn` " +
                                                          "LEFT OUTER JOIN `clients` AS `ad` ON `pn`.`admin_id` = `ad`.`id` " +
                                                          "LEFT OUTER JOIN `groups` AS `gr_ad` ON `ad`.`group_id` = `gr_ad`.`id` " +
                                                          "WHERE `pn`.`client_id` = ? AND `pn`.`type` = ? AND `pn`.`active` = 1 AND (`pn`.`time_expire` is NULL or `pn`.`time_expire` > ?) " +
                                                          "ORDER BY `pn`.`time_add` DESC";
    
    private static final String INSERT = "INSERT INTO `penalties` (`client_id`, `admin_id`, `type`, `reason`, `time_add`, `time_edit`, `time_expire`) VALUES (?,?,?,?,?,?,?)";
    private static final String UPDATE = "UPDATE `penalties` SET `client_id` = ?, `admin_id` = ?, `type` = ?, `active` = ?, `reason` = ?, `time_edit` = ?, `time_expire`= ? WHERE `id` = ?";
    private static final String DELETE = "DELETE FROM `penalties` WHERE `id` = ?";
    
    
    /**
     * Object constructor
     * 
     * @author Daniele Pantaleone
     * @param  orion <tt>Orion</tt> object reference
     **/
    public MySqlPenaltyDao(Orion orion) {
        this.timezone = orion.timezone;
        this.storage = orion.storage;
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
    public List<Penalty> loadByLimit(int limit) throws ClassNotFoundException, SQLException, UnknownHostException {
        
        this.statement = this.storage.getConnection().prepareStatement(LOAD_BY_LIMIT);
        this.statement.setLong(1, new Date().getTime());
        this.statement.setInt(2, limit);
        this.resultset = this.statement.executeQuery();
        List<Penalty> collection = this.getCollectionFromResultSet(this.resultset);
        this.resultset.close();
        this.statement.close();
        
        return collection;
            
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
        
        this.statement = this.storage.getConnection().prepareStatement(LOAD_BY_LIMIT_AND_TYPE);
        this.statement.setString(1, type.name());
        this.statement.setLong(2, new Date().getTime());
        this.statement.setInt(3, limit);
        this.resultset = this.statement.executeQuery();
        List<Penalty> collection = this.getCollectionFromResultSet(this.resultset);
        this.resultset.close();
        this.statement.close();
        
        return collection;
            
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
    public List<Penalty> loadByClient(Client client) throws ClassNotFoundException, SQLException, UnknownHostException {
        
        this.statement = this.storage.getConnection().prepareStatement(LOAD_BY_CLIENT);
        this.statement.setInt(1, client.id);
        this.statement.setLong(2, new Date().getTime());
        this.resultset = this.statement.executeQuery();
        List<Penalty> collection = this.getCollectionFromResultSet(this.resultset, client);
        this.resultset.close();
        this.statement.close();
        
        return collection;
            
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
    public List<Penalty> loadByClient(Client client, PenaltyType type) throws ClassNotFoundException, SQLException, UnknownHostException {
        
        this.statement = this.storage.getConnection().prepareStatement(LOAD_BY_CLIENT_AND_TYPE);
        this.statement.setInt(1, client.id);
        this.statement.setString(2, type.name());
        this.statement.setLong(3, new Date().getTime());
        this.resultset = this.statement.executeQuery();
        List<Penalty> collection = this.getCollectionFromResultSet(this.resultset, client);
        this.resultset.close();
        this.statement.close();
        
        return collection;
            
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
        
        this.statement = this.storage.getConnection().prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS);
        this.statement.setInt(1, penalty.client.id);
        this.statement.setInt(2, penalty.admin.id);
        this.statement.setString(3, penalty.type.name());
        if (penalty.reason != null) this.statement.setString(4, penalty.reason);
        else this.statement.setNull(4, Types.VARCHAR);
        this.statement.setLong(5, penalty.time_add.getMillis());
        this.statement.setLong(6, penalty.time_edit.getMillis());
        if (penalty.time_expire != null) this.statement.setLong(7, penalty.time_expire.getMillis());
        else this.statement.setNull(7, Types.BIGINT);
        
        // Executing the statement
        this.statement.executeUpdate();

        // Retrieving the generated primary key
        this.resultset = this.statement.getGeneratedKeys();
        if (!this.resultset.next()) throw new SQLException("Unable to retrieve generated primary key from `penalties` table");
        
        // Storing the new generated client id
        penalty.id = this.resultset.getInt(1);
        
        this.resultset.close();
        this.statement.close();
                
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
        
        this.statement = this.storage.getConnection().prepareStatement(UPDATE);
        this.statement.setInt(1, penalty.client.id);
        this.statement.setInt(2, penalty.admin.id);
        this.statement.setString(3, penalty.type.name());
        this.statement.setBoolean(4, penalty.active);
        if (penalty.reason != null) this.statement.setString(5, penalty.reason);
        else this.statement.setNull(5, Types.VARCHAR);
        this.statement.setLong(6, penalty.time_edit.getMillis());
        if (penalty.time_expire != null) this.statement.setLong(7, penalty.time_expire.getMillis());
        else this.statement.setNull(7, Types.BIGINT);
        this.statement.setInt(9, penalty.id);

        // Executing the statement
        this.statement.executeUpdate();
        this.statement.close();
        
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
        
        this.statement = this.storage.getConnection().prepareStatement(DELETE);
        statement.setInt(1, penalty.id);
        
        // Executing the statement
        statement.executeUpdate();
        statement.close();
        
    }
    
    
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////// AUXILIARY METHODS /////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
    
    /**
     * Return a collection of <tt>Penalty</tt> objects accessible through the <tt>List</tt> interface
     * 
     * @author Daniele Pantaleone
     * @param  resultset The <tt>ResultSet</tt> object from which to fetch data
     * @throws SQLException If the <tt>ResultSet</tt> object is not consistent
     * @throws IndexOutOfBoundsException If we cannot generate a <tt>PenaltyType</tt> object using the <tt>Penalty</tt> type name
     * @throws UnknownHostException If we can't generate an <tt>InetAddress</tt> object using the <tt>Client</tt> IP address
     * @return A collection of <tt>Penalty</tt> objects matching the given <tt>ResultSet</tt>
     **/
    private List<Penalty> getCollectionFromResultSet(ResultSet resultset) throws SQLException, IndexOutOfBoundsException, UnknownHostException {
        
        List<Penalty> collection = new LinkedList<Penalty>();
        
        while (resultset.next()) { 
            // Appending all the objects to the collection.
            Penalty penalty = this.getObjectFromCursor(resultset);
            collection.add(penalty);
        }       
        
        return Collections.unmodifiableList(collection);

    }
    
    
    /**
     * Return a collection of <tt>Penalty</tt> objects accessible through the <tt>List</tt> interface
     * 
     * @author Daniele Pantaleone
     * @param  resultset The <tt>ResultSet</tt> object from which to fetch data
     * @parma  client The <tt>Client</tt> who those <tt>Penalty</tt> objects belong to
     * @throws SQLException If the <tt>ResultSet</tt> object is not consistent
     * @throws IndexOutOfBoundsException If we cannot generate a <tt>PenaltyType</tt> object using the <tt>Penalty</tt> type name
     * @throws UnknownHostException If we can't generate an <tt>InetAddress</tt> object using the <tt>Client</tt> IP address
     * @return A collection of <tt>Penalty</tt> objects matching the given <tt>ResultSet</tt>
     **/
    private List<Penalty> getCollectionFromResultSet(ResultSet resultset, Client client) throws SQLException, IndexOutOfBoundsException, UnknownHostException {
        
        List<Penalty> collection = new LinkedList<Penalty>();
        
        while (resultset.next()) { 
            // Appending all the objects to the collection.
            Penalty penalty = this.getObjectFromCursor(resultset, client);
            collection.add(penalty);
        }       
        
        return Collections.unmodifiableList(collection);

    }
    
    
    /**
     * Return a <tt>Penalty</tt> object matching the given <tt>ResultSet</tt> tuple
     * 
     * @author Daniele Pantaleone
     * @param  resultset The <tt>ResultSet</tt> object from which to build the <tt>Penalty</tt> object
     * @throws SQLException If the <tt>ResultSet</tt> object is not consistent
     * @throws IndexOutOfBoundsException If we cannot generate a <tt>PenaltyType</tt> object using the <tt>Penalty</tt> type name
     * @throws UnknownHostException If we can't generate an <tt>InetAddress</tt> object using the <tt>Client</tt> IP address
     * @return A <tt>Penalty</tt> object matching the given <tt>ResultSet</tt> tuple
     **/
    private Penalty getObjectFromCursor(ResultSet resultset) throws SQLException, IndexOutOfBoundsException, UnknownHostException {
        
        Group cgroup = new Group(resultset.getInt("gr_cl_id"), 
                                 resultset.getString("gr_cl_name"), 
                                 resultset.getString("gr_cl_keyword"), 
                                 resultset.getInt("gr_cl_level"));
        
        Client client = new Client(resultset.getInt("cl_id"), 
                                   cgroup, 
                                   resultset.getString("cl_name"), 
                                   resultset.getInt("cl_connections"), 
                                   InetAddress.getByName(resultset.getString("cl_ip")),
                                   resultset.getString("cl_guid"), 
                                   resultset.getString("cl_auth"), 
                                   new DateTime(resultset.getLong("cl_time_add"), this.timezone), 
                                   new DateTime(resultset.getLong("cl_time_edit"), this.timezone));
        
        if (resultset.getObject("ad_id") != null) {
            
            // Penalty issued by an admin
            
            Group agroup = new Group(resultset.getInt("gr_ad_id"), 
                                     resultset.getString("gr_ad_name"), 
                                     resultset.getString("gr_ad_keyword"), 
                                     resultset.getInt("gr_ad_level"));
            
            Client admin = new Client(resultset.getInt("cl_ad"), 
                                      agroup, resultset.getString("ad_name"), 
                                      resultset.getInt("ad_connections"), 
                                      InetAddress.getByName(resultset.getString("ad_ip")),
                                      resultset.getString("ad_guid"), 
                                      resultset.getString("ad_auth"), 
                                      new DateTime(resultset.getLong("ad_time_add"), this.timezone), 
                                      new DateTime(resultset.getLong("ad_time_edit"), this.timezone));
        
            return new Penalty(resultset.getInt("pn_id"), 
                               client, 
                               admin, 
                               PenaltyType.getByName(resultset.getString("pn_type")), 
                               resultset.getBoolean("pn_active"), 
                               resultset.getString("pn_reason"),
                               new DateTime(resultset.getLong("pn_time_add"), this.timezone), 
                               new DateTime(resultset.getLong("pn_time_edit"), this.timezone), 
                               (resultset.getObject("pn_time_expire") != null) ? new DateTime(resultset.getLong("pn_time_expire"), timezone) : null);
        
        } else {
            
            // Penalty automatically issued by Orion
            
            return new Penalty(resultset.getInt("pn_id"), 
                               client, PenaltyType.getByName(resultset.getString("pn_type")), 
                               resultset.getBoolean("pn_active"), 
                               resultset.getString("pn_reason"),
                               new DateTime(resultset.getLong("pn_time_add"), this.timezone), 
                               new DateTime(resultset.getLong("pn_time_edit"), this.timezone), 
                               (resultset.getObject("pn_time_expire") != null) ? new DateTime(resultset.getLong("pn_time_expire"), this.timezone) : null);

        }
     
    }
    
    
    /**
     * Return a <tt>Penalty</tt> object matching the given <tt>ResultSet</tt> tuple
     * 
     * @author Daniele Pantaleone
     * @param  resultset The <tt>ResultSet</tt> object from which to build the Penalty object
     * @param  client The <tt>Client</tt> who suffered the <tt>Penalty</tt>
     * @throws SQLException If the <tt>ResultSet</tt> object is not consistent
     * @throws IndexOutOfBoundsException If we cannot generate a <tt>PenaltyType</tt> object using the <tt>Penalty</tt> type name
     * @throws UnknownHostException If we can't generate an <tt>InetAddress</tt> object using the <tt>Client</tt> IP address
     * @return A <tt>Penalty</tt> object matching the given <tt>ResultSet</tt> tuple
     **/
    private Penalty getObjectFromCursor(ResultSet resultset, Client client) throws SQLException, IndexOutOfBoundsException, UnknownHostException {
        
        if (resultset.getObject("ad_id") != null) {
            
            // Penalty issued by an admin
            
            Group agroup = new Group(resultset.getInt("gr_ad_id"), 
                                     resultset.getString("gr_ad_name"), 
                                     resultset.getString("gr_ad_keyword"), 
                                     resultset.getInt("gr_ad_level"));
            
            Client admin = new Client(resultset.getInt("cl_ad"), 
                                      agroup, 
                                      resultset.getString("ad_name"), 
                                      resultset.getInt("ad_connections"), 
                                      InetAddress.getByName(resultset.getString("ad_ip")),
                                      resultset.getString("ad_guid"), 
                                      resultset.getString("ad_auth"), 
                                      new DateTime(resultset.getLong("ad_time_add"), this.timezone), 
                                      new DateTime(resultset.getLong("ad_time_edit"), this.timezone));
        
            return new Penalty(resultset.getInt("pn_id"), 
                               client, 
                               admin, 
                               PenaltyType.getByName(resultset.getString("pn_type")), 
                               resultset.getBoolean("pn_active"), 
                               resultset.getString("pn_reason"),
                               new DateTime(resultset.getLong("pn_time_add"), this.timezone), 
                               new DateTime(resultset.getLong("pn_time_edit"), this.timezone), 
                               (resultset.getObject("pn_time_expire") != null) ? new DateTime(resultset.getLong("pn_time_expire"), this.timezone) : null);
        
        } else {
            
            // Penalty automatically issued by Orion
            
            return new Penalty(resultset.getInt("pn_id"), 
                               client, 
                               PenaltyType.getByName(resultset.getString("pn_type")), 
                               resultset.getBoolean("pn_active"), 
                               resultset.getString("pn_reason"),
                               new DateTime(resultset.getLong("pn_time_add"), this.timezone), 
                               new DateTime(resultset.getLong("pn_time_edit"), this.timezone), 
                               (resultset.getObject("pn_time_expire") != null) ? new DateTime(resultset.getLong("pn_time_expire"), this.timezone) : null);

        }
     
    }

    
}