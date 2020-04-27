package org.seasar.doma.criteria.statement

import org.seasar.doma.criteria.context.InsertContext
import org.seasar.doma.criteria.declaration.InsertDeclaration
import org.seasar.doma.criteria.query.InsertBuilder
import org.seasar.doma.criteria.query.InsertQuery
import org.seasar.doma.def.EntityDef
import org.seasar.doma.jdbc.Config
import org.seasar.doma.jdbc.SqlLogType
import org.seasar.doma.jdbc.command.Command
import org.seasar.doma.jdbc.command.InsertCommand

class SqlInsertStatement<ENTITY, ENTITY_DEF : EntityDef<ENTITY>>(
    private val entityDefProvider: () -> ENTITY_DEF,
    private val block: InsertDeclaration.(ENTITY_DEF) -> Unit
) : AbstractStatement<Int>() {

    override fun createCommand(config: Config, commenter: (String) -> String, logType: SqlLogType): Command<Int> {
        val entityDef = entityDefProvider()
        val context = InsertContext(config, entityDef.asType())
        val declaration = InsertDeclaration(context)
        declaration.block(entityDef)
        val builder = InsertBuilder(context, commenter, logType)
        val sql = builder.build()
        val query = InsertQuery(config, sql, javaClass.name, executeMethodName)
        return InsertCommand(query)
    }
}
