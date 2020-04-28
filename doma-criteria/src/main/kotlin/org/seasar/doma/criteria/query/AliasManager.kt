package org.seasar.doma.criteria.query

import org.seasar.doma.criteria.context.Context
import org.seasar.doma.def.EntityDef
import org.seasar.doma.def.PropertyDef

class AliasManager(
    context: Context,
    private val parentManager: AliasManager? = null
) {

    private var index: Int = parentManager?.index ?: 0
    private val entityAliasMap = mutableMapOf<EntityDef<*>, String>()
    private val propAliasMap = mutableMapOf<PropertyDef<*>, String>()

    init {
        context.entityDefs.forEach { entityDef ->
            val alias = "t${index++}_"
            entityAliasMap[entityDef] = alias
            entityDef.allPropertyDefs().forEach {
                propAliasMap[it] = alias
            }
        }
    }

    operator fun get(key: EntityDef<*>): String? {
        return parentManager?.get(key) ?: entityAliasMap[key]
    }

    operator fun get(key: PropertyDef<*>): String? {
        return parentManager?.get(key) ?: propAliasMap[key]
    }
}
