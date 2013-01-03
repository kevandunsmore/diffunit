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

package com.sunsprinter.diffunit.core.instancetracking;


import junit.framework.Assert;
import org.junit.Test;


/**
 * Tests the functionality of the {@link com.sunsprinter.diffunit.core.instancetracking.ObjectIdentifier} class.
 *
 * @author Kevan Dunsmore
 * @created 2013/01/02
 */
public class ObjectIdentifierTest
{
    @Test
    public void testAccessorsAndMutators()
    {
        // Test using default constructor.
        ObjectIdentifier oid = new ObjectIdentifier();
        Assert.assertNull(oid.getObject());
        Assert.assertEquals(0, oid.getInstanceNumber());

        oid.setObject(this);
        Assert.assertSame(this, oid.getObject());

        oid.setInstanceNumber(4517734);
        Assert.assertEquals(4517734, oid.getInstanceNumber());

        // Test again with parameterized constructor.
        final Object trackedObject = new Object();
        oid = new ObjectIdentifier(trackedObject, 80085);
        Assert.assertSame(trackedObject, oid.getObject());
        Assert.assertEquals(80085, oid.getInstanceNumber());

        oid.setObject(this);
        Assert.assertSame(this, oid.getObject());

        oid.setInstanceNumber(4517734);
        Assert.assertEquals(4517734, oid.getInstanceNumber());
    }


    @Test
    public void testToString()
    {
        ObjectIdentifier oid = new ObjectIdentifier(this, 4517734);
        Assert.assertEquals(String.format("%s#4517734", getClass().getName()), oid.toString());
    }


    @Test
    public void testHashCode()
    {
        ObjectIdentifier oid = new ObjectIdentifier(this, 2);
        Assert.assertEquals(System.identityHashCode(this), oid.hashCode());
    }


    @SuppressWarnings("EqualsBetweenInconvertibleTypes")
    @Test
    public void testEquals()
    {
        ObjectIdentifier oid1 = new ObjectIdentifier(this, 2);
        ObjectIdentifier oid2 = new ObjectIdentifier(this, 2);
        Assert.assertTrue(oid1.equals(oid2));
        Assert.assertTrue(oid2.equals(oid1));

        ObjectIdentifier oid3 = new ObjectIdentifier(new Object(), 2);
        Assert.assertFalse(oid3.equals(oid1));
        Assert.assertFalse(oid3.equals(oid2));

        Assert.assertFalse(oid1.equals(new Object()));
        Assert.assertFalse(oid2.equals(this));
        Assert.assertFalse(oid3.equals("something else"));
    }
}
