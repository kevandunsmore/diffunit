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

package com.sunsprinter.diffunit.core;


import java.io.File;
import java.util.Collection;
import java.util.List;

import com.sunsprinter.diffunit.core.comparison.IFileComparer;
import com.sunsprinter.diffunit.core.context.ITestingContext;
import com.sunsprinter.diffunit.core.injection.DiffUnitInject;
import com.sunsprinter.diffunit.core.instancetracking.IObjectInstanceTracker;
import com.sunsprinter.diffunit.core.output.IOutputManager;
import com.sunsprinter.diffunit.core.translators.IRegExReplacementPair;
import com.sunsprinter.diffunit.core.translators.IRootTranslator;
import com.sunsprinter.diffunit.core.translators.ITranslator;
import com.sunsprinter.diffunit.core.translators.TranslationException;


/**
 * Forms an abstract base class for unit tests.  You can do all of this yourself in your own test by requesting
 * injection of the {@link com.sunsprinter.diffunit.core.context.ITestingContext} instance but this class provides some
 * handy shortcuts to avoid tedious typing.  Use it if you don't have an existing superclass in your test.<p/>
 *
 * This class requests injection of the {@link com.sunsprinter.diffunit.core.context.ITestingContext} and allows access
 * to it via the {@link #getTestingContext()} method.  It implements all of the main interface contracts, however,
 * including {@link com.sunsprinter.diffunit.core.context.ITestingContext} itself, so it's unlikely you'll even need to
 * touch the testing context.
 *
 * @author Kevan Dunsmore
 * @created 2011/11/15
 */
public class AbstractDiffUnitTest implements ITestingContext, IRootTranslator, IOutputManager
{
    /**
     * The DiffUnit translator object, injected by DiffUnit.
     */
    @DiffUnitInject
    private ITestingContext _testingContext;


    /**
     * Returns the testing context instance.  You can use this but can also use {@code this} in its place, as this class
     * implements the {@link com.sunsprinter.diffunit.core.context.ITestingContext} interface.
     *
     * @return The testing context instance.  Will never be null.
     */
    protected ITestingContext getTestingContext()
    {
        return _testingContext;
    }


    /**
     * Sets the testing context.  Provided for extensibility purposes.
     */
    @SuppressWarnings("unused")
    protected void setTestingContext(final ITestingContext testingContext)
    {
        _testingContext = testingContext;
    }


    @Override
    public String getTestName()
    {
        return getTestingContext().getTestName();
    }


    @Override
    public Class<?> getTestClass()
    {
        return getTestingContext().getTestClass();
    }


    @Override
    public Object getTest()
    {
        return getTestingContext().getTest();
    }


    @Override
    public IObjectInstanceTracker getInstanceTracker()
    {
        return getTestingContext().getInstanceTracker();
    }


    @Override
    public IRootTranslator getRootTranslator()
    {
        return getTestingContext().getRootTranslator();
    }


    @Override
    public Collection<Object> getOutputObjects()
    {
        return getTestingContext().getOutputObjects();
    }


    @Override
    public IOutputManager getOutputManager()
    {
        return getTestingContext().getOutputManager();
    }


    @Override
    public IFileComparer getFileComparer()
    {
        return getTestingContext().getFileComparer();
    }


    @Override
    public File getOutputDirectory()
    {
        return getTestingContext().getOutputDirectory();
    }


    @Override
    public List<IRegExReplacementPair> getRegExReplacementPairs()
    {
        return getTestingContext().getRegExReplacementPairs();
    }


    @Override
    public void setRegExReplacementPairs(final List<IRegExReplacementPair> replacementPairs)
    {
        getTestingContext().setRegExReplacementPairs(replacementPairs);
    }


    @Override
    public String translate(final Object object) throws TranslationException
    {
        return getRootTranslator().translate(object);
    }


    @Override
    public void bind(final ITranslator<?> translator, final Class<?>... types)
    {
        getRootTranslator().bind(translator, types);
    }


    @Override
    public void addBlankLine() throws RuntimeException
    {
        getOutputManager().addBlankLine();
    }


    @Override
    public void add(final Object object) throws RuntimeException
    {
        getOutputManager().add(object);
    }


    @Override
    public void addAsString(final Object object) throws RuntimeException
    {
        getOutputManager().addAsString(object);
    }


    @Override
    public void writeFile(final String fileName) throws RuntimeException
    {
        getOutputManager().writeFile(fileName);
    }
}
