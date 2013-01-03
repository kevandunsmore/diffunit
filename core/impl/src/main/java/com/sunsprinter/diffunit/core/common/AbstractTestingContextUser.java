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

package com.sunsprinter.diffunit.core.common;


import com.sunsprinter.diffunit.core.context.ITestingContext;
import com.sunsprinter.diffunit.core.context.TestingContextHolder;


/**
 * AbstractTestingContextUser
 *
 * @author Kevan Dunsmore
 * @created 2011/11/15
 */
public class AbstractTestingContextUser
{
    /**
     * The context of the test.
     */
    private ITestingContext _testingContext;


    protected ITestingContext getTestingContext()
    {
        return _testingContext == null ? TestingContextHolder.CONTEXT : _testingContext;
    }


    public void setTestingContext(final ITestingContext testingContext)
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
