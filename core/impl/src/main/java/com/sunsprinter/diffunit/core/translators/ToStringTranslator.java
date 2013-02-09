/*
 * Copyright 2011 Kevan Dunsmore.  All rights reserved.
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
 * The most basic translator.  Converts objects to strings by calling {@link Object#toString()} on each one.  Works well
 * if you have sensible, repeatable string output from the target object.  You must resist the temptation to implement
 * the {@link #toString()} on your class simply to make the tests, and this translator, work.  Instead implement a
 * custom translator and limit your conversion to the testing space.
 *
 * @author Kevan Dunsmore
 * @created 2011/11/10
 */
public class ToStringTranslator<T> extends AbstractTranslator<T>
{
    @Override
    protected String doTranslate(final T object) throws TranslationException
    {
        return object.toString();
    }
}
