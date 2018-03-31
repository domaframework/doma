package org.seasar.doma.internal.apt.meta;

import org.seasar.doma.internal.apt.AptTestCase;
import org.seasar.doma.internal.apt.Context;
import org.seasar.doma.internal.apt.meta.entity.EntityMeta;
import org.seasar.doma.internal.apt.meta.entity.EntityMetaFactory;
import org.seasar.doma.internal.apt.processor.entity.NamingType1Entity;
import org.seasar.doma.jdbc.entity.NamingType;

public class EntityMetaFactoryTest extends AptTestCase {

  public void testNaming1Type() throws Exception {
    Class<?> target = NamingType1Entity.class;
    addCompilationUnit(target);
    addProcessor(
        new AptProcessor(
            ctx -> {
              var entityMeta = createEntityMeta(ctx, target);
              assertEquals(NamingType.UPPER_CASE, entityMeta.getNamingType());
            }));
    compile();
    assertTrue(getCompiledResult());
  }

  public void testNaming2Type() throws Exception {
    Class<?> target = NamingType2Entity.class;
    addCompilationUnit(target);
    addProcessor(
        new AptProcessor(
            ctx -> {
              var entityMeta = createEntityMeta(ctx, target);
              assertEquals(NamingType.UPPER_CASE, entityMeta.getNamingType());
            }));
    compile();
    assertTrue(getCompiledResult());
  }

  public void testNaming3Type() throws Exception {
    Class<?> target = NamingType3Entity.class;
    addCompilationUnit(target);
    addProcessor(
        new AptProcessor(
            ctx -> {
              var entityMeta = createEntityMeta(ctx, target);
              assertEquals(NamingType.NONE, entityMeta.getNamingType());
            }));
    compile();
    assertTrue(getCompiledResult());
  }

  protected EntityMeta createEntityMeta(Context ctx, Class<?> clazz) {
    var entityElement = ctx.getElements().getTypeElement(clazz);
    var entityMetaFactory = new EntityMetaFactory(ctx, entityElement);
    return entityMetaFactory.createTypeElementMeta();
  }
}
