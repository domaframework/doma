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
package org.seasar.doma.internal;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class ClassNamesTest {

  private static class Inner {}

  @Test
  void normalizeBinaryName() {
    assertEquals("Ccc", ClassNames.normalizeBinaryName("Ccc"));
    assertEquals("aaa.bbb.Ccc", ClassNames.normalizeBinaryName("aaa.bbb.Ccc"));
    assertEquals("aaa.bbb.Ccc__Ddd__Eee", ClassNames.normalizeBinaryName("aaa.bbb.Ccc$Ddd$Eee"));
  }

  @Test
  void newEmbeddableTypeClassName() {
    ClassName className = ClassNames.newEmbeddableTypeClassName(getClass().getName());
    assertEquals("org.seasar.doma.internal._ClassNamesTest", className.toString());
  }

  @Test
  void newEmbeddableTypeClassName_innerClass() {
    ClassName className = ClassNames.newEmbeddableTypeClassName(Inner.class.getName());
    assertEquals("org.seasar.doma.internal._ClassNamesTest__Inner", className.toString());
  }

  @Test
  void newEntityTypeClassName() {
    ClassName className = ClassNames.newEntityTypeClassName(getClass().getName());
    assertEquals("org.seasar.doma.internal._ClassNamesTest", className.toString());
  }

  @Test
  void newDomainTypeClassName() {
    ClassName className = ClassNames.newDomainTypeClassName(getClass().getName());
    assertEquals("org.seasar.doma.internal._ClassNamesTest", className.toString());
  }

  @Test
  void newExternalDomainTypClassName() {
    ClassName className = ClassNames.newExternalDomainTypeClassName(getClass().getName());
    assertEquals("__.org.seasar.doma.internal._ClassNamesTest", className.toString());
  }

  @Test
  void newEntityDefClassNameBuilder() {
    ClassName className =
        ClassNames.newEntityMetamodelClassNameBuilder(getClass().getName(), "P", "S");
    assertEquals("org.seasar.doma.internal.PClassNamesTestS", className.toString());
  }

  @Test
  void newEntityDefClassNameBuilder_withDefaultValues() {
    ClassName className =
        ClassNames.newEntityMetamodelClassNameBuilder(getClass().getName(), "", "_");
    assertEquals("org.seasar.doma.internal.ClassNamesTest_", className.toString());
  }
}
