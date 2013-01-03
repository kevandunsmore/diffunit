/*
 * Copyright 2011-2013 Kevan Dunsmore.  All rights reserved.
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


import java.io.File;


/**
 * The file comparer is responsible for comparing each written output file with a corresponding known-good version read
 * from some location.<p/>
 *
 * The location is controlled by the {@link com.sunsprinter.diffunit.core.comparison.DiffUnitInputLocation} annotation,
 * which can be set at the class or the test method level.  By default, the file comparer follows the Maven convention
 * for file location.  It expects that the known good files are in the classpath in the location {@code
 * /{TestClassSimpleName}/{TestName}} and that for each output file there is a file of the same name in that location
 * that contains the known good text.<p/>
 *
 * For example, suppose you have a test class called {@code com.mycompany.cool.MyCoolTest} and it has a test method
 * called {@code testSomething}.  The file comparer will assume that you have the resource {@code
 * /MyCoolTest/testSomething/results.txt} in the classpath.  If you write multiple files, called "file1.txt",
 * "myXmlFile.xml" and "file2.txt", as part of your test using explicit calls to {@link
 * com.sunsprinter.diffunit.core.output.IOutputManager#writeFile(String)}, this comparer will expect you to have {@code
 * /MyCoolTest/testSomething/file1.txt}, {@code /MyCoolTest/testSomething/file2.txt} and {@code
 * /MyCoolTest/testSomething/myXmlFile.xml} in the classpath.
 *
 * @author Kevan Dunsmore
 * @created 2011/11/13
 */
public interface IFileComparer
{
    /**
     * Tells the file comparer that a new generated output file must be compared with the corresponding known good
     * file.
     *
     * @param file The file to compare.  May not be null.
     */
    void registerFileToCompare(final File file);

    /**
     * Compares all registered files with their known good versions.
     */
    void compareAllFiles() throws Exception;
}
