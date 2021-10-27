package org.seasar.doma.it.criteria;

import org.seasar.doma.Entity;
import org.seasar.doma.Id;
import org.seasar.doma.Metamodel;
import org.seasar.doma.Table;
import org.seasar.doma.Version;

@Entity(metamodel = @Metamodel)
@Table(name = "DEPARTMENT")
public class Division {

  @Id public Integer departmentId;

  public Integer departmentNo;

  public String departmentName;

  public Names location;

  @Version public Integer version;
}
