package org.seasar.doma.it.criteria;

import org.seasar.doma.Entity;
import org.seasar.doma.Id;
import org.seasar.doma.Metamodel;
import org.seasar.doma.Table;

@Entity(metamodel = @Metamodel)
@Table(name = "ADDRESS")
public class Place {
  @Id private Integer addressId;
  private Avenue street;
  private Integer version;

  public Integer getAddressId() {
    return addressId;
  }

  public void setAddressId(Integer addressId) {
    this.addressId = addressId;
  }
}
