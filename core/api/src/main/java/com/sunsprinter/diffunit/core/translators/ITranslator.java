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
 * The basic translator contract.  A translator knows how to turn an object into a reproducible string form suitable for
 * comparison with a known good version.
 *
 * @author Kevan Dunsmore
 * @created 2011/11/10
 */
public interface ITranslator<T>
{
    /**
     * Translates the supplied object into string form.
     *
     * @param object The object to be translated.
     *
     * @return The string form of the object.
     * @throws TranslationException if an error occurs during translation.
     */
    String translate(T object) throws TranslationException;
}
