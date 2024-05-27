package org.seasar.doma.kotlin.jdbc.criteria.expression

import org.seasar.doma.jdbc.criteria.context.SubSelectContext
import org.seasar.doma.jdbc.criteria.expression.AggregateFunction
import org.seasar.doma.jdbc.criteria.expression.AggregateFunction.Avg
import org.seasar.doma.jdbc.criteria.expression.AggregateFunction.AvgAsDouble
import org.seasar.doma.jdbc.criteria.expression.AggregateFunction.Max
import org.seasar.doma.jdbc.criteria.expression.AggregateFunction.Min
import org.seasar.doma.jdbc.criteria.expression.AggregateFunction.Sum
import org.seasar.doma.jdbc.criteria.expression.ArithmeticExpression
import org.seasar.doma.jdbc.criteria.expression.ArithmeticExpression.Add
import org.seasar.doma.jdbc.criteria.expression.ArithmeticExpression.Mul
import org.seasar.doma.jdbc.criteria.expression.ArithmeticExpression.Sub
import org.seasar.doma.jdbc.criteria.expression.CaseExpression
import org.seasar.doma.jdbc.criteria.expression.Expressions
import org.seasar.doma.jdbc.criteria.expression.SelectExpression
import org.seasar.doma.jdbc.criteria.expression.StringExpression
import org.seasar.doma.jdbc.criteria.expression.StringExpression.Concat
import org.seasar.doma.jdbc.criteria.expression.StringExpression.Ltrim
import org.seasar.doma.jdbc.criteria.expression.StringExpression.Rtrim
import org.seasar.doma.jdbc.criteria.expression.StringExpression.Trim
import org.seasar.doma.jdbc.criteria.expression.StringExpression.Upper
import org.seasar.doma.jdbc.criteria.expression.UserDefinedExpression
import org.seasar.doma.jdbc.criteria.metamodel.PropertyMetamodel
import java.math.BigDecimal
import java.math.BigInteger
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

object KExpressions {
    fun literal(value: BigDecimal): PropertyMetamodel<BigDecimal> {
        return Expressions.literal(value)
    }

    fun literal(value: BigInteger): PropertyMetamodel<BigInteger> {
        return Expressions.literal(value)
    }

    fun literal(value: Boolean): PropertyMetamodel<Boolean> {
        return Expressions.literal(value)
    }

    fun literal(value: Byte): PropertyMetamodel<Byte> {
        return Expressions.literal(value)
    }

    fun literal(value: Double): PropertyMetamodel<Double> {
        return Expressions.literal(value)
    }

    fun literal(value: Float): PropertyMetamodel<Float> {
        return Expressions.literal(value)
    }

    fun literal(value: Int): PropertyMetamodel<Int> {
        return Expressions.literal(value)
    }

    fun literal(value: LocalDate): PropertyMetamodel<LocalDate> {
        return Expressions.literal(value)
    }

    fun literal(value: LocalDateTime): PropertyMetamodel<LocalDateTime> {
        return Expressions.literal(value)
    }

    fun literal(value: LocalTime): PropertyMetamodel<LocalTime> {
        return Expressions.literal(value)
    }

    fun literal(value: Long): PropertyMetamodel<Long> {
        return Expressions.literal(value)
    }

    fun literal(value: Short): PropertyMetamodel<Short> {
        return Expressions.literal(value)
    }

    fun literal(value: String): PropertyMetamodel<String> {
        return Expressions.literal(value)
    }

    fun <PROPERTY> add(
        left: PropertyMetamodel<PROPERTY>,
        right: PROPERTY,
    ): Add<PROPERTY> {
        return Expressions.add(left, right)
    }

    fun <PROPERTY> add(
        left: PROPERTY,
        right: PropertyMetamodel<PROPERTY>,
    ): Add<PROPERTY> {
        return Expressions.add(left, right)
    }

    fun <PROPERTY> add(
        left: PropertyMetamodel<PROPERTY>,
        right: PropertyMetamodel<PROPERTY>,
    ): Add<PROPERTY> {
        return Expressions.add(left, right)
    }

