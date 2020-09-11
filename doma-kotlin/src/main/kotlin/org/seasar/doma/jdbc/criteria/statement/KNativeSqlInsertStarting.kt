package org.seasar.doma.jdbc.criteria.statement

import org.seasar.doma.jdbc.criteria.context.SubSelectContext
import org.seasar.doma.jdbc.criteria.declaration.KInsertSelectDeclaration
import org.seasar.doma.jdbc.criteria.declaration.KValuesDeclaration

class KNativeSqlInsertStarting(private val statement: NativeSqlInsertStarting) {

    fun values(block: KValuesDeclaration.() -> Unit): KNativeSqlInsertTerminal {
        val terminal = statement.values { block(KValuesDeclaration(it)) }
        return KNativeSqlInsertTerminal(terminal)
    }

    fun select(block: KInsertSelectDeclaration.() -> SubSelectContext<*>): KNativeSqlInsertTerminal {
        val terminal = statement.select { block(KInsertSelectDeclaration()) }
        return KNativeSqlInsertTerminal(terminal)
    }
}
