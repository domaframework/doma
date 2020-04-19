package org.seasar.doma.criteria

import org.seasar.doma.internal.jdbc.sql.BasicInParameter
import org.seasar.doma.jdbc.entity.EntityPropertyType
import org.seasar.doma.jdbc.entity.EntityType

class SelectContext(
    val entityType: EntityType<*>,
    var distinct: Boolean = false,
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

sealed class Operand {
    data class Param(val value: BasicInParameter<*>) : Operand()
    data class Prop(val value: EntityPropertyType<*, *>) : Operand()
}

sealed class Criterion {
    data class Eq(val left: Operand.Prop, val right: Operand) : Criterion()
    data class Ne(val left: Operand.Prop, val right: Operand) : Criterion()
    data class Gt(val left: Operand.Prop, val right: Operand) : Criterion()
    data class Ge(val left: Operand.Prop, val right: Operand) : Criterion()
    data class Lt(val left: Operand.Prop, val right: Operand) : Criterion()
    data class Le(val left: Operand.Prop, val right: Operand) : Criterion()
    data class In(val left: Operand.Prop, val right: List<Operand.Param>) : Criterion()

    //    data class NotIn(val prop: KProperty1<*, *>, val values: List<*>) : Criterion()
//    data class In2(val props: Pair<KProperty1<*, *>, KProperty1<*, *>>, val values: List<Pair<*, *>>) : Criterion()
//    data class NotIn2(val props: Pair<KProperty1<*, *>, KProperty1<*, *>>, val values: List<Pair<*, *>>) :
//        Criterion()
//
//    data class In3(
//        val props: Triple<KProperty1<*, *>, KProperty1<*, *>, KProperty1<*, *>>,
//        val values: List<Triple<*, *, *>>
//    ) : Criterion()
//
//    data class NotIn3(
//        val props: Triple<KProperty1<*, *>, KProperty1<*, *>, KProperty1<*, *>>,
//        val values: List<Triple<*, *, *>>
//    ) : Criterion()
//
//    data class Like(val prop: KProperty1<*, *>, val value: String?) : Criterion()
//    data class NotLike(val prop: KProperty1<*, *>, val value: String?) : Criterion()
    data class Between(val prop: Operand.Prop, val begin: Operand.Param, val end: Operand.Param) : Criterion()
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
