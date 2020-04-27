package org.seasar.doma.criteria.declaration

import org.seasar.doma.criteria.context.SelectContext
import org.seasar.doma.def.EntityDef

@Declaration
class EntityqlSelectDeclaration(context: SelectContext) : SelectDeclaration(context) {
    @Suppress("UNCHECKED_CAST")
    fun <ENTITY, ENTITY2> associate(first: EntityDef<ENTITY>, second: EntityDef<ENTITY2>, associator: (ENTITY, ENTITY2) -> Unit) {
        // TODO
        require(context.entityTypes.contains(first.asType())) {
            "The first is unknown. Ensure that you added its constructor " +
                    "to the from function or the join functions."
        }
        // TODO
        require(context.entityTypes.contains(second.asType())) {
            "The second is unknown. Ensure that you added its constructor " +
                    "to the from function or the join functions."
        }
        context.associations[first.asType() to second.asType()] = associator as (Any, Any) -> Unit
    }
}
