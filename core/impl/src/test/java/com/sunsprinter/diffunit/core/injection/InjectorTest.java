/*
 * Copyright 2013 Kevan Dunsmore.  All rights reserved.
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


import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.fail;


/**
 * Tests the functionality of the {@link Injector} class.
 *
 * @author Kevan Dunsmore
 * @created 2013/01/20
 */
public class InjectorTest
{
    /**
     * Test target field for injector.  This will be injected by object id.  Package-protected to allow access via
     * subclass in test.
     */
    @DiffUnitInject(objectId = "idTarget")
    String _idTarget;

    /**
     * Test target field for injector.  This will be injected by type.  Package-protected to allow access via subclass
     * in test.
     */
    @DiffUnitInject
    HashMap _typeTarget;

    /**
     * The injector under test.
     */
    private Injector _injector;

    /**
     * A map holding the values to be injected.
     */
    private Map<Object, Object> _injectionMap;


    @Before
    public void setUp() throws Exception
    {
        _injector = new Injector();

        _injectionMap = new HashMap<>();
        _injectionMap.put("idTarget", "something-with-an-id");
        _injectionMap.put("anotherIdTarget", "another-thing-with-an-id");
        _injectionMap.put(HashMap.class, new HashMap());
        _injectionMap.put(List.class, new ArrayList());

        _injector.setInjectionMap(new HashMap<>(_injectionMap));
    }


    @Test
    public void testInjection() throws Exception
    {
        // Test this class as target.
        _injector.inject(this);

        assertSame(_injectionMap.get("idTarget"), _idTarget);
        assertSame(_injectionMap.get(HashMap.class), _typeTarget);

        // Same thing, this time with a hierarchy with annotated fields.
        final SubClass target = new SubClass();
        _injector.inject(target);

        assertSame(_injectionMap.get("idTarget"), target._idTarget);
        assertSame(_injectionMap.get(HashMap.class), target._typeTarget);
        assertSame(_injectionMap.get("anotherIdTarget"), target._anotherIdTarget);
        assertSame(_injectionMap.get(List.class), target._anotherTypeTarget);
    }


    @Test
    public void testInjectionFailsWhenObjectIdNotKnown()
    {
        try
        {
            _injector.inject(new ClassWithUnknownId());
            fail("Expected exception to be thrown.");
        }
        catch (final DiffUnitInjectionException e)
        {
            assertEquals(
                    "DiffUnit unable to inject field '_idTarget' of class " +
                            "'com.sunsprinter.diffunit.core.injection.InjectorTest$ClassWithUnknownId' on test " +
                            "of class 'com.sunsprinter.diffunit.core.injection.InjectorTest$ClassWithUnknownId'.  " +
                            "Component key is 'unknownid'.  Target field type is 'java.lang.String'.  No object with " +
                            "type or key 'unknownid' held in injection map.",
                    e.getMessage());
        }

        try
        {
            _injector.inject(new ClassWithUnknownType());
            fail("Expected exception to be thrown.");
        }
        catch (final DiffUnitInjectionException e)
        {
            assertEquals(
                    "DiffUnit unable to inject field '_doubleTarget' of class " +
                            "'com.sunsprinter.diffunit.core.injection.InjectorTest$ClassWithUnknownType' on test of " +
                            "class 'com.sunsprinter.diffunit.core.injection.InjectorTest$ClassWithUnknownType'.  " +
                            "Component key is 'class java.lang.Double'.  Target field type is 'java.lang.Double'.  " +
                            "No object with type or key 'class java.lang.Double' held in injection map.",
                    e.getMessage());
        }
    }


    @Test
    public void testInjectionFailsWithTypeMismatch() throws Exception
    {
        _injector.getInjectionMap().put("double", 1.1);

        try
        {
            _injector.inject(new ClassWithTypeMismatch());
            fail("Expected exception to be thrown.");
        }
        catch (final DiffUnitInjectionException e)
        {
            assertEquals(
                    "DiffUnit unable to inject field '_stringTarget' of class " +
                            "'com.sunsprinter.diffunit.core.injection.InjectorTest$ClassWithTypeMismatch' on test " +
                            "of class 'com.sunsprinter.diffunit.core.injection.InjectorTest$ClassWithTypeMismatch'.  " +
                            "Component key is 'double'.  Target field type is 'java.lang.String'.  Value is of " +
                            "type 'java.lang.Double'.  Value is '1.1'.",
                    e.getMessage());
        }
    }


    /**
     * Subclass with injection targets, used to test ability of injector to walk up the hierarchy.
     */
    private class SubClass extends InjectorTest
    {
        /**
         * Test target field for injector.  This will be injected by object id.
         */
        @DiffUnitInject(objectId = "anotherIdTarget")
        private String _anotherIdTarget;

        /**
         * Test target field for injector.  This will be injected by type.
         */
        @DiffUnitInject
        private List _anotherTypeTarget;
    }


    /**
     * Test injection target with a field not known to the injector.
     */
    private class ClassWithUnknownId
    {
        /**
         * Test target field for injector.  The id is not known to the injector so this should cause an error.
         */
        @DiffUnitInject(objectId = "unknownid")
        private String _idTarget;
    }


    /**
     * Test injection target with a field not known to the injector.
     */
    private class ClassWithUnknownType
    {
        /**
         * Test target field for injector.  The id is not known to the injector so this should cause an error.
         */
        @DiffUnitInject
        private Double _doubleTarget;
    }


    /**
     * Test injection target with deliberate type mismatch between target field and value in injection map.
     */
    private class ClassWithTypeMismatch
    {
        @DiffUnitInject(objectId = "double")
        private String _stringTarget;
    }
}
