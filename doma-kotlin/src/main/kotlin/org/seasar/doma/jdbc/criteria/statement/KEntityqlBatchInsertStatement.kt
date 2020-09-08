package org.seasar.doma.jdbc.criteria.statement

import org.seasar.doma.jdbc.BatchResult
import org.seasar.doma.jdbc.Sql

class KEntityqlBatchInsertStatement<ENTITY>(
        private val statement: Statement<BatchResult<ENTITY>>) : KStatement<BatchResult<ENTITY>> {

    override fun execute(): BatchResult<ENTITY> {
        return statement.execute()
    }

    override fun asSql(): Sql<*> {
        return statement.asSql()
    }
}