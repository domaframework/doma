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
package org.seasar.doma.internal.apt.processor;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.seasar.doma.internal.apt.AptIllegalStateException;
import org.seasar.doma.internal.apt.CompilerSupport;
import org.seasar.doma.message.Message;

class ElementProcessorSupportTest extends CompilerSupport {

  @BeforeEach
  void beforeEach() {
    disableCompilationAssertion();
  }

  @AfterEach
  void afterEach() {
    enableCompilationAssertion();
  }

  @Test
  void testAptIllegalStateException() {
    Class<?> target = Person.class;
    addCompilationUnit(target);
    addProcessor(
        new MyAnnotationProcessor(
            __ -> {
              throw new AptIllegalStateException("hoge");
            }));
    try {
      // javac throws an exception, but ecj doesn't.
      compile();
    } catch (Exception ignored) {
    }
    assertMessage(Message.DOMA4039);
  }

  @Test
  void testRuntimeException() {
    Class<?> target = Person.class;
    addCompilationUnit(target);
    addProcessor(
        new MyAnnotationProcessor(
            __ -> {
              throw new NullPointerException("hoge");
            }));
    try {
      // javac throws an exception, but ecj doesn't.
      compile();
    } catch (Exception ignored) {
    }
    assertMessage(Message.DOMA4016);
  }

  @Test
  void testAssertionError() {
    Class<?> target = Person.class;
    addCompilationUnit(target);
    addProcessor(
        new MyAnnotationProcessor(
            __ -> {
              throw new AssertionError("hoge");
            }));
    try {
      // javac throws an exception, but ecj doesn't.
      compile();
    } catch (Exception ignored) {
    }
    assertMessage(Message.DOMA4016);
  }
}
