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

package com.sunsprinter.diffunit.core.comparison;


import com.sunsprinter.diffunit.core.context.TestingContext;
import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import java.io.File;
import java.io.InputStream;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;


/**
 * Tests the functionality of the {@link com.sunsprinter.diffunit.core.comparison.AbstractFileComparer} class.
 *
 * @author Kevan Dunsmore
 * @created 2013/01/05
 */
@DiffUnitInputLocation(location = "/{CustomParentDirectory}/{CustomChildDirectory}")
public class UnitAbstractFileComparerTest
{
    /**
     * The comparer under test.
     */
    private TestFileComparer _comparer;

    /**
     * A context to use to test the file comparer.
     */
    private TestingContext _testingContext;

    /**
     * The name of the currently-executing test.
     */
    private String _testName;

    /**
     * The output directory for test files.
     */
    private File _outputDirectory;

    /**
     * A JUnit rule to extract the test name before the test runs.
     */
    @Rule
    public TestRule _rule = new TestRule()
    {
        @Override
        public Statement apply(Statement base, Description description)
        {
            _testName = description.getMethodName();
            return base;
        }
    };


    @Before
    public void setUp() throws Exception
    {
        _testingContext = new TestingContext();
        _testingContext.setTest(this);
        _testingContext.setTestMethod(getClass().getMethod(_testName));

        _testingContext.getNameValuePairs().put("TestClassName", getClass().getSimpleName());
        _testingContext.getNameValuePairs().put("TestName", _testName);
        _testingContext.getNameValuePairs().put("CustomParentDirectory", "CustomParent");
        _testingContext.getNameValuePairs().put("CustomChildDirectory", "CustomChild");

        _comparer = new TestFileComparer();
        _comparer.setTestingContext(_testingContext);

        // Create an output directory.
        _outputDirectory = new File(String.format("target/%s/%s", getClass().getSimpleName(), _testName));
        if (_outputDirectory.exists())
        {
            FileUtils.forceDelete(_outputDirectory);
        }
        FileUtils.forceMkdir(_outputDirectory);
    }


    @Test
    public void testAccessorsAndMutators() throws Exception
    {
        assertSame(_testingContext, _comparer.getTestingContext());
        final TestingContext testingContext = new TestingContext();
        _comparer.setTestingContext(testingContext);
        assertSame(testingContext, _comparer.getTestingContext());

        assertTrue(_comparer.getFilesToCompare().isEmpty());
        final Collection<File> toCompare = Collections.emptyList();
        _comparer.setFilesToCompare(toCompare);
        assertSame(toCompare, _comparer.getFilesToCompare());

        assertTrue(_comparer.getFailOnNoFilesRegistered());
        _comparer.setFailOnNoFilesRegistered(false);
        assertFalse(_comparer.getFailOnNoFilesRegistered());
        _comparer.setFailOnNoFilesRegistered(true);
        assertTrue(_comparer.getFailOnNoFilesRegistered());
    }


    @Test
    public void testRegisterFileToCompare() throws Exception
    {
        final File firstFile = new File("hello/world.txt");
        _comparer.registerFileToCompare(firstFile);

        assertEquals(1, _comparer.getFilesToCompare().size());
        assertSame(firstFile, _comparer.getFilesToCompare().iterator().next());

        final File secondFile = new File("wibble/giblets.txt");
        _comparer.registerFileToCompare(secondFile);

        assertEquals(2, _comparer.getFilesToCompare().size());
        final Iterator<File> files = _comparer.getFilesToCompare().iterator();
        assertSame(firstFile, files.next());
        assertSame(secondFile, files.next());
    }


    @Test
    public void testCompareFiles() throws Exception
    {
        // By default the comparer should fail the test if there are no files registered for comparison.
        try
        {
            _comparer.compareAllFiles();
        }
        catch (final TestFileComparer.TestFileComparerException e)
        {
            assertEquals("No files registered for comparison by DiffUnit.", e.getMessage());
        }

        // But we can configure it to not fail when this happens...
        _comparer.setFailOnNoFilesRegistered(false);
        _comparer.compareAllFiles();
    }


