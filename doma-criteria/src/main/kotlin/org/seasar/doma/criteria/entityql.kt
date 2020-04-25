package org.seasar.doma.criteria

import org.seasar.doma.criteria.declaration.EntityqlDeclaration
import org.seasar.doma.criteria.statement.Statement

fun <RESULT> entityql(block: EntityqlDeclaration.() -> Statement<RESULT>): Statement<RESULT> {
    return EntityqlDeclaration().block()
}
