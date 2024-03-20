package org.seasar.doma.kotlin.jdbc.criteria.statement

import org.seasar.doma.jdbc.Sql
import org.seasar.doma.jdbc.criteria.declaration.InsertOnDuplicateKeyUpdateSetValuesDeclaration
import org.seasar.doma.jdbc.criteria.metamodel.PropertyMetamodel
import org.seasar.doma.jdbc.criteria.statement.NativeSqlUpsertOnDuplicateKeyUpdateSelectingKeys
import org.seasar.doma.jdbc.criteria.statement.NativeSqlUpsertTerminal

class KNativeSqlUpsertOnDuplicateKeyUpdateSelectingKeys(val statement: NativeSqlUpsertOnDuplicateKeyUpdateSelectingKeys) : KStatement<Int> {
    fun keys(vararg keys: PropertyMetamodel<*>): KNativeSqlUpsertOnDuplicateKeyUpdateSelectingSet {
        return KNativeSqlUpsertOnDuplicateKeyUpdateSelectingSet(statement.keys(*keys))
    }

    fun set(block: (InsertOnDuplicateKeyUpdateSetValuesDeclaration) -> Unit): NativeSqlUpsertTerminal {
        return statement.set(block)
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
