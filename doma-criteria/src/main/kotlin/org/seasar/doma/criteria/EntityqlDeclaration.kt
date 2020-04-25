package org.seasar.doma.criteria

import org.seasar.doma.jdbc.entity.EntityType

@Declaration
class EntityqlDeclaration {
    fun <ENTITY, ENTITY_TYPE : EntityType<ENTITY>> from(
        from: () -> ENTITY_TYPE,
        block: EntityqlSelectDeclaration.(ENTITY_TYPE) -> Unit
    ): Statement<List<ENTITY>> {
        return EntityqlStatement(from, block)
    }
}
