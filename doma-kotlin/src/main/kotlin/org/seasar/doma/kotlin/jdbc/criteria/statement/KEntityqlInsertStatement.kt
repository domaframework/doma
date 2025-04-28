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

import org.seasar.doma.jdbc.Result
import org.seasar.doma.jdbc.Sql
import org.seasar.doma.jdbc.criteria.metamodel.PropertyMetamodel
import org.seasar.doma.jdbc.criteria.statement.EntityqlInsertStatement

class KEntityqlInsertStatement<ENTITY>(
    private val statement: EntityqlInsertStatement<ENTITY>,
) : KStatement<Result<ENTITY>> {

    fun onDuplicateKeyUpdate(): KEntityqlUpsertStatement<ENTITY> {
        return KEntityqlUpsertStatement(statement.onDuplicateKeyUpdate())
    }

    fun onDuplicateKeyIgnore(): KEntityqlUpsertStatement<ENTITY> {
        return KEntityqlUpsertStatement(statement.onDuplicateKeyIgnore())
    }

    fun returning(vararg properties: PropertyMetamodel<*>): KStatement<Result<ENTITY>> {
        val s = statement.returning(*properties)
        return KStatementAdapter(s)
    }

    override fun execute(): Result<ENTITY> {
        return statement.execute()
    }

    override fun asSql(): Sql<*> {
        return statement.asSql()
    }
}
