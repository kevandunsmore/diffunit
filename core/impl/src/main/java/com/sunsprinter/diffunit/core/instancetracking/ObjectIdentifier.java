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
 * Implementation of the {@link com.sunsprinter.diffunit.core.instancetracking.IObjectIdentifier} interface.  The hash
 * code of an instance of this object is the identity hash code of the object being tracked.  Provides a decent {@link
 * #toString()} implementation to allow the standard string translation policy of DiffUnit to be effective and
 * reproducible.
 *
 * @author Kevan Dunsmore
 * @created 2011/11/11
 */
public class ObjectIdentifier implements IObjectIdentifier
{
    /**
     * The object being tracked.
     */
    private Object _object;

    /**
     * The instance number of the object being tracked.
     */
    private int _instanceNumber;


    /**
     * Default constructor, provided for extensitiblity.
     */
    protected ObjectIdentifier()
    {
    }


    public ObjectIdentifier(final Object object, final int instanceNumber)
    {
        _object = object;
        _instanceNumber = instanceNumber;
    }


    @SuppressWarnings("unchecked")
    public <T> T getObject()
    {
        return (T)_object;
    }


    /**
     * Sets the object being tracked.  Provided for extensibility.
     *
     * @param object The object to set.
     */
    protected void setObject(final Object object)
    {
        _object = object;
    }


    public int getInstanceNumber()
    {
        return _instanceNumber;
    }


    /**
     * Sets the instance number.  Provided for extensibility.
     *
     * @param instanceNumber The instance number to use.
     */
    protected void setInstanceNumber(final int instanceNumber)
    {
        _instanceNumber = instanceNumber;
    }


    /**
     * Returns a string representation of this object identifier in the form {FullyQualifiedTrackedObjectClassName}#{TrackedObjectInstanceNumber}.
     */
    @Override
    public String toString()
    {
        return String.format("%s#%d", getObject().getClass().getName(), getInstanceNumber());
    }


    /**
     * Returns the identify hash code of the object being tracked.
     */
    @Override
    public int hashCode()
    {
        return System.identityHashCode(getObject());
    }


    /**
     * True if the supplied object is an instance of {@link com.sunsprinter.diffunit.core.instancetracking.IObjectIdentifier}
     * and its identity hash code is equal to our identity hash code.
     */
    @Override
    public boolean equals(final Object obj)
    {
        return obj instanceof IObjectIdentifier && hashCode() == obj.hashCode();
    }
}
