package org.seasar.doma.criteria.declaration

import org.seasar.doma.def.PropertyDef

interface Row {
    operator fun <PROPERTY> get(propDef: PropertyDef<PROPERTY>): PROPERTY?
}