    fun <PROPERTY> sub(
        left: PropertyMetamodel<PROPERTY>,
        right: PROPERTY,
    ): Sub<PROPERTY> {
        return Expressions.sub(left, right)
    }

    fun <PROPERTY> sub(
        left: PROPERTY,
        right: PropertyMetamodel<PROPERTY>,
    ): Sub<PROPERTY> {
        return Expressions.sub(left, right)
    }

    fun <PROPERTY> sub(
        left: PropertyMetamodel<PROPERTY>,
        right: PropertyMetamodel<PROPERTY>,
    ): Sub<PROPERTY> {
        return Expressions.sub(left, right)
    }

    fun <PROPERTY> mul(
        left: PropertyMetamodel<PROPERTY>,
        right: PROPERTY,
    ): Mul<PROPERTY> {
        return Expressions.mul(left, right)
    }

    fun <PROPERTY> mul(
        left: PROPERTY,
        right: PropertyMetamodel<PROPERTY>,
    ): Mul<PROPERTY> {
        return Expressions.mul(left, right)
    }

    fun <PROPERTY> mul(
        left: PropertyMetamodel<PROPERTY>,
        right: PropertyMetamodel<PROPERTY>,
    ): Mul<PROPERTY> {
        return Expressions.mul(left, right)
    }

    fun <PROPERTY> div(
        left: PropertyMetamodel<PROPERTY>,
        right: PROPERTY,
    ): ArithmeticExpression.Div<PROPERTY> {
        return Expressions.div(left, right)
    }

    fun <PROPERTY> div(
        left: PROPERTY,
        right: PropertyMetamodel<PROPERTY>,
    ): ArithmeticExpression.Div<PROPERTY> {
        return Expressions.div(left, right)
    }

    fun <PROPERTY> div(
        left: PropertyMetamodel<PROPERTY>,
        right: PropertyMetamodel<PROPERTY>,
    ): ArithmeticExpression.Div<PROPERTY> {
        return Expressions.div(left, right)
    }

    fun <PROPERTY> mod(
        left: PropertyMetamodel<PROPERTY>,
        right: PROPERTY,
    ): ArithmeticExpression.Mod<PROPERTY> {
        return Expressions.mod(left, right)
    }

    fun <PROPERTY> mod(
        left: PROPERTY,
        right: PropertyMetamodel<PROPERTY>,
    ): ArithmeticExpression.Mod<PROPERTY> {
        return Expressions.mod(left, right)
    }

    fun <PROPERTY> mod(
        left: PropertyMetamodel<PROPERTY>,
        right: PropertyMetamodel<PROPERTY>,
    ): ArithmeticExpression.Mod<PROPERTY> {
        return Expressions.mod(left, right)
    }

    fun <PROPERTY> concat(
        left: PropertyMetamodel<PROPERTY>,
        right: PROPERTY,
    ): Concat<PROPERTY> {
        return Expressions.concat(left, right)
    }

    fun <PROPERTY> concat(
        left: PROPERTY,
        right: PropertyMetamodel<PROPERTY>,
    ): Concat<PROPERTY> {
        return Expressions.concat(left, right)
    }

    fun <PROPERTY> concat(
        left: PropertyMetamodel<PROPERTY>,
        right: PropertyMetamodel<PROPERTY>?,
    ): Concat<PROPERTY> {
        return Expressions.concat(left, right)
    }

    fun <PROPERTY> lower(
        propertyMetamodel: PropertyMetamodel<PROPERTY>,
    ): StringExpression.Lower<PROPERTY> {
        return Expressions.lower(propertyMetamodel)
    }

    fun <PROPERTY> ltrim(
        propertyMetamodel: PropertyMetamodel<PROPERTY>,
    ): Ltrim<PROPERTY> {
        return Expressions.ltrim(propertyMetamodel)
    }

