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
 * @version     1.4
 * @copyright   Daniele Pantaleone, 10 February, 2013
 * @package     com.orion.console
 **/

package com.orion.console;

import static com.google.common.base.Preconditions.checkNotNull;

import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.goreclan.rcon.JRcon;
import net.goreclan.rcon.RconException;

import org.apache.commons.logging.Log;

import com.orion.command.Command;
import com.orion.domain.Client;
import com.orion.urt.Color;
import com.orion.urt.Cvar;
import com.orion.urt.Team;
import com.orion.utility.Splitter;

public class UrT42Console {
    
    private static final int CHAT_DELAY = 1000;
    private static final int CENTER_SCREEN_DELAY = 2000;
    private static final int MAX_SAY_STRLEN = 62;
    
    private final JRcon rcon;
    private final Log log;
    
    private Cvar authEnable = null;
    private Cvar authOwners = null;
    
    
    /**
     * Object constructor
     * 
     * @author Daniele Pantaleone 
     * @param  log Main logger object reference
     * @param  address The remote server address
     * @param  port The virtual port on which the server is accepting connections
     * @param  password The server RCON password
     * @throws IllegalArgumentException If the given port is out of range
     * @throws UnknownHostException If the IP address of a host could not be determined
     **/
    public UrT42Console(Log log, 
                        String address, 
                        int port, 
                        String password) throws IllegalArgumentException, UnknownHostException {
        
        this.log = log;
        this.rcon = new JRcon(address, port, password);
        
        try {
            
            this.authEnable = this.getCvar("auth_enable");
            this.authOwners = this.getCvar("auth_owners");
            
        } catch (NullPointerException | RconException e) {
            // Console will work but without auth support
            this.log.warn("Could not retrieve auth related CVARs. Auth support will be disbled");
        }      
        
        this.log.debug("Urban Terror 4.2 console initialized");
        
    }
    
    
    /**
     * Fetch FS Auth System informations for the specified <tt>Client</tt>
     * 
     * @author Daniele Pantaleone
     * @param  slot The slot of the <tt>Client</tt> whose informations needs to be retrieved
     * @throws UnsupportedOperationException If the Auth System has not been correctly initialized
     * @throws RconException If the RCON command fails in being executed
     * @throws ParserException If the auth-whois response couldn't be parsed correctly
     * @return A <tt>Map</tt> containing the auth-whois command response
     **//*
    public Map<String, String> authwhois(int slot) throws UnsupportedOperationException, RconException, ParserException {
        
        if (!this.game.isCvar("auth_enable") || !this.game.getCvar("auth_enable").getBoolean())
            throw new UnsupportedOperationException("auth system is disabled");
        
        Map<String, String> data = new HashMap<String, String>();
        String result = this.rcon.send("auth-whois " + slot, true);
        
        // Collecting FS Auth System informations
        Pattern pattern = Pattern.compile("^auth:\\s*id:\\s*(?<slot>\\d+)\\s*-\\s*name:\\s*(?<name>\\w+)\\s*-\\s*login:\\s*(?<login>\\w*)\\s*-\\s*notoriety:\\s*(?<notoriety>.*)\\s*-\\s*level:\\s*(?<level>\\d+)\\s*-\\s*(?<rank>.*)$", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(result);
        
        if (!matcher.matches())
            throw new ParserException("could not parse auth-whois command response");
        
        data.put("slot",        matcher.group("slot"));
        data.put("name",        matcher.group("name"));
        data.put("login",       matcher.group("login").isEmpty() ? null : matcher.group("login"));
        data.put("notoriety",   matcher.group("login").isEmpty() ? null : matcher.group("notoriety"));
        data.put("level",       matcher.group("login").isEmpty() ? null : matcher.group("level"));
        data.put("rank",        matcher.group("login").isEmpty() ? null : matcher.group("rank"));
        
        return data;
        
    }*/
    
    
    /**
     * Fetch FS Auth System informations for the specified <tt>Client</tt>
     * 
     * @author Daniele Pantaleone
     * @param  client The <tt>Client</tt> whose informations needs to be retrieved
     * @throws UnsupportedOperationException If the Auth System has not been correctly initialized
     * @throws RconException If the RCON command fails in being executed
     * @throws ParserException If the auth-whois response couldn't be parsed correctly
     * @throws NullPointerException If the given <tt>Client</tt> is <tt>null</tt>
     * @return A <tt>Map</tt> containing the auth-whois command response
     **//*
    public Map<String, String> authwhois(Client client) throws UnsupportedOperationException, RconException, ParserException, NullPointerException {
        return this.authwhois(checkNotNull(client).getSlot());
    }*/
    
    
    /**
     * Ban a <tt>Client</tt> from the server permanently
     * 
     * @author Daniele Pantaleone
     * @param  client The <tt>Client</tt> who is going to be banned
     * @throws RconException If the RCON command fails in being executed
     * @throws NullPointerException If the given <tt>Client</tt> is <tt>null</tt>
     **/
    public void ban(Client client) throws RconException, NullPointerException {
        
        try {
            
            checkNotNull(this.authEnable, "auth_enable CVAR is null");
            checkNotNull(this.authOwners, "auth_owners CVAR is null");
            
            if (!this.authEnable.getBoolean())
                throw new UnsupportedOperationException("auth system is disabled");
            
            this.rcon.send("addip " + client.getIp().getHostAddress());
            this.rcon.send("auth-ban " + client.getSlot() + " 0 0 0");
    
        } catch(NullPointerException | UnsupportedOperationException e) {
            
            this.rcon.send("addip " + client.getIp().getHostAddress());
            this.rcon.send("kick " + client.getSlot());
            
        }
  
    }
    
    
    /**
     * Cycle the current map on the server
     * 
     * @author Daniele Pantaleone
     * @throws RconException If the RCON commands fails in being executed
     **/
    public void cyclemap() throws RconException {
        this.rcon.send("cyclemap");
    }
    
    
    /*
    /**
     * Retrieve userinfo data for the specified <tt>Client</tt> slot number
     * 
     * @author Daniele Pantaleone
     * @param  slot The slot of the <tt>Client</tt> whose informations needs to be retrieved
     * @throws RconException If the <tt>Client</tt> informations couldn't be retrieved
     * @return A <tt>Map</tt> containing userinfo data or <tt>null</tt> 
     *         if the <tt>Client</tt> is not connected anymore
     **//*
    public Map<String, String> dumpuser(int slot) throws RconException {
        
        String result = this.rcon.send("dumpuser " + slot, true);
        
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
            if (m.matches()) 
                map.put(m.group("key"), m.group("value"));
        }
        
        return map.size() > 0 ? map : null;
        
    }
    
    
    /**
     * Retrieve userinfo data for the specified <tt>Client</tt>
     * 
     * @author Daniele Pantaleone
     * @param  client The <tt>Client</tt> whose informations needs to be retrieved
     * @throws RconException If the <tt>Client</tt> informations couldn't be retrieved
     * @throws NullPointerException If the given <tt>Client</tt> is <tt>null</tt>
     * @return A <tt>Map</tt> containing userinfo data or <tt>null</tt> 
     *         if the <tt>Client</tt> is not connected anymore
     **//*
    public Map<String, String> dumpuser(Client client) throws RconException, NullPointerException {
        return this.dumpuser(checkNotNull(client).getSlot());
    }
    */
    
