package org.seasar.doma.criteria

import org.seasar.doma.jdbc.Config
import org.seasar.doma.jdbc.entity.EntityPropertyDesc

@Declaration
@Suppress("FunctionName")
class WhereDeclaration(private val config: Config, private val add: (Criterion) -> Unit) {

    private val support = DeclarationSupport(config)

    fun <CONTAINER> eq(left: EntityPropertyDesc<*, *, CONTAINER>, right: CONTAINER?) {
        add(Criterion.Eq(support.toProp(left), support.toParam(left, right)))
    }

    fun <CONTAINER> eq(left: EntityPropertyDesc<*, *, CONTAINER>, right: EntityPropertyDesc<*, *, CONTAINER>) {
        add(Criterion.Eq(support.toProp(left), support.toProp(right)))
    }

    fun <CONTAINER> ne(left: EntityPropertyDesc<*, *, CONTAINER>, right: CONTAINER?) {
        add(Criterion.Ne(support.toProp(left), support.toParam(left, right)))
    }

    fun <CONTAINER> ne(left: EntityPropertyDesc<*, *, CONTAINER>, right: EntityPropertyDesc<*, *, CONTAINER>) {
        add(Criterion.Ne(support.toProp(left), support.toProp(right)))
    }

    fun <CONTAINER> gt(left: EntityPropertyDesc<*, *, CONTAINER>, right: CONTAINER?) {
        add(Criterion.Gt(support.toProp(left), support.toParam(left, right)))
    }

    fun <CONTAINER> gt(left: EntityPropertyDesc<*, *, CONTAINER>, right: EntityPropertyDesc<*, *, CONTAINER>) {
        add(Criterion.Gt(support.toProp(left), support.toProp(right)))
    }

    fun <CONTAINER> ge(left: EntityPropertyDesc<*, *, CONTAINER>, right: CONTAINER?) {
        add(Criterion.Ge(support.toProp(left), support.toParam(left, right)))
    }

    fun <CONTAINER> ge(left: EntityPropertyDesc<*, *, CONTAINER>, right: EntityPropertyDesc<*, *, CONTAINER>) {
        add(Criterion.Ge(support.toProp(left), support.toProp(right)))
    }

    fun <CONTAINER> lt(left: EntityPropertyDesc<*, *, CONTAINER>, right: CONTAINER?) {
        add(Criterion.Lt(support.toProp(left), support.toParam(left, right)))
    }

    fun <CONTAINER> lt(left: EntityPropertyDesc<*, *, CONTAINER>, right: EntityPropertyDesc<*, *, CONTAINER>) {
        add(Criterion.Lt(support.toProp(left), support.toProp(right)))
    }

    fun <CONTAINER> le(left: EntityPropertyDesc<*, *, CONTAINER>, right: CONTAINER?) {
        add(Criterion.Le(support.toProp(left), support.toParam(left, right)))
    }

    fun <CONTAINER> le(left: EntityPropertyDesc<*, *, CONTAINER>, right: EntityPropertyDesc<*, *, CONTAINER>) {
        add(Criterion.Le(support.toProp(left), support.toProp(right)))
    }

    fun <CONTAINER> like(left: EntityPropertyDesc<*, String, CONTAINER>, right: CONTAINER?) {
        add(Criterion.Like(support.toProp(left), support.toParam(left, right)))
    }

    fun <CONTAINER> notLike(left: EntityPropertyDesc<*, String, CONTAINER>, right: CONTAINER?) {
        add(Criterion.NotLike(support.toProp(left), support.toParam(left, right)))
    }

    fun <CONTAINER> between(propType: EntityPropertyDesc<*, *, CONTAINER>, begin: CONTAINER?, end: CONTAINER?) {
        add(Criterion.Between(support.toProp(propType), support.toParam(propType, begin), support.toParam(propType, end)))
    }

    fun exists(block: ExistsSubQueryDeclaration.() -> SelectContext) {
        val declaration = ExistsSubQueryDeclaration(config)
        val context = declaration.block()
        add(Criterion.Exists(context))
    }

    fun notExists(block: ExistsSubQueryDeclaration.() -> SelectContext) {
        val declaration = ExistsSubQueryDeclaration(config)
        val context = declaration.block()
        add(Criterion.NotExists(context))
    }

