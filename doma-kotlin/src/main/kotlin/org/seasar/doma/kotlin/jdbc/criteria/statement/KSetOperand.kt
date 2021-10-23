package org.seasar.doma.kotlin.jdbc.criteria.statement

import org.seasar.doma.jdbc.Sql
import org.seasar.doma.jdbc.criteria.context.SetOperationContext
import org.seasar.doma.jdbc.criteria.statement.SetOperand

interface KSetOperand<ELEMENT> : KSequenceMappable<ELEMENT> {

    val context: SetOperationContext<ELEMENT>

    fun union(other: KSetOperand<ELEMENT>): KSetOperator<ELEMENT>

    fun unionAll(other: KSetOperand<ELEMENT>): KSetOperator<ELEMENT>

    fun asSetOperand(): SetOperand<ELEMENT>

    override fun peek(block: (Sql<*>) -> Unit): KSetOperand<ELEMENT>
}
