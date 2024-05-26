package org.seasar.doma.kotlin.jdbc.criteria.statement

import org.seasar.doma.jdbc.Sql
import org.seasar.doma.jdbc.criteria.context.SetOperationContext
import org.seasar.doma.jdbc.criteria.statement.SetOperand
import org.seasar.doma.jdbc.criteria.statement.SetOperator
import org.seasar.doma.kotlin.jdbc.criteria.declaration.KOrderByIndexDeclaration
import java.util.stream.Stream
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

    /**
     * Open a stream.
     *
     * You must close the stream after using it.
     *
     * @return the opened stream
     */
    override fun openStream(): Stream<ELEMENT> {
        return statement.openStream()
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