    fun <CONTAINER> `in`(left: EntityPropertyDesc<*, *, CONTAINER>, right: List<CONTAINER?>) {
        add(Criterion.InSingle(support.toProp(left), right.map { support.toParam(left, it) }))
    }

    fun <CONTAINER1, CONTAINER2> `in`(
        left: Pair<EntityPropertyDesc<*, *, CONTAINER1>, EntityPropertyDesc<*, *, CONTAINER2>>,
        right: List<Pair<CONTAINER1, CONTAINER2>>
    ) {
        val prop1 = support.toProp(left.first)
        val prop2 = support.toProp(left.second)
        val params = right.map { (first, second) ->
            val param1 = support.toParam(left.first, first)
            val param2 = support.toParam(left.second, second)
            param1 to param2
        }
        add(Criterion.InPair(prop1 to prop2, params))
    }

    fun <CONTAINER> `in`(
        propType: EntityPropertyDesc<*, *, CONTAINER>,
        block: SingleSubQueryDeclaration<CONTAINER>.() -> SelectContext
    ) {
        val declaration = SingleSubQueryDeclaration<CONTAINER>(config)
        val context = declaration.block()
        add(Criterion.InSingleSubQuery(support.toProp(propType), context))
    }

    fun <CONTAINER1, CONTAINER2> `in`(
        pair: Pair<EntityPropertyDesc<*, *, CONTAINER1>, EntityPropertyDesc<*, *, CONTAINER2>>,
        block: PairSubQueryDeclaration<CONTAINER1, CONTAINER2>.() -> SelectContext
    ) {
        val prop1 = support.toProp(pair.first)
        val prop2 = support.toProp(pair.second)
        val declaration = PairSubQueryDeclaration<CONTAINER1, CONTAINER2>(config)
        val context = declaration.block()
        add(Criterion.InPairSubQuery(prop1 to prop2, context))
    }

    fun <CONTAINER> notIn(left: EntityPropertyDesc<*, *, CONTAINER>, right: List<CONTAINER>) {
        add(Criterion.NotInSingle(support.toProp(left), right.map { support.toParam(left, it) }))
    }

    fun <CONTAINER1, CONTAINER2> notIn(
        left: Pair<EntityPropertyDesc<*, *, CONTAINER1>, EntityPropertyDesc<*, *, CONTAINER2>>,
        right: List<Pair<CONTAINER1, CONTAINER2>>
    ) {
        val (first, second) = left
        val prop1 = support.toProp(first)
        val prop2 = support.toProp(second)
        val params = right.map { (first, second) ->
            val param1 = support.toParam(left.first, first)
            val param2 = support.toParam(left.second, second)
            param1 to param2
        }
        add(Criterion.NotInPair(prop1 to prop2, params))
    }

    fun <CONTAINER> notIn(propType: EntityPropertyDesc<*, *, CONTAINER>, block: SingleSubQueryDeclaration<CONTAINER>.() -> SelectContext) {
        val declaration = SingleSubQueryDeclaration<CONTAINER>(config)
        val context = declaration.block()
        add(Criterion.NotInSingleSubQuery(support.toProp(propType), context))
    }

    fun <CONTAINER1, CONTAINER2> notIn(
        pair: Pair<EntityPropertyDesc<*, *, CONTAINER1>, EntityPropertyDesc<*, *, CONTAINER2>>,
        block: PairSubQueryDeclaration<CONTAINER1, CONTAINER2>.() -> SelectContext
    ) {
        val prop1 = support.toProp(pair.first)
        val prop2 = support.toProp(pair.second)
        val declaration = PairSubQueryDeclaration<CONTAINER1, CONTAINER2>(config)
        val context = declaration.block()
        add(Criterion.NotInPairSubQuery(prop1 to prop2, context))
    }

    fun not(block: WhereDeclaration.() -> Unit) = runBlock(block, Criterion::Not)

    fun and(block: WhereDeclaration.() -> Unit) = runBlock(block, Criterion::And)

    fun or(block: WhereDeclaration.() -> Unit) = runBlock(block, Criterion::Or)

    private fun runBlock(block: WhereDeclaration.() -> Unit, newCriterion: (List<Criterion>) -> Criterion) {
        val criterionList = mutableListOf<Criterion>()
        val declaration = WhereDeclaration(config) { criterionList.add(it) }
        declaration.block()
        if (criterionList.isNotEmpty()) {
            add(newCriterion(criterionList))
        }
    }
}
