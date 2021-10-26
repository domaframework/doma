package org.seasar.doma.kotlin.jdbc.criteria.statement

import org.seasar.doma.jdbc.Sql

interface KStatement<RESULT> : KBuildable<KStatement<RESULT>> {

    fun execute(): RESULT

    override fun peek(block: (Sql<*>) -> Unit): KStatement<RESULT> {
        super.peek(block)
        return this
    }
}
