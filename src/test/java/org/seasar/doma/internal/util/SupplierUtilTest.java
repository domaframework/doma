package org.seasar.doma.internal.util;

import static org.seasar.doma.internal.util.SupplierUtil.iife;

import junit.framework.TestCase;

public class SupplierUtilTest extends TestCase {

  public void testIife() {
    assertEquals("a", iife(() -> "a"));
  }
}
