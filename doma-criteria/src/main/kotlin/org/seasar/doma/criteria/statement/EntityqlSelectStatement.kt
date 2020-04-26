package org.seasar.doma.criteria.statement

import org.seasar.doma.criteria.command.MultiEntityCommand
import org.seasar.doma.criteria.context.SelectContext
import org.seasar.doma.criteria.declaration.EntityqlSelectDeclaration
import org.seasar.doma.criteria.query.SelectBuilder
import org.seasar.doma.criteria.query.SelectQuery
import org.seasar.doma.jdbc.Config
import org.seasar.doma.jdbc.SqlLogType
import org.seasar.doma.jdbc.command.Command
import org.seasar.doma.jdbc.entity.EntityType

class EntityqlSelectStatement<ENTITY, ENTITY_TYPE : EntityType<ENTITY>>(
    val entityTypeProvider: () -> ENTITY_TYPE,
    private val block: EntityqlSelectDeclaration.(ENTITY_TYPE) -> Unit
) : AbstractStatement<List<ENTITY>>() {

    override fun createCommand(config: Config, commenter: (String) -> String, logType: SqlLogType): Command<List<ENTITY>> {
        val entityType = entityTypeProvider()
        val context = SelectContext(config, entityType)
        val declaration = EntityqlSelectDeclaration(context)
        declaration.block(entityType)
        val builder = SelectBuilder(context, commenter, logType)
        val sql = builder.build()
        val query = SelectQuery(config, sql, javaClass.name, executeMethodName)
        return MultiEntityCommand(context, query)
    }
}
