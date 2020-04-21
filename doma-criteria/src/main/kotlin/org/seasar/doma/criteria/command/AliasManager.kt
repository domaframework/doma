package org.seasar.doma.criteria.command

import org.seasar.doma.criteria.SelectContext
import org.seasar.doma.jdbc.entity.EntityPropertyType
import org.seasar.doma.jdbc.entity.EntityType

class AliasManager(
    selectContext: SelectContext,
    private val parentManager: AliasManager? = null
) {

    private var index: Int = parentManager?.index ?: 0
    private val entityAliasMap = mutableMapOf<EntityType<*>, String>()
    private val propAliasMap = mutableMapOf<EntityPropertyType<*, *>, String>()

    init {
        val entityTypes = listOf(selectContext.entityType) + selectContext.joins.map { it.entityType }
        entityTypes.forEach { entityType ->
            val alias = "t${index++}_"
            entityAliasMap[entityType] = alias
            entityType.entityPropertyTypes.forEach {
                propAliasMap[it] = alias
            }
        }
    }

    operator fun get(key: EntityType<*>): String? {
        // TODO throw an exception when the alias is not found
        return parentManager?.get(key) ?: entityAliasMap[key]
    }

    operator fun get(key: EntityPropertyType<*, *>): String? {
        // TODO throw an exception when the alias is not found
        return parentManager?.get(key) ?: propAliasMap[key]
    }
}
