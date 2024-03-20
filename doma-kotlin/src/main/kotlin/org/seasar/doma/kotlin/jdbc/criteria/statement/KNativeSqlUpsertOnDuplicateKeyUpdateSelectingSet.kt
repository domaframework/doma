package org.seasar.doma.kotlin.jdbc.criteria.statement

import org.seasar.doma.jdbc.Sql
import org.seasar.doma.jdbc.criteria.declaration.InsertOnDuplicateKeyUpdateSetValuesDeclaration
import org.seasar.doma.jdbc.criteria.statement.NativeSqlUpsertOnDuplicateKeyUpdateSelectingSet

class KNativeSqlUpsertOnDuplicateKeyUpdateSelectingSet(val statement: NativeSqlUpsertOnDuplicateKeyUpdateSelectingSet) : KStatement<Int> {
    fun set(block: (InsertOnDuplicateKeyUpdateSetValuesDeclaration) -> Unit): KNativeSqlUpsertTerminal {
        return KNativeSqlUpsertTerminal(statement.set(block))
    }

    override fun execute(): Int {
        return statement.execute()
    }

    override fun asSql(): Sql<*> {
        return statement.asSql()
    }

    override fun peek(block: (Sql<*>) -> Unit): KStatement<Int> {
        statement.peek(block)
        return this
    }
}
