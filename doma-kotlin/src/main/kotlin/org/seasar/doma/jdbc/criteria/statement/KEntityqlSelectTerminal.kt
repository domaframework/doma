package org.seasar.doma.jdbc.criteria.statement

import org.seasar.doma.jdbc.Sql

class KEntityqlSelectTerminal<ENTITY>(val statement : EntityqlSelectTerminal<ENTITY>) : KListable<ENTITY> {

    override fun peek(block: (Sql<*>) -> Unit): KListable<ENTITY> {
        statement.peek(block)
        return this
    }

    override fun execute(): List<ENTITY> {
        return statement.execute()
    }

    override fun asSql(): Sql<*> {
        return statement.asSql()
    }

}