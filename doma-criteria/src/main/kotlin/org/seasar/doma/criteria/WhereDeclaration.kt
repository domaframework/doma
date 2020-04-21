package org.seasar.doma.criteria

import java.util.function.Supplier
import org.seasar.doma.internal.jdbc.scalar.Scalars
import org.seasar.doma.internal.jdbc.sql.BasicInParameter
import org.seasar.doma.internal.jdbc.sql.ScalarInParameter
import org.seasar.doma.jdbc.ConfigSupport
import org.seasar.doma.jdbc.entity.EntityPropertyType
import org.seasar.doma.jdbc.entity.EntityType
import org.seasar.doma.wrapper.IntegerWrapper
import org.seasar.doma.wrapper.StringWrapper

class SelectSingle<BASIC>(val context: SelectContext)
class SelectPair<BASIC1, BASIC2>(val context: SelectContext)

@Declaration
@Suppress("FunctionName")
class WhereDeclaration(private val add: (Criterion) -> Unit) {

    fun eq(prop: EntityPropertyType<*, Int>, value: Int?) {
        _eq(prop, Operand.Param(BasicInParameter<Int?>(Supplier { IntegerWrapper(value) })))
    }

    fun eq(prop: EntityPropertyType<*, String>, value: String?) {
        _eq(prop, Operand.Param(BasicInParameter<String?>(Supplier { StringWrapper(value) })))
    }

    fun <BASIC> eq(left: EntityPropertyType<*, BASIC>, right: EntityPropertyType<*, BASIC>) {
        _eq(left, Operand.Prop(right))
    }

    private fun _eq(left: EntityPropertyType<*, *>, right: Operand) {
        add(Criterion.Eq(Operand.Prop(left), right))
    }

    fun ne(prop: EntityPropertyType<*, Int>, value: Int?) {
        _ne(prop, Operand.Param(BasicInParameter<Int?>(Supplier { IntegerWrapper(value) })))
    }

    fun ne(prop: EntityPropertyType<*, String>, value: String?) {
        _ne(prop, Operand.Param(BasicInParameter<String?>(Supplier { StringWrapper(value) })))
    }

    private fun _ne(prop: EntityPropertyType<*, *>, param: Operand.Param) {
        add(Criterion.Ne(Operand.Prop(prop), param))
    }

    //    fun KProperty1<*, String?>.like(value: String?) {
//        add(Criterion.Like(this, value))
//    }
//
//    fun KProperty1<*, String?>.notLike(value: String?) {
//        add(Criterion.NotLike(this, value))
//    }
//
    fun `in`(prop: EntityPropertyType<*, Int>, values: List<Int?>, hint: Int? = null) {
        _inSingle(prop, values.map { Operand.Param(BasicInParameter<Int?>(Supplier { IntegerWrapper(it) })) })
    }

    fun `in`(prop: EntityPropertyType<*, String>, values: List<String?>, hint: String? = null) {
        _inSingle(prop, values.map { Operand.Param(BasicInParameter<String?>(Supplier { StringWrapper(it) })) })
    }

    private fun _inSingle(prop: EntityPropertyType<*, *>, params: List<Operand.Param>) {
        add(Criterion.In(Operand.Prop(prop), params))
    }

    fun <BASIC1, BASIC2> `in`(left: Pair<EntityPropertyType<*, BASIC1>, EntityPropertyType<*, BASIC2>>, values: List<Pair<BASIC1, BASIC2>>) {
        val (first, second) = left
        val prop1 = Operand.Prop(first)
        val prop2 = Operand.Prop(second)
        // TODO
        val params = values.map {
            val (value1, value2) = it
            val scalarSupplier1 = Scalars.wrap(value1, first.createProperty().wrapper.basicClass, false, ConfigSupport.defaultClassHelper)
            val scalarSupplier2 = Scalars.wrap(value2, second.createProperty().wrapper.basicClass, false, ConfigSupport.defaultClassHelper)
            val param1 = Operand.Param(ScalarInParameter(scalarSupplier1.get()))
            val param2 = Operand.Param(ScalarInParameter(scalarSupplier2.get()))
            param1 to param2
        }
        add(Criterion.InPair(prop1 to prop2, params))
    }

