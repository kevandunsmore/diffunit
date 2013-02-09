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


import com.sunsprinter.diffunit.core.common.AbstractTestingContextUserTest;
import com.sunsprinter.diffunit.core.instancetracking.IObjectInstanceTracker;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertSame;
import static org.mockito.Mockito.mock;


/**
 * Base class for testing {@link com.sunsprinter.diffunit.core.translators.AbstractTranslator} and subclasses.
 *
 * @author Kevan Dunsmore
 * @created 2013/01/26
 */
public abstract class AbstractTranslatorTest<T extends AbstractTranslator> extends AbstractTestingContextUserTest<T>
{
    @Override
    protected final T createTestingContextUser()
    {
        return createTranslator();
    }


    /**
     * Factory method to create a translator.
     */
    protected abstract T createTranslator();


    protected T getTranslator()
    {
        return getTestingContextUser();
    }


    @Override
    @Test
    public void testAccessorsAndMutators() throws Exception
    {
        super.testAccessorsAndMutators();

        assertEquals("", getTranslator().getPreTranslationString());
        getTranslator().setPreTranslationString("giblets");
        assertEquals("giblets", getTranslator().getPreTranslationString());

        assertSame(getTranslator(), getTranslator().prependToOutput("prependValue"));
        assertEquals("prependValue", getTranslator().getPreTranslationString());

        assertEquals("", getTranslator().getPostTranslationString());
        getTranslator().setPostTranslationString("wibble");
        assertEquals("wibble", getTranslator().getPostTranslationString());

        assertSame(getTranslator(), getTranslator().appendToOutput("appendValue"));
        assertEquals("appendValue", getTranslator().getPostTranslationString());

        final IObjectInstanceTracker instanceTracker = mock(IObjectInstanceTracker.class);
        assertNull(getTranslator().getInstanceTracker());
        getTranslator().setInstanceTracker(instanceTracker);
        assertSame(instanceTracker, getTranslator().getInstanceTracker());
    }


    @Override
    public void testUse() throws Exception
    {
        super.testUse();

        final IObjectInstanceTracker instanceTracker = mock(IObjectInstanceTracker.class);
        assertSame(getTranslator(), getTranslator().use(instanceTracker));
        assertSame(instanceTracker, getTranslator().getInstanceTracker());
    }


    @Test
    public abstract void testTranslate() throws Exception;
}
