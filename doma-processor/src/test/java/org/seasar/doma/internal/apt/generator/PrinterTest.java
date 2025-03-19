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
package org.seasar.doma.internal.apt.generator;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.Formatter;
import java.util.List;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.seasar.doma.internal.apt.AbstractCompilerTest;
import org.seasar.doma.internal.apt.RoundContext;
import org.seasar.doma.internal.apt.TestProcessor;

class PrinterTest extends AbstractCompilerTest {

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
          protected void run(RoundContext ctx) {
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
          protected void run(RoundContext ctx) {
            TypeElement typeElement = ctx.getMoreElements().getTypeElement(String.class);
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
          protected void run(RoundContext ctx) {
            TypeElement typeElement = ctx.getMoreElements().getTypeElement(String.class);
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
          protected void run(RoundContext ctx) {
            TypeElement typeElement = ctx.getMoreElements().getTypeElement(testClass);
            VariableElement field =
                ElementFilter.fieldsIn(typeElement.getEnclosedElements()).stream()
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
          protected void run(RoundContext ctx) {
            TypeElement typeElement = ctx.getMoreElements().getTypeElement(String.class);
            TypeMirror typeMirror = typeElement.asType();
            List<Object> list =
                Arrays.asList(
                    String.class,
                    typeElement,
                    typeMirror,
                    Collections.singletonList(Integer.class));

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
          protected void run(RoundContext ctx) {
            Code code = new Code(p -> p.print("%1$s", String.class));
            Formatter formatter = new Formatter();
            Printer printer = new Printer(ctx, formatter);
            printer.print("<%1$s>", code);
            assertEquals("<java.lang.String>", formatter.toString());
          }
        });
  }
}
