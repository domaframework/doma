package org.seasar.doma.criteria.command

import org.seasar.doma.criteria.context.SelectContext
import org.seasar.doma.criteria.query.SelectQuery
import org.seasar.doma.def.EntityDef
import org.seasar.doma.jdbc.command.Command
import org.seasar.doma.jdbc.command.SelectCommand
import org.seasar.doma.jdbc.query.Query

class MultiEntityCommand<ENTITY>(
    private val context: SelectContext,
    private val query: SelectQuery
) : Command<List<ENTITY>> {

    override fun execute(): List<ENTITY> {
        val cache = mutableMapOf<Pair<EntityDef<Any>, EntityKey>, Any>()

        @Suppress("UNCHECKED_CAST")
        val entityDefs = context.getProjectionTargets() as List<EntityDef<Any>>
        val command = SelectCommand(query, MultiEntityIterationHandler(entityDefs))
        val multiEntityList = command.execute()
        for (multiEntity in multiEntityList) {
            val associationCandidate = mutableMapOf<EntityDef<*>, Any?>()
            for ((entityDef, keyData) in multiEntity.keyDataMap) {
                val (key, data) = keyData
                val entity = cache.computeIfAbsent(entityDef to key) {
                    val entityType = entityDef.asType()
                    entityType.newEntity(data.states).also {
                        if (!entityType.isImmutable) {
                            entityType.saveCurrentStates(it)
                        }
                    }
                }
                associationCandidate[entityDef] = entity
            }
            associate(associationCandidate)
        }
        @Suppress("UNCHECKED_CAST")
        return cache.asSequence()
                .filter { (key, _) -> key.first == context.entityDef }
                .map { (_, value) -> value }
                .toList() as List<ENTITY>
    }

    private fun associate(associationCandidate: Map<EntityDef<*>, Any?>) {
        for ((pair, associator) in context.associations) {
            val entity1 = associationCandidate[pair.first]
            val entity2 = associationCandidate[pair.second]
            if (entity1 == null || entity2 == null) {
                continue
            }
            associator(entity1, entity2)
        }
    }

    override fun getQuery(): Query {
        return query
    }
}
