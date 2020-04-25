package org.seasar.doma.criteria.statement

import org.seasar.doma.criteria.context.DeleteContext
import org.seasar.doma.criteria.declaration.DeleteDeclaration
import org.seasar.doma.criteria.query.DeleteBuilder
import org.seasar.doma.criteria.query.DeleteQuery
import org.seasar.doma.jdbc.Config
import org.seasar.doma.jdbc.PreparedSql
import org.seasar.doma.jdbc.command.Command
import org.seasar.doma.jdbc.command.DeleteCommand
import org.seasar.doma.jdbc.entity.EntityType

class SqlDeleteStatement<ENTITY, ENTITY_TYPE : EntityType<ENTITY>>(
    private val entityTypeProvider: () -> ENTITY_TYPE,
    private val block: DeleteDeclaration.(ENTITY_TYPE) -> Unit
) : AbstractStatement<Int>() {

    override fun commandAndSql(config: Config): Pair<Command<Int>, PreparedSql> {
        val entityType = entityTypeProvider()
        val context = DeleteContext(config, entityType)
        val declaration = DeleteDeclaration(context)
        declaration.block(entityType)
        val builder = DeleteBuilder(context)
        val sql = builder.build()
        val query = DeleteQuery(config, sql)
        val command = DeleteCommand(query)
        return command to sql
    }
}
