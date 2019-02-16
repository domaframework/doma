package org.seasar.doma.internal.apt.generator;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.Formatter;
import java.util.List;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.seasar.doma.internal.apt.CompilerSupport;
import org.seasar.doma.internal.apt.TestProcessor;

class PrinterTest extends CompilerSupport {

  @SuppressWarnings("unused")
  private Integer field;

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
  void print_Class() {
    addProcessor(
        new TestProcessor() {
          @Override
          protected void run() {
            Formatter formatter = new Formatter();
            Printer printer = new Printer(ctx, formatter);
            printer.print("%1$s", String.class);
            assertEquals("java.lang.String", formatter.toString());
          }
        });
  }

  @Test
  void print_TypeMirror() {
    addProcessor(
        new TestProcessor() {
          @Override
          protected void run() {
            TypeElement typeElement = ctx.getElements().getTypeElement(String.class);
            TypeMirror typeMirror = typeElement.asType();
            Formatter formatter = new Formatter();
            Printer printer = new Printer(ctx, formatter);
            printer.print("%1$s", typeMirror);
            assertEquals("java.lang.String", formatter.toString());
          }
        });
  }

  @Test
  void print_TypeElement() {
    addProcessor(
        new TestProcessor() {
          @Override
          protected void run() {
            TypeElement typeElement = ctx.getElements().getTypeElement(String.class);
            Formatter formatter = new Formatter();
            Printer printer = new Printer(ctx, formatter);
            printer.print("%1$s", typeElement);
            assertEquals("java.lang.String", formatter.toString());
          }
        });
  }

  @Test
  void print_Element() {
    Class<?> testClass = getClass();
    addProcessor(
        new TestProcessor() {
          @Override
          protected void run() {
            TypeElement typeElement = ctx.getElements().getTypeElement(testClass);
            VariableElement field =
                ElementFilter.fieldsIn(typeElement.getEnclosedElements())
                    .stream()
                    .filter(f -> f.getSimpleName().contentEquals("field"))
                    .findFirst()
                    .orElseThrow(AssertionError::new);
            Formatter formatter = new Formatter();
            Printer printer = new Printer(ctx, formatter);
            printer.print("%1$s", field);
            assertEquals("field", formatter.toString());
          }
        });
  }

  @Test
  void print_List() {
    addProcessor(
        new TestProcessor() {
          @Override
          protected void run() {
            TypeElement typeElement = ctx.getElements().getTypeElement(String.class);
            TypeMirror typeMirror = typeElement.asType();
            List<Object> list =
                Arrays.asList(String.class, typeElement, typeMirror, Arrays.asList(Integer.class));

            Formatter formatter = new Formatter();
            Printer printer = new Printer(ctx, formatter);
            printer.print("%1$s", list);
            assertEquals(
                "java.lang.String, java.lang.String, java.lang.String, java.lang.Integer",
                formatter.toString());
          }
        });
  }

  @Test
  void print_Code() {
    addProcessor(
        new TestProcessor() {
          @Override
          protected void run() {
            Code code =
                new Code(
                    p -> {
                      p.print("%1$s", String.class);
                    });
            Formatter formatter = new Formatter();
            Printer printer = new Printer(ctx, formatter);
            printer.print("<%1$s>", code);
            assertEquals("<java.lang.String>", formatter.toString());
          }
        });
  }
}
