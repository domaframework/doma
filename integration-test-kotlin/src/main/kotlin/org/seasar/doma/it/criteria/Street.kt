package org.seasar.doma.it.criteria

import org.seasar.doma.Domain

@Domain(valueType = String::class)
class Street(val value: String) : CharSequence {

    override val length: Int
        get() = value.length

    override fun get(index: Int): Char {
        return value[index]
    }

    override fun subSequence(startIndex: Int, endIndex: Int): CharSequence {
        return value.subSequence(startIndex, endIndex)
    }
}
