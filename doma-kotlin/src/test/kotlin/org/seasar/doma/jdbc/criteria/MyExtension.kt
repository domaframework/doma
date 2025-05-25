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
package org.seasar.doma.jdbc.criteria

import org.seasar.doma.jdbc.criteria.metamodel.PropertyMetamodel
import org.seasar.doma.kotlin.jdbc.criteria.declaration.KUserDefinedCriteriaContext

data class MyExtension(val context: KUserDefinedCriteriaContext) {
    fun likeMultiple(propertyMetamodel: PropertyMetamodel<String>, vararg patterns: String) {
        context.add {
            patterns.forEach { pattern ->
                appendExpression(propertyMetamodel)
                appendSql(" like ")
                appendParameter(propertyMetamodel, "%$pattern%")
                appendSql(" escape '\\'")
                appendSql(" and ")
            }
            cutBackSql(5)
        }
    }

    fun eq2(entityMetamodel: PropertyMetamodel<String?>, pattern: String?) {
        context.add {
            appendExpression(entityMetamodel)
            appendSql(" = ")
            appendParameter(entityMetamodel, pattern)
        }
    }
}
