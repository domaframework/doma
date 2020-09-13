package org.seasar.doma.jdbc.criteria.statement

import org.seasar.doma.jdbc.Sql
import org.seasar.doma.jdbc.criteria.declaration.KWhereDeclaration

class KNativeSqlUpdateTerminal(val statement: NativeSqlUpdateTerminal) : KStatement<Int> {

    fun where(block: KWhereDeclaration.() -> Unit): KStatement<Int> {
        statement.where { block(KWhereDeclaration(it)) }
        return this
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
