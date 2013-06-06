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
 * @version     1.1
 * @copyright   Daniele Pantaleone, 10 February, 2013
 * @package     com.orion.console
 **/

package com.orion.console;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;

import com.orion.bot.Orion;
import com.orion.command.Command;
import com.orion.domain.Client;
import com.orion.exception.RconException;
import com.orion.urt.Color;
import com.orion.urt.Game;
import com.orion.urt.Team;
import com.orion.utility.BooleanParser;
import com.orion.utility.Rcon;
import com.orion.utility.Splitter;

public class UrT41Console implements Console {
    
	protected static int MAX_SAY_STRLEN = 62;
	
    protected final Log log;
    protected final Rcon rcon;
    protected Game game;
    
 
    /**
     * Object constructor
     * 
     * @author Daniele Pantaleone 
     * @param  address The remote server address
     * @param  port The virtual port on which the server is accepting connections
     * @param  password The server RCON password
     * @param  orion Orion object reference
     * @throws UnknownHostException If the IP address of a host could not be determined
     * @throws RconException If the RCON utility object fails in being initialized
     **/
    public UrT41Console(String address, int port, String password, Orion orion) throws UnknownHostException, RconException {
        
    	this.rcon = new Rcon(address, port, password, orion);
        this.game = orion.game;
        this.log = orion.log;
        
        // Printing a log message so the user can check if he's using the correct console
        this.log.debug("Console initialized: com.orion.console." + orion.config.getString("orion", "game") + "Console");
        
        // Retrieve Urban Terror 4.1/4.2 specific CVARs
        this.game.setFsGame(this.getCvar("fs_game"));
        this.game.setFsBasePath(this.getCvar("fs_basepath"));
        this.game.setFsHomePath(this.getCvar("fs_homepath"));
        
    }
    
    
    /**
     * Ban a client from the server using the FS Auth System
     * 
     * @author Daniele Pantaleone
     * @param  client The client to be banned
     * @param  days The number of days of the ban
     * @param  hours The number of hours of the ban
     * @param  mins The number of minutes of the ban
     * @throws UnsupportedOperationException If the RCON command is not supported by the console implementation
     **/
    public void authban(Client client, int days, int hours, int mins) throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Unable to execute auth-ban RCON command: such command is only available as from Urban Terror 4.2 version");
    }
    
    
    /**
     * Ban a client from the server using the FS Auth System
     * 
     * @author Daniele Pantaleone
     * @param  slot The slot of the client to be banned
     * @param  days The number of days of the ban
     * @param  hours The number of hours of the ban
     * @param  mins The number of minutes of the ban
     * @throws UnsupportedOperationException If the RCON command is not supported by the console implementation
     **/
    public void authban(int slot, int days, int hours, int mins) throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Unable to execute auth-ban RCON command: such command is only available as from Urban Terror 4.2 version");
    }
    
    
    /**
     * Fetch FS Auth System informations for the specified client
     * 
     * @author Daniele Pantaleone
     * @param  client The client whose informations needs to be retrieved
     * @throws UnsupportedOperationException If the RCON command is not supported by the console implementation
     * @return A Map<String, String> containing the auth-whois command result
     **/
    public Map<String, String> authwhois(Client client) throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Unable to execute auth-whois RCON command: such command is only available as from Urban Terror 4.2 version");
    }
    
    
    /**
     * Fetch FS Auth System informations for the specified client
     * 
     * @author Daniele Pantaleone
     * @param  slot The slot of the client whose informations needs to be retrieved
     * @throws UnsupportedOperationException If the RCON command is not supported by the console implementation
     * @return A Map<String, String> containing the auth-whois command result
     **/
    public Map<String, String> authwhois(int slot) throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Unable to execute auth-whois RCON command: such command is only available as from Urban Terror 4.2 version");
    }
    
    
    /**
     * Ban a player from the server permanently
     * 
     * @author Daniele Pantaleone
     * @param  client The client to ban from the server
     **/
    public void ban(Client client) {
        this.rcon.sendNoRead("addip " + client.getIp().getHostAddress());
    }
    
    
    /**
     * Ban an ip address from the server permanently
     * 
     * @author Daniele Pantaleone
     * @param  ip The IP address to ban from the server
     **/
    public void ban(String ip) {
        this.rcon.sendNoRead("addip " + ip);
    }
    
    
    /**
     * Write a bold message in the middle of the screen of all players
     * 
     * @author Daniele Pantaleone
     * @param  message The message to be printed
     **/
    public void bigtext(String message) {
    	
    	if (message.length() > MAX_SAY_STRLEN) {
    		
    		// Splitting the message into multiple sentences
    		// In this way it won't overflow the game chat and it will print nicer
    		List<String> collection = Splitter.split(message, MAX_SAY_STRLEN);
    		
    		for (String sentence: collection) {
    			// Printing separate sentences. We'll also introduce a sleep
    			// in between the messages since the a bigtext overlap a previous
    			// printed message with a new one. It would be unreadable.
    			sentence = sentence.trim();
    			this.rcon.sendNoRead("bigtext \"" + Color.WHITE + sentence + "\"");
    		
				try { Thread.sleep(2000); } 
				catch (InterruptedException e) {
					this.log.error(e);
				}
				
    		}
    		
    	} else {
    		
    		// Normal bigtext
    		this.rcon.sendNoRead("bigtext \"" + Color.WHITE + message + "\"");
    		
    	}
    	
    }
    
    
    /**
     * Broadcast a message in the top-left screen
     * 
     * @author Daniele Pantaleone
     * @param  message The message to be sent
     **/
    public void broadcast(String message) {
    	
    	if (message.length() > MAX_SAY_STRLEN) {
    		
    		// Splitting the message into multiple sentences
    		// In this way it won't overflow the game chat and it will print nicer
    		List<String> collection = Splitter.split(message, MAX_SAY_STRLEN);
    		
    		for (String sentence: collection) {
    		
    			// Sending the message
    			sentence = sentence.trim();
    			this.rcon.sendNoRead(Color.WHITE + sentence);
    			
    			try { Thread.sleep(1000); } 
				catch (InterruptedException e) {
					this.log.error(e);
				}
    			
    		}
    		
    	} else {
    		
    		// Normal broadcast.
    		this.rcon.sendNoRead(Color.WHITE + message);
    		
    	}
    	
    }
    
    
    /**
     * Cycle the current map on the server
     * 
     * @author Daniele Pantaleone
     **/
    public void cyclemap() {
        this.rcon.sendNoRead("cyclemap");
    }
    
    
    /**
     * Dump user information for the specified client
     * 
     * @author Daniele Pantaleone
     * @param  client The client you want to retrieve informations
     * @return A Map<String,String> with the dumped result or return null if the player is not connected anymore
     **/
    public Map<String, String> dumpuser(Client client) {
        
        String result = this.rcon.sendRead("dumpuser " + client.getSlot());
        
        if (result == null) {
            this.log.debug("Unable to parse dumpuser command response for client " + client.getSlot() + ": RCON UDP socket returned NULL");
            return null;
        }
        
        // This is the string we expect from the /rcon dumpuser <slot> command.
        // We need to parse it and build an HashMap containing the client data.
        //
        // userinfo
        // --------
        // ip                  93.40.100.128:59685
        // gear                GZJATWA
        // rate                25000
        // name                [FS]Fenix
        // racered             2
        
        Map<String, String> map = new LinkedHashMap<String, String>();
        Pattern pattern = Pattern.compile("^\\s*(?<key>\\w+)\\s+(?<value>.*)$");
        String[] lines = result.split("\n");
        
        for (String line: lines) {
            Matcher m = pattern.matcher(line);
            if (m.matches()) map.put(m.group("key"), m.group("value"));
        }
        
        if (map.size() == 0) return null;
        return map;
        
    }
    
    
    /**
     * Dump user information for the specified player slot.
     * 
     * @author Daniele Pantaleone
     * @param  slot The player slot on which perform the dumpuser command
     * @return A Map<String,String> with the dumped result or return null if the player is not connected anymore
     **/
    public Map<String, String> dumpuser(int slot) {
        
        String result = this.rcon.sendRead("dumpuser " + slot);
        
        if (result == null) {
            this.log.debug("Unable to parse dumpuser command response for client " + slot + ": RCON UDP socket returned NULL");
            return null;
        }
        
        // This is the string we expect from the /rcon dumpuser <slot> command.
        // We need to parse it and build an HashMap containing the client data.
        //
        // userinfo
        // --------
        // ip                  93.40.100.128:59685
        // gear                GZJATWA
        // rate                25000
        // name                [FS]Fenix
        // racered             2
        
        Map<String, String> map = new LinkedHashMap<String, String>();
        Pattern pattern = Pattern.compile("^\\s*(?<key>\\w+)\\s+(?<value>.*)$");
        String[] lines = result.split("\n");
        
        for (String line: lines) {
            Matcher m = pattern.matcher(line);
            if (m.matches()) map.put(m.group("key"), m.group("value"));
        }
        
        if (map.size() == 0) return null;
        return map;
        
    }
        
    
    /**
     * Force a player in the blue team
     * 
     * @author Daniele Pantaleone
     * @param  client The client who is going to be forced in the blue team
     
     **/
    public void forceblue(Client client) {
        // Do not execute if the client is already in the specified team
        // This will prevent to overflow the server with RCON commands
        if (client.getTeam() != Team.BLUE)
            this.rcon.sendNoRead("forceteam " + client.getSlot() + " blue");
    }
    
    
    /**
     * Force a player in the blue team
     * 
     * @author Daniele Pantaleone
     * @param  slot The slot of the player who is going to be forced in the blue team
     
     **/
    public void forceblue(int slot) {
        // Since we do not have a Client object as input, we cannot match the current
        // client team. The RCON command is going to be executed anyway
        // NOTE: Use the previous version of the command if possible
        this.rcon.sendNoRead("forceteam " + slot + " blue");
    }
    
    
    /**
     * Force a player in the free team (autojoin)
     *
     * @author Daniele Pantaleone
     * @param  client The client who is going to be forced
     **/
    public void forcefree(Client client) {
        this.rcon.sendNoRead("forceteam " + client.getSlot() + " free");
    }
    
    
    /**
     * Force a player in the free team (autojoin)
     * 
     * @author Daniele Pantaleone
     * @param  slot The slot of the player who is going to be forced
     **/
    public void forcefree(int slot) {
        this.rcon.sendNoRead("forceteam " + slot + " free");
    }
    
    
    /**
     * Force a player in the red team
     * 
     * @author Daniele Pantaleone
     * @param  client The client who is going to be forced in the red team
     **/
    public void forcered(Client client) {
        // Do not execute if the client is already in the specified team
        // This will prevent to overflow the server with RCON commands
        if (client.getTeam() != Team.RED)
            this.rcon.sendNoRead("forceteam " + client.getSlot() + " red");
    }
    
    
    /**
     * Force a player in the red team
     * 
     * @author Daniele Pantaleone
     * @param  slot The slot of the player who is going to be forced in the red team
     **/
    public void forcered(int slot) {
        // Since we do not have a Client object as input, we cannot match the current
        // client team. The RCON command is going to be executed anyway
        // NOTE: Use the previous version of the command if possible
        this.rcon.sendNoRead("forceteam " + slot + " red");
    }
    
    
    /**
     * Force a player in the spectator team
     * 
     * @author Daniele Pantaleone
     * @param  client The client who is going to be forced in the spectators team
     **/
    public void forcespec(Client client) {
        // Do not execute if the client is already in the specified team
        // This will prevent to overflow the server with RCON commands
        if (client.getTeam() != Team.SPECTATOR)
            this.rcon.sendNoRead("forceteam " + client.getSlot() + " spectator");
    }
    
    
    /**
     * Force a player in the spectator team
     * 
     * @author Daniele Pantaleone
     * @param  slot The slot of the player who is going to be forced in the spectators team
     **/
    public void forcespec(int slot) {
        // Since we do not have a Client object as input, we cannot match the current
        // client team. The RCON command is going to be executed anyway
        // NOTE: Use the previous version of the command if possible
        this.rcon.sendNoRead("forceteam " + slot + " spectator");
    }
    
    
    /**
     * Force a player in the specified team
     *
     * @author Daniele Pantaleone
     * @param  client The client who is going to be forced
     * @param  team The team where to force the player in
     **/
    public void forceteam(Client client, Team team) {
        
        switch (team) {
            
            case RED:
                this.forcered(client);
                break;
                
            case BLUE:
                this.forceblue(client);
                break;
                
            case SPECTATOR:
                this.forcespec(client);
                break;
            
            case FREE:
                this.forcefree(client);
                break;
        
        }
        
    }
    
    
    /**
     * Force a player in the specified team
     *
     * @author Daniele Pantaleone
     * @param  slot The slot of the player who is going to be forced
     * @param  team The team where to force the player in
     **/
    public void forceteam(int slot, Team team) {
        
        switch (team) {
        
            case RED:
                this.forcered(slot);
                break;
                
            case BLUE:
                this.forceblue(slot);
                break;
                
            case SPECTATOR:
                this.forcespec(slot);
                break;
            
            case FREE:
                this.forcefree(slot);
                break;
                
        }
    
    }
        
    
    /**
     * Return a CVAR value
     * 
     * @author Daniele Pantaleone
     * @param  name The CVAR name
     * @return The CVAR value as a <tt>String</tt>
     **/
    public String getCvar(String name) {
        
        String value;
        String result = this.rcon.sendRead(name);
        
        if (result == null) {
            this.log.debug("Unable to retrieve CVAR value [ cvar : " + name + " ]: RCON UDP socket returned NULL");
            return null;
        }
        
        Pattern pattern = Pattern.compile("\\s*\\\"[\\w+]*\\\"\\sis:\\\"(?<value>[\\w:\\.\\-\\\\/]*)\\\".*", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(result);
        
        if (matcher.matches()) {
            
        	value = matcher.group("value");
            if (value.trim().isEmpty()) { 
                this.log.debug("Unable to retrieve CVAR value [ cvar : " + name + " ]: CVAR is not set");
            	return null;
            }
            
            this.log.trace("Retrieved CVAR [ " + name + " : " + value + " ]");
            return value;
            
        }

        return null;
        
    }
    
    
    /**
     * Return a CVAR value converted into the specified <tt>Object</tt> class
     * 
     * @author Daniele Pantaleone
     * @param  name The CVAR name
     * @param  c The <tt>Class</tt> into which convert the CVAR value
     * @return The CVAR value parsed according to the input <tt>Class</tt> given
     **/
    @SuppressWarnings("unchecked")
    public <V> V getCvar(String name, Class<V> c) {
        
    	String value = this.getCvar(name);
    	
        try {
            
            if (c == String.class) {
                return (V)value;
            } else if ((c == Integer.class) || (c == int.class)) {
                return (V)Integer.valueOf(value);
            } else if ((c == Double.class) || (c == double.class)) {
                return (V)Double.valueOf(value);
            } else if ((c == Float.class) || (c == float.class)) {
                return (V)Float.valueOf(value);
            } else if ((c == Short.class) || (c == short.class)) {
                return (V)Short.valueOf(value);
            } else if ((c == Long.class) || (c == long.class)) {
                return (V)Long.valueOf(value);
            } else if ((c == Boolean.class) || (c == boolean.class)) {
                return (V)BooleanParser.valueOf(value);
            }
            
            return null;
        
        } catch (NumberFormatException e) {
        	
        	// Log the Exception
            this.log.warn("Unable to convert CVAR value to " + c.getCanonicalName() + " [ cvar : " + name + " | value : " + value + " ]");
            return null;
        
        }
        
    } 
    
        
    /**
     * Return the current map name
     * 
     * @author Daniele Pantaleone
     * @return The current map name 
     **/
    public String getMap() {
        return this.getCvar("mapname");
    }
    
    
    /**
     * Return a list of available maps.
     * 
     * @author Daniele Pantaleone
     * @return A list of all the maps available on the server
     **/
    public List<String> getMapList() {
        
        String result = this.rcon.sendRead("fdir *.bsp");
        
        if (result == null) {
            this.log.debug("Unable to retrieve map list: RCON UDP socket returned NULL");
            return null;
        }
        
        List<String> maplist = new LinkedList<String>();
        Pattern pattern = Pattern.compile("^*maps/(?<mapname>.*).bsp$");
        
        String[] lines = result.split("\n");

        for (String line: lines) {
            Matcher matcher = pattern.matcher(line);
            if (matcher.matches()) 
                maplist.add(matcher.group("mapname"));
        }
        
        return maplist;
    }
    
    
    /**
     * Return a <tt>List</tt> of maps matching the given search key
     * 
     * @author Daniele Pantaleone
     * @param  search The map name search <tt>String</tt>
     * @return A <tt>List</tt> of maps matching the given search key
     **/
    public List<String> getMapSoundingLike(String search) {
    	
    	List<String> collection = new LinkedList<String>();
    	
    	// Trimming and making lower case the search key
    	search = search.toLowerCase().trim();
    	
    	// Server map list not computed yet. Build the map list
    	if ((this.game.getMapList() == null) || (this.game.getMapList().size() == 0))
    		this.game.setMapList(this.getMapList());
    	
    	// We were not able to retrieve the server map list
    	if ((this.game.getMapList() == null) || (this.game.getMapList().size() == 0))
    		return null;
    	
    	for (String map : this.game.getMapList())
            if ((map != null) && (map.toLowerCase().contains(search.toLowerCase())))
                collection.add(map);
    	
    	return collection;
    	
    }
    
    
    /**
     * Return the name of the nextmap set on the server<br>
     * Will return <tt>null</tt> if the operation doesn't succeed
     * 
     * @author Daniele Pantaleone
     * @return The name of the nextmap set on the server or <tt>null</tt> if the operation doesn't succeed
     **/
    public String getNextMap() {
        
    	String path = null;
        String line = null;
        String firstmap = null;
        String tmpmap = null;
        List<String> maplist = null;
        RandomAccessFile mapfile = null;
        
        // Checking if the nextmap on the server has been
        // manually change using an RCON command or after a vote
        String nextmap = this.getCvar("g_nextmap");
        if (nextmap != null) 
            return nextmap;
        
        // Checking if the current map name has been
        // saved in the Game object reference on map load
        if (this.game.getMapName() == null) {
            this.game.setMapName(this.getMap());
            if (this.game.getMapName() == null) 
                return null;
        }
        
        // Checking if the g_mapcycle cvar has been
        // already retrieved from the server
        if (this.game.getMapcycle() == null) {
            this.game.setMapcycle(this.getCvar("g_mapcycle"));
            if (this.game.getMapcycle() == null) {
                return null;
            }
        }
        
        // Checking if fs_game has been initialized
        if (this.game.getFsGame() == null) {
            this.game.setFsGame(this.getCvar("fs_game"));
            if (this.game.getFsGame() == null) {
                return null;
            }
        }
        
        // Checking if fs_basepath has been initialized
        if (this.game.getFsBasePath() == null) {
            this.game.setFsBasePath(this.getCvar("fs_basepath"));
            if (this.game.getFsBasePath() == null) {
                return null;
            }
        }
        
        // Building the mapcycle file absolute path
        path = this.game.getFsBasePath() + 
               System.getProperty("file.separator") + 
               this.game.getFsGame() + 
               System.getProperty("file.separator") + 
               this.game.getMapcycle();
        
        try {
            
            // Opening the mapcycle file under fs_basepath
            mapfile = new RandomAccessFile(path, "r");
            
        } catch (FileNotFoundException e) {
            
            // Mapcycle file is not located under fs_basepath. Trying fs_homepath.
            this.log.debug("Unable to locate mapcycle file [ path : " + path + " ]");
            
            // Checking if fs_homepath has been initialized.
            if (this.game.getFsHomePath() == null) {
                this.game.setFsHomePath(this.getCvar("fs_homepath"));
                if (this.game.getFsHomePath() == null) {
                    return null;
                }
            }
            
            // Building the mapcycle file absolute path
            path = this.game.getFsHomePath() + 
                   System.getProperty("file.separator") + 
                   this.game.getFsGame() + 
                   System.getProperty("file.separator") + 
                   this.game.getMapcycle();
            
            try {
                
                // Opening the mapcycle file under fs_basepath
                mapfile = new RandomAccessFile(path, "r");
                
            } catch (FileNotFoundException e1) {
                
                // Mapcycle file is not located under fs_homepath either.
                this.log.debug("Unable to locate mapcycle file [ path : " + path + " ]");
                this.log.error("Mapcycle file missing: unable to retrieve nextmap name");
                return null;
                
            }
            
        }
        
        try {
            
            // Copying the content of the mapcycle file
            // into a LinkedList for later scrolling
            maplist = new LinkedList<String>();
            while ((line = mapfile.readLine()) != null) {
                maplist.add(line);
            }
            
            try {
                
                // Closing the file
                mapfile.close();
                
            } catch (IOException e) {
                // Logging the exception...this is very rare!
                this.log.error("Unable to close mapcycle file handler", e);
            }
            
        } catch (IOException e1) {
            this.log.error("Unable to read mapcycle file", e1);
            return null;
        }
        
        // Mapcycle file empty
        if (maplist.size() == 0) {
            this.log.warn("Mapcycle file is empty [ path : " + path + " ]");
            return null;
        }
        
        // Copying the 1st map name here it as our nextmap if we do
        // not manage to find a proper match for the current map name
        firstmap = maplist.get(0);
        
        try {
            
            tmpmap = maplist.remove(0);
            while (!this.game.getMapName().equals(tmpmap)) {
                tmpmap = maplist.remove(0);
            }
            
            if (this.game.getMapName().equals(tmpmap)) {
                
                if (maplist.size() > 0) {
                    // We still have 1 or more maps after
                    // the current one in the mapcycle file
                    return maplist.get(0);
                } else {
                    // Mapcycle is restarting from the beginning
                    // We are playing the last map in the file
                    // or we are playing a map that is not listed
                    // in the mapcycle file
                    return firstmap;
                }
            }
            
        } catch(IndexOutOfBoundsException e) {
            return firstmap;
        }
        
        this.log.warn("Unable to compute nextmap name....giving up!");
        return null;
        
    }
    
 
    /**
     * Return an List containing the result of the "/rcon players" command
     * 
     * @author Daniele Pantaleone
     * @return A list containing players informations
     **/
    public List<List<String>> getPlayers() {
        
        String result = this.rcon.sendRead("players");
        
        if (result == null) {
            this.log.debug("Unable to parse players command response: RCON UDP socket returned NULL");
            return null;
        }
        
        // This is the string we expect from the /rcon players command
        // We need to parse it and build an Array with players informations
        //
        // Map: ut4_casa
        // Players: 1
        // Score: R:0 B:0
        // 0:  [FS]Fenix  SPECTATOR  k:0  d:0  ping:50  62.75.235.91:27960
        
        List<List<String>> collection = new LinkedList<List<String>>();
        Pattern pattern = Pattern.compile("^\\s*(?<slot>\\d+):\\s+(?<name>.*)\\s+(?<team>RED|BLUE|SPECTATOR|FREE)\\s+k:(?<kills>\\d+)\\s+d:(?<deaths>\\d+)\\s+ping:(?<ping>\\d+|CNCT|ZMBI)\\s*([?<address>\\d.]+):([?<port>\\d-]+)?$", Pattern.CASE_INSENSITIVE);
        String[] lines = result.split("\n");
        
        for (String line: lines) {
            
            Matcher matcher = pattern.matcher(line);
            
            if (matcher.matches()) {
                
                List<String> x = new ArrayList<String>();
                x.add(matcher.group("slot"));
                x.add(matcher.group("name"));
                x.add(matcher.group("team"));
                x.add(matcher.group("kills"));
                x.add(matcher.group("deaths"));
                x.add(matcher.group("ping"));
                x.add(matcher.group("address"));
                x.add(matcher.group("port"));
                collection.add(x);
                
            }
        }
            
        return collection;
        
    }
    
    
    /**
     * Return an List containing the result of the "/rcon status" command
     * 
     * @author Daniele Pantaleone
     * @return A list containing status informations
     **/
    public List<List<String>> getStatus() {
        
        String result = this.rcon.sendRead("status");
        
        if (result == null) {
            this.log.debug("Unable to status command response: RCON UDP socket returned NULL");
            return null;
        }
        
        // This is the string we expect from the /rcon status command
        // We need to parse it and build an Array with players informations
        //
        // map: ut4_casa
        // num score ping name            lastmsg address               qport rate
        // --- ----- ---- --------------- ------- --------------------- ----- -----
        //   1    19   33 [FS]Fenix            33 62.212.106.216:27960   5294 25000
        
        List<List<String>> collection = new LinkedList<List<String>>();
        Pattern pattern = Pattern.compile("^\\s*(?<slot>\\d+)\\s*(?<score>[\\d-]+)\\s*(?<ping>\\d+|CNCT|ZMBI)\\s*(?<name>.*?)\\s*(?<lastmsg>\\d+)\\s*(?<address>[\\d.]+|loopback|localhost):?(?<port>[\\d-]*)\\s*(?<qport>[\\d-]+)\\s*(?<rate>\\d+)$", Pattern.CASE_INSENSITIVE);
        String[] lines = result.split("\n");

        for (String line: lines) {
            
            Matcher matcher = pattern.matcher(line);
            
            if (matcher.matches()) {
                
                List<String> x = new LinkedList<String>();
                x.add(matcher.group("slot"));
                x.add(matcher.group("score"));
                x.add(matcher.group("ping"));
                x.add(matcher.group("name"));
                x.add(matcher.group("lastmsg"));
                x.add(matcher.group("address"));
                x.add(matcher.group("port"));
                x.add(matcher.group("qport"));
                x.add(matcher.group("rate"));

                collection.add(x);
                
            }
        }
        
        return collection;
    }
    
    
    /**
     * Kick the specified client from the server
     * 
     * @author Daniele Pantaleone
     * @param  client The client who is going to be kicked from the server
     **/
    public void kick(Client client) {
        this.rcon.sendNoRead("kick " + client.getSlot());
    }
    
    
    /**
     * Kick the specified client from the server
     * 
     * @author Daniele Pantaleone
     * @param  slot The slot of the player who is going to be kicked from the server
     **/
    public void kick(int slot) {
        this.rcon.sendNoRead("kick " + slot);
    }
    
    
    /**
     * Kick the specified client from the server by specifying a reason
     * 
     * @author Daniele Pantaleone
     * @param  client The client who is going to be kicked from the server
     * @param  reason The reason why the client is going to be kicked
     **/
    public void kick(Client client, String reason) {
        this.kick(client);
    }
    
    
    /**
     * Kick the specified client from the server by specifying a reason
     * 
     * @author Daniele Pantaleone
     * @param  slot The slot of the player who is going to be kicked from the server
     * @param  reason The reason why the player with the specified slot is going to be kicked
     **/
    public void kick(int slot, String reason) {
        this.kick(slot);
    }
    
    
    /**
     * Instantly kill a player
     * 
     * @author Daniele Pantaleone
     * @param  client The client who is going to be killed
     * @throws UnsupportedOperationException If the RCON command is not supported by the console implementation
     **/
    public void kill(Client client) throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Unable to execute smite RCON command: such command is only available as from Urban Terror 4.2 version");
    }
    
    
    /**
     * Instantly kill a player
     * 
     * @author Daniele Pantaleone
     * @param  slot The slot of the player who is going to be killed
     * @throws UnsupportedOperationException If the RCON command is not supported by the console implementation
     **/
    public void kill(int slot) throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Unable to execute smite RCON command: such command is only available as from Urban Terror 4.2 version");
    }
    
    
    /**
     * Change server current map
     * 
     * @author Daniele Pantaleone
     * @param  mapname The name of the map to load
     **/
    public void map(String mapname) {
        this.rcon.sendNoRead("map " + mapname);
    }
    
    
    /**
     * Mute a player
     * 
     * @author Daniele Pantaleone 
     * @param  client The client who is going to be muted
     **/
    public void mute(Client client) {
        this.rcon.sendNoRead("mute " + client.getSlot());
    }
    
    
    /**
     * Mute a player
     * 
     * @author Daniele Pantaleone 
     * @param  slot The slot of the player who is going to be muted
     **/
    public void mute(int slot) {
        this.rcon.sendNoRead("mute " + slot);
    }
    
    
    /**
     * Mute a player
     * 
     * @author Daniele Pantaleone 
     * @param  client The client who is going to be muted
     * @param  seconds The amount of seconds after which the mute will expire
     **/
    public void mute(Client client, int seconds) {
        this.rcon.sendNoRead("mute " + client.getSlot() + " " + seconds);
    }
    
    
    /**
     * Mute a player
     * 
     * @author Daniele Pantaleone 
     * @param  slot The slot of the player who is going to be muted
     * @param  seconds The amount of seconds after which the mute will expire
     **/
    public void mute(int slot, int seconds) {
        this.rcon.sendNoRead("mute " + slot + " " + seconds);
    }
    
     
    /**
     * Nuke a player
     * 
     * @author Daniele Pantaleone
     * @param  client The client who is going to be nuked
     **/
    public void nuke(Client client) {
        this.rcon.sendNoRead("nuke " + client.getSlot());
    }
    
    
    /**
     * Nuke a player
     * 
     * @author Daniele Pantaleone
     * @param  slot The slot of the player who is going to be nuked
     **/
    public void nuke(int slot) {
        this.rcon.sendNoRead("nuke " + slot);
    }
    
    
    /**
     * Print a message to the in-game chat
     * 
     * @author Daniele Pantaleone
     * @param  message The message to print
     **/
    public void say(String message) {
    	
    	if (message.length() > MAX_SAY_STRLEN) {
    		
    		// Splitting the message into multiple sentences
    		// In this way it won't overflow the game chat and it will print nicer
    		List<String> collection = Splitter.split(message, MAX_SAY_STRLEN);
    		
    		for (String sentence: collection) {
    			
    			// Sending the message
    			sentence = sentence.trim();
    			this.rcon.sendNoRead("say " + Color.WHITE + sentence);
    			
    			try { Thread.sleep(1000); } 
				catch (InterruptedException e) {
					this.log.error(e);
				}
    			
    		}
    		
    	} else {
    		
    		// Normal say command
            this.rcon.sendNoRead("say " + Color.WHITE + message);
    		
    	}
        
    }
    
    
    /**
     * Print an in-game message with visibility regulated by the command object
     * 
     * @author Daniele Pantaleone
     * @param  command The command issued
     * @param  message The message to be printed
     **/
    public void sayLoudOrPm(Command command, String message) {
    	
    	switch (command.prefix) {
    		
    		case NORMAL:
    			this.tell(command.client, message);
    			break;
    		case LOUD:
    			this.say(message);
    			break;
    		case BIG:
    			this.bigtext(message);
    			break;
    	
    	}
    }
    
    
    /**
     * Set a CVAR value
     * 
     * @author Daniele Pantaleone
     * @param  name The name of the cvar
     * @param  value The value to assign to the cvar
     **/
    public void setCvar(String name, Object value) {
        this.rcon.sendNoRead("set " + name + " \"" + String.valueOf(value) + "\"");
    }
    
    
    /**
     * Slap a player
     * 
     * @author Daniele Pantaleone
     * @param  client The client who is going to be slapped
     **/
    public void slap(Client client) {
        this.rcon.sendNoRead("slap " + client.getSlot());
    }
    
    
    /**
     * Slap a player
     * 
     * @author Daniele Pantaleone
     * @param  slot The slot of the player who is going to be slapped
     **/
    public void slap(int slot) {
        this.rcon.sendNoRead("slap " + slot);
    }
    
    
    /**
     * Start recording a server side demo of a player
     * 
     * @author Daniele Pantaleone
     * @param  client The client whose we want to record a demo
     **/
    public void startserverdemo(Client client) {
        this.rcon.sendNoRead("startserverdemo " + client.getSlot());
    }

    
    /**
     * Start recording a server side demo of a player
     * 
     * @author Daniele Pantaleone
     * @param  slot The slot of the player whose we want to record a demo
     **/
    public void startserverdemo(int slot) {
        this.rcon.sendNoRead("startserverdemo " + slot);
    }
    
    
    /**
     * Start recording a server side demo of all the online players
     * 
     * @author Daniele Pantaleone
     **/
    public void startserverdemo() {
        this.rcon.sendNoRead("startserverdemo all");
    }
    
    
    /**
     * Stop recording a server side demo of a player
     * 
     * @author Daniele Pantaleone 
     * @param  client The client whose we want to stop a demo recording
     **/
    public void stopserverdemo(Client client) {
        this.rcon.sendNoRead("stopserverdemo " + client.getSlot());
    }
    
    
    /**
     * Stop recording a server side demo of a player
     * 
     * @author Daniele Pantaleone 
     * @param  slot The slot of the player whose we want to stop a demo recording
     **/
    public void stopserverdemo(int slot) {
        this.rcon.sendNoRead("stopserverdemo " + slot);
    }
    
    
    /**
     * Stop recording a server side demo of all the online players
     * 
     * @author Daniele Pantaleone
     **/
    public void stopserverdemo() {
        this.rcon.sendNoRead("stopserverdemo all");
    }
    
    
    /**
     * Send a private message to a player
     * 
     * @author Daniele Pantaleone
     * @param  client The client you want to send the message
     * @param  message The message to be sent
     **/
    public void tell(Client client, String message) {
    	
    	if (message.length() > MAX_SAY_STRLEN) {
    		
    		// Splitting the message into multiple sentences
    		// In this way it won't overflow the game chat and it will print nicer
    		List<String> collection = Splitter.split(message, MAX_SAY_STRLEN);
    		
    		for (String sentence: collection) {
    			
    			// Sending the message
    			sentence = sentence.trim();
    			this.rcon.sendNoRead("tell " + client.getSlot() + " " + Color.WHITE + sentence);
    			
    			try { Thread.sleep(1000); } 
				catch (InterruptedException e) {
					this.log.error(e);
				}
    			
    		}
    		
    	} else {
    		
    		// Normal tell command
    		this.rcon.sendNoRead("tell " + client.getSlot() + " " + Color.WHITE + message);
    		
    	}
    	
    }
    
    
    /**
     * Send a private message to a player
     * 
     * @author Daniele Pantaleone
     * @param  slot The slot of the player you want to send the message
     * @param  message The message to be sent
     **/
    public void tell(int slot, String message) {
        
    	if (message.length() > MAX_SAY_STRLEN) {
    		
    		// Splitting the message into multiple sentences
    		// In this way it won't overflow the game chat and it will print nicer
    		List<String> collection = Splitter.split(message, MAX_SAY_STRLEN);
    		
    		for (String sentence: collection) {
    			
    			// Sending the message
    			sentence = sentence.trim();
    			this.rcon.sendNoRead("tell " + slot + " " + Color.WHITE + sentence);
    			
    			try { Thread.sleep(1000); } 
				catch (InterruptedException e) {
					this.log.error(e);
				}
    			
    		}
    		
    	} else {
    		
    		// Normal tell command
    		this.rcon.sendNoRead("tell " + slot + " " + Color.WHITE + message);
    		
    	}

    }
    
    
    /**
     * Unban a player from the server
     * 
     * @author Daniele Pantaleone
     * @param  client The client we want to unban
     **/
    public void unban(Client client) {
        this.rcon.sendNoRead("removeip " + client.getIp().getHostAddress());
    }
    

    /**
     * Unban a player from the server
     * 
     * @author Daniele Pantaleone
     * @param  ip The IP address of the player we want to unban
     **/
    public void unban(String ip) {
        this.rcon.sendNoRead("removeip " + ip);
    }
    
    
    /**
     * Write a message directly in the Urban Terror console<br>
     * Try to avoid the use of this command: se instead the other optimized methods available in this class
     * 
     * @author Daniele Pantaleone
     * @param  command The command to execute
     * @return The server response to the RCON command
     **/
    public String write(String command)  {
        return this.rcon.sendRead(command);
    }
     
}