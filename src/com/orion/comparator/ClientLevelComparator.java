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
 * @copyright   Daniele Pantaleone, 11 March, 2013
 * @package     com.orion.comparator
 **/

package com.orion.comparator;

import java.util.Comparator;

import com.google.common.collect.ComparisonChain;
import com.orion.domain.Client;


public class ClientLevelComparator implements Comparator<Client> {
    
    /**
     * Compare two <tt>Client</tt> objects according to their <tt>Group</tt> level
     * 
     * @author Daniele Pantaleone
     * @return The comparison between the given <tt>Client</tt> objects according to their <tt>Group</tt> level
     **/
    public int compare(Client c1, Client c2) {
        return ComparisonChain.start()
                 .compare(c1.getGroup().getLevel(), c2.getGroup().getLevel())
                 .compare(c1.getName(), c2.getName())
                 .result();
    }
    
}

