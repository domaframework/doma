package org.seasar.doma.jdbc.criteria.statement

import org.seasar.doma.jdbc.ObjectProvider
import org.seasar.doma.jdbc.Sql
import org.seasar.doma.jdbc.criteria.command.DataRow
import org.seasar.doma.jdbc.criteria.command.MappedResultProvider
import org.seasar.doma.jdbc.criteria.context.SetOperationContext
import org.seasar.doma.jdbc.criteria.declaration.KHavingDeclaration
import org.seasar.doma.jdbc.criteria.declaration.KJoinDeclaration
import org.seasar.doma.jdbc.criteria.declaration.KOrderByNameDeclaration
import org.seasar.doma.jdbc.criteria.declaration.KWhereDeclaration
import org.seasar.doma.jdbc.criteria.metamodel.EntityMetamodel
import org.seasar.doma.jdbc.criteria.metamodel.PropertyMetamodel
import org.seasar.doma.jdbc.criteria.option.DistinctOption
import org.seasar.doma.jdbc.criteria.option.ForUpdateOption
import org.seasar.doma.jdbc.criteria.tuple.Row
import org.seasar.doma.jdbc.criteria.tuple.Tuple2
import org.seasar.doma.jdbc.criteria.tuple.Tuple3
import org.seasar.doma.jdbc.criteria.tuple.Tuple4
import org.seasar.doma.jdbc.criteria.tuple.Tuple5
import org.seasar.doma.jdbc.criteria.tuple.Tuple6
import org.seasar.doma.jdbc.criteria.tuple.Tuple7
import org.seasar.doma.jdbc.criteria.tuple.Tuple8
import org.seasar.doma.jdbc.criteria.tuple.Tuple9
import org.seasar.doma.jdbc.query.SelectQuery
import java.util.function.Function
import kotlin.streams.asSequence

class KNativeSqlSelectStarting<ENTITY>(private val statement: NativeSqlSelectStarting<ENTITY>) : KStatement<List<ENTITY>>, KSetOperand<ENTITY> {

    fun distinct(distinctOption: DistinctOption = DistinctOption.basic()): KNativeSqlSelectStarting<ENTITY> {
        statement.distinct(distinctOption)
        return this
    }

    fun innerJoin(
        entityMetamodel: EntityMetamodel<*>,
        block: KJoinDeclaration.() -> Unit
    ): KNativeSqlSelectStarting<ENTITY> {
        statement.innerJoin(entityMetamodel) { block(KJoinDeclaration(it)) }
        return this
    }

    fun leftJoin(
        entityMetamodel: EntityMetamodel<*>,
        block: KJoinDeclaration.() -> Unit
    ): KNativeSqlSelectStarting<ENTITY> {
        statement.leftJoin(entityMetamodel) { block(KJoinDeclaration(it)) }
        return this
    }

    fun where(block: KWhereDeclaration.() -> Unit): KNativeSqlSelectStarting<ENTITY> {
        statement.where { block(KWhereDeclaration(it)) }
        return this
    }

    fun groupBy(vararg propertyMetamodels: PropertyMetamodel<*>): KNativeSqlSelectStarting<ENTITY> {
        statement.groupBy(*propertyMetamodels)
        return this
    }

    fun having(block: KHavingDeclaration.() -> Unit): KNativeSqlSelectStarting<ENTITY> {
        statement.having { block(KHavingDeclaration(it)) }
        return this
    }

    fun orderBy(block: KOrderByNameDeclaration.() -> Unit): KNativeSqlSelectStarting<ENTITY> {
        statement.orderBy { block(KOrderByNameDeclaration(it)) }
        return this
    }

    fun limit(limit: Int?): KNativeSqlSelectStarting<ENTITY> {
        statement.limit(limit)
        return this
    }

    fun offset(offset: Int?): KNativeSqlSelectStarting<ENTITY> {
        statement.offset(offset)
        return this
    }

    fun forUpdate(option: ForUpdateOption = ForUpdateOption.basic()): KNativeSqlSelectStarting<ENTITY> {
        statement.forUpdate(option)
        return this
    }

    fun select(): KSetOperand<ENTITY> {
        val setOperand = statement.select()
        return KNativeSqlSelectIntermediate(setOperand)
    }

    fun <T> select(entityMetamodel: EntityMetamodel<T>): KSetOperand<T> {
        val setOperand = statement.select(entityMetamodel)
        return KNativeSqlSelectIntermediate(setOperand)
    }

