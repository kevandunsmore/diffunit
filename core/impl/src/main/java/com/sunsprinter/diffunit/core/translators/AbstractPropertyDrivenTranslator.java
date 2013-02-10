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

package com.sunsprinter.diffunit.core.translators;


import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.lang3.StringUtils;


/**
 * Abstract base for translators that convert objects to strings by examining property values.
 *
 * @author Kevan Dunsmore
 * @created 2011/11/10
 */
public abstract class AbstractPropertyDrivenTranslator<T> extends AbstractDelegatingTranslator<T>
{
    private Collection<String> _propertiesToTranslate = new LinkedHashSet<>();
    private Set<String> _propertiesToSkip = new HashSet<>(Arrays.asList("class"));


    @SuppressWarnings({"unchecked", "ConstantConditions"})
    public <I extends AbstractPropertyDrivenTranslator<T>> I translateProperties(final String... propertiesToTranslate)
    {
        if (propertiesToTranslate != null)
        {
            if (getPropertiesToTranslate() == null)
            {
                setPropertiesToTranslate(new LinkedList<String>());
            }

            getPropertiesToTranslate().addAll(Arrays.asList(propertiesToTranslate));
        }

        return (I)this;
    }


    @SuppressWarnings("unchecked")
    public <I extends AbstractPropertyDrivenTranslator<T>> I skipProperties(final String... propertiesToSkip)
    {
        if (propertiesToSkip != null)
        {
            getPropertiesToSkip().addAll(Arrays.asList(propertiesToSkip));
        }
        return (I)this;
    }


    protected Collection<String> getPropertiesToTranslate()
    {
        return _propertiesToTranslate;
    }


    protected void setPropertiesToTranslate(final Collection<String> propertiesToTranslate)
    {
        _propertiesToTranslate = propertiesToTranslate;
    }


    protected Set<String> getPropertiesToSkip()
    {
        return _propertiesToSkip;
    }


    protected void setPropertiesToSkip(Set<String> propertiesToSkip)
    {
        _propertiesToSkip = propertiesToSkip;
    }


    protected Collection<PropertyDescriptor> determinePropertiesEligibleForTranslation(final T object) throws TranslationException
    {
        // We don't have a map of property names to properties.  We create one now.  We use a linked hash map to
        // preserve the order of the properties.  After all, we've gone to a lot of bother to order our properties
        // so the look nice in our test output.
        final Map<String, PropertyDescriptor> allPropertiesMap = buildAllPropertiesMap(object);

        final Collection<PropertyDescriptor> properties;
        if (getPropertiesToTranslate().isEmpty())
        {
            // The client hasn't specified the properties to translate so we do all of them.
            properties = allPropertiesMap.values();
        }
        else
        {
            properties = new LinkedList<>();

            // The client has specified the properties we have to translate.
            for (final String propertyName : getPropertiesToTranslate())
            {
                final PropertyDescriptor propertyDescriptor = allPropertiesMap.get(propertyName);
                if (propertyDescriptor == null)
                {
                    // Oops.  The client specified a property that doesn't exist.
                    throw new TranslationException(object,
                                                   String.format("Property '%s' does not exist on object '%s'.  " +
                                                                         "Available properties are '%s'.",
                                                                 propertyName,
                                                                 getInstanceTracker().getObjectId(object),
                                                                 StringUtils.join(allPropertiesMap.keySet(),
                                                                                  "', '")));
                }

                // Skip write-only properties.  Seriously, who does write-only properties?  You'd be surprised.
                if (propertyDescriptor.getReadMethod() != null)
                {
                    properties.add(propertyDescriptor);
                }
            }
        }

        // Now build up our eligible properties collection by adding everything except the things we've been told to skip.
        final Collection<PropertyDescriptor> eligibleProperties = createEligiblePropertiesCollection();
        for (final PropertyDescriptor propertyDescriptor : properties)
        {
            if (!getPropertiesToSkip().contains(propertyDescriptor.getName()))
            {
                eligibleProperties.add(propertyDescriptor);
            }
        }

        return eligibleProperties;
    }


