package org.seasar.doma.criteria.context

import org.seasar.doma.def.EntityDef
import org.seasar.doma.jdbc.Config

class InsertContext(
    val config: Config,
    val entityDef: EntityDef<*>,
    val values: MutableMap<Operand.Prop, Operand.Param> = mutableMapOf()
) : Context {

    override val entityDefs: List<EntityDef<*>>
        get() = listOf(entityDef)
}
