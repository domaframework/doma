package org.seasar.doma.kotlin.jdbc.criteria.declaration

import org.seasar.doma.jdbc.criteria.declaration.OrderByIndexDeclaration

class KOrderByIndexDeclaration(val declaration: OrderByIndexDeclaration) {

    fun asc(index: Int) {
        declaration.asc(index)
    }

    fun desc(index: Int) {
        declaration.desc(index)
    }
}
