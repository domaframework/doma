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

    fun <ENTITY, BASIC, CONTAINER> count(propDesc: EntityPropertyDesc<ENTITY, BASIC, CONTAINER>): Count<ENTITY, BASIC, CONTAINER> {
        return Count(propDesc)
    }
}
