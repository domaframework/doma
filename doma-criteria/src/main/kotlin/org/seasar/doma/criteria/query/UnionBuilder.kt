package org.seasar.doma.criteria.query

import org.seasar.doma.criteria.context.SelectContext
import org.seasar.doma.criteria.statement.SetOperator
import org.seasar.doma.internal.jdbc.sql.PreparedSqlBuilder
import org.seasar.doma.jdbc.Config
import org.seasar.doma.jdbc.PreparedSql
import org.seasar.doma.jdbc.SqlKind
import org.seasar.doma.jdbc.SqlLogType

class UnionBuilder(
    private val config: Config,
    private val contexts: SetOperator<SelectContext>,
        // TODO the SqlLogType value should be passed from the caller
    private val buf: PreparedSqlBuilder = PreparedSqlBuilder(config, SqlKind.SELECT, SqlLogType.FORMATTED)
) {
    fun build(): PreparedSql {
        fun visit(operator: SetOperator<SelectContext>) {
            when (operator) {
                is SetOperator.Select -> {
                    val builder = SelectBuilder(operator.content, buf)
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
        // TODO Use config.commenter
        return buf.build { it }
    }
}
