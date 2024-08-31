package org.seasar.doma.it.entity;

import org.seasar.doma.Column;
import org.seasar.doma.Entity;
import org.seasar.doma.GeneratedValue;
import org.seasar.doma.GenerationType;
import org.seasar.doma.Id;
import org.seasar.doma.Table;

@Entity
@Table(name = "IDENTITY_STRATEGY2")
public class IdentityStrategy2 {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  Integer id;

  String uniqueValue;

  @Column(quote = true)
  String value;

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getUniqueValue() {
    return uniqueValue;
  }

  public void setUniqueValue(String uniqueValue) {
    this.uniqueValue = uniqueValue;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }
}
