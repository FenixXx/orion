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
 * @copyright   Daniele Pantaleone, 19 January, 2013
 * @package     com.orion.control
 **/

package com.orion.control;

import java.net.UnknownHostException;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import com.google.common.base.Joiner;
import com.orion.bot.Orion;
import com.orion.comparator.ClientSlotComparator;
import com.orion.console.Console;
import com.orion.dao.ClientDao;
import com.orion.dao.MySqlClientDao;
import com.orion.domain.Client;
import com.orion.urt.Color;
import com.orion.urt.Team;

public class ClientC {
    
    private final Log log;
    private final Console console;
    private final DateTimeZone timezone;
    private final ClientDao dao;
    
    private List<Client> clients;
    
    
    /**
     * Object constructor
     * 
     * @author Daniele Pantaleone
     * @param  orion <tt>Orion</tt> object reference
     **/
    public ClientC(Orion orion) {
        this.log = orion.log;
        this.console = orion.console;
        this.timezone = orion.timezone;
        this.dao = new MySqlClientDao(orion);
        this.clients = new LinkedList<Client>();
    }
    
    
    /**
     * Return the <tt>Client</tt> object matching the specified <tt>Client</tt> id<br>
     * The search is performed on the online <tt>Client</tt> list at first 
     * and also on the storage layer if necessary
     * 
     * @author Daniele Pantaleone
     * @param  id The <tt>Client</tt> object primary key
     * @throws ClassNotFoundException If the JDBC driver fails in being loaded
     * @throws SQLException If the load query fails somehow
     * @throws UnknownHostException If we can't generate an <tt>InetAddress</tt> object using the <tt>Client</tt> IP address
     * @return A <tt>Client</tt> object matching the given id or <tt>null</tt> if we have no match
     **/
    public Client getById(int id) throws ClassNotFoundException, SQLException, UnknownHostException {
        
        // Searching on the online client list
        for (Client client : this.clients)
            if (client.id == id) 
                return client;
        
        // Loading data from the storage
        return this.dao.loadById(id);
    }
    
    
    /**
     * Return the <tt>Client</tt> object matching the specified slot<br>
     * The search is performed only on the online <tt>Client</tt> list
     * 
     * @author Daniele Pantaleone
     * @param  slot The <tt>Client</tt> object slot value
     * @return A <tt>Client</tt> object matching the given slot or <tt>null</tt> if we have no match
     **/
    public Client getBySlot(Integer slot) {
        
        // Searching on the online client list
        for (Client client : this.clients)
            if ((client.slot) != null && (client.slot.equals(slot))) 
                return client;
        
        return null;
        
    }
    
    
    /**
     * Return a list of <tt>Client</tt> objects matching the specified name<br>
     * This method also handle partial <tt>Client</tt> name matching<br>
     * The search is performed only in the online <tt>Client</tt> list
     * 
     * @author Daniele Pantaleone
     * @param  name The <tt>Client</tt> object name value
     * @return A collection of <tt>Client</tt> objects matching the given name
     **/
    public List<Client> getByName(String name) {
        
        List<Client> collection = new LinkedList<Client>();
        
        // Searching on the online client list
        for (Client client : this.clients)
            if ((client.name != null) && (client.name.toLowerCase().contains(name.toLowerCase())))
                collection.add(client);
        
        return collection;
        
    }
    
    
    /**
     * Return the <tt>Client</tt> object matching the specified pattern<br>
     * The search is performed only on the online <tt>Client</tt> list
     * 
     * @author Daniele Pantaleone
     * @param  client The <tt>Client</tt> who is performing the search
     * @param  pattern The pattern matching the name so search
     * @return The </tt>Client</tt> whose name matches the given pattern 
     **/
    public Client getByMagic(Client client, String pattern) { 
        
        // Don't bother if the pattern is null
        if (pattern == null) return null;
        
        // Searching by slot
        if (pattern.matches("^\\d+$")) {
            
            Client sclient = this.getBySlot(Integer.valueOf(pattern));
            if ((sclient == null) && (client != null)) {
                // No client found for the given slot number
                this.console.tell(client, "No client found in slot " + Color.RED + pattern); 
                return null;
            }
            
            return sclient;
        
        }
        
        // Searching by partial name
        List<Client> collection = this.getByName(pattern);
        
        if (collection.size() == 0) {
   
            // No client found for the matching name pattern
            this.console.tell(client, "No client in found matching " + Color.RED + pattern);
            return null;
            
        } else if (collection.size() > 1) {
            
            // Sorting the client list
            Comparator<Client> comparator = new ClientSlotComparator();
            Collections.sort(collection, comparator);
            
            List<String> list = new LinkedList<String>();
            for (Client c : collection) {
                // Appending all the client details to our collection
                // using the following format: [slot] nickname
                list.add("[" + Color.YELLOW + c.slot + Color.WHITE + "] " + c.name);
            }

            this.console.tell(client, "Do you mean: " + Joiner.on(", ").join(list) + "?");
            return null;
            
        }
        
        // Found just one client
        return collection.get(0);
        
        
    }
    
    
    /**
     * Return the <tt>Client</tt> object matching the specified pattern<br>
     * The search is performed on the online <tt>Client</tt> list at first 
     * and also on the storage layer if necessary (only when the pattern
     * correspond to the <tt>Client</tt> primary key)
     * 
     * @author Daniele Pantaleone
     * @param  client The <tt>Client</tt> who is performing the search
     * @param  pattern The pattern matching the name so search
     * @throws ClassNotFoundException If the JDBC driver fails in being loaded
     * @throws SQLException If the load query fails somehow
     * @throws UnknownHostException If we can't generate an <tt>InetAddress</tt> object using the <tt>Client</tt> IP address
     * @return The </tt>Client</tt> whose name matches the given pattern 
     **/
    public Client getByMagicFull(Client client, String pattern) throws ClassNotFoundException, SQLException, UnknownHostException { 
        
        // Don't bother if the pattern is null
        if (pattern == null) return null;
        
        // Searching by id
        if (pattern.matches("^@\\d+$")) {
            
            Client sclient = this.getById(Integer.parseInt(pattern.substring(1)));
            if ((sclient == null) && (client != null)) {
                // No client found for the given id
                this.console.tell(client, "No client found with id " + Color.RED + pattern); 
                return null;
            }
            
            return sclient;

        }
         
        // Searching by slot
        if (pattern.matches("^\\d+$")) {
            
            Client sclient = this.getBySlot(Integer.valueOf(pattern));
            if ((sclient == null) && (client != null)) {
                // No client found for the given slot number
                this.console.tell(client, "No client found in slot " + Color.RED + pattern); 
                return null;
            }
            
            return sclient;
        
        }
        
        // Searching by partial name
        List<Client> collection = this.getByName(pattern);
        
        if (collection.size() == 0) {
   
            // No client found for the matching name pattern
            this.console.tell(client, "No client in found matching " + Color.RED + pattern);
            return null;
            
        } else if (collection.size() > 1) {
            
            // Sorting the client list
            Comparator<Client> comparator = new ClientSlotComparator();
            Collections.sort(collection, comparator);
            
            List<String> list = new LinkedList<String>();
            for (Client c : collection) {
                // Appending all the client details to our collection
                // using the following format: [slot] nickname
                list.add("[" + Color.YELLOW + c.slot + Color.WHITE + "] " + c.name);
            }

            this.console.tell(client, "Do you mean: " + Joiner.on(", ").join(list) + "?");
            return null;
            
        }
        
        // Found just one client
        return collection.get(0);
        
        
    }
  
    
    /**
     * Return the <tt>Client</tt> object matching the specified <tt>Client</tt> guid<br>
     * The search is performed only on the storage layer
     * 
     * @author Daniele Pantaleone
     * @param  guid The <tt>Client</tt> guid
     * @throws ClassNotFoundException If the JDBC driver fails in being loaded
     * @throws SQLException If the load query fails somehow
     * @throws UnknownHostException If we can't generate an <tt>InetAddress</tt> object using the <tt>Client</tt> IP address
     * @return A <tt>Client</tt> object matching the given guid or <tt>null</tt> if we have no match
     **/
    public Client getByGuid(String guid) throws ClassNotFoundException, SQLException, UnknownHostException {
        // Loading data from the storage
        return this.dao.loadByGuid(guid); 
    }
    
    
    /**
     * Return the <tt>Client</tt> object matching the specified <tt>Client</tt> auth login<br>
     * The search is performed only on the storage layer
     * 
     * @author Daniele Pantaleone
     * @param  auth The <tt>Client</tt> auth login
     * @throws ClassNotFoundException If the JDBC driver fails in being loaded
     * @throws SQLException If the load query fails somehow
     * @throws UnknownHostException If we can't generate an <tt>InetAddress</tt> object using the <tt>Client</tt> IP address
     * @return A <tt>Client</tt> object matching the given auth login or <tt>null</tt> if we have no match
     **/
    public Client getByAuth(String auth) throws ClassNotFoundException, SQLException, UnknownHostException {
        // Loading data from the storage
        return this.dao.loadByAuth(auth);  
    }
    
    
    /**
     * Return a list of <tt>Client</tt> objects whose <tt>Group</tt> level is equal to the given one<br>
     * The search is performed only in the online <tt>Client</tt> list
     * 
     * @author Daniele Pantaleone
     * @param  level The <tt>Group</tt> level to match
     * @return A collection of <tt>Client</tt> objects whose <tt>Group</tt> level is equal to the given one
     **/
    public List<Client> getByGroup(int level) {
        
        List<Client> collection = new LinkedList<Client>();
        
        for (Client client : this.clients)
            if (client.group.level == level)
                collection.add(client);
        
        return collection;
        
    }
    
    
    /**
     * Return a list of <tt>Client</tt> objects whose <tt>Group</tt> keyword is equal to the specified one<br>
     * The search is performed only in the online <tt>Client</tt> list
     * 
     * @author Daniele Pantaleone
     * @param  keyword The <tt>Group</tt> keyword to match
     * @return A collection of <tt>Client</tt> objects whose <tt>Group</tt> keyword is equal to the specified one
     **/
    public List<Client> getByGroup(String keyword) {
        
        List<Client> collection = new LinkedList<Client>();
        
        for (Client client : this.clients)
            if (client.group.keyword.equals(keyword))
                collection.add(client);
        
        return collection;
        
    }
    
    
    /**
     * Return a list of <tt>Client</tt> objects whose <tt>Group</tt> level is equal to the given one<br>
     * <b>THE SEARCH IS PERFORMED ONLY ON THE STORAGE LAYER</b>
     * 
     * @author Daniele Pantaleone
     * @param  level The <tt>Group</tt> level to match
     * @throws ClassNotFoundException If the JDBC driver fails in being loaded
     * @throws SQLException If the load query fails somehow
     * @throws UnknownHostException If we can't generate an <tt>InetAddress</tt> object using the <tt>Client</tt> IP address
     * @return A collection of <tt>Client</tt> objects whose <tt>Group</tt> level is equal to the given one
     **/
    public List<Client> getByGroupFull(int level) throws ClassNotFoundException, UnknownHostException, SQLException {
        return this.dao.loadByGroup(level);
        
    }
    
    
    /**
     * Return a list of <tt>Client</tt> objects whose <tt>Group</tt> keyword is equal to the specified one<br>
     * <b>THE SEARCH IS PERFORMED ONLY ON THE STORAGE LAYER</b>
     * 
     * @author Daniele Pantaleone
     * @param  keyword The <tt>Group</tt> keyword to match
     * @throws ClassNotFoundException If the JDBC driver fails in being loaded
     * @throws SQLException If the load query fails somehow
     * @throws UnknownHostException If we can't generate an <tt>InetAddress</tt> object using the <tt>Client</tt> IP address 
     * @return A collection of <tt>Client</tt> objects whose <tt>Group</tt> keyword is equal to the specified one
     **/
    public List<Client> getByGroupFull(String keyword) throws ClassNotFoundException, UnknownHostException, SQLException {
    	return this.dao.loadByGroup(keyword);
    }
    
    
    /**
     * Return a list of <tt>Client</tt> objects whose <tt>Group</tt> level is equal/higher than the given one<br>
     * The search is performed only in the online <tt>Client</tt> list
     * 
     * @author Daniele Pantaleone
     * @param  minLevel The minimum <tt>Group</tt> level to be added in the collection
     * @return A collection of <tt>Client</tt> objects whose <tt>Group</tt> level is equal/higher than the given one
     **/
    public List<Client> getByMinGroup(int minLevel) {
        
        List<Client> collection = new LinkedList<Client>();
        
        for (Client client : this.clients)
            if (client.group.level >= minLevel)
                collection.add(client);
        
        return collection;
        
    }
    
    
    /**
     * Return a <tt>List</tt> containing all the online clients
     * 
     * @author Daniele Pantaleone
     * @return A <tt>List</tt> containing all the online clients
     **/
    public List<Client> getList() {
        return this.clients;
    }
    
    
    /**
     * Return a <tt>List</tt> of online clients that are playing in the RED team
     * 
     * @author Daniele Pantaleone
     * @return A <tt>List</tt> of online clients that are playing in the RED team
     **/
    public List<Client> getRedTeamList() {
        
        List<Client> collection = new LinkedList<Client>();
        
        for (Client client : this.clients)
            if (client.team == Team.RED)
                collection.add(client);
        
        return collection;
        
    }
    
    
    /**
     * Return a <tt>List</tt> of online clients that are playing in the BLUE team
     * 
     * @author Daniele Pantaleone
     * @return A <tt>List</tt> of online clients that are playing in the BLUE team
     **/
    public List<Client> getBlueTeamList() {
        
        List<Client> collection = new LinkedList<Client>();
        
        for (Client client : this.clients)
            if (client.team == Team.BLUE)
                collection.add(client);
        
        return collection;
        
    }
    
    
    /**
     * Return a <tt>List</tt> of online client that are in the SPECTATOR team
     * 
     * @author Daniele Pantaleone
     * @return A <tt>List</tt> of online client that are in the SPECTATOR team
     **/
    public List<Client> getSpectatorList() {
        
        List<Client> collection = new LinkedList<Client>();
        
        for (Client client : this.clients)
            if (client.team == Team.SPECTATOR)
                collection.add(client);
        
        return collection;
        
    }
    
    
    /**
     * Return and remove a <tt>Client</tt> object from the online client <tt>List</tt>
     * 
     * @author Daniele Pantaleone
     * @param  id The id of the <tt>Client</tt> to be removed
     * @throws UnsupportedOperationException If the remove operation is not supported by this <tt>List</tt> 
     * @return The <tt>Client</tt> object matching the given id or <tt>null</tt> if we have no match
     **/
    public Client removeById(int id) throws UnsupportedOperationException {
        
        Client client = null;
        Iterator<Client> i = this.clients.iterator();
        
        while (i.hasNext()) { 
            client = i.next();
            if (client.id == id) {
                i.remove();
                return client;
            }
        }
        
        return null;
    
    }
    
    
    /**
     * Return and remove a <tt>Client</tt> object from the online client <tt>List</tt>
     * 
     * @author Daniele Pantaleone
     * @param  slot The slot of the <tt>Client</tt> to be removed
     * @throws UnsupportedOperationException If the remove operation is not supported by this <tt>List</tt> 
     * @return The <tt>Client</tt> object matching the given slot or <tt>null</tt> if we have no match
     **/
    public Client removeBySlot(int slot) {
        
        Client client = null;
        Iterator<Client> i = this.clients.iterator();
        
        while (i.hasNext()) {
            client = i.next();
            if (client.slot == slot) {
                i.remove();
                return client;
            }
        }
        
        return null;
        
    }
    
    
    /**
     * Add a new <tt>Client</tt> object to the online client <tt>List</tt>
     * 
     * @author Daniele Pantaleone
     * @param  client The <tt>Client</tt> object to be added to the online client <tt>List</tt>
     **/
    public void add(Client client) {
        this.clients.add(client);
    }
    
    
    /**
     * Clear the online client <tt>List</tt>
     * 
     * @author Daniele Pantaleone
     **/
    public void clear() {
        this.clients.clear();
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
        
        if (!client.bot) {
        	DateTime date = new DateTime(this.timezone);
            client.time_add  = date;
            client.time_edit = date;
            this.log.trace("[SQL] INSERT `clients`: " + client.toString());
            this.dao.insert(client);
        }
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
        
        if (!client.bot) {
            client.time_edit = new DateTime(this.timezone);
            this.log.trace("[SQL] UPDATE `clients`: " + client.toString());
            this.dao.update(client);
        }
    
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
        
        if (!client.bot) {
            this.log.trace("[SQL] DELETE `clients`: " + client.toString());
            this.dao.delete(client);
        }
    
    }
    
    
    /**
     * Save the <tt>Client</tt> object in the database
     *
     * @author Daniele Pantaleone
     * @param  client The <tt>Client</tt> object to be saved in the database
     * @throws ClassNotFoundException If the JDBC driver fails in being loaded
     * @throws SQLException If the load query fails somehow
     **/
    public void save(Client client) throws ClassNotFoundException, SQLException { 
        if (client.id > 0) { this.update(client); } 
        else { this.insert(client); }
    }

}
