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

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class AnnotationProcessor {
    
    public static List<Handler> process(Object obj, int priority) {
        
        List<Handler> collectedHandlers = new ArrayList<>();
        
        for (Method method : obj.getClass().getMethods()) {
            
            EventHandler annotation = method.getAnnotation(EventHandler.class);
            
            // Skip methods which aren't annotated with @EventHandler
            if (annotation == null) continue;
            
            Class<?>[] parameterTypes = method.getParameterTypes();
            
            // Verify the requirement that annotated @EventHandler methods must have
            // exactly one parameter for which its type defines which events shall be
            // dispatched to this method
            if (parameterTypes.length != 1) 
                throw new IllegalArgumentException(); // TODO: Add exception message
                        
            // Collect handler info
            Class<?> eventClass = parameterTypes[0];
            
            collectedHandlers.add(
                new Handler.Builder(obj, method, eventClass)
                    .handlerPriority(annotation.priority())
                    .subscriberPriority(priority)
                    .invocationPrecondition(InvocationPrecondition.NONE)
                    .build());
        }
        
        return collectedHandlers;
    }
    
}
