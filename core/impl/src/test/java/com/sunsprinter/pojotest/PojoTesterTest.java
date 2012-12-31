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

package com.sunsprinter.pojotest;


import org.junit.Assert;
import org.junit.Test;



/**
 * Tests the functionality of the {@link com.sunsprinter.pojotest.PojoTester} class.
 *
 * @author Kevan Dunsmore
 * @created 2011/12/07
 */
public class PojoTesterTest
{
    @Test
    public void testNothing() throws Exception
    {
        new PojoTester(new Pojo())
                .test();
    }


    @Test
    public void testStandardPublicProperties() throws Exception
    {
        // Basic test, restricted to public properties.
        new PojoTester(new Pojo())
                .restrictToPublicProperties()
                .addProperty("Name", null, "van", null, "helsing")
                .addProperty("PropertyWithNonNullDefault", "wibble", null, "count", null, "dracula")
                .test();

        // And again, this time with the restriction turned off.  This should pass also.
        new PojoTester(new Pojo())
                .addProperty("Name", null, "van", null, "helsing")
                .addProperty("PropertyWithNonNullDefault", "wibble", null, "count", null, "dracula")
                .test();

        // Same test, this time with messed up default for "name".
        try
        {
            new PojoTester(new Pojo())
                    .restrictToPublicProperties()
                    .addProperty("Name", "incorrect", "van", null, "helsing")
                    .addProperty("PropertyWithNonNullDefault", "wibble", null, "count", null, "dracula")
                    .test();
        }
        catch (final PojoTestException e)
        {
             Assert.assertEquals("Property 'Name' of class com.sunsprinter.pojotest.Pojo did not have expected value " +
                                 "of 'incorrect'.  Actual value was 'null'.",
                                 e.getMessage());
        }


        // As before, this time for the "PropertyWithNonNullDefault" property.
        try
        {
            new PojoTester(new Pojo())
                    .restrictToPublicProperties()
                    .addProperty("Name", null, "van", null, "helsing")
                    .addProperty("PropertyWithNonNullDefault", null, null, "count", null, "dracula")
                    .test();
        }
        catch (final PojoTestException e)
        {
             Assert.assertEquals("Property 'PropertyWithNonNullDefault' of class com.sunsprinter.pojotest.Pojo did " +
                                 "not have expected value of 'null'.  Actual value was 'wibble'.", e.getMessage());
        }
    }


    // TODO: 2012/06/03 KTD:  Re-enable once POJO testing is working again.
//    @Test
    public void testReadOnlyProperty() throws Exception
    {
        // Pass case.
        new PojoTester(new Pojo())
                .addProperty("ReadOnlyProperty", "giblets")
                .test();

        // Fail case.
        try
        {
            new PojoTester(new Pojo())
                    .addProperty("ReadOnlyProperty", "wibble")
                    .test();
        }
        catch (final PojoTestException e)
        {
            Assert.assertEquals("Property 'ReadOnlyProperty' of class com.sunsprinter.pojotest.Pojo did " +
                                 "not have expected value of 'wibble'.  Actual value was 'giblets'.", e.getMessage());
        }
    }
}