    /**
     * Force a <tt>Client</tt> in the specified team
     *
     * @author Daniele Pantaleone
     * @param  client The <tt>Client</tt> who is going to be moved
     * @param  team The <tt>Team</tt> where to force the player in
     * @throws RconException If the <tt>Client</tt> fails in being moved
     * @throws NullPointerException If the given <tt>Client</tt> is <tt>null</tt>
     **/
    public void forceteam(Client client, Team team) throws RconException {
        this.write("forceteam " + client.getSlot() + " " + team.name().toLowerCase());
    }
        
    
    /**
     * Retrieve a CVAR from the server
     * 
     * @author Daniele Pantaleone
     * @param  name The CVAR name
     * @throws RconException If the CVAR could not be retrieved form the server
     * @return The <tt>Cvar</tt> object associated to the given CVAR name or <tt>null</tt> 
     *         if such CVAR is not set on the server
     **/
    public Cvar getCvar(String name) throws RconException, NullPointerException {
        
        try {
            
            String result = this.write(name, true); 
            
            Pattern pattern = Pattern.compile("\\s*\\\"[\\w+]*\\\"\\sis:\\\"(?<value>[\\w:\\.\\-\\\\/]*)\\\".*", Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(result);
            
            if (matcher.matches()) {
                
                String value = matcher.group("value");
                
                if (!value.trim().isEmpty()) {
                    this.log.trace("Retrieved CVAR " + name + ": " + value);
                    return new Cvar(name, value);
                }

            }
        
        } catch (RconException e) {
            // Catch and re-throw the same Exception but with more details
            throw new RconException("could not retrieve CVAR " + name, e);
        }
        
        return null;
        
    }
    
        
    /**
     * Return the current map name
     * 
     * @author Daniele Pantaleone
     * @throws RconException If the current map name couldn't be retrieved
     * @return The current map name 
     **/
    public String getMap() throws RconException {
        return this.getCvar("mapname").getString();
    }
    
    
    /**
     * Return a <tt>List</tt> of available maps
     * 
     * @author Daniele Pantaleone
     * @throws RconException If the map list couldn't be retrieved
     * @return A <tt>List</tt> of all the maps available on the server
     **/
    public List<String> getMapList() throws RconException {
        
        String result = this.write("fdir *.bsp", true);

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
     * @param  search The name of the map to search (or a part of it)
     * @throws RconException If the list of available maps couldn't be computed
     * @return A <tt>List</tt> of maps matching the given search key
     **/
    public List<String> getMapSoundingLike(String search) throws RconException {
        
        List<String> collection = new LinkedList<String>();
        List<String> maplist = this.getMapList();
        
        search = search.toLowerCase().trim();

        for (String map : maplist) {
            if (map.toLowerCase().contains(search.toLowerCase())) {
                collection.add(map);
            }
        }
        
        return collection;
        
    }
    
    
    /**
     * Return the name of the nextmap set on the server
     * 
     * @author Daniele Pantaleone
     * @throws RconException If an RCON command fails in being executed
     **/
    public String getNextMap() throws RconException {
        
        Cvar nextmap = null;
        
        if ((nextmap = this.getCvar("g_nextmap")) != null) {
            // Nextmap has been manually changed
            return nextmap.getString();
        }
        
        // Return the nextmap set in the mapcycle file
        nextmap = this.getCvar("g_nextcyclemap");
        return nextmap.getString(); 
   
    }
    
 /*
    /**
     * Return a <tt>List</tt> containing the result of the <tt>/rcon players</tt> command
     * 
     * @author Daniele Pantaleone
     * @throws RconException If we couldn't fetch informations from the server
     * @return A <tt>List</tt> containing players informations
     **//*
    public List<List<String>> getPlayers() throws RconException {
        
        String result = this.rcon.send("players", true);

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
    */
    

    /**
     * Return a <tt>List</tt> containing the result of the <tt>/rcon status</tt> command
     * 
     * @author Daniele Pantaleone
     * @throws RconException If we couldn't fetch informations from the server
     * @return A <tt>List</tt> containing status informations
     **//*
    public List<List<String>> getStatus() throws RconException {
        
        String result = this.rcon.send("status", true);
        
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
    
    */
    /**
     * Kick the specified <tt>Client</tt> from the server
     * 
     * @author Daniele Pantaleone
     * @param  client The <tt>Client</tt> who is going to be kicked from the server
     * @throws RconException If the RCON command fails in being executed
     **/
    public void kick(Client client) throws RconException {
        this.write("kick " + client.getSlot());
    }
    
    
    /**
     * Kick the specified <tt>Client</tt> from the server by specifying a reason
     * 
     * @author Daniele Pantaleone
     * @param  client The <tt>Client</tt> who is going to be kicked from the server
     * @param  reason The reason why the <tt>Client</tt> is going to be kicked
     * @throws RconException If the RCON command fails in being executed
     **/
    public void kick(Client client, String reason) throws RconException {
        this.write("kick " + client.getSlot() + " \"" + reason + "\"");
    }
   
  
    /**
     * Instantly kill a <tt>Client</tt>
     * 
     * @author Daniele Pantaleone
     * @param  client The <tt>Client</tt> who is going to be killed
     * @throws RconException If the RCON command fails in being executed
     **/
    public void kill(Client client) throws RconException {
        this.write("smite " + client.getSlot());
    }
    
    
    /**
     * Spawn the server onto a new level
     * 
     * @author Daniele Pantaleone
     * @param  mapname The name of the level to load
     * @throws RconException If the RCON command fails in being executed
     **/
    public void map(String mapname) throws RconException {
        this.write("map " + mapname);
    }
    
    
    /**
     * Mute a <tt>Client</tt>
     * 
     * @author Daniele Pantaleone 
     * @param  client The <tt>Client</tt> who is going to be muted
     * @throws RconException If the <tt>Client</tt> couldn't be muted
     **/
    public void mute(Client client) throws RconException {
        this.write("mute " + client.getSlot());
    }
  
    
    /**
     * Mute a <tt>Client</tt>
     * 
     * @author Daniele Pantaleone 
     * @param  client The <tt>Client</tt> who is going to be muted
     * @param  seconds The amount of seconds the <tt>Client</tt> will be muted
     * @throws RconException If the <tt>Client</tt> couldn't be muted
     **/
    public void mute(Client client, int seconds) throws RconException {
        this.write("mute " + client.getSlot() + " " + seconds);
    }
    
    
    /**
     * Nuke a <tt>Client</tt>
     * 
     * @author Daniele Pantaleone 
     * @param  client The <tt>Client</tt> who is going to be nuked
     * @throws RconException If the <tt>Client</tt> couldn't be nuked
     **/
    public void nuke(Client client) throws RconException {
        this.write("nuke " + client.getSlot());
    }
    
    
    /**
     * Print a message in the game chat
     * 
     * @author Daniele Pantaleone
     * @param  message The message to be printed
     * @throws RconException If the RCON command fails in being executed
     **/
    public void say(String message) throws RconException {
        
        if (message.length() > MAX_SAY_STRLEN) {
            
            // Splitting the message into multiple strings
            List<String> collection = Splitter.split(message, MAX_SAY_STRLEN);
            
            for (String sentence: collection) {
                
                // Sending the message
                sentence = sentence.trim();
                this.write("say " + Color.WHITE + sentence);
                
                try { Thread.sleep(CHAT_DELAY); } 
                catch (InterruptedException e) { }
                
            }
            
        } else {
            
            // No need to split here. Just send the command
            this.write("say " + Color.WHITE + message);
            
        }
        
    }
    
    
    /**
     * Write a bold message in the middle of the screen
     * 
     * @author Daniele Pantaleone
     * @param  message The message to be printed
     * @throws RconException If the RCON command fails in being executed
     **/
    public void sayBig(String message) throws RconException {
        
        if (message.length() > MAX_SAY_STRLEN) {
            
            // Splitting the message into multiple strings
            List<String> collection = Splitter.split(message, MAX_SAY_STRLEN);
            
            for (String sentence: collection) {
 
                sentence = sentence.trim();
                this.write("bigtext \"" + Color.WHITE + sentence + "\"");
            
                try { Thread.sleep(CENTER_SCREEN_DELAY); } 
                catch (InterruptedException e) { }
                
            }
            
        } else {
            
            // No need to split here. Just send the command
            this.write("bigtext \"" + Color.WHITE + message + "\"");
            
        }
        
    }
    
    
    /**
     * Send a private message to a <tt>Client</tt>
     * 
     * @author Daniele Pantaleone
     * @param  client The <tt>Client</tt> who will receive the message
     * @param  message The message to be sent
     * @throws RconException If the RCON command fails in being executed
     **/
    public void sayPrivate(Client client, String message) throws RconException {
        
        if (message.length() > MAX_SAY_STRLEN) {
            
            // Splitting the message into multiple sentences
            // In this way it won't overflow the game chat and it will print nicer
            List<String> collection = Splitter.split(message, MAX_SAY_STRLEN);
            
            for (String sentence: collection) {
                
                // Sending the message
                sentence = sentence.trim();
                this.write("tell " + client.getSlot() + " " + Color.WHITE + sentence);
                
                try { Thread.sleep(CENTER_SCREEN_DELAY); } 
                catch (InterruptedException e) { }
                
            }
            
        } else {
            
            // No need to split here. Just send the command
            this.write("tell " + client.getSlot() + " " + Color.WHITE + message);
            
        }

    }
    
    
    /**
     * Print an in-game message with visibility regulated by the command object
     * 
     * @author Daniele Pantaleone
     * @param  command The command issued
     * @param  message The message to be printed
     * @throws RconException If the RCON command fails in being executed
     **/
    public void sayLoudOrPm(Command command, String message) throws RconException {
        
        switch (command.getPrefix()) {
            
            case NORMAL:
                this.sayPrivate(command.getClient(), message);
                break;
            case LOUD:
                this.say(message);
                break;
            case BIG:
                this.sayBig(message);
                break;
        
        }
        
    }
      
    
    /**
     * Set a CVAR value
     * 
     * @author Daniele Pantaleone
     * @param  name The name of the CVAR
     * @param  value The value to assign to the CVAR
     * @throws RconException If the CVAR could not be set
     **/
    public void setCvar(String name, Object value) throws RconException {
        this.write("set " + name + " \"" + String.valueOf(value) + "\"");
    }
    
    
    /**
     * Slap a <tt>Client</tt>
     * 
     * @author Daniele Pantaleone 
     * @param  client The <tt>Client</tt> who is going to be slapped
     * @throws RconException If the <tt>Client</tt> couldn't be slapped
     **/
    public void slap(Client client) throws RconException {
        this.write("slap " + client.getSlot());
    }
    
    
    /**
     * Unban a <tt>Client</tt> from the server
     * 
     * @author Daniele Pantaleone
     * @param  client The <tt>Client</tt> whose IP address we want to unban
     * @throws RconException If the <tt>Client</tt> could not be unbanned
     **/
    public void unban(Client client) throws RconException {
        this.write("removeip " + client.getIp().getHostAddress());
    }
    
    
    /**
     * Write an RCON command in the remote
     * console without returning the server response
     * 
     * @author Daniele Pantaleone
     * @param  command The command to execute
     * @throws RconException If the RCON command fails in being executed
     **/
    public void write(String command) throws RconException {
        this.rcon.send(command);
    }
    
    
    /**
     * Write an RCON command in the remote console. Will return
     * the server response if specified in the command execution,
     * otherwise it will return <tt>null</tt>
     * 
     * @author Daniele Pantaleone
     * @param  command The command to execute
     * @throws RconException If the RCON command fails in being executed
     * @return The server response to the RCON command if specified 
     *         in the command execution, otherwise <tt>null</tt>
     **/
    public String write(String command, boolean read) throws RconException {
        
        if (!read) {
            this.write(command);
            return null;
        }
        
        return this.rcon.send(command, true);
    
    }
     
}
