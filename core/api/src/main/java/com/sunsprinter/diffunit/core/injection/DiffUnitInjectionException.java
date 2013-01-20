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

package com.sunsprinter.diffunit.core.injection;


/**
 * Thrown when there's a problem injecting values into a test class.
 *
 * @author Kevan Dunsmore
 * @created 2011/11/13
 */
public class DiffUnitInjectionException extends Exception
{
    public DiffUnitInjectionException(final String message)
    {
        super(message);
    }


    public DiffUnitInjectionException(final String message, final Throwable cause)
    {
        super(message, cause);
    }
}
