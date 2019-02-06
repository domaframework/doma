package org.seasar.doma.jdbc.entity;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

import example.entity.Emp;
import org.junit.jupiter.api.Test;
import org.seasar.doma.DomaIllegalArgumentException;
import org.seasar.doma.jdbc.ClassHelper;

public class EntityTypeFactoryTest {

  private ClassHelper classHelper = new ClassHelper() {};

  @Test
  public void testGetEntityType() throws Exception {
    EntityType<Emp> type = EntityTypeFactory.getEntityType(Emp.class, classHelper);
    assertNotNull(type);
  }

  @Test
  public void testGetEntityType_forNestedEntity() throws Exception {
    EntityType<NotTopLevelEntity.Hoge> type =
        EntityTypeFactory.getEntityType(NotTopLevelEntity.Hoge.class, classHelper);
    assertNotNull(type);
  }

  @Test
  public void testGetEntityType_DomaIllegalArgumentException() throws Exception {
    try {
      EntityTypeFactory.getEntityType(Object.class, classHelper);
      fail();
    } catch (DomaIllegalArgumentException e) {
      System.out.println(e.getMessage());
    }
  }

  @Test
  public void testGetEntityType_EntityTypeNotFoundException() throws Exception {
    try {
      EntityTypeFactory.getEntityType(Dept.class, classHelper);
      fail();
    } catch (EntityTypeNotFoundException e) {
      System.out.println(e.getMessage());
    }
  }
}
