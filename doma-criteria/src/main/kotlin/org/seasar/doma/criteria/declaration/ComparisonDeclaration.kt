package org.seasar.doma.criteria.declaration

import org.seasar.doma.criteria.context.Criterion
import org.seasar.doma.jdbc.Config
import org.seasar.doma.jdbc.entity.EntityPropertyDesc

open class ComparisonDeclaration<DECLARATION : ComparisonDeclaration<DECLARATION>>(
    protected val config: Config,
    protected val add: (Criterion) -> Unit,
    private val newDeclaration: (Config, (Criterion) -> Unit) -> DECLARATION
) {

    protected val support = DeclarationSupport(config)

    infix fun <CONTAINER> EntityPropertyDesc<*, *, CONTAINER>.eq(other: CONTAINER?) {
        add(Criterion.Eq(support.toProp(this), support.toParam(this, other)))
    }

    infix fun <CONTAINER> EntityPropertyDesc<*, *, CONTAINER>.eq(other: EntityPropertyDesc<*, *, CONTAINER>) {
        add(Criterion.Eq(support.toProp(this), support.toProp(other)))
    }

    infix fun <CONTAINER> EntityPropertyDesc<*, *, CONTAINER>.ne(other: CONTAINER?) {
        add(Criterion.Ne(support.toProp(this), support.toParam(this, other)))
    }

    infix fun <CONTAINER> EntityPropertyDesc<*, *, CONTAINER>.ne(other: EntityPropertyDesc<*, *, CONTAINER>) {
        add(Criterion.Ne(support.toProp(this), support.toProp(other)))
    }

    infix fun <CONTAINER> EntityPropertyDesc<*, *, CONTAINER>.gt(other: CONTAINER?) {
        add(Criterion.Gt(support.toProp(this), support.toParam(this, other)))
    }

    infix fun <CONTAINER> EntityPropertyDesc<*, *, CONTAINER>.gt(other: EntityPropertyDesc<*, *, CONTAINER>) {
        add(Criterion.Gt(support.toProp(this), support.toProp(other)))
    }

    infix fun <CONTAINER> EntityPropertyDesc<*, *, CONTAINER>.ge(other: CONTAINER?) {
        add(Criterion.Ge(support.toProp(this), support.toParam(this, other)))
    }

    infix fun <CONTAINER> EntityPropertyDesc<*, *, CONTAINER>.ge(other: EntityPropertyDesc<*, *, CONTAINER>) {
        add(Criterion.Ge(support.toProp(this), support.toProp(other)))
    }

    infix fun <CONTAINER> EntityPropertyDesc<*, *, CONTAINER>.lt(other: CONTAINER?) {
        add(Criterion.Lt(support.toProp(this), support.toParam(this, other)))
    }

    infix fun <CONTAINER> EntityPropertyDesc<*, *, CONTAINER>.lt(other: EntityPropertyDesc<*, *, CONTAINER>) {
        add(Criterion.Lt(support.toProp(this), support.toProp(other)))
    }

    infix fun <CONTAINER> EntityPropertyDesc<*, *, CONTAINER>.le(other: CONTAINER?) {
        add(Criterion.Le(support.toProp(this), support.toParam(this, other)))
    }

    infix fun <CONTAINER> EntityPropertyDesc<*, *, CONTAINER>.le(other: EntityPropertyDesc<*, *, CONTAINER>) {
        add(Criterion.Le(support.toProp(this), support.toProp(other)))
    }

    fun not(block: DECLARATION.() -> Unit) = runBlock(block, Criterion::Not)

    fun and(block: DECLARATION.() -> Unit) = runBlock(block, Criterion::And)

    fun or(block: DECLARATION.() -> Unit) = runBlock(block, Criterion::Or)

    private fun runBlock(block: DECLARATION.() -> Unit, newCriterion: (List<Criterion>) -> Criterion) {
        val criterionList = mutableListOf<Criterion>()
        val declaration = newDeclaration(config) { criterionList.add(it) }
        declaration.block()
        if (criterionList.isNotEmpty()) {
            add(newCriterion(criterionList))
        }
    }
}
