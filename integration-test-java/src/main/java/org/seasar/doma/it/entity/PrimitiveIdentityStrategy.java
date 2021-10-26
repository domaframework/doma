package org.seasar.doma.it.entity;

import org.seasar.doma.Entity;
import org.seasar.doma.GeneratedValue;
import org.seasar.doma.GenerationType;
import org.seasar.doma.Id;
import org.seasar.doma.OriginalStates;
import org.seasar.doma.Table;

@Entity
@Table(name = "IDENTITY_STRATEGY")
public class PrimitiveIdentityStrategy {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  int id = -1;

  Integer value;

  @OriginalStates PrimitiveIdentityStrategy originalStates;

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public Integer getValue() {
    return value;
  }

  public void setValue(Integer value) {
    this.value = value;
  }
}
