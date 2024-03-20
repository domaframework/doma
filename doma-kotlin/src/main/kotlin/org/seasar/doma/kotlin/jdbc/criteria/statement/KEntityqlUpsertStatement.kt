package org.seasar.doma.kotlin.jdbc.criteria.statement

import org.seasar.doma.jdbc.Result
import org.seasar.doma.jdbc.Sql
import org.seasar.doma.jdbc.criteria.statement.Statement

class KEntityqlUpsertStatement<ENTITY>(private val statement: Statement<Result<ENTITY>>) : KStatement<Result<ENTITY>> {

    override fun execute(): Result<ENTITY> {
        return statement.execute()
    }

    override fun asSql(): Sql<*> {
        return statement.asSql()
    }
}
