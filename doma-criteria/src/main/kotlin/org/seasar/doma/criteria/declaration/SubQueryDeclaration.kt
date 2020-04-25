package org.seasar.doma.criteria.declaration

import org.seasar.doma.criteria.context.Projection
import org.seasar.doma.criteria.context.SelectContext
import org.seasar.doma.jdbc.Config
import org.seasar.doma.jdbc.entity.EntityType

@Declaration
class SubQueryDeclaration<RESULT>(private val config: Config) {
    fun <ENTITY, ENTITY_TYPE : EntityType<ENTITY>> from(
        entityTypeProvider: () -> ENTITY_TYPE,
        block: SqlSelectDeclaration.(ENTITY_TYPE) -> RESULT
    ): SelectContext {
        val entityType = entityTypeProvider()
        val context = SelectContext(config, entityType, projection = Projection.Asterisk)
        val declaration = SqlSelectDeclaration(context)
        declaration.block(entityType)
        return context
    }
}
