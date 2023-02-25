package org.seasar.doma.kotlin.jdbc.criteria.declaration

import org.seasar.doma.jdbc.criteria.declaration.SetDeclaration
import org.seasar.doma.jdbc.criteria.metamodel.PropertyMetamodel

class KSetDeclaration(private val declaration: SetDeclaration) {

    fun <PROPERTY> value(left: PropertyMetamodel<PROPERTY>, right: PROPERTY?) {
        declaration.value(left, right)
    }

    fun <PROPERTY> value(
        left: PropertyMetamodel<PROPERTY>,
        right: PropertyMetamodel<PROPERTY>,
    ) {
        declaration.value(left, right)
    }
}
