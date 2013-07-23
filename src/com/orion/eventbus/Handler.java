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
 **/
package com.orion.eventbus;

import static com.google.common.base.Preconditions.*;
import com.orion.exception.HandlerInvocationException;
import java.lang.reflect.Method;


public class Handler
{
    private final int subscriberPriority;
    private final int handlerPriority;
    private final Method method;
    private final Object subscriber;
    private final Class<?> eventClass;
    private final InvocationPrecondition precondition;
    
    
    private Handler(Builder builder)
    {
        this.subscriber = checkNotNull(builder.subscriber);
        this.method = checkNotNull(builder.method);
        this.eventClass = checkNotNull(builder.eventClass);
        this.precondition = checkNotNull(builder.invocationPrecondition);
        this.subscriberPriority = builder.subscriberPriority;
        this.handlerPriority = builder.handlerPriority;
    }
    
    
    public final int getSubscriberPriority()
    {
        return subscriberPriority;
    }
    
    
    public final int getHandlerPriority()
    {
        return handlerPriority;
    }
    
    
    public final Object getSubscriber()
    {
        return subscriber;
    }
    
    
    public final Class<?> getEventClass()
    {
        return eventClass;
    }
    
    
    public final void invoke(Object event)
    {
        if (!precondition.isSatisfied(subscriber)) return;
        
        try
        {
            method.setAccessible(true);
            method.invoke(subscriber, event);
        }
        catch (ReflectiveOperationException ex)
        {
            throw new HandlerInvocationException(ex);
        }
    }
    
    
    public static class Builder
    {
        private final Object subscriber;
        private final Method method;
        private final Class<?> eventClass;
        
        private int handlerPriority = Priority.NORMAL;
        private int subscriberPriority = Priority.NORMAL;
        private InvocationPrecondition invocationPrecondition = InvocationPrecondition.NONE;
        
        
        public Builder(Object subscriber, Method method, Class<?> eventClass)
        {
            this.subscriber = subscriber;
            this.method = method;
            this.eventClass = eventClass;
        }
        
        
        public Builder handlerPriority(int priority)
        {
            this.handlerPriority = priority;
            return this;
        }
        
        
        public Builder subscriberPriority(int priority)
        {
            this.subscriberPriority = priority;
            return this;
        }
        
        
        public Builder invocationPrecondition(InvocationPrecondition precondition)
        {
            this.invocationPrecondition = precondition;
            return this;
        }
        
        
        public Handler build()
        {
            return new Handler(this);
        }
    }
    
}