    fun <T1, T2> select(
        entityMetamodel1: EntityMetamodel<T1>,
        entityMetamodel2: EntityMetamodel<T2>
    ): KSetOperand<Tuple2<T1, T2>> {
        val setOperand = statement.select(entityMetamodel1, entityMetamodel2)
        return KNativeSqlSelectIntermediate(setOperand)
    }

    fun <T1, T2, T3> select(
        entityMetamodel1: EntityMetamodel<T1>,
        entityMetamodel2: EntityMetamodel<T2>,
        entityMetamodel3: EntityMetamodel<T3>
    ): KSetOperand<Tuple3<T1, T2, T3>> {
        val setOperand = statement.select(entityMetamodel1, entityMetamodel2, entityMetamodel3)
        return KNativeSqlSelectIntermediate(setOperand)
    }

    fun <T1, T2, T3, T4> select(
        entityMetamodel1: EntityMetamodel<T1>,
        entityMetamodel2: EntityMetamodel<T2>,
        entityMetamodel3: EntityMetamodel<T3>,
        entityMetamodel4: EntityMetamodel<T4>
    ): KSetOperand<Tuple4<T1, T2, T3, T4>> {
        val setOperand = statement.select(entityMetamodel1, entityMetamodel2, entityMetamodel3, entityMetamodel4)
        return KNativeSqlSelectIntermediate(setOperand)
    }

    fun <T1, T2, T3, T4, T5> select(
        entityMetamodel1: EntityMetamodel<T1>,
        entityMetamodel2: EntityMetamodel<T2>,
        entityMetamodel3: EntityMetamodel<T3>,
        entityMetamodel4: EntityMetamodel<T4>,
        entityMetamodel5: EntityMetamodel<T5>
    ): KSetOperand<Tuple5<T1, T2, T3, T4, T5>> {
        val setOperand = statement.select(entityMetamodel1, entityMetamodel2, entityMetamodel3, entityMetamodel4, entityMetamodel5)
        return KNativeSqlSelectIntermediate(setOperand)
    }

    fun <T1, T2, T3, T4, T5, T6> select(
        entityMetamodel1: EntityMetamodel<T1>,
        entityMetamodel2: EntityMetamodel<T2>,
        entityMetamodel3: EntityMetamodel<T3>,
        entityMetamodel4: EntityMetamodel<T4>,
        entityMetamodel5: EntityMetamodel<T5>,
        entityMetamodel6: EntityMetamodel<T6>
    ): KSetOperand<Tuple6<T1, T2, T3, T4, T5, T6>> {
        val setOperand = statement.select(entityMetamodel1, entityMetamodel2, entityMetamodel3, entityMetamodel4, entityMetamodel5, entityMetamodel6)
        return KNativeSqlSelectIntermediate(setOperand)
    }

    fun <T1, T2, T3, T4, T5, T6, T7> select(
        entityMetamodel1: EntityMetamodel<T1>,
        entityMetamodel2: EntityMetamodel<T2>,
        entityMetamodel3: EntityMetamodel<T3>,
        entityMetamodel4: EntityMetamodel<T4>,
        entityMetamodel5: EntityMetamodel<T5>,
        entityMetamodel6: EntityMetamodel<T6>,
        entityMetamodel7: EntityMetamodel<T7>
    ): KSetOperand<Tuple7<T1, T2, T3, T4, T5, T6, T7>> {
        val setOperand = statement.select(entityMetamodel1, entityMetamodel2, entityMetamodel3, entityMetamodel4, entityMetamodel5, entityMetamodel6, entityMetamodel7)
        return KNativeSqlSelectIntermediate(setOperand)
    }

    fun <T1, T2, T3, T4, T5, T6, T7, T8> select(
        entityMetamodel1: EntityMetamodel<T1>,
        entityMetamodel2: EntityMetamodel<T2>,
        entityMetamodel3: EntityMetamodel<T3>,
        entityMetamodel4: EntityMetamodel<T4>,
        entityMetamodel5: EntityMetamodel<T5>,
        entityMetamodel6: EntityMetamodel<T6>,
        entityMetamodel7: EntityMetamodel<T7>,
        entityMetamodel8: EntityMetamodel<T8>
    ): KSetOperand<Tuple8<T1, T2, T3, T4, T5, T6, T7, T8>> {
        val setOperand = statement.select(entityMetamodel1, entityMetamodel2, entityMetamodel3, entityMetamodel4, entityMetamodel5, entityMetamodel6, entityMetamodel7, entityMetamodel8)
        return KNativeSqlSelectIntermediate(setOperand)
    }

