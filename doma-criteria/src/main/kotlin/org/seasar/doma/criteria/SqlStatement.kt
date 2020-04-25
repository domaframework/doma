package org.seasar.doma.criteria

import org.seasar.doma.criteria.command.MappedObjectIterationHandler
import org.seasar.doma.criteria.query.DeleteBuilder
import org.seasar.doma.criteria.query.DeleteQuery
import org.seasar.doma.criteria.query.InsertBuilder
import org.seasar.doma.criteria.query.InsertQuery
import org.seasar.doma.criteria.query.SelectBuilder
import org.seasar.doma.criteria.query.SelectQuery
import org.seasar.doma.criteria.query.UpdateQuery
import org.seasar.doma.criteria.query.UpdateSqlBuilder
import org.seasar.doma.jdbc.Config
import org.seasar.doma.jdbc.PreparedSql
import org.seasar.doma.jdbc.command.Command
import org.seasar.doma.jdbc.command.DeleteCommand
import org.seasar.doma.jdbc.command.InsertCommand
import org.seasar.doma.jdbc.command.SelectCommand
import org.seasar.doma.jdbc.command.UpdateCommand
import org.seasar.doma.jdbc.entity.EntityPropertyType
import org.seasar.doma.jdbc.entity.EntityType

class SelectSqlStatement<ENTITY, ENTITY_TYPE : EntityType<ENTITY>, RESULT>(
    private val entityTypeProvider: () -> ENTITY_TYPE,
    private val block: SqlSelectDeclaration.(ENTITY_TYPE) -> Pair<List<EntityPropertyType<*, *>>, (Row) -> RESULT>
) : AbstractStatement<List<RESULT>>() {

    override fun commandAndSql(config: Config): Pair<Command<List<RESULT>>, PreparedSql> {
        val entityType = entityTypeProvider()
        val context = SelectContext(config, entityType)
        val declaration = SqlSelectDeclaration(context)
        val (propTypes, mapper) = declaration.block(entityType)
        val builder = SelectBuilder(context)
        val sql = builder.build()
        val query = SelectQuery(config, sql)
        val handler = MappedObjectIterationHandler(propTypes, mapper)
        val command = SelectCommand(query, handler)
        return command to sql
    }
}

class DeleteSqlStatement<ENTITY, ENTITY_TYPE : EntityType<ENTITY>>(
    private val entityTypeProvider: () -> ENTITY_TYPE,
    private val block: DeleteDeclaration.(ENTITY_TYPE) -> Unit
) : AbstractStatement<Int>() {

    override fun commandAndSql(config: Config): Pair<Command<Int>, PreparedSql> {
        val entityType = entityTypeProvider()
        val context = DeleteContext(config, entityType)
        val declaration = DeleteDeclaration(context)
        declaration.block(entityType)
        val builder = DeleteBuilder(context)
        val sql = builder.build()
        val query = DeleteQuery(config, sql)
        val command = DeleteCommand(query)
        return command to sql
    }
}

class InsertSqlStatement<ENTITY, ENTITY_TYPE : EntityType<ENTITY>>(
    private val entityTypeProvider: () -> ENTITY_TYPE,
    private val block: InsertDeclaration.(ENTITY_TYPE) -> Unit
) : AbstractStatement<Int>() {

    override fun commandAndSql(config: Config): Pair<Command<Int>, PreparedSql> {
        val entityType = entityTypeProvider()
        val context = InsertContext(config, entityType)
        val declaration = InsertDeclaration(context)
        declaration.block(entityType)
        val builder = InsertBuilder(context)
        val sql = builder.build()
        val query = InsertQuery(config, sql)
        val command = InsertCommand(query)
        return command to sql
    }
}

class UpdateSqlStatement<ENTITY, ENTITY_TYPE : EntityType<ENTITY>>(
    private val entityTypeProvider: () -> ENTITY_TYPE,
    private val block: UpdateDeclaration.(ENTITY_TYPE) -> Unit
) : AbstractStatement<Int>() {

    override fun commandAndSql(config: Config): Pair<Command<Int>, PreparedSql> {
        val entityType = entityTypeProvider()
        val context = UpdateContext(config, entityType)
        val declaration = UpdateDeclaration(context)
        declaration.block(entityType)
        val builder = UpdateSqlBuilder(context)
        val sql = builder.build()
        val query = UpdateQuery(config, sql)
        val command = UpdateCommand(query)
        return command to sql
    }
}
