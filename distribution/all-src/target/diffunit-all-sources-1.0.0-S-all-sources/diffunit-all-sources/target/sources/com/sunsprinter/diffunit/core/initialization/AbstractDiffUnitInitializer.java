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

package com.sunsprinter.diffunit.core.initialization;


import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

import org.apache.commons.io.FileUtils;

import com.sunsprinter.diffunit.core.comparison.IFileComparer;
import com.sunsprinter.diffunit.core.context.ITestingContext;
import com.sunsprinter.diffunit.core.context.TestingContext;
import com.sunsprinter.diffunit.core.context.TestingContextHolder;
import com.sunsprinter.diffunit.core.injection.IInjector;
import com.sunsprinter.diffunit.core.injection.Injector;
import com.sunsprinter.diffunit.core.instancetracking.IObjectIdentifier;
import com.sunsprinter.diffunit.core.instancetracking.IObjectInstanceTracker;
import com.sunsprinter.diffunit.core.instancetracking.ObjectInstanceTracker;
import com.sunsprinter.diffunit.core.output.DiffUnitOutputLocation;
import com.sunsprinter.diffunit.core.output.IOutputManager;
import com.sunsprinter.diffunit.core.output.OutputManager;
import com.sunsprinter.diffunit.core.translators.CollectionTranslator;
import com.sunsprinter.diffunit.core.translators.IRootTranslator;
import com.sunsprinter.diffunit.core.translators.ITypeBindingTranslator;
import com.sunsprinter.diffunit.core.translators.IteratorTranslator;
import com.sunsprinter.diffunit.core.translators.MapTranslator;
import com.sunsprinter.diffunit.core.translators.RegExReplacementTranslatorDecorator;
import com.sunsprinter.diffunit.core.translators.RootTranslator;
import com.sunsprinter.diffunit.core.translators.StackTraceReplacementPair;
import com.sunsprinter.diffunit.core.translators.ThrowableMessageTranslator;
import com.sunsprinter.diffunit.core.translators.ToPrettyXmlTranslator;
import com.sunsprinter.diffunit.core.translators.ToStringTranslator;


/**
 * AbstractDiffUnitInitializer
 *
 * @author Kevan Dunsmore
 * @created 2011/11/12
 */
public abstract class AbstractDiffUnitInitializer
{
    private TestingContext _testingContext;


    protected TestingContext getTestingContext()
    {
        return _testingContext;
    }


    protected void setTestingContext(final TestingContext testingContext)
    {
        _testingContext = testingContext;
    }


    public ITestingContext initialize(final Object test, final String testName) throws Exception
    {
        setTestingContext(createTestingContext());

        getTestingContext().setOutputObjects(createOutputObjectsCollection());
        getTestingContext().setTestName(testName);
        getTestingContext().setTest(test);
        getTestingContext().setInstanceTracker(new ObjectInstanceTracker());

        final IRootTranslator rootTranslator = createRootTranslator();
        bindStandardTypesToTranslators(rootTranslator);

        final RegExReplacementTranslatorDecorator<IRootTranslator> regExDecorator = createRegExReplacementTranslatorDecorator(rootTranslator);
        regExDecorator.setReplacementPairs(getTestingContext().getRegExReplacementPairs());
        getTestingContext().setRootTranslator(regExDecorator.getProxy());

        installRegExReplacementPairs();

        getTestingContext().setOutputManager(createOutputManager());
        getTestingContext().setFileComparer(createFileComparer());

        TestingContextHolder.CONTEXT = getTestingContext();

        getTestingContext().setOutputDirectory(determineTestOutputDirectory());
        if (getTestingContext().getOutputDirectory().exists())
        {
            FileUtils.deleteDirectory(getTestingContext().getOutputDirectory());
        }
        if (!getTestingContext().getOutputDirectory().mkdirs())
        {
            throw new DiffUnitInitializationException(
                    String.format("Could not create output directory %s.",
                                  getTestingContext().getOutputDirectory().getAbsolutePath()));
        }

        createInjector().inject(test);

        return getTestingContext();
    }


    protected void installRegExReplacementPairs()
    {
        getTestingContext().getRegExReplacementPairs().add(new StackTraceReplacementPair());
    }


    protected RegExReplacementTranslatorDecorator<IRootTranslator> createRegExReplacementTranslatorDecorator(final IRootTranslator rootTranslator)
    {
        return new RegExReplacementTranslatorDecorator<IRootTranslator>(rootTranslator);
    }


    protected Collection<Object> createOutputObjectsCollection()
    {
        return new LinkedList<Object>();
    }


    protected TestingContext createTestingContext()
    {
        return new TestingContext();
    }


    protected IRootTranslator createRootTranslator()
    {
        return new RootTranslator();
    }


    protected void bindStandardTypesToTranslators(final ITypeBindingTranslator translator)
    {
        translator.bind(new IteratorTranslator(), Iterator.class);
        translator.bind(new CollectionTranslator(), Collection.class);
        translator.bind(new MapTranslator(), Map.class);
        translator.bind(new ToPrettyXmlTranslator<MapTranslator.KeyValuePair>(), MapTranslator.KeyValuePair.class);
        translator.bind(new ToStringTranslator<Object>(), IObjectIdentifier.class);
        translator.bind(new ThrowableMessageTranslator<Throwable>(), Throwable.class);
    }


    protected IInjector createInjector()
    {
        final Injector injector = new Injector();

        final Map<Object, Object> injectionMap = createInjectionMap();
        populateInjectionMap(injectionMap);
        injector.setInjectionMap(injectionMap);

        return injector;
    }


    protected Map<Object, Object> createInjectionMap()
    {
        return new HashMap<Object, Object>();
    }


    protected void populateInjectionMap(final Map<Object, Object> injectionMap)
    {
        injectionMap.put(ITestingContext.class, getTestingContext());
        injectionMap.put(IObjectInstanceTracker.class, getTestingContext().getInstanceTracker());
        injectionMap.put("TestName", getTestingContext().getTestName());
        injectionMap.put("TestClass", getTestingContext().getTestClass());
        injectionMap.put(IRootTranslator.class, getTestingContext().getRootTranslator());
        injectionMap.put(ITypeBindingTranslator.class, getTestingContext().getRootTranslator());
        injectionMap.put("OutputObjects", getTestingContext().getOutputObjects());
        injectionMap.put(IOutputManager.class, getTestingContext().getOutputManager());
        injectionMap.put(IFileComparer.class, getTestingContext().getFileComparer());
    }


    protected IOutputManager createOutputManager()
    {
        final OutputManager outputManager = new OutputManager();
        outputManager.setTestingContext(getTestingContext());
        return outputManager;
    }


    protected abstract IFileComparer createFileComparer();


    protected File determineTestOutputDirectory()
    {
        final DiffUnitOutputLocation classOutputLocationAnnotation =
                getTestingContext().getTestClass().getAnnotation(DiffUnitOutputLocation.class);
        final String classOutputLocation = classOutputLocationAnnotation == null ? "target/diffunit" : classOutputLocationAnnotation.location();

        return new File(String.format("%s/%s/%s",
                                      classOutputLocation,
                                      getTestingContext().getTestClass().getSimpleName(),
                                      getTestingContext().getTestName()));

    }
}
