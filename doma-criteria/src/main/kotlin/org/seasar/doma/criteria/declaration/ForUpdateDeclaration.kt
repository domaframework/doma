package org.seasar.doma.criteria.declaration

import org.seasar.doma.criteria.context.ForUpdate

@Declaration
class ForUpdateDeclaration(private val update: (ForUpdate) -> Unit) {

    fun nowait(nowait: Boolean = true) {
        update(ForUpdate(nowait))
    }
}
