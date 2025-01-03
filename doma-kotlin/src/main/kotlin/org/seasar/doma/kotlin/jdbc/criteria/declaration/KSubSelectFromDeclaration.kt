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

import org.seasar.doma.jdbc.criteria.context.SelectContext
import org.seasar.doma.jdbc.criteria.context.SubSelectContext
import org.seasar.doma.jdbc.criteria.declaration.OrderByNameDeclaration
import org.seasar.doma.jdbc.criteria.declaration.SubSelectFromDeclaration
import org.seasar.doma.jdbc.criteria.metamodel.EntityMetamodel
import org.seasar.doma.jdbc.criteria.metamodel.PropertyMetamodel
import org.seasar.doma.jdbc.criteria.tuple.Tuple2

class KSubSelectFromDeclaration<ENTITY>(private val declaration: SubSelectFromDeclaration<ENTITY>) :
    SubSelectContext<ENTITY> {

    override fun get(): SelectContext {
        return declaration.get()
    }

    fun innerJoin(
        entityMetamodel: EntityMetamodel<*>,
        block: KJoinDeclaration.() -> Unit,
    ): KSubSelectFromDeclaration<ENTITY> {
        declaration.innerJoin(entityMetamodel) { block(KJoinDeclaration(it)) }
        return this
    }

    fun leftJoin(
        entityMetamodel: EntityMetamodel<*>,
        block: KJoinDeclaration.() -> Unit,
    ): KSubSelectFromDeclaration<ENTITY> {
        declaration.leftJoin(entityMetamodel) { block(KJoinDeclaration(it)) }
        return this
    }

    fun where(block: KWhereDeclaration.() -> Unit): KSubSelectFromDeclaration<ENTITY> {
        declaration.where { block(KWhereDeclaration(it)) }
        return this
    }

    fun groupBy(vararg propertyMetamodels: PropertyMetamodel<*>): KSubSelectFromDeclaration<ENTITY> {
        declaration.groupBy(*propertyMetamodels)
        return this
    }

    fun having(block: KHavingDeclaration.() -> Unit): KSubSelectFromDeclaration<ENTITY> {
        declaration.having {
            block(
                KHavingDeclaration(
                    it,
                ),
            )
        }
        return this
    }

    fun orderBy(block: OrderByNameDeclaration.() -> Unit): KSubSelectFromDeclaration<ENTITY> {
        declaration.orderBy(block)
        return this
    }

    fun select(): SubSelectContext<EntityMetamodel<ENTITY>> {
        return declaration.select()
    }

    fun select(entityMetamodel: EntityMetamodel<*>): SubSelectContext<List<PropertyMetamodel<*>>> {
        return declaration.select(entityMetamodel)
    }

    fun <PROPERTY : Any> select(propertyMetamodel: PropertyMetamodel<PROPERTY>): SubSelectContext.Single<PROPERTY> {
        return declaration.select(propertyMetamodel)
    }

    fun <PROPERTY1 : Any, PROPERTY2 : Any> select(
        first: PropertyMetamodel<PROPERTY1>,
        second: PropertyMetamodel<PROPERTY2>,
    ): SubSelectContext<Tuple2<PropertyMetamodel<PROPERTY1>, PropertyMetamodel<PROPERTY2>>> {
        return declaration.select(first, second)
    }

    fun select(
        propertyMetamodel1: PropertyMetamodel<*>,
        propertyMetamodel2: PropertyMetamodel<*>,
        vararg propertyMetamodels: PropertyMetamodel<*>,
    ): SubSelectContext<List<PropertyMetamodel<*>>> {
        return declaration.select(propertyMetamodel1, propertyMetamodel2, *propertyMetamodels)
    }
}
