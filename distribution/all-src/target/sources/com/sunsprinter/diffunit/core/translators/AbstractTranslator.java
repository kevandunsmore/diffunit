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


import com.sunsprinter.diffunit.core.common.AbstractTestingContextUser;
import com.sunsprinter.diffunit.core.context.ITestingContext;
import com.sunsprinter.diffunit.core.context.TestingContextHolder;
import com.sunsprinter.diffunit.core.instancetracking.IObjectInstanceTracker;


/**
 * Abstract base for translators.  Provides the following functionality for subclasses:<p/>
 *
 * Testing context handling: This class allows customization of the testing context.  If the context is not customized,
 * this class will return {@link TestingContextHolder#CONTEXT} when {@link #getTestingContext()} is called.<p/>
 *
 * Null reference handling: Checks for null references and returns a consistent string translation for those references.
 * Subclasses will be called on the {@link #doTranslate(Object)} method, guaranteed not to be supplied a null
 * reference.<p/>
 *
 * Instance tracker handling: This class obtains its instance tracker from the {@link ITestingContext}.  The instance
 * tracker may be set explicitly, in which case it will be used in preference to the default one.  Subclasses can call
 * {@link #getInstanceTracker()} and be guaranteed to get a non-null tracker.<p/>
 *
 * Output prepend and append: Clients can configure what text, if any, should be placed before and after the output of
 * this translator.  By default, nothing is prepended or appended.  Prepend and append occurs only on a request to
 * translate a non-<code>null</code> reference.
 *
 * @author Kevan Dunsmore
 * @created 2011/11/10
 */
public abstract class AbstractTranslator<T> extends AbstractTestingContextUser implements ITranslator<T>
{
    /**
     * The instance tracker used by this translator.
     */
    private IObjectInstanceTracker _instanceTracker;

    /**
     * Contains text prepended to the output of this translator.  Defaults to empty string.  Only used when the object
     * translated is not null.
     */
    private String _preTranslationString = "";

    /**
     * Contains the text appended to the output of this translator.  Defaults to empty string.  Only used when the
     * object translated is not null.
     */
    private String _postTranslationString = "";


    public String getPreTranslationString()
    {
        return _preTranslationString;
    }


    protected void setPreTranslationString(final String preTranslationString)
    {
        _preTranslationString = preTranslationString;
    }


    @SuppressWarnings("unchecked")
    public <I extends AbstractTranslator<T>> I prependToOutput(final String text)
    {
        setPreTranslationString(text);
        return (I)this;
    }


    protected String getPostTranslationString()
    {
        return _postTranslationString;
    }


    protected void setPostTranslationString(final String postTranslationString)
    {
        _postTranslationString = postTranslationString;
    }


    @SuppressWarnings("unchecked")
    public <I extends AbstractTranslator<T>> I appendToOutput(final String text)
    {
        setPostTranslationString(text);
        return (I)this;
    }


    /**
     * Returns the instance tracker to use.
     *
     * @return The instance tracker set on this translator or, if no instance tracker has been set then the instance
     *         tracker obtained from the testing context provided by {@link ITestingContext} which is in turn provided
     *         by {@link TestingContextHolder}.  Will never be <code>null</code>.
     */
    public IObjectInstanceTracker getInstanceTracker()
    {
        return _instanceTracker == null ? getTestingContext().getInstanceTracker() : _instanceTracker;
    }


    public void setInstanceTracker(final IObjectInstanceTracker instanceTracker)
    {
        _instanceTracker = instanceTracker;
    }


    /**
     * Specifies the instance tracker to use.
     *
     * @param instanceTracker The instance tracker to use.  If <code>null</code>, the default instance tracker is used.
     *
     * @return This translator, to facilitate call chaining.  Will never be <code>null</code>.
     */
    @SuppressWarnings("unchecked")
    public <I extends AbstractTranslator<T>> I use(final IObjectInstanceTracker instanceTracker)
    {
        setInstanceTracker(instanceTracker);
        return (I)this;
    }


    @Override
    public String translate(final T object) throws TranslationException
    {
        if (object == null)
        {
            return "null";
        }

        final String translation = doTranslate(object);
        return String.format("%s%s%s", getPreTranslationString(), translation, getPostTranslationString());
    }


    /**
     * Called by {@link #translate(Object)} to perform the work of translation only if the supplied object is not
     * <code>null</code>.
     *
     * @param object The object to be translated.  May not be <code>null</code>.
     *
     * @return The object translated to a {@link String}.
     *
     * @throws TranslationException If an error occurs.  The exception will contain a message and the object that caused
     *                              the translation failure.
     */
    protected abstract String doTranslate(T object) throws TranslationException;
}
