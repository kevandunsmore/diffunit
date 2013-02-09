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


import com.sunsprinter.diffunit.core.instancetracking.IObjectIdentifier;
import com.sunsprinter.diffunit.core.instancetracking.IObjectInstanceTracker;
import com.sunsprinter.diffunit.core.instancetracking.ObjectIdentifier;

import static junit.framework.Assert.assertEquals;


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
        getTranslator().setInstanceTracker(new IObjectInstanceTracker()
        {
            private int _instanceCounter;

            @Override
            public void reset()
            {
                // Do nothing.
            }


            @Override
            public IObjectIdentifier getObjectId(final Object object)
            {
                return new ObjectIdentifier(object, _instanceCounter++);
            }
        });
        getTranslator().setDelegateTranslator(new ITranslator<Object>()
        {
            @Override
            public String translate(final Object object) throws TranslationException
            {
                return "TRANSLATED: " + object.toString();
            }
        });

        assertEquals("null", getTranslator().translate(null));

        assertEquals("TRANSLATED: java.lang.String#0", getTranslator().translate("hello"));
        assertEquals("TRANSLATED: java.lang.Class#1", getTranslator().translate(getClass()));

        getTranslator().prependToOutput("<<< ");
        getTranslator().appendToOutput(" >>>");

        assertEquals("<<< TRANSLATED: java.lang.String#2 >>>", getTranslator().translate("hello"));
        assertEquals("<<< TRANSLATED: java.lang.Class#3 >>>", getTranslator().translate(getClass()));
    }
}
