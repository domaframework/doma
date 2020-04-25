package org.seasar.doma.criteria.statement

import org.seasar.doma.criteria.command.MappedObjectIterationHandler
import org.seasar.doma.criteria.context.SelectContext
import org.seasar.doma.criteria.declaration.SqlSelectDeclaration
import org.seasar.doma.criteria.declaration.SqlSelectResult
import org.seasar.doma.criteria.query.SelectBuilder
import org.seasar.doma.criteria.query.SelectQuery
import org.seasar.doma.jdbc.Config
import org.seasar.doma.jdbc.PreparedSql
import org.seasar.doma.jdbc.command.Command
import org.seasar.doma.jdbc.command.SelectCommand
import org.seasar.doma.jdbc.entity.EntityType

class SqlSelectStatement<ENTITY, ENTITY_TYPE : EntityType<ENTITY>, RESULT>(
    private val entityTypeProvider: () -> ENTITY_TYPE,
    private val block: SqlSelectDeclaration.(ENTITY_TYPE) -> SqlSelectResult<RESULT>
) : AbstractStatement<List<RESULT>>() {

    override fun commandAndSql(config: Config): Pair<Command<List<RESULT>>, PreparedSql> {
        val entityType = entityTypeProvider()
        val context = SelectContext(config, entityType)
        val declaration = SqlSelectDeclaration(context)
        val (propTypes, mapper) = declaration.block(entityType)
        val builder = SelectBuilder(context)
        val sql = builder.build()
        val query = SelectQuery(config, sql)
        val handler = MappedObjectIterationHandler(propTypes, mapper)
        val command = SelectCommand(query, handler)
        return command to sql
    }
}
