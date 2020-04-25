package org.seasar.doma.criteria

@Declaration
open class DeleteDeclaration(private val context: DeleteContext) {
    private val whereDeclaration = WhereDeclaration(context.config) { context.where.add(it) }

    fun where(block: WhereDeclaration.() -> Unit) = whereDeclaration.block()
}
