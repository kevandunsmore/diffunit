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


import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;


/**
 * ThrowableMessageTranslator
 *
 * @author Kevan Dunsmore
 * @created 2011/11/14
 */
public class ThrowableMessageTranslator<T extends Throwable> extends AbstractTranslator<T>
{
    private boolean _includeCause = true;


    protected boolean getIncludeCause()
    {
        return _includeCause;
    }


    protected void setIncludeCause(final boolean includeCause)
    {
        _includeCause = includeCause;
    }


    @SuppressWarnings("unchecked")
    public <I extends ThrowableMessageTranslator<T>> I includeCause(final boolean include)
    {
        setIncludeCause(include);
        return (I)this;
    }


    @Override
    protected String doTranslate(final T throwable) throws TranslationException
    {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        final PrintWriter pw = new PrintWriter(baos);

        pw.print(translateSingle(throwable));

        if (getIncludeCause())
        {
            String indent = "  ";
            Throwable current = throwable.getCause();
            while (current != null)
            {
                pw.println();
                pw.print(String.format("%scaused by %s", indent, translateSingle(current)));
                indent += indent;
                current = current.getCause();
            }
        }

        pw.flush();
        return baos.toString();
    }


    protected String translateSingle(final Throwable throwable)
    {
        return String.format("%s(message=%s)", getTestingContext().getInstanceTracker().getObjectId(throwable), throwable.getMessage());
    }
}
