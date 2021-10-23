package org.seasar.doma.kotlin.jdbc.criteria.statement

import org.seasar.doma.jdbc.criteria.statement.NativeSqlUpdateStarting
import org.seasar.doma.kotlin.jdbc.criteria.declaration.KSetDeclaration

class KNativeSqlUpdateStarting(private val statement: NativeSqlUpdateStarting) {

    fun set(block: KSetDeclaration.() -> Unit): KNativeSqlUpdateTerminal {
        val terminal = statement.set { block(KSetDeclaration(it)) }
        return KNativeSqlUpdateTerminal(terminal)
    }
}
