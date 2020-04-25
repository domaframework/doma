package org.seasar.doma.criteria.declaration

import org.seasar.doma.jdbc.entity.EntityPropertyDesc

interface Values {
    operator fun <CONTAINER> set(propType: EntityPropertyDesc<*, *, CONTAINER>, value: CONTAINER?)
}
