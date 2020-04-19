package org.seasar.doma.criteria.command

import org.seasar.doma.jdbc.entity.EntityType
import org.seasar.doma.jdbc.entity.Property

class MultiEntity {
    val keyDataMap = mutableMapOf<EntityType<Any>, Pair<EntityKey, EntityData>>()
}

data class EntityKey(val items: List<Any?>)

class EntityData(val states: Map<String, Property<Any, *>>)
