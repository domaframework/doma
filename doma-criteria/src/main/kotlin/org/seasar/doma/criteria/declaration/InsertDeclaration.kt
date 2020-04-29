package org.seasar.doma.criteria.declaration

import org.seasar.doma.criteria.context.InsertContext
import org.seasar.doma.def.PropertyDef

@Declaration
class InsertDeclaration(private val context: InsertContext) {
    private val support = DeclarationSupport(context.config)
    private val valuesDeclaration = ValuesDeclaration(context.config)

    fun values(block: ValuesDeclaration.(Values) -> Unit) {
        valuesDeclaration.block(Values())
    }

    inner class Values {
        operator fun <PROPERTY> set(propDef: PropertyDef<PROPERTY>, value: PROPERTY?) {
            val prop = support.toProp(propDef)
            val param = support.toParam(propDef, value)
            context.values[prop] = param
        }
    }
}
