package org.seasar.doma.kotlin.jdbc.criteria.statement

import org.seasar.doma.jdbc.Sql
import org.seasar.doma.jdbc.criteria.metamodel.PropertyMetamodel
import org.seasar.doma.jdbc.criteria.statement.NativeSqlUpsertOnDuplicateKeyIgnoreSelectingKeys

class KNativeSqlUpsertOnDuplicateKeyIgnoreSelectingKeys(val statement: NativeSqlUpsertOnDuplicateKeyIgnoreSelectingKeys) : KStatement<Int> {
    fun keys(vararg keys: PropertyMetamodel<*>): KNativeSqlUpsertTerminal {
        return KNativeSqlUpsertTerminal(statement.keys(*keys))
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
