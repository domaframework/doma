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
package org.seasar.doma.kotlin.jdbc.criteria

import org.seasar.doma.jdbc.criteria.WithQueryDsl
import org.seasar.doma.jdbc.criteria.context.SelectSettings
import org.seasar.doma.jdbc.criteria.context.WithContext
import org.seasar.doma.jdbc.criteria.metamodel.EntityMetamodel
import org.seasar.doma.kotlin.jdbc.criteria.statement.KSetOperand
import org.seasar.doma.kotlin.jdbc.criteria.statement.KUnifiedSelectStarting

class KWithQueryDsl(private val dsl: WithQueryDsl) {

    fun <ENTITY : Any> from(
        entityMetamodel: EntityMetamodel<ENTITY>,
        block: SelectSettings.() -> Unit = {},
    ): KUnifiedSelectStarting<ENTITY> {
        val statement = dsl.from(entityMetamodel, block)
        return KUnifiedSelectStarting(statement)
    }

    fun <ENTITY : Any> from(
        entityMetamodel: EntityMetamodel<ENTITY>,
        setOperandForSubQuery: KSetOperand<*>,
        block: SelectSettings.() -> Unit = {},
    ): KUnifiedSelectStarting<ENTITY> {
        val statement = dsl.from(entityMetamodel, setOperandForSubQuery.asSetOperand(), block)
        return KUnifiedSelectStarting(statement)
    }

    fun with(
        vararg pairs: Pair<EntityMetamodel<*>, KSetOperand<*>>,
    ): KWithQueryDsl {
        val withContexts = pairs.map { WithContext(it.first, it.second.asSetOperand().context) }
        return KWithQueryDsl(dsl.with(withContexts))
    }
}
