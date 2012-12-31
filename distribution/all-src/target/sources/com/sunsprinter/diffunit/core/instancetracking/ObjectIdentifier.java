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


/**
 * ObjectIdentifier
 *
 * @author Kevan Dunsmore
 * @created 2011/11/11
 */
public class ObjectIdentifier implements IObjectIdentifier
{
    private Object _object;
    private int _instanceNumber;


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


    protected void setObject(final Object object)
    {
        _object = object;
    }


    public int getInstanceNumber()
    {
        return _instanceNumber;
    }


    protected void setInstanceNumber(final int instanceNumber)
    {
        _instanceNumber = instanceNumber;
    }


    @Override
    public String toString()
    {
        return String.format("%s#%d", getObject().getClass().getName(), getInstanceNumber());
    }


    @Override
    public int hashCode()
    {
        return System.identityHashCode(getObject());
    }


    @Override
    public boolean equals(final Object obj)
    {
        return obj instanceof IObjectIdentifier && hashCode() == obj.hashCode();
    }
}