    fun <T1, T2, T3, T4, T5, T6, T7, T8, T9> select(
        entityMetamodel1: EntityMetamodel<T1>,
        entityMetamodel2: EntityMetamodel<T2>,
        entityMetamodel3: EntityMetamodel<T3>,
        entityMetamodel4: EntityMetamodel<T4>,
        entityMetamodel5: EntityMetamodel<T5>,
        entityMetamodel6: EntityMetamodel<T6>,
        entityMetamodel7: EntityMetamodel<T7>,
        entityMetamodel8: EntityMetamodel<T8>,
        entityMetamodel9: EntityMetamodel<T9>
    ): KSetOperand<Tuple9<T1, T2, T3, T4, T5, T6, T7, T8, T9>> {
        val setOperand = statement.select(entityMetamodel1, entityMetamodel2, entityMetamodel3, entityMetamodel4, entityMetamodel5, entityMetamodel6, entityMetamodel7, entityMetamodel8, entityMetamodel9)
        return KNativeSqlSelectIntermediate(setOperand)
    }

    fun <T> select(propertyMetamodel: PropertyMetamodel<T>): KSetOperand<T> {
        val setOperand = statement.select(propertyMetamodel)
        return KNativeSqlSelectIntermediate(setOperand)
    }

    fun <T1, T2> select(
        propertyMetamodel1: PropertyMetamodel<T1>,
        propertyMetamodel2: PropertyMetamodel<T2>
    ): KSetOperand<Tuple2<T1, T2>> {
        val setOperand = statement.select(propertyMetamodel1, propertyMetamodel2)
        return KNativeSqlSelectIntermediate(setOperand)
    }

    fun <T1, T2, T3> select(
        propertyMetamodel1: PropertyMetamodel<T1>,
        propertyMetamodel2: PropertyMetamodel<T2>,
        propertyMetamodel3: PropertyMetamodel<T3>
    ): KSetOperand<Tuple3<T1, T2, T3>> {
        val setOperand = statement.select(propertyMetamodel1, propertyMetamodel2, propertyMetamodel3)
        return KNativeSqlSelectIntermediate(setOperand)
    }

    fun <T1, T2, T3, T4> select(
        propertyMetamodel1: PropertyMetamodel<T1>?,
        propertyMetamodel2: PropertyMetamodel<T2>?,
        propertyMetamodel3: PropertyMetamodel<T3>?,
        propertyMetamodel4: PropertyMetamodel<T4>?
    ): KSetOperand<Tuple4<T1, T2, T3, T4>> {
        val setOperand = statement.select(propertyMetamodel1, propertyMetamodel2, propertyMetamodel3, propertyMetamodel4)
        return KNativeSqlSelectIntermediate(setOperand)
    }

    fun <T1, T2, T3, T4, T5> select(
        propertyMetamodel1: PropertyMetamodel<T1>?,
        propertyMetamodel2: PropertyMetamodel<T2>?,
        propertyMetamodel3: PropertyMetamodel<T3>?,
        propertyMetamodel4: PropertyMetamodel<T4>?,
        propertyMetamodel5: PropertyMetamodel<T5>?
    ): KSetOperand<Tuple5<T1, T2, T3, T4, T5>> {
        val setOperand = statement.select(propertyMetamodel1, propertyMetamodel2, propertyMetamodel3, propertyMetamodel4, propertyMetamodel5)
        return KNativeSqlSelectIntermediate(setOperand)
    }

    fun <T1, T2, T3, T4, T5, T6> select(
        propertyMetamodel1: PropertyMetamodel<T1>?,
        propertyMetamodel2: PropertyMetamodel<T2>?,
        propertyMetamodel3: PropertyMetamodel<T3>?,
        propertyMetamodel4: PropertyMetamodel<T4>?,
        propertyMetamodel5: PropertyMetamodel<T5>?,
        propertyMetamodel6: PropertyMetamodel<T6>?
    ): KSetOperand<Tuple6<T1, T2, T3, T4, T5, T6>> {
        val setOperand = statement.select(propertyMetamodel1, propertyMetamodel2, propertyMetamodel3, propertyMetamodel4, propertyMetamodel5, propertyMetamodel6)
        return KNativeSqlSelectIntermediate(setOperand)
    }

    fun <T1, T2, T3, T4, T5, T6, T7> select(
        propertyMetamodel1: PropertyMetamodel<T1>?,
        propertyMetamodel2: PropertyMetamodel<T2>?,
        propertyMetamodel3: PropertyMetamodel<T3>?,
        propertyMetamodel4: PropertyMetamodel<T4>?,
        propertyMetamodel5: PropertyMetamodel<T5>?,
        propertyMetamodel6: PropertyMetamodel<T6>?,
        propertyMetamodel7: PropertyMetamodel<T7>?
    ): KSetOperand<Tuple7<T1, T2, T3, T4, T5, T6, T7>> {
        val setOperand = statement.select(propertyMetamodel1, propertyMetamodel2, propertyMetamodel3, propertyMetamodel4, propertyMetamodel5, propertyMetamodel6, propertyMetamodel7)
        return KNativeSqlSelectIntermediate(setOperand)
    }

