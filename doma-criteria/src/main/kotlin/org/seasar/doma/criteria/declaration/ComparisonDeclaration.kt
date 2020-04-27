package org.seasar.doma.criteria.declaration

import org.seasar.doma.criteria.context.Criterion
import org.seasar.doma.def.PropertyDef
import org.seasar.doma.jdbc.Config

open class ComparisonDeclaration<DECLARATION : ComparisonDeclaration<DECLARATION>>(
    protected val config: Config,
    protected val add: (Criterion) -> Unit,
    private val newDeclaration: (Config, (Criterion) -> Unit) -> DECLARATION
) {

    protected val support = DeclarationSupport(config)

    infix fun <PROPERTY> PropertyDef<PROPERTY>.eq(other: PROPERTY?) {
        add(Criterion.Eq(support.toProp(this), support.toParam(this, other)))
    }

    infix fun <PROPERTY> PropertyDef<PROPERTY>.eq(other: PropertyDef<PROPERTY>) {
        add(Criterion.Eq(support.toProp(this), support.toProp(other)))
    }

    infix fun <PROPERTY> PropertyDef<PROPERTY>.ne(other: PROPERTY?) {
        add(Criterion.Ne(support.toProp(this), support.toParam(this, other)))
    }

    infix fun <PROPERTY> PropertyDef<PROPERTY>.ne(other: PropertyDef<PROPERTY>) {
        add(Criterion.Ne(support.toProp(this), support.toProp(other)))
    }

    infix fun <PROPERTY> PropertyDef<PROPERTY>.gt(other: PROPERTY) {
        add(Criterion.Gt(support.toProp(this), support.toParam(this, other)))
    }

    infix fun <PROPERTY> PropertyDef<PROPERTY>.gt(other: PropertyDef<PROPERTY>) {
        add(Criterion.Gt(support.toProp(this), support.toProp(other)))
    }

    infix fun <PROPERTY> PropertyDef<PROPERTY>.ge(other: PROPERTY) {
        add(Criterion.Ge(support.toProp(this), support.toParam(this, other)))
    }

    infix fun <PROPERTY> PropertyDef<PROPERTY>.ge(other: PropertyDef<PROPERTY>) {
        add(Criterion.Ge(support.toProp(this), support.toProp(other)))
    }

    infix fun <PROPERTY> PropertyDef<PROPERTY>.lt(other: PROPERTY) {
        add(Criterion.Lt(support.toProp(this), support.toParam(this, other)))
    }

    infix fun <PROPERTY> PropertyDef<PROPERTY>.lt(other: PropertyDef<PROPERTY>) {
        add(Criterion.Lt(support.toProp(this), support.toProp(other)))
    }

    infix fun <PROPERTY> PropertyDef<PROPERTY>.le(other: PROPERTY) {
        add(Criterion.Le(support.toProp(this), support.toParam(this, other)))
    }

    infix fun <PROPERTY> PropertyDef<PROPERTY>.le(other: PropertyDef<PROPERTY>) {
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
