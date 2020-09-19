package org.seasar.doma.jdbc.criteria.statement

import org.seasar.doma.jdbc.Sql

interface KBuildable<BUILDABLE : KBuildable<BUILDABLE>> {

    fun asSql(): Sql<*>

    fun peek(block: (Sql<*>) -> Unit): KBuildable<BUILDABLE> {
        block(asSql())
        return this
    }
}
