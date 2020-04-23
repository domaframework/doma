package org.seasar.doma.criteria

import org.seasar.doma.criteria.command.MappedObjectIterationHandler
import org.seasar.doma.criteria.command.MappedObjectQuery
import org.seasar.doma.criteria.command.MultiEntityCommand
import org.seasar.doma.criteria.command.MultiEntityQuery
import org.seasar.doma.criteria.command.MultiEntitySqlBuilder
import org.seasar.doma.jdbc.Config
import org.seasar.doma.jdbc.PreparedSql
import org.seasar.doma.jdbc.Sql
import org.seasar.doma.jdbc.command.SelectCommand
import org.seasar.doma.jdbc.entity.EntityType

fun <ENTITY, ENTITY_TYPE : EntityType<ENTITY>> select(
    from: () -> ENTITY_TYPE,
    block: AssociableDeclaration.(ENTITY_TYPE) -> Unit
): SelectStatement<ENTITY, ENTITY_TYPE> {
    return SelectStatement(from, block)
}

fun <ENTITY, ENTITY_TYPE : EntityType<ENTITY>, RESULT> from(
    from: () -> ENTITY_TYPE,
    block: FromDeclaration.(ENTITY_TYPE) -> Select<RESULT>
): FromQuery<ENTITY, ENTITY_TYPE, RESULT> {
    return FromQuery(from, block)
}

infix operator fun <ENTITY, ENTITY_TYPE : EntityType<ENTITY>, DECLARATION : SelectDeclaration>
        (DECLARATION.(ENTITY_TYPE) -> Unit).plus(other: DECLARATION.(ENTITY_TYPE) -> Unit): DECLARATION.(ENTITY_TYPE) -> Unit {
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
        val declaration = AssociableDeclaration(context)
        declaration.block(entityType)
        val builder = MultiEntitySqlBuilder(context)
        return context to builder.build()
    }

    operator fun plus(other: SelectStatement<ENTITY, ENTITY_TYPE>): SelectStatement<ENTITY, ENTITY_TYPE> {
        return SelectStatement(this.from, this.block + other.block)
    }
}

class FromQuery<ENTITY, ENTITY_TYPE : EntityType<ENTITY>, RESULT>(
    val from: () -> ENTITY_TYPE,
    private val block: FromDeclaration.(ENTITY_TYPE) -> Select<RESULT>
) {

    fun execute(config: Config): List<RESULT> {
        val (select, context, sql) = buildContextAndSql(config)
        val query = MappedObjectQuery(config, sql)
        val handler = MappedObjectIterationHandler(select.propTypes, select.mapper)
        val command = SelectCommand(query, handler)
        return command.execute()
    }

    fun asSql(config: Config): Sql<*> {
        val (_, _, sql) = buildContextAndSql(config)
        return sql
    }

    private fun buildContextAndSql(config: Config): Triple<Select<RESULT>, SelectContext, PreparedSql> {
        val entityType = from()
        val context = SelectContext(config, entityType)
        val declaration = FromDeclaration(context)
        val select = declaration.block(entityType)
        context.projection = Projection.List(select.propTypes)
        val builder = MultiEntitySqlBuilder(context)
        val sql = builder.build()
        return Triple(select, context, sql)
    }

//    operator fun plus(other: SelectColumnsStatement<ENTITY, ENTITY_TYPE>): SelectColumnsStatement<ENTITY, ENTITY_TYPE> {
//        return SelectColumnsStatement(this.from, this.block + other.block)
//    }
}

// TODO Define in another file
class SelectSingle<BASIC>(val context: SelectContext)
// TODO Define in another file
class SelectPair<BASIC1, BASIC2>(val context: SelectContext)
