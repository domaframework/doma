package org.seasar.doma.criteria.declaration

import org.seasar.doma.jdbc.entity.EntityPropertyType

data class SqlSelectResult<RESULT>(
    val propTypes: List<EntityPropertyType<*, *>>,
    val mapper: (Row) -> RESULT
)
