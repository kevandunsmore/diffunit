/*
 * Copyright 2013 Kevan Dunsmore.  All rights reserved.
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


import com.sunsprinter.diffunit.core.comparison.IFileComparer;
import com.sunsprinter.diffunit.core.context.ITestingContext;
import com.sunsprinter.diffunit.core.context.TestingContext;
import com.sunsprinter.diffunit.core.context.TestingContextHolder;
import com.sunsprinter.diffunit.core.injection.IInjector;
import com.sunsprinter.diffunit.core.injection.Injector;
import com.sunsprinter.diffunit.core.instancetracking.IObjectIdentifier;
import com.sunsprinter.diffunit.core.instancetracking.IObjectInstanceTracker;
import com.sunsprinter.diffunit.core.output.IOutputManager;
import com.sunsprinter.diffunit.core.translators.CollectionTranslator;
import com.sunsprinter.diffunit.core.translators.IRootTranslator;
import com.sunsprinter.diffunit.core.translators.ITranslator;
import com.sunsprinter.diffunit.core.translators.ITypeBindingTranslator;
import com.sunsprinter.diffunit.core.translators.IteratorTranslator;
import com.sunsprinter.diffunit.core.translators.MapTranslator;
import com.sunsprinter.diffunit.core.translators.ThrowableMessageTranslator;
import com.sunsprinter.diffunit.core.translators.ToPrettyXmlTranslator;
import com.sunsprinter.diffunit.core.translators.ToStringTranslator;
import com.sunsprinter.diffunit.core.translators.TranslationException;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


/**
 * Tests the functionality of the {@link com.sunsprinter.diffunit.core.initialization.AbstractDiffUnitInitializer}
 * class.
 *
 * @author Kevan Dunsmore
 * @created 2013/01/15
 */
public class UnitAbstractDiffUnitInitializerTest
{
    /**
     * The initializer under test.
     */
    private TestDiffUnitInitializer _initializer;

    /**
     * Mock testing context used to test the initializer.
     */
    private ITestingContext _testingContext;


    @Before
    public void setUp() throws Exception
    {
        _initializer = spy(new TestDiffUnitInitializer());

        _testingContext = mock(ITestingContext.class);
        _initializer.setTestingContext(_testingContext);
    }


    @SuppressWarnings("unchecked")
    @Test
    public void testInjectorCreationAndInitialization() throws Exception
    {
        final IObjectInstanceTracker instanceTracker = mock(IObjectInstanceTracker.class);
        when(_testingContext.getInstanceTracker()).thenReturn(instanceTracker);
        when(_testingContext.getTestName()).thenReturn("Bob");
        when(_testingContext.getTestClass()).thenReturn((Class)getClass());

        final IRootTranslator rootTranslator = mock(IRootTranslator.class);
        when(_testingContext.getRootTranslator()).thenReturn(rootTranslator);

        final Collection<Object> outputObjects = new ArrayList<>();
        when(_testingContext.getOutputObjects()).thenReturn(outputObjects);

        final IOutputManager outputManager = mock(IOutputManager.class);
        when(_testingContext.getOutputManager()).thenReturn(outputManager);

        final IFileComparer fileComparer = mock(IFileComparer.class);
        when(_testingContext.getFileComparer()).thenReturn(fileComparer);

        final IInjector inj = _initializer.createInjector();
        assertTrue(inj instanceof Injector);

        final Injector injector = (Injector)inj;
        assertTrue(injector.getInjectionMap() instanceof HashMap);

        assertEquals(9, injector.getInjectionMap().size());
        assertSame("Bob", injector.getInjectionMap().get("TestName"));
        assertSame(getClass(), injector.getInjectionMap().get("TestClass"));
        assertSame(outputObjects, injector.getInjectionMap().get("OutputObjects"));
        assertSame(_testingContext, injector.getInjectionMap().get(ITestingContext.class));
        assertSame(instanceTracker, injector.getInjectionMap().get(IObjectInstanceTracker.class));
        assertSame(rootTranslator, injector.getInjectionMap().get(IRootTranslator.class));
        assertSame(rootTranslator, injector.getInjectionMap().get(ITypeBindingTranslator.class));
        assertSame(outputManager, injector.getInjectionMap().get(IOutputManager.class));
        assertSame(fileComparer, injector.getInjectionMap().get(IFileComparer.class));
    }


