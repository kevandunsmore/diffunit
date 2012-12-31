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

package com.sunsprinter.diffunit.core.context;


import java.io.File;
import java.util.Collection;
import java.util.List;

import com.sunsprinter.diffunit.core.comparison.IFileComparer;
import com.sunsprinter.diffunit.core.instancetracking.IObjectInstanceTracker;
import com.sunsprinter.diffunit.core.output.IOutputManager;
import com.sunsprinter.diffunit.core.translators.IRegExReplacementPair;
import com.sunsprinter.diffunit.core.translators.IRootTranslator;


/**
 * ITestingContext
 *
 * @author Kevan Dunsmore
 * @created 2011/11/11
 */
public interface ITestingContext
{
    String getTestName();
    Class<?> getTestClass();
    Object getTest();
    IObjectInstanceTracker getInstanceTracker();
    IRootTranslator getRootTranslator();
    Collection<Object> getOutputObjects();
    IOutputManager getOutputManager();
    IFileComparer getFileComparer();
    File getOutputDirectory();
    List<IRegExReplacementPair> getRegExReplacementPairs();
    void setRegExReplacementPairs(List<IRegExReplacementPair> replacementPairs);
}
