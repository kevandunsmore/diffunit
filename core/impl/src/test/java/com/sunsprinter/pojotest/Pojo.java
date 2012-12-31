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

package com.sunsprinter.pojotest;


import java.util.Collection;


/**
 * Pojo
 *
 * @author Kevan Dunsmore
 * @created 2011/12/07
 */
public class Pojo
{
    private String _name;
    private Collection<String> _psuedonyms;
    private String _propertyWithNonNullDefault = "wibble";
    private final String _readOnlyProperty;
    private String _propertyWithCustomAccessorAndMutator;


    public Pojo()
    {
        _readOnlyProperty = "giblets";
    }


    public String getName()
    {
        return _name;
    }


    public void setName(final String name)
    {
        _name = name;
    }


    Collection<String> getPsuedonyms()
    {
        return _psuedonyms;
    }


    private void setPsuedonyms(final Collection<String> psuedonyms)
    {
        _psuedonyms = psuedonyms;
    }


    public String getPropertyWithNonNullDefault()
    {
        return _propertyWithNonNullDefault;
    }


    public void setPropertyWithNonNullDefault(final String propertyWithNonNullDefault)
    {
        _propertyWithNonNullDefault = propertyWithNonNullDefault;
    }


    public String getReadOnlyProperty()
    {
        return _readOnlyProperty;
    }


    public void customMutator(final String newValue)
    {
        _propertyWithCustomAccessorAndMutator = newValue;
    }


    public String customAccessor()
    {
        return _propertyWithCustomAccessorAndMutator;
    }
}
