package org.seasar.doma.criteria.query

import org.seasar.doma.criteria.context.Criterion
import org.seasar.doma.criteria.context.Operand
import org.seasar.doma.criteria.context.SelectContext
import org.seasar.doma.criteria.declaration.CountAsterisk
import org.seasar.doma.criteria.declaration.SqlFunction
import org.seasar.doma.internal.jdbc.sql.PreparedSqlBuilder
import org.seasar.doma.jdbc.Config
import org.seasar.doma.jdbc.entity.EntityPropertyType
import org.seasar.doma.jdbc.entity.EntityType

class BuilderSupport(
    private val config: Config,
    private val buf: PreparedSqlBuilder,
    private val aliasManager: AliasManager
) {

    fun table(entityType: EntityType<*>) {
        buf.appendSql(entityType.getQualifiedTableName(config.naming::apply, config.dialect::applyQuote))
        buf.appendSql(" ")
        buf.appendSql(aliasManager[entityType])
    }

    fun column(prop: Operand.Prop) {
        column(prop.value)
    }

    fun column(propType: EntityPropertyType<*, *>) {
        fun appendColumn(p: EntityPropertyType<*, *>) {
            buf.appendSql(aliasManager[p])
            buf.appendSql(".")
            buf.appendSql(p.getColumnName(config.naming::apply, config.dialect::applyQuote))
        }

        if (propType is SqlFunction) {
            buf.appendSql(propType.functionName)
            buf.appendSql("(")
            if (propType.argument is CountAsterisk) {
                buf.appendSql("*")
            } else {
                appendColumn(propType.argument)
            }
            buf.appendSql(")")
        } else {
            appendColumn(propType)
        }
    }

    fun param(param: Operand.Param) {
        buf.appendParameter(param.value)
    }

    fun visitCriterion(index: Int, c: Criterion) {
        when (c) {
            is Criterion.Eq -> equality(c.left, c.right, "=")
            is Criterion.Ne -> equality(c.left, c.right, "<>")
            is Criterion.Gt -> comparison(c.left, c.right, ">")
            is Criterion.Ge -> comparison(c.left, c.right, ">=")
            is Criterion.Lt -> comparison(c.left, c.right, "<")
            is Criterion.Le -> comparison(c.left, c.right, "<=")
            is Criterion.IsNull -> isNull(c.prop)
            is Criterion.IsNotNull -> isNotNull(c.prop)
            is Criterion.Like -> like(c.left, c.right)
            is Criterion.NotLike -> like(c.left, c.right, true)
            is Criterion.Between -> between(c.prop, c.begin, c.end)
            is Criterion.InSingle -> inSingle(c.left, c.right)
            is Criterion.NotInSingle -> inSingle(c.left, c.right, true)
            is Criterion.InPair -> inPair(c.left, c.right)
            is Criterion.NotInPair -> inPair(c.left, c.right, true)
            is Criterion.InSingleSubQuery -> inSingleSubQuery(c.left, c.right)
            is Criterion.NotInSingleSubQuery -> inSingleSubQuery(c.left, c.right, true)
            is Criterion.InPairSubQuery -> inPairSubQuery(c.left, c.right)
            is Criterion.NotInPairSubQuery -> inPairSubQuery(c.left, c.right, true)
            is Criterion.Exists -> exists(c.context)
            is Criterion.NotExists -> exists(c.context, true)
            is Criterion.And -> and(c.list, index)
            is Criterion.Or -> or(c.list, index)
            is Criterion.Not -> not(c.list)
        }
    }

    private fun equality(left: Operand.Prop, right: Operand, op: String) {
        fun isValueNull(operand: Operand): Boolean {
            return when (operand) {
                is Operand.Param -> operand.value.wrapper.get() == null
                is Operand.Prop -> false
            }
        }

        if (isValueNull(right)) {
            when (op) {
                "=" -> isNull(left)
                "<>" -> isNotNull(left)
                else -> error("The operator '$op' is illegal.")
            }
        } else {
            comparison(left, right, op)
        }
    }

    private fun comparison(left: Operand.Prop, right: Operand, op: String) {
        column(left)
        buf.appendSql(" $op ")
        when (right) {
            is Operand.Param -> param(right)
            is Operand.Prop -> column(right)
        }
    }

    private fun isNull(prop: Operand.Prop) {
        column(prop)
        buf.appendSql(" is null")
    }

    private fun isNotNull(prop: Operand.Prop) {
        column(prop)
        buf.appendSql(" is not null")
    }

    private fun like(left: Operand.Prop, right: Operand, not: Boolean = false) {
        column(left)
        if (not) {
            buf.appendSql(" not")
        }
        buf.appendSql(" like ")
        when (right) {
            is Operand.Param -> param(right)
            is Operand.Prop -> column(right)
        }
    }

    private fun between(prop: Operand.Prop, begin: Operand.Param, end: Operand.Param) {
        column(prop)
        buf.appendSql(" between ")
        param(begin)
        buf.appendSql(" and ")
        param(end)
    }

    private fun inSingle(left: Operand.Prop, right: List<Operand.Param>, not: Boolean = false) {
        column(left)
        if (not) {
            buf.appendSql(" not")
        }
        buf.appendSql(" in (")
        if (right.isEmpty()) {
            buf.appendSql("null")
        } else {
            right.forEach {
                param(it)
                buf.appendSql(", ")
            }
            buf.cutBackSql(2)
        }
        buf.appendSql(")")
    }

    private fun inPair(left: Pair<Operand.Prop, Operand.Prop>, right: List<Pair<Operand.Param, Operand.Param>>, not: Boolean = false) {
        buf.appendSql("(")
        column(left.first)
        buf.appendSql(", ")
        column(left.second)
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
                param(it.first)
                buf.appendSql(", ")
                param(it.second)
                buf.appendSql("), ")
            }
            buf.cutBackSql(2)
        }
        buf.appendSql(")")
    }

    private fun inSingleSubQuery(left: Operand.Prop, right: SelectContext, not: Boolean = false) {
        column(left)
        if (not) {
            buf.appendSql(" not")
        }
        buf.appendSql(" in (")
        val parentAliasManager = AliasManager(right, aliasManager)
        val builder = SelectBuilder(right, buf, parentAliasManager)
        builder.interpretContext()
        buf.appendSql(")")
    }

    private fun inPairSubQuery(left: Pair<Operand.Prop, Operand.Prop>, right: SelectContext, not: Boolean = false) {
        val (prop1, prop2) = left
        buf.appendSql("(")
        column(prop1)
        buf.appendSql(", ")
        column(prop2)
        buf.appendSql(")")
        if (not) {
            buf.appendSql(" not")
        }
        buf.appendSql(" in (")
        val parentAliasManager = AliasManager(right, aliasManager)
        val builder = SelectBuilder(right, buf, parentAliasManager)
        builder.interpretContext()
        buf.appendSql(")")
    }

    private fun exists(selectContext: SelectContext, not: Boolean = false) {
        if (not) {
            buf.appendSql("not ")
        }
        buf.appendSql("exists (")
        val parentAliasManager = AliasManager(selectContext, aliasManager)
        val builder = SelectBuilder(selectContext, buf, parentAliasManager)
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

    private fun not(list: List<Criterion>) {
        if (list.isNotEmpty()) {
            buf.appendSql(" not ")
            buf.appendSql("(")
            list.forEachIndexed(::visitCriterion)
            buf.appendSql(")")
        }
    }
}
