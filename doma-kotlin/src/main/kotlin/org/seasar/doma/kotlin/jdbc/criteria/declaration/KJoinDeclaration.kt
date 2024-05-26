package org.seasar.doma.kotlin.jdbc.criteria.declaration

import org.seasar.doma.jdbc.criteria.context.SubSelectContext
import org.seasar.doma.jdbc.criteria.declaration.JoinDeclaration
import org.seasar.doma.jdbc.criteria.declaration.SubSelectFromDeclaration
import org.seasar.doma.jdbc.criteria.metamodel.EntityMetamodel
import org.seasar.doma.jdbc.criteria.metamodel.PropertyMetamodel
import org.seasar.doma.jdbc.criteria.option.LikeOption
import org.seasar.doma.jdbc.criteria.tuple.Tuple2
import org.seasar.doma.jdbc.criteria.tuple.Tuple3

class KJoinDeclaration(private val declaration: JoinDeclaration) {
    fun <PROPERTY> eq(left: PropertyMetamodel<PROPERTY>, right: PROPERTY) {
        declaration.eq(left, right)
    }

    fun <PROPERTY> eq(left: PropertyMetamodel<PROPERTY>, right: PropertyMetamodel<PROPERTY>) {
        declaration.eq(left, right)
    }

    fun <PROPERTY> ne(left: PropertyMetamodel<PROPERTY>, right: PROPERTY) {
        declaration.ne(left, right)
    }

    fun <PROPERTY> ne(left: PropertyMetamodel<PROPERTY>, right: PropertyMetamodel<PROPERTY>) {
        declaration.ne(left, right)
    }

    fun <PROPERTY> gt(left: PropertyMetamodel<PROPERTY>, right: PROPERTY) {
        declaration.gt(left, right)
    }

    fun <PROPERTY> gt(left: PropertyMetamodel<PROPERTY>, right: PropertyMetamodel<PROPERTY>) {
        declaration.gt(left, right)
    }

    fun <PROPERTY> ge(left: PropertyMetamodel<PROPERTY>, right: PROPERTY) {
        declaration.ge(left, right)
    }

    fun <PROPERTY> ge(left: PropertyMetamodel<PROPERTY>, right: PropertyMetamodel<PROPERTY>) {
        declaration.ge(left, right)
    }

    fun <PROPERTY> lt(left: PropertyMetamodel<PROPERTY>, right: PROPERTY) {
        declaration.lt(left, right)
    }

    fun <PROPERTY> lt(left: PropertyMetamodel<PROPERTY>, right: PropertyMetamodel<PROPERTY>) {
        declaration.lt(left, right)
    }

    fun <PROPERTY> le(left: PropertyMetamodel<PROPERTY>, right: PROPERTY) {
        declaration.le(left, right)
    }

    fun <PROPERTY> le(left: PropertyMetamodel<PROPERTY>, right: PropertyMetamodel<PROPERTY>) {
        declaration.le(left, right)
    }

    fun <PROPERTY> isNull(propertyMetamodel: PropertyMetamodel<PROPERTY>) {
        declaration.isNull(propertyMetamodel)
    }

    fun <PROPERTY> isNotNull(propertyMetamodel: PropertyMetamodel<PROPERTY>) {
        declaration.isNotNull(propertyMetamodel)
    }

    fun <PROPERTY> eqOrIsNull(left: PropertyMetamodel<PROPERTY>, right: PROPERTY) {
        declaration.eqOrIsNull(left, right)
    }

    fun <PROPERTY> neOrIsNotNull(left: PropertyMetamodel<PROPERTY>, right: PROPERTY) {
        declaration.neOrIsNotNull(left, right)
    }

    fun <PROPERTY> like(left: PropertyMetamodel<PROPERTY>, right: CharSequence) {
        declaration.like(left, right)
    }

    fun <PROPERTY> like(left: PropertyMetamodel<PROPERTY>, right: CharSequence, option: LikeOption) {
        declaration.like(left, right, option)
    }

    fun <PROPERTY> notLike(left: PropertyMetamodel<PROPERTY>, right: CharSequence) {
        declaration.notLike(left, right)
    }

    fun <PROPERTY> notLike(left: PropertyMetamodel<PROPERTY>, right: CharSequence, option: LikeOption) {
        declaration.notLike(left, right, option)
    }

    fun <PROPERTY> between(propertyMetamodel: PropertyMetamodel<PROPERTY>, start: PROPERTY, end: PROPERTY) {
        declaration.between(propertyMetamodel, start, end)
    }

    fun <PROPERTY> `in`(left: PropertyMetamodel<PROPERTY>, right: List<PROPERTY>) {
        declaration.`in`(left, right)
    }

