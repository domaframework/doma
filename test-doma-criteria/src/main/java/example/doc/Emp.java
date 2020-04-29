package example.doc;

import org.seasar.doma.Entity;
import org.seasar.doma.Id;
import org.seasar.doma.Transient;
import org.seasar.doma.Version;

@Entity
public class Emp {
  @Id public Integer employeeId;
  public String employeeName;
  public Integer managerId;
  @Transient public Emp manager;
  public Integer departmentId;
  @Transient public Dept department;
  @Version public Integer version;
}
