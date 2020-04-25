package org.seasar.doma.criteria.declaration

import org.seasar.doma.criteria.context.Criterion
import org.seasar.doma.criteria.context.Join
import org.seasar.doma.criteria.context.Operand
import org.seasar.doma.jdbc.entity.EntityPropertyDesc

@Declaration
class JoinDeclaration(private val join: Join) {

    infix fun <BASIC, CONTAINER> EntityPropertyDesc<*, BASIC, CONTAINER>.eq(other: EntityPropertyDesc<*, BASIC, CONTAINER>) {
        join.on.add(Criterion.Eq(Operand.Prop(this), Operand.Prop(other)))
    }
}
