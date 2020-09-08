package org.seasar.doma.jdbc.criteria.declaration

class KOrderByIndexDeclaration(val declaration: OrderByIndexDeclaration) {

    fun asc(index: Int) {
        declaration.asc(index)
    }

    fun desc(index: Int) {
        declaration.desc(index)
    }
}