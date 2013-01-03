/*
 * Copyright 2011 Kevan Dunsmore.  All rights reserved.
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


import com.sunsprinter.diffunit.core.translators.ITranslator;
import com.sunsprinter.diffunit.core.translators.RegExReplacementPair;


/**
 * A replacement pair that strips out unreproducable Mockito hashcode strings (for example <code>Mock for IFoo,
 * hashCode: 1590505011</code>).
 *
 * @author Kevan Dunsmore
 * @created 2011/11/15
 */
public class MockitoMockObjectHexIdsReplacementPair extends RegExReplacementPair
{
    private final static String REG_EX = "^*, hashCode: [0-9]+";


    /**
     * Creates a replacement pair that substitutes an empty string for every Mockito mock string it encounters.
     */
    public MockitoMockObjectHexIdsReplacementPair()
    {
        this("");
    }


    public MockitoMockObjectHexIdsReplacementPair(final String replacementValue)
    {
        super(REG_EX, replacementValue);
    }


    public MockitoMockObjectHexIdsReplacementPair(final ITranslator<String> translator)
    {
        super(REG_EX, translator);
    }
}
