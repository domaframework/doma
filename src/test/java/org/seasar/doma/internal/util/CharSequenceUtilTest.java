package org.seasar.doma.internal.util;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class CharSequenceUtilTest {

  @Test
  public void testIsEmpty() throws Exception {
    assertTrue(CharSequenceUtil.isEmpty(null));
    assertTrue(CharSequenceUtil.isEmpty(""));
    assertFalse(CharSequenceUtil.isEmpty(" "));
    assertFalse(CharSequenceUtil.isEmpty(" \t\n\r "));
    assertFalse(CharSequenceUtil.isEmpty("a"));
    assertFalse(CharSequenceUtil.isEmpty(" a "));
  }

  @Test
  public void testIsNotEmpty() throws Exception {
    assertFalse(CharSequenceUtil.isNotEmpty(null));
    assertFalse(CharSequenceUtil.isNotEmpty(""));
    assertTrue(CharSequenceUtil.isNotEmpty(" "));
    assertTrue(CharSequenceUtil.isNotEmpty(" \t\n\r "));
    assertTrue(CharSequenceUtil.isNotEmpty("a"));
    assertTrue(CharSequenceUtil.isNotEmpty(" a "));
  }

  @Test
  public void testIsBlank() throws Exception {
    assertTrue(CharSequenceUtil.isBlank(null));
    assertTrue(CharSequenceUtil.isBlank(""));
    assertTrue(CharSequenceUtil.isBlank(" "));
    assertTrue(CharSequenceUtil.isBlank(" \t\n\r "));
    assertFalse(CharSequenceUtil.isBlank("a"));
    assertFalse(CharSequenceUtil.isBlank(" a "));
  }

  @Test
  public void testIsNotBlank() throws Exception {
    assertFalse(CharSequenceUtil.isNotBlank(null));
    assertFalse(CharSequenceUtil.isNotBlank(""));
    assertFalse(CharSequenceUtil.isNotBlank(" "));
    assertFalse(CharSequenceUtil.isNotBlank(" \t\n\r "));
    assertTrue(CharSequenceUtil.isNotBlank("a"));
    assertTrue(CharSequenceUtil.isNotBlank(" a "));
  }
}
