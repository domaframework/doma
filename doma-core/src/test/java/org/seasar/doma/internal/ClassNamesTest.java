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
    ClassName className = ClassNames.newEmbeddableDescClassName(getClass().getName());
    assertEquals("org.seasar.doma.internal._ClassNamesTest", className.toString());
  }

  @Test
  void newEmbeddableTypeClassName_innerClass() {
    ClassName className = ClassNames.newEmbeddableDescClassName(Inner.class.getName());
    assertEquals("org.seasar.doma.internal._ClassNamesTest__Inner", className.toString());
  }

  @Test
  void newEntityTypeClassName() {
    ClassName className = ClassNames.newEntityDescClassName(getClass().getName());
    assertEquals("org.seasar.doma.internal._ClassNamesTest", className.toString());
  }

  @Test
  void newDomainTypeClassName() {
    ClassName className = ClassNames.newDomainDescClassName(getClass().getName());
    assertEquals("org.seasar.doma.internal._ClassNamesTest", className.toString());
  }

  @Test
  void newExternalDomainTypClassName() {
    ClassName className = ClassNames.newExternalDomainDescClassName(getClass().getName());
    assertEquals("__.org.seasar.doma.internal._ClassNamesTest", className.toString());
  }
}
