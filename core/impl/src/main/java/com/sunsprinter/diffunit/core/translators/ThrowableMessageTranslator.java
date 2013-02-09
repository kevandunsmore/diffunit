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
 * This translator converts a {@link Throwable} into a string by using its {@link Throwable#getMessage()}, optionally
 * adding the message strings of the {@link Throwable#getCause()} (and sub causes), if present.<p/>
 *
 * To use:<p/>
 *
 * <pre>
 * // Bind to all Throwable instances.  Outputs all messages, including cause messages.
 * bind(new ThrowableMessageTranslator(), Throwable.class);
 *
 * // Or, just to a specific type.  Excludes cause messages.
 * bind(new ThrowableMessageTranslator().excludeCause(), MyException.class);
 * </pre><p/>
 *
 * Output looks like this, for a {@link Throwable} with nested causes:<p/>
 *
 * <pre>
 * java.lang.Exception#4(message=outer)
 *     caused by java.lang.Exception#5(message=inner1)
 *         caused by java.lang.Exception#6(message=inner2)
 * </pre><p/>
 *
 * Note the instance tracking number, obtained from {@link #getInstanceTracker()}.
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


    /**
     * Causes the translator to include cause {@link Throwable} messages, if any cause is present on the {@link
     * Throwable} under translation.  Causes are included by default, so this call is not usually necessary.
     *
     * @param <I> The specific type of the translator.
     *
     * @return This translator, for call-chaining purposes.
     */
    @SuppressWarnings("unchecked")
    public <I extends ThrowableMessageTranslator<T>> I includeCause()
    {
        setIncludeCause(true);
        return (I)this;
    }


    /**
     * Causes the translator to exclude cause {@link Throwable} messages, if any cause is present on the {@link
     * Throwable} under translation.
     *
     * @param <I> The specific type of the translator.
     *
     * @return This translator, for call-chaining purposes.
     */
    @SuppressWarnings("unchecked")
    public <I extends ThrowableMessageTranslator<T>> I excludeCause()
    {
        setIncludeCause(false);
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


    /**
     * Translates a single {@link Throwable}.  Subclasses may override this method to customize message output.
     *
     * @param throwable The throwable to be translated.  Will never be null.
     *
     * @return The string form of the throwable parameter.
     */
    protected String translateSingle(final Throwable throwable)
    {
        return String.format("%s(message=%s)", getInstanceTracker().getObjectId(throwable), throwable.getMessage());
    }
}
