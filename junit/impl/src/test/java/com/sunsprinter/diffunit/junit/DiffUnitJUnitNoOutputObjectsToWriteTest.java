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
import com.sunsprinter.diffunit.junit.runners.model.DiffUnitStatement;
import junit.framework.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import java.io.File;


/**
 * Tests that DiffUnit functions correctly when there's no output objects to be written.  Must be a standalone test
 * because of the custom rule that executes.
 *
 * @author Kevan Dunsmore
 * @created 2013/01/03
 */
public class DiffUnitJUnitNoOutputObjectsToWriteTest extends AbstractDiffUnitTest
{
    /**
     * Signify that this is a JUnit test case.
     */
    @Rule
    public DiffUnitRule _diffUnitRule = new DiffUnitRule(this)
    {
        @Override
        public Statement apply(Statement base, Description description)
        {
            return new DiffUnitStatement(description, getTest(), base, getNameValuePairs())
            {
                @Override
                public void evaluate() throws Throwable
                {
                    super.evaluate();

                    // Make sure no results file exists.
                    final File outputFile = new File(getTestingContext().getOutputDirectory(), "results.txt");
                    Assert.assertFalse("output file " + outputFile.getAbsolutePath() + " should not exist", outputFile.exists());
                }
            };
        }
    };


    @Test
    public void testNoOutputObjectsToWriteToFile() throws Exception
    {
        // This test is empty in order to trick JUnit into running the DiffUnit rule.  All testing is done as part of
        // the rule above.
    }
}
