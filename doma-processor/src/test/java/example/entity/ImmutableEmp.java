package example.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import org.seasar.doma.Entity;
import org.seasar.doma.Id;
import org.seasar.doma.Version;
import org.seasar.doma.jdbc.entity.NamingType;

@Entity(naming = NamingType.SNAKE_UPPER_CASE, immutable = true)
public class ImmutableEmp implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id final Integer id;

  final String name;

  final BigDecimal salary;

  @Version final Integer version;

  public ImmutableEmp(Integer id, String name, BigDecimal salary, Integer version) {
    super();
    this.id = id;
    this.name = name;
    this.salary = salary;
    this.version = version;
  }

  public Integer getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public BigDecimal getSalary() {
    return salary;
  }

  public Integer getVersion() {
    return version;
  }
}