    protected Map<String, PropertyDescriptor> buildAllPropertiesMap(final T object) throws TranslationException
    {
        final Map<String, PropertyDescriptor> allPropertiesMap = new LinkedHashMap<String, PropertyDescriptor>();

        for (final PropertyDescriptor propertyDescriptor : retrieveAllProperties(object))
        {
            // Skip write-only properties.
            if (propertyDescriptor.getReadMethod() != null)
            {
                allPropertiesMap.put(propertyDescriptor.getName(), propertyDescriptor);
            }
        }

        return allPropertiesMap;
    }


    protected Collection<PropertyDescriptor> retrieveAllProperties(final T object) throws TranslationException
    {
        try
        {
            final Collection<PropertyDescriptor> properties = new TreeSet<PropertyDescriptor>(createPropertyDescriptorComparator());
            properties.addAll(Arrays.asList(Introspector.getBeanInfo(object.getClass()).getPropertyDescriptors()));
            return properties;
        }
        catch (final IntrospectionException e)
        {
            throw new TranslationException(object,
                                           String.format("Unable to retrieve properties for object '%s'.",
                                                         getInstanceTracker().getObjectId(object)));
        }
    }


    /**
     * Factory method to create a comparator for {@link PropertyDescriptor} objects.  By default creates an instance of
     * {@link PropertyDescriptorComparator}.
     *
     * @return A comparator for {@link PropertyDescriptor} objects.
     */
    protected Comparator<PropertyDescriptor> createPropertyDescriptorComparator()
    {
        return new PropertyDescriptorComparator();
    }


    /**
     * Comparator implementation that sorts {@link PropertyDescriptor} objects by declaring class (of write method) then
     * by name. Properties declared by superclasses are considered less than those of a subclass.  If both classes are
     * the same, properties are sorted alphabetically by name.
     */
    protected class PropertyDescriptorComparator implements Comparator<PropertyDescriptor>
    {
        public int compare(final PropertyDescriptor left, final PropertyDescriptor right)
        {
            // Short-circuit for the same instances passed as both params.
            if (left == right)
            {
                return 0;
            }

            final Class<?> leftReadMethodDeclarer = left.getReadMethod() == null ? null : left.getReadMethod().getDeclaringClass();
            final Class<?> leftWriteMethodDeclarer = left.getWriteMethod() == null ? null : left.getWriteMethod().getDeclaringClass();

            final Class<?> rightReadMethodDeclarer = right.getReadMethod() == null ? null : right.getReadMethod().getDeclaringClass();
            final Class<?> rightWriteMethodDeclarer = right.getWriteMethod() == null ? null : right.getWriteMethod().getDeclaringClass();

            final Class<?> leftClass = determineMostSpecific(leftReadMethodDeclarer, leftWriteMethodDeclarer);
            final Class<?> rightClass = determineMostSpecific(rightReadMethodDeclarer, rightWriteMethodDeclarer);

            if (leftClass == rightClass)
            {
                // Both properties are from the same class.  We compare by name.
                return left.getName().compareTo(right.getName());
            }

            // The properties were declared by different classes.  More-specific is higher than less-specific.  The
            // previous check means that the two classes at this point are different.
            if (leftClass.isAssignableFrom(rightClass))
            {
                // The left class is a superclass of the right so it is smaller.
                return -1;
            }

            // The left class is a subclass of the right, so it is higher.
            return 1;
        }
    }


    protected Class<?> determineMostSpecific(final Class<?> left, final Class<?> right)
    {
        Class<?> mostSpecific;

        if (left == null)
        {
            mostSpecific = right;
        }
        else if (right == null)
        {
            mostSpecific = left;
        }
        else
        {
            mostSpecific = left.isAssignableFrom(right) ? right : left;
        }

        return mostSpecific;
    }


    protected Collection<PropertyDescriptor> createEligiblePropertiesCollection()
    {
        return new LinkedList<PropertyDescriptor>();
    }
}
