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


import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;


/**
 * Base test class for classes testing {@link com.sunsprinter.diffunit.core.translators.AbstractPropertyDrivenTranslator}
 * subclasses.
 *
 * @author Kevan Dunsmore
 * @created 2013/02/09
 */
public abstract class AbstractPropertyDrivenTranslatorTest<T extends AbstractPropertyDrivenTranslator> extends AbstractDelegatingTranslatorTest<T>
{
    @Override
    public void testAccessorsAndMutators() throws Exception
    {
        super.testAccessorsAndMutators();

        // TODO: Complete this!
        assertTrue(getTranslator().getPropertiesToTranslate().isEmpty());
        assertTrue(getTranslator().getPropertiesToTranslate() instanceof LinkedHashSet);

        final Collection<String> coll = new ArrayList<>();
        getTranslator().setPropertiesToTranslate(coll);
        assertSame(coll, getTranslator().getPropertiesToTranslate());

        assertEquals(1, getTranslator().getPropertiesToSkip().size());
        assertTrue(getTranslator().getPropertiesToSkip().contains("class"));

        final Set<String> set = new HashSet<>();
        getTranslator().setPropertiesToSkip(set);
        assertSame(set, getTranslator().getPropertiesToSkip());
    }
}
