package org.seasar.doma.criteria

import org.seasar.doma.jdbc.entity.EntityPropertyDesc
import org.seasar.doma.jdbc.entity.EntityPropertyType

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

    fun <RESULT> select(vararg propTypes: EntityPropertyDesc<*, *, *>, mapper: (Row) -> RESULT): Pair<List<EntityPropertyType<*, *>>, (Row) -> RESULT> {
        val list = listOf(*propTypes)
        context.projection = Projection.List(list)
        return list to mapper
    }
}
