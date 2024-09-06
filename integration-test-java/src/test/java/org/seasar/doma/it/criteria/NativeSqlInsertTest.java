package org.seasar.doma.it.criteria;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
  void insert_onDuplicateKeyUpdate_unsetKey() {
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
  void insert_onDuplicateKeyUpdate_unsetSetValue() {
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
            .execute();

    if (dialect.getName().equals("mysql") || dialect.getName().equals("mariadb")) {
      assertEquals(2, count);
    } else {
      assertEquals(1, count);
    }

    Department resultDepartment = nativeSql.from(d).where(c -> c.eq(d.departmentId, 1)).fetchOne();
    // updated
    assertEquals(60, resultDepartment.getDepartmentNo());
    assertEquals("DEVELOPMENT", resultDepartment.getDepartmentName());
    assertEquals("KYOTO", resultDepartment.getLocation());
    assertEquals(2, resultDepartment.getVersion());
  }

  @Test
  void insert_onDuplicateKeyUpdate_unsetKey_unsetSetValue() {
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
            .execute();

    if (dialect.getName().equals("mysql") || dialect.getName().equals("mariadb")) {
      assertEquals(2, count);
    } else {
      assertEquals(1, count);
    }

    Department resultDepartment = nativeSql.from(d).where(c -> c.eq(d.departmentId, 1)).fetchOne();
    // updated
    assertEquals(60, resultDepartment.getDepartmentNo());
    assertEquals("DEVELOPMENT", resultDepartment.getDepartmentName());
    assertEquals("KYOTO", resultDepartment.getLocation());
    assertEquals(2, resultDepartment.getVersion());
  }

  @Test
  void insert_onDuplicateKeyUpdate_compositeKey() {
    CompKeyDepartment_ d = new CompKeyDepartment_();

    int count =
        nativeSql
            .insert(d)
            .values(
                c -> {
                  c.value(d.departmentId1, 1);
                  c.value(d.departmentId2, 1);
                  c.value(d.departmentNo, 60);
                  c.value(d.departmentName, "DEVELOPMENT");
                  c.value(d.location, "KYOTO");
                  c.value(d.version, 2);
                })
            .onDuplicateKeyUpdate()
            .keys(d.departmentId1, d.departmentId2)
            .execute();

    if (dialect.getName().equals("mysql") || dialect.getName().equals("mariadb")) {
      assertEquals(2, count);
    } else {
      assertEquals(1, count);
    }

    CompKeyDepartment resultDepartment =
        nativeSql
            .from(d)
            .where(
                c -> {
                  c.eq(d.departmentId1, 1);
                  c.eq(d.departmentId2, 1);
                })
            .fetchOne();
    // updated
    assertEquals(60, resultDepartment.getDepartmentNo());
    assertEquals("DEVELOPMENT", resultDepartment.getDepartmentName());
    assertEquals("KYOTO", resultDepartment.getLocation());
    assertEquals(2, resultDepartment.getVersion());
  }

  @Test
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
  void insert_onDuplicateKeyIgnore_unsetKey() {
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
  void insert_onDuplicateKeyIgnore_compositeKey() {
    CompKeyDepartment_ d = new CompKeyDepartment_();

    int count =
        nativeSql
            .insert(d)
            .values(
                c -> {
                  c.value(d.departmentId1, 1);
                  c.value(d.departmentId2, 1);
                  c.value(d.departmentNo, 60);
                  c.value(d.departmentName, "DEVELOPMENT");
                  c.value(d.location, "KYOTO");
                  c.value(d.version, 2);
                })
            .onDuplicateKeyIgnore()
            .keys(d.departmentId1, d.departmentId2)
            .execute();

    assertEquals(0, count);

    CompKeyDepartment resultDepartment =
        nativeSql
            .from(d)
            .where(
                c -> {
                  c.eq(d.departmentId1, 1);
                  c.eq(d.departmentId2, 1);
                })
            .fetchOne();
    // ignored
    assertEquals(10, resultDepartment.getDepartmentNo());
    assertEquals("ACCOUNTING", resultDepartment.getDepartmentName());
    assertEquals("NEW YORK", resultDepartment.getLocation());
    assertEquals(1, resultDepartment.getVersion());
  }

  @Test
  public void onDuplicateKeyUpdate_nonDuplicated_identityTable() {
    IdentityTable_ i = new IdentityTable_();

    nativeSql
        .insert(i)
        .values(
            c -> {
              c.value(i.uniqueValue, "1");
              c.value(i.value, "A");
            })
        .onDuplicateKeyUpdate()
        .keys(i.uniqueValue)
        .execute();
    nativeSql
        .insert(i)
        .values(
            c -> {
              c.value(i.uniqueValue, "2");
              c.value(i.value, "B");
            })
        .onDuplicateKeyUpdate()
        .keys(i.uniqueValue)
        .execute();

    var entities = nativeSql.from(i).orderBy(c -> c.asc(i.id)).fetch();
    assertEquals(2, entities.size());
    assertEquals(1, entities.get(0).getId());
    assertEquals("1", entities.get(0).getUniqueValue());
    assertEquals("A", entities.get(0).getValue());
    assertEquals(2, entities.get(1).getId());
    assertEquals("2", entities.get(1).getUniqueValue());
    assertEquals("B", entities.get(1).getValue());
  }

  @Test
  public void onDuplicateKeyUpdate_duplicated_identityTable() {
    IdentityTable_ i = new IdentityTable_();

    nativeSql
        .insert(i)
        .values(
            c -> {
              c.value(i.uniqueValue, "1");
              c.value(i.value, "A");
            })
        .onDuplicateKeyUpdate()
        .keys(i.uniqueValue)
        .execute();
    nativeSql
        .insert(i)
        .values(
            c -> {
              c.value(i.uniqueValue, "1");
              c.value(i.value, "B");
            })
        .onDuplicateKeyUpdate()
        .keys(i.uniqueValue)
        .execute();

    var entities = nativeSql.from(i).orderBy(c -> c.asc(i.id)).fetch();
    assertEquals(1, entities.size());
    assertEquals(1, entities.get(0).getId());
    assertEquals("1", entities.get(0).getUniqueValue());
    assertEquals("B", entities.get(0).getValue());
  }

  @Test
  public void onDuplicateKeyIgnore_nonDuplicated_identityTable() {
    IdentityTable_ i = new IdentityTable_();

    nativeSql
        .insert(i)
        .values(
            c -> {
              c.value(i.uniqueValue, "1");
              c.value(i.value, "A");
            })
        .onDuplicateKeyIgnore()
        .execute();
    nativeSql
        .insert(i)
        .values(
            c -> {
              c.value(i.uniqueValue, "2");
              c.value(i.value, "B");
            })
        .onDuplicateKeyIgnore()
        .execute();

    var entities = nativeSql.from(i).orderBy(c -> c.asc(i.id)).fetch();
    assertEquals(2, entities.size());
    assertEquals(1, entities.get(0).getId());
    assertEquals("1", entities.get(0).getUniqueValue());
    assertEquals("A", entities.get(0).getValue());
    assertEquals(2, entities.get(1).getId());
    assertEquals("2", entities.get(1).getUniqueValue());
    assertEquals("B", entities.get(1).getValue());
  }

  @Test
  @Run(onlyIf = {Dbms.MYSQL, Dbms.MYSQL8, Dbms.POSTGRESQL})
  public void onDuplicateKeyIgnore_duplicated_identityTable() {
    IdentityTable_ i = new IdentityTable_();

    nativeSql
        .insert(i)
        .values(
            c -> {
              c.value(i.uniqueValue, "1");
              c.value(i.value, "A");
            })
        .onDuplicateKeyIgnore()
        .execute();
    nativeSql
        .insert(i)
        .values(
            c -> {
              c.value(i.uniqueValue, "1");
              c.value(i.value, "B");
            })
        .onDuplicateKeyIgnore()
        .execute();

    var entities = nativeSql.from(i).orderBy(c -> c.asc(i.id)).fetch();
    assertEquals(1, entities.size());
    assertEquals(1, entities.get(0).getId());
    assertEquals("1", entities.get(0).getUniqueValue());
    assertEquals("A", entities.get(0).getValue());
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

  @Test
  void onDuplicateKeyIgnore_peek() {
    Department_ d = new Department_();
    final AtomicBoolean invoked = new AtomicBoolean(false);
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
        .peek(it -> invoked.set(true))
        .execute();
    assertTrue(invoked.get());
  }

  @Test
  void onDuplicateKeyUpdate_peek() {
    Department_ d = new Department_();
    final AtomicBoolean invoked = new AtomicBoolean(false);
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
        .peek(System.out::println)
        .execute();
    assertTrue(invoked.get());
  }

  @Test
  void onDuplicateKeyUpdate_keys_peek() {
    var d = new Department_();
    final AtomicBoolean invoked = new AtomicBoolean(false);
    nativeSql
        .insert(d)
        .values(
            c -> {
              c.value(d.departmentId, 6);
              c.value(d.departmentNo, 60);
              c.value(d.departmentName, "DEVELOPMENT");
              c.value(d.location, "KYOTO");
              c.value(d.version, 2);
            })
        .onDuplicateKeyUpdate()
        .keys(d.departmentId)
        .peek(it -> invoked.set(true))
        .execute();
    assertTrue(invoked.get());
  }
}
