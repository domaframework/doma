package org.seasar.doma.criteria.declaration

import org.seasar.doma.criteria.context.SelectContext
import org.seasar.doma.jdbc.entity.EntityType

@Declaration
class EntityqlSelectDeclaration(context: SelectContext) : SelectDeclaration(context) {
    @Suppress("UNCHECKED_CAST")
    fun <ENTITY, ENTITY2> associate(type1: EntityType<ENTITY>, type2: EntityType<ENTITY2>, associator: (ENTITY, ENTITY2) -> Unit) {
        require(context.entityTypes.contains(type1)) {
            "The type1 is unknown. Ensure that you added its constructor " +
                    "to the from function or the join functions."
        }
        require(context.entityTypes.contains(type2)) {
            "The type2 is unknown. Ensure that you added its constructor " +
                    "to the from function or the join functions."
        }
        context.associations[type1 to type2] = associator as (Any, Any) -> Unit
    }
}
