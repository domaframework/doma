package org.seasar.doma.jdbc.criteria.statement

import org.seasar.doma.jdbc.Sql

interface KListable<ELEMENT> : KStatement<List<ELEMENT>> {

    fun fetch(): List<ELEMENT> {
        return execute()
    }

    fun fetchOne(): ELEMENT {
        return execute().first()
    }

    fun fetchOneOrNull(): ELEMENT? {
        return execute().firstOrNull()
    }

    fun sequence(): Sequence<ELEMENT> {
        return execute().asSequence()
    }

    override fun peek(block: (Sql<*>) -> Unit): KListable<ELEMENT>
}
