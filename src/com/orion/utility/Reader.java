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
 * @author      Mathias Van Malderen, Daniele Pantaleone
 * @version     1.0
 * @copyright   Mathias Van Malderen, 03 February, 2012
 * @package     com.orion.utility
 **/

package com.orion.utility;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

import org.apache.commons.logging.Log;

import com.orion.bot.Orion;
import com.orion.parser.Parser;

public class Reader implements Runnable {
    
    private final Log log;
    private final Parser parser;
    private final RandomAccessFile file;
    private final int delay;
    
    
    /**
     * Object constructor.
     * 
     * @author Mathias Van Malderen
     * @param  path The filepath of the Urban Terror log file
     * @param  delay Amount of milliseconds to wait for new log lines to be read
     * @param  orion Orion object reference
     * @throws FileNotFoundException If the specified path is not valid
     **/
    public Reader(String path, int delay, Orion orion) throws FileNotFoundException {
        
        this.log = orion.log;
        this.parser = orion.parser;
        this.delay = delay;
        this.file = new RandomAccessFile(path, "r");
        
        // Print some debugging info so the user can check if he's reading the correct log file.
        this.log.debug("Log reader initialized [ logfile : " + path + " | delay : " + this.delay + "ms ]");
        
    }
   
    
    /**
     * Runnable implementation.<br>
     * Will keep processing the Urban Terror log file
     * until it finds new log lines to be processed or
     * untill the process is manually killed by a user.
     * 
     * @author Mathias Van Malderen, Daniele Pantaleone
     **/
    public void run(){
        
        String line = null;
        
        // Notifying Thread start in the log file.
        this.log.debug("Starting log reader [ systime : " + System.currentTimeMillis() + " ]");

        try {
            
            // Moving the file pointer at the end of the file.
            // This actually define from when/where Orion will start 
            // processing data. All the past log lines are going to
            // be discarded. Usually is better to fully initialize
            // the BOT before starting to process the log file.
            // otherwise the event/command queue will be filled
            // without the possibility of dispatching such objects.
            this.file.seek(this.file.length());
            
        } catch (IOException e) {
            
            // If the moving fails, we can terminate the Thread gracefully!
            // If we let Orion to parse a log file which can be huge in terms of
            // lines to be processed, it will takes possibily ours to get synced
            // with the current game, and waste tons of resources.
            this.log.error("Unable to move file pointer to last log line", e);
            this.log.debug("Log file and game server are going to be out of sync. Shutting down...");
            Thread.currentThread().interrupt();
            
        }
     
          
        try {
            
             while (true) {
                 
                 try {
                     
                     line = null;
                     Thread.sleep(this.delay);
                     
                     // Read log lines until EOF has been reached or until the thread is interrupted.
                     while ((line = this.file.readLine()) != null) {
                         
                         // Parsing retrieved data.
                         this.parser.parseLine(line);
                     }
                 
                 } catch (IOException e) {
                     
                     // Logging the Exception. This doesn't happen often but still...
                     this.log.error("Unable to read line from the log file", e);
                     this.log.debug("Last log line discarded. Will keep processing the log file anyway...");
                     continue;
             
                 } catch (InterruptedException e) {
                     
                     // Thread has received interrupt signal.
                     // Breaking the cycle so it will terminate.
                     break;
                     
                 }
                 
             }
            
        } finally {
        
            try {
                
                // Trying to close the log file pointer.
                // This will generate an IOException if the
                // log file has been removed meanwhile the
                // Thread was reading new lines from it.
                this.file.close();
                
            } catch (IOException e) {
            
                // Logging the Exception.
                this.log.error("Unable to close file pointer", e);
            
            }
            
        }
        
        // Thread is going to be shutted down, no matter if the
        // log file pointer has been closed correctly. It will be 
        // eventually closed when the process terminate.
        this.log.debug("Stopping log reader [ systime : " + System.currentTimeMillis() + " ]");
        
    }
    
}
