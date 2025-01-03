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
package org.seasar.doma.kotlin.jdbc.criteria.declaration

import org.seasar.doma.jdbc.criteria.context.SubSelectContext
import org.seasar.doma.jdbc.criteria.declaration.SubSelectFromDeclaration
import org.seasar.doma.jdbc.criteria.declaration.WhereDeclaration
import org.seasar.doma.jdbc.criteria.metamodel.EntityMetamodel
import org.seasar.doma.jdbc.criteria.metamodel.PropertyMetamodel
import org.seasar.doma.jdbc.criteria.option.LikeOption
import org.seasar.doma.jdbc.criteria.tuple.Tuple2

class KWhereDeclaration(declaration: WhereDeclaration) : KComparisonDeclaration<WhereDeclaration>(
    declaration
) {

    fun like(left: PropertyMetamodel<*>, right: CharSequence?, option: LikeOption = LikeOption.none()) {
        declaration.like(left, right, option)
    }

    fun notLike(left: PropertyMetamodel<*>?, right: CharSequence?, option: LikeOption = LikeOption.none()) {
        declaration.notLike(left, right, option)
    }

    fun <PROPERTY : Any> between(
        propertyMetamodel: PropertyMetamodel<PROPERTY>,
        start: PROPERTY?,
        end: PROPERTY?,
    ) {
        declaration.between(propertyMetamodel, start, end)
    }

    fun <PROPERTY : Any> `in`(left: PropertyMetamodel<PROPERTY>, right: List<PROPERTY>?) {
        declaration.`in`(left, right)
    }

    fun <PROPERTY : Any> notIn(left: PropertyMetamodel<PROPERTY>, right: List<PROPERTY>?) {
        declaration.notIn(left, right)
    }

    fun <PROPERTY : Any> `in`(
        left: PropertyMetamodel<PROPERTY>,
        right: SubSelectContext<PropertyMetamodel<PROPERTY>>,
    ) {
        declaration.`in`(left, right)
    }

    fun <PROPERTY : Any> notIn(
        left: PropertyMetamodel<PROPERTY>,
        right: SubSelectContext<PropertyMetamodel<PROPERTY>>,
    ) {
        declaration.notIn(left, right)
    }

    fun <PROPERTY1 : Any, PROPERTY2 : Any> `in`(
        left: Tuple2<PropertyMetamodel<PROPERTY1>, PropertyMetamodel<PROPERTY2>>,
        right: List<Tuple2<PROPERTY1, PROPERTY2>>?,
    ) {
        declaration.`in`(left, right)
    }

    fun <PROPERTY1 : Any, PROPERTY2 : Any> notIn(
        left: Tuple2<PropertyMetamodel<PROPERTY1>, PropertyMetamodel<PROPERTY2>>,
        right: List<Tuple2<PROPERTY1, PROPERTY2>>?,
    ) {
        declaration.notIn(left, right)
    }

    fun <PROPERTY1 : Any, PROPERTY2 : Any> `in`(
        left: Tuple2<PropertyMetamodel<PROPERTY1>, PropertyMetamodel<PROPERTY2>>,
        right: SubSelectContext<Tuple2<PropertyMetamodel<PROPERTY1>, PropertyMetamodel<PROPERTY2>>>,
    ) {
        declaration.`in`(left, right)
    }

    fun <PROPERTY1 : Any, PROPERTY2 : Any> notIn(
        left: Tuple2<PropertyMetamodel<PROPERTY1>, PropertyMetamodel<PROPERTY2>>,
        right: SubSelectContext<Tuple2<PropertyMetamodel<PROPERTY1>, PropertyMetamodel<PROPERTY2>>>,
    ) {
        declaration.notIn(left, right)
    }

    fun exists(subSelectContext: SubSelectContext<*>) {
        declaration.exists(subSelectContext)
    }

    fun notExists(subSelectContext: SubSelectContext<*>) {
        declaration.notExists(subSelectContext)
    }

    fun <ENTITY : Any> from(entityMetamodel: EntityMetamodel<ENTITY>): KSubSelectFromDeclaration<ENTITY> {
        val declaration = SubSelectFromDeclaration<ENTITY>(entityMetamodel)
        return KSubSelectFromDeclaration(declaration)
    }
}
