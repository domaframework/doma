package example;

import org.seasar.doma.Entity;
import org.seasar.doma.Id;

@Entity
public class Address {
  @Id private Integer addressId;
  private String street;
  private Integer version;

  public Integer getAddressId() {
    return addressId;
  }

  public void setAddressId(Integer addressId) {
    this.addressId = addressId;
  }

  public String getStreet() {
    return street;
  }

  public void setStreet(String street) {
    this.street = street;
  }

  public Integer getVersion() {
    return version;
  }

  public void setVersion(Integer version) {
    this.version = version;
  }
}
