package org.seasar.doma.criteria.query

import org.seasar.doma.criteria.InsertContext
import org.seasar.doma.internal.jdbc.sql.PreparedSqlBuilder
import org.seasar.doma.jdbc.InParameter
import org.seasar.doma.jdbc.PreparedSql
import org.seasar.doma.jdbc.SqlKind
import org.seasar.doma.jdbc.SqlLogType
import org.seasar.doma.jdbc.entity.EntityPropertyType
import org.seasar.doma.jdbc.entity.EntityType

class InsertBuilder(
    private val context: InsertContext,
        // TODO the SqlLogType value should be passed from the caller
    private val buf: PreparedSqlBuilder = PreparedSqlBuilder(context.config, SqlKind.INSERT, SqlLogType.FORMATTED)
) {

    private val config = context.config

    fun build(): PreparedSql {
        buf.appendSql("insert into ")
        table(context.entityType)
        if (context.values.isNotEmpty()) {
            buf.appendSql(" (")
            context.values.forEach { (key, _) ->
                column(key.value)
                buf.appendSql(", ")
            }
            buf.cutBackSql(2)
            buf.appendSql(") values (")
            context.values.forEach { (_, value) ->
                param(value.value)
                buf.appendSql(", ")
            }
            buf.cutBackSql(2)
            buf.appendSql(")")
        }
        // TODO Use config.commenter
        return buf.build { it }
    }

    private fun table(entityType: EntityType<*>) {
        buf.appendSql(entityType.getQualifiedTableName(config.naming::apply, config.dialect::applyQuote))
    }

    private fun column(propType: EntityPropertyType<*, *>) {
        buf.appendSql(propType.getColumnName(config.naming::apply, config.dialect::applyQuote))
    }

    private fun param(param: InParameter<*>) {
        buf.appendParameter(param)
    }
}
