package org.seasar.doma.criteria

import org.seasar.doma.jdbc.entity.EntityPropertyType

@Declaration
class JoinDeclaration(private val join: Join) {

    fun <BASIC> eq(left: EntityPropertyType<*, BASIC>, right: EntityPropertyType<*, BASIC>) {
        join.on.add(Criterion.Eq(Operand.Prop(left), Operand.Prop(right)))
    }
}