    fun <BASIC> `in`(left: EntityPropertyType<*, BASIC>, right: SelectSingle<BASIC>) {
        add(Criterion.InSelect(Operand.Prop(left), right.context))
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

    fun notIn(prop: EntityPropertyType<*, Int>, values: List<Int?>, hint: Int? = null) {
        _notInSingle(prop, values.map { Operand.Param(BasicInParameter<Int?>(Supplier { IntegerWrapper(it) })) })
    }

    fun notIn(prop: EntityPropertyType<*, String>, values: List<String?>, hint: String? = null) {
        _notInSingle(prop, values.map { Operand.Param(BasicInParameter<String?>(Supplier { StringWrapper(it) })) })
    }

    private fun _notInSingle(prop: EntityPropertyType<*, *>, params: List<Operand.Param>) {
        add(Criterion.NotIn(Operand.Prop(prop), params))
    }

    fun <BASIC1, BASIC2> notIn(left: Pair<EntityPropertyType<*, BASIC1>, EntityPropertyType<*, BASIC2>>, values: List<Pair<BASIC1, BASIC2>>) {
        val (first, second) = left
        val prop1 = Operand.Prop(first)
        val prop2 = Operand.Prop(second)
        // TODO
        val params = values.map {
            val (value1, value2) = it
            val scalarSupplier1 = Scalars.wrap(value1, first.createProperty().wrapper.basicClass, false, ConfigSupport.defaultClassHelper)
            val scalarSupplier2 = Scalars.wrap(value2, second.createProperty().wrapper.basicClass, false, ConfigSupport.defaultClassHelper)
            val param1 = Operand.Param(ScalarInParameter(scalarSupplier1.get()))
            val param2 = Operand.Param(ScalarInParameter(scalarSupplier2.get()))
            param1 to param2
        }
        add(Criterion.NotInPair(prop1 to prop2, params))
    }

    fun <BASIC> notIn(left: EntityPropertyType<*, BASIC>, right: SelectSingle<BASIC>) {
        add(Criterion.NotInSelect(Operand.Prop(left), right.context))
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

    //    fun KProperty1<*, java.sql.Array?>.`in`(value: List<java.sql.Array?>, @Suppress("UNUSED_PARAMETER") `_`: java.sql.Array? = null) =
//        _in(this, value)
//
//    fun KProperty1<*, BigDecimal?>.`in`(value: List<BigDecimal?>, @Suppress("UNUSED_PARAMETER") `_`: BigDecimal? = null) =
//        _in(this, value)
//
//    fun KProperty1<*, BigInteger?>.`in`(value: List<BigInteger?>, @Suppress("UNUSED_PARAMETER") `_`: BigInteger? = null) =
//        _in(this, value)
//
//    fun KProperty1<*, Blob?>.`in`(value: List<Blob?>, @Suppress("UNUSED_PARAMETER") `_`: Blob? = null) =
//        _in(this, value)
//
//    fun KProperty1<*, Byte?>.`in`(value: List<Byte?>, @Suppress("UNUSED_PARAMETER") `_`: Byte? = null) =
//        _in(this, value)
//
//    fun KProperty1<*, ByteArray?>.`in`(value: List<ByteArray?>, @Suppress("UNUSED_PARAMETER") `_`: ByteArray? = null) =
//        _in(this, value)
//
//    fun KProperty1<*, Clob?>.`in`(value: List<Clob?>, @Suppress("UNUSED_PARAMETER") `_`: Clob? = null) =
//        _in(this, value)
//
//    fun KProperty1<*, Double?>.`in`(value: List<Double?>, @Suppress("UNUSED_PARAMETER") `_`: Double? = null) =
//        _in(this, value)
//
//    fun <V : Enum<V>> KProperty1<*, V?>.`in`(value: List<V?>, @Suppress("UNUSED_PARAMETER") `_`: V? = null) =
//        _in(this, value)
//
//    fun KProperty1<*, Float?>.`in`(value: List<Float?>, @Suppress("UNUSED_PARAMETER") `_`: Float? = null) =
//        _in(this, value)
//
//    fun KProperty1<*, Int?>.`in`(value: List<Int?>, @Suppress("UNUSED_PARAMETER") `_`: Int? = null) =
//        _in(this, value)
//
//    fun KProperty1<*, LocalDateTime?>.`in`(value: List<LocalDateTime?>, @Suppress("UNUSED_PARAMETER") `_`: LocalDateTime? = null) =
//        _in(this, value)
//
//    fun KProperty1<*, LocalDate?>.`in`(value: List<LocalDate?>, @Suppress("UNUSED_PARAMETER") `_`: LocalDate? = null) =
//        _in(this, value)
//
//    fun KProperty1<*, LocalTime?>.`in`(value: List<LocalTime?>, @Suppress("UNUSED_PARAMETER") `_`: LocalTime? = null) =
//        _in(this, value)
//
//    fun KProperty1<*, Long?>.`in`(value: List<Long?>, @Suppress("UNUSED_PARAMETER") `_`: Long? = null) =
//        _in(this, value)
//
//    fun KProperty1<*, NClob?>.`in`(value: List<NClob?>, @Suppress("UNUSED_PARAMETER") `_`: NClob? = null) =
//        _in(this, value)
//
//    fun KProperty1<*, Short?>.`in`(value: List<Short?>, @Suppress("UNUSED_PARAMETER") `_`: Short? = null) =
//        _in(this, value)
//
//    fun KProperty1<*, String?>.`in`(value: List<String?>, @Suppress("UNUSED_PARAMETER") `_`: String? = null) =
//        _in(this, value)
//
//    fun KProperty1<*, SQLXML?>.`in`(value: List<SQLXML?>, @Suppress("UNUSED_PARAMETER") `_`: SQLXML? = null) =
//        _in(this, value)
//

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

    //
//    fun <V : Any> KProperty1<*, V>.notIn(value: List<V>, @Suppress("UNUSED_PARAMETER") kClass: KClass<V>) =
//        _notIn(this, value)
//
//    fun KProperty1<*, java.sql.Array?>.notIn(value: List<java.sql.Array?>, @Suppress("UNUSED_PARAMETER") `_`: java.sql.Array? = null) =
//        _notIn(this, value)
//
//    fun KProperty1<*, BigDecimal?>.notIn(value: List<BigDecimal?>, @Suppress("UNUSED_PARAMETER") `_`: BigDecimal? = null) =
//        _notIn(this, value)
//
//    fun KProperty1<*, BigInteger?>.notIn(value: List<BigInteger?>, @Suppress("UNUSED_PARAMETER") `_`: BigInteger? = null) =
//        _notIn(this, value)
//
//    fun KProperty1<*, Blob?>.notIn(value: List<Blob?>, @Suppress("UNUSED_PARAMETER") `_`: Blob? = null) =
//        _notIn(this, value)
//
//    fun KProperty1<*, Byte?>.notIn(value: List<Byte?>, @Suppress("UNUSED_PARAMETER") `_`: Byte? = null) =
//        _notIn(this, value)
//
//    fun KProperty1<*, ByteArray?>.notIn(value: List<ByteArray?>, @Suppress("UNUSED_PARAMETER") `_`: ByteArray? = null) =
//        _notIn(this, value)
//
//    fun KProperty1<*, Clob?>.notIn(value: List<Clob?>, @Suppress("UNUSED_PARAMETER") `_`: Clob? = null) =
//        _notIn(this, value)
//
//    fun KProperty1<*, Double?>.notIn(value: List<Double?>, @Suppress("UNUSED_PARAMETER") `_`: Double? = null) =
//        _notIn(this, value)
//
//    fun <V : Enum<V>> KProperty1<*, V?>.notIn(value: List<V?>, @Suppress("UNUSED_PARAMETER") `_`: V? = null) =
//        _notIn(this, value)
//
//    fun KProperty1<*, Float?>.notIn(value: List<Float?>, @Suppress("UNUSED_PARAMETER") `_`: Float? = null) =
//        _notIn(this, value)
//
//    fun KProperty1<*, Int?>.notIn(value: List<Int?>, @Suppress("UNUSED_PARAMETER") `_`: Int? = null) =
//        _notIn(this, value)
//
//    fun KProperty1<*, LocalDateTime?>.notIn(value: List<LocalDateTime?>, @Suppress("UNUSED_PARAMETER") `_`: LocalDateTime? = null) =
//        _notIn(this, value)
//
//    fun KProperty1<*, LocalDate?>.notIn(value: List<LocalDate?>, @Suppress("UNUSED_PARAMETER") `_`: LocalDate? = null) =
//        _notIn(this, value)
//
//    fun KProperty1<*, LocalTime?>.notIn(value: List<LocalTime?>, @Suppress("UNUSED_PARAMETER") `_`: LocalTime? = null) =
//        _notIn(this, value)
//
//    fun KProperty1<*, Long?>.notIn(value: List<Long?>, @Suppress("UNUSED_PARAMETER") `_`: Long? = null) =
//        _notIn(this, value)
//
//    fun KProperty1<*, NClob?>.notIn(value: List<NClob?>, @Suppress("UNUSED_PARAMETER") `_`: NClob? = null) =
//        _notIn(this, value)
//
//    fun KProperty1<*, Short?>.notIn(value: List<Short?>, @Suppress("UNUSED_PARAMETER") `_`: Short? = null) =
//        _notIn(this, value)
//
//    fun KProperty1<*, String?>.notIn(value: List<String?>, @Suppress("UNUSED_PARAMETER") `_`: String? = null) =
//        _notIn(this, value)
//
//    fun KProperty1<*, SQLXML?>.notIn(value: List<SQLXML?>, @Suppress("UNUSED_PARAMETER") `_`: SQLXML? = null) =
//        _notIn(this, value)
//
//    private fun _notIn(prop: KProperty1<*, *>, value: List<Any?>) {
//        add(Criterion.NotIn(prop, value))
//    }
//
//    fun <A, B> Pair<KProperty1<*, A>, KProperty1<*, B>>.`in`(value: List<Pair<A, B>>) {
//        add(Criterion.In2(this, value))
//    }
//
//    fun <A, B> Pair<KProperty1<*, A>, KProperty1<*, B>>.notIn(value: List<Pair<A, B>>) {
//        add(Criterion.NotIn2(this, value))
//    }
//
//    fun <A, B, C> Triple<KProperty1<*, A>, KProperty1<*, B>, KProperty1<*, C>>.`in`(value: List<Triple<A, B, C>>) {
//        add(Criterion.In3(this, value))
//    }
//
//    fun <A, B, C> Triple<KProperty1<*, A>, KProperty1<*, B>, KProperty1<*, C>>.notIn(value: List<Triple<A, B, C>>) {
//        add(Criterion.NotIn3(this, value))
//    }
//
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
}
