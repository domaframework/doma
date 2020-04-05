package org.seasar.doma.internal.apt;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.seasar.doma.ParameterName;
import org.seasar.doma.internal.apt.def.TypeParametersDef;

class MoreElementsTest extends CompilerSupport {

  @SuppressWarnings("unused")
  private void test(String arg1, @ParameterName("aaa") String arg2) {}

  @interface MyAnnotation {
    String key1();

    @SuppressWarnings("unused")
    String key2() default "bbb";

    int key3() default 1;
  }

  @MyAnnotation(key1 = "aaa", key3 = 2)
  private class Inner {}

  @SuppressWarnings("unused")
  private class ParameterizedClass<T, U extends Number> {}

  @SuppressWarnings("unused")
  private class BoundType<T extends Number> {}

  @SuppressWarnings("unused")
  private class IntersectionType<T extends Number & Runnable> {}

  @SuppressWarnings("unused")
  private class ReferredTypeVar<T extends Number, S extends List<T>> {}

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
  void getParameterName() {
    Class<?> testClass = getClass();
    addProcessor(
        new TestProcessor() {
          @Override
          protected void run() {
            ExecutableElement method =
                createMethodElement(testClass, "test", String.class, String.class);
            Iterator<? extends VariableElement> parameters = method.getParameters().iterator();
            VariableElement p1 = parameters.next();
            VariableElement p2 = parameters.next();
            assertEquals("arg1", ctx.getMoreElements().getParameterName(p1));
            assertEquals("aaa", ctx.getMoreElements().getParameterName(p2));
          }
        });
  }

  @Test
  void toTypeElement() {
    addProcessor(
        new TestProcessor() {
          @Override
          protected void run() {
            Element element = ctx.getMoreElements().getTypeElement(String.class);
            TypeElement typeElement = ctx.getMoreElements().toTypeElement(element);
            assertNotNull(typeElement);
          }
        });
  }

  @Test
  void getTypeElement() {
    addProcessor(
        new TestProcessor() {
          @Override
          protected void run() {
            TypeElement outerTypeElement =
                ctx.getMoreElements()
                    .getTypeElementFromBinaryName("org.seasar.doma.internal.apt.MoreElementsTest");
            assertNotNull(outerTypeElement);
            TypeElement innerTypeElement =
                ctx.getMoreElements()
                    .getTypeElementFromBinaryName(
                        "org.seasar.doma.internal.apt.MoreElementsTest$Inner");
            assertNotNull(innerTypeElement);
          }
        },
        new TestProcessor() {
          @Override
          protected void run() {
            TypeElement outerTypeElement =
                ctx.getMoreElements().getTypeElement(MoreElementsTest.class);
            assertNotNull(outerTypeElement);
            TypeElement innerTypeElement = ctx.getMoreElements().getTypeElement(Inner.class);
            assertNotNull(innerTypeElement);
          }
        });
  }

  @Test
  void getAnnotationMirror() {
    Class<?> testClass = getClass();
    addProcessor(
        new TestProcessor() {
          @Override
          protected void run() {
            ExecutableElement method = createMethodElement(testClass, "beforeEach");
            AnnotationMirror annotationMirror =
                ctx.getMoreElements().getAnnotationMirror(method, BeforeEach.class);
            assertNotNull(annotationMirror);
            assertEquals(
                "org.junit.jupiter.api.BeforeEach",
                annotationMirror.getAnnotationType().toString());
          }
        },
        new TestProcessor() {
          @Override
          protected void run() {
            ExecutableElement method = createMethodElement(testClass, "beforeEach");
            AnnotationMirror annotationMirror =
                ctx.getMoreElements()
                    .getAnnotationMirror(method, "org.junit.jupiter.api.BeforeEach");
            assertNotNull(annotationMirror);
            assertEquals(
                "org.junit.jupiter.api.BeforeEach",
                annotationMirror.getAnnotationType().toString());
          }
        });
  }

  @Test
  void getNoArgConstructor() {
    addProcessor(
        new TestProcessor() {
          @Override
          protected void run() {
            TypeElement typeElement = ctx.getMoreElements().getTypeElement(String.class);
            ExecutableElement constructor = ctx.getMoreElements().getNoArgConstructor(typeElement);
            assertNotNull(constructor);
            assertTrue(constructor.getParameters().isEmpty());
          }
        });
  }

  @Test
  void getValuesWithDefaults() {
    addProcessor(
        new TestProcessor() {
          @Override
          protected void run() {
            TypeElement typeElement = ctx.getMoreElements().getTypeElement(Inner.class);
            AnnotationMirror annotationMirror =
                ctx.getMoreElements().getAnnotationMirror(typeElement, MyAnnotation.class);
            Map<String, AnnotationValue> map =
                ctx.getMoreElements().getValuesWithDefaults(annotationMirror);
            assertEquals(3, map.size());
            assertEquals("aaa", map.get("key1").getValue());
            assertEquals("bbb", map.get("key2").getValue());
            assertEquals(2, map.get("key3").getValue());
          }
        });
  }

  @Test
  void getTypeParametersDef() {
    addProcessor(
        new TestProcessor() {
          @Override
          protected void run() {
            TypeElement typeElement =
                ctx.getMoreElements().getTypeElement(ParameterizedClass.class);
            TypeParametersDef def = ctx.getMoreElements().getTypeParametersDef(typeElement);
            assertIterableEquals(Arrays.asList("T", "U"), def.getTypeVariables());
            assertIterableEquals(
                Arrays.asList("T", "U extends java.lang.Number"), def.getTypeParameters());
          }
        });
  }

  @Test
  void getTypeParameterName() {
    addProcessor(
        new TestProcessor() {
          @Override
          protected void run() {
            TypeElement typeElement = ctx.getMoreElements().getTypeElement(List.class);
            List<String> names =
                ctx.getMoreElements().getTypeParameterNames(typeElement.getTypeParameters());
            assertIterableEquals(Collections.singletonList("E"), names);
          }
        },
        new TestProcessor() {
          @Override
          protected void run() {
            TypeElement typeElement = ctx.getMoreElements().getTypeElement(BoundType.class);
            List<String> names =
                ctx.getMoreElements().getTypeParameterNames(typeElement.getTypeParameters());
            assertIterableEquals(Collections.singletonList("T extends java.lang.Number"), names);
          }
        },
        new TestProcessor() {
          @Override
          protected void run() {
            TypeElement typeElement = ctx.getMoreElements().getTypeElement(IntersectionType.class);
            List<String> names =
                ctx.getMoreElements().getTypeParameterNames(typeElement.getTypeParameters());
            assertIterableEquals(
                Collections.singletonList("T extends java.lang.Number&java.lang.Runnable"), names);
          }
        },
        new TestProcessor() {
          @Override
          protected void run() {
            TypeElement typeElement = ctx.getMoreElements().getTypeElement(Enum.class);
            List<String> names =
                ctx.getMoreElements().getTypeParameterNames(typeElement.getTypeParameters());
            assertIterableEquals(Collections.singletonList("E extends java.lang.Enum<E>"), names);
          }
        },
        new TestProcessor() {
          @Override
          protected void run() {
            TypeElement typeElement = ctx.getMoreElements().getTypeElement(ReferredTypeVar.class);
            List<String> names =
                ctx.getMoreElements().getTypeParameterNames(typeElement.getTypeParameters());
            assertIterableEquals(
                Arrays.asList("T extends java.lang.Number", "S extends java.util.List<T>"), names);
          }
        });
  }
}
