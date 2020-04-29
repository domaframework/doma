package org.seasar.doma.criteria.statement

sealed class SetOperator<CONTENT> {
    data class Select<CONTENT>(val content: CONTENT) : SetOperator<CONTENT>()
    data class Union<CONTENT>(val all: Boolean, val left: SetOperator<CONTENT>, val right: SetOperator<CONTENT>) : SetOperator<CONTENT>()

    fun <TO> map(mapper: (CONTENT) -> TO): SetOperator<TO> {
        fun visit(o: SetOperator<CONTENT>): SetOperator<TO> {
            return when (o) {
                is Select -> {
                    Select(mapper(o.content))
                }
                is Union -> {
                    Union(o.all, visit(o.left), visit(o.right))
                }
            }
        }
        return visit(this)
    }

    fun toList(): List<CONTENT> {
        val results = mutableListOf<CONTENT>()
        fun visit(o: SetOperator<CONTENT>) {
            when (o) {
                is Select -> {
                    results.add(o.content)
                }
                is Union -> {
                    visit(o.left)
                    visit(o.right)
                }
            }
        }
        visit(this)
        return results
    }
}
