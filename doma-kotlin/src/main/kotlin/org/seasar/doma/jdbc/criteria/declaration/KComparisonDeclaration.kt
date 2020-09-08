package org.seasar.doma.jdbc.criteria.declaration

import org.seasar.doma.jdbc.criteria.metamodel.PropertyMetamodel

abstract class KComparisonDeclaration<DECLARATION : ComparisonDeclaration>(protected val declaration: DECLARATION) {

    fun <PROPERTY> eq(left: PropertyMetamodel<PROPERTY>, right: PROPERTY?) {
        declaration.eq(left, right)
    }

    fun <PROPERTY> eq(left: PropertyMetamodel<PROPERTY>, right: PropertyMetamodel<PROPERTY>) {
        declaration.eq(left, right)
    }

    fun <PROPERTY> ne(left: PropertyMetamodel<PROPERTY>, right: PROPERTY?) {
        declaration.ne(left, right)
    }

    fun <PROPERTY> ne(left: PropertyMetamodel<PROPERTY>, right: PropertyMetamodel<PROPERTY>) {
        declaration.ne(left, right)
    }

    fun <PROPERTY> gt(left: PropertyMetamodel<PROPERTY>, right: PROPERTY?) {
        declaration.gt(left, right)
    }

    fun <PROPERTY> gt(left: PropertyMetamodel<PROPERTY>, right: PropertyMetamodel<PROPERTY>) {
        declaration.gt(left, right)
    }

    fun <PROPERTY> ge(left: PropertyMetamodel<PROPERTY>, right: PROPERTY?) {
        declaration.ge(left, right)
    }

    fun <PROPERTY> ge(left: PropertyMetamodel<PROPERTY>, right: PropertyMetamodel<PROPERTY>) {
        declaration.ge(left, right)
    }

    fun <PROPERTY> lt(left: PropertyMetamodel<PROPERTY>, right: PROPERTY?) {
        declaration.lt(left, right)
    }

    fun <PROPERTY> lt(left: PropertyMetamodel<PROPERTY>, right: PropertyMetamodel<PROPERTY>) {
        declaration.lt(left, right)
    }

    fun <PROPERTY> le(left: PropertyMetamodel<PROPERTY>, right: PROPERTY?) {
        declaration.le(left, right)
    }

    fun <PROPERTY> le(left: PropertyMetamodel<PROPERTY>, right: PropertyMetamodel<PROPERTY>) {
        declaration.le(left, right)
    }

    fun <PROPERTY> isNull(propertyMetamodel: PropertyMetamodel<PROPERTY>) {
        declaration.isNull(propertyMetamodel)
    }

    fun <PROPERTY> isNotNull(propertyMetamodel: PropertyMetamodel<PROPERTY>) {
        declaration.isNotNull(propertyMetamodel)
    }

    fun <PROPERTY> eqOrIsNull(left: PropertyMetamodel<PROPERTY>, right: PROPERTY?) {
        declaration.eqOrIsNull(left, right)
    }

    fun <PROPERTY> neOrIsNotNull(left: PropertyMetamodel<PROPERTY>, right: PROPERTY?) {
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
}