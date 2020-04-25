package org.seasar.doma.criteria

import org.seasar.doma.criteria.command.MultiEntityCommand
import org.seasar.doma.criteria.query.SelectBuilder
import org.seasar.doma.criteria.query.SelectQuery
import org.seasar.doma.jdbc.Config
import org.seasar.doma.jdbc.PreparedSql
import org.seasar.doma.jdbc.command.Command
import org.seasar.doma.jdbc.entity.EntityType

class EntityqlStatement<ENTITY, ENTITY_TYPE : EntityType<ENTITY>>(
    val entityTypeProvider: () -> ENTITY_TYPE,
    private val block: EntityqlSelectDeclaration.(ENTITY_TYPE) -> Unit
) : AbstractStatement<List<ENTITY>>() {

    override fun commandAndSql(config: Config): Pair<Command<List<ENTITY>>, PreparedSql> {
        val entityType = entityTypeProvider()
        val context = SelectContext(config, entityType)
        val declaration = EntityqlSelectDeclaration(context)
        declaration.block(entityType)
        val builder = SelectBuilder(context)
        val sql = builder.build()
        val query = SelectQuery(config, sql)
        val command = MultiEntityCommand<ENTITY>(context, query)
        return command to sql
    }
}
