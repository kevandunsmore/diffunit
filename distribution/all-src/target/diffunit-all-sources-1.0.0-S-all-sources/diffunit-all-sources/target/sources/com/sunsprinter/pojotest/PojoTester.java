package com.sunsprinter.pojotest;


import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;


/**
 * PojoTester
 *
 * @author Kevan Dunsmore
 * @created 2011/12/07
 */
public class PojoTester
{
    private final Object _pojo;
    private final Collection<PojoTestPath> _testPaths = new LinkedList<PojoTestPath>();
    private boolean _restrictedToPublicProperties;
    private Map<String, Method> _methodCache = new HashMap<String, Method>();


    public PojoTester(final Object pojo) throws IntrospectionException
    {
        _pojo = pojo;
    }


    protected Object getPojo()
    {
        return _pojo;
    }


    protected Collection<PojoTestPath> getTestPaths()
    {
        return _testPaths;
    }


    public PojoTester restrictToPublicProperties()
    {
        setRestrictedToPublicProperties(true);
        return this;
    }


    protected boolean isRestrictedToPublicProperties()
    {
        return _restrictedToPublicProperties;
    }


    protected void setRestrictedToPublicProperties(final boolean restrictedToPublicProperties)
    {
        _restrictedToPublicProperties = restrictedToPublicProperties;
    }


    public PojoTester addProperty(final String propertyName,
                                  final Object initialValue,
                                  final Object... values)
    {
        return addPropertyCustomMethods(propertyName, null, null, initialValue, values);
    }


    public PojoTester addPropertyCustomReadMethod(final String propertyName,
                                                  final String readMethodName,
                                                  final Object initialValue,
                                                  final Object... values)
    {
        return addPropertyCustomMethods(propertyName, readMethodName, null, initialValue, values);
    }


    public PojoTester addPropertyCustomWriteMethod(final String propertyName,
                                                   final String writeMethodName,
                                                   final Object initialValue,
                                                   final Object... values)
    {
        return addPropertyCustomMethods(propertyName, null, writeMethodName, initialValue, values);
    }


    public PojoTester addPropertyCustomMethods(final String propertyName,
                                               final String readMethodName,
                                               final String writeMethodName,
                                               final Object initialValue,
                                               final Object[] values)
    {
        getTestPaths().add(createTestPath(propertyName,
                                          readMethodName == null ? "get" + propertyName : readMethodName,
                                          writeMethodName == null ? "set" + propertyName : writeMethodName,
                                          initialValue, values));
        return this;
    }


    public void test() throws Exception
    {
        for (final PojoTestPath testPath : getTestPaths())
        {
            testPath.test();
        }
    }


    protected PojoTestPath createTestPath(final String propertyName,
                                          final String readMethodName,
                                          final String writeMethodName,
                                          final Object initialValue,
                                          final Object[] values)
    {
        return new PojoTestPath(propertyName, readMethodName, writeMethodName, initialValue, values);
    }


    protected Map<String, Method> getMethodCache()
    {
        return _methodCache;
    }


    protected Method getMethod(final String methodName) throws Exception
    {
        Method method = getMethodCache().get(methodName);
        if (method == null)
        {
            method = getMethod(getPojo().getClass(), methodName);
            if (method == null)
            {
                throw new PojoTestException(String.format("Cannot find method of name '%s' in hierarchy of POJO class '%s'.",
                                                          methodName,
                                                          getPojo().getClass().getName()));
            }

            getMethodCache().put(methodName, method);
        }

        return method;
    }


    protected Method getMethod(final Class<?> pojoClass, final String methodName)
    {
        if (pojoClass == Object.class)
        {
            return null;
        }

        for (final Method method : pojoClass.getDeclaredMethods())
        {
            if (method.getName().equals(methodName))
            {
                return method;
            }
        }

        return getMethod(pojoClass.getSuperclass(), methodName);
    }


    protected class PojoTestPath
    {
        private final String _propertyName;
        private final Object _initialValue;
        private final Object[] _values;
        private final String _readMethodName;
        private final String _writeMethodName;


        public PojoTestPath(final String propertyName, final String readMethodName,
                            final String writeMethodName, final Object initialValue, final Object[] values)
        {
            _propertyName = propertyName;
            _readMethodName = readMethodName;
            _writeMethodName = writeMethodName;
            _initialValue = initialValue;
            _values = values;
        }

        @SuppressWarnings("unused")
        private void nullWriteMethod(final Object value)
        {
        }

        public void test() throws Exception
        {
            final Method readMethod = getMethod(getReadMethodName());

            // If we have no values to write then we must have a read-only property.  In that case we do not have a
            // write method to call so to prevent lots of null checks we point our write method at a null implementation
            // on this class.
            final Method writeMethod = getValues() == null ? getClass().getMethod("nullWriteMethod", Object.class) : getMethod(getWriteMethodName());

            final boolean readMethodAccessible = readMethod.isAccessible();
            final boolean writeMethodAccessible = writeMethod.isAccessible();

            if (!isRestrictedToPublicProperties())
            {
                readMethod.setAccessible(true);
                writeMethod.setAccessible(true);
            }

            try
            {
                assertValue(readMethod, getInitialValue());

                for (final Object newValue : getValues())
                {
                    getMethod(getWriteMethodName()).invoke(getPojo(), newValue);
                    assertValue(readMethod, newValue);
                }
            }
            finally
            {
                if (!isRestrictedToPublicProperties())
                {
                    readMethod.setAccessible(readMethodAccessible);
                    writeMethod.setAccessible(writeMethodAccessible);
                }
            }
        }


        protected void assertValue(final Method readMethod,
                                   final Object expectedValue) throws Exception
        {
            Object actualValue = readMethod.invoke(getPojo());

            // TODO: abstract the assertion for different testing frameworks.  This is ugly because it'll fail with
            // TODO: an error, not a standard test failure.
            if (expectedValue != actualValue ||
                (expectedValue != null && !expectedValue.equals(actualValue)))
            {
                throw new PojoTestException(String.format("Property '%s' of class %s did not have expected value of '%s'.  Actual value was '%s'.",
                                                          getPropertyName(),
                                                          getPojo().getClass().getName(),
                                                          expectedValue,
                                                          actualValue));
            }
        }


        protected Object getInitialValue()
        {
            return _initialValue;
        }


        protected String getPropertyName()
        {
            return _propertyName;
        }


        protected Object[] getValues()
        {
            return _values;
        }


        protected String getReadMethodName()
        {
            return _readMethodName;
        }


        protected String getWriteMethodName()
        {
            return _writeMethodName;
        }
    }
}
