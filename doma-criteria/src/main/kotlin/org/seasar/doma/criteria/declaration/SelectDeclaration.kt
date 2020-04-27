package org.seasar.doma.criteria.declaration

import org.seasar.doma.criteria.context.ForUpdate
import org.seasar.doma.criteria.context.Join
import org.seasar.doma.criteria.context.JoinKind
import org.seasar.doma.criteria.context.SelectContext
import org.seasar.doma.def.EntityDef

@Declaration
open class SelectDeclaration(protected val context: SelectContext) {
    private val whereDeclaration = WhereDeclaration(context.config) { context.where.add(it) }
    private val orderByDeclaration = OrderByDeclaration { context.orderBy.add(it) }
    private val forUpdateDeclaration = ForUpdateDeclaration { context.forUpdate = it }

    fun distinct(value: Boolean = true) {
        context.distinct = value
    }

    fun <ENTITY_DEF : EntityDef<*>> innerJoin(
        entityDefProvider: () -> ENTITY_DEF,
        block: JoinDeclaration.(ENTITY_DEF) -> Unit
    ): ENTITY_DEF {
        return join(entityDefProvider, block, JoinKind.INNER)
    }

    fun <ENTITY_DEF : EntityDef<*>> leftJoin(
        entityDefProvider: () -> ENTITY_DEF,
        block: JoinDeclaration.(ENTITY_DEF) -> Unit
    ): ENTITY_DEF {
        return join(entityDefProvider, block, JoinKind.LEFT)
    }

    private fun <ENTITY_DEF : EntityDef<*>> join(
        entityDefProvider: () -> ENTITY_DEF,
        block: JoinDeclaration.(ENTITY_DEF) -> Unit,
        joinKind: JoinKind
    ): ENTITY_DEF {
        val entityDef = entityDefProvider()
        val join = Join(entityDef.asType(), joinKind).also {
            val declaration = JoinDeclaration(it)
            declaration.block(entityDef)
        }
        context.joins.add(join)
        return entityDef
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
