package org.seasar.doma.criteria.declaration

import org.seasar.doma.criteria.context.UpdateContext
import org.seasar.doma.def.PropertyDef

@Declaration
class UpdateDeclaration(private val context: UpdateContext) {
    private val support = DeclarationSupport(context.config)
    private val valuesDeclaration = ValuesDeclaration()
    private val whereDeclaration = WhereDeclaration(context.config) { context.where.add(it) }

    fun set(block: ValuesDeclaration.(Values) -> Unit) {
        valuesDeclaration.block(object : Values {
            override fun <PROPERTY> set(propDef: PropertyDef<PROPERTY>, value: PROPERTY?) {
                val prop = support.toProp(propDef)
                val param = support.toParam(propDef, value)
                context.set[prop] = param
            }
        })
    }

    fun where(block: WhereDeclaration.() -> Unit) = whereDeclaration.block()
}
