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

package com.sunsprinter.diffunit.core.translators;


/**
 * Special root translator available on all DiffUnit tests.  The root translator may be used to bind specific
 * translators to types.  By default, the root translator try to translate objects by calling {@link Object#toString()}
 * on them (unless the supplied object reference is null, in which case it translates to the string "null").
 *
 * @author Kevan Dunsmore
 * @created 2011/11/13
 */
public interface IRootTranslator extends ITypeBindingTranslator
{
}
