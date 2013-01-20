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

package com.sunsprinter.diffunit.core.initialization;


import com.sunsprinter.diffunit.core.common.AbstractTestingContextUser;
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
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;


/**
 * Abstract base for a testing framework-specific DiffUnit initializer.  Typically there are steps that are testing
 * framework specific in the initialization process (for example, creating the specific file comparer) so these are left
 * to concrete subclasses.
 *
 * @author Kevan Dunsmore
 * @created 2011/11/12
 */
public abstract class AbstractDiffUnitInitializer extends AbstractTestingContextUser
{
    /**
     * Initializes DiffUnit.
     *
     * @param test           The instance of the test class being executed.  May not be null.
     * @param testName       The name of the test being executed.  May not be null.
     * @param nameValuePairs The name value pair map.  May not be null.  The values contained here are used in
     *                       preference to the default name / value pair values.
     *
     * @return The testing context created.  Will never be null.
     * @throws Exception If there's a problem initializing.
     */
    public ITestingContext initialize(final Object test, final String testName, final Map<String, String> nameValuePairs) throws Exception
    {
        final TestingContext testingContext = createTestingContext();
        use(testingContext);
        TestingContextHolder.CONTEXT = testingContext;

        initializeNameValuePairsWithDefaultValues(testingContext, test, testName);
        overlayCustomNameValuePairs(testingContext, nameValuePairs);

        testingContext.setOutputObjects(createOutputObjectsCollection());
        testingContext.setTest(test);
        testingContext.setInstanceTracker(createInstanceTracker());
        testingContext.setTestMethod(testingContext.getTestClass().getMethod(testName));

        final IRootTranslator rootTranslator = createRootTranslator();
        bindStandardTypesToTranslators(rootTranslator);

        final RegExReplacementTranslatorDecorator<IRootTranslator> regExDecorator = createRegExReplacementTranslatorDecorator(rootTranslator);
        regExDecorator.setReplacementPairs(getTestingContext().getRegExReplacementPairs());
        testingContext.setRootTranslator(regExDecorator.getProxy());

        installRegExReplacementPairs();

        testingContext.setOutputManager(createOutputManager());
        testingContext.setFileComparer(createFileComparer());

        testingContext.setOutputDirectory(determineTestOutputDirectory());
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


    /**
     * Factory method to create an instance tracker.  Returns a new instance of {@link
     * com.sunsprinter.diffunit.core.instancetracking.ObjectInstanceTracker}. Override to customize the instance tracker
     * used in the tests.
     */
    protected IObjectInstanceTracker createInstanceTracker()
    {
        return new ObjectInstanceTracker();
    }


    /**
     * Overlays the testCustomLocationOnFileSystemAtMethodLevel name-value pair map on the default name-value pair map,
     * overwriting any default values.
     */
    protected void overlayCustomNameValuePairs(final TestingContext testingContext, final Map<String, String> nameValuePairs)
    {
        testingContext.getNameValuePairs().putAll(nameValuePairs);
    }


    /**
     * Initializes the testing context's name-value pair map with default values.  This implementation sets the
     * following:<p/>
     *
     * <pre>
     *     TestClassName: The name of the test class being executed.
     *     TestName:      The name of the test being executed.
     * </pre>
     */
    protected void initializeNameValuePairsWithDefaultValues(final TestingContext testingContext, final Object test, final String testName)
    {
        testingContext.getNameValuePairs().put("TestClassName", test.getClass().getSimpleName());
        testingContext.getNameValuePairs().put("TestName", testName);
    }


    /**
     * Installs standard reg exp replacement pairs.  This implementation:
     *
     * <pre>
     *     1. Installs the {@link com.sunsprinter.diffunit.core.translators.StackTraceReplacementPair}.
     * </pre>
     */
    protected void installRegExReplacementPairs()
    {
        getTestingContext().getRegExReplacementPairs().add(new StackTraceReplacementPair());
    }


    /**
     * Factory method to create the translator decorator that replaces text output using regular expressions.  Override
     * this method to customize the decorator.
     *
     * @param rootTranslator The root translator.  Will never be null.
     *
     * @return the translator decorator.  Will never be null.
     */
    protected RegExReplacementTranslatorDecorator<IRootTranslator> createRegExReplacementTranslatorDecorator(final IRootTranslator rootTranslator)
    {
        return new RegExReplacementTranslatorDecorator<>(rootTranslator);
    }


    protected Collection<Object> createOutputObjectsCollection()
    {
        return new LinkedList<>();
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
        translator.bind(new ToStringTranslator<>(), IObjectIdentifier.class);
        translator.bind(new ThrowableMessageTranslator<>(), Throwable.class);
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
        return new HashMap<>();
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
