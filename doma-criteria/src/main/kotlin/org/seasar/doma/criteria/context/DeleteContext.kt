package org.seasar.doma.criteria.context

import org.seasar.doma.jdbc.Config
import org.seasar.doma.jdbc.entity.EntityType

class DeleteContext(
    val config: Config,
    val entityType: EntityType<*>,
    val where: MutableList<Criterion> = mutableListOf()
) : Context {

    override val entityTypes: List<EntityType<*>>
        get() = listOf(entityType)
}