    /**
     * The initializer sets up standard translators for known classes, so stuff works out of the box (this is called
     * binding).  This method makes sure the standard bindings are performed correctly.
     */
    @Test
    public void testBindStandardTypesToTranslators() throws Exception
    {
        final Map<Class<?>, ITranslator<?>> bindingMap = new HashMap<>();

        _initializer.bindStandardTypesToTranslators(new ITypeBindingTranslator()
        {
            @Override
            public String translate(Object object) throws TranslationException
            {
                // We don't care.  Do nothing.
                return null;
            }


            @Override
            public void bind(ITranslator<?> translator, Class<?>... types)
            {
                for (final Class<?> type : types)
                {
                    bindingMap.put(type, translator);
                }
            }
        });

        assertEquals(6, bindingMap.size());
        assertTrue(bindingMap.get(Iterator.class) instanceof IteratorTranslator);
        assertTrue(bindingMap.get(Collection.class) instanceof CollectionTranslator);
        assertTrue(bindingMap.get(Map.class) instanceof MapTranslator);
        assertTrue(bindingMap.get(MapTranslator.KeyValuePair.class) instanceof ToPrettyXmlTranslator);
        assertTrue(bindingMap.get(IObjectIdentifier.class) instanceof ToStringTranslator);
        assertTrue(bindingMap.get(Throwable.class) instanceof ThrowableMessageTranslator);
    }


    @Test
    public void testInitialize() throws Exception
    {
        final IInjector injector = mock(IInjector.class);
        doReturn(injector).when(_initializer).createInjector();

        final Map<String, String> nameValuePairs = new HashMap<>();
        nameValuePairs.put("wibble", "giblets");
        nameValuePairs.put("something", "wonderful");

        ITestingContext tc = _initializer.initialize(this, "testInitialize", nameValuePairs);

        assertTrue(tc instanceof TestingContext);
        TestingContext testingContext = (TestingContext)tc;

        // Make sure the testing context holder is set up properly.
        assertSame(testingContext, TestingContextHolder.CONTEXT);

        // The name / value pair collection should be set up with defaults and initialized.
        assertEquals(4, testingContext.getNameValuePairs().size());
        assertEquals("testInitialize", testingContext.getNameValuePairs().get("TestName"));
        assertEquals(getClass().getSimpleName(), testingContext.getNameValuePairs().get("TestClassName"));
        assertEquals("giblets", testingContext.getNameValuePairs().get("wibble"));
        assertEquals("wonderful", testingContext.getNameValuePairs().get("something"));

        // Check that the testing context was set up.
        assertNotNull(testingContext.getOutputObjects());
        assertSame(this, testingContext.getTest());
        assertNotNull(testingContext.getInstanceTracker());
        assertEquals(getClass().getMethod("testInitialize"), testingContext.getTestMethod());
        assertNotNull(testingContext.getOutputManager());
        assertNotNull(testingContext.getFileComparer());
        assertNotNull(testingContext.getOutputDirectory());

        // Make sure the root translator is the regexp replacement pair proxy.
        assertTrue(Proxy.isProxyClass(testingContext.getRootTranslator().getClass()));

        // Check the output directory.
        assertEquals("target/diffunit/UnitAbstractDiffUnitInitializerTest/testInitialize", testingContext.getOutputDirectory().getPath());

        // Check that the directory is created and that it is empty.
        assertTrue(testingContext.getOutputDirectory().exists());
        assertEquals(0, testingContext.getOutputDirectory().list().length);

        // Make sure the injector was called.
        verify(injector).inject(this);
    }


    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Test
    public void testInitializeCannotCreateOutputDirectory() throws Exception
    {
        final IInjector injector = mock(IInjector.class);
        doReturn(injector).when(_initializer).createInjector();

        // Mock up a file that throws an exception when the mkdirs() method is called.
        final File outputDir = spy(new File("target/diffunit/" + getClass().getSimpleName() + "/testInitializeCannotCreateOutputDirectory"));
        doReturn(false).when(outputDir).mkdirs();

        // Now have the spied initializer return the file when it figures out the output directory.
        doReturn(outputDir).when(_initializer).determineTestOutputDirectory();

        // Kick off the initialization.  We should get an exception.
        try
        {
            _initializer.initialize(this, "testInitialize", new HashMap<String, String>());
            fail("Expected exception to be thrown");
        }
        catch (final DiffUnitInitializationException e)
        {
            final String expectedMessage = String.format("Could not create output directory %s.", outputDir.getAbsolutePath());
            assertEquals(expectedMessage, e.getMessage());
        }
    }
}
