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

package com.sunsprinter.diffunit.junit.comparison;


import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;


/**
 * Tests the functionality of the {@link com.sunsprinter.diffunit.junit.comparison.JUnitFileComparer} class.
 *
 * @author Kevan Dunsmore
 * @created 2013/01/03
 */
public class JUnitFileComparerTest
{
    /**
     * The file comparer under test.
     */
    private JUnitFileComparer _fileComparer;


    @Before
    public void setUp() throws Exception
    {
        _fileComparer = new JUnitFileComparer();
    }


    @Test
    public void testFail() throws Exception
    {
        try
        {
            _fileComparer.fail("should fail");
            Assert.fail("file comparer did not correctly fail test");
        }
        catch (AssertionError e)
        {
            Assert.assertEquals("should fail", e.getMessage());
        }
    }
}
