package org.seasar.doma.criteria

import org.seasar.doma.criteria.command.MultiEntityCommand
import org.seasar.doma.criteria.command.MultiEntityQuery
import org.seasar.doma.criteria.command.MultiEntitySqlBuilder
import org.seasar.doma.jdbc.Config
import org.seasar.doma.jdbc.PreparedSql
import org.seasar.doma.jdbc.entity.EntityType

fun <ENTITY, ENTITY_TYPE : EntityType<ENTITY>> select(
    from: () -> ENTITY_TYPE,
    block: AssociableDeclaration.(ENTITY_TYPE) -> Unit
): SelectStatement<ENTITY, ENTITY_TYPE> {
    return SelectStatement(from, block)
}

infix operator fun <ENTITY, ENTITY_TYPE : EntityType<ENTITY>>
        (AssociableDeclaration.(ENTITY_TYPE) -> Unit).plus(other: AssociableDeclaration.(ENTITY_TYPE) -> Unit): AssociableDeclaration.(ENTITY_TYPE) -> Unit {
    val self = this
    return {
        self(it)
        other(it)
    }
}

class SelectStatement<ENTITY, ENTITY_TYPE : EntityType<ENTITY>>(
    val from: () -> ENTITY_TYPE,
    private val block: AssociableDeclaration.(ENTITY_TYPE) -> Unit
) {

    fun buildContextAndSql(config: Config): Pair<SelectContext, PreparedSql> {
        val entityType = from()
        val context = SelectContext(entityType)
        val declaration = AssociableDeclaration(context)
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
        return SelectStatement(this.from, this.block + other.block)
    }
}

class SelectSingle<BASIC>(val context: SelectContext)
class SelectPair<BASIC1, BASIC2>(val context: SelectContext)
