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

package com.sunsprinter.diffunit.core.common;


import com.sunsprinter.diffunit.core.context.ITestingContext;
import com.sunsprinter.diffunit.core.context.TestingContextHolder;


/**
 * Abstract class that forms a base for classes that use the testing context.  If no specific testing context is set,
 * this class will use the one on the {@link com.sunsprinter.diffunit.core.context.TestingContextHolder} when its {@link
 * #getTestingContext()} method is called.
 *
 * @author Kevan Dunsmore
 * @created 2011/11/15
 */
public abstract class AbstractTestingContextUser
{
    /**
     * The context of the test.
     */
    private ITestingContext _testingContext;


    /**
     * Returns the testing context for this object.  Will use the specific one set on it by a previous call to {@link
     * #setTestingContext(com.sunsprinter.diffunit.core.context.ITestingContext)}.  If nothing has been set, will return
     * the testing context held by {@link com.sunsprinter.diffunit.core.context.TestingContextHolder#CONTEXT}.
     */
    protected ITestingContext getTestingContext()
    {
        return _testingContext == null ? TestingContextHolder.CONTEXT : _testingContext;
    }


    /**
     * Sets the testing context.
     *
     * @param testingContext The context to use or null if this user should use the testing context on {@link
     *                       com.sunsprinter.diffunit.core.context.TestingContextHolder#CONTEXT}.
     */
    protected void setTestingContext(final ITestingContext testingContext)
    {
        _testingContext = testingContext;
    }


    /**
     * Specifies the testing context to use.
     *
     * @param testingContext The testing context to use.  If <code>null</code>, the default testing context is used.
     *
     * @return This object, to facilitate call chaining.  Will never be <code>null</code>.
     */
    @SuppressWarnings("unchecked")
    public <T extends AbstractTestingContextUser> T use(final ITestingContext testingContext)
    {
        setTestingContext(testingContext);
        return (T)this;
    }
}
