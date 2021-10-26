package org.seasar.doma.it.criteria

import org.seasar.doma.Entity
import org.seasar.doma.Id
import org.seasar.doma.Metamodel

@Entity(metamodel = Metamodel())
class Address {
    @Id
    var addressId: Int? = null
    var street: Street? = null
    var version: Int? = null
}