    @Test
    public void testWriteAndCompareDefaultOutputFile() throws Exception
    {
        final File outputFile = new File(_outputDirectory, "results.txt");
        FileUtils.writeStringToFile(outputFile, "This text should appear in the default results.txt output file", "UTF-8");

        _comparer.registerFileToCompare(outputFile);
        _comparer.compareAllFiles();
    }


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
    @DiffUnitInputLocation(locationType = InputLocationType.FILE_SYSTEM, location = "target/{TestClassName}/{TestName}")
    public void testCustomLocationOnFileSystemAtMethodLevel() throws Exception
    {
        // First copy the known good file to the right location.
        final File knownGoodDir = new File(String.format("target/%s/%s", getClass().getSimpleName(), _testName));
        if (knownGoodDir.exists())
        {
            FileUtils.forceDelete(knownGoodDir);
        }
        FileUtils.forceMkdir(knownGoodDir);
        try (final InputStream is = getClass().getResourceAsStream(String.format("/%s/%s/results.txt", getClass().getSimpleName(), _testName)))
        {
            FileUtils.copyInputStreamToFile(is, new File(knownGoodDir, "results.txt"));
        }

        // Then run the test.
        testWriteAndCompareDefaultOutputFile();
    }


    @Test
    @DiffUnitInputLocation(location = "/{TestClassName}/{TestName}")
    public void testNoInputFileFoundInClassPath() throws Exception
    {
        try
        {
            testWriteAndCompareDefaultOutputFile();
            fail("No exception thrown");
        }
        catch (final Exception e)
        {
            final String expected = "Generated file /<<SNIP>>/target/UnitAbstractFileComparerTest/testNoInputFileFoundInClassPath/results.txt does not match known good file /UnitAbstractFileComparerTest/testNoInputFileFoundInClassPath/results.txt.  First difference detected at line number 1, position 0.\n" +
                    "\n" +
                    "Known Good: Input file /UnitAbstractFileComparerTest/testNoInputFileFoundInClassPath/results.txt not found.\n" +
                    "Generated : This text should appear in the default results.txt output file\n";

            final File currentDir = new File(".");
            final String actualMessage = e.getMessage().replace(currentDir.getCanonicalPath(), "/<<SNIP>>");

            assertEquals(expected, actualMessage);
        }
    }


    @Test
    @DiffUnitInputLocation(locationType = InputLocationType.FILE_SYSTEM, location = "/DoesNotExist")
    public void testNoInputFileFoundInFileSystem() throws Exception
    {
        try
        {
            testWriteAndCompareDefaultOutputFile();
            fail("No exception thrown");
        }
        catch (final Exception e)
        {
            final String expected = "Generated file /<<SNIP>>/target/UnitAbstractFileComparerTest/testNoInputFileFoundInFileSystem/results.txt does not match known good file /DoesNotExist/results.txt.  First difference detected at line number 1, position 0.\n" +
                    "\n" +
                    "Known Good: Input file /DoesNotExist/results.txt not found.\n" +
                    "Generated : This text should appear in the default results.txt output file\n";

            final File currentDir = new File(".");
            final String actualMessage = e.getMessage().replace(currentDir.getCanonicalPath(), "/<<SNIP>>");

            assertEquals(expected, actualMessage);
        }
    }


    @Test
    @DiffUnitInputLocation(location = "/{TestClassName}/{TestName}")
    public void testInputFilesHaveDifferentNumberLines() throws Exception
    {
        try
        {
            testWriteAndCompareDefaultOutputFile();
            fail("No exception thrown");
        }
        catch (final Exception e)
        {
            final String expected = "Generated file /<<SNIP>>/target/UnitAbstractFileComparerTest/testInputFilesHaveDifferentNumberLines/results.txt does not match known good file /UnitAbstractFileComparerTest/testInputFilesHaveDifferentNumberLines/results.txt.  The number of lines is different.  The known good file has 4 lines.  The generated file has 1 lines.  First difference detected at line number 1, position 0.\n" +
                    "\n" +
                    "Known Good: I have more\n" +
                    "Generated : This text should appear in the default results.txt output file\n";

            final File currentDir = new File(".");
            final String actualMessage = e.getMessage().replace(currentDir.getCanonicalPath(), "/<<SNIP>>");

            assertEquals(expected, actualMessage);
        }
    }

}
