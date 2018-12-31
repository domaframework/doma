package org.seasar.doma.internal.apt.meta;

import javax.annotation.processing.ProcessingEnvironment;
import org.seasar.doma.internal.apt.AptTestCase;
import org.seasar.doma.internal.apt.Context;
import org.seasar.doma.internal.apt.meta.entity.EntityMeta;
import org.seasar.doma.internal.apt.meta.entity.EntityMetaFactory;
import org.seasar.doma.internal.apt.meta.entity.EntityPropertyMetaFactory;
import org.seasar.doma.internal.apt.processor.entity.NamingType1Entity;
import org.seasar.doma.jdbc.entity.NamingType;

public class EntityMetaFactoryTest extends AptTestCase {

  public void testNaming1Type() throws Exception {
    Class<?> target = NamingType1Entity.class;
    addCompilationUnit(target);
    compile();

    EntityMetaFactory entityMetaFactory = createEntityMetaFactory();
    EntityMeta entityMeta = entityMetaFactory.createTypeElementMeta(getTypeElement(target));
    assertEquals(NamingType.UPPER_CASE, entityMeta.getNamingType());
  }

  public void testNaming2Type() throws Exception {
    Class<?> target = NamingType2Entity.class;
    addCompilationUnit(target);
    compile();

    EntityMetaFactory entityMetaFactory = createEntityMetaFactory();
    EntityMeta entityMeta = entityMetaFactory.createTypeElementMeta(getTypeElement(target));
    assertEquals(NamingType.UPPER_CASE, entityMeta.getNamingType());
  }

  public void testNaming3Type() throws Exception {
    Class<?> target = NamingType3Entity.class;
    addCompilationUnit(target);
    compile();

    EntityMetaFactory entityMetaFactory = createEntityMetaFactory();
    EntityMeta entityMeta = entityMetaFactory.createTypeElementMeta(getTypeElement(target));
    assertEquals(NamingType.NONE, entityMeta.getNamingType());
  }

  protected EntityMetaFactory createEntityMetaFactory() {
    ProcessingEnvironment env = getProcessingEnvironment();
    Context ctx = new Context(env);
    EntityPropertyMetaFactory propertyMetaFactory = new EntityPropertyMetaFactory(ctx);
    return new EntityMetaFactory(ctx, propertyMetaFactory);
  }
}
