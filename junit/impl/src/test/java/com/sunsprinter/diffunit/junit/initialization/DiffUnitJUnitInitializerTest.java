package com.sunsprinter.diffunit.junit.initialization;


import com.sunsprinter.diffunit.core.context.TestingContext;
import com.sunsprinter.diffunit.junit.comparison.JUnitFileComparer;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Method;


/**
 * Tests the functionality of the {@link com.sunsprinter.diffunit.junit.initialization.DiffUnitJUnitInitializer} class.
 *
 * @author Kevan Dunsmore
 * @created 2013/01/03
 */
public class DiffUnitJUnitInitializerTest
{
    /**
     * The initializer under test.
     */
    private DiffUnitJUnitInitializer _initializer;

    /**
     * Context used as part of this test.
     */
    private TestingContext _testingContext;


    @Before
    public void setUp() throws Exception
    {
        _initializer = new DiffUnitJUnitInitializer();

        _testingContext = new TestingContext();
        _initializer.use(_testingContext);
    }


    @Test
    public void testCreateFileComparer() throws Exception
    {
        final JUnitFileComparer fileComparer = _initializer.createFileComparer();
        Assert.assertNotNull(fileComparer);

        // Ugly but gets the job done.
        final Method getTestingContextMethod = fileComparer.getClass().getSuperclass().getDeclaredMethod("getTestingContext");
        getTestingContextMethod.setAccessible(true);
        Assert.assertSame(_testingContext, getTestingContextMethod.invoke(fileComparer));
    }
}
