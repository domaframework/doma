package org.seasar.doma.kotlin.jdbc.criteria.statement

import org.seasar.doma.jdbc.BatchResult
import org.seasar.doma.jdbc.Result
import org.seasar.doma.jdbc.criteria.statement.UnifiedUpdateStarting
import org.seasar.doma.kotlin.jdbc.criteria.declaration.KSetDeclaration

class KUnifiedUpdateStating<ENTITY : Any>(private val statement: UnifiedUpdateStarting<ENTITY>) {

    fun single(entity: ENTITY): KStatement<Result<ENTITY>> {
        return KEntityqlUpdateStatement(statement.single(entity))
    }

    fun batch(entities: List<ENTITY>): KStatement<BatchResult<ENTITY>> {
        return KEntityqlBatchUpdateStatement(statement.batch(entities))
    }

    fun set(block: KSetDeclaration.() -> Unit): KNativeSqlUpdateTerminal {
        val terminal = statement.set { block(KSetDeclaration(it)) }
        return KNativeSqlUpdateTerminal(terminal)
    }
}
