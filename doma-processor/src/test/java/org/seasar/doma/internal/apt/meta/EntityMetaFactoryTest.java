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
package org.seasar.doma.internal.apt.meta;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import javax.lang.model.element.TypeElement;
import org.junit.jupiter.api.Test;
import org.seasar.doma.internal.apt.AbstractCompilerTest;
import org.seasar.doma.internal.apt.RoundContext;
import org.seasar.doma.internal.apt.TestProcessor;
import org.seasar.doma.internal.apt.meta.entity.EntityMeta;
import org.seasar.doma.internal.apt.meta.entity.EntityMetaFactory;
import org.seasar.doma.internal.apt.processor.entity.NamingType1Entity;
import org.seasar.doma.jdbc.entity.NamingType;

class EntityMetaFactoryTest extends AbstractCompilerTest {

  @Test
  void testNaming1Type() throws Exception {
    Class<?> target = NamingType1Entity.class;
    addCompilationUnit(target);
    addProcessor(
        new TestProcessor() {
          @Override
          protected void run(RoundContext ctx) {
            TypeElement typeElement = ctx.getMoreElements().getTypeElement(target);
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
          protected void run(RoundContext ctx) {
            TypeElement typeElement = ctx.getMoreElements().getTypeElement(target);
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
          protected void run(RoundContext ctx) {
            TypeElement typeElement = ctx.getMoreElements().getTypeElement(target);
            EntityMetaFactory entityMetaFactory = new EntityMetaFactory(ctx);
            EntityMeta entityMeta = entityMetaFactory.createTypeElementMeta(typeElement);
            assertEquals(NamingType.NONE, entityMeta.getNamingType());
          }
        });
    compile();
    assertTrue(getCompiledResult());
  }
}
