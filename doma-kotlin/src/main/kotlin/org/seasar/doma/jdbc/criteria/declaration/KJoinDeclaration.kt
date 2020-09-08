package org.seasar.doma.jdbc.criteria.declaration

import org.seasar.doma.jdbc.criteria.context.Criterion
import org.seasar.doma.jdbc.criteria.context.Criterion.Eq
import org.seasar.doma.jdbc.criteria.context.Criterion.Ge
import org.seasar.doma.jdbc.criteria.context.Criterion.Le
import org.seasar.doma.jdbc.criteria.context.Criterion.Ne
import org.seasar.doma.jdbc.criteria.context.Join
import org.seasar.doma.jdbc.criteria.context.Operand
import org.seasar.doma.jdbc.criteria.metamodel.PropertyMetamodel
import java.util.Objects

class KJoinDeclaration(private val declaration: JoinDeclaration) {

    fun <PROPERTY> eq(left: PropertyMetamodel<PROPERTY>, right: PropertyMetamodel<PROPERTY>) {
        declaration.eq(left, right)
    }

    fun <PROPERTY> ne(left: PropertyMetamodel<PROPERTY>, right: PropertyMetamodel<PROPERTY>) {
        declaration.ne(left, right)
    }

    fun <PROPERTY> ge(left: PropertyMetamodel<PROPERTY>, right: PropertyMetamodel<PROPERTY>) {
        declaration.ge(left, right)
    }

    fun <PROPERTY> gt(left: PropertyMetamodel<PROPERTY>, right: PropertyMetamodel<PROPERTY>) {
        declaration.gt(left, right)
    }

    fun <PROPERTY> le(left: PropertyMetamodel<PROPERTY>, right: PropertyMetamodel<PROPERTY>) {
        declaration.le(left, right)
    }

    fun <PROPERTY> lt(left: PropertyMetamodel<PROPERTY>, right: PropertyMetamodel<PROPERTY>) {
        declaration.lt(left, right)
    }
}