package org.seasar.doma.criteria.query

import org.seasar.doma.criteria.context.InsertContext
import org.seasar.doma.criteria.context.Operand
import org.seasar.doma.def.EntityDef
import org.seasar.doma.internal.jdbc.sql.PreparedSqlBuilder
import org.seasar.doma.jdbc.PreparedSql
import org.seasar.doma.jdbc.SqlKind
import org.seasar.doma.jdbc.SqlLogType

class InsertBuilder(
    private val context: InsertContext,
    private val commenter: (String) -> String,
    private val buf: PreparedSqlBuilder
) {

    constructor(context: InsertContext, commenter: (String) -> String, logType: SqlLogType) :
            this(context, commenter, PreparedSqlBuilder(context.config, SqlKind.INSERT, logType))

    private val config = context.config

    fun build(): PreparedSql {
        buf.appendSql("insert into ")
        table(context.entityDef)
        if (context.values.isNotEmpty()) {
            buf.appendSql(" (")
            context.values.forEach { (key, _) ->
                column(key)
                buf.appendSql(", ")
            }
            buf.cutBackSql(2)
            buf.appendSql(") values (")
            context.values.forEach { (_, value) ->
                param(value)
                buf.appendSql(", ")
            }
            buf.cutBackSql(2)
            buf.appendSql(")")
        }
        return buf.build(commenter)
    }

    private fun table(entityDef: EntityDef<*>) {
        val entityType = entityDef.asType()
        buf.appendSql(entityType.getQualifiedTableName(config.naming::apply, config.dialect::applyQuote))
    }

    private fun column(prop: Operand.Prop) {
        val propType = prop.value.asType()
        buf.appendSql(propType.getColumnName(config.naming::apply, config.dialect::applyQuote))
    }

    private fun param(param: Operand.Param) {
        buf.appendParameter(param.value)
    }
}
