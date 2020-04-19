package org.seasar.doma.criteria

import org.seasar.doma.criteria.command.MultiEntityCommand
import org.seasar.doma.criteria.command.MultiEntityQuery
import org.seasar.doma.criteria.command.MultiEntitySqlBuilder
import org.seasar.doma.jdbc.Config
import org.seasar.doma.jdbc.PreparedSql
import org.seasar.doma.jdbc.entity.EntityType

fun <ENTITY, ENTITY_TYPE : EntityType<ENTITY>> select(
    provider: () -> ENTITY_TYPE,
    block: SelectDeclaration.(ENTITY_TYPE) -> Unit
): SelectStatement<ENTITY, ENTITY_TYPE> {
    return SelectStatement(provider, block)
}

infix operator fun <ENTITY, ENTITY_TYPE : EntityType<ENTITY>>
        (SelectDeclaration.(ENTITY_TYPE) -> Unit).plus(other: SelectDeclaration.(ENTITY_TYPE) -> Unit): SelectDeclaration.(ENTITY_TYPE) -> Unit {
    val self = this
    return {
        self(it)
        other(it)
    }
}

class SelectStatement<ENTITY, ENTITY_TYPE : EntityType<ENTITY>>(
    val provider: () -> ENTITY_TYPE,
    private val block: SelectDeclaration.(ENTITY_TYPE) -> Unit
) {

    fun buildContextAndSql(config: Config): Pair<SelectContext, PreparedSql> {
        val entityType = provider()
        val context = SelectContext(entityType)
        val declaration = SelectDeclaration(context)
        declaration.block(entityType)
        val builder = MultiEntitySqlBuilder(config, context)
        return context to builder.build()
    }

    fun execute(config: Config): List<ENTITY> {
        val (context, sql) = buildContextAndSql(config)
        val query = MultiEntityQuery(config, sql)
        val command = MultiEntityCommand<ENTITY>(context, query)
        return command.execute()
    }

    operator fun plus(other: SelectStatement<ENTITY, ENTITY_TYPE>): SelectStatement<ENTITY, ENTITY_TYPE> {
        return SelectStatement(this.provider, this.block + other.block)
    }
}
