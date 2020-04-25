package org.seasar.doma.criteria.declaration

import org.seasar.doma.criteria.context.ForUpdate
import org.seasar.doma.criteria.context.Join
import org.seasar.doma.criteria.context.JoinKind
import org.seasar.doma.criteria.context.SelectContext
import org.seasar.doma.jdbc.entity.EntityPropertyDesc
import org.seasar.doma.jdbc.entity.EntityType

@Declaration
open class SelectDeclaration(protected val context: SelectContext) {
    private val whereDeclaration = WhereDeclaration(context.config) { context.where.add(it) }
    private val orderByDeclaration = OrderByDeclaration { context.orderBy.add(it) }
    private val forUpdateDeclaration = ForUpdateDeclaration { context.forUpdate = it }

    fun distinct(value: Boolean = true) {
        context.distinct = value
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
        context.joins.add(join)
        return entityType
    }

    fun where(block: WhereDeclaration.() -> Unit) = whereDeclaration.block()

    fun orderBy(block: OrderByDeclaration.() -> Unit) = orderByDeclaration.block()

    fun limit(value: Int) {
        context.limit = value
    }

    fun offset(value: Int) {
        context.offset = value
    }

    fun forUpdate(block: ForUpdateDeclaration.() -> Unit) {
        context.forUpdate = ForUpdate()
        forUpdateDeclaration.block()
    }
}

class Select<RESULT>(val propTypes: List<EntityPropertyDesc<*, *, *>>, val mapper: (Row) -> RESULT)

interface SqlFunction {
    val functionName: String
    val propDesc: EntityPropertyDesc<*, *, *>
}

class Count<ENTITY, BASIC, CONTAINER>(override val propDesc: EntityPropertyDesc<ENTITY, BASIC, CONTAINER>) :
        EntityPropertyDesc<ENTITY, BASIC, CONTAINER> by propDesc, SqlFunction {

    override val functionName: String
        get() = "count"

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Count<*, *, *>

        if (propDesc != other.propDesc) return false

        return true
    }

    override fun hashCode(): Int {
        return propDesc.hashCode()
    }
}
