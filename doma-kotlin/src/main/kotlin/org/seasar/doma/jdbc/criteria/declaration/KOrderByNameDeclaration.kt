package org.seasar.doma.jdbc.criteria.declaration

import org.seasar.doma.jdbc.criteria.metamodel.PropertyMetamodel

class KOrderByNameDeclaration(private val declaration: OrderByNameDeclaration) {

    fun asc(propertyMetamodel: PropertyMetamodel<*>) {
        declaration.asc(propertyMetamodel)
    }

    fun desc(propertyMetamodel: PropertyMetamodel<*>) {
        declaration.desc(propertyMetamodel)
    }
}