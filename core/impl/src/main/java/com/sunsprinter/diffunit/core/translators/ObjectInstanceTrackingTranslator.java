/*
 * Copyright 2011 Kevan Dunsmore.  All rights reserved.
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


import com.sunsprinter.diffunit.core.context.ITestingContext;
import com.sunsprinter.diffunit.core.context.TestingContextHolder;
import com.sunsprinter.diffunit.core.instancetracking.IObjectIdentifier;
import com.sunsprinter.diffunit.core.instancetracking.IObjectInstanceTracker;


/**
 * Translates objects to strings based on the {@link IObjectIdentifier}s returned by {@link IObjectInstanceTracker} for
 * each presented object.  As the string value is based on the object id, which is in turn calculated by the instance
 * tracker in a deterministic fashion (the order in which the objects are presented), this translator is a good choice
 * for converting objects that are not the primary focus of a test.  It ensures that a specific type of object is
 * created and that each instance is seen in the same order.<p/>
 *
 * This class uses the instance tracker to get each object's identifier and then uses a delegate translator to covert
 * the object id to a string.  The delegate translator may be customized.</p>
 *
 * @author Kevan Dunsmore
 * @created 2011/11/10
 */
public class ObjectInstanceTrackingTranslator extends AbstractDelegatingTranslator<Object>
{
    @Override
    protected synchronized String doTranslate(final Object object) throws TranslationException
    {
        return getDelegateTranslator().translate(getInstanceTracker().getObjectId(object));
    }
}
