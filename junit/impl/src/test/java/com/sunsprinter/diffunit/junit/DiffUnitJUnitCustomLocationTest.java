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


import com.sunsprinter.diffunit.core.comparison.DiffUnitInputLocation;
import com.sunsprinter.diffunit.core.comparison.InputLocationType;
import org.apache.commons.io.FileUtils;
import org.junit.Test;

import java.io.File;
import java.io.InputStream;


/**
 * Tests the redirection of input location functionality by extending another test and then changing the default
 * location to point at that tests known good directory.
 *
 * @author Kevan Dunsmore
 * @created 2013/01/03
 */
@DiffUnitInputLocation(location = "/DiffUnitJUnitTest/{TestName}")
public class DiffUnitJUnitCustomLocationTest extends DiffUnitJUnitTest
{
    // This test overrides the class-level customized input location.  It puts the input location back to the original
    // location.
    @Test
    @DiffUnitInputLocation(location = "/{TestClassName}/{TestName}")
    public void testCustomLocationAtMethodLevel() throws Exception
    {
        testWriteAndCompareDefaultOutputFile();
    }


    // Now we'll change the known-good location to the file system and test again.
    @Test
    @DiffUnitInputLocation(locationType = InputLocationType.FILE_SYSTEM, location = "target/{TestClassName}/custom")
    public void testCustomLocationOnFileSystemAtMethodLevel() throws Exception
    {
        // First copy the known good file to the right location.
        final File knownGoodDir = new File("target/" + getClass().getSimpleName() + "/custom");
        if (knownGoodDir.exists())
        {
            FileUtils.forceDelete(knownGoodDir);
        }
        FileUtils.forceMkdir(knownGoodDir);
        try (final InputStream is = getClass().getResourceAsStream("/DiffUnitJUnitTest/testWriteAndCompareDefaultOutputFile/results.txt"))
        {
            FileUtils.copyInputStreamToFile(is, new File(knownGoodDir, "results.txt"));
        }

        // Then run the test.
        testWriteAndCompareDefaultOutputFile();
    }
}