    fun <PROPERTY> rtrim(
        propertyMetamodel: PropertyMetamodel<PROPERTY>,
    ): Rtrim<PROPERTY> {
        return Expressions.rtrim(propertyMetamodel)
    }

    fun <PROPERTY> trim(
        propertyMetamodel: PropertyMetamodel<PROPERTY>,
    ): Trim<PROPERTY> {
        return Expressions.trim(propertyMetamodel)
    }

    fun <PROPERTY> upper(
        propertyMetamodel: PropertyMetamodel<PROPERTY>,
    ): Upper<PROPERTY> {
        return Expressions.upper(propertyMetamodel)
    }

    fun <PROPERTY> avg(
        propertyMetamodel: PropertyMetamodel<PROPERTY>,
    ): Avg<PROPERTY> {
        return Expressions.avg(propertyMetamodel)
    }

    fun avgAsDouble(
        propertyMetamodel: PropertyMetamodel<*>,
    ): AvgAsDouble {
        return Expressions.avgAsDouble(propertyMetamodel)
    }

    fun count(): AggregateFunction.Count {
        return Expressions.count()
    }

    fun count(propertyMetamodel: PropertyMetamodel<*>): AggregateFunction.Count {
        return Expressions.count(propertyMetamodel)
    }

    fun countDistinct(propertyMetamodel: PropertyMetamodel<*>): AggregateFunction.Count {
        return Expressions.countDistinct(propertyMetamodel)
    }

    fun <PROPERTY> max(
        propertyMetamodel: PropertyMetamodel<PROPERTY>,
    ): Max<PROPERTY> {
        return Expressions.max(propertyMetamodel)
    }

    fun <PROPERTY> min(
        propertyMetamodel: PropertyMetamodel<PROPERTY>?,
    ): Min<PROPERTY> {
        return Expressions.min(propertyMetamodel)
    }

    fun <PROPERTY> sum(
        propertyMetamodel: PropertyMetamodel<PROPERTY>?,
    ): Sum<PROPERTY> {
        return Expressions.sum(propertyMetamodel)
    }

    fun <PROPERTY> case(
        block: CaseExpression<PROPERTY>.Declaration.() -> Unit,
        otherwise: PropertyMetamodel<PROPERTY>,
    ): CaseExpression<PROPERTY> {
        return Expressions.`when`(block, otherwise)
    }

    fun <PROPERTY> select(
        block: KSelectExpression.Declaration.() -> SubSelectContext.Single<PROPERTY>,
    ): SelectExpression<PROPERTY> {
        return Expressions.select { block(KSelectExpression.Declaration()) }
    }

    inline fun <reified PROPERTY> userDefined(
        name: String,
        operands: List<PropertyMetamodel<*>>,
        noinline block: UserDefinedExpression.Declaration.() -> Unit,
    ): UserDefinedExpression<PROPERTY> {
        return Expressions.userDefined(PROPERTY::class.java, name, operands, block)
    }

    inline fun <reified PROPERTY> userDefined(
        name: String,
        vararg operands: PropertyMetamodel<*>,
        noinline block: UserDefinedExpression.Declaration.() -> Unit,
    ): UserDefinedExpression<PROPERTY> {
        return Expressions.userDefined(PROPERTY::class.java, name, operands.toList(), block)
    }

    fun <PROPERTY> userDefined(
        resultPropertyMetamodel: PropertyMetamodel<PROPERTY>,
        name: String,
        operands: List<PropertyMetamodel<*>>,
        block: UserDefinedExpression.Declaration.() -> Unit,
    ): UserDefinedExpression<PROPERTY> {
        return Expressions.userDefined(resultPropertyMetamodel, name, operands, block)
    }

    fun <PROPERTY> userDefined(
        resultPropertyMetamodel: PropertyMetamodel<PROPERTY>,
        name: String,
        vararg operands: PropertyMetamodel<*>,
        block: UserDefinedExpression.Declaration.() -> Unit,
    ): UserDefinedExpression<PROPERTY> {
        return Expressions.userDefined(resultPropertyMetamodel, name, operands.toList(), block)
    }
}
