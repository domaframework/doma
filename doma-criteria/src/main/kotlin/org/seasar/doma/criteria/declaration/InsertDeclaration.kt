package org.seasar.doma.criteria.declaration

import org.seasar.doma.criteria.context.InsertContext

@Declaration
class InsertDeclaration(private val context: InsertContext) {

    private val valuesDeclaration = ValuesDeclaration(context.config) { (key, value) -> context.values[key] = value }

    fun values(block: ValuesDeclaration.() -> Unit) = valuesDeclaration.block()
}
