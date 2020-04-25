package org.seasar.doma.criteria.declaration

import java.util.Optional
import java.util.OptionalDouble
import java.util.OptionalInt
import java.util.OptionalLong
import java.util.function.BiFunction
import java.util.function.Function
import org.seasar.doma.criteria.context.Operand
import org.seasar.doma.internal.jdbc.scalar.BasicScalar
import org.seasar.doma.internal.jdbc.scalar.Scalars
import org.seasar.doma.internal.jdbc.sql.ScalarInParameter
import org.seasar.doma.jdbc.Config
import org.seasar.doma.jdbc.InParameter
import org.seasar.doma.jdbc.entity.EntityPropertyDesc
import org.seasar.doma.jdbc.entity.NamingType
import org.seasar.doma.jdbc.entity.Property
import org.seasar.doma.wrapper.LongWrapper
import org.seasar.doma.wrapper.Wrapper

class DeclarationSupport(private val config: Config) {

    fun <ENTITY, BASIC, CONTAINER> avg(propType: EntityPropertyDesc<ENTITY, BASIC, CONTAINER>): Avg<ENTITY, BASIC, CONTAINER> {
        return Avg(propType)
    }

    fun count(propType: EntityPropertyDesc<*, *, *>): Count {
        return Count(propType)
    }

    fun <ENTITY, BASIC, CONTAINER> max(propType: EntityPropertyDesc<ENTITY, BASIC, CONTAINER>): Max<ENTITY, BASIC, CONTAINER> {
        return Max(propType)
    }

    fun <ENTITY, BASIC, CONTAINER> min(propType: EntityPropertyDesc<ENTITY, BASIC, CONTAINER>): Min<ENTITY, BASIC, CONTAINER> {
        return Min(propType)
    }

    fun <ENTITY, BASIC, CONTAINER> sum(propType: EntityPropertyDesc<ENTITY, BASIC, CONTAINER>): Sum<ENTITY, BASIC, CONTAINER> {
        return Sum(propType)
    }

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

interface SqlFunction {
    val functionName: String
    val argument: EntityPropertyDesc<*, *, *>
}

data class Avg<ENTITY, BASIC, CONTAINER>(override val argument: EntityPropertyDesc<ENTITY, BASIC, CONTAINER>) :
        EntityPropertyDesc<ENTITY, BASIC, CONTAINER> by argument, SqlFunction {

    override val functionName: String
        get() = "avg"
}

data class Count(override val argument: EntityPropertyDesc<*, *, *>) : EntityPropertyDesc<Any, Long, Long>, SqlFunction {

    override val functionName: String
        get() = "count"

    override fun getName(): String {
        throw UnsupportedOperationException()
    }

    override fun getColumnName(): String {
        throw UnsupportedOperationException()
    }

    override fun getColumnName(quoteFunction: Function<String, String>?): String {
        throw UnsupportedOperationException()
    }

    override fun getColumnName(namingFunction: BiFunction<NamingType, String, String>?): String {
        throw UnsupportedOperationException()
    }

    override fun getColumnName(namingFunction: BiFunction<NamingType, String, String>?, quoteFunction: Function<String, String>?): String {
        throw UnsupportedOperationException()
    }

    override fun isQuoteRequired(): Boolean {
        throw UnsupportedOperationException()
    }

    override fun isId(): Boolean {
        throw UnsupportedOperationException()
    }

    override fun isVersion(): Boolean {
        throw UnsupportedOperationException()
    }

    override fun isTenantId(): Boolean {
        throw UnsupportedOperationException()
    }

    override fun isInsertable(): Boolean {
        throw UnsupportedOperationException()
    }

    override fun isUpdatable(): Boolean {
        throw UnsupportedOperationException()
    }

    override fun createProperty(): Property<Any, Long> {
        return LongProperty()
    }

    override fun copy(dest: Any, src: Any) {
        throw UnsupportedOperationException()
    }
}

data class Max<ENTITY, BASIC, CONTAINER>(override val argument: EntityPropertyDesc<ENTITY, BASIC, CONTAINER>) :
        EntityPropertyDesc<ENTITY, BASIC, CONTAINER> by argument, SqlFunction {

    override val functionName: String
        get() = "max"
}

data class Min<ENTITY, BASIC, CONTAINER>(override val argument: EntityPropertyDesc<ENTITY, BASIC, CONTAINER>) :
        EntityPropertyDesc<ENTITY, BASIC, CONTAINER> by argument, SqlFunction {

    override val functionName: String
        get() = "min"
}

data class Sum<ENTITY, BASIC, CONTAINER>(override val argument: EntityPropertyDesc<ENTITY, BASIC, CONTAINER>) :
        EntityPropertyDesc<ENTITY, BASIC, CONTAINER> by argument, SqlFunction {

    override val functionName: String
        get() = "sum"
}

object CountAsterisk : EntityPropertyDesc<Any, Any, Any> {

    override fun getName(): String {
        throw UnsupportedOperationException()
    }

    override fun getColumnName(): String {
        throw UnsupportedOperationException()
    }

    override fun getColumnName(quoteFunction: Function<String, String>?): String {
        throw UnsupportedOperationException()
    }

    override fun getColumnName(namingFunction: BiFunction<NamingType, String, String>?): String {
        throw UnsupportedOperationException()
    }

    override fun getColumnName(namingFunction: BiFunction<NamingType, String, String>?, quoteFunction: Function<String, String>?): String {
        throw UnsupportedOperationException()
    }

    override fun isQuoteRequired(): Boolean {
        throw UnsupportedOperationException()
    }

    override fun isId(): Boolean {
        throw UnsupportedOperationException()
    }

    override fun isVersion(): Boolean {
        throw UnsupportedOperationException()
    }

    override fun isTenantId(): Boolean {
        throw UnsupportedOperationException()
    }

    override fun isInsertable(): Boolean {
        throw UnsupportedOperationException()
    }

    override fun isUpdatable(): Boolean {
        throw UnsupportedOperationException()
    }

    override fun createProperty(): Property<Any, Any> {
        throw UnsupportedOperationException()
    }

    override fun copy(dest: Any, src: Any) {
        throw UnsupportedOperationException()
    }
}

class LongProperty : Property<Any, Long> {
    private val scalar = BasicScalar(::LongWrapper)

    override fun getDomainClass(): Optional<Class<*>> {
        return scalar.domainClass
    }

    override fun get(): Any {
        return scalar.get()
    }

    override fun getWrapper(): Wrapper<Long> {
        return scalar.wrapper
    }

    override fun asInParameter(): InParameter<Long> {
        return ScalarInParameter(scalar)
    }

    override fun load(entity: Any?): Property<Any, Long> {
        throw UnsupportedOperationException()
    }

    override fun save(entity: Any?): Property<Any, Long> {
        throw UnsupportedOperationException()
    }
}
