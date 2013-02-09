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

package com.sunsprinter.diffunit.core.output;


import com.sunsprinter.diffunit.core.comparison.IFileComparer;
import com.sunsprinter.diffunit.core.context.ITestingContext;
import com.sunsprinter.diffunit.core.context.TestingContext;
import com.sunsprinter.diffunit.core.translators.IRootTranslator;
import com.sunsprinter.diffunit.core.translators.ITranslator;
import com.sunsprinter.diffunit.core.translators.TranslationException;
import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;


/**
 * Test class for {@link com.sunsprinter.diffunit.core.output.OutputManager}.
 *
 * @author Kevan Dunsmore
 * @created 2013/01/21
 */
public class OutputManagerTest
{
    /**
     * The output manager under test.
     */
    private OutputManager _outputManager;


    @Before
    public void setUp() throws Exception
    {
        _outputManager = new OutputManager();
    }


    @Test
    public void testAccessorsAndMutators() throws Exception
    {
        final ITestingContext testingContext = new TestingContext();
        assertNull(_outputManager.getTestingContext());
        _outputManager.setTestingContext(testingContext);
        assertSame(testingContext, _outputManager.getTestingContext());
    }


    @Test
    public void testAddingObjects() throws Exception
    {
        final List<Object> objects = new ArrayList<>();
        final TestingContext testingContext = new TestingContext();
        testingContext.setOutputObjects(objects);
        _outputManager.setTestingContext(testingContext);

        // Add a blank line and test.
        _outputManager.addBlankLine();
        assertEquals(1, objects.size());
        assertEquals("", objects.get(0));

        // Add some objects.
        _outputManager.add("hello");
        _outputManager.add(this);
        assertEquals(3, objects.size());
        assertEquals("", objects.get(0));
        assertEquals("hello", objects.get(1));
        assertSame(this, objects.get(2));

        // Add something directly as a string, forcing translation.
        testingContext.setRootTranslator(new FakeRootTranslator());
        _outputManager.addAsString(this);
        assertEquals(4, objects.size());
        assertEquals("", objects.get(0));
        assertEquals("hello", objects.get(1));
        assertSame(this, objects.get(2));
        assertEquals("TRANSLATED: " + getClass().getSimpleName(), objects.get(3));
    }


    @Test
    public void testWriteFileFailsWhenOutputFileOpenErrorOccurs() throws Exception
    {
        final TestingContext testingContext = new TestingContext();
        testingContext.setOutputObjects(new ArrayList<>());
        _outputManager.setTestingContext(testingContext);

        // We create our output dir, if it doesn't already exist.
        final File outputDir = new File("target/diffunit/" + getClass().getSimpleName());
        if (!outputDir.exists())
        {
            FileUtils.forceMkdir(outputDir);
        }
        testingContext.setOutputDirectory(outputDir);

        // Now create a test sub directory.
        final String dirName = "testWriteFileFailsWhenOutputFileOpenErrorOccurs";
        final File testSubDir = new File(outputDir, dirName);
        if (!testSubDir.exists())
        {
            FileUtils.forceMkdir(testSubDir);
        }

        // Now have the output manager write a file with the same name as the directory we just created.  It will fail,
        // allowing us to test that we handle the situation properly.
        try
        {
            _outputManager.writeFile(dirName);
            fail("Expected DiffUnitOutputManagerRuntimeException to be thrown.");
        }
        catch (final DiffUnitOutputManagerRuntimeException e)
        {
            assertEquals("Unable to write file " + dirName + ".  Cannot create PrintWriter.", e.getMessage());
            assert(e.getCause() instanceof FileNotFoundException);
        }
    }


    @Test
    public void testWriteOutputObjects() throws Exception
    {
        final TestingContext testingContext = new TestingContext();
        testingContext.setOutputObjects(new ArrayList<>());
        _outputManager.setTestingContext(testingContext);

        // We create our output dir, if it doesn't already exist.
        final File outputDir = new File(String.format("target/diffunit/%s/%s", getClass().getSimpleName(), "testWriteOutputObjects"));
        if (outputDir.exists())
        {
            FileUtils.forceDelete(outputDir);
        }
        FileUtils.forceMkdir(outputDir);
        testingContext.setOutputDirectory(outputDir);

        testingContext.setRootTranslator(new FakeRootTranslator());

        final IFileComparer fileComparer = mock(IFileComparer.class);
                                    testingContext.setFileComparer(fileComparer);

        // OK, now add some stuff to the output objects.
        _outputManager.add("hello");
        _outputManager.add("world");

        final File outputFile = new File(outputDir, "results.txt");
        _outputManager.writeFile(outputFile.getName());

        assertTrue(outputFile.exists());
        final List<String> fileLines = FileUtils.readLines(outputFile);
        assertEquals(2, fileLines.size());
        assertEquals("TRANSLATED: hello", fileLines.get(0));
        assertEquals("TRANSLATED: world", fileLines.get(1));

        verify(fileComparer).registerFileToCompare(eq(outputFile));
    }


    private final class FakeRootTranslator implements IRootTranslator
    {
        @Override
        public String translate(Object object) throws TranslationException
        {
            return "TRANSLATED: " + ((object instanceof String) ? object.toString() : object.getClass().getSimpleName());
        }


        @Override
        public void bind(ITranslator<?> translator, Class<?>... types)
        {
            // Do nothing - we don't care.
        }
    }
}
