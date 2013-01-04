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

package com.sunsprinter.diffunit.core.context;


import java.io.File;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.sunsprinter.diffunit.core.comparison.IFileComparer;
import com.sunsprinter.diffunit.core.instancetracking.IObjectInstanceTracker;
import com.sunsprinter.diffunit.core.output.IOutputManager;
import com.sunsprinter.diffunit.core.translators.IRegExReplacementPair;
import com.sunsprinter.diffunit.core.translators.IRootTranslator;


/**
 * The testing context is central to the operation of DiffUnit.  All collaborators are held by the testing context. You
 * can have any of the collaborators injected individually into your test case or the entire testing context itself. To
 * get a reference to the testing context:<p/>
 *
 * <pre>
 * public class MyTest
 * {
 *    // Necessary to initialize DiffUnit under JUnit.
 *    {@code @Rule} public DiffUnitRule _diffUnitRule = new DiffUnitRule(this);
 *
 *    {@code @DiffUnitInject} private ITestingContext _testingContext;
 * }
 * </pre>
 *
 * @author Kevan Dunsmore
 * @created 2011/11/11
 */
public interface ITestingContext
{
    /**
     * Returns the name of the currently executing test.
     */
    String getTestName();

    /**
     * Returns the method of the currently executing test.
     */
    Method getTestMethod();

    /**
     * Returns the class of the test.
     */
    Class<?> getTestClass();

    /**
     * Returns the actual test instance being executed.
     */
    Object getTest();

    /**
     * Returns the DiffUnit instance tracker object.
     */
    IObjectInstanceTracker getInstanceTracker();

    /**
     * Returns the root DiffUnit translator instance.
     */
    IRootTranslator getRootTranslator();

    /**
     * Returns the collection of objects that will be translated and written by the output manager to a file for
     * comparison with a known good version.
     */
    Collection<Object> getOutputObjects();

    /**
     * Returns the output manager responsible for initiating translation and writing the resulting string to a file for
     * comparison with a known good version.
     */
    IOutputManager getOutputManager();

    /**
     * Returns the object that compares all written files with known good versions.
     */
    IFileComparer getFileComparer();

    /**
     * Returns the output directory for the currently-executing test.
     */
    File getOutputDirectory();

    /**
     * Returns the replacement pairs that should be processed before the output text is written to a file.
     */
    List<IRegExReplacementPair> getRegExReplacementPairs();

    /**
     * Sets the replacement pair collection.
     *
     * @param replacementPairs A collection of replacement pairs that should be processed before the output text is
     *                         written to a file.
     */
    void setRegExReplacementPairs(List<IRegExReplacementPair> replacementPairs);

    Map<String, String> getNameValuePairs();
}
