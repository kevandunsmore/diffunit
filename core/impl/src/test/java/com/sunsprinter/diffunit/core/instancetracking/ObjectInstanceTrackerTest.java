package com.sunsprinter.diffunit.core.instancetracking;


import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;


/**
 * Tests the functionality of the {@link com.sunsprinter.diffunit.core.instancetracking.ObjectInstanceTracker} class.
 *
 * @author Kevan Dunsmore
 * @created 2013/01/02
 */
public class ObjectInstanceTrackerTest
{
    /**
     * The tracker under test.
     */
    private ObjectInstanceTracker _tracker;


    @Before
    public void setUp()
    {
        _tracker = new ObjectInstanceTracker();
        Assert.assertTrue(_tracker.getKnownObjectMap().isEmpty());
        Assert.assertTrue(_tracker.getReferenceCountMap().isEmpty());
    }


    @Test
    public void testTrackObjects() throws Exception
    {
        // Test tracking.
        testTrackFromEmptyState();

        // Test reset.
        _tracker.reset();
        Assert.assertTrue(_tracker.getKnownObjectMap().isEmpty());
        Assert.assertTrue(_tracker.getReferenceCountMap().isEmpty());

        // Test tracking again from an empty state now that we've reset everything.
        testTrackFromEmptyState();
    }


    private void testTrackFromEmptyState()
    {
        // Track some objects.
        for (int i = 0; i < 100; i++)
        {
            final Object object = new Object();
            final IObjectIdentifier objectId = _tracker.getObjectId(object);

            Assert.assertTrue(objectId instanceof ObjectIdentifier);

            Assert.assertEquals(i + 1, objectId.getInstanceNumber());
            Assert.assertSame(object, objectId.getObject());

            Assert.assertEquals(i + 1, _tracker.getKnownObjectMap().size());
            Assert.assertEquals(1, _tracker.getReferenceCountMap().size());

            Assert.assertSame(objectId, _tracker.getKnownObjectMap().get(System.identityHashCode(object)));
        }

        // Now some strings.
        for (char i = 32; i < 65; i++)
        {
            final String str = Character.toString(i);

            final IObjectIdentifier objectId = _tracker.getObjectId(str);

            Assert.assertTrue(objectId instanceof ObjectIdentifier);

            Assert.assertEquals(i - 31, objectId.getInstanceNumber());
            Assert.assertSame(str, objectId.getObject());

            Assert.assertEquals(100 + i - 31, _tracker.getKnownObjectMap().size());
            Assert.assertEquals(2, _tracker.getReferenceCountMap().size());

            Assert.assertSame(objectId, _tracker.getKnownObjectMap().get(System.identityHashCode(str)));
        }
    }
}
