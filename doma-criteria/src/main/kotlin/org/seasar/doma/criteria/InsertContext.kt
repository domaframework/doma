package org.seasar.doma.criteria

import org.seasar.doma.jdbc.Config
import org.seasar.doma.jdbc.entity.EntityType

class InsertContext(
    val config: Config,
    val entityType: EntityType<*>,
    val values: MutableMap<Operand.Prop, Operand.Param> = mutableMapOf()
) : CriteriaContext {

    override val entityTypes: List<EntityType<*>>
        get() = listOf(entityType)
}
