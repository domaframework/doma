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

import org.seasar.doma.jdbc.BatchResult
import org.seasar.doma.jdbc.criteria.statement.UnifiedDeleteStarting
import org.seasar.doma.kotlin.jdbc.criteria.declaration.KWhereDeclaration

class KUnifiedDeleteStarting<ENTITY : Any>(private val statement: UnifiedDeleteStarting<ENTITY>) {

    fun single(entity: ENTITY): KEntityqlDeleteStatement<ENTITY> {
        return KEntityqlDeleteStatement(statement.single(entity))
    }

    fun batch(entities: List<ENTITY>): KStatement<BatchResult<ENTITY>> {
        return KEntityqlBatchDeleteStatement(statement.batch(entities))
    }

    fun where(block: KWhereDeclaration.() -> Unit): KStatement<Int> {
        val whereStatement = statement.where { block(KWhereDeclaration(it)) }
        return object : KStatement<Int> {
            override fun execute(): Int {
                return whereStatement.execute()
            }

            override fun asSql(): org.seasar.doma.jdbc.Sql<*> {
                return whereStatement.asSql()
            }
        }
    }

    fun all(): KStatement<Int> {
        val deleteAll = statement.all()
        return object : KStatement<Int> {
            override fun execute(): Int {
                return deleteAll.execute()
            }

            override fun asSql(): org.seasar.doma.jdbc.Sql<*> {
                return deleteAll.asSql()
            }
        }
    }
}
