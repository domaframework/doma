package org.seasar.doma.internal.util;

import junit.framework.TestCase;

/** @author taedium */
public class StringUtilTest extends TestCase {

  public void testCapitalize() throws Exception {
    assertEquals("Aaa", StringUtil.capitalize("aaa"));
  }

  public void testDecapitalize() throws Exception {
    assertEquals("aAA", StringUtil.decapitalize("AAA"));
  }

  public void testFromSnakeCaseToCamelCase() throws Exception {
    assertEquals("aaaBbbCcc", StringUtil.fromSnakeCaseToCamelCase("AAA_BBB_CCC"));
    assertEquals("aaaBbbCcc", StringUtil.fromSnakeCaseToCamelCase("aaa_bbb_ccc"));
    assertEquals("abc", StringUtil.fromSnakeCaseToCamelCase("ABC"));
    assertEquals("abc", StringUtil.fromSnakeCaseToCamelCase("abc"));
  }

  public void testFromCamelCaseToSnakeCase() throws Exception {
    assertEquals("aaa_bbb_ccc", StringUtil.fromCamelCaseToSnakeCase("aaaBbbCcc"));
    assertEquals("abc", StringUtil.fromCamelCaseToSnakeCase("abc"));
    assertEquals("aa1_bbb_ccc", StringUtil.fromCamelCaseToSnakeCase("aa1BbbCcc"));
  }

  public void testIsWhitespace() throws Exception {
    assertFalse(StringUtil.isWhitespace(""));
    assertTrue(StringUtil.isWhitespace(" "));
    assertTrue(StringUtil.isWhitespace("  "));
    assertTrue(StringUtil.isWhitespace(" \t"));
  }

  public void testTrimWhitespace() throws Exception {
    assertEquals("aaa", StringUtil.trimWhitespace(" aaa "));
    assertEquals("aaa", StringUtil.trimWhitespace("aaa "));
    assertEquals("aaa", StringUtil.trimWhitespace("\raaa\n\t"));
    assertEquals("aaa", StringUtil.trimWhitespace("aaa"));
  }
}
