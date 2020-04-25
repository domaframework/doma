package org.seasar.doma.criteria.declaration

import java.util.Optional
import java.util.OptionalDouble
import java.util.OptionalInt
import java.util.OptionalLong
import org.seasar.doma.criteria.context.Operand
import org.seasar.doma.internal.jdbc.scalar.Scalars
import org.seasar.doma.internal.jdbc.sql.ScalarInParameter
import org.seasar.doma.jdbc.Config
import org.seasar.doma.jdbc.entity.EntityPropertyDesc

class DeclarationSupport(private val config: Config) {
    fun <CONTAINER> toProp(propType: EntityPropertyDesc<*, *, CONTAINER>): Operand.Prop {
        return Operand.Prop(propType)
    }

    // TODO simplify
    fun <CONTAINER> toParam(propType: EntityPropertyDesc<*, *, CONTAINER>, value: CONTAINER?): Operand.Param {
        val v = when (value) {
            is Optional<*> -> if (value.isPresent) value.get() else null
            is OptionalInt -> if (value.isPresent) value.asInt else null
            is OptionalLong -> if (value.isPresent) value.asLong else null
            is OptionalDouble -> if (value.isPresent) value.asDouble else null
            else -> value
        }
        val prop = propType.createProperty()
        val clazz = if (prop.domainClass.isPresent) {
            prop.domainClass.get()
        } else {
            prop.wrapper.basicClass
        }
        val supplier = Scalars.wrap(v, clazz, false, config.classHelper)
        return Operand.Param(ScalarInParameter(supplier.get()))
    }
}
