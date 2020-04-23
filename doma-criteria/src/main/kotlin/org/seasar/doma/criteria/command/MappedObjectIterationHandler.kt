package org.seasar.doma.criteria.command

import org.seasar.doma.criteria.Row
import org.seasar.doma.internal.jdbc.command.AbstractIterationHandler
import org.seasar.doma.internal.jdbc.command.ResultListCallback
import org.seasar.doma.jdbc.ObjectProvider
import org.seasar.doma.jdbc.entity.EntityPropertyDesc
import org.seasar.doma.jdbc.query.SelectQuery

class MappedObjectIterationHandler<RESULT>(
    private val propTypes: List<EntityPropertyDesc<*, *, *>>,
    private val mapper: (Row) -> RESULT
) :
        AbstractIterationHandler<RESULT, List<RESULT>>(ResultListCallback()) {

    override fun createObjectProvider(query: SelectQuery): ObjectProvider<RESULT> {
        return MappedObjectProvider<RESULT>(query, propTypes, mapper)
    }
}
