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

package com.sunsprinter.diffunit.core.output;


/**
 * The output manager is responsible for collecting objects to be written to one or more files.  It can also perform
 * some formatting on the output it generates.<p/>
 *
 * <pre>
 * IOutputManager om = // get reference to output manager
 *
 * om.addBlankLine();
 *
 * // Adds a new instance of Foo.  Upon file writing, the instance will be translated to a string by the translator
 * // registered against the Foo type.
 * om.add(new Foo());
 * </pre><p/>
 *
 * Sometimes an object will change state during the course of a test.  For example, suppose you have a class like
 * this:<p/>
 *
 * <pre>
 * public class Container
 * {
 *     private Collection<String> _allText = new LinkedList<>();
 *
 *     public void addText(String text)
 *     {
 *         _allText.add(text);
 *     }
 *
 *     public Collection<String> getAllText()
 *     {
 *         return Collections.unmodifiableCollection(_allText));
 *     }
 * }
 * </pre><p/>
 *
 * Then it's natural to write a test something like this:
 *
 * <pre>
 * public void testAdd()
 * {
 *     IOutputManager om = // get reference to output manager
 *
 *     om.add("Empty State");
 *     om.add(_container);
 *
 *     om.addBlankLine();
 *     om.add("Single value");
 *     _container.addText("hello world");
 *     om.add(_container);
 *
 *     om.addBlankLine();
 *     om.add("Multiple values");
 *     _container.addText("wibble");
 *     _container.addText("giblets");
 *     om.add(_container);
 * }
 * </pre><p/>
 *
 * If you do this you'll get incorrect results.  The full contents of the container will be written to the output file
 * three times, once under each heading.  This is because translation happens immediately prior to file writing, so
 * DiffUnit ends up translating the same instance multiple times then dumping the lot to the output file.  Not right at
 * all.<p/>
 *
 * To avoid this, use the {@link #addAsString(Object)} method.  This performs immediate translation and so takes a
 * snapshot of object state at the time it is added.  The above test can be rewritten like this:<p/>
 *
 * pre> public void testAdd() { IOutputManager om = // get reference to output manager
 *
 * om.add("Empty State"); om.addAsString(_container);
 *
 * om.addBlankLine(); om.add("Single value"); _container.addText("hello world"); om.addAsString(_container);
 *
 * om.addBlankLine(); om.add("Multiple values"); _container.addText("wibble"); _container.addText("giblets");
 *
 * om.addAsString(_container); } </pre><p/>
 *
 * @author Kevan Dunsmore
 * @created 2011/11/13
 * @see DiffUnitOutputLocation
 */
public interface IOutputManager
{
    /**
     * Formatting method.  Adds a blank line to the collection of objects to be output when the file is written.
     */
    void addBlankLine();

    /**
     * Adds an object to be written to the output file.  You can use this to add your objects to be translated as well
     * as formatting in the way of headings.  Adding a string value will simply cause the string to be output to the
     * generated file.
     *
     * @param object The object to be output.  May be null.
     */
    void add(Object object);

    /**
     * Adds an object to the collection of objects to be written to the output file.  Differs from the {@link
     * #add(Object)} method in that translation is performed immediately rather than immediately prior to writing the
     * file.
     *
     * @param object The object to be translated and added.  May be null.
     */
    void addAsString(Object object);

    /**
     * Writes the output file to the output location, translating objects as necessary.  Once the file is written, the
     * collection of objects to be translated and written will be cleared.  This lets you write multiple files in the
     * same test if you want to. It's not necessary to call this method if you only need a single output file.  DiffUnit
     * will automatically call it with the value {@code results.txt} if there's anything to be written.
     *
     * @param fileName The name of the file to write.  The file will be written to the output location for the test.
     */
    void writeFile(final String fileName);
}
