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

package com.sunsprinter.diffunit.core.injection;


import java.lang.reflect.Field;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;


/**
 * A simple injection implementation that sets values on any class with fields declared with the {@link
 * com.sunsprinter.diffunit.core.injection.DiffUnitInject} annotation.<p/>
 *
 * This injector sets value on the target object (usually the test object itself but you can use any object if you want)
 * by looking up the appropriate key in its injection map.  If the {@link com.sunsprinter.diffunit.core.injection.DiffUnitInject}
 * annotation has an {@code objectId} value then it will be used as the key in the map lookup operation.  If the
 * annotation has no {@code objectId} value then the target field's type is used as the key in the lookup.  This means
 * that you must have a strict type match; this injector doesn't do anything fancy like hierarchy walking or best type
 * matching.<p/>
 *
 * If you try to inject something that's not known to this injector, it will throw an exception during the {@link
 * #inject(Object)} operation.
 *
 * @author Kevan Dunsmore
 * @created 2011/11/13
 */
public class Injector implements IInjector
{
    /**
     * The map of things to be injected.
     */
    private Map<Object, Object> _injectionMap;


    public Map<Object, Object> getInjectionMap()
    {
        return _injectionMap;
    }


    public void setInjectionMap(final Map<Object, Object> injectionMap)
    {
        _injectionMap = injectionMap;
    }


    public void inject(final Object test) throws DiffUnitInjectionException
    {
        inject(test.getClass(), test.getClass(), test);
    }


    protected void inject(final Class<?> currentClass,
                          final Class<?> testClass,
                          final Object test) throws DiffUnitInjectionException
    {
        if (currentClass != Object.class)
        {
            for (final Field field : currentClass.getDeclaredFields())
            {
                final DiffUnitInject annotation = field.getAnnotation(DiffUnitInject.class);
                if (annotation != null)
                {
                    final Object key = StringUtils.isEmpty(annotation.objectId()) ? field.getType() : annotation.objectId();
                    final Object value = getInjectionMap().get(key);
                    if (value == null)
                    {
                        throw new DiffUnitInjectionException(
                                String.format("DiffUnit unable to inject field '%s' of class '%s' on test of class '%s'.  " +
                                                      "Component key is '%s'.  Target field type is '%s'.  No object " +
                                                      "with type or key '%s' held in injection map.",
                                              field.getName(),
                                              currentClass.getName(),
                                              testClass.getName(),
                                              key,
                                              field.getType().getName(),
                                              key));
                    }

                    final boolean fieldAccessible = field.isAccessible();
                    try
                    {
                        field.setAccessible(true);
                        field.set(test, getInjectionMap().get(key));
                    }
                    catch (final Exception e)
                    {
                        throw new DiffUnitInjectionException(
                                String.format("DiffUnit unable to inject field '%s' of class '%s' on test of class '%s'.  " +
                                                      "Component key is '%s'.  Target field type is '%s'.  Value is " +
                                                      "of type '%s'.  Value is '%s'.",
                                              field.getName(),
                                              currentClass.getName(),
                                              testClass.getName(),
                                              key,
                                              field.getType().getName(),
                                              value.getClass().getName(),
                                              value.toString()),
                                e);
                    }
                    finally
                    {
                        field.setAccessible(fieldAccessible);
                    }
                }
            }

            inject(currentClass.getSuperclass(), testClass, test);
        }
    }
}
