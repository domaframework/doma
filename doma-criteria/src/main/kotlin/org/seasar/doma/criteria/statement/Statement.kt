package org.seasar.doma.criteria.statement

import org.seasar.doma.jdbc.CommentContext
import org.seasar.doma.jdbc.Config
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
        val command = createCommand(config, commenter(config, comment), logType)
        return command.execute()
    }

    override fun asSql(config: Config, comment: String?): Sql<*> {
        val command = createCommand(config, commenter(config, comment), SqlLogType.FORMATTED)
        val query = command.query
        return query.sql
    }

    private fun commenter(config: Config, comment: String?): (String) -> String {
        return { sql ->
            val commentContext = CommentContext(javaClass.name, executeMethodName, config, null, comment)
            config.commenter.comment(sql, commentContext)
        }
    }

    protected abstract fun createCommand(config: Config, commenter: (String) -> String, logType: SqlLogType): Command<RESULT>
}
