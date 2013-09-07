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
 * @author      Mathias Van Malderen, Daniele Pantaleone
 * @version     1.0
 * @copyright   Mathias Van Malderen, 03 February, 2012
 * @package     com.orion.utility
 **/

package com.orion.misc;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

import org.joda.time.DateTime;
import org.slf4j.Logger;

import com.orion.parser.Parser;

public class Reader implements Runnable {
    
    private final Logger log;
    private final Parser parser;
    private final RandomAccessFile file;
    
    
    /**
     * Object constructor
     * 
     * @author Mathias Van Malderen
     * @param  log Main logger object reference
     * @param  parser Main parser object reference
     * @param  path The gameserver log filepath
     * @throws FileNotFoundException If the specified path is not valid
     **/
    public Reader(Logger log, 
                  Parser parser,
                  String path) throws FileNotFoundException {
        
        this.log = log;
        this.parser = parser;
        this.file = new RandomAccessFile(path, "r");
        
        this.log.debug("Log reader initialized: using " + path);
        
    }
   
    
    /**
     * Runnable implementation.<br>
     * Will keep processing the game server log file
     * until it finds new log lines to be processed or
     * until the process is manually killed by a user.
     * 
     * @author Mathias Van Malderen, Daniele Pantaleone
     **/
    public void run(){
        
        String line = null;
        
        this.log.debug("Log reader started: " + new DateTime().toString());

        try {
            
            // Moving the file pointer at the end of the file
            // This actually define from when/where Orion will start 
            // processing data. All the past log lines are going to
            // be discarded. Usually is better to fully initialize
            // the BOT before starting to process the log file
            // otherwise the event/command queue will be filled
            // without the possibility of dispatching such objects
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
                     
                     while ((line = this.file.readLine()) != null) {
                         
                         if (Thread.interrupted())
                             throw new InterruptedException();

                         this.parser.parseLine(line);
                         
                     }
                 
                 } catch (IOException e) {
                     
                     // Logging the Exception. This doesn't happen often but still...
                     this.log.error("Unable to read line from the log file", e);
                     this.log.debug("Last log line discarded. Will keep processing the log file anyway...");
                     continue;
             
                 } catch (InterruptedException e) {
                     
                     break;
                     
                 }
                 
             }
            
        } finally {
        
            try {
                
                // Trying to close the log file pointer.
                // This will generate an IOException if the
                // log file has been removed meanwhile the
                // Thread was reading new lines from it
                this.file.close();
                
            } catch (IOException e) {
            
                // This is very rare!
                this.log.error("Could not to close file pointer", e);
            
            }
            
        }
        
        this.log.debug("Log reader stopped: " + new DateTime().toString());
        
    }
    
}
