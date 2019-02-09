package org.seasar.doma.internal;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class ConventionsTest {

  private static class Inner {}

  @Test
  void normalizeBinaryName() {
    assertEquals("Ccc", Conventions.normalizeBinaryName("Ccc"));
    assertEquals("aaa.bbb.Ccc", Conventions.normalizeBinaryName("aaa.bbb.Ccc"));
    assertEquals("aaa.bbb.Ccc__Ddd__Eee", Conventions.normalizeBinaryName("aaa.bbb.Ccc$Ddd$Eee"));
  }

  @Test
  void newEmbeddableTypeClassName() {
    ClassName className = Conventions.newEmbeddableTypeClassName(getClass().getName());
    assertEquals("org.seasar.doma.internal._ConventionsTest", className.toString());
  }

  @Test
  void newEmbeddableTypeClassName_innerClass() {
    ClassName className = Conventions.newEmbeddableTypeClassName(Inner.class.getName());
    assertEquals("org.seasar.doma.internal._ConventionsTest__Inner", className.toString());
  }

  @Test
  void newEntityTypeClassName() {
    ClassName className = Conventions.newEntityTypeClassName(getClass().getName());
    assertEquals("org.seasar.doma.internal._ConventionsTest", className.toString());
  }

  @Test
  void newDomainTypeClassName() {
    ClassName className = Conventions.newDomainTypeClassName(getClass().getName());
    assertEquals("org.seasar.doma.internal._ConventionsTest", className.toString());
  }

  @Test
  void newExternalDomainTypClassName() {
    ClassName className = Conventions.newExternalDomainTypClassName(getClass().getName());
    assertEquals("__.org.seasar.doma.internal._ConventionsTest", className.toString());
  }
}
