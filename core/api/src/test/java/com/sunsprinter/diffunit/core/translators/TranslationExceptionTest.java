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


import junit.framework.Assert;
import org.junit.Test;


/**
 * Tests the functionality of the {@link com.sunsprinter.diffunit.core.translators.TranslationException} class.
 *
 * @author Kevan Dunsmore
 * @created 2013/01/02
 */
public class TranslationExceptionTest
{
    @Test
    public void testInitializationWithMessage() throws Exception
    {
        TranslationException ex = new TranslationException(this, "hello");
        Assert.assertEquals("hello", ex.getMessage());
        Assert.assertSame(this, ex.getFailedObject());

        final Object culprit = new Object();
        ex = new TranslationException(culprit, "world");
        Assert.assertEquals("world", ex.getMessage());
        Assert.assertSame(culprit, ex.getFailedObject());
    }

    @Test
    public void testInitializationWithMessageAndCause() throws Exception
    {
        Exception cause = new Exception();
        TranslationException ex = new TranslationException(this, "hello", cause);
        Assert.assertEquals("hello", ex.getMessage());
        Assert.assertSame(this, ex.getFailedObject());
        Assert.assertSame(cause, ex.getCause());

        final Object culprit = new Object();
        cause = new Exception();
        ex = new TranslationException(culprit, "world", cause);
        Assert.assertEquals("world", ex.getMessage());
        Assert.assertSame(culprit, ex.getFailedObject());
        Assert.assertSame(cause, ex.getCause());
    }
}
