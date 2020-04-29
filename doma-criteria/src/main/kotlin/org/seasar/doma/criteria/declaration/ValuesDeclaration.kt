package org.seasar.doma.criteria.declaration

import org.seasar.doma.criteria.context.Operand
import org.seasar.doma.jdbc.Config

@Declaration
class ValuesDeclaration(val config: Config) {
    private val expressionDeclaration = ExpressionDeclaration(config)

    fun expression(block: ExpressionDeclaration.() -> Operand.Expr): Operand.Expr {
        return expressionDeclaration.block()
    }
}
