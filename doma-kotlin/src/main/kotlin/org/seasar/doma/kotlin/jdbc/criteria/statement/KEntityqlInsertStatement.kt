package org.seasar.doma.kotlin.jdbc.criteria.statement

import org.seasar.doma.jdbc.Result
import org.seasar.doma.jdbc.Sql
import org.seasar.doma.jdbc.criteria.statement.EntityqlInsertStatement

class KEntityqlInsertStatement<ENTITY>(
    private val statement: EntityqlInsertStatement<ENTITY>,
) : KStatement<Result<ENTITY>> {

    fun onDuplicateKeyUpdate(): KStatement<Result<ENTITY>> {
        return KEntityqlUpsertStatement(statement.onDuplicateKeyUpdate())
    }

    fun onDuplicateKeyIgnore(): KStatement<Result<ENTITY>> {
        return KEntityqlUpsertStatement(statement.onDuplicateKeyIgnore())
    }

    override fun execute(): Result<ENTITY> {
        return statement.execute()
    }

    override fun asSql(): Sql<*> {
        return statement.asSql()
    }
}
