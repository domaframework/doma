package org.seasar.doma.criteria.declaration

import org.seasar.doma.criteria.context.SelectContext
import org.seasar.doma.jdbc.entity.EntityPropertyType

data class SqlSelectResult<RESULT_ELEMENT>(
    val context: SelectContext,
    val propTypes: List<EntityPropertyType<*, *>>,
    val mapper: (Row) -> RESULT_ELEMENT
)
