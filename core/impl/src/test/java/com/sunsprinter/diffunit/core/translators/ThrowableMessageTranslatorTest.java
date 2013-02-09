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


import com.sunsprinter.diffunit.core.context.TestingContext;
import com.sunsprinter.diffunit.core.instancetracking.ObjectInstanceTracker;
import junit.framework.Assert;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;


/**
 * Tests the functionality of the {@link com.sunsprinter.diffunit.core.translators.ThrowableMessageTranslator} class.
 *
 * @author Kevan Dunsmore
 * @created 2013/02/09
 */
public class ThrowableMessageTranslatorTest extends AbstractTranslatorTest<ThrowableMessageTranslator>
{
    @Override
    protected ThrowableMessageTranslator createTranslator()
    {
        return new ThrowableMessageTranslator();
    }


    @Override
    public void testAccessorsAndMutators() throws Exception
    {
        super.testAccessorsAndMutators();

        Assert.assertTrue(getTranslator().getIncludeCause());
        getTranslator().setIncludeCause(false);
        Assert.assertFalse(getTranslator().getIncludeCause());

        Assert.assertSame(getTranslator(), getTranslator().includeCause());
        Assert.assertTrue(getTranslator().getIncludeCause());

        Assert.assertSame(getTranslator(), getTranslator().excludeCause());
        Assert.assertFalse(getTranslator().getIncludeCause());
    }


    @SuppressWarnings({"unchecked", "ThrowableInstanceNeverThrown"})
    @Test
    public void testTranslate() throws Exception
    {
        final TestingContext testingContext = new TestingContext();
        testingContext.setInstanceTracker(new ObjectInstanceTracker());
        getTranslator().use(testingContext);

        assertEquals("null", getTranslator().translate(null));

        Exception outer = new Exception("outer");
        assertEquals("java.lang.Exception#1(message=outer)", getTranslator().translate(outer));

        outer = new Exception("outer", new Exception("inner1"));
        assertEquals("java.lang.Exception#2(message=outer)\n" +
                             "  caused by java.lang.Exception#3(message=inner1)", getTranslator().translate(outer));

        outer = new Exception("outer", new Exception("inner1", new Exception("inner2")));
        assertEquals("java.lang.Exception#4(message=outer)\n" +
                             "  caused by java.lang.Exception#5(message=inner1)\n" +
                             "    caused by java.lang.Exception#6(message=inner2)", getTranslator().translate(outer));

        getTranslator().prependToOutput("<<< ");
        getTranslator().appendToOutput(" >>>");

        outer = new Exception("outer");
        assertEquals("<<< java.lang.Exception#7(message=outer) >>>", getTranslator().translate(outer));

        outer = new Exception("outer", new Exception("inner1"));
        assertEquals("<<< java.lang.Exception#8(message=outer)\n" +
                             "  caused by java.lang.Exception#9(message=inner1) >>>", getTranslator().translate(outer));

        outer = new Exception("outer", new Exception("inner1", new Exception("inner2")));
        assertEquals("<<< java.lang.Exception#10(message=outer)\n" +
                             "  caused by java.lang.Exception#11(message=inner1)\n" +
                             "    caused by java.lang.Exception#12(message=inner2) >>>", getTranslator().translate(outer));
    }
}
