package org.seasar.doma.internal;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class ConventionsTest {

  @Test
  public void testNormalizeBinaryName() throws Exception {
    assertEquals("Ccc", Conventions.normalizeBinaryName("Ccc"));
    assertEquals("aaa.bbb.Ccc", Conventions.normalizeBinaryName("aaa.bbb.Ccc"));
    assertEquals("aaa.bbb.Ccc__Ddd__Eee", Conventions.normalizeBinaryName("aaa.bbb.Ccc$Ddd$Eee"));
  }

  @Test
  public void testToFullMetaName() throws Exception {
    assertEquals("_Ccc", Conventions.toFullMetaName("Ccc"));
    assertEquals("aaa.bbb._Ccc", Conventions.toFullMetaName("aaa.bbb.Ccc"));
    assertEquals("aaa.bbb._Ccc__Ddd__Eee", Conventions.toFullMetaName("aaa.bbb.Ccc$Ddd$Eee"));
  }

  @Test
  public void testToSimpleMetaName() throws Exception {
    assertEquals("_Ccc", Conventions.toSimpleMetaName("Ccc"));
    assertEquals("_Ccc", Conventions.toSimpleMetaName("aaa.bbb.Ccc"));
    assertEquals("_Ccc__Ddd__Eee", Conventions.toSimpleMetaName("aaa.bbb.Ccc$Ddd$Eee"));
  }
}
