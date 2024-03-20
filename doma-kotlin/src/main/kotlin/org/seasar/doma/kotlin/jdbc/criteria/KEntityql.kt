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
import org.seasar.doma.kotlin.jdbc.criteria.statement.KEntityqlSelectStarting
import org.seasar.doma.kotlin.jdbc.criteria.statement.KEntityqlUpdateStatement
import org.seasar.doma.kotlin.jdbc.criteria.statement.KStatement

class KEntityql(config: Config) {

    private val entityql = org.seasar.doma.jdbc.criteria.Entityql(config)

    fun <ENTITY> from(
        entityMetamodel: EntityMetamodel<ENTITY>,
        block: SelectSettings.() -> Unit = {},
    ): KEntityqlSelectStarting<ENTITY> {
        val statement = entityql.from(entityMetamodel, block)
        return KEntityqlSelectStarting(statement)
    }

    fun <ENTITY> update(
        entityMetamodel: EntityMetamodel<ENTITY>,
        entity: ENTITY,
        block: UpdateSettings.() -> Unit = {},
    ): KStatement<Result<ENTITY>> {
        val statement = entityql.update(entityMetamodel, entity, block)
        return KEntityqlUpdateStatement(statement)
    }

    fun <ENTITY> delete(
        entityMetamodel: EntityMetamodel<ENTITY>,
        entity: ENTITY,
        block: DeleteSettings.() -> Unit = {},
    ): KStatement<Result<ENTITY>> {
        val statement = entityql.delete(entityMetamodel, entity, block)
        return KEntityqlDeleteStatement(statement)
    }

    fun <ENTITY> insert(
        entityMetamodel: EntityMetamodel<ENTITY>,
        entity: ENTITY,
        block: InsertSettings.() -> Unit = {},
    ): KEntityqlInsertStatement<ENTITY> {
        val statement = entityql.insert(entityMetamodel, entity, block)
        return KEntityqlInsertStatement(statement)
    }

    fun <ENTITY> update(
        entityMetamodel: EntityMetamodel<ENTITY>,
        entities: List<ENTITY>,
        block: UpdateSettings.() -> Unit = {},
    ): KStatement<BatchResult<ENTITY>> {
        val statement = entityql.update(entityMetamodel, entities, block)
        return KEntityqlBatchUpdateStatement(statement)
    }

    fun <ENTITY> delete(
        entityMetamodel: EntityMetamodel<ENTITY>,
        entities: List<ENTITY>,
        block: DeleteSettings.() -> Unit = {},
    ): KStatement<BatchResult<ENTITY>> {
        val statement = entityql.delete(entityMetamodel, entities, block)
        return KEntityqlBatchDeleteStatement(statement)
    }

    fun <ENTITY> insert(
        entityMetamodel: EntityMetamodel<ENTITY>,
        entities: List<ENTITY>,
        block: InsertSettings.() -> Unit = {},
    ): KEntityqlBatchInsertStatement<ENTITY> {
        val statement = entityql.insert(entityMetamodel, entities, block)
        return KEntityqlBatchInsertStatement(statement)
    }
}
