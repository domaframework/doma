package org.seasar.doma.criteria

import org.seasar.doma.jdbc.Config
import org.seasar.doma.jdbc.PreparedSql
import org.seasar.doma.jdbc.Sql
import org.seasar.doma.jdbc.command.Command

interface Statement<RESULT> {
    fun execute(config: Config): RESULT
    fun asSql(config: Config): Sql<*>
}

abstract class AbstractStatement<RESULT> : Statement<RESULT> {
    override fun execute(config: Config): RESULT {
        val (command, _) = commandAndSql(config)
        return command.execute()
    }

    override fun asSql(config: Config): Sql<*> {
        val (_, sql) = commandAndSql(config)
        return sql
    }

    protected abstract fun commandAndSql(config: Config): Pair<Command<RESULT>, PreparedSql>
}
