package org.seasar.doma.criteria.context

import org.seasar.doma.jdbc.InParameter
import org.seasar.doma.jdbc.entity.EntityPropertyType
import org.seasar.doma.jdbc.entity.EntityType

interface Context {
    val entityTypes: List<EntityType<*>>
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
    data class NotInSingle(val left: Operand.Prop, val right: List<Operand.Param>) : Criterion()
    data class InPair(val left: Pair<Operand.Prop, Operand.Prop>, val right: List<Pair<Operand.Param, Operand.Param>>) : Criterion()
    data class NotInPair(val left: Pair<Operand.Prop, Operand.Prop>, val right: List<Pair<Operand.Param, Operand.Param>>) : Criterion()
    data class InSingleSubQuery(val left: Operand.Prop, val right: SelectContext) : Criterion()
    data class NotInSingleSubQuery(val left: Operand.Prop, val right: SelectContext) : Criterion()
    data class InPairSubQuery(val left: Pair<Operand.Prop, Operand.Prop>, val right: SelectContext) : Criterion()
    data class NotInPairSubQuery(val left: Pair<Operand.Prop, Operand.Prop>, val right: SelectContext) : Criterion()
    data class Not(val list: List<Criterion>) : Criterion()
    data class And(val list: List<Criterion>) : Criterion()
    data class Or(val list: List<Criterion>) : Criterion()
}
