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


import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;


/**
 * TypeBindingTranslator
 *
 * @author Kevan Dunsmore
 * @created 2011/11/10
 */
public class TypeBindingTranslator extends AbstractTranslator<Object> implements ITypeBindingTranslator
{
    private ITranslator<Object> _defaultTranslator;
    private Map<Class<?>, ITranslator<Object>> _translatorMap;
    private Set<Integer> _translationScopeSet;


    public TypeBindingTranslator()
    {
        _defaultTranslator = createDefaultTranslator();
        _translatorMap = createTranslatorMap();
        _translationScopeSet = createTranslationScopeSet();
    }


    public void bind(final ITranslator<?> translator, final Class<?>... types)
    {
        for (final Class<?> type : types)
        {
            getTranslatorMap().put(type, createCircularTranslationPreventionDecorator(translator));
        }
    }


    @Override
    protected String doTranslate(final Object object) throws TranslationException
    {
        return retrieveTranslatorForClass(object.getClass()).translate(object);
    }


    protected ITranslator<Object> getDefaultTranslator()
    {
        return _defaultTranslator;
    }


    protected void setDefaultTranslator(final ITranslator<Object> defaultTranslator)
    {
        _defaultTranslator = defaultTranslator;
    }


    protected Map<Class<?>, ITranslator<Object>> getTranslatorMap()
    {
        return _translatorMap;
    }


    protected void setTranslatorMap(final Map<Class<?>, ITranslator<Object>> translatorMap)
    {
        _translatorMap = translatorMap;
    }


    protected Set<Integer> getTranslationScopeSet()
    {
        return _translationScopeSet;
    }


    protected void setTranslationScopeSet(final Set<Integer> translationScopeSet)
    {
        _translationScopeSet = translationScopeSet;
    }


    protected ITranslator<Object> retrieveTranslatorForClass(final Class<?> aClass)
    {
        ITranslator<Object> translator;

        if (aClass == null)
        {
            // We've walked all the way up the hierarchy and not found anything so we return the default translator
            // for this object.
            translator = getDefaultTranslator();
        }
        else
        {
            translator = getTranslatorMap().get(aClass);
            if (translator == null)
            {
                // Nope - nothing bound for this class.  Let's try its interfaces.
                final Iterator<Class<?>> interfaces = Arrays.asList(aClass.getInterfaces()).iterator();
                while (translator == null && interfaces.hasNext())
                {
                    translator = getTranslatorMap().get(interfaces.next());
                }

                if (translator == null)
                {
                    // Nothing for this class or its interfaces.  Check the superclass.
                    translator = retrieveTranslatorForClass(aClass.getSuperclass());
                }
            }
        }

        return translator;
    }


    /**
     * Factory method to create the default translator.  By default returns a new instance of {@link
     * ObjectInstanceTrackingTranslator}. Subclasses may override to supply a testCustomLocationOnFileSystemAtMethodLevel default translator.
     *
     * @return The default translator.  Will never be <code>null</code>.
     */
    protected ITranslator<Object> createDefaultTranslator()
    {
        return new ObjectInstanceTrackingTranslator();
    }


    /**
     * Factory method to create the translator map.  Creates a new instance of {@link HashMap}.  Subclasses may override
     * to supply a testCustomLocationOnFileSystemAtMethodLevel translator map.
     *
     * @return The translator map.  Will never be <code>null</code>.
     */
    protected Map<Class<?>, ITranslator<Object>> createTranslatorMap()
    {
        return new HashMap<>();
    }


    /**
     * Factory method to create translation scope set.  Creates a new instance of {@link HashSet}.  Subclasses may
     * override to supply a testCustomLocationOnFileSystemAtMethodLevel {@link java.util.Set} type.
     *
     * @return The translation scope set.  Will never be <code>null</code>.
     */
    protected Set<Integer> createTranslationScopeSet()
    {
        return new HashSet<>();
    }


    /**
     * Factory method to create a translator decorator that will prevent circular translations.  Circular translations
     * are bad because they result in stack overflows.
     *
     * @param toBeDecorated The translator to be decorated.  May not be <code>null</code>.
     *
     * @return The translator decorator that prevents circular translations.  Will never be <code>null</code>.
     */
    protected ITranslator<Object> createCircularTranslationPreventionDecorator(final ITranslator<?> toBeDecorated)
    {
        return new CircularTranslationPreventionDecorator(toBeDecorated);
    }


    /**
     * This decorator translator ensures that translation only occurs for objects not currently being translated.  In
     * other words, if you have an object graph A->B->C->A...  Then this decorator will detect the second attempt to
     * translate A and prevent it.
     */
    protected class CircularTranslationPreventionDecorator implements ITranslator<Object>
    {
        /**
         * The delegate translator that does most of the work.
         */
        private final ITranslator<Object> _delegate;


        @SuppressWarnings("unchecked")
        public CircularTranslationPreventionDecorator(final ITranslator<?> delegate)
        {
            _delegate = (ITranslator<Object>)delegate;
        }


        @Override
        public String translate(final Object object) throws TranslationException
        {
            final int identityHashCode = System.identityHashCode(object);

            // We don't want to track nulls.  The identity hash code of null is always 0.
            if (identityHashCode == 0)
            {
                // We've seen this object before on our travels.  We don't translate it because we'll end up with a
                // circular translation and, eventually, a stack overflow.
                return String.format("Translation of '%s' skipped to prevent stack overflow",
                                     getInstanceTracker().getObjectId(object));
            }

            try
            {
                getTranslationScopeSet().add(identityHashCode);
                return getDelegate().translate(object);
            }
            finally
            {
                getTranslationScopeSet().remove(identityHashCode);
            }
        }


        /**
         * @return The delegate translator responsible for the actual translation of objects not seen before.
         */
        protected ITranslator<Object> getDelegate()
        {
            return _delegate;
        }
    }
}
