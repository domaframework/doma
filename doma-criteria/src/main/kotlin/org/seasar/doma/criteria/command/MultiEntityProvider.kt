package org.seasar.doma.criteria.command

import java.sql.ResultSet
import org.seasar.doma.internal.jdbc.command.AbstractObjectProvider
import org.seasar.doma.jdbc.entity.EntityPropertyType
import org.seasar.doma.jdbc.entity.EntityType
import org.seasar.doma.jdbc.entity.Property
import org.seasar.doma.jdbc.query.Query

class MultiEntityProvider(private val entityTypes: List<EntityType<Any>>, query: Query) : AbstractObjectProvider<MultiEntity>() {

    private val jdbcMappingVisitor = query.config.dialect.jdbcMappingVisitor

    override fun get(resultSet: ResultSet): MultiEntity {
        val multiEntity = MultiEntity()
        var index = 1
        for (entityType in entityTypes) {
            val pairs = mutableListOf<Pair<EntityPropertyType<*, *>, Property<Any, *>>>()
            entityType.entityPropertyTypes.forEach { propType ->
                val prop = propType.createProperty().also {
                    fetch(resultSet, it, index, jdbcMappingVisitor)
                    index++
                }
                pairs.add(propType to prop)
            }
            val key = pairs.filter { (propType, _) -> propType.isId }
                    .map { (_, prop) -> prop.wrapper.get() }
                    .let { EntityKey(it) }
            val data = pairs.map { (propType, prop) -> propType.name to prop }.toMap().let { EntityData(it) }
            if (data.states.values.all { it.wrapper.get() == null }) {
                continue
            }
            multiEntity.keyDataMap[entityType] = key to data
        }
        return multiEntity
    }
}
