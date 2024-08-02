package org.seasar.doma.kotlin.jdbc.criteria.statement

import org.seasar.doma.jdbc.MultiResult
import org.seasar.doma.jdbc.Sql
import org.seasar.doma.jdbc.criteria.statement.EntityqlMultiInsertStatement

class KEntityqlMultiInsertStatement<ENTITY>(
    private val statement: EntityqlMultiInsertStatement<ENTITY>,
) : KStatement<MultiResult<ENTITY>> {

    fun onDuplicateKeyUpdate(): KStatement<MultiResult<ENTITY>> {
        return KEntityqlMultiUpsertStatement(statement.onDuplicateKeyUpdate())
    }

    fun onDuplicateKeyIgnore(): KStatement<MultiResult<ENTITY>> {
        return KEntityqlMultiUpsertStatement(statement.onDuplicateKeyIgnore())
    }

    override fun execute(): MultiResult<ENTITY> {
        return statement.execute()
    }

    override fun asSql(): Sql<*> {
        return statement.asSql()
    }
}
