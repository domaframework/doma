package org.seasar.doma.jdbc.criteria.statement

import org.seasar.doma.jdbc.Sql
import org.seasar.doma.jdbc.criteria.declaration.KOrderByIndexDeclaration
import org.seasar.doma.jdbc.criteria.declaration.OrderByIndexDeclaration

interface KSetOperator<ELEMENT> : KSetOperand<ELEMENT> {

    fun orderBy(block: KOrderByIndexDeclaration.() -> Unit): KSetOperand<ELEMENT>

    override fun peek(block: (Sql<*>) -> Unit): KSetOperator<ELEMENT>
}
