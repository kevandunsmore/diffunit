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

package com.sunsprinter.diffunit.junit.runners.model;


import com.sunsprinter.diffunit.core.context.TestingContextHolder;
import com.sunsprinter.diffunit.core.output.IOutputManager;
import com.sunsprinter.diffunit.junit.comparison.JUnitFileComparer;
import com.sunsprinter.diffunit.junit.initialization.DiffUnitJUnitInitializer;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


/**
 * Tests the functionality of the {@link com.sunsprinter.diffunit.junit.runners.model.DiffUnitStatement} class.
 *
 * @author Kevan Dunsmore
 * @created 2013/01/03
 */
public class DiffUnitStatementTest
{
    /**
     * The statement under test.
     */
    private DiffUnitStatement _statement;

    /**
     * A mock description used to test our DiffUnit statement.
     */
    private Description _description;

    /**
     * A mock statement used to test our DiffUnit statement.
     */
    private Statement _innerStatement;

    /**
     * Name value pairs used to test the statement.
     */
    private Map<String, String> _nameValuePairs;


    @Before
    public void setUp() throws Exception
    {
        TestingContextHolder.CONTEXT = null;

        _description = mock(Description.class);
        _innerStatement = mock(Statement.class);

        _nameValuePairs = new HashMap<>();
        _statement = new DiffUnitStatement(_description, this, _innerStatement, _nameValuePairs);
    }


    @Test
    public void testAccessorsAndMutators() throws Exception
    {
        assertSame(this, _statement.getTest());
        assertSame(_description, _statement.getDescription());
        assertSame(_innerStatement, _statement.getInnerStatement());
        assertSame(_nameValuePairs, _statement.getNameValuePairs());
        performAccessorMutatorTest();

        // Same again, this time the default constructor.
        _statement = new DiffUnitStatement();
        assertNull(_statement.getTest());
        assertNull(_statement.getDescription());
        assertNull(_statement.getInnerStatement());
        assertNull(_statement.getNameValuePairs());
        performAccessorMutatorTest();
    }


    private void performAccessorMutatorTest()
    {
        final Description description = mock(Description.class);
        _statement.setDescription(description);
        assertSame(description, _statement.getDescription());

        final Statement statement = mock(Statement.class);
        _statement.setInnerStatement(statement);
        assertSame(statement, _statement.getInnerStatement());

        final Object newTest = new Object();
        _statement.setTest(newTest);
        assertSame(newTest, _statement.getTest());

        final Map<String, String> nameValuePairs = Collections.emptyMap();
        _statement.setNameValuePairs(nameValuePairs);
        assertSame(nameValuePairs, _statement.getNameValuePairs());
    }


    @Test
    public void testEvaluate() throws Throwable
    {
        // Configure our JUnit mock test description object.
        when(_description.getMethodName()).thenReturn("testEvaluate");

        EvaluateTestMethodObject evaluateTestMethodObject = new EvaluateTestMethodObject().invoke();
        DiffUnitJUnitInitializer initializer = evaluateTestMethodObject.getInitializer();
        IOutputManager outputManager = evaluateTestMethodObject.getOutputManager();

        verify(initializer).initialize(this, "testEvaluate", _nameValuePairs);
        verify(outputManager, times(0)).writeFile("results.txt");

        assertEquals(2, TestingContextHolder.CONTEXT.getNameValuePairs().size());
        assertEquals(getClass().getSimpleName(), TestingContextHolder.CONTEXT.getNameValuePairs().get("TestClassName"));
        assertEquals("testEvaluate", TestingContextHolder.CONTEXT.getNameValuePairs().get("TestName"));
    }


