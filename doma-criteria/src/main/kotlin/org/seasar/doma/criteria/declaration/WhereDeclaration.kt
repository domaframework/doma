package org.seasar.doma.criteria.declaration

import org.seasar.doma.criteria.context.Criterion
import org.seasar.doma.criteria.context.SelectContext
import org.seasar.doma.jdbc.Config
import org.seasar.doma.jdbc.entity.EntityPropertyDesc

@Declaration
@Suppress("FunctionName")
class WhereDeclaration(
    config: Config,
    add: (Criterion) -> Unit
) : ComparisonDeclaration<WhereDeclaration>(config, add, ::WhereDeclaration) {

    infix fun <CONTAINER> EntityPropertyDesc<*, String, CONTAINER>.like(other: CONTAINER) {
        add(Criterion.Like(support.toProp(this), support.toParam(this, other)))
    }

    infix fun <CONTAINER> EntityPropertyDesc<*, String, CONTAINER>.notLike(other: CONTAINER) {
        add(Criterion.NotLike(support.toProp(this), support.toParam(this, other)))
    }

    infix fun <CONTAINER : Comparable<CONTAINER>> EntityPropertyDesc<*, *, CONTAINER>.between(other: ClosedRange<CONTAINER>) {
        add(Criterion.Between(support.toProp(this),
                support.toParam(this, other.start),
                support.toParam(this, other.endInclusive)))
    }

    infix fun <CONTAINER> EntityPropertyDesc<*, *, CONTAINER>.`in`(other: List<CONTAINER>) {
        add(Criterion.InSingle(support.toProp(this), other.map { support.toParam(this, it) }))
    }

    infix fun <CONTAINER1, CONTAINER2> Pair<EntityPropertyDesc<*, *, CONTAINER1>, EntityPropertyDesc<*, *, CONTAINER2>>.`in`(
        other: List<Pair<CONTAINER1, CONTAINER2>>
    ) {
        val prop1 = support.toProp(this.first)
        val prop2 = support.toProp(this.second)
        val params = other.map { (first, second) ->
            val param1 = support.toParam(this.first, first)
            val param2 = support.toParam(this.second, second)
            param1 to param2
        }
        add(Criterion.InPair(prop1 to prop2, params))
    }

    infix fun <CONTAINER> EntityPropertyDesc<*, *, CONTAINER>.`in`(
        block: SingleSubQueryDeclaration<CONTAINER>.() -> SelectContext
    ) {
        val declaration = SingleSubQueryDeclaration<CONTAINER>(config)
        val context = declaration.block()
        add(Criterion.InSingleSubQuery(support.toProp(this), context))
    }

    infix fun <CONTAINER1, CONTAINER2> Pair<EntityPropertyDesc<*, *, CONTAINER1>, EntityPropertyDesc<*, *, CONTAINER2>>.`in`(
        block: PairSubQueryDeclaration<CONTAINER1, CONTAINER2>.() -> SelectContext
    ) {
        val prop1 = support.toProp(this.first)
        val prop2 = support.toProp(this.second)
        val declaration = PairSubQueryDeclaration<CONTAINER1, CONTAINER2>(config)
        val context = declaration.block()
        add(Criterion.InPairSubQuery(prop1 to prop2, context))
    }

    infix fun <CONTAINER> EntityPropertyDesc<*, *, CONTAINER>.notIn(other: List<CONTAINER>) {
        add(Criterion.NotInSingle(support.toProp(this), other.map { support.toParam(this, it) }))
    }

    infix fun <CONTAINER1, CONTAINER2> Pair<EntityPropertyDesc<*, *, CONTAINER1>, EntityPropertyDesc<*, *, CONTAINER2>>.notIn(
        other: List<Pair<CONTAINER1, CONTAINER2>>
    ) {
        val prop1 = support.toProp(this.first)
        val prop2 = support.toProp(this.second)
        val params = other.map { (first, second) ->
            val param1 = support.toParam(this.first, first)
            val param2 = support.toParam(this.second, second)
            param1 to param2
        }
        add(Criterion.NotInPair(prop1 to prop2, params))
    }

    infix fun <CONTAINER> EntityPropertyDesc<*, *, CONTAINER>.notIn(block: SingleSubQueryDeclaration<CONTAINER>.() -> SelectContext) {
        val declaration = SingleSubQueryDeclaration<CONTAINER>(config)
        val context = declaration.block()
        add(Criterion.NotInSingleSubQuery(support.toProp(this), context))
    }

    infix fun <CONTAINER1, CONTAINER2> Pair<EntityPropertyDesc<*, *, CONTAINER1>, EntityPropertyDesc<*, *, CONTAINER2>>.notIn(
        block: PairSubQueryDeclaration<CONTAINER1, CONTAINER2>.() -> SelectContext
    ) {
        val prop1 = support.toProp(this.first)
        val prop2 = support.toProp(this.second)
        val declaration = PairSubQueryDeclaration<CONTAINER1, CONTAINER2>(config)
        val context = declaration.block()
        add(Criterion.NotInPairSubQuery(prop1 to prop2, context))
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
}
