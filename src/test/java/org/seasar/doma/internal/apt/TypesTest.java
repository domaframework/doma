package org.seasar.doma.internal.apt;

import static org.junit.jupiter.api.Assertions.*;

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

class TypesTest extends CompilerSupport {

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
            TypeMirror type = ctx.getTypes().getTypeMirror(String.class);
            TypeElement typeElement = ctx.getTypes().toTypeElement(type);
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
            TypeMirror type = ctx.getTypes().getTypeMirror(String.class);
            DeclaredType declaredType = ctx.getTypes().toDeclaredType(type);
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
            TypeElement typeElement = ctx.getElements().getTypeElement(List.class);
            TypeParameterElement typeParameterElement =
                typeElement.getTypeParameters().iterator().next();
            TypeVariable typeVariable =
                ctx.getTypes().toTypeVariable(typeParameterElement.asType());
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
            TypeMirror componentType = ctx.getTypes().getTypeMirror(String.class);
            TypeMirror type = ctx.getTypes().getArrayType(componentType);
            ArrayType arrayType = ctx.getTypes().toArrayType(type);
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
            TypeMirror componentType = ctx.getTypes().getTypeMirror(String.class);
            TypeMirror type = ctx.getTypes().getArrayType(componentType);
            assertTrue(ctx.getTypes().isArray(type));
          }
        });
  }

  @Test
  void getTypeName() {
    addProcessor(
        new TestProcessor() {
          @Override
          protected void run() {
            TypeMirror intType = ctx.getTypes().getPrimitiveType(TypeKind.INT);
            assertEquals("int", ctx.getTypes().getTypeName(intType));
            TypeMirror listType = ctx.getElements().getTypeElement(List.class).asType();
            assertEquals("java.util.List<E>", ctx.getTypes().getTypeName(listType));
          }
        },
        new TestProcessor() {
          @Override
          protected void run() {
            TypeElement typeElement = ctx.getElements().getTypeElement(NumberList.class);
            TypeParameterElement typeParameterElement =
                typeElement.getTypeParameters().iterator().next();
            assertEquals("E", ctx.getTypes().getTypeName(typeParameterElement.asType()));
          }
        });
  }

  @Test
  void getBoxedTypeName() {
    addProcessor(
        new TestProcessor() {
          @Override
          protected void run() {
            TypeMirror booleanType = ctx.getTypes().getPrimitiveType(TypeKind.BOOLEAN);
            assertEquals("java.lang.Boolean", ctx.getTypes().getBoxedTypeName(booleanType));
            TypeMirror byteType = ctx.getTypes().getPrimitiveType(TypeKind.BYTE);
            assertEquals("java.lang.Byte", ctx.getTypes().getBoxedTypeName(byteType));
            TypeMirror shortType = ctx.getTypes().getPrimitiveType(TypeKind.SHORT);
            assertEquals("java.lang.Short", ctx.getTypes().getBoxedTypeName(shortType));
            TypeMirror intType = ctx.getTypes().getPrimitiveType(TypeKind.INT);
            assertEquals("java.lang.Integer", ctx.getTypes().getBoxedTypeName(intType));
            TypeMirror longType = ctx.getTypes().getPrimitiveType(TypeKind.LONG);
            assertEquals("java.lang.Long", ctx.getTypes().getBoxedTypeName(longType));
            TypeMirror floatType = ctx.getTypes().getPrimitiveType(TypeKind.FLOAT);
            assertEquals("java.lang.Float", ctx.getTypes().getBoxedTypeName(floatType));
            TypeMirror doubleType = ctx.getTypes().getPrimitiveType(TypeKind.DOUBLE);
            assertEquals("java.lang.Double", ctx.getTypes().getBoxedTypeName(doubleType));
            TypeMirror charType = ctx.getTypes().getPrimitiveType(TypeKind.CHAR);
            assertEquals("java.lang.Character", ctx.getTypes().getBoxedTypeName(charType));
            TypeMirror listType = ctx.getElements().getTypeElement(List.class).asType();
            assertEquals("java.util.List<E>", ctx.getTypes().getTypeName(listType));
          }
        });
  }

  @Test
  void getTypeParameterName() {
    addProcessor(
        new TestProcessor() {
          @Override
          protected void run() {
            TypeElement typeElement = ctx.getElements().getTypeElement(NumberList.class);
            TypeParameterElement typeParameterElement =
                typeElement.getTypeParameters().iterator().next();
            assertEquals(
                "E extends java.lang.Number",
                ctx.getTypes().getTypeParameterName(typeParameterElement.asType()));
          }
        });
  }

  @Test
  void boxIfPrimitive() {
    addProcessor(
        new TestProcessor() {
          @Override
          protected void run() {
            TypeMirror type = ctx.getTypes().getPrimitiveType(TypeKind.INT);
            TypeMirror t1 = ctx.getTypes().boxIfPrimitive(type);
            TypeMirror t2 = ctx.getTypes().getTypeMirror(Integer.class);
            assertTrue(ctx.getTypes().isSameType(t1, t2));
          }
        },
        new TestProcessor() {
          @Override
          protected void run() {
            TypeMirror t1 = ctx.getTypes().getTypeMirror(Integer.class);
            TypeMirror t2 = ctx.getTypes().boxIfPrimitive(t1);
            assertTrue(ctx.getTypes().isSameType(t1, t2));
          }
        });
  }

  @Test
  void getTypeMirror() {
    addProcessor(
        new TestProcessor() {
          @Override
          protected void run() {
            TypeMirror type = ctx.getTypes().getTypeMirror(String.class);
            assertNotNull(type);
            assertEquals("java.lang.String", type.toString());
          }
        });
  }

  @Test
  void isAssignableWithErasure() {
    addProcessor(
        new TestProcessor() {
          @Override
          protected void run() {
            TypeElement listTypeElement = ctx.getElements().getTypeElement(List.class);
            TypeMirror stringType = ctx.getTypes().getTypeMirror(String.class);
            TypeMirror t1 = ctx.getTypes().getDeclaredType(listTypeElement, stringType);
            TypeElement collectionTypeElement = ctx.getElements().getTypeElement(Collection.class);
            TypeMirror integerType = ctx.getTypes().getTypeMirror(Integer.class);
            TypeMirror t2 = ctx.getTypes().getDeclaredType(collectionTypeElement, integerType);
            assertFalse(ctx.getTypes().isAssignable(t1, t2));
            assertTrue(ctx.getTypes().isAssignableWithErasure(t1, t2));
          }
        });
  }

  @Test
  void isSameTypeWithErasure() {
    addProcessor(
        new TestProcessor() {
          @Override
          protected void run() {
            TypeElement listTypeElement = ctx.getElements().getTypeElement(List.class);
            TypeMirror stringType = ctx.getTypes().getTypeMirror(String.class);
            TypeMirror t1 = ctx.getTypes().getDeclaredType(listTypeElement, stringType);
            TypeMirror integerType = ctx.getTypes().getTypeMirror(Integer.class);
            TypeMirror t2 = ctx.getTypes().getDeclaredType(listTypeElement, integerType);
            assertFalse(ctx.getTypes().isSameType(t1, t2));
            assertTrue(ctx.getTypes().isSameTypeWithErasure(t1, t2));
          }
        });
  }
}
