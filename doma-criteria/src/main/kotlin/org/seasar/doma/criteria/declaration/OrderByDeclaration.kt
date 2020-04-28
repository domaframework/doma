package org.seasar.doma.criteria.declaration

import org.seasar.doma.def.PropertyDef

@Declaration
class OrderByDeclaration(private val add: (Pair<PropertyDef<*>, String>) -> Unit) {

    fun PropertyDef<*>.desc() {
        add(this to "desc")
    }

    fun PropertyDef<*>.asc() {
        add(this to "asc")
    }
}
