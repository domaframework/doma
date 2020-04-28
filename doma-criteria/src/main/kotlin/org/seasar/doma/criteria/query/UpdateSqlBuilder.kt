package org.seasar.doma.criteria.query

import org.seasar.doma.criteria.context.Criterion
import org.seasar.doma.criteria.context.UpdateContext
import org.seasar.doma.def.EntityDef
import org.seasar.doma.def.PropertyDef
import org.seasar.doma.internal.jdbc.sql.PreparedSqlBuilder
import org.seasar.doma.jdbc.InParameter
import org.seasar.doma.jdbc.PreparedSql
import org.seasar.doma.jdbc.SqlKind
import org.seasar.doma.jdbc.SqlLogType

class UpdateSqlBuilder(
    private val context: UpdateContext,
    private val commenter: (String) -> String,
    private val buf: PreparedSqlBuilder,
    aliasManager: AliasManager
) {

    constructor(context: UpdateContext, commenter: (String) -> String, logType: SqlLogType) :
            this(context, commenter, PreparedSqlBuilder(context.config, SqlKind.UPDATE, logType), AliasManager(context))

    private val config = context.config

    private val support = BuilderSupport(config, commenter, buf, aliasManager)

    fun build(): PreparedSql {
        buf.appendSql("update ")
        table(context.entityDef)
        if (context.set.isNotEmpty()) {
            buf.appendSql(" set ")
            context.set.forEach { (prop, param) ->
                column(prop.value)
                buf.appendSql(" = ")
                param(param.value)
                buf.appendSql(", ")
            }
            buf.cutBackSql(2)
        }
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

    private fun column(propDef: PropertyDef<*>) {
        support.column(propDef)
    }

    private fun param(param: InParameter<*>) {
        buf.appendParameter(param)
    }

    private fun visitCriterion(index: Int, c: Criterion) {
        support.visitCriterion(index, c)
    }
}
