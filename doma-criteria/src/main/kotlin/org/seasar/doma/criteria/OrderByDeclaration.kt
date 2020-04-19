package org.seasar.doma.criteria

import org.seasar.doma.jdbc.entity.EntityPropertyType

@Declaration
class OrderByDeclaration(private val add: (Pair<EntityPropertyType<*, *>, String>) -> Unit) {

    fun desc(prop: EntityPropertyType<*, *>) {
        add(prop to "desc")
    }

    fun asc(prop: EntityPropertyType<*, *>) {
        add(prop to "asc")
    }
}
