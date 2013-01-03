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

package com.sunsprinter.diffunit.core.instancetracking;


/**
 * Forms the identifier for a specific object.  An object identifier is an ID allocated to an object by the {@link
 * com.sunsprinter.diffunit.core.instancetracking.IObjectInstanceTracker}.  An object id is comprised of the object
 * itself and an instance number.  The instance number is a number that applies to the object and is guaranteed not to
 * change.  The instance number applies to the <i>type</i> of the object.  It is allocated on a per-type basis and
 * increments on a per-type basis.  See {@link com.sunsprinter.diffunit.core.instancetracking.IObjectInstanceTracker}
 * for more information.<p/>
 *
 * Think of the {@link com.sunsprinter.diffunit.core.instancetracking.IObjectIdentifier}s as unique black boxes.  They
 * can automatically be translated to reproducible string form by DiffUnit.
 *
 * @author Kevan Dunsmore
 * @created 2011/11/11
 */
public interface IObjectIdentifier
{
    /**
     * The object to which this identifier applies.
     *
     * @param <T> The type of the object.
     *
     * @return the object to which this identifier applies.
     */
    <T> T getObject();

    /**
     * The instance number of this object.
     *
     * @return the instance number of this object.
     */
    int getInstanceNumber();
}
