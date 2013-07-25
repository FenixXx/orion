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
import com.orion.exception.HandlerInvocationException;
import java.lang.reflect.Method;

final class Handler {
    
    private final int subscriberPriority;
    private final int handlerPriority;
    private final Method method;
    private final Object subscriber;
    private final Class<?> eventClass;
    
    
    private Handler(Builder builder) {
        this.subscriber = checkNotNull(builder.subscriber);
        this.method = checkNotNull(builder.method);
        this.eventClass = checkNotNull(builder.eventClass);
        this.subscriberPriority = builder.subscriberPriority;
        this.handlerPriority = builder.handlerPriority;
    }
    
    
    int getSubscriberPriority() {
        return this.subscriberPriority;
    }
    
    
    int getHandlerPriority() {
        return this.handlerPriority;
    }
    
    
    Object getSubscriber() {
        return this.subscriber;
    }
    
    
    Class<?> getEventClass() {
        return this.eventClass;
    }
    
    
    void invoke(Object event) {
        
        try {
            this.method.setAccessible(true);
            this.method.invoke(subscriber, event);
        } catch (ReflectiveOperationException ex) {
            throw new HandlerInvocationException(ex);
        }
        
    }
    
    
    static class Builder {
        
        private final Object subscriber;
        private final Method method;
        private final Class<?> eventClass;
        
        private int handlerPriority = Priority.NORMAL;
        private int subscriberPriority = Priority.NORMAL;
        
        
        Builder(Object subscriber, Method method, Class<?> eventClass) {
            this.subscriber = subscriber;
            this.method = method;
            this.eventClass = eventClass;
        }
        
        
        Builder handlerPriority(int priority) {
            this.handlerPriority = priority;
            return this;
        }
        
        
        Builder subscriberPriority(int priority) {
            this.subscriberPriority = priority;
            return this;
        }
        
        
        Handler build() {
            return new Handler(this);
        }
        
    }
    
}

