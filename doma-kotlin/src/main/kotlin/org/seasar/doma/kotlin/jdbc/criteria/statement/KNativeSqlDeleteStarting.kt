package org.seasar.doma.kotlin.jdbc.criteria.statement

import org.seasar.doma.jdbc.Sql
import org.seasar.doma.jdbc.criteria.statement.NativeSqlDeleteStarting
import org.seasar.doma.kotlin.jdbc.criteria.declaration.KWhereDeclaration

class KNativeSqlDeleteStarting(private val statement: NativeSqlDeleteStarting) : KStatement<Int> {

    fun where(block: KWhereDeclaration.() -> Unit): KStatement<Int> {
        val terminal = statement.where { block(KWhereDeclaration(it)) }
        return KNativeSqlDeleteTerminal(terminal)
    }

    override fun execute(): Int {
        return statement.execute()
    }

    override fun asSql(): Sql<*> {
        return statement.asSql()
    }

    override fun peek(block: (Sql<*>) -> Unit): KNativeSqlDeleteStarting {
        statement.peek(block)
        return this
    }
}
