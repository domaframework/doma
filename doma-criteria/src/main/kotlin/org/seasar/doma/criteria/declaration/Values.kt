package org.seasar.doma.criteria.declaration

import org.seasar.doma.def.PropertyDef

interface Values {
    operator fun <PROPERTY> set(propDef: PropertyDef<PROPERTY>, value: PROPERTY?)
}
