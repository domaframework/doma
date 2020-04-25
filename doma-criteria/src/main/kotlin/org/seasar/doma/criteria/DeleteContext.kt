package org.seasar.doma.criteria

import org.seasar.doma.jdbc.Config
import org.seasar.doma.jdbc.entity.EntityType

class DeleteContext(
    val config: Config,
    val entityType: EntityType<*>,
    val where: MutableList<Criterion> = mutableListOf()
) : CriteriaContext {

    override val entityTypes: List<EntityType<*>>
        get() = listOf(entityType)
}
