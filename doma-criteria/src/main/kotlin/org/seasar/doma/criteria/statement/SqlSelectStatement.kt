package org.seasar.doma.criteria.statement

import org.seasar.doma.criteria.command.MappedObjectIterationHandler
import org.seasar.doma.criteria.context.SelectContext
import org.seasar.doma.criteria.declaration.SqlSelectDeclaration
import org.seasar.doma.criteria.declaration.SqlSelectResult
import org.seasar.doma.criteria.query.SelectBuilder
import org.seasar.doma.criteria.query.SelectQuery
import org.seasar.doma.jdbc.Config
import org.seasar.doma.jdbc.SqlLogType
import org.seasar.doma.jdbc.command.Command
import org.seasar.doma.jdbc.command.SelectCommand
import org.seasar.doma.jdbc.entity.EntityType

class SqlSelectStatement<ENTITY, ENTITY_TYPE : EntityType<ENTITY>, RESULT_ELEMENT>(
    private val entityTypeProvider: () -> ENTITY_TYPE,
    private val block: SqlSelectDeclaration.(ENTITY_TYPE) -> SqlSelectResult<RESULT_ELEMENT>
) : AbstractStatement<List<RESULT_ELEMENT>>() {

    override fun createCommand(config: Config, commenter: (String) -> String, logType: SqlLogType): Command<List<RESULT_ELEMENT>> {
        val (context, propTypes, mapper) = runSelectDeclaration(config)
        val builder = SelectBuilder(context, commenter, logType)
        val sql = builder.build()
        val query = SelectQuery(config, sql, javaClass.name, executeMethodName)
        val handler = MappedObjectIterationHandler(propTypes, mapper)
        return SelectCommand(query, handler)
    }

    internal fun runSelectDeclaration(config: Config): SqlSelectResult<RESULT_ELEMENT> {
        val entityType = entityTypeProvider()
        val context = SelectContext(config, entityType)
        val declaration = SqlSelectDeclaration(context)
        return declaration.block(entityType)
    }
}
