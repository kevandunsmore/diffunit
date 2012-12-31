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

package com.sunsprinter.diffunit.mockito.translators;


import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.sunsprinter.diffunit.core.AbstractDiffUnitTest;
import com.sunsprinter.diffunit.core.translators.ITranslator;
import com.sunsprinter.diffunit.core.translators.TranslationException;
import com.sunsprinter.diffunit.junit.rules.DiffUnitRule;


/**
 * MockitoSpiedObjectHexIdsReplacementPairTest
 *
 * @author Kevan Dunsmore
 * @created 2011/11/15
 */
public final class MockitoSpiedObjectHexIdsReplacementPairTest extends AbstractDiffUnitTest
{
    /**
     * Mark this test as a DiffUnit test.
     */
    @Rule
    public DiffUnitRule _diffUnitRule = new DiffUnitRule(this);

    private Collection<String> _inputs;


    @Before
    public void setUp()
    {
        _inputs = new LinkedList<String>();

        _inputs.add("$$EnhancerByMockitoWithCGLIB$$123AAff");
        _inputs.add("com.sunsprinter.diffunit.mockito.translators.MockitoSpiedObjectHexIdsReplacementPairTest" +
                    "$Inner$$EnhancerByMockitoWithCGLIB$$1c380094#1.method()\n" +
                    "com.sunsprinter.diffunit.mockito.translators.MockitoSpiedObjectHexIdsReplacementPairTest.method()");

        _inputs.add("There are other considerations, but without seeing the sample datafile and your " +
                    "anticipated search pattern, it is hard to give any more thoughts.");
    }


    @Test
    public void testReplacement()
    {
        final Map<String, MockitoSpiedObjectHexIdsReplacementPair> pairsToTest = new LinkedHashMap<String, MockitoSpiedObjectHexIdsReplacementPair>();
        pairsToTest.put("Default", new MockitoSpiedObjectHexIdsReplacementPair());
        pairsToTest.put("Custom Translator", new MockitoSpiedObjectHexIdsReplacementPair("|REPLACED|"));
        pairsToTest.put("Custom Translator Constructor", new MockitoSpiedObjectHexIdsReplacementPair(new ITranslator<String>()
        {
            @Override
            public String translate(final String object) throws TranslationException
            {
                return "|REPLACED BY CUSTOM TRANSLATOR|";
            }
        }));

        // Go through all replacement pairs and replace matching blocks.
        for (final String description : pairsToTest.keySet())
        {
            add("---- " + description + " ----");

            for (final String input : _inputs)
            {
                final MockitoSpiedObjectHexIdsReplacementPair pair = pairsToTest.get(description);
                final Pattern pattern = Pattern.compile(pair.getRegExp());
                final Matcher matcher = pattern.matcher(input);

				final StringBuffer sb = new StringBuffer();

				while (matcher.find())
				{
					final String matched = input.substring(matcher.start(), matcher.end());
					final String replacement = pair.getTranslator().translate(matched);
					matcher.appendReplacement(sb, replacement);
				}
				matcher.appendTail(sb);

                add(input + " ->");
                add(sb.toString());
                addBlankLine();
            }

            addBlankLine();
            addBlankLine();
        }
    }
}
