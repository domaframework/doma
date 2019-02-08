package org.seasar.doma.internal.apt.meta;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import javax.lang.model.element.TypeElement;
import org.junit.jupiter.api.Test;
import org.seasar.doma.internal.apt.CompilerSupport;
import org.seasar.doma.internal.apt.TestProcessor;
import org.seasar.doma.internal.apt.meta.entity.EntityMeta;
import org.seasar.doma.internal.apt.meta.entity.EntityMetaFactory;
import org.seasar.doma.internal.apt.processor.entity.NamingType1Entity;
import org.seasar.doma.jdbc.entity.NamingType;

class EntityMetaFactoryTest extends CompilerSupport {

  @Test
  void testNaming1Type() throws Exception {
    Class<?> target = NamingType1Entity.class;
    addCompilationUnit(target);
    addProcessor(
        new TestProcessor() {
          @Override
          protected void run() {
            TypeElement typeElement = ctx.getElements().getTypeElement(target);
            EntityMetaFactory entityMetaFactory = new EntityMetaFactory(ctx);
            EntityMeta entityMeta = entityMetaFactory.createTypeElementMeta(typeElement);
            assertEquals(NamingType.UPPER_CASE, entityMeta.getNamingType());
          }
        });
    compile();
    assertTrue(getCompiledResult());
  }

  @Test
  void testNaming2Type() throws Exception {
    Class<?> target = NamingType2Entity.class;
    addCompilationUnit(target);
    addProcessor(
        new TestProcessor() {
          @Override
          protected void run() {
            TypeElement typeElement = ctx.getElements().getTypeElement(target);
            EntityMetaFactory entityMetaFactory = new EntityMetaFactory(ctx);
            EntityMeta entityMeta = entityMetaFactory.createTypeElementMeta(typeElement);
            assertEquals(NamingType.UPPER_CASE, entityMeta.getNamingType());
          }
        });
    compile();
    assertTrue(getCompiledResult());
  }

  @Test
  void testNaming3Type() throws Exception {
    Class<?> target = NamingType3Entity.class;
    addCompilationUnit(target);
    addProcessor(
        new TestProcessor() {
          @Override
          protected void run() {
            TypeElement typeElement = ctx.getElements().getTypeElement(target);
            EntityMetaFactory entityMetaFactory = new EntityMetaFactory(ctx);
            EntityMeta entityMeta = entityMetaFactory.createTypeElementMeta(typeElement);
            assertEquals(NamingType.NONE, entityMeta.getNamingType());
          }
        });
    compile();
    assertTrue(getCompiledResult());
  }
}
