package org.seasar.doma.criteria.statement

import org.seasar.doma.criteria.context.InsertContext
import org.seasar.doma.criteria.declaration.InsertDeclaration
import org.seasar.doma.criteria.query.InsertBuilder
import org.seasar.doma.criteria.query.InsertQuery
import org.seasar.doma.jdbc.Config
import org.seasar.doma.jdbc.PreparedSql
import org.seasar.doma.jdbc.SqlLogType
import org.seasar.doma.jdbc.command.Command
import org.seasar.doma.jdbc.command.InsertCommand
import org.seasar.doma.jdbc.entity.EntityType

class SqlInsertStatement<ENTITY, ENTITY_TYPE : EntityType<ENTITY>>(
    private val entityTypeProvider: () -> ENTITY_TYPE,
    private val block: InsertDeclaration.(ENTITY_TYPE) -> Unit
) : AbstractStatement<Int>() {

    override fun commandAndSql(config: Config, commenter: (String) -> String, logType: SqlLogType): Pair<Command<Int>, PreparedSql> {
        val entityType = entityTypeProvider()
        val context = InsertContext(config, entityType)
        val declaration = InsertDeclaration(context)
        declaration.block(entityType)
        val builder = InsertBuilder(context, commenter, logType)
        val sql = builder.build()
        val query = InsertQuery(config, sql, javaClass.name, executeMethodName)
        val command = InsertCommand(query)
        return command to sql
    }
}
