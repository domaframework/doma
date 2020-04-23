package org.seasar.doma.criteria

import java.util.Optional
import java.util.OptionalDouble
import java.util.OptionalInt
import java.util.OptionalLong
import org.seasar.doma.internal.jdbc.scalar.Scalars
import org.seasar.doma.internal.jdbc.sql.ScalarInParameter
import org.seasar.doma.jdbc.Config
import org.seasar.doma.jdbc.entity.EntityPropertyDesc

@Declaration
@Suppress("FunctionName")
class HavingDeclaration(private val config: Config, private val add: (Criterion) -> Unit) {

    fun <ENTITY, BASIC, CONTAINER> count(propDesc: EntityPropertyDesc<ENTITY, BASIC, CONTAINER>): Count<ENTITY, BASIC, CONTAINER> {
        return Count(propDesc)
    }

    fun <CONTAINER> eq(left: EntityPropertyDesc<*, *, CONTAINER>, right: CONTAINER?) {
        add(Criterion.Eq(toProp(left), toParam(left, right)))
    }

    fun <CONTAINER> eq(left: EntityPropertyDesc<*, *, CONTAINER>, right: EntityPropertyDesc<*, *, CONTAINER>) {
        add(Criterion.Eq(toProp(left), toProp(right)))
    }

    fun <CONTAINER> ne(left: EntityPropertyDesc<*, *, CONTAINER>, right: CONTAINER?) {
        add(Criterion.Ne(toProp(left), toParam(left, right)))
    }

    fun <CONTAINER> ne(left: EntityPropertyDesc<*, *, CONTAINER>, right: EntityPropertyDesc<*, *, CONTAINER>) {
        add(Criterion.Ne(toProp(left), toProp(right)))
    }

    fun <CONTAINER> gt(left: EntityPropertyDesc<*, *, CONTAINER>, right: CONTAINER?) {
        add(Criterion.Gt(toProp(left), toParam(left, right)))
    }

    fun <CONTAINER> gt(left: EntityPropertyDesc<*, *, CONTAINER>, right: EntityPropertyDesc<*, *, CONTAINER>) {
        add(Criterion.Gt(toProp(left), toProp(right)))
    }

    fun <CONTAINER> ge(left: EntityPropertyDesc<*, *, CONTAINER>, right: CONTAINER?) {
        add(Criterion.Ge(toProp(left), toParam(left, right)))
    }

    fun <CONTAINER> ge(left: EntityPropertyDesc<*, *, CONTAINER>, right: EntityPropertyDesc<*, *, CONTAINER>) {
        add(Criterion.Ge(toProp(left), toProp(right)))
    }

    fun <CONTAINER> lt(left: EntityPropertyDesc<*, *, CONTAINER>, right: CONTAINER?) {
        add(Criterion.Lt(toProp(left), toParam(left, right)))
    }

    fun <CONTAINER> lt(left: EntityPropertyDesc<*, *, CONTAINER>, right: EntityPropertyDesc<*, *, CONTAINER>) {
        add(Criterion.Lt(toProp(left), toProp(right)))
    }

    fun <CONTAINER> le(left: EntityPropertyDesc<*, *, CONTAINER>, right: CONTAINER?) {
        add(Criterion.Le(toProp(left), toParam(left, right)))
    }

    fun <CONTAINER> le(left: EntityPropertyDesc<*, *, CONTAINER>, right: EntityPropertyDesc<*, *, CONTAINER>) {
        add(Criterion.Le(toProp(left), toProp(right)))
    }

    fun not(block: HavingDeclaration.() -> Unit) = runBlock(block, Criterion::Not)

    fun and(block: HavingDeclaration.() -> Unit) = runBlock(block, Criterion::And)

    fun or(block: HavingDeclaration.() -> Unit) = runBlock(block, Criterion::Or)

    private fun runBlock(block: HavingDeclaration.() -> Unit, newCriterion: (List<Criterion>) -> Criterion) {
        val criterionList = mutableListOf<Criterion>()
        val declaration = HavingDeclaration(config) { criterionList.add(it) }
        declaration.block()
        if (criterionList.isNotEmpty()) {
            add(newCriterion(criterionList))
        }
    }

    private fun <CONTAINER> toProp(propType: EntityPropertyDesc<*, *, CONTAINER>): Operand.Prop {
        return Operand.Prop(propType)
    }

    // TODO simplify
    private fun <CONTAINER> toParam(propType: EntityPropertyDesc<*, *, CONTAINER>, value: CONTAINER?): Operand.Param {
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
