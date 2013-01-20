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

package com.sunsprinter.diffunit.core.comparison;


/**
 * A concrete file comparer implementation designed to test the core abstract class.
 *
 * @author Kevan Dunsmore
 * @created 2013/01/12
 */
public class TestFileComparer extends AbstractFileComparer
{
    /**
     * This comparer throws instances of this exception in its {@link com.sunsprinter.diffunit.core.comparison.TestFileComparer#fail(String)}
     * method.
     */
    public class TestFileComparerException extends Exception
    {
        public TestFileComparerException(String message)
        {
            super(message);
        }
    }


    @Override
    protected void fail(final String message) throws Exception
    {
        throw new TestFileComparerException(message);
    }
}
