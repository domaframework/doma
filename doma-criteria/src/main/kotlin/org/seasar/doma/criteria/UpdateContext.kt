package org.seasar.doma.criteria

import org.seasar.doma.jdbc.Config
import org.seasar.doma.jdbc.entity.EntityType

class UpdateContext(
    val config: Config,
    val entityType: EntityType<*>,
    val set: MutableMap<Operand.Prop, Operand.Param> = mutableMapOf(),
    val where: MutableList<Criterion> = mutableListOf()
) : CriteriaContext {

    override val entityTypes: List<EntityType<*>>
        get() = listOf(entityType)
}
