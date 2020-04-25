package org.seasar.doma.criteria.declaration

import org.seasar.doma.criteria.context.UpdateContext
import org.seasar.doma.jdbc.entity.EntityPropertyDesc

@Declaration
open class UpdateDeclaration(private val context: UpdateContext) {
    private val support = DeclarationSupport(context.config)
    private val valuesDeclaration = ValuesDeclaration()
    private val whereDeclaration = WhereDeclaration(context.config) { context.where.add(it) }

    fun set(block: ValuesDeclaration.(Values) -> Unit) {
        valuesDeclaration.block(object : Values {
            override fun <CONTAINER> set(propType: EntityPropertyDesc<*, *, CONTAINER>, value: CONTAINER?) {
                val prop = support.toProp(propType)
                val param = support.toParam(propType, value)
                context.set[prop] = param
            }
        })
    }

    fun where(block: WhereDeclaration.() -> Unit) = whereDeclaration.block()
}
