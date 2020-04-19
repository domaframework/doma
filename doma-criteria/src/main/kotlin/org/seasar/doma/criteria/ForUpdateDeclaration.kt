package org.seasar.doma.criteria

@Declaration
class ForUpdateDeclaration(private val update: (ForUpdate) -> Unit) {

    fun nowait(nowait: Boolean = true) {
        update(ForUpdate(nowait))
    }
}
