package org.seasar.doma.criteria.declaration

import org.seasar.doma.criteria.context.Projection
import org.seasar.doma.criteria.context.SelectContext
import org.seasar.doma.jdbc.entity.EntityPropertyDesc

@Declaration
class SqlSelectDeclaration(context: SelectContext) : SelectDeclaration(context) {
    private val havingDeclaration = HavingDeclaration(context.config) { context.having.add(it) }

    fun <ENTITY, BASIC, CONTAINER> count(propType: EntityPropertyDesc<ENTITY, BASIC, CONTAINER>): Count<ENTITY, BASIC, CONTAINER> {
        return Count(propType)
    }

    fun groupBy(vararg propTypes: EntityPropertyDesc<*, *, *>) {
        context.groupBy.addAll(propTypes.asList())
    }

    fun having(block: HavingDeclaration.() -> Unit) = havingDeclaration.block()

    fun <RESULT> select(propType: EntityPropertyDesc<*, *, RESULT>): SqlSelectResult<RESULT?> {
        return select(propType) {
            it[propType]
        }
    }

    fun <CONTAINER1, CONTAINER2> select(
        propType1: EntityPropertyDesc<*, *, CONTAINER1>,
        propType2: EntityPropertyDesc<*, *, CONTAINER2>
    ): SqlSelectResult<Pair<CONTAINER1?, CONTAINER2?>> {
        return select(propType1, propType2) {
            val a = it[propType1]
            val b = it[propType2]
            a to b
        }
    }

    fun <CONTAINER1, CONTAINER2, CONTAINER3> select(
        propType1: EntityPropertyDesc<*, *, CONTAINER1>,
        propType2: EntityPropertyDesc<*, *, CONTAINER2>,
        propType3: EntityPropertyDesc<*, *, CONTAINER3>
    ): SqlSelectResult<Triple<CONTAINER1?, CONTAINER2?, CONTAINER3?>> {
        return select(propType1, propType2, propType3) {
            val a = it[propType1]
            val b = it[propType2]
            val c = it[propType3]
            Triple(a, b, c)
        }
    }

    fun <RESULT> select(vararg propTypes: EntityPropertyDesc<*, *, *>, mapper: (Row) -> RESULT): SqlSelectResult<RESULT> {
        val list = listOf(*propTypes)
        context.projection = Projection.List(list)
        return SqlSelectResult(list, mapper)
    }
}
