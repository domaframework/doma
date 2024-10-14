package org.seasar.doma.kotlin.jdbc.criteria.statement

import org.seasar.doma.jdbc.BatchResult
import org.seasar.doma.jdbc.Result
import org.seasar.doma.jdbc.criteria.statement.UnifiedDeleteStarting
import org.seasar.doma.kotlin.jdbc.criteria.declaration.KWhereDeclaration

class KUnifiedDeleteStarting<ENTITY : Any>(private val statement: UnifiedDeleteStarting<ENTITY>) {

    fun single(entity: ENTITY): KStatement<Result<ENTITY>> {
        return KEntityqlDeleteStatement(statement.single(entity))
    }

    fun batch(entities: List<ENTITY>): KStatement<BatchResult<ENTITY>> {
        return KEntityqlBatchDeleteStatement(statement.batch(entities))
    }

    fun where(block: KWhereDeclaration.() -> Unit): KStatement<Int> {
        val whereStatement = statement.where { block(KWhereDeclaration(it)) }
        return object : KStatement<Int> {
            override fun execute(): Int {
                return whereStatement.execute()
            }

            override fun asSql(): org.seasar.doma.jdbc.Sql<*> {
                return whereStatement.asSql()
            }
        }
    }

    fun all(): KStatement<Int> {
        val deleteAll = statement.all()
        return object : KStatement<Int> {
            override fun execute(): Int {
                return deleteAll.execute()
            }

            override fun asSql(): org.seasar.doma.jdbc.Sql<*> {
                return deleteAll.asSql()
            }
        }
    }
}
