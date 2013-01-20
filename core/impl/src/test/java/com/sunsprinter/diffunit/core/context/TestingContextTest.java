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

package com.sunsprinter.diffunit.core.context;


import com.sunsprinter.diffunit.core.comparison.IFileComparer;
import com.sunsprinter.diffunit.core.instancetracking.IObjectInstanceTracker;
import com.sunsprinter.diffunit.core.output.IOutputManager;
import com.sunsprinter.diffunit.core.translators.IRegExReplacementPair;
import com.sunsprinter.diffunit.core.translators.IRootTranslator;
import org.apache.commons.beanutils.PropertyUtilsBean;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;


/**
 * Tests the functionality of the {@link com.sunsprinter.diffunit.core.context.TestingContext} class.
 *
 * @author Kevan Dunsmore
 * @created 2013/01/14
 */
public class TestingContextTest
{
    /**
     * The context under test.
     */
    private TestingContext _context;

    /**
     * Apache commons utility class for accessor / mutator testing.
     */
    private PropertyUtilsBean _propertyUtils;


    @Before
    public void setUp() throws Exception
    {
        _context = new TestingContext();
        _propertyUtils = new PropertyUtilsBean();
    }


    @Test
    public void testAccessorsAndMutators() throws Exception
    {
        assertTrue(_context.getNameValuePairs().isEmpty());
        final Map<String, String> map = new HashMap<>();
        _context.setNameValuePairs(map);
        assertSame(map, _context.getNameValuePairs());

        assertTrue(_context.getRegExReplacementPairs().isEmpty());
        final List<IRegExReplacementPair> regExReplacementPairList = new ArrayList<>();
        _context.setRegExReplacementPairs(regExReplacementPairList);
        assertSame(regExReplacementPairList, _context.getRegExReplacementPairs());

        testAccessorAndMutator("fileComparer", null, mock(IFileComparer.class));
        testAccessorAndMutator("instanceTracker", null, mock(IObjectInstanceTracker.class));
        testAccessorAndMutator("outputDirectory", null, new File("."));
        testAccessorAndMutator("outputManager", null, mock(IOutputManager.class));
        testAccessorAndMutator("outputObjects", null, new ArrayList());
        testAccessorAndMutator("rootTranslator", null, mock(IRootTranslator.class));
        testAccessorAndMutator("test", null, this);
        testAccessorAndMutator("testMethod", null, getClass().getMethod("testAccessorsAndMutators"));

        assertEquals("testAccessorsAndMutators", _context.getTestName());
        assertSame(getClass(), _context.getTestClass());
    }


    private void testAccessorAndMutator(final String propertyName, final Object expectedInitialValue, final Object valueToSet) throws Exception
    {
        Object value = _propertyUtils.getProperty(_context, propertyName);
        assertSame(propertyName + ": initial value incorrect", expectedInitialValue, value);

        _propertyUtils.setProperty(_context, propertyName, valueToSet);
        value =  _propertyUtils.getProperty(_context, propertyName);
        assertSame(propertyName + ": incorrect value after set", valueToSet, value);
    }
}
