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

package com.sunsprinter.diffunit.core.translators;


import static org.junit.Assert.fail;


/**
 * Tests the functionality of the {@link com.sunsprinter.diffunit.core.translators.ObjectInstanceTrackingTranslator}
 * class.
 *
 * @author Kevan Dunsmore
 * @created 2013/02/09
 */
public class ObjectInstanceTrackingTranslatorTest extends AbstractDelegatingTranslatorTest<ObjectInstanceTrackingTranslator>
{
    @Override
    protected ObjectInstanceTrackingTranslator createTranslator()
    {
        return new ObjectInstanceTrackingTranslator();
    }


    @Override
    public void testTranslate() throws Exception
    {
        fail("broken");
    }
}
