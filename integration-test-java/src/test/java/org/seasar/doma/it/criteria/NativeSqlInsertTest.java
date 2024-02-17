package org.seasar.doma.it.criteria;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Arrays;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.seasar.doma.DomaIllegalArgumentException;
import org.seasar.doma.it.Dbms;
import org.seasar.doma.it.IntegrationTestEnvironment;
import org.seasar.doma.it.Run;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.SqlLogType;
import org.seasar.doma.jdbc.criteria.NativeSql;
import org.seasar.doma.jdbc.dialect.Dialect;

@ExtendWith(IntegrationTestEnvironment.class)
public class NativeSqlInsertTest {

  private final NativeSql nativeSql;
  private final Dialect dialect;

  public NativeSqlInsertTest(Config config) {
    this.nativeSql = new NativeSql(config);
    this.dialect = config.getDialect();
  }

  @Test
  void settings() {
    Department_ d = new Department_();

    int count =
        nativeSql
            .insert(
                d,
                settings -> {
                  settings.setComment("insert department");
                  settings.setQueryTimeout(1000);
                  settings.setSqlLogType(SqlLogType.RAW);
                  settings.setBatchSize(20);
                })
            .values(
                c -> {
                  c.value(d.departmentId, 99);
                  c.value(d.departmentNo, 99);
                  c.value(d.departmentName, "aaa");
                  c.value(d.location, "bbb");
                  c.value(d.version, 1);
                })
            .execute();

    assertEquals(1, count);
  }

  @Test
  void insert() {
    Department_ d = new Department_();

    int count =
        nativeSql
            .insert(d)
            .values(
                c -> {
                  c.value(d.departmentId, 99);
                  c.value(d.departmentNo, 99);
                  c.value(d.departmentName, "aaa");
                  c.value(d.location, "bbb");
                  c.value(d.version, 1);
                })
            .execute();

    assertEquals(1, count);
  }

  @Test
  @Run(onlyIf = {Dbms.MYSQL, Dbms.POSTGRESQL}) // TODO: Implement it to work in other dialects
  void insert_onDuplicateKeyUpdate_duplicate() {
    Department_ d = new Department_();

    int count =
        nativeSql
            .insert(d)
            .values(
                c -> {
                  c.value(d.departmentId, 1);
                  c.value(d.departmentNo, 60);
                  c.value(d.departmentName, "DEVELOPMENT");
                  c.value(d.location, "KYOTO");
                  c.value(d.version, 2);
                })
            .onDuplicateKeyUpdate()
            .keys(d.departmentId)
            .set(
                c -> {
                  c.value(d.departmentName, c.excluded(d.departmentName));
                  c.value(d.location, "KYOTO");
                  c.value(d.version, 3);
                })
            .execute();

    if (dialect.getName().equals("mysql") || dialect.getName().equals("mariadb")) {
      assertEquals(2, count);
    } else {
      assertEquals(1, count);
    }

    Department resultDepartment = nativeSql.from(d).where(c -> c.eq(d.departmentId, 1)).fetchOne();
    // updated
    assertEquals(10, resultDepartment.getDepartmentNo()); // not updated
    assertEquals("DEVELOPMENT", resultDepartment.getDepartmentName());
    assertEquals("KYOTO", resultDepartment.getLocation());
    assertEquals(3, resultDepartment.getVersion());
  }

  @Test
  @Run(onlyIf = {Dbms.MYSQL, Dbms.POSTGRESQL}) // TODO: Implement it to work in other dialects
  void insert_onDuplicateKeyUpdate_nonDuplicate() {
    Department_ d = new Department_();

    int count =
        nativeSql
            .insert(d)
            .values(
                c -> {
                  c.value(d.departmentId, 5);
                  c.value(d.departmentNo, 50);
                  c.value(d.departmentName, "PLANNING");
                  c.value(d.location, "TOKYO");
                  c.value(d.version, 2);
                })
            .onDuplicateKeyUpdate()
            .keys(d.departmentId)
            .set(
                c -> {
                  c.value(d.departmentName, c.excluded(d.departmentName));
                  c.value(d.location, "TOKYO");
                  c.value(d.version, 2);
                })
            .execute();

    assertEquals(1, count);

    Department resultDepartment = nativeSql.from(d).where(c -> c.eq(d.departmentId, 5)).fetchOne();
    // inserted
    assertEquals(50, resultDepartment.getDepartmentNo());
    assertEquals("PLANNING", resultDepartment.getDepartmentName());
    assertEquals("TOKYO", resultDepartment.getLocation());
    assertEquals(2, resultDepartment.getVersion());
  }

  @Test
  void insert_onDuplicateKeyUpdate_emptyKeyException() {
    Department_ d = new Department_();

    DomaIllegalArgumentException ex =
        assertThrows(
            DomaIllegalArgumentException.class,
            () -> {
              nativeSql
                  .insert(d)
                  .values(
                      c -> {
                        c.value(d.departmentId, 5);
                        c.value(d.departmentNo, 50);
                        c.value(d.departmentName, "PLANNING");
                        c.value(d.location, "TOKYO");
                        c.value(d.version, 2);
                      })
                  .onDuplicateKeyUpdate()
                  .keys()
                  .set(
                      c -> {
                        c.value(d.departmentName, c.excluded(d.departmentName));
                        c.value(d.location, "TOKYO");
                        c.value(d.version, 2);
                      })
                  .execute();
            });
    System.out.println(ex.getMessage());
  }

