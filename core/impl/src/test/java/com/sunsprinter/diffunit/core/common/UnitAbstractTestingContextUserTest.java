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

package com.sunsprinter.diffunit.core.common;


import com.sunsprinter.diffunit.core.context.TestingContext;
import com.sunsprinter.diffunit.core.context.TestingContextHolder;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;


/**
 * Tests the functionality of the {@link com.sunsprinter.diffunit.core.common.AbstractTestingContextUser} class.
 *
 * @author Kevan Dunsmore
 * @created 2013/01/05
 */
public class UnitAbstractTestingContextUserTest
{
    /**
     * The testing context user under test.
     */
    private ConcreteTestingContextUser _testingContextUser;


    @Before
    public void setUp() throws Exception
    {
        _testingContextUser = new ConcreteTestingContextUser();
        TestingContextHolder.CONTEXT = null;
    }


    @Test
    public void testAccessorsAndMutators() throws Exception
    {
        // By default, if no specific context is set, the testing context user will use the global one on the holder.
        assertNull(_testingContextUser.getTestingContext());
        TestingContextHolder.CONTEXT = new TestingContext();
        assertSame(TestingContextHolder.CONTEXT, _testingContextUser.getTestingContext());

        // Now try with a testCustomLocationOnFileSystemAtMethodLevel value.
        final TestingContext testingContext = new TestingContext();
        _testingContextUser.setTestingContext(testingContext);
        assertSame(testingContext, _testingContextUser.getTestingContext());
        assertNotSame(TestingContextHolder.CONTEXT, _testingContextUser.getTestingContext());
    }


    @Test
    public void testUse() throws Exception
    {
        final TestingContext testingContext = new TestingContext();
        _testingContextUser.use(testingContext);
        assertSame(testingContext, _testingContextUser.getTestingContext());
        assertNotSame(TestingContextHolder.CONTEXT, _testingContextUser.getTestingContext());
    }


    /**
     * Forms a concrete subclass to allow us to instantiate.
     */
    private final class ConcreteTestingContextUser extends AbstractTestingContextUser
    {
        // Concrete subclass to allow us to instantiate.
    }
}
