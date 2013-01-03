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
 * RegExReplacementPair
 *
 * @author Kevan Dunsmore
 * @created 2011/11/15
 */
public class RegExReplacementPair implements IRegExReplacementPair
{
    private String _regEx;
    private ITranslator<String> _translator;


    public RegExReplacementPair(final String regEx)
    {
        this(regEx, "");
    }


    public RegExReplacementPair(final String regEx, final String replacementValue)
    {
        this(regEx, new ITranslator<String>()
        {
            @Override
            public String translate(final String object) throws TranslationException
            {
                return replacementValue;
            }
        });
    }


    public RegExReplacementPair(final String regEx, final ITranslator<String> translator)
    {
        _regEx = regEx;
        _translator = translator;
    }


    protected String getRegEx()
    {
        return _regEx;
    }


    protected void setRegEx(final String regEx)
    {
        _regEx = regEx;
    }


    @Override
    public String getRegExp()
    {
        return _regEx;
    }


    protected void setTranslator(final ITranslator<String> translator)
    {
        _translator = translator;
    }


    @Override
    public ITranslator<String> getTranslator()
    {
        return _translator;
    }
}
