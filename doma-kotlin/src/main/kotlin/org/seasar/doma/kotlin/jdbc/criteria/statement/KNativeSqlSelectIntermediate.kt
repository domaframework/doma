/*
 * Copyright Doma Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.seasar.doma.kotlin.jdbc.criteria.statement

import org.seasar.doma.jdbc.Sql
import org.seasar.doma.jdbc.criteria.context.SetOperationContext
import org.seasar.doma.jdbc.criteria.statement.SetOperand
import java.util.stream.Stream
import kotlin.streams.asSequence

class KNativeSqlSelectIntermediate<ELEMENT>(private val statement: SetOperand<ELEMENT>) : KStatement<List<ELEMENT>>, KSetOperand<ELEMENT> {

    override val context: SetOperationContext<ELEMENT>
        get() = statement.context

    override fun openStream(): Stream<ELEMENT> {
        return statement.openStream()
    }

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
