package org.seasar.doma.criteria

import java.util.Optional
import java.util.OptionalDouble
import java.util.OptionalInt
import java.util.OptionalLong
import org.seasar.doma.internal.jdbc.scalar.Scalars
import org.seasar.doma.internal.jdbc.sql.ScalarInParameter
import org.seasar.doma.jdbc.Config
import org.seasar.doma.jdbc.entity.DefaultPropertyType
import org.seasar.doma.jdbc.entity.EntityType

@Declaration
@Suppress("FunctionName")
class WhereDeclaration(private val config: Config, private val add: (Criterion) -> Unit) {

    fun <CONTAINER> eq(left: DefaultPropertyType<*, *, CONTAINER>, right: CONTAINER?) {
        add(Criterion.Eq(toProp(left), toParam(left, right)))
    }

    fun <CONTAINER> eq(left: DefaultPropertyType<*, *, CONTAINER>, right: DefaultPropertyType<*, *, CONTAINER>) {
        add(Criterion.Eq(toProp(left), toProp(right)))
    }

    fun <CONTAINER> ne(left: DefaultPropertyType<*, *, CONTAINER>, right: CONTAINER?) {
        add(Criterion.Ne(toProp(left), toParam(left, right)))
    }

    fun <CONTAINER> ne(left: DefaultPropertyType<*, *, CONTAINER>, right: DefaultPropertyType<*, *, CONTAINER>) {
        add(Criterion.Ne(toProp(left), toProp(right)))
    }

    fun <CONTAINER> between(propType: DefaultPropertyType<*, *, CONTAINER>, begin: CONTAINER?, end: CONTAINER?) {
        add(Criterion.Between(toProp(propType), toParam(propType, begin), toParam(propType, end)))
    }

    fun <ENTITY, ENTITY_TYPE : EntityType<ENTITY>> exists(
        from: () -> ENTITY_TYPE,
        block: SelectDeclaration.(ENTITY_TYPE) -> Unit
    ) {
        val context = createSubContext(from(), block, Projection.Asterisk)
        add(Criterion.Exists(context))
    }

    fun <ENTITY, ENTITY_TYPE : EntityType<ENTITY>> notExists(
        from: () -> ENTITY_TYPE,
        block: SelectDeclaration.(ENTITY_TYPE) -> Unit
    ) {
        val context = createSubContext(from(), block, Projection.Asterisk)
        add(Criterion.NotExists(context))
    }

    fun <CONTAINER> `in`(left: DefaultPropertyType<*, *, CONTAINER>, right: List<CONTAINER?>) {
        add(Criterion.InSingle(toProp(left), right.map { toParam(left, it) }))
    }

    fun <CONTAINER1, CONTAINER2> `in`(
        left: Pair<DefaultPropertyType<*, *, CONTAINER1>, DefaultPropertyType<*, *, CONTAINER2>>,
        right: List<Pair<CONTAINER1, CONTAINER2>>
    ) {
        val prop1 = toProp(left.first)
        val prop2 = toProp(left.second)
        val params = right.map { (first, second) ->
            val param1 = toParam(left.first, first)
            val param2 = toParam(left.second, second)
            param1 to param2
        }
        add(Criterion.InPair(prop1 to prop2, params))
    }

    fun <CONTAINER> `in`(left: DefaultPropertyType<*, *, CONTAINER>, right: SelectSingle<CONTAINER>) {
        add(Criterion.InSelectSingle(toProp(left), right.context))
    }

    fun <CONTAINER1, CONTAINER2> `in`(
        left: Pair<DefaultPropertyType<*, *, CONTAINER1>, DefaultPropertyType<*, *, CONTAINER2>>,
        right: SelectPair<CONTAINER1, CONTAINER2>
    ) {
        val (first, second) = left
        val prop1 = Operand.Prop(first)
        val prop2 = Operand.Prop(second)
        add(Criterion.InSelectPair(prop1 to prop2, right.context))
    }

    fun <CONTAINER> notIn(left: DefaultPropertyType<*, *, CONTAINER>, right: List<CONTAINER>) {
        add(Criterion.NotInSingle(toProp(left), right.map { toParam(left, it) }))
    }

