package org.seasar.doma.criteria

import java.util.function.Supplier
import org.seasar.doma.internal.jdbc.scalar.Scalars
import org.seasar.doma.internal.jdbc.sql.BasicInParameter
import org.seasar.doma.internal.jdbc.sql.ScalarInParameter
import org.seasar.doma.jdbc.ConfigSupport
import org.seasar.doma.jdbc.entity.EntityPropertyType
import org.seasar.doma.jdbc.entity.EntityType
import org.seasar.doma.wrapper.IntegerWrapper

@Declaration
@Suppress("FunctionName")
class WhereDeclaration(private val add: (Criterion) -> Unit) {

    fun <BASIC> eq(left: EntityPropertyType<*, BASIC>, right: BASIC?) {
        add(Criterion.Eq(toProp(left), toParam(left, right)))
    }

    fun <BASIC> eq(left: EntityPropertyType<*, BASIC>, right: EntityPropertyType<*, BASIC>) {
        add(Criterion.Eq(toProp(left), toProp(right)))
    }

    fun <BASIC> ne(left: EntityPropertyType<*, BASIC>, right: BASIC?) {
        add(Criterion.Ne(toProp(left), toParam(left, right)))
    }

    fun <BASIC> ne(left: EntityPropertyType<*, BASIC>, right: EntityPropertyType<*, BASIC>) {
        add(Criterion.Ne(toProp(left), toProp(right)))
    }

    fun between(prop: EntityPropertyType<*, Int>, begin: Int?, end: Int?) {
        add(Criterion.Between(Operand.Prop(prop),
                Operand.Param(BasicInParameter<Int?>(Supplier { IntegerWrapper(begin) })),
                Operand.Param(BasicInParameter<Int?>(Supplier { IntegerWrapper(end) }))))
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

    fun <BASIC> `in`(left: EntityPropertyType<*, BASIC>, right: List<BASIC?>) {
        add(Criterion.InSingle(toProp(left), right.map { toParam(left, it) }))
    }

    fun <BASIC1, BASIC2> `in`(
        left: Pair<EntityPropertyType<*, BASIC1>, EntityPropertyType<*, BASIC2>>,
        right: List<Pair<BASIC1, BASIC2>>
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

    fun <BASIC> `in`(left: EntityPropertyType<*, BASIC>, right: SelectSingle<BASIC>) {
        add(Criterion.InSelectSingle(toProp(left), right.context))
    }

    fun <BASIC1, BASIC2> `in`(
        left: Pair<EntityPropertyType<*, BASIC1>, EntityPropertyType<*, BASIC2>>,
        right: SelectPair<BASIC1, BASIC2>
    ) {
        val (first, second) = left
        val prop1 = Operand.Prop(first)
        val prop2 = Operand.Prop(second)
        add(Criterion.InSelectPair(prop1 to prop2, right.context))
    }

    fun <BASIC> notIn(left: EntityPropertyType<*, BASIC>, right: List<BASIC>) {
        add(Criterion.NotInSingle(toProp(left), right.map { toParam(left, it) }))
    }

    fun <BASIC1, BASIC2> notIn(
        left: Pair<EntityPropertyType<*, BASIC1>, EntityPropertyType<*, BASIC2>>,
        values: List<Pair<BASIC1, BASIC2>>
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

    fun <BASIC> notIn(left: EntityPropertyType<*, BASIC>, right: SelectSingle<BASIC>) {
        add(Criterion.NotInSelectSingle(Operand.Prop(left), right.context))
    }

    fun <BASIC1, BASIC2> notIn(
        left: Pair<EntityPropertyType<*, BASIC1>, EntityPropertyType<*, BASIC2>>,
        right: SelectPair<BASIC1, BASIC2>
    ) {
        val (first, second) = left
        val prop1 = Operand.Prop(first)
        val prop2 = Operand.Prop(second)
        add(Criterion.NotInSelectPair(prop1 to prop2, right.context))
    }

    fun <ENTITY, ENTITY_TYPE : EntityType<ENTITY>,
            BASIC, PROPERTY_TYPE : EntityPropertyType<ENTITY, BASIC>> selectSingle(
                list: (ENTITY_TYPE) -> PROPERTY_TYPE,
                from: () -> ENTITY_TYPE,
                block: SelectDeclaration.(ENTITY_TYPE) -> Unit
            ): SelectSingle<BASIC> {
        val entityType = from()
        val propType = list(entityType)
        val projection = Projection.Single(propType)
        val context = createSubContext(entityType, block, projection)
        return SelectSingle(context)
    }

    fun <ENTITY, ENTITY_TYPE : EntityType<ENTITY>,
            BASIC1, PROPERTY_TYPE1 : EntityPropertyType<ENTITY, BASIC1>,
            BASIC2, PROPERTY_TYPE2 : EntityPropertyType<ENTITY, BASIC2>
            > selectPair(
                list: (ENTITY_TYPE) -> Pair<PROPERTY_TYPE1, PROPERTY_TYPE2>,
                from: () -> ENTITY_TYPE,
                block: SelectDeclaration.(ENTITY_TYPE) -> Unit
            ): SelectPair<BASIC1, BASIC2> {
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
        val declaration = WhereDeclaration { criteriaList.add(it) }
        declaration.block()
        if (criteriaList.isNotEmpty()) {
            add(context(criteriaList))
        }
    }

    private fun <BASIC> toProp(propType: EntityPropertyType<*, BASIC>): Operand.Prop {
        return Operand.Prop(propType)
    }

    private fun <BASIC> toParam(propType: EntityPropertyType<*, BASIC>, value: BASIC?): Operand.Param {
        // TODO
        val supplier = Scalars.wrap(value, propType.createProperty().wrapper.basicClass, false, ConfigSupport.defaultClassHelper)
        return Operand.Param(ScalarInParameter(supplier.get()))
    }

    private fun <ENTITY, ENTITY_TYPE : EntityType<ENTITY>> createSubContext(
        entityType: ENTITY_TYPE,
        block: SelectDeclaration.(ENTITY_TYPE) -> Unit,
        projection: Projection
    ): SelectContext {
        val context = SelectContext(entityType, projection = projection)
        val declaration = SelectDeclaration(context)
        declaration.block(entityType)
        if (context.associations.isNotEmpty()) {
            TODO()
        }
        return context
    }
}
