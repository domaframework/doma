package org.seasar.doma.jdbc.entity;

import example.entity.Dept;
import example.entity.Emp;
import example.entity.ImmutableEmp;
import example.entity._Dept;
import example.entity._Emp;
import example.entity._ImmutableEmp;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import junit.framework.TestCase;

public class EntityDescTest extends TestCase {

  public void test() throws Exception {
    EntityDesc<Emp> entityDesc = _Emp.getSingletonInternal();
    entityDesc.getName();
  }

  public void testImmutable_newEntity() throws Exception {
    var emp = new ImmutableEmp(99, "hoge", BigDecimal.ONE, 1);
    EntityDesc<ImmutableEmp> entityDesc = _ImmutableEmp.getSingletonInternal();
    Map<String, Property<ImmutableEmp, ?>> args = new HashMap<>();

    var idType = entityDesc.getEntityPropertyDesc("id");
    var id = idType.createProperty();
    id.load(emp);
    args.put(idType.getName(), id);

    var salaryType = entityDesc.getEntityPropertyDesc("salary");
    var salary = salaryType.createProperty();
    salary.load(emp);
    args.put(salaryType.getName(), salary);

    var newEmp = entityDesc.newEntity(args);

    assertEquals(Integer.valueOf(99), newEmp.getId());
    assertNull(newEmp.getName());
    assertEquals(BigDecimal.ONE, newEmp.getSalary());
    assertNull(newEmp.getVersion());
  }

  public void testGetTableName_naming() throws Exception {
    EntityDesc<Dept> entityDesc = _Dept.getSingletonInternal();
    assertEquals("dept", entityDesc.getTableName((namingType, text) -> text.toLowerCase()));
  }

  public void testGetQualifiedName_naming_quote() throws Exception {
    EntityDesc<Dept> entityDesc = _Dept.getSingletonInternal();
    assertEquals(
        "[CATA].[dept]",
        entityDesc.getQualifiedTableName(
            (namingType, text) -> text.toLowerCase(), text -> "[" + text + "]"));
  }
}
