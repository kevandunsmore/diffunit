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


import java.util.Iterator;

import org.apache.commons.lang3.SystemUtils;


/**
 * AbstractCollectionTranslator
 *
 * @author Kevan Dunsmore
 * @created 2011/11/12
 */
public abstract class AbstractCollectionTranslator<T> extends AbstractDelegatingTranslator<T>
{
    private String _preElementString = "";
    private String _surroundingString = "";
    private String _postElementString = "";
    private String _elementSeparator = "\n";


    protected String getSurroundingString()
    {
        return _surroundingString;
    }


    protected void setSurroundingString(final String surroundingString)
    {
        _surroundingString = surroundingString;
    }


    @SuppressWarnings("unchecked")
    public <I extends AbstractCollectionTranslator<T>> I surroundWith(final String text)
    {
        setSurroundingString(text);
        return (I)this;
    }


    protected String getElementSeparator()
    {
        return _elementSeparator;
    }


    protected void setElementSeparator(final String elementSeparator)
    {
        _elementSeparator = elementSeparator;
    }


    @SuppressWarnings("unchecked")
    public <I extends AbstractCollectionTranslator<T>> I separateElementsWith(final String text)
    {
        setElementSeparator(text);
        return (I)this;
    }


    protected String getPreElementString()
    {
        return _preElementString;
    }


    protected void setPreElementString(final String preElementString)
    {
        _preElementString = preElementString;
    }


    @SuppressWarnings("unchecked")
    public <I extends AbstractCollectionTranslator<T>> I prependElementsWith(final String text)
    {
        setPreElementString(text);
        return (I)this;
    }


    protected String getPostElementString()
    {
        return _postElementString;
    }


    protected void setPostElementString(final String postElementString)
    {
        _postElementString = postElementString;
    }


    @SuppressWarnings("unchecked")
    public <I extends AbstractCollectionTranslator<T>> I appendElementsWith(final String text)
    {
        setPreElementString(text);
        return (I)this;
    }


    @Override
    protected String doTranslate(final T object) throws TranslationException
    {
        final Iterator<Object> iterator = convertToIterator(object);

        final StringBuilder sb = new StringBuilder();

        if (iterator.hasNext())
        {
            sb.append(getPreElementString());
            sb.append(getDelegateTranslator().translate(iterator.next()));
            sb.append(getPostElementString());
        }
        while (iterator.hasNext())
        {
            sb.append(getElementSeparator());
            sb.append(getPreElementString());
            sb.append(getDelegateTranslator().translate(iterator.next()));
            sb.append(getPostElementString());
        }

        // Only surround contents when the buffer is not empty.
        return sb.length() == 0 ? "" : getSurroundingString() + sb.toString() + getSurroundingString();
    }


    protected abstract Iterator<Object> convertToIterator(T object) throws TranslationException;
}
