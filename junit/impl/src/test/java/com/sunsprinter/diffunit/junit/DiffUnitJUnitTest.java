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

package com.sunsprinter.diffunit.junit;


import com.sunsprinter.diffunit.core.AbstractDiffUnitTest;
import com.sunsprinter.diffunit.junit.rules.DiffUnitRule;
import org.junit.Rule;
import org.junit.Test;


/**
 * Test that DiffUnit is correctly integrated into JUnit.
 *
 * @author Kevan Dunsmore
 * @created 2013/01/03
 */
public class DiffUnitJUnitTest extends AbstractDiffUnitTest
{
    /**
     * Signify that this is a JUnit test case.
     */
    @Rule
    public DiffUnitRule _diffUnitRule = new DiffUnitRule(this);


    @Test
    public void testWriteAndCompareDefaultOutputFile() throws Exception
    {
        add("This text should appear in the default results.txt output file");
    }


    @Test
    public void testWriteAndCompareMultipleOutputFiles() throws Exception
    {
        add("Should be in results file 1");
        writeFile("result-file-1.txt");

        add("Should be in results file 2");
        writeFile("result-file-2.txt");

        add("Should be in results file 3");
        writeFile("result-file-3.txt");
    }

    @Test
    public void testWriteAndCompareMultipleOutputFilesWithDefaultRemainder() throws Exception
    {
        add("Should be in results file 1");
        writeFile("result-file-1.txt");

        add("Should be in results file 2");
        writeFile("result-file-2.txt");

        add("Should be in results file 3");
        writeFile("result-file-3.txt");

        add("This text should appear in the default results.txt output file");
    }
}
