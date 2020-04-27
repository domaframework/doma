package org.seasar.doma.criteria.declaration

import org.seasar.doma.criteria.context.Projection
import org.seasar.doma.criteria.context.SelectContext
import org.seasar.doma.def.PropertyDef

@Declaration
class SqlSelectDeclaration(context: SelectContext) : SelectDeclaration(context), AggregateDeclaration {
    private val havingDeclaration = HavingDeclaration(context.config) { context.having.add(it) }

    fun groupBy(vararg propDefs: PropertyDef<*>) {
        context.groupBy.addAll(propDefs.asList().map { it.asType() })
    }

    fun having(block: HavingDeclaration.() -> Unit) = havingDeclaration.block()

    fun <PROPERTY> select(propType: PropertyDef<PROPERTY>): SqlSelectResult<PROPERTY?> {
        return select(propType) {
            it[propType]
        }
    }

    fun <PROPERTY1, PROPERTY2> select(
        first: PropertyDef<PROPERTY1>,
        second: PropertyDef<PROPERTY2>
    ): SqlSelectResult<Pair<PROPERTY1?, PROPERTY2?>> {
        return select(first, second) {
            val a = it[first]
            val b = it[second]
            a to b
        }
    }

    fun <PROPERTY1, PROPERTY2, PROPERTY3> select(
        first: PropertyDef<PROPERTY1>,
        second: PropertyDef<PROPERTY2>,
        third: PropertyDef<PROPERTY3>
    ): SqlSelectResult<Triple<PROPERTY1?, PROPERTY2?, PROPERTY3?>> {
        return select(first, second, third) {
            val a = it[first]
            val b = it[second]
            val c = it[third]
            Triple(a, b, c)
        }
    }

    fun <RESULT_ELEMENT> select(vararg propDefs: PropertyDef<*>, mapper: (Row) -> RESULT_ELEMENT): SqlSelectResult<RESULT_ELEMENT> {
        val list = listOf(*propDefs)
        context.projection = Projection.List(list.map { it.asType() })
        return SqlSelectResult(context, list, mapper)
    }
}
