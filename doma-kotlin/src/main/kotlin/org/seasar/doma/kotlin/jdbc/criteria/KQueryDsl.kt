package org.seasar.doma.kotlin.jdbc.criteria

import org.seasar.doma.jdbc.Config
import org.seasar.doma.jdbc.criteria.QueryDsl
import org.seasar.doma.jdbc.criteria.context.DeleteSettings
import org.seasar.doma.jdbc.criteria.context.InsertSettings
import org.seasar.doma.jdbc.criteria.context.SelectSettings
import org.seasar.doma.jdbc.criteria.context.UpdateSettings
import org.seasar.doma.jdbc.criteria.metamodel.EntityMetamodel
import org.seasar.doma.kotlin.jdbc.criteria.statement.KSetOperand
import org.seasar.doma.kotlin.jdbc.criteria.statement.KUnifiedDeleteStating
import org.seasar.doma.kotlin.jdbc.criteria.statement.KUnifiedInsertStating
import org.seasar.doma.kotlin.jdbc.criteria.statement.KUnifiedSelectStating
import org.seasar.doma.kotlin.jdbc.criteria.statement.KUnifiedUpdateStating

class KQueryDsl(config: Config) {

    private val dsl = QueryDsl(config)

    fun <ENTITY : Any> from(
        entityMetamodel: EntityMetamodel<ENTITY>,
        block: SelectSettings.() -> Unit = {},
    ): KUnifiedSelectStating<ENTITY> {
        val statement = dsl.from(entityMetamodel, block)
        return KUnifiedSelectStating(statement)
    }

    fun <ENTITY : Any> from(
        entityMetamodel: EntityMetamodel<ENTITY>,
        setOperandForSubQuery: KSetOperand<*>,
        block: SelectSettings.() -> Unit = {},
    ): KUnifiedSelectStating<ENTITY> {
        val statement = dsl.from(entityMetamodel, setOperandForSubQuery.asSetOperand(), block)
        return KUnifiedSelectStating(statement)
    }

    fun <ENTITY : Any> update(
        entityMetamodel: EntityMetamodel<ENTITY>,
        block: UpdateSettings.() -> Unit = {},
    ): KUnifiedUpdateStating<ENTITY> {
        val statement = dsl.update(entityMetamodel, block)
        return KUnifiedUpdateStating(statement)
    }

    fun <ENTITY : Any> delete(
        entityMetamodel: EntityMetamodel<ENTITY>,
        block: DeleteSettings.() -> Unit = {},
    ): KUnifiedDeleteStating<ENTITY> {
        val statement = dsl.delete(entityMetamodel, block)
        return KUnifiedDeleteStating(statement)
    }

    fun <ENTITY : Any> insert(
        entityMetamodel: EntityMetamodel<ENTITY>,
        block: InsertSettings.() -> Unit = {},
    ): KUnifiedInsertStating<ENTITY> {
        val statement = dsl.insert(entityMetamodel, block)
        return KUnifiedInsertStating(statement)
    }
}
