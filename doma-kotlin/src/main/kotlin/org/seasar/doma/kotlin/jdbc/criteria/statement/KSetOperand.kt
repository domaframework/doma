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

import org.seasar.doma.jdbc.Sql
import org.seasar.doma.jdbc.criteria.context.SetOperationContext
import org.seasar.doma.jdbc.criteria.statement.SetOperand

interface KSetOperand<ELEMENT> : KSequenceMappable<ELEMENT> {

    val context: SetOperationContext<ELEMENT>

    fun union(other: KSetOperand<ELEMENT>): KSetOperator<ELEMENT>

    fun unionAll(other: KSetOperand<ELEMENT>): KSetOperator<ELEMENT>

    fun asSetOperand(): SetOperand<ELEMENT>

    override fun peek(block: (Sql<*>) -> Unit): KSetOperand<ELEMENT>
}
