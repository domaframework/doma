package org.seasar.doma.jdbc.criteria.statement

import org.seasar.doma.jdbc.Sql

interface KBuildable<BUILDABLE : KBuildable<BUILDABLE>> {

    fun asSql(): Sql<*>

    @Suppress("UNCHECKED_CAST")
    fun peek(block: (Sql<*>) -> Unit): BUILDABLE {
        block(asSql())
        return this as BUILDABLE
    }
}