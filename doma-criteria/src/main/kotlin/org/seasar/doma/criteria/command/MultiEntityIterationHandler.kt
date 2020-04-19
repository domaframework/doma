package org.seasar.doma.criteria.command

import org.seasar.doma.internal.jdbc.command.AbstractIterationHandler
import org.seasar.doma.internal.jdbc.command.ResultListCallback
import org.seasar.doma.jdbc.ObjectProvider
import org.seasar.doma.jdbc.entity.EntityType
import org.seasar.doma.jdbc.query.SelectQuery

class MultiEntityIterationHandler(private val entityTypes: List<EntityType<Any>>) :
    AbstractIterationHandler<MultiEntity, List<MultiEntity>>(ResultListCallback()) {

    override fun createObjectProvider(query: SelectQuery): ObjectProvider<MultiEntity> {
        return MultiEntityProvider(entityTypes, query)
    }
}
