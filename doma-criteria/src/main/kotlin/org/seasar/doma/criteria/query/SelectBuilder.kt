package org.seasar.doma.criteria.query

import org.seasar.doma.criteria.context.Criterion
import org.seasar.doma.criteria.context.JoinKind
import org.seasar.doma.criteria.context.Projection
import org.seasar.doma.criteria.context.SelectContext
import org.seasar.doma.internal.jdbc.sql.PreparedSqlBuilder
import org.seasar.doma.jdbc.PreparedSql
import org.seasar.doma.jdbc.SqlKind
import org.seasar.doma.jdbc.SqlLogType
import org.seasar.doma.jdbc.entity.EntityPropertyType
import org.seasar.doma.jdbc.entity.EntityType

class SelectBuilder(
    private val context: SelectContext,
    private val commenter: (String) -> String,
    private val buf: PreparedSqlBuilder,
    aliasManager: AliasManager
) {

    constructor(context: SelectContext, commenter: (String) -> String, logType: SqlLogType) :
            this(context, commenter, PreparedSqlBuilder(context.config, SqlKind.SELECT, logType), AliasManager(context))

    private val config = context.config
    private val support = BuilderSupport(config, commenter, buf, aliasManager)
    fun build(): PreparedSql {
        interpretContext()
        return buf.build(commenter)
    }

    internal fun interpretContext() {
        buf.appendSql("select ")
        if (context.distinct) {
            buf.appendSql("distinct ")
        }
        when (val projection = context.projection) {
            is Projection.All -> {
                context.getProjectionTargets().forEach {
                    it.entityPropertyTypes.forEach { prop ->
                        column(prop)
                        buf.appendSql(", ")
                    }
                }
                buf.cutBackSql(2)
            }
            is Projection.Asterisk -> {
                buf.appendSql("*")
            }
            is Projection.Single -> {
                column(projection.propType)
            }
            is Projection.Pair -> {
                column(projection.first)
                buf.appendSql(", ")
                column(projection.second)
            }
            is Projection.List -> {
                projection.propTypes.forEach {
                    column(it)
                    buf.appendSql(", ")
                }
                buf.cutBackSql(2)
            }
        }
        buf.appendSql(" from ")
        table(context.entityType)
        if (context.joins.isNotEmpty()) {
            context.joins.forEach { join ->
                when (join.kind) {
                    JoinKind.INNER -> buf.appendSql(" inner join ")
                    JoinKind.LEFT -> buf.appendSql(" left outer join ")
                }
                table(join.entityType)
                if (join.on.isNotEmpty()) {
                    buf.appendSql(" on (")
                    join.on.forEachIndexed { index, c ->
                        visitCriterion(index, c)
                        buf.appendSql(" and ")
                    }
                    buf.cutBackSql(5)
                    buf.appendSql(")")
                }
            }
        }
        if (context.where.isNotEmpty()) {
            buf.appendSql(" where ")
            context.where.forEachIndexed { index, c ->
                visitCriterion(index, c)
                buf.appendSql(" and ")
            }
            buf.cutBackSql(5)
        }
        if (context.groupBy.isNotEmpty()) {
            buf.appendSql(" group by ")
            context.groupBy.forEach { p ->
                column(p)
                buf.appendSql(", ")
            }
            buf.cutBackSql(2)
        }
        if (context.having.isNotEmpty()) {
            buf.appendSql(" having ")
            context.having.forEachIndexed { index, c ->
                visitCriterion(index, c)
                buf.appendSql(" and ")
            }
            buf.cutBackSql(5)
        }
        if (context.orderBy.isNotEmpty()) {
            buf.appendSql(" order by ")
            context.orderBy.forEach { (p, sort) ->
                column(p)
                buf.appendSql(" $sort")
                buf.appendSql(", ")
            }
            buf.cutBackSql(2)
        }
        context.limit?.let {
            buf.appendSql(" limit $it")
        }
        context.offset?.let {
            buf.appendSql(" offset $it")
        }
        context.forUpdate?.let {
            buf.appendSql(" for update")
            if (it.nowait) {
                buf.appendSql(" nowait")
            }
        }
    }

    private fun table(entityType: EntityType<*>) {
        support.table(entityType)
    }

    private fun column(propType: EntityPropertyType<*, *>) {
        support.column(propType)
    }

    private fun visitCriterion(index: Int, c: Criterion) {
        support.visitCriterion(index, c)
    }
}
