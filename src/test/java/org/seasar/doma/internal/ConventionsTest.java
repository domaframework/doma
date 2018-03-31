package org.seasar.doma.internal;

import junit.framework.TestCase;

/** @author nakamura-to */
public class ConventionsTest extends TestCase {

  public void testNormalizeBinaryName() throws Exception {
    assertEquals("Ccc", Conventions.normalizeBinaryName("Ccc"));
    assertEquals("aaa.bbb.Ccc", Conventions.normalizeBinaryName("aaa.bbb.Ccc"));
    assertEquals("aaa.bbb.Ccc__Ddd__Eee", Conventions.normalizeBinaryName("aaa.bbb.Ccc$Ddd$Eee"));
  }

  public void testToFullMetaName() throws Exception {
    assertEquals("_Ccc", Conventions.toFullDescClassName("Ccc"));
    assertEquals("aaa.bbb._Ccc", Conventions.toFullDescClassName("aaa.bbb.Ccc"));
    assertEquals("aaa.bbb._Ccc__Ddd__Eee", Conventions.toFullDescClassName("aaa.bbb.Ccc$Ddd$Eee"));
  }
}
