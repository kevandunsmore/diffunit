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
 * Defines a regular expression and the translator used to generate the replacement value for each match of the regular
 * expression. The specification of a translator, instead of a specific value, allows further processing to take place
 * on the matched string.  In most cases this isn't necessary - all you need to do is to convert it to a consistent
 * value
 *
 * @author Kevan Dunsmore
 * @created 2011/11/14
 */
public interface IRegExReplacementPair
{
    /**
     * The regular expression to use.
     */
    String getRegExp();

    /**
     * The translator to use to convert each matched value to a string.
     */
    ITranslator<String> getTranslator();
}