    fun <T1, T2, T3, T4, T5, T6, T7, T8> select(
        propertyMetamodel1: PropertyMetamodel<T1>?,
        propertyMetamodel2: PropertyMetamodel<T2>?,
        propertyMetamodel3: PropertyMetamodel<T3>?,
        propertyMetamodel4: PropertyMetamodel<T4>?,
        propertyMetamodel5: PropertyMetamodel<T5>?,
        propertyMetamodel6: PropertyMetamodel<T6>?,
        propertyMetamodel7: PropertyMetamodel<T7>?,
        propertyMetamodel8: PropertyMetamodel<T8>?
    ): KSetOperand<Tuple8<T1, T2, T3, T4, T5, T6, T7, T8>> {
        val setOperand = statement.select(propertyMetamodel1, propertyMetamodel2, propertyMetamodel3, propertyMetamodel4, propertyMetamodel5, propertyMetamodel6, propertyMetamodel7, propertyMetamodel8)
        return KNativeSqlSelectIntermediate(setOperand)
    }

    fun <T1, T2, T3, T4, T5, T6, T7, T8, T9> select(
        propertyMetamodel1: PropertyMetamodel<T1>?,
        propertyMetamodel2: PropertyMetamodel<T2>?,
        propertyMetamodel3: PropertyMetamodel<T3>?,
        propertyMetamodel4: PropertyMetamodel<T4>?,
        propertyMetamodel5: PropertyMetamodel<T5>?,
        propertyMetamodel6: PropertyMetamodel<T6>?,
        propertyMetamodel7: PropertyMetamodel<T7>?,
        propertyMetamodel8: PropertyMetamodel<T8>?,
        propertyMetamodel9: PropertyMetamodel<T9>?
    ): KSetOperand<Tuple9<T1, T2, T3, T4, T5, T6, T7, T8, T9>> {
        val setOperand = statement.select(propertyMetamodel1, propertyMetamodel2, propertyMetamodel3, propertyMetamodel4, propertyMetamodel5, propertyMetamodel6, propertyMetamodel7, propertyMetamodel8, propertyMetamodel9)
        return KNativeSqlSelectIntermediate(setOperand)
    }

    fun select(
        propertyMetamodel: PropertyMetamodel<*>,
        vararg propertyMetamodels: PropertyMetamodel<*>?
    ): KSetOperand<Row> {
        val setOperand = statement.select(propertyMetamodel, *propertyMetamodels)
        return KNativeSqlSelectIntermediate(setOperand)
    }

    fun <RESULT> selectTo(
        entityMetamodel: EntityMetamodel<RESULT>?,
        vararg propertyMetamodels: PropertyMetamodel<*>?
    ): KSetOperand<RESULT> {
        val setOperand = statement.selectTo(entityMetamodel, *propertyMetamodels)
        return KNativeSqlSelectIntermediate(setOperand)
    }

    private fun <RESULT> createMappedResultProviderFactory(
        rowMapper: Function<DataRow, RESULT>
    ): Function<SelectQuery, ObjectProvider<RESULT>> {
        return Function { query: SelectQuery? -> MappedResultProvider(query, rowMapper) }
    }

    override val context: SetOperationContext<ENTITY>
        get() = statement.context

    override fun execute(): List<ENTITY> {
        return statement.execute()
    }

    override fun <RESULT> mapSequence(sequenceMapper: (Sequence<ENTITY>) -> RESULT): RESULT {
        return statement.mapStream {
            sequenceMapper(it.asSequence())
        }
    }

    override fun union(other: KSetOperand<ENTITY>): KSetOperator<ENTITY> {
        val setOperator = statement.union(other.asSetOperand())
        return KNativeSqlSetStarting(setOperator)
    }

    override fun unionAll(other: KSetOperand<ENTITY>): KSetOperator<ENTITY> {
        val setOperator = statement.unionAll(other.asSetOperand())
        return KNativeSqlSetStarting(setOperator)
    }

    override fun peek(block: (Sql<*>) -> Unit): KSetOperand<ENTITY> {
        statement.peek(block)
        return this
    }

    override fun asSql(): Sql<*> {
        return statement.asSql()
    }

    override fun asSetOperand(): SetOperand<ENTITY> {
        return statement
    }
}
