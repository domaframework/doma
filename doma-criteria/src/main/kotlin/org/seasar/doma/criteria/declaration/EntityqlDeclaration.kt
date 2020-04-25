package org.seasar.doma.criteria.declaration

import org.seasar.doma.criteria.statement.EntityqlSelectStatement
import org.seasar.doma.criteria.statement.Statement
import org.seasar.doma.jdbc.entity.EntityType

@Declaration
class EntityqlDeclaration {
    fun <ENTITY, ENTITY_TYPE : EntityType<ENTITY>> from(
        from: () -> ENTITY_TYPE,
        block: EntityqlSelectDeclaration.(ENTITY_TYPE) -> Unit
    ): Statement<List<ENTITY>> {
        return EntityqlSelectStatement(from, block)
    }
}
