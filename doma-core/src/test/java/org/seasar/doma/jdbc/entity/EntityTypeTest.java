package org.seasar.doma.jdbc.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import example.entity.Dept;
import example.entity.Emp;
import example.entity.ImmutableEmp;
import example.entity._Dept;
import example.entity._Emp;
import example.entity._ImmutableEmp;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;

public class EntityTypeTest {

  @Test
  public void test() {
    EntityType<Emp> entityType = _Emp.getSingletonInternal();
    entityType.getName();
  }

  @Test
  public void testImmutable_newEntity() {
    ImmutableEmp emp = new ImmutableEmp(99, "hoge", BigDecimal.ONE, 1);
    EntityType<ImmutableEmp> entityType = _ImmutableEmp.getSingletonInternal();
    Map<String, Property<ImmutableEmp, ?>> args = new HashMap<>();

    EntityPropertyType<ImmutableEmp, ?> idType = entityType.getEntityPropertyType("id");
    Property<ImmutableEmp, ?> id = idType.createProperty();
    id.load(emp);
    args.put(idType.getName(), id);

    EntityPropertyType<ImmutableEmp, ?> salaryType = entityType.getEntityPropertyType("salary");
    Property<ImmutableEmp, ?> salary = salaryType.createProperty();
    salary.load(emp);
    args.put(salaryType.getName(), salary);

    ImmutableEmp newEmp = entityType.newEntity(args);

    assertEquals(Integer.valueOf(99), newEmp.getId());
    assertNull(newEmp.getName());
    assertEquals(BigDecimal.ONE, newEmp.getSalary());
    assertNull(newEmp.getVersion());
  }

  @SuppressWarnings("deprecation")
  @Test
  public void testGetTableName() {
    EntityType<Dept> entityType = _Dept.getSingletonInternal();
    assertEquals("DEPT", entityType.getTableName());
  }

  @Test
  public void testGetTableName_naming() {
    EntityType<Dept> entityType = _Dept.getSingletonInternal();
    assertEquals("dept", entityType.getTableName((namingType, text) -> text.toLowerCase()));
  }

  @SuppressWarnings("deprecation")
  @Test
  public void testGetQualifiedName() {
    EntityType<Dept> entityType = _Dept.getSingletonInternal();
    assertEquals("CATA.DEPT", entityType.getQualifiedTableName());
  }

  @SuppressWarnings("deprecation")
  @Test
  public void testGetQualifiedName_quote() {
    EntityType<Dept> entityType = _Dept.getSingletonInternal();
    assertEquals("[CATA].[DEPT]", entityType.getQualifiedTableName(text -> "[" + text + "]"));
  }

  @Test
  public void testGetQualifiedName_naming_quote() {
    EntityType<Dept> entityType = _Dept.getSingletonInternal();
    assertEquals(
        "[CATA].[dept]",
        entityType.getQualifiedTableName(
            (namingType, text) -> text.toLowerCase(), text -> "[" + text + "]"));
  }
}
