package org.seasar.doma.criteria.declaration

import org.seasar.doma.criteria.context.SelectContext
import org.seasar.doma.def.PropertyDef

data class SqlSelectResult<RESULT_ELEMENT>(
    val context: SelectContext,
    val propDefs: List<PropertyDef<*>>,
    val mapper: (Row) -> RESULT_ELEMENT
)
