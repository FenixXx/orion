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
 * @copyright   Daniele Pantaleone, 04 October, 2012
 * @package     com.orion.utility
 **/

package com.orion.utility;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import org.apache.commons.logging.Log;

import com.orion.bot.Orion;
import com.orion.exception.RconException;

public class Rcon {
    
    private static final int  PACKET_SIZE             = 1400;
    private static final int  PACKET_TIMEOUT          = 2000;
    private static final int  MULTIPLE_PACKET_TIMEOUT = 300;
    private static final long SOCKET_DELAY            = 200;
    
    private final Log log;

    private long lastCommandTime;
    private String password;
    private InetAddress host;
    private int port; 
    
    /**
     * Object constructor
     * 
     * @author Daniele Pantaleone 
     * @param  address The remote server address
     * @param  port The port on which the server is accepting connections
     * @param  password The server RCON password
     * @param  orion <tt>Orion</tt> object reference
     * @throws UnknownHostException If the IP address of a host could not be determined
     * @throws RconException If the RCON communication with the server is not working properly
     **/
    public Rcon(String address, int port, String password, Orion orion) throws UnknownHostException, RconException {
        
        this.log = orion.log;
        this.password = password.trim();
        this.host = InetAddress.getByName(address);
        this.port = port;
        
        // Printing some RCON informations in the log so the user can check if he committed mistakes in the configuration file
        this.log.debug("RCON configured [ host : " + this.host.getHostAddress() + " | port : " + this.port + " | password : " + this.password + " ]");
        
        // Testing RCON utility
        this.log.debug("Testing RCON...");
        String result = this.sendRead("status");
        
        if (result == null)
            throw new RconException("RCON UDP socket not responding. Can't start without RCON support");
        
        if (result.toLowerCase().contains("no rconpassword"))
            throw new RconException("Missing RCON password on game server. Can't start without RCON support");
        
        if (result.toLowerCase().contains("bad rconpassword"))
            throw new RconException("Bad RCON password. Can't start without RCON support");
        
        // Printing RCON test result
        this.log.debug("RCON status: OK");
        
    }
    
    
    /**
     * Build and return <tt>DatagramPacket</tt> containing the RCON command 
     * ready to be sent to the server engine for evaluation
     * 
     * @author Daniele Pantaleone
     * @param  command The RCON command to be sent to the server engine
     * @return A <tt>DatagramPacket</tt> with the RCON command ready to be sent to the server engine
     **/
    private DatagramPacket getDatagramPacket(String command) {
        
        byte[] buff = command.getBytes();
        byte[] send = new byte[buff.length + 5];
        
        send[0] = (byte)0xFF;
        send[1] = (byte)0xFF;
        send[2] = (byte)0xFF;
        send[3] = (byte)0xFF;
        
        // Copying the command byte to byte into the byte array
        // byte array that we are going to send on the UDP socket
        for (int i = 0; i < buff.length; i++) send[i + 4] = buff[i];
        
        // Set the last byte to 0x00
        send[buff.length + 4] = (byte)0x00;
        
        // Building and returning the DatagramPacket
        return new DatagramPacket(send, send.length, this.host, this.port);
   
    }
    
  
    /**
     * Write a command in the RCON console without returning the server response
     * 
     * @author Daniele Pantaleone 
     * @param  command The command to be sent to the server engine
     **/
    public void sendNoRead(String command) {
        
        DatagramSocket socket = null;
        
        try {
            
            // Be sure not to overflow the server engine
            long currentTime = System.currentTimeMillis();
            if ((currentTime - this.lastCommandTime) < SOCKET_DELAY)
                Thread.sleep(SOCKET_DELAY - (currentTime - this.lastCommandTime));
            
            // Logging the RCON send command
            this.log.trace("RCON sending [" + this.host.getHostAddress() + ":" + this.port + "] " + command);
            
            // Creating the UDP socket
            socket = new DatagramSocket();
            
            // Creating the DatagramPacket for the given command and sending it over the socket
            DatagramPacket out = this.getDatagramPacket("rcon " + this.password + " " + command);
            socket.send(out);

            // Updating last command execution time
            this.lastCommandTime = System.currentTimeMillis();

            
        } catch (IOException | InterruptedException e) {

            // Logging the Exception
            this.log.error("Unable to send RCON command: " + command, e);
            
        } finally {
            
            // Closing the socket if it has been actually
            // created and is still connected to the server
            if ((socket != null) && (!socket.isClosed())) {
                socket.close();
            }
        
        }
        
    }
    
    
    /**
     * Write a command in the RCON console and return the result
     * Note that this function simply return the <tt>String</tt> 
     * returned by the server engine without parsing the server response
     * 
     * @author Daniele Pantaleone 
     * @param  command The command to be sent to the server engine
     * @return A <tt>String</tt> with the parsed RCON server response
     **/
    public String sendRead(String command) {
        
        DatagramSocket socket = null;
        DatagramPacket packet = null;
        StringBuilder builder = new StringBuilder();
        
        try {
            
            // Be sure not to overflow the server engine
            long currentTime = System.currentTimeMillis();
            if ((currentTime - this.lastCommandTime) < SOCKET_DELAY)
                Thread.sleep(SOCKET_DELAY - (currentTime - this.lastCommandTime));
            
            // Logging the RCON send command
            this.log.trace("RCON sending [" + this.host.getHostAddress() + ":" + this.port + "] " + command);
            
            // Creating the UDP socket
            socket = new DatagramSocket();
            
            // Creating the DatagramPacket for the given command and sending it over the socket
            DatagramPacket out = this.getDatagramPacket("rcon " + this.password + " " + command);
            socket.send(out);
            
            // Creating the DatagramPacket where to store the server response
            packet = new DatagramPacket(new byte[PACKET_SIZE], PACKET_SIZE);
            
            // Getting the response
            socket.setSoTimeout(PACKET_TIMEOUT);
            socket.receive(packet);
            builder.append(new String(packet.getData(), 0, packet.getLength()));
            
            try {
                
                // Wait for a possible multiple packets
                packet = new DatagramPacket(new byte[PACKET_SIZE], PACKET_SIZE);
                socket.setSoTimeout(MULTIPLE_PACKET_TIMEOUT);
                
                while (true) {
                    socket.receive(packet);
                    builder.append(new String(packet.getData(), 0, packet.getLength()));
                }
                
            } catch(SocketTimeoutException e) {
                // Server didn't send more packets
            }
            
            // Updating last command execution time
            this.lastCommandTime = System.currentTimeMillis();
            
            return builder.substring(10)
                          .replaceAll("\\^[0-9]{1}", "")
                          .trim();
            
        } catch (IOException | InterruptedException e) {

            // Logging the Exception
            this.log.error("Unable to send RCON command: " + command, e);
            return null;
            
        } finally {
            
            // Closing the socket if it has been actually
            // created and is still connected to the server
            if ((socket != null) && (!socket.isClosed())) {
                socket.close();
            }
        
        }
        
    }
    
}