package org.seasar.doma.criteria

@Declaration
open class UpdateDeclaration(private val context: UpdateContext) {
    private val valuesDeclaration = ValuesDeclaration(context.config) { (key, value) -> context.set[key] = value }
    private val whereDeclaration = WhereDeclaration(context.config) { context.where.add(it) }

    fun set(block: ValuesDeclaration.() -> Unit) = valuesDeclaration.block()

    fun where(block: WhereDeclaration.() -> Unit) = whereDeclaration.block()
}
