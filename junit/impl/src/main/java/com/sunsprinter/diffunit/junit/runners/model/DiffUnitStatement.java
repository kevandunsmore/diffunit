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

package com.sunsprinter.diffunit.junit.runners.model;


import com.sunsprinter.diffunit.core.context.ITestingContext;
import com.sunsprinter.diffunit.core.initialization.AbstractDiffUnitInitializer;
import com.sunsprinter.diffunit.junit.initialization.DiffUnitJUnitInitializer;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import java.util.Map;


/**
 * Statement used to initialize and run DiffUnit under JUnit.  This statement is responsible for creating the DiffUnit
 * initializer ({@link #createInitializer()} and running it.  This statement then evaluates its inner statement (JUnit
 * speak for running the rest of the test) and then will output the default DiffUnit results file if the context has
 * output objects waiting to be written.  Lastly, this statement will compare all written files with known good
 * versions, failing the test if it detects any differences.
 *
 * @author Kevan Dunsmore
 * @created 2011/11/13
 */
public class DiffUnitStatement extends Statement
{
    private Description _description;
    private Object _test;
    private Statement _innerStatement;
    private Map<String, String> _nameValuePairs;


    /**
     * Default constructor, provided for extensibility.
     */
    protected DiffUnitStatement()
    {
    }


    /**
     * Creates a new instance of the statement.
     *
     * @param description    The JUnit test description objet to use.  May not be null.
     * @param test           The test object being executed (instance of the test class).  May not be null.
     * @param innerStatement The JUnit inner statement to execute.  May not be null.
     * @param nameValuePairs The name value pair map.  May not be null.
     */
    public DiffUnitStatement(final Description description, final Object test, final Statement innerStatement, final Map<String, String> nameValuePairs)
    {
        _description = description;
        _test = test;
        _innerStatement = innerStatement;
        _nameValuePairs = nameValuePairs;
    }


    protected Description getDescription()
    {
        return _description;
    }


    protected void setDescription(Description description)
    {
        _description = description;
    }


    protected Object getTest()
    {
        return _test;
    }


    protected void setTest(final Object test)
    {
        _test = test;
    }


    protected Statement getInnerStatement()
    {
        return _innerStatement;
    }


    protected void setInnerStatement(final Statement innerStatement)
    {
        _innerStatement = innerStatement;
    }


    protected Map<String, String> getNameValuePairs()
    {
        return _nameValuePairs;
    }


    protected void setNameValuePairs(Map<String, String> nameValuePairs)
    {
        _nameValuePairs = nameValuePairs;
    }


    @Override
    public void evaluate() throws Throwable
    {
        final ITestingContext context = createInitializer().initialize(getTest(), getDescription().getMethodName(), getNameValuePairs());

        getInnerStatement().evaluate();

        // If the test hasn't explicitly written a file then we do it here.
        if (!context.getOutputObjects().isEmpty())
        {
            context.getOutputManager().writeFile("results.txt");
        }

        context.getFileComparer().compareAllFiles();
    }


    /**
     * Factory method to create a DiffUnit initializer.  Default behavior is to return a new instance of {@link
     * com.sunsprinter.diffunit.junit.initialization.DiffUnitJUnitInitializer}.
     */
    protected AbstractDiffUnitInitializer createInitializer()
    {
        return new DiffUnitJUnitInitializer();
    }
}
