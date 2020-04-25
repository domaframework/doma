package org.seasar.doma.criteria.declaration

import org.seasar.doma.jdbc.entity.EntityPropertyType

@Declaration
class OrderByDeclaration(private val add: (Pair<EntityPropertyType<*, *>, String>) -> Unit) {

    fun EntityPropertyType<*, *>.desc() {
        add(this to "desc")
    }

    fun EntityPropertyType<*, *>.asc() {
        add(this to "asc")
    }
}
