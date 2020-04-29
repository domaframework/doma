package org.seasar.doma.criteria.declaration

import org.seasar.doma.criteria.context.Operand
import org.seasar.doma.def.PropertyDef
import org.seasar.doma.jdbc.Config

@Declaration
class ExpressionDeclaration(private val config: Config) {

    private val support = DeclarationSupport(config)

    infix operator fun <PROPERTY> PropertyDef<PROPERTY>.plus(other: PROPERTY?): Operand.Expr {
        val prop = support.toProp(this)
        val param = support.toParam(this, other)
        return Operand.Expr.Plus(Operand.Expr.Prop(prop), Operand.Expr.Param(param))
    }

    infix operator fun <PROPERTY> PROPERTY?.plus(other: PropertyDef<PROPERTY>): Operand.Expr {
        val param = support.toParam(other, this)
        val prop = support.toProp(other)
        return Operand.Expr.Plus(Operand.Expr.Param(param), Operand.Expr.Prop(prop))
    }
}
