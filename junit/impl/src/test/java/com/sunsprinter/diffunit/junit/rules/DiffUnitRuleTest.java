package com.sunsprinter.diffunit.junit.rules;


import com.sunsprinter.diffunit.junit.runners.model.DiffUnitStatement;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;


/**
 * Tests the functionality of the {@link com.sunsprinter.diffunit.junit.rules.DiffUnitRule} class.
 *
 * @author Kevan Dunsmore
 * @created 2013/01/03
 */
public class DiffUnitRuleTest
{
    /**
     * The rule under test.
     */
    private DiffUnitRule _rule;


    @Before
    public void setUp() throws Exception
    {
        _rule = new DiffUnitRule(this);
    }


    @Test
    public void testAccessorsAndMutators() throws Exception
    {
        assertSame(this, _rule.getTest());

        final Object fakeTest = new Object();
        _rule.setTest(fakeTest);
        assertSame(fakeTest, _rule.getTest());

        assertTrue(_rule.getNameValuePairs().isEmpty());
        final Map<String, String> newMap = new HashMap<>();
        _rule.setNameValuePairs(newMap);
        assertSame(newMap, _rule.getNameValuePairs());
    }


    @Test
    public void testApply() throws Exception
    {
        final Statement baseStatement = mock(Statement.class);
        final Description description = mock(Description.class);

        final DiffUnitStatement statement = (DiffUnitStatement)_rule.apply(baseStatement, description);

        final Method getTestMethod = statement.getClass().getDeclaredMethod("getTest");
        getTestMethod.setAccessible(true);
        assertSame(this, getPropertyValue(statement, "getTest"));
        assertSame(baseStatement, getPropertyValue(statement, "getInnerStatement"));
        assertSame(description, getPropertyValue(statement, "getDescription"));
    }


    /**
     * Ugly hack to get the value of a non-accessible property.
     */
    private Object getPropertyValue(final Object object, final String methodName) throws Exception
    {
        final Method method = object.getClass().getDeclaredMethod(methodName);
        method.setAccessible(true);
        return method.invoke(object);
    }


    @Test
    public void testNameValuePairs() throws Exception
    {
        assertTrue(_rule.getNameValuePairs().isEmpty());

        _rule.addNameValuePair("hello", "world")
                .addNameValuePair("my god!", "it's full of stars!")
                .addNameValuePair("man", "ape");

        assertEquals(3, _rule.getNameValuePairs().size());
        assertEquals("world", _rule.getNameValuePairs().get("hello"));
        assertEquals("it's full of stars!", _rule.getNameValuePairs().get("my god!"));
        assertEquals("ape", _rule.getNameValuePairs().get("man"));

        _rule.removeNameValuePair("not there")
                .removeNameValuePair("man");

        assertEquals(2, _rule.getNameValuePairs().size());
        assertEquals("world", _rule.getNameValuePairs().get("hello"));
        assertEquals("it's full of stars!", _rule.getNameValuePairs().get("my god!"));

        _rule.removeNameValuePair("my god!")
                .removeNameValuePair("hello");

        assertTrue(_rule.getNameValuePairs().isEmpty());
    }
}
