package org.seasar.doma.kotlin.jdbc.criteria.statement

import org.seasar.doma.jdbc.Sql
import org.seasar.doma.jdbc.criteria.statement.NativeSqlInsertTerminal

class KNativeSqlInsertTerminal(private val statement: NativeSqlInsertTerminal) : KStatement<Int> {

    override fun execute(): Int {
        return statement.execute()
    }

    fun onDuplicateKeyUpdate(): KNativeSqlUpsertOnDuplicateKeyUpdateSelectingKeys {
        return KNativeSqlUpsertOnDuplicateKeyUpdateSelectingKeys(statement.onDuplicateKeyUpdate())
    }

    fun onDuplicateKeyIgnore(): KNativeSqlUpsertOnDuplicateKeyIgnoreSelectingKeys {
        return KNativeSqlUpsertOnDuplicateKeyIgnoreSelectingKeys(statement.onDuplicateKeyIgnore())
    }

    override fun asSql(): Sql<*> {
        return statement.asSql()
    }
}
