package org.seasar.doma.criteria.declaration

import org.seasar.doma.criteria.context.Projection
import org.seasar.doma.criteria.context.SelectContext
import org.seasar.doma.def.EntityDef
import org.seasar.doma.jdbc.Config

@Declaration
class SubQueryDeclaration<RESULT>(private val config: Config) {
    fun <ENTITY, ENTITY_DEF : EntityDef<ENTITY>> from(
        entityDefProvider: () -> ENTITY_DEF,
        block: SqlSelectDeclaration.(ENTITY_DEF) -> RESULT
    ): SelectContext {
        val entityDef = entityDefProvider()
        val context = SelectContext(config, entityDef, projection = Projection.Asterisk)
        val declaration = SqlSelectDeclaration(context)
        declaration.block(entityDef)
        return context
    }
}
