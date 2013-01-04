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

package com.sunsprinter.diffunit.junit.rules;


import com.sunsprinter.diffunit.junit.runners.model.DiffUnitStatement;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import java.util.HashMap;
import java.util.Map;


/**
 * This class forms the standard way of informing JUnit that the test to be executed is a DiffUnit test case.  Per JUnit
 * standards, set a public member of your test case to an instance of this class.<p/>
 *
 * <pre>
 * public class MyTest
 * {
 *    {@code @Rule}
 *     public DiffUnitRule _rule = new DiffUnitRule(this);
 *
 *     .
 *     .
 *     .
 * }
 * </pre><p/>
 *
 * Of course, to do anything useful you'll want the {@link com.sunsprinter.diffunit.core.context.ITestingContext}
 * injected:
 *
 * <pre>
 * public class MyTest
 * {
 *    {@code @Rule}
 *     public DiffUnitRule _rule = new DiffUnitRule(this);
 *
 *    {@code @DiffUnitInject}
 *     private ITestingContext _testingContext;
 *
 *     .
 *     .
 *     .
 * }
 * </pre><p/>
 *
 * or
 *
 * <pre>
 * public class MyTest extends AbstractDiffUnitTest
 * {
 *    {@code @Rule}
 *     public DiffUnitRule _rule = new DiffUnitRule(this);
 *
 *     .
 *     .
 *     .
 * }
 * </pre><p/>
 *
 * @author Kevan Dunsmore
 * @created 2011/11/13
 */
public class DiffUnitRule implements TestRule
{
    /**
     * The test instance.
     */
    private Object _test;

    /**
     * The name value pairs for the test.  Defaults to an empty map.
     */
    private Map<String, String> _nameValuePairs = new HashMap<>();


    /**
     * Creates a new rule with the supplied test object.
     *
     * @param test The test being executed.  May not be null.
     */
    public DiffUnitRule(final Object test)
    {
        _test = test;
    }


    /**
     * Returns the test object.  Will never be null.
     */
    protected Object getTest()
    {
        return _test;
    }


    /**
     * Sets the test object.
     *
     * @param test The test object.  May not be null.
     */
    protected void setTest(final Object test)
    {
        _test = test;
    }


    /**
     * {@inheritDoc}<p/>
     *
     * @return a new instance of {@link com.sunsprinter.diffunit.junit.runners.model.DiffUnitStatement}.
     */
    @Override
    public Statement apply(final Statement base, final Description description)
    {
        return new DiffUnitStatement(description, getTest(), base, getNameValuePairs());
    }


    public DiffUnitRule addNameValuePair(final String name, final String value)
    {
        getNameValuePairs().put(name, value);
        return this;
    }


    public DiffUnitRule removeNameValuePair(final String name)
    {
        getNameValuePairs().remove(name);
        return this;
    }


    public Map<String, String> getNameValuePairs()
    {
        return _nameValuePairs;
    }


    public void setNameValuePairs(final Map<String, String> nameValuePairs)
    {
        _nameValuePairs = nameValuePairs;
    }
}
