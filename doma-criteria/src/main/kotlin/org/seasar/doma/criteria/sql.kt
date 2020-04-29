package org.seasar.doma.criteria

import org.seasar.doma.criteria.declaration.SqlDeclaration
import org.seasar.doma.criteria.statement.Statement

fun <RESULT> sql(block: SqlDeclaration.() -> Statement<RESULT>): Statement<RESULT> {
    return SqlDeclaration().block()
}
