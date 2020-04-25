package org.seasar.doma.criteria

import org.seasar.doma.jdbc.Config
import org.seasar.doma.jdbc.entity.EntityPropertyDesc

@Declaration
class ValuesDeclaration(private val config: Config, private val add: (Pair<Operand.Prop, Operand.Param>) -> Unit) {

    private val support = DeclarationSupport(config)

    fun <CONTAINER> value(left: EntityPropertyDesc<*, *, CONTAINER>, right: CONTAINER?) {
        add(support.toProp(left) to support.toParam(left, right))
    }
}
