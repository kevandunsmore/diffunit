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


/**
 * This class forms a holder for the singleton instance of {@link com.sunsprinter.diffunit.core.context.ITestingContext}
 * used in the execution of a test.  If you don't use the injection system to get a hold of the context, use this class
 * and its static member.
 *
 * @author Kevan Dunsmore
 * @created 2011/11/11
 */
public class TestingContextHolder
{
    /**
     * The testing context being used by the currently executing test.  This field is read-write but you should only
     * write to ths field if you want bad things to happen and you like pain and suffering.
     */
    public static ITestingContext CONTEXT;
}
