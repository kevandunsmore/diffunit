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

package com.sunsprinter.diffunit.core.translators;


/**
 * Exception thrown when there's a problem translating an object to string form.
 *
 * @author Kevan Dunsmore
 * @created 2011/11/10
 */
public class TranslationException extends RuntimeException
{
    /**
     * The object being translated that failed the process.
     */
    private final Object _failedObject;


    /**
     * Constructs a new exception.
     *
     * @param failedObject The object that failed the translation process.  May not be null.
     * @param message      A descriptive message.  May not be null.
     */
    public TranslationException(final Object failedObject, final String message)
    {
        super(message);
        _failedObject = failedObject;
    }


    /**
     * Constructs a new exception.
     *
     * @param failedObject The object that failed the translation process.  May not be null.
     * @param message      A descriptive message.  May not be null.
     * @param cause        The underlying cause of this exception.
     */
    public TranslationException(final Object failedObject, final String message, final Throwable cause)
    {
        super(message, cause);
        _failedObject = failedObject;
    }


    /**
     * Returns the object that caused the translation process to fail.
     *
     * @return the object that caused the translation process to fail.  Will never be null.
     */
    public Object getFailedObject()
    {
        return _failedObject;
    }
}
