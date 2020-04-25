package org.seasar.doma.criteria.declaration

import org.seasar.doma.criteria.context.Criterion
import org.seasar.doma.criteria.context.Join
import org.seasar.doma.criteria.context.Operand
import org.seasar.doma.jdbc.entity.EntityPropertyType

@Declaration
class JoinDeclaration(private val join: Join) {

    fun <BASIC> eq(left: EntityPropertyType<*, BASIC>, right: EntityPropertyType<*, BASIC>) {
        join.on.add(Criterion.Eq(Operand.Prop(left), Operand.Prop(right)))
    }
}
