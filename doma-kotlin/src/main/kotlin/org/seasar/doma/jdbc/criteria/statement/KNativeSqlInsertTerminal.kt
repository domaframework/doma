package org.seasar.doma.jdbc.criteria.statement

import org.seasar.doma.jdbc.Sql

class KNativeSqlInsertTerminal(private val statement: NativeSqlInsertTerminal) : KStatement<Int> {

    override fun execute(): Int {
        return statement.execute()
    }

    override fun asSql(): Sql<*> {
        return statement.asSql()
    }
}
