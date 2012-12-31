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


import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.sunsprinter.diffunit.core.common.AbstractTestingContextUser;


/**
 * RegExReplacementTranslatorDecorator
 *
 * @author Kevan Dunsmore
 * @created 2011/11/14
 */
public class RegExReplacementTranslatorDecorator<T extends ITranslator> extends AbstractTestingContextUser implements InvocationHandler
{
    private ITranslator _delegate;
    private T _proxy;
    private List<IRegExReplacementPair> _replacementPairs = createReplacementPairsCollection();


    @SuppressWarnings("unchecked")
    public RegExReplacementTranslatorDecorator(final ITranslator delegate)
    {
        _delegate = delegate;
        _proxy = (T)Proxy.newProxyInstance(getDelegate().getClass().getClassLoader(),
                                           getDelegate().getClass().getInterfaces(),
                                           this);
    }


    public List<IRegExReplacementPair> getReplacementPairs()
    {
        return _replacementPairs;
    }


    public void setReplacementPairs(final List<IRegExReplacementPair> replacementPairs)
    {
        _replacementPairs = replacementPairs;
    }


    protected List<IRegExReplacementPair> createReplacementPairsCollection()
    {
        return new LinkedList<IRegExReplacementPair>();
    }


    public T getProxy()
    {
        return _proxy;
    }


    protected void setProxy(final T proxy)
    {
        _proxy = proxy;
    }


    protected ITranslator getDelegate()
    {
        return _delegate;
    }


    protected void setDelegate(final ITranslator delegate)
    {
        _delegate = delegate;
    }


    @SuppressWarnings("unchecked")
    public <I extends RegExReplacementTranslatorDecorator<T>> I add(final IRegExReplacementPair... replacementPairs)
    {
        getReplacementPairs().addAll(Arrays.asList(replacementPairs));
        return (I)this;
    }


    @Override
    public Object invoke(final Object proxy, final Method method, final Object[] args) throws Throwable
    {
        Object retVal = method.invoke(getDelegate(), args);

        if (retVal != null &&
            method.getName().equals("translate") &&
            method.getReturnType().equals(String.class) &&
            method.getParameterTypes().length == 1 &&
            method.getParameterTypes()[0].equals(Object.class))
        {
            String updatedRetVal = (String)retVal;

            // Go through all replacement pairs and replace matching blocks.
            for (final IRegExReplacementPair pair : getReplacementPairs())
            {
                final Pattern pattern = Pattern.compile(pair.getRegExp());
                final Matcher matcher = pattern.matcher(updatedRetVal);

                final StringBuffer sb = new StringBuffer();

                while (matcher.find())
                {
                    // Get the matched text.
                    final String matched = updatedRetVal.substring(matcher.start(), matcher.end());

                    // Get translate the replacement object into a string then use it to replace the matched string.
                    final String replacement = pair.getTranslator().translate(matched);
                    matcher.appendReplacement(sb, replacement);
                }
                matcher.appendTail(sb);

                updatedRetVal = sb.toString();
            }

            retVal = updatedRetVal;
        }

        return retVal;
    }
}
