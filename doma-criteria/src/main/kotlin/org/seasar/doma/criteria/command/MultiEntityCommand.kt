package org.seasar.doma.criteria.command

import org.seasar.doma.criteria.SelectContext
import org.seasar.doma.jdbc.command.SelectCommand
import org.seasar.doma.jdbc.entity.EntityType

class MultiEntityCommand<ENTITY>(
    private val context: SelectContext,
    private val query: MultiEntityQuery
) {

    fun execute(): List<ENTITY> {
        val cache = mutableMapOf<Pair<EntityType<Any>, EntityKey>, Any>()
        @Suppress("UNCHECKED_CAST")
        val entityTypes = context.getProjectionTargets() as List<EntityType<Any>>
        val command = SelectCommand(query, MultiEntityIterationHandler(entityTypes))
        val multiEntityList = command.execute()
        for (multiEntity in multiEntityList) {
            val associationCandidate = mutableMapOf<EntityType<*>, Any?>()
            for ((entityType, keyData) in multiEntity.keyDataMap) {
                val (key, data) = keyData
                val entity = cache.computeIfAbsent(entityType to key) {
                    entityType.newEntity(data.states).also {
                        if (!entityType.isImmutable) {
                            entityType.saveCurrentStates(it)
                        }
                    }
                }
                associationCandidate[entityType] = entity
            }
            associate(associationCandidate)
        }
        @Suppress("UNCHECKED_CAST")
        return cache.asSequence()
                .filter { (key, _) -> key.first == context.entityType }
                .map { (_, value) -> value }
                .toList() as List<ENTITY>
    }

    private fun associate(associationCandidate: Map<EntityType<*>, Any?>) {
        for ((pair, associator) in context.associations) {
            val entity1 = associationCandidate[pair.first]
            val entity2 = associationCandidate[pair.second]
            if (entity1 == null || entity2 == null) {
                continue
            }
            associator(entity1, entity2)
        }
    }
}
