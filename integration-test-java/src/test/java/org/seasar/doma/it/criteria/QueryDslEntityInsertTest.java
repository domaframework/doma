/*
 * Copyright Doma Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.seasar.doma.it.criteria;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.seasar.doma.it.Dbms;
import org.seasar.doma.it.IntegrationTestEnvironment;
import org.seasar.doma.it.Run;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.Result;
import org.seasar.doma.jdbc.criteria.QueryDsl;
import org.seasar.doma.jdbc.dialect.Dialect;

@ExtendWith(IntegrationTestEnvironment.class)
public class QueryDslEntityInsertTest {

  private final QueryDsl dsl;
  private final Dialect dialect;

  public QueryDslEntityInsertTest(Config config) {
    this.dsl = new QueryDsl(config);
    this.dialect = config.getDialect();
  }

  @BeforeEach
  void before() {
    OfficeListener.buffer.setLength(0);
  }

  @Test
  void test() {
    Department_ d = new Department_();

    Department department = new Department();
    department.setDepartmentId(99);
    department.setDepartmentNo(99);
    department.setDepartmentName("aaa");
    department.setLocation("bbb");

    Result<Department> result = dsl.insert(d).single(department).execute();
    assertEquals(department, result.getEntity());

    Department department2 =
        dsl.from(d).where(c -> c.eq(d.departmentId, department.getDepartmentId())).fetchOne();
    assertNotNull(department2);
    assertEquals("aaa", department2.getDepartmentName());
  }

  @Run(onlyIf = Dbms.POSTGRESQL)
  @Test
  public void uuid() {
    Book_ b = new Book_();
    UUID id = UUID.randomUUID();

    Book book = new Book();
    book.id = id;
    book.title = "BOOK TITLE";
    dsl.insert(b).single(book).execute();

    Book book2 = dsl.from(b).where(c -> c.eq(b.id, id)).fetchOne();
    assertEquals(id, book2.id);
    assertEquals("BOOK TITLE", book2.title);
  }

  @Test
  public void onDuplicateKeyUpdate_nonDuplicated() {
    Department_ d = new Department_();

    Department department = new Department();
    department.setDepartmentId(5);
    department.setDepartmentNo(50);
    department.setDepartmentName("PLANNING");
    department.setLocation("TOKYO");
    Result<Department> result1 = dsl.insert(d).single(department).onDuplicateKeyUpdate().execute();
    assertEquals(1, result1.getCount());
    assertEquals(department, result1.getEntity());
    Department resultDepartment =
        dsl.from(d).where(c -> c.eq(d.departmentId, department.getDepartmentId())).fetchOne();
    // inserted
    assertEquals(50, resultDepartment.getDepartmentNo());
    assertEquals("PLANNING", resultDepartment.getDepartmentName());
    assertEquals("TOKYO", resultDepartment.getLocation());
  }

  @Test
  public void onDuplicateKeyUpdate_duplicated() {
    Department_ d = new Department_();

    Department department = new Department();
    department.setDepartmentId(1);
    department.setDepartmentNo(60);
    department.setDepartmentName("DEVELOPMENT");
    department.setLocation("KYOTO");
    Result<Department> result1 = dsl.insert(d).single(department).onDuplicateKeyUpdate().execute();
    if (dialect.getName().equals("mysql") || dialect.getName().equals("mariadb")) {
      assertEquals(2, result1.getCount());
    } else {
      assertEquals(1, result1.getCount());
    }
    assertEquals(department, result1.getEntity());
    Department resultDepartment =
        dsl.from(d).where(c -> c.eq(d.departmentId, department.getDepartmentId())).fetchOne();
    // updated
    assertEquals(60, resultDepartment.getDepartmentNo());
    assertEquals("DEVELOPMENT", resultDepartment.getDepartmentName());
    assertEquals("KYOTO", resultDepartment.getLocation());
  }

  @Test
  public void onDuplicateKeyIgnore_nonDuplicated() {
    Department_ d = new Department_();

    Department department = new Department();
    department.setDepartmentId(5);
    department.setDepartmentNo(50);
    department.setDepartmentName("PLANNING");
    department.setLocation("TOKYO");
    Result<Department> result1 = dsl.insert(d).single(department).onDuplicateKeyIgnore().execute();
    assertEquals(1, result1.getCount());
    assertEquals(department, result1.getEntity());
    Department resultDepartment =
        dsl.from(d).where(c -> c.eq(d.departmentId, department.getDepartmentId())).fetchOne();
    // inserted
    assertEquals(50, resultDepartment.getDepartmentNo());
    assertEquals("PLANNING", resultDepartment.getDepartmentName());
    assertEquals("TOKYO", resultDepartment.getLocation());
  }

  @Test
  public void onDuplicateKeyIgnore_duplicated() {
    Department_ d = new Department_();

    Department department = new Department();
    department.setDepartmentId(1);
    department.setDepartmentNo(60);
    department.setDepartmentName("DEVELOPMENT");
    department.setLocation("KYOTO");
    Result<Department> result1 = dsl.insert(d).single(department).onDuplicateKeyIgnore().execute();
    assertEquals(0, result1.getCount());
    assertEquals(department, result1.getEntity());
    Department resultDepartment =
        dsl.from(d).where(c -> c.eq(d.departmentId, department.getDepartmentId())).fetchOne();
    // ignored
    assertEquals(10, resultDepartment.getDepartmentNo());
    assertEquals("ACCOUNTING", resultDepartment.getDepartmentName());
    assertEquals("NEW YORK", resultDepartment.getLocation());
  }

  @Test
  public void onDuplicateKeyUpdate_nonDuplicated_identityTable() {
    IdentityTable_ i = new IdentityTable_();

    var entity1 = new IdentityTable();
    entity1.setUniqueValue("1");
    entity1.setValue("A");
    var entity2 = new IdentityTable();
    entity2.setUniqueValue("2");
    entity2.setValue("B");

    var result1 =
        dsl.insert(i).single(entity1).onDuplicateKeyUpdate().keys(i.uniqueValue).execute();
    assertEquals(entity1, result1.getEntity());
    if (!dialect.getName().equals("oracle")) {
      assertEquals(1, entity1.getId());
    }
    var result2 =
        dsl.insert(i).single(entity2).onDuplicateKeyUpdate().keys(i.uniqueValue).execute();
    assertEquals(entity2, result2.getEntity());
    if (!dialect.getName().equals("oracle")) {
      assertEquals(2, entity2.getId());
    }

    var entities = dsl.from(i).orderBy(c -> c.asc(i.id)).fetch();
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

    var entity1 = new IdentityTable();
    entity1.setUniqueValue("1");
    entity1.setValue("A");
    var entity2 = new IdentityTable();
    entity2.setUniqueValue("1");
    entity2.setValue("B");

    var result1 =
        dsl.insert(i).single(entity1).onDuplicateKeyUpdate().keys(i.uniqueValue).execute();
    assertEquals(entity1, result1.getEntity());
    if (!dialect.getName().equals("oracle")) {
      assertEquals(1, entity1.getId());
    }
    var result2 =
        dsl.insert(i).single(entity2).onDuplicateKeyUpdate().keys(i.uniqueValue).execute();
    assertEquals(entity2, result2.getEntity());
    if (!dialect.getName().equals("oracle")) {
      assertEquals(1, entity2.getId());
    }

    var entities = dsl.from(i).orderBy(c -> c.asc(i.id)).fetch();
    assertEquals(1, entities.size());
    assertEquals(1, entities.get(0).getId());
    assertEquals("1", entities.get(0).getUniqueValue());
    assertEquals("B", entities.get(0).getValue());
  }

  @Test
  public void onDuplicateKeyIgnore_nonDuplicated_identityTable() {
    IdentityTable_ i = new IdentityTable_();

    var entity1 = new IdentityTable();
    entity1.setUniqueValue("1");
    entity1.setValue("A");
    var entity2 = new IdentityTable();
    entity2.setUniqueValue("2");
    entity2.setValue("B");

    var result1 = dsl.insert(i).single(entity1).onDuplicateKeyIgnore().execute();
    assertEquals(entity1, result1.getEntity());
    if (!dialect.getName().equals("oracle")) {
      assertEquals(1, entity1.getId());
    }
    var result2 = dsl.insert(i).single(entity2).onDuplicateKeyIgnore().execute();
    assertEquals(entity2, result2.getEntity());
    assertEquals(2, entity2.getId());

    var entities = dsl.from(i).orderBy(c -> c.asc(i.id)).fetch();
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

    var entity1 = new IdentityTable();
    entity1.setUniqueValue("1");
    entity1.setValue("A");
    var entity2 = new IdentityTable();
    entity2.setUniqueValue("1");
    entity2.setValue("B");

    var result1 = dsl.insert(i).single(entity1).onDuplicateKeyIgnore().execute();
    assertEquals(entity1, result1.getEntity());
    if (!dialect.getName().equals("oracle")) {
      assertEquals(1, entity1.getId());
    }
    var result2 = dsl.insert(i).single(entity2).onDuplicateKeyIgnore().execute();
    assertEquals(entity2, result2.getEntity());
    assertNull(entity2.getId());

    var entities = dsl.from(i).orderBy(c -> c.asc(i.id)).fetch();
    assertEquals(1, entities.size());
    assertEquals(1, entities.get(0).getId());
    assertEquals("1", entities.get(0).getUniqueValue());
    assertEquals("A", entities.get(0).getValue());
  }

  @Test
  @Run(unless = {Dbms.MYSQL, Dbms.MYSQL8, Dbms.ORACLE})
  void returning() {
    Department_ d = new Department_();

    Department department = new Department();
    department.setDepartmentId(99);
    department.setDepartmentNo(99);
    department.setDepartmentName("aaa");
    department.setLocation("bbb");

    Department resultEntity = dsl.insert(d).single(department).returning().fetchOne();
    assertNotNull(resultEntity);
    assertNotEquals(department, resultEntity);
    assertEquals(department.getDepartmentId(), resultEntity.getDepartmentId());
    assertEquals(department.getDepartmentNo(), resultEntity.getDepartmentNo());
    assertEquals(department.getDepartmentName(), resultEntity.getDepartmentName());
    assertEquals(department.getLocation(), resultEntity.getLocation());
    assertEquals(1, resultEntity.getVersion());

    Department department2 =
        dsl.from(d).where(c -> c.eq(d.departmentId, department.getDepartmentId())).fetchOne();
    assertNotNull(department2);
    assertEquals("aaa", department2.getDepartmentName());
  }

  @Test
  @Run(unless = {Dbms.MYSQL, Dbms.MYSQL8, Dbms.ORACLE})
  void returning_specificProperties() {
    Department_ d = new Department_();

    Department department = new Department();
    department.setDepartmentId(99);
    department.setDepartmentNo(99);
    department.setDepartmentName("aaa");
    department.setLocation("bbb");

    Department resultEntity =
        dsl.insert(d).single(department).returning(d.departmentNo, d.departmentName).fetchOne();
    assertNotNull(resultEntity);
    assertNotEquals(department, resultEntity);
    assertNull(resultEntity.getDepartmentId());
    assertEquals(department.getDepartmentNo(), resultEntity.getDepartmentNo());
    assertEquals(department.getDepartmentName(), resultEntity.getDepartmentName());
    assertNull(resultEntity.getLocation());
    assertNull(resultEntity.getVersion());

    Department department2 =
        dsl.from(d).where(c -> c.eq(d.departmentId, department.getDepartmentId())).fetchOne();
    assertNotNull(department2);
    assertEquals("aaa", department2.getDepartmentName());
  }

  @Test
  @Run(unless = {Dbms.MYSQL, Dbms.MYSQL8, Dbms.ORACLE})
  public void returning_identityTable() {
    IdentityTable_ i = new IdentityTable_();

    var entity1 = new IdentityTable();
    entity1.setUniqueValue("1");
    entity1.setValue("A");

    var resultEntity = dsl.insert(i).single(entity1).returning().fetchOne();
    assertNotEquals(entity1, resultEntity);
    assertNotNull(resultEntity.getId());
    assertEquals("1", resultEntity.getUniqueValue());
    assertEquals("A", resultEntity.getValue());
  }

  @Test
  @Run(unless = {Dbms.MYSQL, Dbms.MYSQL8, Dbms.ORACLE})
  public void returning_onDuplicateKeyUpdate_nonDuplicated() {
    Department_ d = new Department_();

    Department department = new Department();
    department.setDepartmentId(5);
    department.setDepartmentNo(50);
    department.setDepartmentName("PLANNING");
    department.setLocation("TOKYO");
    Department resultDepartment =
        dsl.insert(d).single(department).onDuplicateKeyUpdate().returning().fetchOne();
    assertNotNull(resultDepartment);
    assertNotEquals(department, resultDepartment);
    // inserted
    assertEquals(50, resultDepartment.getDepartmentNo());
    assertEquals("PLANNING", resultDepartment.getDepartmentName());
    assertEquals("TOKYO", resultDepartment.getLocation());
  }

  @Test
  @Run(unless = {Dbms.MYSQL, Dbms.MYSQL8, Dbms.ORACLE})
  public void returning_onDuplicateKeyUpdate_duplicated() {
    Department_ d = new Department_();

    Department department = new Department();
    department.setDepartmentId(1);
    department.setDepartmentNo(60);
    department.setDepartmentName("DEVELOPMENT");
    department.setLocation("KYOTO");
    Department resultDepartment =
        dsl.insert(d).single(department).onDuplicateKeyUpdate().returning().fetchOne();
    assertNotNull(resultDepartment);
    assertNotEquals(department, resultDepartment);
    // updated
    assertEquals(60, resultDepartment.getDepartmentNo());
    assertEquals("DEVELOPMENT", resultDepartment.getDepartmentName());
    assertEquals("KYOTO", resultDepartment.getLocation());
  }

  @Test
  @Run(unless = {Dbms.MYSQL, Dbms.MYSQL8, Dbms.ORACLE})
  public void returning_onDuplicateKeyIgnore_nonDuplicated() {
    Department_ d = new Department_();

    Department department = new Department();
    department.setDepartmentId(5);
    department.setDepartmentNo(50);
    department.setDepartmentName("PLANNING");
    department.setLocation("TOKYO");
    Department resultDepartment =
        dsl.insert(d).single(department).onDuplicateKeyIgnore().returning().fetchOne();
    assertNotNull(resultDepartment);
    assertNotEquals(department, resultDepartment);
    // inserted
    assertEquals(50, resultDepartment.getDepartmentNo());
    assertEquals("PLANNING", resultDepartment.getDepartmentName());
    assertEquals("TOKYO", resultDepartment.getLocation());
  }

  @Test
  @Run(unless = {Dbms.MYSQL, Dbms.MYSQL8, Dbms.ORACLE})
  public void returning_onDuplicateKeyIgnore_duplicated() {
    Department_ d = new Department_();

    Department department = new Department();
    department.setDepartmentId(1);
    department.setDepartmentNo(60);
    department.setDepartmentName("DEVELOPMENT");
    department.setLocation("KYOTO");
    Department resultEntity =
        dsl.insert(d).single(department).onDuplicateKeyIgnore().returning().fetchOne();
    assertNull(resultEntity);
  }

  @Test
  @Run(unless = {Dbms.MYSQL, Dbms.MYSQL8, Dbms.ORACLE})
  public void returning_onDuplicateKeyUpdate_nonDuplicated_identityTable() {
    IdentityTable_ i = new IdentityTable_();

    var entity1 = new IdentityTable();
    entity1.setUniqueValue("1");
    entity1.setValue("A");
    var entity2 = new IdentityTable();
    entity2.setUniqueValue("2");
    entity2.setValue("B");

    var resultEntity1 =
        dsl.insert(i)
            .single(entity1)
            .onDuplicateKeyUpdate()
            .keys(i.uniqueValue)
            .returning()
            .fetchOne();
    assertNotNull(resultEntity1);
    assertNotEquals(entity1, resultEntity1);
    assertNotNull(resultEntity1.getId());
    assertEquals("1", resultEntity1.getUniqueValue());
    assertEquals("A", resultEntity1.getValue());

    var resultEntity2 =
        dsl.insert(i)
            .single(entity2)
            .onDuplicateKeyUpdate()
            .keys(i.uniqueValue)
            .returning()
            .fetchOne();
    assertNotNull(resultEntity2);
    assertNotEquals(entity2, resultEntity2);
    assertNotNull(resultEntity2.getId());
    assertEquals("2", resultEntity2.getUniqueValue());
    assertEquals("B", resultEntity2.getValue());
  }

  @Test
  @Run(onlyIf = {Dbms.POSTGRESQL, Dbms.SQLITE})
  public void returning_onDuplicateKeyIgnore_duplicated_identityTable() {
    IdentityTable_ i = new IdentityTable_();

    var entity1 = new IdentityTable();
    entity1.setUniqueValue("1");
    entity1.setValue("A");
    var entity2 = new IdentityTable();
    entity2.setUniqueValue("1");
    entity2.setValue("B");

    var resultEntity1 = dsl.insert(i).single(entity1).onDuplicateKeyIgnore().returning().fetchOne();
    assertNotNull(resultEntity1);
    assertNotEquals(entity1, resultEntity1);
    assertNotNull(resultEntity1.getId());
    assertEquals("1", resultEntity1.getUniqueValue());
    assertEquals("A", resultEntity1.getValue());

    var resultEntity2 = dsl.insert(i).single(entity2).onDuplicateKeyIgnore().returning().fetchOne();
    assertNull(resultEntity2);
  }

  @Test
  @Run(unless = {Dbms.MYSQL, Dbms.MYSQL8, Dbms.ORACLE})
  public void returning_listener() {
    Office_ o = new Office_();

    Office office = new Office();
    office.setDepartmentId(100);
    office.setDepartmentNo(100);
    office.setDepartmentName("PLANNING");
    office.setLocation("TOKYO");

    dsl.insert(o).single(office).returning(o.departmentId, o.version).fetchOne();

    assertEquals(
        "preInsert:departmentId,version. postInsert:departmentId,version. ",
        OfficeListener.buffer.toString());
  }

  @Test
  public void embeddable() {
    Customer_ c = new Customer_();

    Customer customer = new Customer();
    customer.setCustomerId(10);
    CustomerAddress billingAddress = new CustomerAddress("OSAKA", "530-0001", "789 RIVER RD");
    customer.setBillingAddress(billingAddress);
    CustomerAddress shippingAddress = new CustomerAddress("KYOTO", "600-8216", "321 HILL ST");
    customer.setShippingAddress(shippingAddress);

    dsl.insert(c).single(customer).execute();

    Customer inserted =
        dsl.from(c).where(w -> w.eq(c.customerId, customer.getCustomerId())).fetchOne();
    assertNotNull(inserted);
    assertEquals(10, inserted.getCustomerId());

    CustomerAddress insertedBilling = inserted.getBillingAddress();
    assertNotNull(insertedBilling);
    assertEquals("789 RIVER RD", insertedBilling.street());
    assertEquals("OSAKA", insertedBilling.city());
    assertEquals("530-0001", insertedBilling.zipCode());

    CustomerAddress insertedShipping = inserted.getShippingAddress();
    assertNotNull(insertedShipping);
    assertEquals("321 HILL ST", insertedShipping.street());
    assertEquals("KYOTO", insertedShipping.city());
    assertEquals("600-8216", insertedShipping.zipCode());
  }
}
