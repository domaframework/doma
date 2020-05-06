package example;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.criteria.NativeSql;
import org.seasar.doma.jdbc.criteria.statement.Statement;

@ExtendWith(Env.class)
public class NativeSqlInsertTest {

  private final NativeSql nativeSql;

  public NativeSqlInsertTest(Config config) {
    this.nativeSql = new NativeSql(config);
  }

  @Test
  void insert() {
    Department_ d = new Department_();

    Statement<Integer> stmt =
        nativeSql
            .insert(d)
            .values(
                c -> {
                  c.value(d.departmentId, 99);
                  c.value(d.departmentNo, 99);
                  c.value(d.departmentName, "aaa");
                  c.value(d.location, "bbb");
                  c.value(d.version, 1);
                });

    int count = stmt.execute();
    assertEquals(1, count);

    System.out.println(stmt.asSql());
  }
}
