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
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sunsprinter.diffunit.core.comparison.IFileComparer;
import com.sunsprinter.diffunit.core.instancetracking.IObjectInstanceTracker;
import com.sunsprinter.diffunit.core.output.IOutputManager;
import com.sunsprinter.diffunit.core.translators.IRegExReplacementPair;
import com.sunsprinter.diffunit.core.translators.IRootTranslator;


/**
 * Bean implementation of the {@link com.sunsprinter.diffunit.core.context.ITestingContext} interface.
 *
 * @author Kevan Dunsmore
 * @created 2011/11/12
 */
public class TestingContext implements ITestingContext
{
    private Method _testMethod;
    private Object _test;
    private IObjectInstanceTracker _instanceTracker;
    private IRootTranslator _rootTranslator;
    private Collection<Object> _outputObjects;
    private IOutputManager _outputManager;
    private IFileComparer _fileComparer;
    private File _outputDirectory;
    private List<IRegExReplacementPair> _regExReplacementPairs = new ArrayList<>();
    private Map<String, String> _nameValuePairs = new HashMap<>();


    public File getOutputDirectory()
    {
        return _outputDirectory;
    }


    public void setOutputDirectory(final File outputDirectory)
    {
        _outputDirectory = outputDirectory;
    }


    public IOutputManager getOutputManager()
    {
        return _outputManager;
    }


    public void setOutputManager(final IOutputManager outputManager)
    {
        _outputManager = outputManager;
    }


    public Collection<Object> getOutputObjects()
    {
        return _outputObjects;
    }


    public void setOutputObjects(final Collection<Object> outputObjects)
    {
        _outputObjects = outputObjects;
    }


    public Object getTest()
    {
        return _test;
    }


    public void setTest(final Object test)
    {
        _test = test;
    }


    public String getTestName()
    {
        return getTestMethod().getName();
    }



    public Method getTestMethod()
    {
        return _testMethod;
    }


    public void setTestMethod(Method testMethod)
    {
        _testMethod = testMethod;
    }


    public Class<?> getTestClass()
    {
        return getTest().getClass();
    }


    @Override
    public IObjectInstanceTracker getInstanceTracker()
    {
        return _instanceTracker;
    }


    public void setInstanceTracker(final IObjectInstanceTracker instanceTracker)
    {
        _instanceTracker = instanceTracker;
    }


    @Override
    public IRootTranslator getRootTranslator()
    {
        return _rootTranslator;
    }


    public void setRootTranslator(final IRootTranslator rootTranslator)
    {
        _rootTranslator = rootTranslator;
    }


    public IFileComparer getFileComparer()
    {
        return _fileComparer;
    }


    public void setFileComparer(final IFileComparer fileComparer)
    {
        _fileComparer = fileComparer;
    }


    @Override
    public List<IRegExReplacementPair> getRegExReplacementPairs()
    {
        return _regExReplacementPairs;
    }


    @Override
    public void setRegExReplacementPairs(final List<IRegExReplacementPair> regExReplacementPairs)
    {
        _regExReplacementPairs = regExReplacementPairs;
    }


    public Map<String, String> getNameValuePairs()
    {
        return _nameValuePairs;
    }


    public void setNameValuePairs(Map<String, String> nameValuePairs)
    {
        _nameValuePairs = nameValuePairs;
    }
}
