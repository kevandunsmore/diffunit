/*
 * Copyright 2012 Kevan Dunsmore.  All rights reserved.
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


import java.util.HashMap;
import java.util.Map;


/**
 * ObjectInstanceTracker
 *
 * @author Kevan Dunsmore
 * @created 2011/11/11
 */
public class ObjectInstanceTracker implements IObjectInstanceTracker
{
    /**
     * Contains counts of the number of times this translator has seen objects of specific types.
     */
    private final Map<Class<?>, Integer> _referenceCountMap = new HashMap<Class<?>, Integer>();

    /**
     * Contains string ids of objects known to this translator, indexed by the object's identity hash code.
     */
    private final Map<Integer, IObjectIdentifier> _knownObjectMap = new HashMap<Integer, IObjectIdentifier>();


    public synchronized void reset()
    {
        _referenceCountMap.clear();
        _knownObjectMap.clear();
    }


    /**
     * Returns the map that holds counts of the number of times this translator has seen objects of specific types.
     *
     * @return The reference count map.  Will never be <code>null</code>.
     */
    protected Map<Class<?>, Integer> getReferenceCountMap()
    {
        return _referenceCountMap;
    }


    /**
     * Returns the map that contains ids of objects known to this translator, indexed by the object's identity hash
     * code.
     *
     * @return The known object map.  Will never be <code>null</code>.
     */
    protected Map<Integer, IObjectIdentifier> getKnownObjectMap()
    {
        return _knownObjectMap;
    }


    public synchronized IObjectIdentifier getObjectId(final Object object)
    {
        // Get the object's id from our known object map.
        IObjectIdentifier id = getKnownObjectMap().get(System.identityHashCode(object));
        if (id == null)
        {
            // We don't know about this object.  Get the reference count from our ref count map.
            Integer referenceCount = getReferenceCountMap().get(object.getClass());
            if (referenceCount == null)
            {
                // This is the first time we've seen anything of this type.
                referenceCount = 0;
            }
            // We have one more of these things, so increment the reference count and put it back in the map for the
            // next time we encounter a different one of these.
            referenceCount++;
            getReferenceCountMap().put(object.getClass(), referenceCount);

            // Create an object id for this object.
            id = createObjectId(object, referenceCount);

            // Update our known object map so we know this object for next time.
            getKnownObjectMap().put(System.identityHashCode(object), id);
        }

        return id;
    }


    /**
     * Factory method to create {@link IObjectIdentifier} instances. Default behavior is to create instances of {@link
     * ObjectIdentifier}.
     *
     * @param object         The object for which the identifier must be created.  Will never be <code>null</code>.
     * @param instanceNumber The instance number of this object.
     *
     * @return A new identifier for the object.  May not be <code>null</code>.
     */
    protected IObjectIdentifier createObjectId(final Object object, final int instanceNumber)
    {
        return new ObjectIdentifier(object, instanceNumber);
    }
}
