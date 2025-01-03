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
package org.seasar.doma.kotlin.jdbc.criteria.statement

import org.seasar.doma.jdbc.criteria.context.SubSelectContext
import org.seasar.doma.jdbc.criteria.statement.NativeSqlInsertStarting
import org.seasar.doma.kotlin.jdbc.criteria.declaration.KInsertSelectDeclaration
import org.seasar.doma.kotlin.jdbc.criteria.declaration.KValuesDeclaration

class KNativeSqlInsertStarting(private val statement: NativeSqlInsertStarting) {

    fun values(block: KValuesDeclaration.() -> Unit): KNativeSqlInsertTerminal {
        val terminal = statement.values { block(KValuesDeclaration(it)) }
        return KNativeSqlInsertTerminal(terminal)
    }

    fun select(block: KInsertSelectDeclaration.() -> SubSelectContext<*>): KNativeSqlInsertTerminal {
        val terminal = statement.select { block(KInsertSelectDeclaration()) }
        return KNativeSqlInsertTerminal(terminal)
    }
}
