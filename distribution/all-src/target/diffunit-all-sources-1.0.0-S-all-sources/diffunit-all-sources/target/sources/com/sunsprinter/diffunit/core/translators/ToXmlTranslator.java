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


import java.beans.PropertyDescriptor;
import java.util.Collection;

import org.apache.commons.lang3.StringEscapeUtils;


/**
 * Translator that converts objects to XML.  The XML is formed by creating an outer element from the short class name
 * which has the instance tracker instance number as an attribute.  The inner elements are formed from the names of the
 * properties of the class.  The contents of the inner elements are generated by calling the delegate translator.<p/>
 *
 * Outer element generation can be turned on and off by calling {@link #includeEnclosingElement(boolean)}.<p/>
 *
 * Instance number generation can be turned on and off by calling {@link #includeInnerElementInstanceNumber(boolean)}
 * and {@link #includeOuterElementInstanceNumber(boolean)}.<p/>
 *
 * @author Kevan Dunsmore
 * @created 2011/11/10
 */
public class ToXmlTranslator<T> extends AbstractPropertyDrivenTranslator<T>
{
    private boolean _includeEnclosingElement = true;
    private boolean _includeOuterElementInstanceNumber;
    private boolean _includeInnerElementInstanceNumber;
    private boolean _escapePropertyValues = false;


    @SuppressWarnings("unchecked")
    public <I extends ToXmlTranslator<T>> I includeEnclosingElement(final boolean include)
    {
        setIncludeEnclosingTags(include);
        return (I)this;
    }


    @SuppressWarnings("unchecked")
    public <I extends ToXmlTranslator<T>> I includeInnerElementInstanceNumber(final boolean include)
    {
        setIncludeInnerElementInstanceNumber(include);
        return (I)this;
    }


    @SuppressWarnings("unchecked")
    public <I extends ToXmlTranslator<T>> I includeOuterElementInstanceNumber(final boolean include)
    {
        setIncludeOuterElementInstanceNumber(include);
        return (I)this;
    }


    @SuppressWarnings("unchecked")
    public <I extends ToXmlTranslator<T>> I escapePropertyValues(final boolean include)
    {
        setEscapePropertyValues(include);
        return (I)this;
    }


    protected boolean getEscapePropertyValues()
    {
        return _escapePropertyValues;
    }


    protected void setEscapePropertyValues(final boolean escapePropertyValues)
    {
        _escapePropertyValues = escapePropertyValues;
    }


    protected boolean getIncludeEnclosingTags()
    {
        return _includeEnclosingElement;
    }


    protected void setIncludeEnclosingTags(final boolean includeEnclosingTags)
    {
        _includeEnclosingElement = includeEnclosingTags;
    }


    protected boolean getIncludeInnerElementInstanceNumber()
    {
        return _includeInnerElementInstanceNumber;
    }


    protected void setIncludeInnerElementInstanceNumber(final boolean includeInnerElementInstanceNumber)
    {
        _includeInnerElementInstanceNumber = includeInnerElementInstanceNumber;
    }


    protected boolean getIncludeOuterElementInstanceNumber()
    {
        return _includeOuterElementInstanceNumber;
    }


    protected void setIncludeOuterElementInstanceNumber(final boolean includeOuterElementInstanceNumber)
    {
        _includeOuterElementInstanceNumber = includeOuterElementInstanceNumber;
    }


    @Override
    protected String doTranslate(final T object) throws TranslationException
    {
        String currentPropertyName = "DIFFUNIT UNKNOWN";
        try
        {
            final StringBuilder sb = new StringBuilder();

            if (getIncludeEnclosingTags())
            {
                sb.append(createStartTag(object.getClass().getSimpleName(), object, getIncludeOuterElementInstanceNumber()));
            }

            final Collection<PropertyDescriptor> eligibleProperties = determinePropertiesEligibleForTranslation(object);
            for (final PropertyDescriptor propertyDescriptor : eligibleProperties)
            {
                currentPropertyName = propertyDescriptor.getName();
                final Object propertyValue = propertyDescriptor.getReadMethod().invoke(object);
                String propertyValueAsString = getDelegateTranslator().translate(propertyValue);
                if (getEscapePropertyValues())
                {
                    propertyValueAsString = StringEscapeUtils.escapeXml(propertyValueAsString);
                }
                sb.append(String.format("%s%s%s",
                                        createStartTag(propertyDescriptor.getName(), propertyValue, getIncludeInnerElementInstanceNumber()),
                                        propertyValueAsString,
                                        createEndTag(propertyDescriptor.getName())));
            }

            if (getIncludeEnclosingTags())
            {
                sb.append(createEndTag(object.getClass().getSimpleName()));
            }

            return sb.toString();
        }
        catch (final Exception e)
        {
            throw new TranslationException(object,
                                           String.format("Unable to translate property '%s' of object '%s'",
                                                         currentPropertyName,
                                                         getInstanceTracker().getObjectId(object)),
                                           e);
        }
    }


    protected String createStartTag(final String tagName, final Object object, final boolean includeInstanceNumber)
    {
        final StringBuilder sb = new StringBuilder(String.format("<%s", tagName));

        if (includeInstanceNumber)
        {
            sb.append(String.format(" instanceNumber=\"%d\"", getInstanceTracker().getObjectId(object).getInstanceNumber()));
        }

        sb.append(">");

        return sb.toString();
    }


    protected String createEndTag(final String tagName)
    {
        return String.format("</%s>", tagName);
    }
}