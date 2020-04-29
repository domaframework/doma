package org.seasar.doma.criteria.context

import org.seasar.doma.def.EntityDef
import org.seasar.doma.jdbc.Config

class UpdateContext(
    val config: Config,
    val entityDef: EntityDef<*>,
    val set: MutableMap<Operand.Prop, Operand> = mutableMapOf(),
    val where: MutableList<Criterion> = mutableListOf()
) : Context {

    override val entityDefs: List<EntityDef<*>>
        get() = listOf(entityDef)
}
