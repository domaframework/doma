package org.seasar.doma.criteria.declaration

import org.seasar.doma.criteria.context.Criterion
import org.seasar.doma.jdbc.Config
import org.seasar.doma.jdbc.entity.EntityPropertyDesc

@Declaration
@Suppress("FunctionName")
class HavingDeclaration(
    config: Config,
    add: (Criterion) -> Unit
) : ComparisonDeclaration<HavingDeclaration>(config, add, ::HavingDeclaration) {

    val `*` = CountAsterisk

    fun <ENTITY, BASIC, CONTAINER> avg(propType: EntityPropertyDesc<ENTITY, BASIC, CONTAINER>): Avg<ENTITY, BASIC, CONTAINER> {
        return support.avg(propType)
    }

    fun count(propType: EntityPropertyDesc<*, *, *>): Count {
        return support.count(propType)
    }

    fun <ENTITY, BASIC, CONTAINER> max(propType: EntityPropertyDesc<ENTITY, BASIC, CONTAINER>): Max<ENTITY, BASIC, CONTAINER> {
        return support.max(propType)
    }

    fun <ENTITY, BASIC, CONTAINER> min(propType: EntityPropertyDesc<ENTITY, BASIC, CONTAINER>): Min<ENTITY, BASIC, CONTAINER> {
        return support.min(propType)
    }

    fun <ENTITY, BASIC, CONTAINER> sum(propType: EntityPropertyDesc<ENTITY, BASIC, CONTAINER>): Sum<ENTITY, BASIC, CONTAINER> {
        return support.sum(propType)
    }
}
