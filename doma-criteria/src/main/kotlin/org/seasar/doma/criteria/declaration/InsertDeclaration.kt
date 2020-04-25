package org.seasar.doma.criteria.declaration

import org.seasar.doma.criteria.context.InsertContext
import org.seasar.doma.jdbc.entity.EntityPropertyDesc

@Declaration
class InsertDeclaration(private val context: InsertContext) {
    private val support = DeclarationSupport(context.config)
    private val valuesDeclaration = ValuesDeclaration()

    fun values(block: ValuesDeclaration.(Values) -> Unit) {
        valuesDeclaration.block(object : Values {
            override fun <CONTAINER> set(propType: EntityPropertyDesc<*, *, CONTAINER>, value: CONTAINER?) {
                val prop = support.toProp(propType)
                val param = support.toParam(propType, value)
                context.values[prop] = param
            }
        })
    }
}
