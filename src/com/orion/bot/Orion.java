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
 * @version     0.2
 * @copyright   Daniele Pantaleone, 07 February, 2013
 * @package     com.orion.bot
 **/

package com.orion.bot;

import java.io.OutputStreamWriter;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.impl.Log4JLogger;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import com.google.common.eventbus.EventBus;
import com.orion.command.Command;
import com.orion.console.Console;
import com.orion.control.AliasC;
import com.orion.control.CallvoteC;
import com.orion.control.ClientC;
import com.orion.control.GroupC;
import com.orion.control.IpAliasC;
import com.orion.control.PenaltyC;
import com.orion.exception.ParserException;
import com.orion.misc.CommandProcessor;
import com.orion.misc.Reader;
import com.orion.misc.RegisteredCommand;
import com.orion.parser.Parser;
import com.orion.plugin.Plugin;
import com.orion.storage.DataSourceManager;
import com.orion.storage.MySqlDataSourceManager;
import com.orion.urt.Color;
import com.orion.urt.Game;
import com.orion.utility.Configuration;
import com.orion.utility.MultiKeyHashMap;
import com.orion.utility.MultiKeyMap;
import com.orion.utility.XmlConfiguration;

public class Orion {
    
    public static final String AUTHOR   = "Daniele Pantaleone, Mathias Van Malderen";
    public static final String WEBSITE  = "www.goreclan.net";
    public static final String BOTNAME  = "Orion";
    public static final String CODENAME = "LICAN";
    public static final String VERSION  = "0.1";
    
    public Log log;
    public Game game;
    public Parser parser;
    public Console console;
    public Configuration config;
    public DataSourceManager storage;
    
    public Thread reader;
    public Thread commandproc;
    
    public AliasC aliases;
    public CallvoteC callvotes;
    public ClientC clients;
    public GroupC groups;
    public IpAliasC ipaliases;
    public PenaltyC penalties;
    
    public BlockingQueue<Command> commandqueue;
    
    public Map<String, Timer> schedule;
    public Map<String, Plugin> plugins;
    public MultiKeyMap<String, String, RegisteredCommand> regcommands;
    
    public EventBus eventBus;
    
