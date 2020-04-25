package org.seasar.doma.criteria

import org.seasar.doma.jdbc.Config
import org.seasar.doma.jdbc.entity.EntityPropertyType
import org.seasar.doma.jdbc.entity.EntityType

class SelectContext(
    val config: Config,
    val entityType: EntityType<*>,
    var distinct: Boolean = false,
    var projection: Projection = Projection.Default,
    val joins: MutableList<Join> = mutableListOf(),
    val where: MutableList<Criterion> = mutableListOf(),
    val orderBy: MutableList<Pair<EntityPropertyType<*, *>, String>> = mutableListOf(),
    val groupBy: MutableList<EntityPropertyType<*, *>> = mutableListOf(),
    val having: MutableList<Criterion> = mutableListOf(),
    var limit: Int? = null,
    var offset: Int? = null,
    var forUpdate: ForUpdate? = null,
    val associations: MutableMap<Pair<EntityType<*>, EntityType<*>>, (Any, Any) -> Unit> = mutableMapOf()
) : CriteriaContext {

    override val entityTypes: List<EntityType<*>>
        get() = listOf(entityType) + joins.map { it.entityType }

    fun getProjectionTargets(): List<EntityType<*>> {
        val entityTypes = mutableListOf<EntityType<*>>()
        entityTypes.add(entityType)
        associations.forEach { (pair, _) ->
            listOf(pair.first, pair.second).forEach {
                if (!entityTypes.contains(it)) {
                    entityTypes.add(it)
                }
            }
        }
        return entityTypes
    }
}

sealed class Projection {
    object Default : Projection()
    object Asterisk : Projection()
    data class Single(val propType: EntityPropertyType<*, *>) : Projection()
    data class Pair(val first: EntityPropertyType<*, *>, val second: EntityPropertyType<*, *>) : Projection()
    data class List(val propTypes: Iterable<EntityPropertyType<*, *>>) : Projection()
}

class Join(
    val entityType: EntityType<*>,
    val kind: JoinKind,
    val on: MutableList<Criterion> = mutableListOf()
)

enum class JoinKind {
    INNER,
    LEFT
}

class ForUpdate(var nowait: Boolean = false)
