package org.seasar.doma.kotlin.jdbc.criteria.statement

import org.seasar.doma.jdbc.Sql
import org.seasar.doma.jdbc.criteria.context.SetOperationContext
import org.seasar.doma.jdbc.criteria.statement.SetOperand
import kotlin.streams.asSequence

class KNativeSqlSelectIntermediate<ELEMENT>(private val statement: SetOperand<ELEMENT>) : KStatement<List<ELEMENT>>, KSetOperand<ELEMENT> {

    override val context: SetOperationContext<ELEMENT>
        get() = statement.context

    override fun <RESULT> mapSequence(sequenceMapper: (Sequence<ELEMENT>) -> RESULT): RESULT {
        return statement.mapStream {
            sequenceMapper(it.asSequence())
        }
    }

    override fun union(other: KSetOperand<ELEMENT>): KSetOperator<ELEMENT> {
        val setOperator = statement.union(other.asSetOperand())
        return KNativeSqlSetStarting(setOperator)
    }

    override fun unionAll(other: KSetOperand<ELEMENT>): KSetOperator<ELEMENT> {
        val setOperator = statement.unionAll(other.asSetOperand())
        return KNativeSqlSetStarting(setOperator)
    }

    override fun peek(block: (Sql<*>) -> Unit): KSetOperand<ELEMENT> {
        statement.peek(block)
        return this
    }

    override fun execute(): List<ELEMENT> {
        return statement.execute()
    }

    override fun asSql(): Sql<*> {
        return statement.asSql()
    }

    override fun asSetOperand(): SetOperand<ELEMENT> {
        return statement
    }
}
