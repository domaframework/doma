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

import org.seasar.doma.jdbc.Config
import org.seasar.doma.jdbc.criteria.QueryDsl
import org.seasar.doma.jdbc.criteria.context.DeleteSettings
import org.seasar.doma.jdbc.criteria.context.InsertSettings
import org.seasar.doma.jdbc.criteria.context.SelectSettings
import org.seasar.doma.jdbc.criteria.context.UpdateSettings
import org.seasar.doma.jdbc.criteria.context.WithContext
import org.seasar.doma.jdbc.criteria.metamodel.EntityMetamodel
import org.seasar.doma.kotlin.jdbc.criteria.statement.KSetOperand
import org.seasar.doma.kotlin.jdbc.criteria.statement.KUnifiedDeleteStarting
import org.seasar.doma.kotlin.jdbc.criteria.statement.KUnifiedInsertStarting
import org.seasar.doma.kotlin.jdbc.criteria.statement.KUnifiedSelectStarting
import org.seasar.doma.kotlin.jdbc.criteria.statement.KUnifiedUpdateStarting

class KQueryDsl(config: Config) {

    private val dsl = QueryDsl(config)

    fun with(
        vararg pairs: Pair<EntityMetamodel<*>, KSetOperand<*>>,
    ): KWithQueryDsl {
        val withContexts = pairs.map { WithContext(it.first, it.second.asSetOperand().context) }
        return KWithQueryDsl(dsl.with(withContexts))
    }

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

    fun <ENTITY : Any> update(
        entityMetamodel: EntityMetamodel<ENTITY>,
        block: UpdateSettings.() -> Unit = {},
    ): KUnifiedUpdateStarting<ENTITY> {
        val statement = dsl.update(entityMetamodel, block)
        return KUnifiedUpdateStarting(statement)
    }

    fun <ENTITY : Any> delete(
        entityMetamodel: EntityMetamodel<ENTITY>,
        block: DeleteSettings.() -> Unit = {},
    ): KUnifiedDeleteStarting<ENTITY> {
        val statement = dsl.delete(entityMetamodel, block)
        return KUnifiedDeleteStarting(statement)
    }

    fun <ENTITY : Any> insert(
        entityMetamodel: EntityMetamodel<ENTITY>,
        block: InsertSettings.() -> Unit = {},
    ): KUnifiedInsertStarting<ENTITY> {
        val statement = dsl.insert(entityMetamodel, block)
        return KUnifiedInsertStarting(statement)
    }
}
