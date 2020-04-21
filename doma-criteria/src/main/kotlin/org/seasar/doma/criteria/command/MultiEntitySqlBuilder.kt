package org.seasar.doma.criteria.command

import org.seasar.doma.criteria.Criterion
import org.seasar.doma.criteria.JoinKind
import org.seasar.doma.criteria.Operand
import org.seasar.doma.criteria.Projection
import org.seasar.doma.criteria.SelectContext
import org.seasar.doma.internal.jdbc.sql.PreparedSqlBuilder
import org.seasar.doma.jdbc.Config
import org.seasar.doma.jdbc.InParameter
import org.seasar.doma.jdbc.PreparedSql
import org.seasar.doma.jdbc.SqlKind
import org.seasar.doma.jdbc.SqlLogType
import org.seasar.doma.jdbc.entity.EntityPropertyType
import org.seasar.doma.jdbc.entity.EntityType

class MultiEntitySqlBuilder(
    private val config: Config,
    private val selectContext: SelectContext,
    private val buf: PreparedSqlBuilder = PreparedSqlBuilder(config, SqlKind.SELECT, SqlLogType.FORMATTED),
    private val aliasManager: AliasManager = AliasManager(selectContext)
) {

    fun build(): PreparedSql {
        interpretContext()
        return buf.build { it }
    }

    private fun interpretContext() {
        buf.appendSql("select ")
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
        if (selectContext.orderBy.isNotEmpty()) {
            buf.appendSql(" order by ")
            selectContext.orderBy.forEach { (p, sort) ->
                column(p)
                buf.appendSql(" $sort")
                buf.appendSql(", ")
            }
            buf.cutBackSql(2)
        }
    }

    private fun table(entityType: EntityType<*>) {
        buf.appendSql(entityType.getQualifiedTableName(config.naming::apply, config.dialect::applyQuote))
        buf.appendSql(" ")
        buf.appendSql(aliasManager[entityType])
    }

    private fun column(prop: EntityPropertyType<*, *>) {
        buf.appendSql(aliasManager[prop])
        buf.appendSql(".")
        buf.appendSql(prop.getColumnName(config.naming::apply, config.dialect::applyQuote))
    }

    private fun param(param: InParameter<*>) {
        buf.appendParameter(param)
    }

    private fun visitCriterion(index: Int, c: Criterion) {
        when (c) {
            is Criterion.Eq -> comparison(c.left, c.right, "=", "is null")
            is Criterion.Ne -> comparison(c.left, c.right, "<>", "is not null")
            is Criterion.Gt -> comparison(c.left, c.right, ">")
            is Criterion.Ge -> comparison(c.left, c.right, ">=")
            is Criterion.Lt -> comparison(c.left, c.right, "<")
            is Criterion.Le -> comparison(c.left, c.right, "<=")
            is Criterion.InSingle -> inSingle(c.left, c.right)
            is Criterion.InPair -> inPair(c.left, c.right)
            is Criterion.InSelectSingle -> inSelectSingle(c.left, c.right)
            is Criterion.InSelectPair -> inSelectPair(c.left, c.right)
            is Criterion.NotInSingle -> inSingle(c.left, c.right, true)
            is Criterion.NotInPair -> inPair(c.left, c.right, true)
            is Criterion.NotInSelectSingle -> inSelectSingle(c.left, c.right, true)
            is Criterion.NotInSelectPair -> inSelectPair(c.left, c.right, true)
            is Criterion.Between -> between(c.prop, c.begin, c.end)
            is Criterion.Exists -> exists(c.context)
            is Criterion.NotExists -> exists(c.context, true)
            is Criterion.And -> and(c.list, index)
            is Criterion.Or -> or(c.list, index)
            is Criterion.Not -> not(c.list, index)
        }
    }

    private fun comparison(left: Operand.Prop, right: Operand, op: String, nullOp: String? = null) {
        fun isRightValueNull(): Boolean {
            return when (right) {
                is Operand.Param -> right.value.wrapper.get() == null
                is Operand.Prop -> false
            }
        }

        column(left.value)
        if (nullOp != null && isRightValueNull()) {
            buf.appendSql(" $nullOp")
        } else {
            buf.appendSql(" $op ")
            when (right) {
                is Operand.Param -> param(right.value)
                is Operand.Prop -> column(right.value)
            }
        }
    }

    private fun inSingle(left: Operand.Prop, right: List<Operand.Param>, not: Boolean = false) {
        column(left.value)
        if (not) {
            buf.appendSql(" not")
        }
        buf.appendSql(" in (")
        if (right.isEmpty()) {
            buf.appendSql("null")
        } else {
            right.forEach {
                param(it.value)
                buf.appendSql(", ")
            }
            buf.cutBackSql(2)
        }
        buf.appendSql(")")
    }

    private fun inPair(left: Pair<Operand.Prop, Operand.Prop>, right: List<Pair<Operand.Param, Operand.Param>>, not: Boolean = false) {
        buf.appendSql("(")
        column(left.first.value)
        buf.appendSql(", ")
        column(left.second.value)
        buf.appendSql(")")
        if (not) {
            buf.appendSql(" not")
        }
        buf.appendSql(" in (")
        if (right.isEmpty()) {
            buf.appendSql("null, null")
        } else {
            right.forEach {
                buf.appendSql("(")
                param(it.first.value)
                buf.appendSql(", ")
                param(it.second.value)
                buf.appendSql("), ")
            }
            buf.cutBackSql(2)
        }
        buf.appendSql(")")
    }

    private fun inSelectSingle(left: Operand.Prop, right: SelectContext, not: Boolean = false) {
        column(left.value)
        if (not) {
            buf.appendSql(" not")
        }
        buf.appendSql(" in (")
        val parentAliasManager = AliasManager(right, aliasManager)
        val builder = MultiEntitySqlBuilder(config, right, buf, parentAliasManager)
        builder.interpretContext()
        buf.appendSql(")")
    }

    private fun inSelectPair(left: Pair<Operand.Prop, Operand.Prop>, right: SelectContext, not: Boolean = false) {
        val (prop1, prop2) = left
        buf.appendSql("(")
        column(prop1.value)
        buf.appendSql(", ")
        column(prop2.value)
        buf.appendSql(")")
        if (not) {
            buf.appendSql(" not")
        }
        buf.appendSql(" in (")
        val parentAliasManager = AliasManager(right, aliasManager)
        val builder = MultiEntitySqlBuilder(config, right, buf, parentAliasManager)
        builder.interpretContext()
        buf.appendSql(")")
    }

    private fun between(prop: Operand.Prop, begin: Operand.Param, end: Operand.Param) {
        column(prop.value)
        buf.appendSql(" between ")
        param(begin.value)
        buf.appendSql(" and ")
        param(end.value)
    }

    private fun exists(selectContext: SelectContext, not: Boolean = false) {
        if (not) {
            buf.appendSql("not ")
        }
        buf.appendSql("exists (")
        val parentAliasManager = AliasManager(selectContext, aliasManager)
        val builder = MultiEntitySqlBuilder(config, selectContext, buf, parentAliasManager)
        builder.interpretContext()
        buf.appendSql(")")
    }

    private fun and(list: List<Criterion>, index: Int) {
        binaryLogicalOperator(list, index, "and")
    }

    private fun or(list: List<Criterion>, index: Int) {
        binaryLogicalOperator(list, index, "or")
    }

    private fun binaryLogicalOperator(list: List<Criterion>, index: Int, operator: String) {
        if (list.isNotEmpty()) {
            if (index > 0) {
                buf.cutBackSql(5)
            }
            buf.appendSql(" $operator ")
            buf.appendSql("(")
            list.forEachIndexed(::visitCriterion)
            buf.appendSql(")")
        }
    }

    private fun not(list: List<Criterion>, index: Int) {
        if (list.isNotEmpty()) {
            buf.appendSql(" not ")
            buf.appendSql("(")
            list.forEachIndexed(::visitCriterion)
            buf.appendSql(")")
        }
    }
}
