package org.seasar.doma.criteria.statement

import org.seasar.doma.criteria.context.UpdateContext
import org.seasar.doma.criteria.declaration.UpdateDeclaration
import org.seasar.doma.criteria.query.UpdateQuery
import org.seasar.doma.criteria.query.UpdateSqlBuilder
import org.seasar.doma.jdbc.Config
import org.seasar.doma.jdbc.PreparedSql
import org.seasar.doma.jdbc.command.Command
import org.seasar.doma.jdbc.command.UpdateCommand
import org.seasar.doma.jdbc.entity.EntityType

class SqlUpdateStatement<ENTITY, ENTITY_TYPE : EntityType<ENTITY>>(
    private val entityTypeProvider: () -> ENTITY_TYPE,
    private val block: UpdateDeclaration.(ENTITY_TYPE) -> Unit
) : AbstractStatement<Int>() {

    override fun commandAndSql(config: Config): Pair<Command<Int>, PreparedSql> {
        val entityType = entityTypeProvider()
        val context = UpdateContext(config, entityType)
        val declaration = UpdateDeclaration(context)
        declaration.block(entityType)
        val builder = UpdateSqlBuilder(context)
        val sql = builder.build()
        val query = UpdateQuery(config, sql)
        val command = UpdateCommand(query)
        return command to sql
    }
}
