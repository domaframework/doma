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

import org.seasar.doma.jdbc.criteria.declaration.UserDefinedCriteriaContext
import org.seasar.doma.jdbc.criteria.metamodel.PropertyMetamodel
import org.seasar.doma.jdbc.dialect.Dialect

abstract class KComparisonDeclaration<DECLARATION : org.seasar.doma.jdbc.criteria.declaration.ComparisonDeclaration>(protected val declaration: DECLARATION) {

    fun <PROPERTY : Any> eq(left: PropertyMetamodel<PROPERTY>, right: PROPERTY?) {
        declaration.eq(left, right)
    }

    fun <PROPERTY : Any> eq(left: PropertyMetamodel<PROPERTY>, right: PropertyMetamodel<PROPERTY>) {
        declaration.eq(left, right)
    }

    fun <PROPERTY : Any> ne(left: PropertyMetamodel<PROPERTY>, right: PROPERTY?) {
        declaration.ne(left, right)
    }

    fun <PROPERTY : Any> ne(left: PropertyMetamodel<PROPERTY>, right: PropertyMetamodel<PROPERTY>) {
        declaration.ne(left, right)
    }

    fun <PROPERTY : Any> gt(left: PropertyMetamodel<PROPERTY>, right: PROPERTY?) {
        declaration.gt(left, right)
    }

    fun <PROPERTY : Any> gt(left: PropertyMetamodel<PROPERTY>, right: PropertyMetamodel<PROPERTY>) {
        declaration.gt(left, right)
    }

    fun <PROPERTY : Any> ge(left: PropertyMetamodel<PROPERTY>, right: PROPERTY?) {
        declaration.ge(left, right)
    }

    fun <PROPERTY : Any> ge(left: PropertyMetamodel<PROPERTY>, right: PropertyMetamodel<PROPERTY>) {
        declaration.ge(left, right)
    }

    fun <PROPERTY : Any> lt(left: PropertyMetamodel<PROPERTY>, right: PROPERTY?) {
        declaration.lt(left, right)
    }

    fun <PROPERTY : Any> lt(left: PropertyMetamodel<PROPERTY>, right: PropertyMetamodel<PROPERTY>) {
        declaration.lt(left, right)
    }

    fun <PROPERTY : Any> le(left: PropertyMetamodel<PROPERTY>, right: PROPERTY?) {
        declaration.le(left, right)
    }

    fun <PROPERTY : Any> le(left: PropertyMetamodel<PROPERTY>, right: PropertyMetamodel<PROPERTY>) {
        declaration.le(left, right)
    }

    fun <PROPERTY : Any> isNull(propertyMetamodel: PropertyMetamodel<PROPERTY>) {
        declaration.isNull(propertyMetamodel)
    }

    fun <PROPERTY : Any> isNotNull(propertyMetamodel: PropertyMetamodel<PROPERTY>) {
        declaration.isNotNull(propertyMetamodel)
    }

    fun <PROPERTY : Any> eqOrIsNull(left: PropertyMetamodel<PROPERTY>, right: PROPERTY?) {
        declaration.eqOrIsNull(left, right)
    }

    fun <PROPERTY : Any> neOrIsNotNull(left: PropertyMetamodel<PROPERTY>, right: PROPERTY?) {
        declaration.neOrIsNotNull(left, right)
    }

    fun and(block: () -> Unit) {
        declaration.and(block)
    }

    fun or(block: () -> Unit) {
        declaration.or(block)
    }

    fun not(block: () -> Unit) {
        declaration.not(block)
    }

    fun <EXTENSION> extension(
        construct: (KUserDefinedCriteriaContext) -> EXTENSION,
        extensionDeclaration: EXTENSION.() -> Unit,
    ) {
        declaration.extension(
            { context -> construct(context.toKotlin()) },
            extensionDeclaration
        )
    }

    private fun UserDefinedCriteriaContext.toKotlin(): KUserDefinedCriteriaContext {
        return KUserDefinedCriteriaContext { builderBlock ->
            this.add { builder ->
                builderBlock(object : KUserDefinedCriteriaContext.Builder {
                    override fun appendSql(sql: String) = builder.appendSql(sql)
                    override fun cutBackSql(length: Int) = builder.cutBackSql(length)
                    override fun appendExpression(propertyMetamodel: PropertyMetamodel<*>) =
                        builder.appendExpression(propertyMetamodel)
                    override fun <PROPERTY> appendParameter(left: PropertyMetamodel<PROPERTY>, right: PROPERTY?) =
                        builder.appendParameter(left, right)
                    override val dialect: Dialect
                        get() = builder.dialect
                })
            }
        }
    }
}
