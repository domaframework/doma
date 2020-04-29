package example.doc;

import java.util.ArrayList;
import java.util.List;
import org.seasar.doma.Entity;
import org.seasar.doma.Id;
import org.seasar.doma.Transient;
import org.seasar.doma.Version;

@Entity
public class Dept {
  @Id public Integer departmentId;
  public String departmentName;
  @Version public Integer version;
  @Transient public List<Emp> employeeList = new ArrayList<>();
}
