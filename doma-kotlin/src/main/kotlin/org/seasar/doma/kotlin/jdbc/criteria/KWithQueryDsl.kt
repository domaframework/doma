package org.seasar.doma.kotlin.jdbc.criteria

import org.seasar.doma.jdbc.criteria.WithQueryDsl
import org.seasar.doma.jdbc.criteria.context.SelectSettings
import org.seasar.doma.jdbc.criteria.context.WithContext
import org.seasar.doma.jdbc.criteria.metamodel.EntityMetamodel
import org.seasar.doma.kotlin.jdbc.criteria.statement.KSetOperand
import org.seasar.doma.kotlin.jdbc.criteria.statement.KUnifiedSelectStarting

class KWithQueryDsl(private val dsl: WithQueryDsl) {

    fun <ENTITY : Any> from(
        entityMetamodel: EntityMetamodel<ENTITY>,
        block: SelectSettings.() -> Unit = {},
    ): KUnifiedSelectStarting<ENTITY> {
        val statement = dsl.from(entityMetamodel, block)
        return KUnifiedSelectStarting(statement)
    }

    fun <ENTITY : Any> from(
        entityMetamodel: EntityMetamodel<ENTITY>,
        setOperandForSubQuery: KSetOperand<*>,
        block: SelectSettings.() -> Unit = {},
    ): KUnifiedSelectStarting<ENTITY> {
        val statement = dsl.from(entityMetamodel, setOperandForSubQuery.asSetOperand(), block)
        return KUnifiedSelectStarting(statement)
    }

    fun with(
        vararg pairs: Pair<EntityMetamodel<*>, KSetOperand<*>>,
    ): KWithQueryDsl {
        val withContexts = pairs.map { WithContext(it.first, it.second.asSetOperand().context) }
        return KWithQueryDsl(dsl.with(withContexts))
    }
}
