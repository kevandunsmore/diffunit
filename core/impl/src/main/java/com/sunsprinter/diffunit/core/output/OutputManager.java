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

package com.sunsprinter.diffunit.core.output;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

import org.apache.commons.io.IOUtils;

import com.sunsprinter.diffunit.core.context.ITestingContext;


/**
 * OutputManager
 *
 * @author Kevan Dunsmore
 * @created 2011/11/13
 */
public class OutputManager implements IOutputManager
{
    private ITestingContext _testingContext;


    public ITestingContext getTestingContext()
    {
        return _testingContext;
    }


    public void setTestingContext(final ITestingContext testingContext)
    {
        _testingContext = testingContext;
    }


    @Override
    public void addBlankLine() throws RuntimeException
    {
        add("");
    }


    @Override
    public void add(final Object object) throws RuntimeException
    {
        getTestingContext().getOutputObjects().add(object);
    }


    @Override
    public void addAsString(final Object object) throws RuntimeException
    {
        getTestingContext().getOutputObjects().add(getTestingContext().getRootTranslator().translate(object));
    }


    @Override
    public void writeFile(final String fileName) throws RuntimeException
    {
        final File outputFile = new File(getTestingContext().getOutputDirectory(), fileName);
        final PrintWriter writer;
        try
        {
            writer = new PrintWriter(outputFile);
        }
        catch (final FileNotFoundException e)
        {
            throw new RuntimeException("Unable to write file " + fileName + ".  Cannot create PrintWriter.", e);
        }
        try
        {
            for (final Object object : getTestingContext().getOutputObjects())
            {
                writer.println(getTestingContext().getRootTranslator().translate(object));
            }
        }
        finally
        {
            IOUtils.closeQuietly(writer);

            // Clear the output objects collection in preparation for the next file, if any.
            getTestingContext().getOutputObjects().clear();
        }

        // Register the file we've just written with the file comparer, for comparison later with its known good version.
        getTestingContext().getFileComparer().registerFileToCompare(outputFile);
    }
}
