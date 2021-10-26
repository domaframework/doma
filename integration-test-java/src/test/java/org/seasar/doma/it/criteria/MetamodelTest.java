package org.seasar.doma.it.criteria;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.seasar.doma.DomaIllegalArgumentException;
import org.seasar.doma.jdbc.entity.EntityType;

public class MetamodelTest {

  @Test
  void tableName() {
    Employee_ e = new Employee_("MY_EMP");
    EntityType<?> entityType = e.asType();
    String tableName = entityType.getQualifiedTableName(null, null);
    assertEquals("MY_EMP", tableName);
  }

  @Test
  void tableName_single_quotation() {
    DomaIllegalArgumentException ex =
        assertThrows(
            DomaIllegalArgumentException.class,
            () -> {
              Employee_ e = new Employee_("ab'c");
              e.asType();
            });
    System.out.println(ex.getMessage());
  }

  @Test
  void tableName_semicolon() {
    DomaIllegalArgumentException ex =
        assertThrows(
            DomaIllegalArgumentException.class,
            () -> {
              Employee_ e = new Employee_("ab;c");
              e.asType();
            });
    System.out.println(ex.getMessage());
  }

  @Test
  void tableName_two_hyphens() {
    DomaIllegalArgumentException ex =
        assertThrows(
            DomaIllegalArgumentException.class,
            () -> {
              Employee_ e = new Employee_("ab--c");
              e.asType();
            });
    System.out.println(ex.getMessage());
  }

  @Test
  void tableName_slash() {
    DomaIllegalArgumentException ex =
        assertThrows(
            DomaIllegalArgumentException.class,
            () -> {
              Employee_ e = new Employee_("ab/*c");
              e.asType();
            });
    System.out.println(ex.getMessage());
  }
}
