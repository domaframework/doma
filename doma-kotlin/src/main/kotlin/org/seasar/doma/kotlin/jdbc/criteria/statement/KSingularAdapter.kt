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
import org.seasar.doma.jdbc.criteria.statement.Singular

class KSingularAdapter<RESULT>(private val singular: Singular<RESULT>) : KSingular<RESULT> {
    override fun peek(block: (Sql<*>) -> Unit): KSingular<RESULT> {
        return KSingularAdapter(singular.peek(block))
    }

    override fun execute(): RESULT {
        return singular.execute()
    }

    override fun asSql(): Sql<*> {
        return singular.asSql()
    }
}
