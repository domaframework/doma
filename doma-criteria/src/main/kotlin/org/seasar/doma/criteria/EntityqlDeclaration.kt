package org.seasar.doma.criteria

import org.seasar.doma.criteria.command.MultiEntityCommand
import org.seasar.doma.criteria.command.MultiEntityQuery
import org.seasar.doma.criteria.command.MultiEntitySqlBuilder
import org.seasar.doma.jdbc.Config
import org.seasar.doma.jdbc.PreparedSql
import org.seasar.doma.jdbc.Sql
import org.seasar.doma.jdbc.entity.EntityType

fun <ENTITY, ENTITY_TYPE : EntityType<ENTITY>> entityql(
    block: EntityqlDeclaration.() -> EntityqlStatement<ENTITY, ENTITY_TYPE>
): EntityqlStatement<ENTITY, ENTITY_TYPE> {
    return EntityqlDeclaration().block()
}

@Declaration
class EntityqlDeclaration {
    fun <ENTITY, ENTITY_TYPE : EntityType<ENTITY>> from(
        from: () -> ENTITY_TYPE,
        block: EntityqlFromDeclaration.(ENTITY_TYPE) -> Unit
    ): EntityqlStatement<ENTITY, ENTITY_TYPE> {
        return EntityqlStatement(from, block)
    }
}

@Declaration
class EntityqlFromDeclaration(_selectContext: SelectContext) : FromDeclaration(_selectContext) {
    @Suppress("UNCHECKED_CAST")
    fun <ENTITY, ENTITY2> associate(type1: EntityType<ENTITY>, type2: EntityType<ENTITY2>, associator: (ENTITY, ENTITY2) -> Unit) {
        // TODO check arguments
        _selectContext.associations[type1 to type2] = associator as (Any, Any) -> Unit
    }
}

class EntityqlStatement<ENTITY, ENTITY_TYPE : EntityType<ENTITY>>(
    val from: () -> ENTITY_TYPE,
    private val block: EntityqlFromDeclaration.(ENTITY_TYPE) -> Unit
) {

    fun execute(config: Config): List<ENTITY> {
        val (context, sql) = buildContextAndSql(config)
        val query = MultiEntityQuery(config, sql)
        val command = MultiEntityCommand<ENTITY>(context, query)
        return command.execute()
    }

    fun asSql(config: Config): Sql<*> {
        val (_, sql) = buildContextAndSql(config)
        return sql
    }

    private fun buildContextAndSql(config: Config): Pair<SelectContext, PreparedSql> {
        val entityType = from()
        val context = SelectContext(config, entityType)
        val declaration = EntityqlFromDeclaration(context)
        declaration.block(entityType)
        val builder = MultiEntitySqlBuilder(context)
        return context to builder.build()
    }
}
