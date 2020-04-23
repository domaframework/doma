package org.seasar.doma.criteria

import org.seasar.doma.jdbc.entity.EntityPropertyDesc

interface Row {
    operator fun <CONTAINER> get(propType: EntityPropertyDesc<*, *, CONTAINER>): CONTAINER?
}
