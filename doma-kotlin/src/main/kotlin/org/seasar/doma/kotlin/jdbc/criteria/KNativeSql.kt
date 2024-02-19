package org.seasar.doma.kotlin.jdbc.criteria

import org.seasar.doma.jdbc.Config
import org.seasar.doma.jdbc.criteria.context.DeleteSettings
import org.seasar.doma.jdbc.criteria.context.InsertSettings
import org.seasar.doma.jdbc.criteria.context.SelectSettings
import org.seasar.doma.jdbc.criteria.context.UpdateSettings
import org.seasar.doma.jdbc.criteria.metamodel.EntityMetamodel
import org.seasar.doma.kotlin.jdbc.criteria.statement.KNativeSqlDeleteStarting
import org.seasar.doma.kotlin.jdbc.criteria.statement.KNativeSqlInsertStarting
import org.seasar.doma.kotlin.jdbc.criteria.statement.KNativeSqlSelectStarting
import org.seasar.doma.kotlin.jdbc.criteria.statement.KNativeSqlUpdateStarting
import org.seasar.doma.kotlin.jdbc.criteria.statement.KSetOperand

class KNativeSql(config: Config?) {

    private val nativeSql = org.seasar.doma.jdbc.criteria.NativeSql(config)

    fun <ENTITY> from(
        entityMetamodel: EntityMetamodel<ENTITY>,
        block: SelectSettings.() -> Unit = {},
    ): KNativeSqlSelectStarting<ENTITY> {
        val statement = nativeSql.from(entityMetamodel, block)
        return KNativeSqlSelectStarting(statement)
    }

    fun <ENTITY> from(
        entityMetamodel: EntityMetamodel<ENTITY>,
        setOperandForSubQuery: KSetOperand<*>,
        block: SelectSettings.() -> Unit = {},
    ): KNativeSqlSelectStarting<ENTITY> {
        val statement = nativeSql.from(entityMetamodel, setOperandForSubQuery.asSetOperand(), block)
        return KNativeSqlSelectStarting(statement)
    }

    fun <ENTITY> update(
        entityMetamodel: EntityMetamodel<ENTITY>,
        block: UpdateSettings.() -> Unit = {},
    ): KNativeSqlUpdateStarting {
        val statement = nativeSql.update(entityMetamodel, block)
        return KNativeSqlUpdateStarting(statement)
    }

    fun <ENTITY> delete(
        entityMetamodel: EntityMetamodel<ENTITY>,
        block: DeleteSettings.() -> Unit = {},
    ): KNativeSqlDeleteStarting {
        val statement = nativeSql.delete(entityMetamodel, block)
        return KNativeSqlDeleteStarting(statement)
    }

    fun <ENTITY> insert(
        entityMetamodel: EntityMetamodel<ENTITY>,
        block: InsertSettings.() -> Unit = {},
    ): KNativeSqlInsertStarting {
        val statement = nativeSql.insert(entityMetamodel, block)
        return KNativeSqlInsertStarting(statement)
    }
}
