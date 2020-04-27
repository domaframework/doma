package org.seasar.doma.criteria.declaration

import org.seasar.doma.criteria.context.Criterion
import org.seasar.doma.criteria.context.Join
import org.seasar.doma.criteria.context.Operand
import org.seasar.doma.def.PropertyDef

@Declaration
class JoinDeclaration(private val join: Join) {

    infix fun <PROPERTY> PropertyDef<PROPERTY>.eq(other: PropertyDef<PROPERTY>) {
        join.on.add(Criterion.Eq(Operand.Prop(this.asType()), Operand.Prop(other.asType())))
    }
}
