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
package org.seasar.doma.internal.apt;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.TypeParameterElement;
import javax.lang.model.type.ArrayType;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.type.TypeVariable;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MoreTypesTest extends CompilerSupport {

  @SuppressWarnings("InnerClassMayBeStatic")
  private class NumberList<E extends Number> extends ArrayList<E> {}

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
  void toTypeElement() {
    addProcessor(
        new TestProcessor() {
          @Override
          protected void run() {
            TypeMirror type = ctx.getMoreTypes().getTypeMirror(String.class);
            TypeElement typeElement = ctx.getMoreTypes().toTypeElement(type);
            assertNotNull(typeElement);
          }
        });
  }

  @Test
  void toDeclaredType() {
    addProcessor(
        new TestProcessor() {
          @Override
          protected void run() {
            TypeMirror type = ctx.getMoreTypes().getTypeMirror(String.class);
            DeclaredType declaredType = ctx.getMoreTypes().toDeclaredType(type);
            assertNotNull(declaredType);
          }
        });
  }

  @Test
  void toTypeVariable() {
    addProcessor(
        new TestProcessor() {
          @Override
          protected void run() {
            TypeElement typeElement = ctx.getMoreElements().getTypeElement(List.class);
            TypeParameterElement typeParameterElement =
                typeElement.getTypeParameters().iterator().next();
            TypeVariable typeVariable =
                ctx.getMoreTypes().toTypeVariable(typeParameterElement.asType());
            assertNotNull(typeVariable);
          }
        });
  }

  @Test
  void toArrayType() {
    addProcessor(
        new TestProcessor() {
          @Override
          protected void run() {
            TypeMirror componentType = ctx.getMoreTypes().getTypeMirror(String.class);
            TypeMirror type = ctx.getMoreTypes().getArrayType(componentType);
            ArrayType arrayType = ctx.getMoreTypes().toArrayType(type);
            assertNotNull(arrayType);
          }
        });
  }

  @Test
  void isArray() {
    addProcessor(
        new TestProcessor() {
          @Override
          protected void run() {
            TypeMirror componentType = ctx.getMoreTypes().getTypeMirror(String.class);
            TypeMirror type = ctx.getMoreTypes().getArrayType(componentType);
            assertTrue(ctx.getMoreTypes().isArray(type));
          }
        });
  }

  @Test
  void getTypeName() {
    addProcessor(
        new TestProcessor() {
          @Override
          protected void run() {
            TypeMirror intType = ctx.getMoreTypes().getPrimitiveType(TypeKind.INT);
            assertEquals("int", ctx.getMoreTypes().getTypeName(intType));
            TypeMirror listType = ctx.getMoreElements().getTypeElement(List.class).asType();
            assertEquals("java.util.List<E>", ctx.getMoreTypes().getTypeName(listType));
          }
        },
        new TestProcessor() {
          @Override
          protected void run() {
            TypeElement typeElement = ctx.getMoreElements().getTypeElement(NumberList.class);
            TypeParameterElement typeParameterElement =
                typeElement.getTypeParameters().iterator().next();
            assertEquals("E", ctx.getMoreTypes().getTypeName(typeParameterElement.asType()));
          }
        });
  }

  @Test
  void boxIfPrimitive() {
    addProcessor(
        new TestProcessor() {
          @Override
          protected void run() {
            TypeMirror type = ctx.getMoreTypes().getPrimitiveType(TypeKind.INT);
            TypeMirror t1 = ctx.getMoreTypes().boxIfPrimitive(type);
            TypeMirror t2 = ctx.getMoreTypes().getTypeMirror(Integer.class);
            assertTrue(ctx.getMoreTypes().isSameType(t1, t2));
          }
        },
        new TestProcessor() {
          @Override
          protected void run() {
            TypeMirror t1 = ctx.getMoreTypes().getTypeMirror(Integer.class);
            TypeMirror t2 = ctx.getMoreTypes().boxIfPrimitive(t1);
            assertTrue(ctx.getMoreTypes().isSameType(t1, t2));
          }
        });
  }

  @Test
  void getTypeMirror() {
    addProcessor(
        new TestProcessor() {
          @Override
          protected void run() {
            TypeMirror type = ctx.getMoreTypes().getTypeMirror(int.class);
            assertNotNull(type);
            assertEquals("int", type.toString());
          }
        },
        new TestProcessor() {
          @Override
          protected void run() {
            TypeMirror type = ctx.getMoreTypes().getTypeMirror(String.class);
            assertNotNull(type);
            assertEquals("java.lang.String", type.toString());
          }
        },
        new TestProcessor() {
          @Override
          protected void run() {
            TypeMirror type = ctx.getMoreTypes().getTypeMirror(String[].class);
            assertNotNull(type);
            assertEquals("java.lang.String[]", type.toString());
          }
        });
  }

  @Test
  void isAssignableWithErasure() {
    addProcessor(
        new TestProcessor() {
          @Override
          protected void run() {
            TypeElement listTypeElement = ctx.getMoreElements().getTypeElement(List.class);
            TypeMirror stringType = ctx.getMoreTypes().getTypeMirror(String.class);
            TypeMirror t1 = ctx.getMoreTypes().getDeclaredType(listTypeElement, stringType);
            TypeElement collectionTypeElement =
                ctx.getMoreElements().getTypeElement(Collection.class);
            TypeMirror integerType = ctx.getMoreTypes().getTypeMirror(Integer.class);
            TypeMirror t2 = ctx.getMoreTypes().getDeclaredType(collectionTypeElement, integerType);
            assertFalse(ctx.getMoreTypes().isAssignable(t1, t2));
            assertTrue(ctx.getMoreTypes().isAssignableWithErasure(t1, t2));
          }
        });
  }

  @Test
  void isSameTypeWithErasure_TypeMirror() {
    addProcessor(
        new TestProcessor() {
          @Override
          protected void run() {
            TypeElement listTypeElement = ctx.getMoreElements().getTypeElement(List.class);
            TypeMirror stringType = ctx.getMoreTypes().getTypeMirror(String.class);
            TypeMirror t1 = ctx.getMoreTypes().getDeclaredType(listTypeElement, stringType);
            TypeMirror integerType = ctx.getMoreTypes().getTypeMirror(Integer.class);
            TypeMirror t2 = ctx.getMoreTypes().getDeclaredType(listTypeElement, integerType);
            assertFalse(ctx.getMoreTypes().isSameType(t1, t2));
            assertTrue(ctx.getMoreTypes().isSameTypeWithErasure(t1, t2));
          }
        });
  }

  @Test
  void isSameTypeWithErasure_Class() {
    addProcessor(
        new TestProcessor() {
          @Override
          protected void run() {
            TypeMirror type = ctx.getMoreTypes().getPrimitiveType(TypeKind.INT);
            assertTrue(ctx.getMoreTypes().isSameTypeWithErasure(type, int.class));
          }
        },
        new TestProcessor() {
          @Override
          protected void run() {
            TypeMirror type = ctx.getMoreTypes().getNoType(TypeKind.VOID);
            assertTrue(ctx.getMoreTypes().isSameTypeWithErasure(type, void.class));
          }
        },
        new TestProcessor() {
          @Override
          protected void run() {
            TypeMirror type = ctx.getMoreTypes().getTypeMirror(int[].class);
            assertTrue(ctx.getMoreTypes().isSameTypeWithErasure(type, int[].class));
          }
        },
        new TestProcessor() {
          @Override
          protected void run() {
            TypeMirror type = ctx.getMoreTypes().getTypeMirror(String[].class);
            assertTrue(ctx.getMoreTypes().isSameTypeWithErasure(type, String[].class));
          }
        });
  }
}
