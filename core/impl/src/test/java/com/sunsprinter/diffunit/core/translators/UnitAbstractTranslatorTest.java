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


import org.junit.Test;

import static junit.framework.Assert.assertEquals;


/**
 * Tests the functionality of the {@link com.sunsprinter.diffunit.core.translators.AbstractTranslator} class.
 *
 * @author Kevan Dunsmore
 * @created 2013/01/26
 */
public class UnitAbstractTranslatorTest extends AbstractTranslatorTest<UnitAbstractTranslatorTest.ConcreteTranslator>
{
    @Override
    protected ConcreteTranslator createTranslator()
    {
        return new ConcreteTranslator();
    }


    @SuppressWarnings("unchecked")
    @Test
    public void testTranslate() throws Exception
    {
        assertEquals("null", getTranslator().translate(null));

        assertEquals("TRANSLATED: hello", getTranslator().translate("hello"));
        assertEquals("TRANSLATED: " + getClass().toString(), getTranslator().translate(getClass()));

        getTranslator().prependToOutput("<<< ");
        getTranslator().appendToOutput(" >>>");

        assertEquals("<<< TRANSLATED: hello >>>", getTranslator().translate("hello"));
        assertEquals("<<< TRANSLATED: " + getClass().toString() + " >>>", getTranslator().translate(getClass()));
    }


    protected static final class ConcreteTranslator extends AbstractTranslator
    {
        @Override
        protected String doTranslate(Object object) throws TranslationException
        {
            return "TRANSLATED: " + object.toString();
        }
    }
}
