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
 * A type-to-translator binder is responsible for allocating a specific {@link com.sunsprinter.diffunit.core.translators.ITranslator}
 * instance to specific {@link Class} types.  DiffUnit will use the specified translator to convert instances of those
 * types to string form.  For example:<p/>
 *
 * <pre>
 * MyTranslator translator = new MyTranslator();
 * binder.bind(translator, Foo.class, Bar.class, Giblets.class);
 *
 * AnotherTranslator anotherTranslator = new AnotherTranslator();
 * binder.bind(anotherTranslator, Wibble.class, Baz.class);
 * </pre><p/>
 *
 * In the code above, DiffUnit will use the instance of <code>MyTranslator</code> to convert instances of
 * <code>Foo</code>, <code>Bar</code> and <code>Giblets</code> to strings.  It will use the instance of
 * <code>AnotherTranslator</code> to convert instances of <code>Wibble</code> and <code>Baz</code>.<p/>
 *
 * In the event that you have an object hierarchy like this:<p/>
 *
 * <pre>
 * class Bar
 * {
 *     // Bar implementation
 * }
 *
 * class Baz extends Bar
 * {
 *     // Baz implementation
 * }
 * </pre><p/>
 *
 * Then DiffUnit will select the most specific type.  In the example here, DiffUnit will select the instance of
 * <code>AnotherTranslator</code> to perform the translation of instances of <code>Baz</code> and
 * <code>MyTranslator</code> to do instances of <code>Bar</code>.
 *
 * @author Kevan Dunsmore
 * @created 2011/11/12
 */
public interface ITypeToTranslatorBinder
{
    /**
     * Binds the supplied translator to the supplied class types for translation.
     *
     * @param translator The translator to use.  May not be null.
     * @param types      The types to bind against the supplied translator.
     */
    void bind(final ITranslator<?> translator, final Class<?>... types);
}
