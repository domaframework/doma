/*
 * Copyright Doma Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.seasar.doma.kotlin.jdbc.criteria.declaration

import org.seasar.doma.jdbc.criteria.declaration.ValuesDeclaration
import org.seasar.doma.jdbc.criteria.metamodel.PropertyMetamodel

class KValuesDeclaration(val declaration: ValuesDeclaration) {

    fun <PROPERTY : Any> value(left: PropertyMetamodel<PROPERTY>, right: PROPERTY?) {
        declaration.value(left, right)
    }
}
