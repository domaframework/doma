package org.seasar.doma.criteria.declaration

import java.util.Optional
import java.util.function.BiFunction
import java.util.function.Function
import org.seasar.doma.def.PropertyDef
import org.seasar.doma.internal.jdbc.scalar.BasicScalar
import org.seasar.doma.internal.jdbc.sql.ScalarInParameter
import org.seasar.doma.jdbc.InParameter
import org.seasar.doma.jdbc.entity.EntityPropertyType
import org.seasar.doma.jdbc.entity.NamingType
import org.seasar.doma.jdbc.entity.Property
import org.seasar.doma.wrapper.LongWrapper
import org.seasar.doma.wrapper.Wrapper

interface AggregateDeclaration {

    val `*`: Asterisk
        get() = Asterisk

    fun <PROPERTY> avg(propType: PropertyDef<PROPERTY>): Avg<PROPERTY> {
        return Avg(propType)
    }

    fun count(asterisk: Asterisk): Count {
        return Count(asterisk)
    }

    fun count(propType: PropertyDef<*>): Count {
        return Count(propType)
    }

    fun <PROPERTY> max(propType: PropertyDef<PROPERTY>): Max<PROPERTY> {
        return Max(propType)
    }

    fun <PROPERTY> min(propType: PropertyDef<PROPERTY>): Min<PROPERTY> {
        return Min(propType)
    }

    fun <PROPERTY> sum(propType: PropertyDef<PROPERTY>): Sum<PROPERTY> {
        return Sum(propType)
    }
}

interface AggregateFunction<PROPERTY> : PropertyDef<PROPERTY> {
    val functionName: String
    val argument: PropertyDef<*>
}

data class Avg<PROPERTY>(override val argument: PropertyDef<PROPERTY>) : AggregateFunction<PROPERTY> {

    override val functionName: String
        get() = "avg"

    override fun asClass(): Class<PROPERTY> {
        return argument.asClass()
    }

    override fun asType(): EntityPropertyType<*, *> {
        return argument.asType()
    }
}

data class Count(override val argument: PropertyDef<*>) : AggregateFunction<Long> {

    override val functionName: String
        get() = "count"

    override fun asClass(): Class<Long> {
        return Long::class.java
    }

    override fun asType(): EntityPropertyType<*, *> {
        return object : EntityPropertyType<Any, Long> by argument.asType() as EntityPropertyType<Any, Long> {
            override fun createProperty(): Property<Any, Long> {
                return LongProperty()
            }
        }
    }
}

data class Max<PROPERTY>(override val argument: PropertyDef<PROPERTY>) : AggregateFunction<PROPERTY> {

    override val functionName: String
        get() = "max"

    override fun asClass(): Class<PROPERTY> {
        return argument.asClass()
    }

    override fun asType(): EntityPropertyType<*, *> {
        return argument.asType()
    }
}

data class Min<PROPERTY>(override val argument: PropertyDef<PROPERTY>) : AggregateFunction<PROPERTY> {

    override val functionName: String
        get() = "min"

    override fun asClass(): Class<PROPERTY> {
        return argument.asClass()
    }

    override fun asType(): EntityPropertyType<*, *> {
        return argument.asType()
    }
}

data class Sum<PROPERTY>(override val argument: PropertyDef<PROPERTY>) : AggregateFunction<PROPERTY> {

    override val functionName: String
        get() = "sum"

    override fun asClass(): Class<PROPERTY> {
        return argument.asClass()
    }

    override fun asType(): EntityPropertyType<*, *> {
        return argument.asType()
    }
}

object Asterisk : PropertyDef<Long> {
    override fun asClass(): Class<Long> {
        return Long::class.java
    }

    override fun asType(): EntityPropertyType<*, Long> {
        return object : EntityPropertyType<Any, Long> {
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

            override fun createProperty(): Property<Any, Long>? {
                throw UnsupportedOperationException()
            }

            override fun copy(dest: Any?, src: Any?) {
                throw UnsupportedOperationException()
            }
        }
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
