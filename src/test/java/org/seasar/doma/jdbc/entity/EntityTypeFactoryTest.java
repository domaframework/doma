package org.seasar.doma.jdbc.entity;

import example.entity.Emp;
import junit.framework.TestCase;
import org.seasar.doma.DomaIllegalArgumentException;
import org.seasar.doma.jdbc.ClassHelper;

/** @author taedium */
public class EntityTypeFactoryTest extends TestCase {

  private ClassHelper classHelper = new ClassHelper() {};

  public void testGetEntityType() throws Exception {
    EntityType<Emp> type = EntityTypeFactory.getEntityType(Emp.class, classHelper);
    assertNotNull(type);
  }

  public void testGetEntityType_forNestedEntity() throws Exception {
    EntityType<NotTopLevelEntity.Hoge> type =
        EntityTypeFactory.getEntityType(NotTopLevelEntity.Hoge.class, classHelper);
    assertNotNull(type);
  }

  public void testGetEntityType_DomaIllegalArgumentException() throws Exception {
    try {
      EntityTypeFactory.getEntityType(Object.class, classHelper);
      fail();
    } catch (DomaIllegalArgumentException e) {
      System.out.println(e.getMessage());
    }
  }

  public void testGetEntityType_EntityTypeNotFoundException() throws Exception {
    try {
      EntityTypeFactory.getEntityType(Dept.class, classHelper);
      fail();
    } catch (EntityTypeNotFoundException e) {
      System.out.println(e.getMessage());
    }
  }
}
