package org.seasar.doma.kotlin.jdbc.criteria

import org.seasar.doma.jdbc.Config
import org.seasar.doma.jdbc.criteria.QueryDsl
import org.seasar.doma.jdbc.criteria.context.DeleteSettings
import org.seasar.doma.jdbc.criteria.context.InsertSettings
import org.seasar.doma.jdbc.criteria.context.SelectSettings
import org.seasar.doma.jdbc.criteria.context.UpdateSettings
import org.seasar.doma.jdbc.criteria.metamodel.EntityMetamodel
import org.seasar.doma.kotlin.jdbc.criteria.statement.KSetOperand
import org.seasar.doma.kotlin.jdbc.criteria.statement.KUnifiedDeleteStarting
import org.seasar.doma.kotlin.jdbc.criteria.statement.KUnifiedInsertStarting
import org.seasar.doma.kotlin.jdbc.criteria.statement.KUnifiedSelectStarting
import org.seasar.doma.kotlin.jdbc.criteria.statement.KUnifiedUpdateStarting

class KQueryDsl(config: Config) {

    private val dsl = QueryDsl(config)

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

    fun <ENTITY : Any> update(
        entityMetamodel: EntityMetamodel<ENTITY>,
        block: UpdateSettings.() -> Unit = {},
    ): KUnifiedUpdateStarting<ENTITY> {
        val statement = dsl.update(entityMetamodel, block)
        return KUnifiedUpdateStarting(statement)
    }

    fun <ENTITY : Any> delete(
        entityMetamodel: EntityMetamodel<ENTITY>,
        block: DeleteSettings.() -> Unit = {},
    ): KUnifiedDeleteStarting<ENTITY> {
        val statement = dsl.delete(entityMetamodel, block)
        return KUnifiedDeleteStarting(statement)
    }

    fun <ENTITY : Any> insert(
        entityMetamodel: EntityMetamodel<ENTITY>,
        block: InsertSettings.() -> Unit = {},
    ): KUnifiedInsertStarting<ENTITY> {
        val statement = dsl.insert(entityMetamodel, block)
        return KUnifiedInsertStarting(statement)
    }
}
