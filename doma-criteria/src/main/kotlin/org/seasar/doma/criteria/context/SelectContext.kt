package org.seasar.doma.criteria.context

import org.seasar.doma.def.EntityDef
import org.seasar.doma.def.PropertyDef
import org.seasar.doma.jdbc.Config

class SelectContext(
    val config: Config,
    val entityDef: EntityDef<*>,
    var distinct: Boolean = false,
    var projection: Projection = Projection.All,
    val joins: MutableList<Join> = mutableListOf(),
    val where: MutableList<Criterion> = mutableListOf(),
    val orderBy: MutableList<Pair<PropertyDef<*>, String>> = mutableListOf(),
    val groupBy: MutableList<PropertyDef<*>> = mutableListOf(),
    val having: MutableList<Criterion> = mutableListOf(),
    var limit: Int? = null,
    var offset: Int? = null,
    var forUpdate: ForUpdate? = null,
    val associations: MutableMap<Pair<EntityDef<*>, EntityDef<*>>, (Any, Any) -> Unit> = mutableMapOf()
) : Context {

    override val entityDefs: List<EntityDef<*>>
        get() = listOf(entityDef) + joins.map { it.entityDef }

    fun getProjectionTargets(): List<EntityDef<*>> {
        val entityDefs = mutableListOf<EntityDef<*>>()
        entityDefs.add(entityDef)
        associations.forEach { (pair, _) ->
            listOf(pair.first, pair.second).forEach {
                if (!entityDefs.contains(it)) {
                    entityDefs.add(it)
                }
            }
        }
        return entityDefs
    }
}

sealed class Projection {
    object All : Projection()
    object Asterisk : Projection()
    data class Single(val propDef: PropertyDef<*>) : Projection()
    data class Pair(val first: PropertyDef<*>, val second: PropertyDef<*>) : Projection()
    data class List(val propDefs: Iterable<PropertyDef<*>>) : Projection()
}

class Join(
    val entityDef: EntityDef<*>,
    val kind: JoinKind,
    val on: MutableList<Criterion> = mutableListOf()
)

enum class JoinKind {
    INNER,
    LEFT
}

class ForUpdate(var nowait: Boolean = false)
