package org.seasar.doma.criteria.declaration

import org.seasar.doma.criteria.statement.EntityqlSelectStatement
import org.seasar.doma.criteria.statement.Statement
import org.seasar.doma.def.EntityDef

@Declaration
class EntityqlDeclaration {
    fun <ENTITY, ENTITY_DEF : EntityDef<ENTITY>> from(
        from: () -> ENTITY_DEF,
        block: EntityqlSelectDeclaration.(ENTITY_DEF) -> Unit
    ): Statement<List<ENTITY>> {
        return EntityqlSelectStatement(from, block)
    }
}
