/**
 * Copyright (c) 2013 Daniele Pantaleone, Mathias Van Malderen
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
 * @author      Mathias Van Malderen
 * @version     1.0
 * @copyright   Mathias Van Malderen, 23 July, 2013
 * @package     com.orion.eventbus
 **/

package com.orion.eventbus;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ComparisonChain;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Multimaps;
import com.orion.exception.HandlerInvocationException;
import com.orion.exception.VetoException;
import com.orion.utility.ListUtil;


/**
 * A custom-tailored event bus implementation designed for Orion.
 * 
 * Features:
 * 
 * 1. Annotation-based event handler declarations.
 * 2. Veto support for event dispatch cancellation.
 * 3. Asynchronous event dispatch using a background worker thread.
 * 4. Thread-safe.
 * 5. Priorities on two levels:
 *      - runtime priority support at subscriber level
 *      - compile-time priority support at handler level
 * 
 * Future features:
 * 
 * 1. Support for predicate methods that determine whether            [TODO ]
 *       an event is to be dispatched to a subscriber or not.
 *       This predicate method is to be called every time before
 *    an actual handler which it applies to, is called.
 *    The support will be implemented using an @EventPredicate
 *    annotation which - by default - applies to all annotated
 *    handler methods. Only basic support will be implemented.
 *    There's however room to enhance this feature to support
 *    more sophisticated method matching, but this will not be
 *    implemented until required.
 * 2. Catch subtypes of specified event type.                       [TODO ]
 * 3. Dispatch dead events in a wrapped DeadEvent object.           [MAYBE]
 * 4. Weak reference support.                                       [MAYBE]
 * 
 * Designed for the following usage pattern:
 * 
 * - Most of the subscribers are registered before the first event gets posted.
 * - Subscribers are very rarely registered in the time interval between the very first and very last event.
 **/

public class OrionBus {
    
    private final BlockingQueue<Object> eventQueue;
    private final ListMultimap<Class<?>, Handler> handlersByType;
    private final Thread dispatchThread;
    
    
    private OrionBus(BlockingQueue<Object> eventQueue) {
        
        this.eventQueue = checkNotNull(eventQueue);
        this.handlersByType = Multimaps.synchronizedListMultimap(ArrayListMultimap.<Class<?>, Handler> create());
        
        // Initialize Event Dispatcher
        this.dispatchThread = new Thread(new EventDispatcher(), "Orion Event Dispatcher");
        this.dispatchThread.setDaemon(true); // do not prevent the JVM from exiting
        this.dispatchThread.start(); // FIXME ???
    }
    
    
    public void post(Object event) {
        
        checkNotNull(event);
        
        try {
            eventQueue.put(event);
        } catch (InterruptedException ex) {
            throw new RuntimeException(ex);
        }
        
    }


    /**
     * @param subscriber
     * @param priority 
     **/
    public void register(Object subscriber, int priority) {
        
        checkNotNull(subscriber);
        
        // Fetch Handler meta data for the candidate subscriber
        List<Handler> handlers = AnnotationProcessor.process(subscriber, priority);
        
        synchronized (this.handlersByType) {
            
            // Skip registration if this 
            // object is already a subscriber
            if (isSubscriber(subscriber)) 
                return;

            // Register all collected handlers for this subscriber.
            for (Handler h : handlers) {
                registerHandler(h);
            }
            
        }
    }
    
    
    // Need to acquire a lock on handlersByType before calling this one.
    private void registerHandler(Handler handler) {
        List<Handler> insertList = this.handlersByType.get(handler.getEventClass());
        ListUtil.insertInOrder(insertList, handler, HANDLERS_BY_PRIORITY_DESCENDING);
    }
    
    
    private boolean isSubscriber(Object obj) {
        
        Collection<Handler> allHandlers = this.handlersByType.values();
        
        synchronized (this.handlersByType) {
            
            for (Handler handler : allHandlers) {
                
                if (handler.getSubscriber() == obj) {
                    return true;
                }
            }
            
            return false;
        }
    }
    
    
    /**
     * Register a subscriber with normal priority
     * 
     * @param subscriber 
     **/
    public void register(Object subscriber) {
        register(subscriber, Priority.NORMAL);
    }
    
    
    public void unregister(Object subscriber) {
        
        Collection<Handler> allHandlers = this.handlersByType.values();
        
        synchronized (this.handlersByType) {
            
            Iterator<Handler> it = allHandlers.iterator();
            
            while (it.hasNext()) {
                
                if (it.next().getSubscriber() == subscriber) {
                    it.remove();
                }
                
            }
        }
    }
    
    
    public boolean hasPendingEvents() {
        return eventQueue.size() > 0;
    }
    
    
    public static OrionBus createBounded(int capacity) {
        return new OrionBus(new ArrayBlockingQueue<>(capacity));
    }
    
    
    public static OrionBus createUnbounded() {
        return new OrionBus(new LinkedBlockingQueue<>());
    }
    
    
    private Collection<Handler> getHandlersForType(Class<?> eventClass) {
        return handlersByType.get(eventClass);
    }
    
    
    private class EventDispatcher implements Runnable {
        
        @Override
        public void run() {
            
            try {
              
                while (true) {
                    dispatchEvent(eventQueue.take());
                }
            
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }

        
        private void dispatchEvent(Object event) {
            
            try {
                
                Collection<Handler> handlers = getHandlersForType(event.getClass());
                
                synchronized (handlersByType) {
                    
                    for (Handler h : handlers) {
                        h.invoke(event);
                    }
                }
                
            } catch (HandlerInvocationException ex) {
                
                if (!isVetoCause(ex)) {
                    // the event wasn't vetoed
                }
                
            } catch (Throwable t) {
                
                // an unexpected error occured
                t.printStackTrace();
                
            }
        }
        
        
        private boolean isVetoCause(Throwable t) {
            
            // Find out whether VetoException is an 
            // underlying cause of this Throwable
            do { 
                
                if (t instanceof VetoException) 
                    return true; 
            
            } while ((t = t.getCause()) != null);
            
            return false;
            
        }
    }
    
    
    private static final Comparator<Handler> HANDLERS_BY_PRIORITY_DESCENDING = new Comparator<Handler>() {
        
        @Override
        public int compare(Handler h1, Handler h2) {
            return ComparisonChain.start()
                .compare(h2.getSubscriberPriority(), h1.getSubscriberPriority())
                .compare(h2.getHandlerPriority(), h1.getHandlerPriority())
                .result();
        }
        
    };
}

