package org.seasar.doma.criteria.query

import org.seasar.doma.criteria.Criterion
import org.seasar.doma.criteria.JoinKind
import org.seasar.doma.criteria.Projection
import org.seasar.doma.criteria.SelectContext
import org.seasar.doma.internal.jdbc.sql.PreparedSqlBuilder
import org.seasar.doma.jdbc.PreparedSql
import org.seasar.doma.jdbc.SqlKind
import org.seasar.doma.jdbc.SqlLogType
import org.seasar.doma.jdbc.entity.EntityPropertyType
import org.seasar.doma.jdbc.entity.EntityType

class SelectBuilder(
    private val selectContext: SelectContext,
        // TODO the SqlLogType value should be passed from the caller
    private val buf: PreparedSqlBuilder = PreparedSqlBuilder(selectContext.config, SqlKind.SELECT, SqlLogType.FORMATTED),
    private val aliasManager: AliasManager = AliasManager(selectContext)
) {

    private val config = selectContext.config
    private val support = BuilderSupport(config, buf, aliasManager)
    fun build(): PreparedSql {
        interpretContext()
        // TODO Use config.commenter
        return buf.build { it }
    }

    internal fun interpretContext() {
        buf.appendSql("select ")
        if (selectContext.distinct) {
            buf.appendSql("distinct ")
        }
        when (val projection = selectContext.projection) {
            is Projection.Default -> {
                selectContext.getProjectionTargets().forEach {
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
        table(selectContext.entityType)
        if (selectContext.joins.isNotEmpty()) {
            selectContext.joins.forEach { join ->
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
        if (selectContext.where.isNotEmpty()) {
            buf.appendSql(" where ")
            selectContext.where.forEachIndexed { index, c ->
                visitCriterion(index, c)
                buf.appendSql(" and ")
            }
            buf.cutBackSql(5)
        }
        if (selectContext.groupBy.isNotEmpty()) {
            buf.appendSql(" group by ")
            selectContext.groupBy.forEach { p ->
                column(p)
                buf.appendSql(", ")
            }
            buf.cutBackSql(2)
        }
        if (selectContext.having.isNotEmpty()) {
            buf.appendSql(" having ")
            selectContext.having.forEachIndexed { index, c ->
                visitCriterion(index, c)
                buf.appendSql(" and ")
            }
            buf.cutBackSql(5)
        }
        if (selectContext.orderBy.isNotEmpty()) {
            buf.appendSql(" order by ")
            selectContext.orderBy.forEach { (p, sort) ->
                column(p)
                buf.appendSql(" $sort")
                buf.appendSql(", ")
            }
            buf.cutBackSql(2)
        }
        selectContext.limit?.let {
            buf.appendSql(" limit $it")
        }
        selectContext.offset?.let {
            buf.appendSql(" offset $it")
        }
        selectContext.forUpdate?.let {
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
