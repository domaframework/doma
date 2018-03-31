package org.seasar.doma.jdbc.entity;

import example.entity.Emp;
import junit.framework.TestCase;
import org.seasar.doma.DomaIllegalArgumentException;
import org.seasar.doma.jdbc.ClassHelper;

public class EntityDescFactoryTest extends TestCase {

  private ClassHelper classHelper = new ClassHelper() {};

  public void testGetEntityDesc() throws Exception {
    var desc = EntityDescFactory.getEntityDesc(Emp.class, classHelper);
    assertNotNull(desc);
  }

  public void testGetEntityDesc_forNestedEntity() throws Exception {
    var desc = EntityDescFactory.getEntityDesc(NotTopLevelEntity.Hoge.class, classHelper);
    assertNotNull(desc);
  }

  public void testGetEntityDesc_DomaIllegalArgumentException() throws Exception {
    try {
      EntityDescFactory.getEntityDesc(Object.class, classHelper);
      fail();
    } catch (DomaIllegalArgumentException e) {
      System.out.println(e.getMessage());
    }
  }

  public void testGetEntityDesc_EntityDescNotFoundException() throws Exception {
    try {
      EntityDescFactory.getEntityDesc(Dept.class, classHelper);
      fail();
    } catch (EntityDescNotFoundException e) {
      System.out.println(e.getMessage());
    }
  }
}
