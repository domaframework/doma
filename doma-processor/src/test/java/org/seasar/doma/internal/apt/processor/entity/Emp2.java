package org.seasar.doma.internal.apt.processor.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import org.seasar.doma.Entity;
import org.seasar.doma.Id;
import org.seasar.doma.OriginalStates;
import org.seasar.doma.Table;
import org.seasar.doma.Version;
import org.seasar.doma.jdbc.entity.NamingType;

@Entity(naming = NamingType.SNAKE_UPPER_CASE)
@Table(catalog = "CATA", quote = true)
public class Emp2 implements Serializable {

  private static final long serialVersionUID = -6511179565163144602L;

  @Id Integer id;

  String name;

  BigDecimal salary;

  @Version Integer version;

  @OriginalStates public Emp2 originalStates;

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public BigDecimal getSalary() {
    return salary;
  }

  public void setSalary(BigDecimal salary) {
    this.salary = salary;
  }

  public Integer getVersion() {
    return version;
  }

  public void setVersion(Integer version) {
    this.version = version;
  }
}
