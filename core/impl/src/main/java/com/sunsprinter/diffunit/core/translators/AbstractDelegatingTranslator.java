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


import com.sunsprinter.diffunit.core.context.TestingContextHolder;
import com.sunsprinter.diffunit.core.instancetracking.IObjectIdentifier;


/**
 * Abstract base for translators that use a delegate translator for some or all of the translation process.  Such
 * subclasses may implement the Decorator pattern or simply perform as a Composite.  The delegate may be used for all or
 * part of the translation work.<p/>
 *
 * If a delegate translator is not specified using the {@link #use(ITranslator)} method then this class will return
 * the root translator, as obtained from the testing context supplied by {@link TestingContextHolder}.
 *
 * @author Kevan Dunsmore
 * @created 2011/11/12
 */
public abstract class AbstractDelegatingTranslator<T> extends AbstractTranslator<T>
{
    /**
     * Holds the translator used to convert {@link IObjectIdentifier} instances to a string.
     */
    private ITranslator<Object> _delegateTranslator;


    protected ITranslator<Object> getDelegateTranslator()
    {
        return _delegateTranslator == null ? getTestingContext().getRootTranslator() : _delegateTranslator;
    }


    protected void setDelegateTranslator(final ITranslator<Object> delegateTranslator)
    {
        _delegateTranslator = delegateTranslator;
    }


    /**
     * Specifies the delegate translator to use.
     *
     * @param delegate The delegate to use.  May not be <code>null</code>.
     *
     * @return This translator, to facilitate call chaining.  Will never be <code>null</code>.
     */
    @SuppressWarnings("unchecked")
    public <I extends AbstractDelegatingTranslator<T>> I use(final ITranslator<Object> delegate)
    {
        setDelegateTranslator(delegate);
        return (I)this;
    }
}
