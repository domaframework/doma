package org.seasar.doma.criteria

import org.seasar.doma.jdbc.Config
import org.seasar.doma.jdbc.entity.EntityPropertyDesc
import org.seasar.doma.jdbc.entity.EntityType

class ExistsSubQueryDeclaration(private val config: Config) {
    fun <ENTITY, ENTITY_TYPE : EntityType<ENTITY>> from(
        entityTypeProvider: () -> ENTITY_TYPE,
        block: ExistsSubQueryFromDeclaration.(ENTITY_TYPE) -> Unit
    ): SelectContext {
        val entityType = entityTypeProvider()
        val context = SelectContext(config, entityType, projection = Projection.Asterisk)
        val declaration = ExistsSubQueryFromDeclaration(context)
        declaration.block(entityType)
        return context
    }
}

class SingleSubQueryDeclaration<CONTAINER>(private val config: Config) {
    fun <ENTITY, ENTITY_TYPE : EntityType<ENTITY>> from(
        entityTypeProvider: () -> ENTITY_TYPE,
        block: SingleSubQueryFromDeclaration<CONTAINER>.(ENTITY_TYPE) -> SelectContext
    ): SelectContext {
        val entityType = entityTypeProvider()
        val context = SelectContext(config, entityType)
        val declaration = SingleSubQueryFromDeclaration<CONTAINER>(context)
        return declaration.block(entityType)
    }
}

class PairSubQueryDeclaration<CONTAINER1, CONTAINER2>(private val config: Config) {
    fun <ENTITY, ENTITY_TYPE : EntityType<ENTITY>> from(
        entityTypeProvider: () -> ENTITY_TYPE,
        block: PairSubQueryFromDeclaration<CONTAINER1, CONTAINER2>.(ENTITY_TYPE) -> SelectContext
    ): SelectContext {
        val entityType = entityTypeProvider()
        val context = SelectContext(config, entityType)
        val declaration = PairSubQueryFromDeclaration<CONTAINER1, CONTAINER2>(context)
        return declaration.block(entityType)
    }
}

@Declaration
open class SubQueryFromDeclaration(_selectContext: SelectContext) : FromDeclaration(_selectContext) {
    private val havingDeclaration = HavingDeclaration(_selectContext.config) { _selectContext.having.add(it) }

    fun <ENTITY, BASIC, CONTAINER> count(propType: EntityPropertyDesc<ENTITY, BASIC, CONTAINER>): Count<ENTITY, BASIC, CONTAINER> {
        return Count(propType)
    }

    fun groupBy(vararg propTypes: EntityPropertyDesc<*, *, *>) {
        _selectContext.groupBy.addAll(propTypes.asList())
    }

    fun having(block: HavingDeclaration.() -> Unit) = havingDeclaration.block()
}

@Declaration
class ExistsSubQueryFromDeclaration(_selectContext: SelectContext) : SubQueryFromDeclaration(_selectContext)

@Declaration
class SingleSubQueryFromDeclaration<CONTAINER>(_selectContext: SelectContext) : SubQueryFromDeclaration(_selectContext) {

    fun select(propType: EntityPropertyDesc<*, *, CONTAINER>): SelectContext {
        _selectContext.projection = Projection.Single(propType)
        return _selectContext
    }
}

@Declaration
class PairSubQueryFromDeclaration<CONTAINER1, CONTAINER2>(_selectContext: SelectContext) : SubQueryFromDeclaration(_selectContext) {

    fun select(pair: Pair<EntityPropertyDesc<*, *, CONTAINER1>, EntityPropertyDesc<*, *, CONTAINER2>>): SelectContext {
        // TODO
        _selectContext.projection = Projection.Pair(pair.first, pair.second)
        return _selectContext
    }
}
