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

package com.sunsprinter.diffunit.mockito.stubbing;


import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.sunsprinter.diffunit.core.context.ITestingContext;
import com.sunsprinter.diffunit.core.context.TestingContextHolder;
import com.sunsprinter.diffunit.core.output.IOutputManager;
import com.sunsprinter.diffunit.core.translators.IRootTranslator;


/**
 * MockitoMethodCallLoggingAnswer
 *
 * @author Kevan Dunsmore
 * @created 2011/11/14
 */
public class MockitoMethodCallLoggingAnswer<T> implements Answer<T>
{
    private ITestingContext _testingContext;
    private T _returnValue;
    private Answer<T> _delegateAnswer = createCallRealMethodAnswer();


    @SuppressWarnings("unchecked")
    public <I extends MockitoMethodCallLoggingAnswer<T>> I use(final ITestingContext testingContext)
    {
        setTestingContext(testingContext);
        return (I)this;
    }


    @SuppressWarnings("unchecked")
    public <I extends MockitoMethodCallLoggingAnswer<T>> I respondWithValue(final T returnValue)
    {
        setReturnValue(returnValue);
        setDelegateAnswer(createReturnValueAnswer());
        return (I)this;
    }


    @SuppressWarnings("unchecked")
    public <I extends MockitoMethodCallLoggingAnswer<T>> I respondWithAnswer(final Answer<T> answer)
    {
        setReturnValue(null);
        setDelegateAnswer(answer);
        return (I)this;
    }


    @SuppressWarnings("unchecked")
    public <I extends MockitoMethodCallLoggingAnswer<T>> I respondWithRealMethod()
    {
        setReturnValue(null);
        setDelegateAnswer(createCallRealMethodAnswer());
        return (I)this;
    }


    protected ITestingContext getTestingContext()
    {
        return _testingContext == null ? TestingContextHolder.CONTEXT : _testingContext;
    }


    protected void setTestingContext(final ITestingContext testingContext)
    {
        _testingContext = testingContext;
    }


    protected T getReturnValue()
    {
        return _returnValue;
    }


    protected void setReturnValue(final T returnValue)
    {
        _returnValue = returnValue;
    }


    protected Answer<T> getDelegateAnswer()
    {
        return _delegateAnswer;
    }


    protected void setDelegateAnswer(final Answer<T> delegateAnswer)
    {
        _delegateAnswer = delegateAnswer;
    }


    @SuppressWarnings("unchecked")
    @Override
    public T answer(final InvocationOnMock invocation) throws Throwable
    {
        final IOutputManager outputManager = getTestingContext().getOutputManager();
        final IRootTranslator rootTranslator = getTestingContext().getRootTranslator();

        final String methodName = String.format("%s.%s",
                                                rootTranslator.translate(invocation.getMock()),
                                                invocation.getMethod().getName());

        outputManager.add(String.format("BEGIN %s(%s",
                                        methodName,
                                        invocation.getArguments().length == 0 ? ")" : ""));

        for (int i = 0; i < invocation.getArguments().length; i++)
        {
            outputManager.add(String.format("   [%d] - %s", i, rootTranslator.translate(invocation.getArguments()[i])));
        }

        if (invocation.getArguments().length != 0)
        {
            outputManager.add(")");
        }

        try
        {
            final T returnValue = getDelegateAnswer().answer(invocation);
            outputManager.add("RETURNED: " + rootTranslator.translate(returnValue));
            return returnValue;
        }
        catch (final Throwable throwable)
        {
            outputManager.add("THROWN: " + rootTranslator.translate(throwable));
            throw throwable;
        }
        finally
        {
            outputManager.add(String.format("END %s", methodName));
        }
    }


    protected Answer<T> createReturnValueAnswer()
    {
        return new ReturnValueAnswer();
    }


    protected Answer<T> createCallRealMethodAnswer()
    {
        return new CallRealMethodAnswer();
    }


    protected class ReturnValueAnswer implements Answer<T>
    {
        @SuppressWarnings("unchecked")
        @Override
        public T answer(final InvocationOnMock invocation) throws Throwable
        {
            return getReturnValue();
        }
    }


    protected class CallRealMethodAnswer implements Answer<T>
    {
        @SuppressWarnings("unchecked")
        @Override
        public T answer(final InvocationOnMock invocation) throws Throwable
        {
            return (T)invocation.callRealMethod();
        }
    }
}
