package org.seasar.doma.criteria

import org.seasar.doma.criteria.command.MappedObjectIterationHandler
import org.seasar.doma.criteria.command.MappedObjectQuery
import org.seasar.doma.criteria.command.MultiEntitySqlBuilder
import org.seasar.doma.jdbc.Config
import org.seasar.doma.jdbc.PreparedSql
import org.seasar.doma.jdbc.Sql
import org.seasar.doma.jdbc.command.SelectCommand
import org.seasar.doma.jdbc.entity.EntityPropertyDesc
import org.seasar.doma.jdbc.entity.EntityType

fun <ENTITY, ENTITY_TYPE : EntityType<ENTITY>, RESULT>
        sql(
            block: SqlDeclaration.() -> SqlStatement<ENTITY, ENTITY_TYPE, RESULT>
        ): SqlStatement<ENTITY, ENTITY_TYPE, RESULT> {
    return SqlDeclaration().block()
}

@Declaration
class SqlDeclaration {
    fun <ENTITY, ENTITY_TYPE : EntityType<ENTITY>, RESULT> from(
        from: () -> ENTITY_TYPE,
        block: SqlFromDeclaration.(ENTITY_TYPE) -> org.seasar.doma.criteria.Select<RESULT>
    ): SqlStatement<ENTITY, ENTITY_TYPE, RESULT> {
        return SqlStatement(from, block)
    }
}

@Declaration
class SqlFromDeclaration(_selectContext: SelectContext) : FromDeclaration(_selectContext) {
    private val havingDeclaration = HavingDeclaration(_selectContext.config) { _selectContext.having.add(it) }

    fun <ENTITY, BASIC, CONTAINER> count(propType: EntityPropertyDesc<ENTITY, BASIC, CONTAINER>): Count<ENTITY, BASIC, CONTAINER> {
        return Count(propType)
    }

    fun groupBy(vararg propTypes: EntityPropertyDesc<*, *, *>) {
        _selectContext.groupBy.addAll(propTypes.asList())
    }

    fun having(block: HavingDeclaration.() -> Unit) = havingDeclaration.block()

    fun <RESULT> select(vararg propTypes: EntityPropertyDesc<*, *, *>, mapper: (Row) -> RESULT): Select<RESULT> {
        return Select(listOf(*propTypes), mapper)
    }
}

class SqlStatement<ENTITY, ENTITY_TYPE : EntityType<ENTITY>, RESULT>(
    val from: () -> ENTITY_TYPE,
    private val block: SqlFromDeclaration.(ENTITY_TYPE) -> Select<RESULT>
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
        val declaration = SqlFromDeclaration(context)
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
