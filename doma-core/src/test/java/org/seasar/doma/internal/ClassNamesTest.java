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
    ClassName className = ClassNames.newEntityDefClassNameBuilder(getClass().getName(), "P", "S");
    assertEquals("org.seasar.doma.internal.PClassNamesTestS", className.toString());
  }

  @Test
  void newEntityDefClassNameBuilder_withDefaultValues() {
    ClassName className = ClassNames.newEntityDefClassNameBuilder(getClass().getName(), "", "_");
    assertEquals("org.seasar.doma.internal.ClassNamesTest_", className.toString());
  }

  @Test
  void newEmbeddableDefClassNameBuilder() {
    ClassName className =
        ClassNames.newEmbeddableDefClassNameBuilder(getClass().getName(), "P", "S");
    assertEquals("org.seasar.doma.internal.PClassNamesTestS", className.toString());
  }

  @Test
  void newEmbeddableDefClassNameBuilder_withDefaultValues() {
    ClassName className =
        ClassNames.newEmbeddableDefClassNameBuilder(getClass().getName(), "", "_");
    assertEquals("org.seasar.doma.internal.ClassNamesTest_", className.toString());
  }
}
