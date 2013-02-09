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

import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;


/**
 * Tests the functionality of the {@link com.sunsprinter.diffunit.core.translators.AbstractDelegatingTranslator} class.
 *
 * @author Kevan Dunsmore
 * @created 2013/02/09
 */
public class UnitAbstractDelegatingTranslatorTest extends AbstractTranslatorTest<UnitAbstractDelegatingTranslatorTest.ConcreteTranslator>
{
    @Override
    protected ConcreteTranslator createTranslator()
    {
        return new ConcreteTranslator();
    }


    @SuppressWarnings("unchecked")
    @Override
    public void testAccessorsAndMutators() throws Exception
    {
        super.testAccessorsAndMutators();

        // Test that the default delegate translator is the root one from the testing context.
        final TestingContext testingContext = new TestingContext();
        final IRootTranslator rootTranslator = mock(IRootTranslator.class);
        testingContext.setRootTranslator(rootTranslator);
        getTranslator().use(testingContext);
        assertSame(rootTranslator, getTranslator().getDelegateTranslator());

        // Now set a new delegate and test again.
        ITranslator<Object> mockTranslator = mock(ITranslator.class);
        getTranslator().setDelegateTranslator(mockTranslator);
        assertSame(mockTranslator, getTranslator().getDelegateTranslator());

        // Again, this time with the "use" method.
        mockTranslator = mock(ITranslator.class);
        assertSame(getTranslator(), getTranslator().use(mockTranslator));
        assertSame(mockTranslator, getTranslator().getDelegateTranslator());
    }


    @Override
    public void testTranslate() throws Exception
    {
        // Do nothing.  The class doesn't define a translate method.
    }


    /**
     * A concrete instance of the class, for testing purposes.
     */
    protected static class ConcreteTranslator extends AbstractDelegatingTranslator
    {
        @Override
        protected String doTranslate(Object object) throws TranslationException
        {
            return null;
        }
    }
}
