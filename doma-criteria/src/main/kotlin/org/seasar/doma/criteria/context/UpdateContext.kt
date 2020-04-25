package org.seasar.doma.criteria.context

import org.seasar.doma.jdbc.Config
import org.seasar.doma.jdbc.entity.EntityType

class UpdateContext(
    val config: Config,
    val entityType: EntityType<*>,
    val set: MutableMap<Operand.Prop, Operand.Param> = mutableMapOf(),
    val where: MutableList<Criterion> = mutableListOf()
) : Context {

    override val entityTypes: List<EntityType<*>>
        get() = listOf(entityType)
}
