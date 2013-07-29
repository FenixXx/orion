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

import net.goreclan.rcon.RconException;

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

public class ClientCtl {
    
    private final Log log;
    private final Console console;
    private final DateTimeZone timezone;
    private final ClientDao dao;
    
    private List<Client> clients;
    
    private static final String P_ID = "^@\\d+$";
    private static final String P_NUMBER = "^\\d+$";
    
    
    /**
     * Object constructor
     * 
     * @author Daniele Pantaleone
     * @param  orion <tt>Orion</tt> object reference
     **/
    public ClientCtl(Orion orion) {
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
            if (client.getId() == id) 
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
            if ((client.getSlot()) != null && (client.getSlot().equals(slot))) 
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
            if ((client.getName() != null) && (client.getName().toLowerCase().contains(name.toLowerCase())))
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
     * @throws RconException If we didn't manage to find a single match and 
     *         we were not able to warn the client using the game chat
     * @return The </tt>Client</tt> whose name matches the given pattern 
     **/
    public Client getByMagic(Client client, String pattern) throws RconException { 
        
        if (pattern == null) 
            return null;
        
        // Searching by slot
        if (pattern.matches(P_NUMBER)) {
            
            Client sclient = this.getBySlot(Integer.valueOf(pattern));
            if ((sclient == null) && (client != null)) {
                // No client found for the given slot number
                this.console.tell(client, "Could not find client in slot " + Color.RED + pattern); 
                return null;
            }
            
            return sclient;
        
        }
        
        // Searching by partial name
        List<Client> collection = this.getByName(pattern);
        
        if (collection.size() == 0) {
   
            // No client found for the matching name pattern
            this.console.tell(client, "Could not find client matching " + Color.RED + pattern);
            return null;
            
        } else if (collection.size() > 1) {
            
            // Sorting the client list
            Comparator<Client> comparator = new ClientSlotComparator();
            Collections.sort(collection, comparator);
            
            List<String> list = new LinkedList<String>();
            for (Client c : collection) {
                // Appending all the client details to our collection
                // using the following format: [slot] nickname
                list.add("[" + Color.YELLOW + c.getSlot() + Color.WHITE + "] " + c.getName());
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
     * @throws UnknownHostException If we can't generate an <tt>InetAddress</tt> 
     *         object using the <tt>Client</tt> IP address
     * @throws RconException If we didn't manage to find a single match and 
     *         we were not able to warn the client using the game chat
     * @return The </tt>Client</tt> whose name matches the given pattern 
     **/
    public Client getByMagicDB(Client client, String pattern) throws ClassNotFoundException, SQLException, UnknownHostException, RconException { 
        
        // Don't bother if the pattern is null
        if (pattern == null) return null;
        
        // Searching by id
        if (pattern.matches(P_ID)) {
            
            Client sclient = this.getById(Integer.parseInt(pattern.substring(1)));
            if ((sclient == null) && (client != null)) {
                // No client found for the given id
                this.console.tell(client, "Could not find client with id " + Color.RED + pattern); 
                return null;
            }
            
            return sclient;

        }
         
        // Searching by slot
        if (pattern.matches(P_NUMBER)) {
            
            Client sclient = this.getBySlot(Integer.valueOf(pattern));
            if ((sclient == null) && (client != null)) {
                // No client found for the given slot number
                this.console.tell(client, "Could not find client in slot " + Color.RED + pattern); 
                return null;
            }
            
            return sclient;
        
        }
        
        // Searching by partial name
        List<Client> collection = this.getByName(pattern);
        
        if (collection.size() == 0) {
   
            // No client found for the matching name pattern
            this.console.tell(client, "Could not find client matching " + Color.RED + pattern);
            return null;
            
        } else if (collection.size() > 1) {
            
            // Sorting the client list
            Comparator<Client> comparator = new ClientSlotComparator();
            Collections.sort(collection, comparator);
            
            List<String> list = new LinkedList<String>();
            for (Client c : collection) {
                // Appending all the client details to our collection
                // using the following format: [slot] nickname
                list.add("[" + Color.YELLOW + c.getSlot() + Color.WHITE + "] " + c.getName());
            }

            this.console.tell(client, "Do you mean: " + Joiner.on(", ").join(list) + "?");
            return null;
            
        }
        
        // Found just one client
        return collection.get(0);
        
        
    }
  
    
    /**
     * Return the <tt>Client</tt> object matching the specified <tt>Client</tt> GUID<br>
     * The search is performed only on the storage layer
     * 
     * @author Daniele Pantaleone
     * @param  guid The <tt>Client</tt> GUID
     * @throws ClassNotFoundException If the JDBC driver fails in being loaded
     * @throws SQLException If the load query fails somehow
     * @throws UnknownHostException If we can't generate an <tt>InetAddress</tt> object using the <tt>Client</tt> IP address
     * @return A <tt>Client</tt> object matching the given GUID or <tt>null</tt> if we have no match
     **/
    public Client getByGuid(String guid) throws ClassNotFoundException, SQLException, UnknownHostException {
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
            if (client.getGroup().getLevel() == level)
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
            if (client.getGroup().getKeyword().equals(keyword))
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
    public List<Client> getByGroupDB(int level) throws ClassNotFoundException, UnknownHostException, SQLException {
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
    public List<Client> getByGroupDB(String keyword) throws ClassNotFoundException, UnknownHostException, SQLException {
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
            if (client.getGroup().getLevel() >= minLevel)
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
            if (client.getTeam() == Team.RED)
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
            if (client.getTeam() == Team.BLUE)
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
            if (client.getTeam() == Team.SPECTATOR)
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
            if (client.getId() == id) {
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
            if (client.getSlot() == slot) {
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
        
        if (!client.isBot()) {
        	DateTime date = new DateTime(this.timezone);
            client.setTimeAdd(date);
            client.setTimeEdit(date);
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
        
        if (!client.isBot()) {
            client.setTimeEdit(new DateTime(this.timezone));
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
        
        if (!client.isBot()) {
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
        if (client.getId() > 0) { this.update(client); } 
        else { this.insert(client); }
    }

}