    fun <PROPERTY> notIn(left: PropertyMetamodel<PROPERTY>, right: List<PROPERTY>) {
        declaration.notIn(left, right)
    }

    fun <PROPERTY> `in`(
        left: PropertyMetamodel<PROPERTY>,
        right: SubSelectContext<PropertyMetamodel<PROPERTY>>,
    ) {
        declaration.`in`(left, right)
    }

    fun <PROPERTY> notIn(
        left: PropertyMetamodel<PROPERTY>,
        right: SubSelectContext<PropertyMetamodel<PROPERTY>>,
    ) {
        declaration.notIn(left, right)
    }

    fun <PROPERTY1, PROPERTY2> `in`(
        left: Tuple2<PropertyMetamodel<PROPERTY1>, PropertyMetamodel<PROPERTY2>>,
        right: List<Tuple2<PROPERTY1, PROPERTY2>>,
    ) {
        declaration.`in`(left, right)
    }

    fun <PROPERTY1, PROPERTY2> notIn(
        left: Tuple2<PropertyMetamodel<PROPERTY1>, PropertyMetamodel<PROPERTY2>>,
        right: List<Tuple2<PROPERTY1, PROPERTY2>>,
    ) {
        declaration.notIn(left, right)
    }

    fun <PROPERTY1, PROPERTY2> `in`(
        left: Tuple2<PropertyMetamodel<PROPERTY1>, PropertyMetamodel<PROPERTY2>>,
        right: SubSelectContext<Tuple2<PropertyMetamodel<PROPERTY1>, PropertyMetamodel<PROPERTY2>>>,
    ) {
        declaration.`in`(left, right)
    }

    fun <PROPERTY1, PROPERTY2> notIn(
        left: Tuple2<PropertyMetamodel<PROPERTY1>, PropertyMetamodel<PROPERTY2>>,
        right: SubSelectContext<Tuple2<PropertyMetamodel<PROPERTY1>, PropertyMetamodel<PROPERTY2>>>,
    ) {
        declaration.notIn(left, right)
    }

    fun <PROPERTY1, PROPERTY2, PROPERTY3> `in`(
        left: Tuple3<PropertyMetamodel<PROPERTY1>, PropertyMetamodel<PROPERTY2>, PropertyMetamodel<PROPERTY3>>,
        right: List<Tuple3<PROPERTY1, PROPERTY2, PROPERTY3>>,
    ) {
        declaration.`in`(left, right)
    }

    fun <PROPERTY1, PROPERTY2, PROPERTY3> notIn(
        left: Tuple3<PropertyMetamodel<PROPERTY1>, PropertyMetamodel<PROPERTY2>, PropertyMetamodel<PROPERTY3>>,
        right: List<Tuple3<PROPERTY1, PROPERTY2, PROPERTY3>>,
    ) {
        declaration.notIn(left, right)
    }

    fun <PROPERTY1, PROPERTY2, PROPERTY3> `in`(
        left: Tuple3<PropertyMetamodel<PROPERTY1>, PropertyMetamodel<PROPERTY2>, PropertyMetamodel<PROPERTY3>>,
        right: SubSelectContext<Tuple3<PropertyMetamodel<PROPERTY1>, PropertyMetamodel<PROPERTY2>, PropertyMetamodel<PROPERTY3>>>,
    ) {
        declaration.`in`(left, right)
    }

    fun <PROPERTY1, PROPERTY2, PROPERTY3> notIn(
        left: Tuple3<PropertyMetamodel<PROPERTY1>, PropertyMetamodel<PROPERTY2>, PropertyMetamodel<PROPERTY3>>,
        right: SubSelectContext<Tuple3<PropertyMetamodel<PROPERTY1>, PropertyMetamodel<PROPERTY2>, PropertyMetamodel<PROPERTY3>>>,
    ) {
        declaration.notIn(left, right)
    }

    fun exists(
        subSelectContext: SubSelectContext<*>,
    ) {
        declaration.exists(subSelectContext)
    }

    fun notExists(
        subSelectContext: SubSelectContext<*>,
    ) {
        declaration.notExists(subSelectContext)
    }

    fun <ENTITY> from(
        entityMetamodel: EntityMetamodel<ENTITY>,
    ): SubSelectFromDeclaration<ENTITY> {
        return declaration.from(entityMetamodel)
    }

    fun and(
        block: KJoinDeclaration.() -> Unit,
    ) {
        return declaration.and {
            block()
        }
    }

    fun or(
        block: KJoinDeclaration.() -> Unit,
    ) {
        return declaration.or {
            block()
        }
    }

    fun not(
        block: KJoinDeclaration.() -> Unit,
    ) {
        return declaration.not {
            block()
        }
    }
}
