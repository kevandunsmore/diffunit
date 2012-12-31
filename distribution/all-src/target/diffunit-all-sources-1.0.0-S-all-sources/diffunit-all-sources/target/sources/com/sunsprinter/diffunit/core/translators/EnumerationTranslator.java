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

package com.sunsprinter.diffunit.core.translators;


import java.util.Enumeration;
import java.util.Iterator;


/**
 * EnumerationTranslator
 *
 * @author Kevan Dunsmore
 * @created 2011/11/12
 */
public class EnumerationTranslator<T extends Enumeration<Object>> extends AbstractCollectionTranslator<T>
{
    @Override
    protected Iterator<Object> convertToIterator(final T object) throws TranslationException
    {
        return new Iterator<Object>()
        {
            @Override
            public boolean hasNext()
            {
                return object.hasMoreElements();
            }


            @Override
            public Object next()
            {
                return object.nextElement();
            }


            @Override
            public void remove()
            {
                throw new UnsupportedOperationException();
            }
        };
    }
}
