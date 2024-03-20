package org.seasar.doma.kotlin.jdbc.criteria.statement

import org.seasar.doma.jdbc.BatchResult
import org.seasar.doma.jdbc.Sql
import org.seasar.doma.jdbc.criteria.statement.EntityqlBatchInsertStatement

class KEntityqlBatchInsertStatement<ENTITY>(
    private val statement: EntityqlBatchInsertStatement<ENTITY>,
) : KStatement<BatchResult<ENTITY>> {

    fun onDuplicateKeyUpdate(): KStatement<BatchResult<ENTITY>> {
        return KEntityqlBatchUpsertStatement(statement.onDuplicateKeyUpdate())
    }

    fun onDuplicateKeyIgnore(): KStatement<BatchResult<ENTITY>> {
        return KEntityqlBatchUpsertStatement(statement.onDuplicateKeyIgnore())
    }

    override fun execute(): BatchResult<ENTITY> {
        return statement.execute()
    }

    override fun asSql(): Sql<*> {
        return statement.asSql()
    }
}
