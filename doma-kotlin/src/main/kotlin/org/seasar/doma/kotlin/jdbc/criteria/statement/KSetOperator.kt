package org.seasar.doma.kotlin.jdbc.criteria.statement

import org.seasar.doma.jdbc.Sql
import org.seasar.doma.kotlin.jdbc.criteria.declaration.KOrderByIndexDeclaration

interface KSetOperator<ELEMENT> : KSetOperand<ELEMENT> {

    fun orderBy(block: KOrderByIndexDeclaration.() -> Unit): KSetOperand<ELEMENT>

    override fun peek(block: (Sql<*>) -> Unit): KSetOperator<ELEMENT>
}
