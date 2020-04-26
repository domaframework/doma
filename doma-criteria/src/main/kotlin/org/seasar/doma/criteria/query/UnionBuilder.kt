package org.seasar.doma.criteria.query

import org.seasar.doma.criteria.context.SelectContext
import org.seasar.doma.criteria.statement.SetOperator
import org.seasar.doma.internal.jdbc.sql.PreparedSqlBuilder
import org.seasar.doma.jdbc.Config
import org.seasar.doma.jdbc.PreparedSql
import org.seasar.doma.jdbc.SqlKind
import org.seasar.doma.jdbc.SqlLogType

class UnionBuilder(
    private val contexts: SetOperator<SelectContext>,
    private val commenter: (String) -> String,
    private val buf: PreparedSqlBuilder
) {

    constructor(config: Config, contexts: SetOperator<SelectContext>, commenter: (String) -> String, logType: SqlLogType) :
            this(contexts, commenter, PreparedSqlBuilder(config, SqlKind.SELECT, logType))

    fun build(): PreparedSql {
        fun visit(operator: SetOperator<SelectContext>) {
            when (operator) {
                is SetOperator.Select -> {
                    val context = operator.content
                    val builder = SelectBuilder(context, commenter, buf, AliasManager(context))
                    builder.interpretContext()
                }
                is SetOperator.Union -> {
                    visit(operator.left)
                    buf.appendSql(" union ")
                    if (operator.all) {
                        buf.appendSql("all ")
                    }
                    visit(operator.right)
                }
            }
        }
        visit(contexts)
        return buf.build(commenter)
    }
}
