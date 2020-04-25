package org.seasar.doma.criteria.declaration

import org.seasar.doma.criteria.context.Projection
import org.seasar.doma.criteria.context.SelectContext
import org.seasar.doma.jdbc.Config
import org.seasar.doma.jdbc.entity.EntityPropertyDesc
import org.seasar.doma.jdbc.entity.EntityType

@Declaration
class ExistsSubQueryDeclaration(private val config: Config) {
    fun <ENTITY, ENTITY_TYPE : EntityType<ENTITY>> from(
        entityTypeProvider: () -> ENTITY_TYPE,
        block: ExistsSubQuerySelectDeclaration.(ENTITY_TYPE) -> Unit
    ): SelectContext {
        val entityType = entityTypeProvider()
        val context = SelectContext(config, entityType, projection = Projection.Asterisk)
        val declaration = ExistsSubQuerySelectDeclaration(context)
        declaration.block(entityType)
        return context
    }
}

@Declaration
class SingleSubQueryDeclaration<CONTAINER>(private val config: Config) {
    fun <ENTITY, ENTITY_TYPE : EntityType<ENTITY>> from(
        entityTypeProvider: () -> ENTITY_TYPE,
        block: SingleSubQuerySelectDeclaration<CONTAINER>.(ENTITY_TYPE) -> SelectContext
    ): SelectContext {
        val entityType = entityTypeProvider()
        val context = SelectContext(config, entityType)
        val declaration = SingleSubQuerySelectDeclaration<CONTAINER>(context)
        return declaration.block(entityType)
    }
}

@Declaration
class PairSubQueryDeclaration<CONTAINER1, CONTAINER2>(private val config: Config) {
    fun <ENTITY, ENTITY_TYPE : EntityType<ENTITY>> from(
        entityTypeProvider: () -> ENTITY_TYPE,
        block: PairSubQuerySelectDeclaration<CONTAINER1, CONTAINER2>.(ENTITY_TYPE) -> SelectContext
    ): SelectContext {
        val entityType = entityTypeProvider()
        val context = SelectContext(config, entityType)
        val declaration = PairSubQuerySelectDeclaration<CONTAINER1, CONTAINER2>(context)
        return declaration.block(entityType)
    }
}

@Declaration
open class SubQuerySelectDeclaration(context: SelectContext) : SelectDeclaration(context) {
    private val havingDeclaration = HavingDeclaration(context.config) { context.having.add(it) }

    fun <ENTITY, BASIC, CONTAINER> count(propType: EntityPropertyDesc<ENTITY, BASIC, CONTAINER>): Count<ENTITY, BASIC, CONTAINER> {
        return Count(propType)
    }

    fun groupBy(vararg propTypes: EntityPropertyDesc<*, *, *>) {
        context.groupBy.addAll(propTypes.asList())
    }

    fun having(block: HavingDeclaration.() -> Unit) = havingDeclaration.block()
}

@Declaration
class ExistsSubQuerySelectDeclaration(_selectContext: SelectContext) : SubQuerySelectDeclaration(_selectContext)

@Declaration
class SingleSubQuerySelectDeclaration<CONTAINER>(context: SelectContext) : SubQuerySelectDeclaration(context) {

    fun select(propType: EntityPropertyDesc<*, *, CONTAINER>): SelectContext {
        context.projection = Projection.Single(propType)
        return context
    }
}

@Declaration
class PairSubQuerySelectDeclaration<CONTAINER1, CONTAINER2>(_selectContext: SelectContext) : SubQuerySelectDeclaration(_selectContext) {

    fun select(pair: Pair<EntityPropertyDesc<*, *, CONTAINER1>, EntityPropertyDesc<*, *, CONTAINER2>>): SelectContext {
        // TODO
        context.projection = Projection.Pair(pair.first, pair.second)
        return context
    }
}
