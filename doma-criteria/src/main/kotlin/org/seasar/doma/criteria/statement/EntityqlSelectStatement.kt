package org.seasar.doma.criteria.statement

import org.seasar.doma.criteria.command.MultiEntityCommand
import org.seasar.doma.criteria.context.SelectContext
import org.seasar.doma.criteria.declaration.EntityqlSelectDeclaration
import org.seasar.doma.criteria.query.SelectBuilder
import org.seasar.doma.criteria.query.SelectQuery
import org.seasar.doma.def.EntityDef
import org.seasar.doma.jdbc.Config
import org.seasar.doma.jdbc.SqlLogType
import org.seasar.doma.jdbc.command.Command

class EntityqlSelectStatement<ENTITY, ENTITY_DEF : EntityDef<ENTITY>>(
    val entityDefProvider: () -> ENTITY_DEF,
    private val block: EntityqlSelectDeclaration.(ENTITY_DEF) -> Unit
) : AbstractStatement<List<ENTITY>>() {

    override fun createCommand(config: Config, commenter: (String) -> String, logType: SqlLogType): Command<List<ENTITY>> {
        val entityDef = entityDefProvider()
        val context = SelectContext(config, entityDef)
        val declaration = EntityqlSelectDeclaration(context)
        declaration.block(entityDef)
        val builder = SelectBuilder(context, commenter, logType)
        val sql = builder.build()
        val query = SelectQuery(config, sql, javaClass.name, executeMethodName)
        return MultiEntityCommand(context, query)
    }
}
