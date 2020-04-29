package org.seasar.doma.criteria.query

import org.seasar.doma.criteria.context.Criterion
import org.seasar.doma.criteria.context.DeleteContext
import org.seasar.doma.def.EntityDef
import org.seasar.doma.internal.jdbc.sql.PreparedSqlBuilder
import org.seasar.doma.jdbc.PreparedSql
import org.seasar.doma.jdbc.SqlKind
import org.seasar.doma.jdbc.SqlLogType

class DeleteBuilder(
    private val context: DeleteContext,
    private val commenter: (String) -> String,
    private val buf: PreparedSqlBuilder,
    aliasManager: AliasManager
) {

    constructor(context: DeleteContext, commenter: (String) -> String, logType: SqlLogType) :
            this(context, commenter, PreparedSqlBuilder(context.config, SqlKind.DELETE, logType), AliasManager(context))

    private val config = context.config

    private val support = BuilderSupport(config, commenter, buf, aliasManager)

    fun build(): PreparedSql {
        buf.appendSql("delete from ")
        table(context.entityDef)
        if (context.where.isNotEmpty()) {
            buf.appendSql(" where ")
            context.where.forEachIndexed { index, c ->
                visitCriterion(index, c)
                buf.appendSql(" and ")
            }
            buf.cutBackSql(5)
        }
        return buf.build(commenter)
    }

    private fun table(entityDef: EntityDef<*>) {
        support.table(entityDef)
    }

    private fun visitCriterion(index: Int, c: Criterion) {
        support.visitCriterion(index, c)
    }
}