    fun <CONTAINER1, CONTAINER2> notIn(
        left: Pair<DefaultPropertyType<*, *, CONTAINER1>, DefaultPropertyType<*, *, CONTAINER2>>,
        values: List<Pair<CONTAINER1, CONTAINER2>>
    ) {
        val (first, second) = left
        val prop1 = Operand.Prop(first)
        val prop2 = Operand.Prop(second)
        val params = values.map { (first, second) ->
            val param1 = toParam(left.first, first)
            val param2 = toParam(left.second, second)
            param1 to param2
        }
        add(Criterion.NotInPair(prop1 to prop2, params))
    }

    fun <CONTAINER> notIn(left: DefaultPropertyType<*, *, CONTAINER>, right: SelectSingle<CONTAINER>) {
        add(Criterion.NotInSelectSingle(Operand.Prop(left), right.context))
    }

    fun <CONTAINER1, CONTAINER2> notIn(
        left: Pair<DefaultPropertyType<*, *, CONTAINER1>, DefaultPropertyType<*, *, CONTAINER2>>,
        right: SelectPair<CONTAINER1, CONTAINER2>
    ) {
        val (first, second) = left
        val prop1 = Operand.Prop(first)
        val prop2 = Operand.Prop(second)
        add(Criterion.NotInSelectPair(prop1 to prop2, right.context))
    }

    fun <ENTITY, ENTITY_TYPE : EntityType<ENTITY>,
            CONTAINER, PROPERTY_TYPE : DefaultPropertyType<ENTITY, *, CONTAINER>> selectSingle(
                list: (ENTITY_TYPE) -> PROPERTY_TYPE,
                from: () -> ENTITY_TYPE,
                block: SelectDeclaration.(ENTITY_TYPE) -> Unit
            ): SelectSingle<CONTAINER> {
        val entityType = from()
        val propType = list(entityType)
        val projection = Projection.Single(propType)
        val context = createSubContext(entityType, block, projection)
        return SelectSingle(context)
    }

    fun <ENTITY, ENTITY_TYPE : EntityType<ENTITY>,
            CONTAINER1, PROPERTY_TYPE1 : DefaultPropertyType<ENTITY, *, CONTAINER1>,
            CONTAINER2, PROPERTY_TYPE2 : DefaultPropertyType<ENTITY, *, CONTAINER2>
            > selectPair(
                list: (ENTITY_TYPE) -> Pair<PROPERTY_TYPE1, PROPERTY_TYPE2>,
                from: () -> ENTITY_TYPE,
                block: SelectDeclaration.(ENTITY_TYPE) -> Unit
            ): SelectPair<CONTAINER1, CONTAINER2> {
        val entityType = from()
        val (first, second) = list(entityType)
        val projection = Projection.Pair(first, second)
        val context = createSubContext(entityType, block, projection)
        return SelectPair(context)
    }

    fun not(block: WhereDeclaration.() -> Unit) = runBlock(block, Criterion::Not)

    fun and(block: WhereDeclaration.() -> Unit) = runBlock(block, Criterion::And)

    fun or(block: WhereDeclaration.() -> Unit) = runBlock(block, Criterion::Or)

    private fun runBlock(block: WhereDeclaration.() -> Unit, context: (List<Criterion>) -> Criterion) {
        val criteriaList = mutableListOf<Criterion>()
        val declaration = WhereDeclaration(config) { criteriaList.add(it) }
        declaration.block()
        if (criteriaList.isNotEmpty()) {
            add(context(criteriaList))
        }
    }

    private fun <CONTAINER> toProp(propType: DefaultPropertyType<*, *, CONTAINER>): Operand.Prop {
        return Operand.Prop(propType)
    }

    // TODO simplify
    private fun <CONTAINER> toParam(propType: DefaultPropertyType<*, *, CONTAINER>, value: CONTAINER?): Operand.Param {
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

    private fun <ENTITY, ENTITY_TYPE : EntityType<ENTITY>> createSubContext(
        entityType: ENTITY_TYPE,
        block: SelectDeclaration.(ENTITY_TYPE) -> Unit,
        projection: Projection
    ): SelectContext {
        val context = SelectContext(config, entityType, projection = projection)
        val declaration = SelectDeclaration(context)
        declaration.block(entityType)
        if (context.associations.isNotEmpty()) {
            TODO()
        }
        return context
    }
}
