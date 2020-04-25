package org.seasar.doma.criteria.query

import org.seasar.doma.criteria.Criterion
import org.seasar.doma.criteria.DeleteContext
import org.seasar.doma.internal.jdbc.sql.PreparedSqlBuilder
import org.seasar.doma.jdbc.PreparedSql
import org.seasar.doma.jdbc.SqlKind
import org.seasar.doma.jdbc.SqlLogType
import org.seasar.doma.jdbc.entity.EntityType

class DeleteBuilder(
    private val context: DeleteContext,
        // TODO the SqlLogType value should be passed from the caller
    private val buf: PreparedSqlBuilder = PreparedSqlBuilder(context.config, SqlKind.INSERT, SqlLogType.FORMATTED),
    aliasManager: AliasManager = AliasManager(context)
) {

    private val config = context.config

    private val support = BuilderSupport(config, buf, aliasManager)

    fun build(): PreparedSql {
        buf.appendSql("delete from ")
        table(context.entityType)
        if (context.where.isNotEmpty()) {
            buf.appendSql(" where ")
            context.where.forEachIndexed { index, c ->
                visitCriterion(index, c)
                buf.appendSql(" and ")
            }
            buf.cutBackSql(5)
        }
        // TODO Use config.commenter
        return buf.build { it }
    }

    private fun table(entityType: EntityType<*>) {
        support.table(entityType)
    }

    private fun visitCriterion(index: Int, c: Criterion) {
        support.visitCriterion(index, c)
    }
}
