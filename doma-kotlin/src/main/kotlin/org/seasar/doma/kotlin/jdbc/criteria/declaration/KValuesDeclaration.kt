package org.seasar.doma.kotlin.jdbc.criteria.declaration

import org.seasar.doma.jdbc.criteria.declaration.ValuesDeclaration
import org.seasar.doma.jdbc.criteria.metamodel.PropertyMetamodel

class KValuesDeclaration(val declaration: ValuesDeclaration) {

    fun <PROPERTY> value(left: PropertyMetamodel<PROPERTY>, right: PROPERTY?) {
        declaration.value(left, right)
    }
}