    public Locale locale;
    public String timeformat;
    public DateTimeZone timezone;
    public DateTime startuptime;
    
    
    /**
     * Object constructor
     * 
     * @author Daniele Pantaleone
     * @param  path The Orion configuration file path
     **/
    public Orion(String path) {
        
        try {
            
            // Loading the main XML configuration file
            this.config = new XmlConfiguration(path);
            
            ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            //////////////////////////////////////////////////// LOGGER SETUP /////////////////////////////////////////////////////////
            ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            Logger.getRootLogger().setLevel(Level.OFF);
            Logger logger = Logger.getLogger("Orion");
            
            FileAppender fa = new FileAppender();
            
            fa.setLayout(new PatternLayout("%-20d{yyyy-MM-dd hh:mm:ss} %-6p %m%n"));
            fa.setFile(this.config.getString("logfile", "basepath") + this.config.getString("logfile", "filename"));
            fa.setAppend(this.config.getBoolean("logfile", "append"));
            fa.setName("FILE");
            fa.activateOptions();
            
            logger.addAppender(fa);
           
            if (this.config.getBoolean("logfile", "console")) {

                ConsoleAppender ca = new ConsoleAppender();
                ca.setLayout(new PatternLayout("%-20d{yyyy-MM-dd hh:mm:ss} %-6p %m%n"));
                ca.setWriter(new OutputStreamWriter(System.out));
                ca.setName("CONSOLE");
                ca.activateOptions();

                logger.addAppender(ca);
            
            }
            
            // Setting the log level for both the log appenders
            logger.setLevel(Level.toLevel(this.config.getString("logfile", "level")));

            // Creating the main Log object
            this.log = new Log4JLogger(logger);
            
            // We got a fully initialized logger utility now: printing some info messages
            this.log.info("Starting " + BOTNAME + " " + VERSION + " [" + CODENAME + "] [ " + AUTHOR + " ] - " + WEBSITE);
            
            
            ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            /////////////////////////////////////////////// LOADING PREFERENCES ///////////////////////////////////////////////////////
            ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            this.timeformat = this.config.getString("orion", "timeformat", "EEE, d MMM yyyy HH:mm:ss");
            this.timezone = DateTimeZone.forID(this.config.getString("orion", "timezone", "CET"));
            this.locale = new Locale(this.config.getString("orion", "locale", "EN"), this.config.getString("orion", "locale", "EN"));
            this.startuptime = new DateTime(this.timezone);
            
            
            ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            ////////////////////////////////////////////// PRE INITIALIZED OBJECTS ////////////////////////////////////////////////////
            ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            this.eventBus = new EventBus("events");
            this.schedule = new LinkedHashMap<String, Timer>();
            this.game = new Game();
            
            
            ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            /////////////////////////////////////////////////// STORAGE SETUP /////////////////////////////////////////////////////////
            ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            this.storage = new MySqlDataSourceManager(this.config.getString("storage", "username"), 
                                                      this.config.getString("storage", "password"), 
                                                      this.config.getString("storage", "connection"), 
                                                      this.log);
            
            
            ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            //////////////////////////////////////////////////// BUFFERS SETUP ////////////////////////////////////////////////////////
            ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            this.commandqueue = new ArrayBlockingQueue<Command>(this.config.getInt("orion", "commandqueue", 100));
            this.regcommands = new MultiKeyHashMap<String, String, RegisteredCommand>();
            

            ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            //////////////////////////////////////////////////// CONSOLE SETUP ////////////////////////////////////////////////////////
            ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            this.console = (Console)Class.forName("com.orion.console." + this.config.getString("orion", "game") + "Console")
                                            .getConstructor(String.class, int.class, String.class, Orion.class)
                                            .newInstance(this.config.getString("server", "rconaddress"),
                                                         this.config.getInt("server", "rconport"),
                                                         this.config.getString("server", "rconpassword"),
                                                         this);
            
            
            ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            /////////////////////////////////////////////////// CONTROLLERS SETUP /////////////////////////////////////////////////////
            ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            this.groups = new GroupC(this);
            this.clients = new ClientC(this);
            this.aliases = new AliasC(this);
            this.callvotes = new CallvoteC(this);
            this.ipaliases = new IpAliasC(this);
            this.penalties = new PenaltyC(this);
            
            
            ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            ///////////////////////////////////////////////////// PARSER SETUP/////////////////////////////////////////////////////////
            ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            this.parser = (Parser)Class.forName("com.orion.parser." + this.config.getString("orion", "game") + "Parser")
                                       .getConstructor(Orion.class)
                                       .newInstance(this);
            
            
            ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            //////////////////////////////////////////////////// LOADING PLUGINS //////////////////////////////////////////////////////
            ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            this.plugins = new LinkedHashMap<String, Plugin>();
            Map<String, String> pluginsList = this.config.getMap("plugins");
            
            for (Map.Entry<String, String> entry : pluginsList.entrySet()) {
                
                try {
                    
                    this.log.debug("Loading plugin [ " + Character.toUpperCase(entry.getKey().charAt(0)) + entry.getKey().substring(1).toLowerCase() + " ]");
                    Plugin plugin = Plugin.getPlugin(entry.getKey(), new XmlConfiguration(entry.getValue(), this.log), this);
                    this.plugins.put(entry.getKey(), plugin);

                } catch (ClassNotFoundException | NoSuchMethodException | SecurityException | InstantiationException |
                         IllegalAccessException | IllegalArgumentException | InvocationTargetException | ParserException e) {
                    
                    // Logging the Exception and keep processing other plugins. This will not stop Orion execution
                    this.log.error("Unable to load plugin [ " + Character.toUpperCase(entry.getKey().charAt(0)) + entry.getKey().substring(1).toLowerCase() + " ]", e);
                
                }
                
            }
            
            
            ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            ////////////////////////////////////////////////////// PLUGINS SETUP //////////////////////////////////////////////////////
            ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            for (Map.Entry<String, Plugin> entry : this.plugins.entrySet()) {
                Plugin plugin = entry.getValue();
                plugin.onLoadConfig();
            }
            
            
            ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            ///////////////////////////////////////////////////// PLUGINS STARTUP /////////////////////////////////////////////////////
            ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            for (Map.Entry<String, Plugin> entry : this.plugins.entrySet()) {
                
                Plugin plugin = entry.getValue();
                
                // Check for the plugin to be enabled. onLoadConfig may have disabled
                // such plugin in case the plugin config file is non well formed
                if (plugin.isEnabled()) plugin.onStartup();
            
            }
            
            
            ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            ///////////////////////////////////////////////////// GAME SERVER SYNC ////////////////////////////////////////////////////
            ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            List<List<String>> status = this.console.getStatus();
            
            if (status == null) {
                this.log.warn("Unable to synchronize current server status: RCON response is NULL");
                return;
            }
            
            for (List<String> line : status) {
                
                // Dumping current user to build an infostring for the onClientConnect parser method
                Map<String, String> userinfo = this.console.dumpuser(Integer.parseInt(line.get(0)));
                
                // Not a valid client
                if (userinfo == null) continue;
                
                String infostring = new String();
                
                for (Map.Entry<String, String> entry : userinfo.entrySet()) {
                    // Appending <key|value> using infostring format
                    infostring += "\\" + entry.getKey() + "\\" + entry.getValue();
                }
                
                // Generating an EVT_CLIENT_CONNECT event for the connected client
                this.parser.parseLine("0:00 ClientUserinfo: " + line.get(0) + " " + infostring);
                
            }
            
            
            ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            /////////////////////////////////////////////////////// THREADS SETUP /////////////////////////////////////////////////////
            ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            this.reader = new Thread(new Reader(this.config.getString("server", "logfile"), this.config.getInt("server", "logdelay"), this));
            this.commandproc = new Thread(new CommandProcessor(this));
            this.reader.setName("READER");
            this.commandproc.setName("COMMAND");
            
            
            ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            ////////////////////////////////////////////////////// THREADS STARTUP ////////////////////////////////////////////////////
            ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            this.commandproc.start();
            this.reader.start();
            
			///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
			//////////////////////////////////////////////////// NOTICE BOT RUNNING ///////////////////////////////////////////////////
			///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            this.console.say(BOTNAME + " " + VERSION + " [" + CODENAME + "] - " + WEBSITE + " >> " + Color.GREEN + "ONLINE");
            
            
        } catch (Exception e) {
            
        	// Stopping Threads if they are alive
        	if ((this.commandproc != null) && (this.commandproc.isAlive())) this.commandproc.interrupt();
        	if ((this.reader != null) && (this.reader.isAlive())) this.reader.interrupt();
        	
            // Logging the Exception. Orion is not going to work if an Exception is catched at startup time
            this.log.fatal("Unable to start " + BOTNAME + " " + VERSION + " [" + CODENAME + "] [ " + AUTHOR + " ] - " + WEBSITE, e);
            
        }
        
    }
    
    
    /**
     * Return the amount of milliseconds since the BOT started
     * 
     * @author Daniele Pantaleone
     * @return The amount of milliseconds since the BOT started
     **/
    public long uptime() {
    	return new DateTime(this.timezone).getMillis() - this.startuptime.getMillis();
    }
    
}