    @Test
    public void testEvaluateWithCustomNameValuePairs() throws Throwable
    {
        // Configure our JUnit mock test description object.
        when(_description.getMethodName()).thenReturn("testEvaluateWithCustomNameValuePairs");

        // Set up some testCustomLocationOnFileSystemAtMethodLevel name / value pairs.
        // Override the test class name.
        _nameValuePairs.put("TestClassName", "wibble");

        // And some other stuff.
        _nameValuePairs.put("Giblets", "Drumsticks");
        _nameValuePairs.put("Oregon", "Ducks");

        EvaluateTestMethodObject evaluateTestMethodObject = new EvaluateTestMethodObject().invoke();
        DiffUnitJUnitInitializer initializer = evaluateTestMethodObject.getInitializer();
        IOutputManager outputManager = evaluateTestMethodObject.getOutputManager();

        verify(initializer).initialize(this, "testEvaluateWithCustomNameValuePairs", _nameValuePairs);
        verify(outputManager, times(0)).writeFile("results.txt");

        assertEquals(4, TestingContextHolder.CONTEXT.getNameValuePairs().size());
        assertEquals("wibble", TestingContextHolder.CONTEXT.getNameValuePairs().get("TestClassName"));
        assertEquals("testEvaluateWithCustomNameValuePairs", TestingContextHolder.CONTEXT.getNameValuePairs().get("TestName"));
        assertEquals("Drumsticks", TestingContextHolder.CONTEXT.getNameValuePairs().get("Giblets"));
        assertEquals("Ducks", TestingContextHolder.CONTEXT.getNameValuePairs().get("Oregon"));
    }


    @Test
    public void testEvaluateWithOutputObjects() throws Throwable
    {
        // Configure our JUnit mock test description object.
        when(_description.getMethodName()).thenReturn("testEvaluateWithOutputObjects");

        // Use an inner statement that adds something to the output objects collection.
        _statement.setInnerStatement(new Statement()
        {
            @Override
            public void evaluate() throws Throwable
            {
                TestingContextHolder.CONTEXT.getOutputObjects().add("hello world");
            }
        });
        EvaluateTestMethodObject evaluateTestMethodObject = new EvaluateTestMethodObject().invoke();
        DiffUnitJUnitInitializer initializer = evaluateTestMethodObject.getInitializer();
        IOutputManager outputManager = evaluateTestMethodObject.getOutputManager();

        verify(initializer).initialize(this, "testEvaluateWithOutputObjects", _nameValuePairs);
        verify(outputManager).writeFile("results.txt");

        assertEquals(2, TestingContextHolder.CONTEXT.getNameValuePairs().size());
        assertEquals(getClass().getSimpleName(), TestingContextHolder.CONTEXT.getNameValuePairs().get("TestClassName"));
        assertEquals("testEvaluateWithOutputObjects", TestingContextHolder.CONTEXT.getNameValuePairs().get("TestName"));
    }


    /**
     * Class that allows us to test evaluation and share some code between tests.
     */
    private class EvaluateTestMethodObject
    {
        private IOutputManager _outputManager;
        private DiffUnitJUnitInitializer _initializer;


        public IOutputManager getOutputManager()
        {
            return _outputManager;
        }


        public DiffUnitJUnitInitializer getInitializer()
        {
            return _initializer;
        }


        public EvaluateTestMethodObject invoke() throws Throwable
        {
            // We need a spied version of the statement under test so we can muck with the factory method that creates
            // the JUnit version of the DiffUnit initializer.
            final DiffUnitStatement statement = spy(_statement);

            _outputManager = mock(IOutputManager.class);

            // And a spied version of the JUnit initializer so we can keep track of what's going on.
            _initializer = spy(new DiffUnitJUnitInitializer()
            {
                @Override
                protected IOutputManager createOutputManager()
                {
                    return _outputManager;
                }


                @Override
                protected JUnitFileComparer createFileComparer()
                {
                    final JUnitFileComparer fileComparer = super.createFileComparer();
                    fileComparer.setFailOnNoFilesRegistered(false);
                    return fileComparer;
                }
            });

            // Mock out the factory method for the initializer to return our own tame version.
            when(statement.createInitializer()).thenReturn(_initializer);

            // Kick things off.
            statement.evaluate();

            return this;
        }
    }
}
