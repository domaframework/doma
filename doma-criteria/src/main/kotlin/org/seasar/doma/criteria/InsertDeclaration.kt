package org.seasar.doma.criteria

@Declaration
class InsertDeclaration(private val context: InsertContext) {

    private val valuesDeclaration = ValuesDeclaration(context.config) { (key, value) -> context.values[key] = value }

    fun values(block: ValuesDeclaration.() -> Unit) = valuesDeclaration.block()
}
