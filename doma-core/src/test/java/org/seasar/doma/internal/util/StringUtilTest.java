package org.seasar.doma.internal.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class StringUtilTest {

  @Test
  public void testCapitalize() throws Exception {
    assertEquals("Aaa", StringUtil.capitalize("aaa"));
  }

  @Test
  public void testDecapitalize() throws Exception {
    assertEquals("aAA", StringUtil.decapitalize("AAA"));
  }

  @Test
  public void testFromSnakeCaseToCamelCase() throws Exception {
    assertEquals("aaaBbbCcc", StringUtil.fromSnakeCaseToCamelCase("AAA_BBB_CCC"));
    assertEquals("aaaBbbCcc", StringUtil.fromSnakeCaseToCamelCase("aaa_bbb_ccc"));
    assertEquals("abc", StringUtil.fromSnakeCaseToCamelCase("ABC"));
    assertEquals("abc", StringUtil.fromSnakeCaseToCamelCase("abc"));
  }

  @Test
  public void testFromCamelCaseToSnakeCase() throws Exception {
    assertEquals("aaa_bbb_ccc", StringUtil.fromCamelCaseToSnakeCase("aaaBbbCcc"));
    assertEquals("abc", StringUtil.fromCamelCaseToSnakeCase("abc"));
    assertEquals("aa1_bbb_ccc", StringUtil.fromCamelCaseToSnakeCase("aa1BbbCcc"));
  }

  @Test
  public void testFromCamelCaseToSnakeCaseWithLenient() throws Exception {
    assertEquals("aaa_bbb_ccc", StringUtil.fromCamelCaseToSnakeCaseWithLenient("aaaBbbCcc"));
    assertEquals("abc", StringUtil.fromCamelCaseToSnakeCaseWithLenient("abc"));
    assertEquals("aa1bbb_ccc", StringUtil.fromCamelCaseToSnakeCaseWithLenient("aa1BbbCcc"));
  }

  @Test
  public void testIsWhitespace() throws Exception {
    assertFalse(StringUtil.isWhitespace(""));
    assertTrue(StringUtil.isWhitespace(" "));
    assertTrue(StringUtil.isWhitespace("  "));
    assertTrue(StringUtil.isWhitespace(" \t"));
  }

  @Test
  public void testTrimWhitespace() throws Exception {
    assertEquals("aaa", StringUtil.trimWhitespace(" aaa "));
    assertEquals("aaa", StringUtil.trimWhitespace("aaa "));
    assertEquals("aaa", StringUtil.trimWhitespace("\raaa\n\t"));
    assertEquals("aaa", StringUtil.trimWhitespace("aaa"));
  }
}
