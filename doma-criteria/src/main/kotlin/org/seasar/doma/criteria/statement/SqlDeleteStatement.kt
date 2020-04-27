package org.seasar.doma.criteria.statement

import org.seasar.doma.criteria.context.DeleteContext
import org.seasar.doma.criteria.declaration.DeleteDeclaration
import org.seasar.doma.criteria.query.DeleteBuilder
import org.seasar.doma.criteria.query.DeleteQuery
import org.seasar.doma.def.EntityDef
import org.seasar.doma.jdbc.Config
import org.seasar.doma.jdbc.SqlLogType
import org.seasar.doma.jdbc.command.Command
import org.seasar.doma.jdbc.command.DeleteCommand

class SqlDeleteStatement<ENTITY, ENTITY_DEF : EntityDef<ENTITY>>(
    private val entityDefProvider: () -> ENTITY_DEF,
    private val block: DeleteDeclaration.(ENTITY_DEF) -> Unit
) : AbstractStatement<Int>() {

    override fun createCommand(config: Config, commenter: (String) -> String, logType: SqlLogType): Command<Int> {
        val entityDef = entityDefProvider()
        val context = DeleteContext(config, entityDef.asType())
        val declaration = DeleteDeclaration(context)
        declaration.block(entityDef)
        val builder = DeleteBuilder(context, commenter, logType)
        val sql = builder.build()
        val query = DeleteQuery(config, sql, javaClass.name, executeMethodName)
        return DeleteCommand(query)
    }
}
