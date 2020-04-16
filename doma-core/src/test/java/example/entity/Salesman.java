package example.entity;

import java.math.BigDecimal;
import org.seasar.doma.Entity;
import org.seasar.doma.Id;
import org.seasar.doma.TenantId;
import org.seasar.doma.Version;
import org.seasar.doma.jdbc.entity.NamingType;

@Entity(naming = NamingType.SNAKE_UPPER_CASE)
public class Salesman {

  @Id Integer id;

  String name;

  BigDecimal salary;

  @TenantId String tenantId;

  @Version Integer version;

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

  public String getTenantId() {
    return tenantId;
  }

  public void setTenantId(String tenantId) {
    this.tenantId = tenantId;
  }

  public Integer getVersion() {
    return version;
  }

  public void setVersion(Integer version) {
    this.version = version;
  }
}
