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

package com.sunsprinter.diffunit.junit.rules;


import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import com.sunsprinter.diffunit.core.context.ITestingContext;
import com.sunsprinter.diffunit.core.initialization.AbstractDiffUnitInitializer;
import com.sunsprinter.diffunit.junit.initialization.DiffUnitJUnitInitializer;


/**
 * DiffUnitRule
 *
 * @author Kevan Dunsmore
 * @created 2011/11/13
 */
public class DiffUnitRule implements TestRule
{
    private Object _test;


    public DiffUnitRule(final Object test)
    {
        _test = test;
    }


    protected Object getTest()
    {
        return _test;
    }


    protected void setTest(final Object test)
    {
        _test = test;
    }


    @Override
    public Statement apply(final Statement base, final Description description)
    {
        return new Statement()
        {
            @Override
            public void evaluate() throws Throwable
            {
                final ITestingContext context = createInitializer().initialize(getTest(), description.getMethodName());

                base.evaluate();

                // If the test hasn't explicitly written a file then we do it here.
                if (!context.getOutputObjects().isEmpty())
                {
                    context.getOutputManager().writeFile("results.txt");
                }

                context.getFileComparer().compareAllFiles();
            }


            protected AbstractDiffUnitInitializer createInitializer()
            {
                return new DiffUnitJUnitInitializer();
            }
        };
    }
}
