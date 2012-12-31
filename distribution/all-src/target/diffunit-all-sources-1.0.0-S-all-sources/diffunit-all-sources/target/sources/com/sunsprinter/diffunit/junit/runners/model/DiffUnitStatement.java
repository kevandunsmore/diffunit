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

package com.sunsprinter.diffunit.junit.runners.model;


import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;

import com.sunsprinter.diffunit.core.context.ITestingContext;
import com.sunsprinter.diffunit.core.initialization.AbstractDiffUnitInitializer;
import com.sunsprinter.diffunit.junit.initialization.DiffUnitJUnitInitializer;


/**
 * DiffUnitStatement
 *
 * @author Kevan Dunsmore
 * @created 2011/11/13
 */
public class DiffUnitStatement extends Statement
{
    private FrameworkMethod _method;
    private Object _test;
    private Statement _innerStatement;


    public DiffUnitStatement(final FrameworkMethod method, final Object test, final Statement innerStatement)
    {
        _method = method;
        _test = test;
        _innerStatement = innerStatement;
    }


    protected FrameworkMethod getMethod()
    {
        return _method;
    }


    protected void setMethod(final FrameworkMethod method)
    {
        _method = method;
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


    @Override
    public void evaluate() throws Throwable
    {
        final ITestingContext context = createInitializer().initialize(getTest(), getMethod().getMethod().getName());

        getInnerStatement().evaluate();

        context.getFileComparer().compareAllFiles();
    }


    protected AbstractDiffUnitInitializer createInitializer()
    {
        return new DiffUnitJUnitInitializer();
    }
}
