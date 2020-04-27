package org.seasar.doma.criteria.declaration

import org.seasar.doma.criteria.context.Criterion
import org.seasar.doma.criteria.context.SelectContext
import org.seasar.doma.def.PropertyDef
import org.seasar.doma.jdbc.Config

@Declaration
class WhereDeclaration(
    config: Config,
    add: (Criterion) -> Unit
) : ComparisonDeclaration<WhereDeclaration>(config, add, ::WhereDeclaration) {

    fun <PROPERTY> PropertyDef<PROPERTY>.isNull() {
        add(Criterion.IsNull(support.toProp(this)))
    }

    fun <PROPERTY> PropertyDef<PROPERTY>.isNotNull() {
        add(Criterion.IsNotNull(support.toProp(this)))
    }

    infix fun <PROPERTY> PropertyDef<PROPERTY>.like(other: PROPERTY) {
        add(Criterion.Like(support.toProp(this), support.toParam(this, other)))
    }

    infix fun <PROPERTY> PropertyDef<PROPERTY>.notLike(other: PROPERTY) {
        add(Criterion.NotLike(support.toProp(this), support.toParam(this, other)))
    }

    infix fun <PROPERTY : Comparable<PROPERTY>> PropertyDef<PROPERTY>.between(other: ClosedRange<PROPERTY>) {
        add(Criterion.Between(support.toProp(this),
                support.toParam(this, other.start),
                support.toParam(this, other.endInclusive)))
    }

    infix fun <PROPERTY> PropertyDef<PROPERTY>.`in`(other: List<PROPERTY>) {
        add(Criterion.InSingle(support.toProp(this), other.map { support.toParam(this, it) }))
    }

    infix fun <PROPERTY1, PROPERTY2> Pair<PropertyDef<PROPERTY1>, PropertyDef<PROPERTY2>>.`in`(
        other: List<Pair<PROPERTY1, PROPERTY2>>
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

    infix fun <PROPERTY> PropertyDef<PROPERTY>.`in`(
        block: SubQueryDeclaration<SqlSelectResult<PROPERTY>>.() -> SelectContext
    ) {
        val declaration = SubQueryDeclaration<SqlSelectResult<PROPERTY>>(config)
        val context = declaration.block()
        add(Criterion.InSingleSubQuery(support.toProp(this), context))
    }

    infix fun <PROPERTY1, PROPERTY2> Pair<PropertyDef<PROPERTY1>, PropertyDef<PROPERTY2>>.`in`(
        block: SubQueryDeclaration<SqlSelectResult<Pair<PROPERTY1, PROPERTY2>>>.() -> SelectContext
    ) {
        val prop1 = support.toProp(this.first)
        val prop2 = support.toProp(this.second)
        val declaration = SubQueryDeclaration<SqlSelectResult<Pair<PROPERTY1, PROPERTY2>>>(config)
        val context = declaration.block()
        add(Criterion.InPairSubQuery(prop1 to prop2, context))
    }

    infix fun <PROPERTY> PropertyDef<PROPERTY>.notIn(other: List<PROPERTY>) {
        add(Criterion.NotInSingle(support.toProp(this), other.map { support.toParam(this, it) }))
    }

    infix fun <PROPERTY1, PROPERTY2> Pair<PropertyDef<PROPERTY1>, PropertyDef<PROPERTY2>>.notIn(
        other: List<Pair<PROPERTY1, PROPERTY2>>
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

    infix fun <PROPERTY> PropertyDef<PROPERTY>.notIn(block: SubQueryDeclaration<SqlSelectResult<PROPERTY>>.() -> SelectContext) {
        val declaration = SubQueryDeclaration<SqlSelectResult<PROPERTY>>(config)
        val context = declaration.block()
        add(Criterion.NotInSingleSubQuery(support.toProp(this), context))
    }

    infix fun <PROPERTY1, PROPERTY2> Pair<PropertyDef<PROPERTY1>, PropertyDef<PROPERTY2>>.notIn(
        block: SubQueryDeclaration<SqlSelectResult<Pair<PROPERTY1, PROPERTY2>>>.() -> SelectContext
    ) {
        val prop1 = support.toProp(this.first)
        val prop2 = support.toProp(this.second)
        val declaration = SubQueryDeclaration<SqlSelectResult<Pair<PROPERTY1, PROPERTY2>>>(config)
        val context = declaration.block()
        add(Criterion.NotInPairSubQuery(prop1 to prop2, context))
    }

    fun exists(block: SubQueryDeclaration<Unit>.() -> SelectContext) {
        val declaration = SubQueryDeclaration<Unit>(config)
        val context = declaration.block()
        add(Criterion.Exists(context))
    }

    fun notExists(block: SubQueryDeclaration<Unit>.() -> SelectContext) {
        val declaration = SubQueryDeclaration<Unit>(config)
        val context = declaration.block()
        add(Criterion.NotExists(context))
    }
}
