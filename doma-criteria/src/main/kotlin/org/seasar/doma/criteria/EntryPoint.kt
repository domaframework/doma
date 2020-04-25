package org.seasar.doma.criteria

fun <RESULT> entityql(block: EntityqlDeclaration.() -> Statement<RESULT>): Statement<RESULT> {
    return EntityqlDeclaration().block()
}

fun <RESULT> sql(block: SqlDeclaration.() -> Statement<RESULT>): Statement<RESULT> {
    return SqlDeclaration().block()
}
