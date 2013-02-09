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


/**
 * Tests the functionality of the {@link com.sunsprinter.diffunit.core.common.AbstractTestingContextUser} class.
 *
 * @author Kevan Dunsmore
 * @created 2013/01/05
 */
public class UnitAbstractTestingContextUserTest extends AbstractTestingContextUserTest<UnitAbstractTestingContextUserTest.ConcreteTestingContextUser>
{
    @Override
    protected ConcreteTestingContextUser createTestingContextUser()
    {
        return new ConcreteTestingContextUser();
    }


    /**
     * Forms a concrete subclass to allow us to instantiate.
     */
    protected final class ConcreteTestingContextUser extends AbstractTestingContextUser
    {
        // Concrete subclass to allow us to instantiate.
    }
}