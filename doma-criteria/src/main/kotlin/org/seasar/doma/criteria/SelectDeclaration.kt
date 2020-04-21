package org.seasar.doma.criteria

import org.seasar.doma.jdbc.entity.EntityType

@Declaration
open class SelectDeclaration(protected val _selectContext: SelectContext) {
    private val whereDeclaration = WhereDeclaration(_selectContext.config) { _selectContext.where.add(it) }
    private val orderByDeclaration = OrderByDeclaration { _selectContext.orderBy.add(it) }
    private val forUpdateDeclaration = ForUpdateDeclaration { _selectContext.forUpdate = it }

    fun distinct(value: Boolean = true) {
        _selectContext.distinct = value
    }

    fun <ENTITY_TYPE : EntityType<*>> innerJoin(
        entityTypeProvider: () -> ENTITY_TYPE,
        block: JoinDeclaration.(ENTITY_TYPE) -> Unit
    ): ENTITY_TYPE {
        return join(entityTypeProvider, block, JoinKind.INNER)
    }

    fun <ENTITY_TYPE : EntityType<*>> leftJoin(
        entityTypeProvider: () -> ENTITY_TYPE,
        block: JoinDeclaration.(ENTITY_TYPE) -> Unit
    ): ENTITY_TYPE {
        return join(entityTypeProvider, block, JoinKind.LEFT)
    }

    private fun <ENTITY_TYPE : EntityType<*>> join(
        entityTypeProvider: () -> ENTITY_TYPE,
        block: JoinDeclaration.(ENTITY_TYPE) -> Unit,
        joinKind: JoinKind
    ): ENTITY_TYPE {
        val entityType = entityTypeProvider()
        val join = Join(entityType, joinKind).also {
            val declaration = JoinDeclaration(it)
            declaration.block(entityType)
        }
        _selectContext.joins.add(join)
        return entityType
    }

    fun where(block: WhereDeclaration.() -> Unit) = whereDeclaration.block()

    fun orderBy(block: OrderByDeclaration.() -> Unit) = orderByDeclaration.block()

    fun limit(value: Int) {
        _selectContext.limit = value
    }

    fun offset(value: Int) {
        _selectContext.offset = value
    }

    fun forUpdate(block: ForUpdateDeclaration.() -> Unit) {
        _selectContext.forUpdate = ForUpdate()
        forUpdateDeclaration.block()
    }
}

@Declaration
class AssociableDeclaration(_selectContext: SelectContext) : SelectDeclaration(_selectContext) {
    fun <ENTITY, ENTITY2> associate(type1: EntityType<ENTITY>, type2: EntityType<ENTITY2>, associator: (ENTITY, ENTITY2) -> Unit) {
        // TODO check arguments
        _selectContext.associations[type1 to type2] = associator as (Any, Any) -> Unit
    }
}
