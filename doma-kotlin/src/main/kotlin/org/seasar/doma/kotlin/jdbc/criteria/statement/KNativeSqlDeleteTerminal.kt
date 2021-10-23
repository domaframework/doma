package org.seasar.doma.kotlin.jdbc.criteria.statement

import org.seasar.doma.jdbc.Sql
import org.seasar.doma.jdbc.criteria.statement.Statement

class KNativeSqlDeleteTerminal(private val statement: Statement<Int>) : KStatement<Int> {

    override fun execute(): Int {
        return statement.execute()
    }

    override fun asSql(): Sql<*> {
        return statement.asSql()
    }
}
