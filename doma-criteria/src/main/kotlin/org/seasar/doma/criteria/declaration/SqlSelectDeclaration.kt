package org.seasar.doma.criteria.declaration

import org.seasar.doma.criteria.context.Projection
import org.seasar.doma.criteria.context.SelectContext
import org.seasar.doma.jdbc.entity.EntityPropertyDesc

@Declaration
class SqlSelectDeclaration(context: SelectContext) : SelectDeclaration(context), AggregateDeclaration {
    private val havingDeclaration = HavingDeclaration(context.config) { context.having.add(it) }

    fun groupBy(vararg propTypes: EntityPropertyDesc<*, *, *>) {
        context.groupBy.addAll(propTypes.asList())
    }

    fun having(block: HavingDeclaration.() -> Unit) = havingDeclaration.block()

    fun <CONTAINER> select(propType: EntityPropertyDesc<*, *, CONTAINER>): SqlSelectResult<CONTAINER?> {
        return select(propType) {
            it[propType]
        }
    }

    fun <CONTAINER1, CONTAINER2> select(
        first: EntityPropertyDesc<*, *, CONTAINER1>,
        second: EntityPropertyDesc<*, *, CONTAINER2>
    ): SqlSelectResult<Pair<CONTAINER1?, CONTAINER2?>> {
        return select(first, second) {
            val a = it[first]
            val b = it[second]
            a to b
        }
    }

    fun <CONTAINER1, CONTAINER2, CONTAINER3> select(
        first: EntityPropertyDesc<*, *, CONTAINER1>,
        second: EntityPropertyDesc<*, *, CONTAINER2>,
        third: EntityPropertyDesc<*, *, CONTAINER3>
    ): SqlSelectResult<Triple<CONTAINER1?, CONTAINER2?, CONTAINER3?>> {
        return select(first, second, third) {
            val a = it[first]
            val b = it[second]
            val c = it[third]
            Triple(a, b, c)
        }
    }

    fun <RESULT> select(vararg propTypes: EntityPropertyDesc<*, *, *>, mapper: (Row) -> RESULT): SqlSelectResult<RESULT> {
        val list = listOf(*propTypes)
        context.projection = Projection.List(list)
        return SqlSelectResult(list, mapper)
    }
}
