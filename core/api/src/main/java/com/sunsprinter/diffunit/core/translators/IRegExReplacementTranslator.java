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
 * A special translator that will use regular expressions in the translated form of an object and further modify the
 * string form.  Regular expressions, and the values to use instead of the matched text, must be supplied in the form of
 * {@link com.sunsprinter.diffunit.core.translators.IRegExReplacementPair} instances.<p/>
 *
 * This form of translator is useful if it is not easy to generate a reproducible string form for an object.  For
 * example, if your string form has a date in it, and it's not easy to set the date (you may not have access to do so),
 * you can use this type of translator to snip out the date text from the generated string.<p/>
 *
 * Instances of this translator will process each {@link com.sunsprinter.diffunit.core.translators.IRegExReplacementPair}
 * in the order added.
 *
 * @author Kevan Dunsmore
 * @created 2011/11/14
 */
public interface IRegExReplacementTranslator<T> extends ITranslator<T>
{
    /**
     * Adds another regexp replacement pair to this translator.
     *
     * @param replacementPair The replacement pair to add.  May not be null.
     *
     * @return this translator, to aid chaining of calls.
     */
    IRegExReplacementTranslator<T> add(IRegExReplacementPair... replacementPair);
}
