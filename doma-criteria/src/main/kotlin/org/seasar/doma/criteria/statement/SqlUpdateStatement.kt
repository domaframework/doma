package org.seasar.doma.criteria.statement

import org.seasar.doma.criteria.context.UpdateContext
import org.seasar.doma.criteria.declaration.UpdateDeclaration
import org.seasar.doma.criteria.query.UpdateQuery
import org.seasar.doma.criteria.query.UpdateSqlBuilder
import org.seasar.doma.def.EntityDef
import org.seasar.doma.jdbc.Config
import org.seasar.doma.jdbc.SqlLogType
import org.seasar.doma.jdbc.command.Command
import org.seasar.doma.jdbc.command.UpdateCommand

class SqlUpdateStatement<ENTITY, ENTITY_DEF : EntityDef<ENTITY>>(
    private val entityDefProvider: () -> ENTITY_DEF,
    private val block: UpdateDeclaration.(ENTITY_DEF) -> Unit
) : AbstractStatement<Int>() {

    override fun createCommand(config: Config, commenter: (String) -> String, logType: SqlLogType): Command<Int> {
        val entityDef = entityDefProvider()
        val context = UpdateContext(config, entityDef)
        val declaration = UpdateDeclaration(context)
        declaration.block(entityDef)
        val builder = UpdateSqlBuilder(context, commenter, logType)
        val sql = builder.build()
        val query = UpdateQuery(config, sql, javaClass.name, executeMethodName)
        return UpdateCommand(query)
    }
}
