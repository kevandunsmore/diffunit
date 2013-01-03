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


import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.plexus.util.xml.pull.MXParser;
import org.codehaus.plexus.util.xml.pull.XmlPullParser;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;


/**
 * ToPrettyXmlTranslator
 *
 * @author Kevan Dunsmore
 * @created 2011/11/13
 */
public class ToPrettyXmlTranslator<T> extends ToXmlTranslator<T>
{
    @Override
    protected String doTranslate(final T object) throws TranslationException
    {
        final String uglyXml = super.doTranslate(object);
        try
        {
            return createPrettyPrinter().format(uglyXml);
        }
        catch (final Exception e)
        {
            throw new TranslationException(
                    object,
                    String.format("Could not reformat XML generated for object '%s'.  Non-formatted XML: %s",
                                  getTestingContext().getInstanceTracker().getObjectId(object),
                                  uglyXml),
                    e);
        }
    }


    protected XmlPrettyPrinter createPrettyPrinter()
    {
        return new XmlPrettyPrinter();
    }


    /**
     * This class forms the pretty printer that does the output formatting work.
     */
    protected static class XmlPrettyPrinter
    {
        protected void format(final String xmlDocument,
                              final PrintWriter writer) throws IOException, XmlPullParserException
        {
            format(new ByteArrayInputStream(xmlDocument.getBytes()), writer);
        }


        protected String format(final String xmlDocument) throws IOException, XmlPullParserException
        {
            final StringWriter writer = new StringWriter();
            format(xmlDocument, new PrintWriter(writer));

            return writer.toString();
        }


        protected void format(final InputStream inputStream,
                              final PrintWriter writer) throws IOException, XmlPullParserException
        {
            final PrettyPrintingParser parser = new PrettyPrintingParser(writer);
            parser.parse(inputStream);
        }


        /**
         * This class forms the parser used by the pretty printer.
         */
        protected class PrettyPrintingParser
        {
            /**
             * Indicates that the previous type of element received was a startElement.
             */
            private static final int START = 1;

            /**
             * Indicates that the previous type of element received was an endElement.
             */
            private static final int END = 2;

            /**
             * Indicates that the previous type of element received was the body (text) of an XML tag.
             */
            private static final int BODY = 3;

            /**
             * The number of spaces to include for each indentation level.
             */
            private static final int INDENT_SIZE = 4;

            /**
             * The output stream that this handler is writing to.
             */
            private final PrintWriter _writer;

            /**
             * A pending start tag that has not yet been written out.
             */
            private String _pendingStartLine;

            /**
             * The name of the current tag.
             */
            private String _currentTag;

            /**
             * The current indentation level of the printer.
             */
            private int _indent;

            /**
             * The previous type of XML element that was received.
             */
            private int _previousType;

            /**
             * New parser.
             */
            private final XmlPullParser _parser = new MXParser();

            /**
             * The current text buffer.
             */
            private StringBuilder _textBuffer;


            /**
             * Creates a SAXHandler that will output an XML string to the given OutputStream.
             *
             * @param writer The writer to write the pretty XML to.
             */
            private PrettyPrintingParser(final PrintWriter writer)
            {
                _writer = writer;
            }


            public void parse(final InputStream input) throws XmlPullParserException, IOException
            {
                _parser.setInput(input, null);

                int eventType = _parser.getEventType();
                do
                {
                    switch (eventType)
                    {
                        case XmlPullParser.START_DOCUMENT:
                            _textBuffer = new StringBuilder();
                            break;

                        case XmlPullParser.END_DOCUMENT:
                            // Do nothing here
                            break;

                        case XmlPullParser.START_TAG:
                            startElement(_parser.getName());
                            break;

                        case XmlPullParser.END_TAG:
                            endElement(_parser.getName());
                            _textBuffer = new StringBuilder();
                            break;

                        case XmlPullParser.TEXT:
                            int holderForStartAndLength[] = new int[2];
                            char ch[] = _parser.getTextCharacters(holderForStartAndLength);
                            int start = holderForStartAndLength[0];
                            int length = holderForStartAndLength[1];
                            characters(ch, start, length);
                            break;
                    }

                    eventType = _parser.next();
                }
                while (eventType != XmlPullParser.END_DOCUMENT);
            }


            private void startElement(final String name) throws XmlPullParserException
            {
                if (_textBuffer.length() != 0)
                {
                    _textBuffer = new StringBuilder();
                }

                _currentTag = name;
                _previousType = START;

                // If we had not yet output the previous start line, do so now
                if (_pendingStartLine != null)
                {
                    _writer.println(_pendingStartLine);
                }

                // Create the tag
                final StringBuilder builder = new StringBuilder();
                builder.append(StringUtils.repeat(" ", _indent * INDENT_SIZE));
                builder.append('<');
                builder.append(name);

                final SortedMap<String, String> sortedAttributeMap = new TreeMap<String, String>();
                if (_parser.getAttributeCount() > 0)
                {
                    final StringBuilder attributeBuilder = new StringBuilder(" ");

                    for (int i = 0; i < _parser.getAttributeCount(); i++)
                    {
                        sortedAttributeMap.put(_parser.getAttributeName(i), _parser.getAttributeValue(i));
                    }
                    for (final String attributeName : sortedAttributeMap.keySet())
                    {
                        attributeBuilder.append(attributeName).append("=\"").append(sortedAttributeMap.get(attributeName)).append("\" ");
                    }

                    builder.append(attributeBuilder.substring(0, attributeBuilder.length() - 1));
                }

                builder.append('>');

                // Don't output the start tag yet because we don't know what's coming
                // next yet.
                _pendingStartLine = builder.toString();

                _indent++;
            }


            private void endElement(final String name) throws XmlPullParserException
            {
                _indent--;

                if (_previousType == BODY)
                {
                    // If the previous element was BODY, then we haven't output
                    // the the start tag yet, so output the whole thing
                    _writer.println(_pendingStartLine + "</" + name + ">");
                }
                else if (_previousType == START)
                {
                    // If we just had a start tag, then this tag immediately ends it
                    // making it an empty tag, so just output an empty version of it.
                    String line = _pendingStartLine.substring(0, _pendingStartLine.length() - 1) + "/>";
                    _writer.println(line);
                }
                else if (_previousType == END)
                {
                    // The previous tag was and END tag so we just output this END
                    // tag normally.
                    _writer.println(StringUtils.repeat(" ", _indent * INDENT_SIZE) + "</" + name + ">");
                }

                // We've taken care of any previous output now
                _pendingStartLine = null;
                _currentTag = null;

                _previousType = END;
            }


            private void characters(final char[] chars, final int start, final int length)
            {
                // We ignore anything that's not the first thing in a tag (i.e. text
                // that lives after other sub-tags).
                if (_currentTag == null)
                {
                    return;
                }

                int len = length;
                int st = 0;


                // The code here is copied from @see java.lang.String.trim() and used to avoid creating new Strings just
                // to remove whitespace.
                while ((st < len) && (chars[start + st] <= ' '))
                {
                    st++;
                }
                while ((st < len) && (chars[start + len - 1] <= ' '))
                {
                    len--;
                }

                // Parser unescapes special characters when it returns text. Since the text is put back into XML it is
                // needed to apply escaping to make sure the resulting XML is well formed.
                _textBuffer.append(StringEscapeUtils.escapeXml(new String(chars, start + st, len - st)));

                // We still don't know how this is going to end
                _pendingStartLine = _pendingStartLine + _textBuffer.toString();

                _previousType = BODY;
            }
        }
    }
}