  @Test
  void insert_onDuplicateKeyUpdate_emptySetValueException() {
    Department_ d = new Department_();

    DomaIllegalArgumentException ex =
        assertThrows(
            DomaIllegalArgumentException.class,
            () -> {
              nativeSql
                  .insert(d)
                  .values(
                      c -> {
                        c.value(d.departmentId, 5);
                        c.value(d.departmentNo, 50);
                        c.value(d.departmentName, "PLANNING");
                        c.value(d.location, "TOKYO");
                        c.value(d.version, 2);
                      })
                  .onDuplicateKeyUpdate()
                  .keys(d.departmentId)
                  .set(
                      c -> {
                        // nothing
                      })
                  .execute();
            });
    System.out.println(ex.getMessage());
  }

  @Test
  @Run(onlyIf = {Dbms.MYSQL, Dbms.POSTGRESQL}) // TODO: Implement it to work in other dialects
  void insert_onDuplicateKeyIgnore_duplicate() {
    Department_ d = new Department_();

    int count =
        nativeSql
            .insert(d)
            .values(
                c -> {
                  c.value(d.departmentId, 1);
                  c.value(d.departmentNo, 60);
                  c.value(d.departmentName, "DEVELOPMENT");
                  c.value(d.location, "KYOTO");
                  c.value(d.version, 2);
                })
            .onDuplicateKeyIgnore()
            .keys(d.departmentId)
            .execute();

    assertEquals(0, count);

    Department resultDepartment = nativeSql.from(d).where(c -> c.eq(d.departmentId, 1)).fetchOne();
    // ignored
    assertEquals(10, resultDepartment.getDepartmentNo());
    assertEquals("ACCOUNTING", resultDepartment.getDepartmentName());
    assertEquals("NEW YORK", resultDepartment.getLocation());
    assertEquals(1, resultDepartment.getVersion());
  }

  @Test
  @Run(onlyIf = {Dbms.MYSQL, Dbms.POSTGRESQL}) // TODO: Implement it to work in other dialects
  void insert_onDuplicateKeyIgnore_nonDuplicate() {
    Department_ d = new Department_();

    int count =
        nativeSql
            .insert(d)
            .values(
                c -> {
                  c.value(d.departmentId, 5);
                  c.value(d.departmentNo, 50);
                  c.value(d.departmentName, "PLANNING");
                  c.value(d.location, "TOKYO");
                  c.value(d.version, 2);
                })
            .onDuplicateKeyIgnore()
            .keys(d.departmentId)
            .execute();

    assertEquals(1, count);

    Department resultDepartment = nativeSql.from(d).where(c -> c.eq(d.departmentId, 5)).fetchOne();
    // inserted
    assertEquals(50, resultDepartment.getDepartmentNo());
    assertEquals("PLANNING", resultDepartment.getDepartmentName());
    assertEquals("TOKYO", resultDepartment.getLocation());
    assertEquals(2, resultDepartment.getVersion());
  }

  @Test
  void insert_onDuplicateKeyIgnore_emptyKeyException() {
    Department_ d = new Department_();

    DomaIllegalArgumentException ex =
        assertThrows(
            DomaIllegalArgumentException.class,
            () -> {
              nativeSql
                  .insert(d)
                  .values(
                      c -> {
                        c.value(d.departmentId, 5);
                        c.value(d.departmentNo, 50);
                        c.value(d.departmentName, "PLANNING");
                        c.value(d.location, "TOKYO");
                        c.value(d.version, 2);
                      })
                  .onDuplicateKeyIgnore()
                  .keys()
                  .execute();
            });
    System.out.println(ex.getMessage());
  }

  @Test
  void insert_select_entity() {
    DepartmentArchive_ da = new DepartmentArchive_();
    Department_ d = new Department_();

    int count =
        nativeSql
            .insert(da)
            .select(c -> c.from(d).where(cc -> cc.in(d.departmentId, Arrays.asList(1, 2))))
            .execute();

    assertEquals(2, count);
  }

  @Test
  void insert_select_properties() {
    DepartmentArchive_ da = new DepartmentArchive_();
    Department_ d = new Department_();

    int count =
        nativeSql
            .insert(da)
            .select(
                c ->
                    c.from(d)
                        .where(cc -> cc.in(d.departmentId, Arrays.asList(1, 2)))
                        .select(
                            d.departmentId,
                            d.departmentNo,
                            d.departmentName,
                            d.location,
                            d.version))
            .execute();

    assertEquals(2, count);
  }

  @Test
  void insert_select_using_same_entityMetamodel() {
    Department_ da = new Department_("DEPARTMENT_ARCHIVE");
    Department_ d = new Department_();

    int count = nativeSql.insert(da).select(c -> c.from(d)).execute();

    assertEquals(4, count);
  }
}
