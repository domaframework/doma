package org.seasar.doma.criteria.declaration

import org.seasar.doma.criteria.context.Operand
import org.seasar.doma.def.PropertyDef
import org.seasar.doma.internal.jdbc.scalar.Scalars
import org.seasar.doma.internal.jdbc.sql.ScalarInParameter
import org.seasar.doma.jdbc.Config

class DeclarationSupport(private val config: Config) {

    fun <PROPERTY> toProp(propDef: PropertyDef<PROPERTY>): Operand.Prop {
        return Operand.Prop(propDef)
    }

    fun <PROPERTY> toParam(propDef: PropertyDef<PROPERTY>, value: PROPERTY?): Operand.Param {
        val clazz = propDef.asClass()
        val supplier = Scalars.wrap(value, clazz, false, config.classHelper)
        return Operand.Param(ScalarInParameter(supplier.get()))
    }
}
