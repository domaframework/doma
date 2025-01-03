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

import org.seasar.doma.jdbc.BatchResult
import org.seasar.doma.jdbc.Config
import org.seasar.doma.jdbc.Result
import org.seasar.doma.jdbc.criteria.context.DeleteSettings
import org.seasar.doma.jdbc.criteria.context.InsertSettings
import org.seasar.doma.jdbc.criteria.context.SelectSettings
import org.seasar.doma.jdbc.criteria.context.UpdateSettings
import org.seasar.doma.jdbc.criteria.metamodel.EntityMetamodel
import org.seasar.doma.kotlin.jdbc.criteria.statement.KEntityqlBatchDeleteStatement
import org.seasar.doma.kotlin.jdbc.criteria.statement.KEntityqlBatchInsertStatement
import org.seasar.doma.kotlin.jdbc.criteria.statement.KEntityqlBatchUpdateStatement
import org.seasar.doma.kotlin.jdbc.criteria.statement.KEntityqlDeleteStatement
import org.seasar.doma.kotlin.jdbc.criteria.statement.KEntityqlInsertStatement
import org.seasar.doma.kotlin.jdbc.criteria.statement.KEntityqlMultiInsertStatement
import org.seasar.doma.kotlin.jdbc.criteria.statement.KEntityqlSelectStarting
import org.seasar.doma.kotlin.jdbc.criteria.statement.KEntityqlUpdateStatement
import org.seasar.doma.kotlin.jdbc.criteria.statement.KSetOperand
import org.seasar.doma.kotlin.jdbc.criteria.statement.KStatement

@Suppress("DEPRECATION")
@Deprecated(
    message = "This class will be removed in the future. Use KQueryDsl instead.",
    replaceWith = ReplaceWith("KQueryDsl", "org.seasar.doma.kotlin.jdbc.criteria.KQueryDsl"),
)
class KEntityql(config: Config) {

    private val entityql = org.seasar.doma.jdbc.criteria.Entityql(config)

    fun <ENTITY : Any> from(
        entityMetamodel: EntityMetamodel<ENTITY>,
        block: SelectSettings.() -> Unit = {},
    ): KEntityqlSelectStarting<ENTITY> {
        val statement = entityql.from(entityMetamodel, block)
        return KEntityqlSelectStarting(statement)
    }

    fun <ENTITY : Any> from(
        entityMetamodel: EntityMetamodel<ENTITY>,
        setOperandForSubQuery: KSetOperand<*>,
        block: SelectSettings.() -> Unit = {},
    ): KEntityqlSelectStarting<ENTITY> {
        val statement = entityql.from(entityMetamodel, setOperandForSubQuery.asSetOperand(), block)
        return KEntityqlSelectStarting(statement)
    }

    fun <ENTITY : Any> update(
        entityMetamodel: EntityMetamodel<ENTITY>,
        entity: ENTITY,
        block: UpdateSettings.() -> Unit = {},
    ): KStatement<Result<ENTITY>> {
        val statement = entityql.update(entityMetamodel, entity, block)
        return KEntityqlUpdateStatement(statement)
    }

    fun <ENTITY : Any> delete(
        entityMetamodel: EntityMetamodel<ENTITY>,
        entity: ENTITY,
        block: DeleteSettings.() -> Unit = {},
    ): KStatement<Result<ENTITY>> {
        val statement = entityql.delete(entityMetamodel, entity, block)
        return KEntityqlDeleteStatement(statement)
    }

    fun <ENTITY : Any> insert(
        entityMetamodel: EntityMetamodel<ENTITY>,
        entity: ENTITY,
        block: InsertSettings.() -> Unit = {},
    ): KEntityqlInsertStatement<ENTITY> {
        val statement = entityql.insert(entityMetamodel, entity, block)
        return KEntityqlInsertStatement(statement)
    }

    fun <ENTITY : Any> update(
        entityMetamodel: EntityMetamodel<ENTITY>,
        entities: List<ENTITY>,
        block: UpdateSettings.() -> Unit = {},
    ): KStatement<BatchResult<ENTITY>> {
        val statement = entityql.update(entityMetamodel, entities, block)
        return KEntityqlBatchUpdateStatement(statement)
    }

    fun <ENTITY : Any> delete(
        entityMetamodel: EntityMetamodel<ENTITY>,
        entities: List<ENTITY>,
        block: DeleteSettings.() -> Unit = {},
    ): KStatement<BatchResult<ENTITY>> {
        val statement = entityql.delete(entityMetamodel, entities, block)
        return KEntityqlBatchDeleteStatement(statement)
    }

    fun <ENTITY : Any> insert(
        entityMetamodel: EntityMetamodel<ENTITY>,
        entities: List<ENTITY>,
        block: InsertSettings.() -> Unit = {},
    ): KEntityqlBatchInsertStatement<ENTITY> {
        val statement = entityql.insert(entityMetamodel, entities, block)
        return KEntityqlBatchInsertStatement(statement)
    }

    fun <ENTITY : Any> insertMulti(
        entityMetamodel: EntityMetamodel<ENTITY>,
        entities: List<ENTITY>,
        block: InsertSettings.() -> Unit = {},
    ): KEntityqlMultiInsertStatement<ENTITY> {
        val statement = entityql.insertMulti(entityMetamodel, entities, block)
        return KEntityqlMultiInsertStatement(statement)
    }
}
