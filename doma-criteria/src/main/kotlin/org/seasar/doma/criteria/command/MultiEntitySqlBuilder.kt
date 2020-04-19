package org.seasar.doma.criteria.command

import org.seasar.doma.criteria.Criterion
import org.seasar.doma.criteria.JoinKind
import org.seasar.doma.criteria.Operand
import org.seasar.doma.criteria.SelectContext
import org.seasar.doma.internal.jdbc.sql.BasicInParameter
import org.seasar.doma.internal.jdbc.sql.PreparedSqlBuilder
import org.seasar.doma.jdbc.Config
import org.seasar.doma.jdbc.PreparedSql
import org.seasar.doma.jdbc.SqlKind
import org.seasar.doma.jdbc.SqlLogType
import org.seasar.doma.jdbc.entity.EntityPropertyType
import org.seasar.doma.jdbc.entity.EntityType

class MultiEntitySqlBuilder(private val config: Config, private val selectContext: SelectContext) {
    private val naming = config.naming
    private val dialect = config.dialect
    private val commenter = config.commenter
    private val buf = PreparedSqlBuilder(config, SqlKind.SELECT, SqlLogType.FORMATTED)
    private val entityAliasMap = mutableMapOf<EntityType<*>, String>()
    private val propAliasMap = mutableMapOf<EntityPropertyType<*, *>, String>()
    private val projectionTargets = selectContext.getProjectionTargets()

    init {
        val entityTypes = listOf(selectContext.entityType) + selectContext.joins.map { it.entityType }
        entityTypes.forEachIndexed { index, entityType ->
            val alias = "t${index}_"
            entityAliasMap[entityType] = alias
            entityType.entityPropertyTypes.forEach {
                propAliasMap[it] = alias
            }
        }
    }

    fun build(): PreparedSql {
        buf.appendSql("select ")
        projectionTargets.forEach {
            it.entityPropertyTypes.forEach { prop ->
                column(prop)
                buf.appendSql(", ")
            }
        }
        buf.cutBackSql(2)
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
        return buf.build { it }
    }

    private fun table(entityType: EntityType<*>) {
        buf.appendSql(entityType.getQualifiedTableName(naming::apply, dialect::applyQuote))
        buf.appendSql(" ")
        buf.appendSql(entityAliasMap[entityType])
    }

    private fun column(prop: EntityPropertyType<*, *>) {
        buf.appendSql(propAliasMap[prop])
        buf.appendSql(".")
        buf.appendSql(prop.getColumnName(naming::apply, dialect::applyQuote))
    }

    private fun param(param: BasicInParameter<*>) {
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
            is Criterion.In -> `in`(c.left, c.right)
            is Criterion.Between -> between(c.prop, c.begin, c.end)
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

    private fun `in`(left: Operand.Prop, right: List<Operand.Param>) {
        column(left.value)
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

    private fun between(prop: Operand.Prop, begin: Operand.Param, end: Operand.Param) {
        column(prop.value)
        buf.appendSql(" between ")
        param(begin.value)
        buf.appendSql(" and ")
        param(end.value)
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
