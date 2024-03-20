package org.seasar.doma.kotlin.jdbc.criteria.statement

import org.seasar.doma.jdbc.BatchResult
import org.seasar.doma.jdbc.Sql
import org.seasar.doma.jdbc.criteria.statement.Statement

class KEntityqlBatchUpsertStatement<ENTITY>(private val statement: Statement<BatchResult<ENTITY>>) : KStatement<BatchResult<ENTITY>> {

    override fun execute(): BatchResult<ENTITY> {
        return statement.execute()
    }

    override fun asSql(): Sql<*> {
        return statement.asSql()
    }
}
