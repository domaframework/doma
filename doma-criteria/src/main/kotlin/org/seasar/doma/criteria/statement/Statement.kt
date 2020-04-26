package org.seasar.doma.criteria.statement

import org.seasar.doma.jdbc.CommentContext
import org.seasar.doma.jdbc.Config
import org.seasar.doma.jdbc.PreparedSql
import org.seasar.doma.jdbc.Sql
import org.seasar.doma.jdbc.SqlLogType
import org.seasar.doma.jdbc.command.Command

interface Statement<RESULT> {
    fun execute(config: Config, comment: String? = null, logType: SqlLogType = SqlLogType.FORMATTED): RESULT
    fun asSql(config: Config, comment: String? = null): Sql<*>
}

abstract class AbstractStatement<RESULT> : Statement<RESULT> {

    protected val executeMethodName = "execute"

    override fun execute(config: Config, comment: String?, logType: SqlLogType): RESULT {
        val (command, _) = commandAndSql(config, commenter(config, comment), logType)
        return command.execute()
    }

    override fun asSql(config: Config, comment: String?): Sql<*> {
        val (_, sql) = commandAndSql(config, commenter(config, comment), SqlLogType.FORMATTED)
        return sql
    }

    // TODO enhance CommentContext to accept a message
    private fun commenter(config: Config, comment: String?): (String) -> String {
        return { sql ->
            val newSql = if (comment == null) sql else "// ${comment}\n$sql"
            val commentContext = CommentContext(javaClass.name, executeMethodName, config, null)
            config.commenter.comment(newSql, commentContext)
        }
    }

    protected abstract fun commandAndSql(config: Config, commenter: (String) -> String, logType: SqlLogType): Pair<Command<RESULT>, PreparedSql>
}
