package org.seasar.doma.it.temp;

import org.seasar.doma.Entity;
import org.seasar.doma.Id;
import org.seasar.doma.Table;

import java.io.Serializable;

@Entity
@Table(name = "c_order_status")
public class OrderStatus implements Serializable {
  private static final long serialVersionUID = 1L;
  @Id
  String code;
  private String name;

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}
