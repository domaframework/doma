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
package org.seasar.doma.internal.apt.decl;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.seasar.doma.internal.apt.CompilerSupport;
import org.seasar.doma.internal.apt.TestProcessor;

class DeclarationsTest extends CompilerSupport {

  @BeforeEach
  void beforeEach() {
    addCompilationUnit(getClass());
  }

  @AfterEach
  void afterEach() throws Exception {
    compile();
    assertTrue(getCompiledResult());
  }

  @Test
  void newPrimitiveBooleanTypeDeclaration() {
    addProcessor(
        new TestProcessor() {
          @Override
          protected void run() {
            TypeDeclaration typeDeclaration =
                ctx.getDeclarations().newPrimitiveBooleanTypeDeclaration();
            assertNotNull(typeDeclaration);
            TypeMirror booleanType = ctx.getMoreTypes().getPrimitiveType(TypeKind.BOOLEAN);
            assertTrue(ctx.getMoreTypes().isSameType(typeDeclaration.getType(), booleanType));
          }
        });
  }

  @Test
  void newTypeDeclaration_Class() {
    addProcessor(
        new TestProcessor() {
          @Override
          protected void run() {
            TypeDeclaration typeDeclaration = ctx.getDeclarations().newTypeDeclaration(List.class);
            assertNotNull(typeDeclaration);
            assertTrue(
                ctx.getMoreTypes().isSameTypeWithErasure(typeDeclaration.getType(), List.class));
          }
        });
  }

  @Test
  void newTypeDeclaration_TypeMirror() {
    addProcessor(
        new TestProcessor() {
          @Override
          protected void run() {
            TypeMirror type = ctx.getMoreTypes().getTypeMirror(List.class);
            TypeDeclaration typeDeclaration = ctx.getDeclarations().newTypeDeclaration(type);
            assertNotNull(typeDeclaration);
            assertTrue(
                ctx.getMoreTypes().isSameTypeWithErasure(typeDeclaration.getType(), List.class));
          }
        });
  }

  @Test
  void newTypeDeclaration_TypeElement() {
    addProcessor(
        new TestProcessor() {
          @Override
          protected void run() {
            TypeElement typeElement = ctx.getMoreElements().getTypeElement(List.class);
            TypeDeclaration typeDeclaration = ctx.getDeclarations().newTypeDeclaration(typeElement);
            assertNotNull(typeDeclaration);
            assertTrue(
                ctx.getMoreTypes().isSameTypeWithErasure(typeDeclaration.getType(), List.class));
          }
        });
  }
}
