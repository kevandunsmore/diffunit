/*
 * Copyright 2011-2013 Kevan Dunsmore.  All rights reserved.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.sunsprinter.diffunit.core.instancetracking;


/**
 * The instance tracker will return a unique {@link com.sunsprinter.diffunit.core.instancetracking.IObjectIdentifier}
 * when presented with any object.  The object id will remain constant on subsequent calls to {@link
 * #getObjectId(Object)} and will remain constant upon multiple invocations of a test.  The id remains constant because
 * it is based on the order in which an object is presented to the tracker.  For example:<p/>
 *
 * <pre>
 *     String aString = "hello";
 *     String anotherString = "world";
 *     Foo foo = new Foo();
 *     Foo foo2 = new Foo();
 *     Bar bar = new Bar();
 *     Bar bar2 = new Bar();
 *
 *     // Object id's instance number will be 1.
 *     IObjectIdentifier id = instanceTracker.getObjectId(aString);
 *
 *     // Same object id returned.
 *     id = instanceTracker.getObjectId(aString);
 *
 *     // Object id's instance number will be 2 because this is the second java.lang.String object the tracker has
 * seen.
 *     id = instanceTracker.getObjectId(anotherString);
 *
 *     // Object id's instance number will be 1 because this is the first Foo object the tracker has seen.
 *     id = instanceTracker.getObjectId(foo);
 *
 *     // Again, the same object id instance as above is returned.
 *     id = instanceTracker.getObjectId(foo);
 *
 *     // Object id's instance number will be 2 because this is the second Foo object the tracker has seen.
 *     id = instanceTracker.getObjectId(foo2);
 * </pre><p/>
 *
 * The instance tracker is an easy way to get a reproducible string form for an object if you don't really care what's
 * inside the object.  Think of the {@link com.sunsprinter.diffunit.core.instancetracking.IObjectIdentifier}s as unique
 * black boxes.<p/>
 *
 * @author Kevan Dunsmore
 * @created 2011/11/11
 */
public interface IObjectInstanceTracker
{
    /**
     * Resets all references known to this translator.
     */
    void reset();

    /**
     * Returns the object id for the supplied object.
     *
     * @param object The object of interest.  May be null.
     *
     * @return The unique id for the object or null if the supplied object reference is null.
     */
    IObjectIdentifier getObjectId(Object object);
}
