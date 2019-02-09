package org.seasar.doma.internal.apt;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Iterator;
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

class ElementsTest extends CompilerSupport {

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
  void getBinaryNameAsString() {
    addProcessor(
        new TestProcessor() {
          @Override
          protected void run() {
            TypeElement typeElement = ctx.getElements().getTypeElement(String.class);
            String binaryName = ctx.getElements().getBinaryNameAsString(typeElement);
            assertEquals("java.lang.String", binaryName);
          }
        });
  }

  @Test
  void getPackageName() {
    addProcessor(
        new TestProcessor() {
          @Override
          protected void run() {
            TypeElement typeElement = ctx.getElements().getTypeElement(String.class);
            String packageName = ctx.getElements().getPackageName(typeElement);
            assertEquals("java.lang", packageName);
          }
        });
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
            assertEquals("arg1", ctx.getElements().getParameterName(p1));
            assertEquals("aaa", ctx.getElements().getParameterName(p2));
          }
        });
  }

  @Test
  void toTypeElement() {
    addProcessor(
        new TestProcessor() {
          @Override
          protected void run() {
            Element element = ctx.getElements().getTypeElement(String.class);
            TypeElement typeElement = ctx.getElements().toTypeElement(element);
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
                ctx.getElements().getTypeElement("org.seasar.doma.internal.apt.ElementsTest");
            assertNotNull(outerTypeElement);
            TypeElement innerTypeElement =
                ctx.getElements().getTypeElement("org.seasar.doma.internal.apt.ElementsTest$Inner");
            assertNotNull(innerTypeElement);
          }
        },
        new TestProcessor() {
          @Override
          protected void run() {
            TypeElement outerTypeElement = ctx.getElements().getTypeElement(ElementsTest.class);
            assertNotNull(outerTypeElement);
            TypeElement innerTypeElement = ctx.getElements().getTypeElement(Inner.class);
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
                ctx.getElements().getAnnotationMirror(method, BeforeEach.class);
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
                ctx.getElements().getAnnotationMirror(method, "org.junit.jupiter.api.BeforeEach");
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
            TypeElement typeElement = ctx.getElements().getTypeElement(String.class);
            ExecutableElement constructor = ctx.getElements().getNoArgConstructor(typeElement);
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
            TypeElement typeElement = ctx.getElements().getTypeElement(Inner.class);
            AnnotationMirror annotationMirror =
                ctx.getElements().getAnnotationMirror(typeElement, MyAnnotation.class);
            Map<String, AnnotationValue> map =
                ctx.getElements().getValuesWithDefaults(annotationMirror);
            assertEquals(3, map.size());
            assertEquals("aaa", map.get("key1").getValue());
            assertEquals("bbb", map.get("key2").getValue());
            assertEquals(2, map.get("key3").getValue());
          }
        });
  }
}
