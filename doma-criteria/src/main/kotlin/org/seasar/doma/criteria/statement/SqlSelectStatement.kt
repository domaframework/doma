package org.seasar.doma.criteria.statement

import org.seasar.doma.criteria.command.MappedObjectIterationHandler
import org.seasar.doma.criteria.context.SelectContext
import org.seasar.doma.criteria.declaration.SqlSelectDeclaration
import org.seasar.doma.criteria.declaration.SqlSelectResult
import org.seasar.doma.criteria.query.SelectBuilder
import org.seasar.doma.criteria.query.SelectQuery
import org.seasar.doma.def.EntityDef
import org.seasar.doma.jdbc.Config
import org.seasar.doma.jdbc.SqlLogType
import org.seasar.doma.jdbc.command.Command
import org.seasar.doma.jdbc.command.SelectCommand

class SqlSelectStatement<ENTITY, ENTITY_DEF : EntityDef<ENTITY>, RESULT_ELEMENT>(
    private val entityDefProvider: () -> ENTITY_DEF,
    private val block: SqlSelectDeclaration.(ENTITY_DEF) -> SqlSelectResult<RESULT_ELEMENT>
) : AbstractStatement<List<RESULT_ELEMENT>>() {

    override fun createCommand(config: Config, commenter: (String) -> String, logType: SqlLogType): Command<List<RESULT_ELEMENT>> {
        val (context, propDefs, mapper) = runSelectDeclaration(config)
        val builder = SelectBuilder(context, commenter, logType)
        val sql = builder.build()
        val query = SelectQuery(config, sql, javaClass.name, executeMethodName)
        val handler = MappedObjectIterationHandler(propDefs, mapper)
        return SelectCommand(query, handler)
    }

    internal fun runSelectDeclaration(config: Config): SqlSelectResult<RESULT_ELEMENT> {
        val entityDef = entityDefProvider()
        val context = SelectContext(config, entityDef)
        val declaration = SqlSelectDeclaration(context)
        return declaration.block(entityDef)
    }
}
