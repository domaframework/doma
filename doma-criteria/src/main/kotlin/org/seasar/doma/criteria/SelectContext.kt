package org.seasar.doma.criteria

import org.seasar.doma.jdbc.Config
import org.seasar.doma.jdbc.InParameter
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
    var limit: Int? = null,
    var offset: Int? = null,
    var forUpdate: ForUpdate? = null,
    val associations: MutableMap<Pair<EntityType<*>, EntityType<*>>, (Any, Any) -> Unit> = mutableMapOf()
) {

    fun getProjectionTargets(): List<EntityType<Any>> {
        val entityTypes = mutableListOf<EntityType<Any>>()
        entityTypes.add(entityType as EntityType<Any>)
        associations.forEach { (pair, _) ->
            listOf(pair.first, pair.second).forEach {
                if (!entityTypes.contains(it)) {
                    entityTypes.add(it as EntityType<Any>)
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
}

sealed class Operand {
    data class Param(val value: InParameter<*>) : Operand()
    data class Prop(val value: EntityPropertyType<*, *>) : Operand()
}

sealed class Criterion {
    data class Eq(val left: Operand.Prop, val right: Operand) : Criterion()
    data class Ne(val left: Operand.Prop, val right: Operand) : Criterion()
    data class Gt(val left: Operand.Prop, val right: Operand) : Criterion()
    data class Ge(val left: Operand.Prop, val right: Operand) : Criterion()
    data class Lt(val left: Operand.Prop, val right: Operand) : Criterion()
    data class Le(val left: Operand.Prop, val right: Operand) : Criterion()
    data class Like(val left: Operand.Prop, val right: Operand) : Criterion()
    data class NotLike(val left: Operand.Prop, val right: Operand) : Criterion()
    data class Between(val prop: Operand.Prop, val begin: Operand.Param, val end: Operand.Param) : Criterion()
    data class Exists(val context: SelectContext) : Criterion()
    data class NotExists(val context: SelectContext) : Criterion()
    data class InSingle(val left: Operand.Prop, val right: List<Operand.Param>) : Criterion()
    data class InPair(val left: Pair<Operand.Prop, Operand.Prop>, val right: List<Pair<Operand.Param, Operand.Param>>) : Criterion()
    data class InSelectSingle(val left: Operand.Prop, val right: SelectContext) : Criterion()
    data class InSelectPair(val left: Pair<Operand.Prop, Operand.Prop>, val right: SelectContext) : Criterion()
    data class NotInSingle(val left: Operand.Prop, val right: List<Operand.Param>) : Criterion()
    data class NotInPair(val left: Pair<Operand.Prop, Operand.Prop>, val right: List<Pair<Operand.Param, Operand.Param>>) : Criterion()
    data class NotInSelectSingle(val left: Operand.Prop, val right: SelectContext) : Criterion()
    data class NotInSelectPair(val left: Pair<Operand.Prop, Operand.Prop>, val right: SelectContext) : Criterion()
    data class Not(val list: List<Criterion>) : Criterion()
    data class And(val list: List<Criterion>) : Criterion()
    data class Or(val list: List<Criterion>) : Criterion()
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
