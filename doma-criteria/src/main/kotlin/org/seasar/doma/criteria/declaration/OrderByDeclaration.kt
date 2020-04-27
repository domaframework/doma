package org.seasar.doma.criteria.declaration

import org.seasar.doma.def.PropertyDef
import org.seasar.doma.jdbc.entity.EntityPropertyType

@Declaration
class OrderByDeclaration(private val add: (Pair<EntityPropertyType<*, *>, String>) -> Unit) {

    fun PropertyDef<*>.desc() {
        add(this.asType() to "desc")
    }

    fun PropertyDef<*>.asc() {
        add(this.asType() to "asc")
    }
}
