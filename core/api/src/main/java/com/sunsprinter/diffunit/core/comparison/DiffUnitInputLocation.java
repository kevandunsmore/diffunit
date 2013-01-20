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

package com.sunsprinter.diffunit.core.comparison;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * Instructs DiffUnit on where to find known-good versions of files.  Known-good versions of files may be held on the
 * local file system or on the classpath.<p/>
 *
 * Here are some examples.  We assume that the test class is called {@code MyTest} and the method being executed is
 * called {@code testSomething}.<p/>
 *
 * <pre>
 * // Input files to be found on the classpath under location /MyTest/testSomething
 * {@code @DiffUnitInputLocation}
 *
 * // Input files to be found on the classpath under location /myLocation
 * {@code @DiffUnitInputLocation(location = "/myLocation"}
 * {@code @DiffUnitInputLocation(ocationType = InputLocationType.CLASSPATH, location = "/myLocation"}
 *
 * // Input files to be found on the file system under location /MyTest/testSomething
 * {@code @DiffUnitInputLocation(locationType = InputLocationType.FILE_SYSTEM}
 *
 * // Input files to be found on the file system under location /wibble/giblets
 * {@code @DiffUnitInputLocation(locationType = InputLocationType.FILE_SYSTEM, location = "/wibble/giblets"}
 * </pre><p/>
 *
 * You can also use some replacement values.  Actually, anything in the name/value pair collection held by the testing
 * context ({@link com.sunsprinter.diffunit.core.context.ITestingContext#getNameValuePairs()} can be used to substitute
 * runtime values.  For example:
 *
 * <pre>
 * // Input files to be found on the classpath under location /MyTest/testSomething
 * {@code @DiffUnitInputLocation(location = "/{TestClassName}/{TestName}"}
 * </pre><p/>
 *
 * If you add values to the name/value pair map, you can substitute those too:<p/>
 *
 * <pre>
 * {@code @DiffUnitInputLocation(location = "/{MyCustomThing}/{AnotherCustomThing}"}
 * public void testSomething()
 * {
 *     getTestingContext().getNameValuePairs().put("MyCustomThing", "SomethingCustom");
 *     getTestingContext().getNameValuePairs().put("AnotherCustomThing", calculateAnotherCustomThing());
 * }
 * </pre>
 *
 * @author Kevan Dunsmore
 * @created 2011/11/13
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface DiffUnitInputLocation
{
    /**
     * The type of location to seek input files.  Default is {@link com.sunsprinter.diffunit.core.comparison.InputLocationType#CLASSPATH}.
     */
    InputLocationType locationType() default InputLocationType.CLASSPATH;

    /**
     * The location of the input files.  Default is /TestClassSimpleName/TestMethodName.
     */
    String location();
}
