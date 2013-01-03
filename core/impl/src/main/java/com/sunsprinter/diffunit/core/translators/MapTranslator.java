/*
 * Copyright 2011 Kevan Dunsmore.  All rights reserved.
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

package com.sunsprinter.diffunit.core.translators;


import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;


/**
 * MapTranslator
 *
 * @author Kevan Dunsmore
 * @created 2012/06/03
 */
public class MapTranslator<T extends Map<Object, Object>> extends AbstractCollectionTranslator<T>
{
    private Comparator<Object> _keyComparator = new ToStringKeyComparator();


    @SuppressWarnings("unchecked")
    @Override
    protected Iterator<Object> convertToIterator(T map) throws TranslationException
    {
        final Map sortedMap;
        if (map instanceof SortedMap)
        {
            sortedMap = map;
        }
        else
        {
            // We have to impose some sorting strategy on the map keys.
            sortedMap = new TreeMap<Object, Object>(getKeyComparator());
            sortedMap.putAll(map);
        }

        // Now shove everything in a collection and return the iterator for it to the superclass.
        final Collection<Object> kvpCollection = new LinkedList<Object>();
        for (final Object key : map.keySet())
        {
            kvpCollection.add(createKeyValuePair(key, sortedMap.get(key)));
        }

        return kvpCollection.iterator();
    }


    public MapTranslator<T> setKeyComparator(final Comparator<Object> comparator)
    {
        _keyComparator = comparator;
        return this;
    }


    @SuppressWarnings("unchecked")
    public <KC extends Comparator<Object>> KC getKeyComparator()
    {
        return (KC)_keyComparator;
    }


    protected KeyValuePair createKeyValuePair(final Object key, final Object value)
    {
        return new KeyValuePair(key, value);
    }


    public static class ToStringKeyComparator implements Comparator<Object>
    {
        @Override
        public int compare(final Object o1, final Object o2)
        {
            if (o1 == null && o2 == null)
            {
                return 0;
            }
            if (o1 != null && o2 == null)
            {
                return 1;
            }
            if (o1 == null)
            {
                return -1;
            }

            return o1.toString().compareTo(o2.toString());
        }
    }


    public static class KeyValuePair
    {
        private Object _key;
        private Object _value;


        public KeyValuePair(final Object key, final Object value)
        {
            _key = key;
            _value = value;
        }


        public Object getKey()
        {
            return _key;
        }


        public void setKey(final Object key)
        {
            _key = key;
        }


        public Object getValue()
        {
            return _value;
        }


        public void setValue(final Object value)
        {
            _value = value;
        }
    }
}
