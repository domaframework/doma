package org.seasar.doma.it.criteria;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.seasar.doma.it.IntegrationTestEnvironment;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.criteria.Entityql;
import org.seasar.doma.jdbc.criteria.NativeSql;

@ExtendWith(IntegrationTestEnvironment.class)
public class DifferenceBetweenTwoDsls {

  private final Entityql entityql;
  private final NativeSql nativeSql;

  public DifferenceBetweenTwoDsls(Config config) {
    this.entityql = new Entityql(config);
    this.nativeSql = new NativeSql(config);
  }

  @Test
  void compare() {
    Department_ d = new Department_();
    Employee_ e = new Employee_();

    // (1) Use Entityql DSL
    List<Department> list1 =
        entityql.from(d).innerJoin(e, on -> on.eq(d.departmentId, e.departmentId)).fetch();

    // (2) Use NativeSql DSL
    List<Department> list2 =
        nativeSql.from(d).innerJoin(e, on -> on.eq(d.departmentId, e.departmentId)).fetch();

    System.out.println(list1.size()); //  3
    System.out.println(list2.size()); // 14
  }
}
