package org.seasar.doma.jdbc.criteria.statement

import org.seasar.doma.jdbc.Sql
import org.seasar.doma.jdbc.criteria.context.SetOperationContext
import org.seasar.doma.jdbc.criteria.declaration.KOrderByIndexDeclaration
import kotlin.streams.asSequence

class KNativeSqlSetStarting<ELEMENT>(val statement: SetOperator<ELEMENT>) : KSetOperator<ELEMENT> {

    override fun orderBy(block: KOrderByIndexDeclaration.() -> Unit): KSetOperand<ELEMENT> {
        statement.orderBy { block(KOrderByIndexDeclaration(it)) }
        return this
    }

    override fun execute(): List<ELEMENT> {
        return statement.execute()
    }

    override fun peek(block: (Sql<*>) -> Unit): KSetOperator<ELEMENT> {
        statement.peek(block)
        return this
    }

    override fun asSql(): Sql<*> {
        return statement.asSql()
    }

    override fun <RESULT> mapSequence(sequenceMapper: (Sequence<ELEMENT>) -> RESULT): RESULT {
        return statement.mapStream {
            sequenceMapper(it.asSequence())
        }
    }

    override val context: SetOperationContext<ELEMENT>
        get() = statement.context

    override fun union(other: KSetOperand<ELEMENT>): KSetOperator<ELEMENT> {
        val setOperator = statement.union(other.asSetOperand())
        return KNativeSqlSetStarting(setOperator)
    }

    override fun unionAll(other: KSetOperand<ELEMENT>): KSetOperator<ELEMENT> {
        val setOperator = statement.unionAll(other.asSetOperand())
        return KNativeSqlSetStarting(setOperator)
    }

    override fun asSetOperand(): SetOperand<ELEMENT> {
        return statement
    }
}
