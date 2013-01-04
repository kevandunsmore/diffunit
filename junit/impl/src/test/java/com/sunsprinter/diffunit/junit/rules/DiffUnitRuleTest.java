package com.sunsprinter.diffunit.junit.rules;


import com.sunsprinter.diffunit.junit.runners.model.DiffUnitStatement;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import org.mockito.Mockito;

import java.lang.reflect.Method;

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
        Assert.assertSame(this, _rule.getTest());

        final Object fakeTest = new Object();
        _rule.setTest(fakeTest);
        Assert.assertSame(fakeTest, _rule.getTest());
    }


    @Test
    public void testApply() throws Exception
    {
        final Statement baseStatement = mock(Statement.class);
        final Description description = mock(Description.class);

        final DiffUnitStatement statement = (DiffUnitStatement)_rule.apply(baseStatement, description);

        final Method getTestMethod = statement.getClass().getDeclaredMethod("getTest");
        getTestMethod.setAccessible(true);
        Assert.assertSame(this, getPropertyValue(statement, "getTest"));
        Assert.assertSame(baseStatement, getPropertyValue(statement, "getInnerStatement"));
        Assert.assertSame(description, getPropertyValue(statement, "getDescription"));
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
}
