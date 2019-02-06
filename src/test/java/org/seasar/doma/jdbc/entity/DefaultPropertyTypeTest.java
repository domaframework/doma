package org.seasar.doma.jdbc.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;
import org.seasar.doma.wrapper.IntegerWrapper;
import org.seasar.doma.wrapper.StringWrapper;

public class DefaultPropertyTypeTest {

  @SuppressWarnings("unused")
  private String hoge;

  @SuppressWarnings("unused")
  private Foo foo;

  @SuppressWarnings("unused")
  private int primitiveInt;

  @SuppressWarnings("unused")
  private Integer integer;

  @Test
  public void testIsQuoteRequired_true() throws Exception {
    boolean isQuoteRequired = true;
    DefaultPropertyType<Object, DefaultPropertyTypeTest, String, Object> propertyType =
        new DefaultPropertyType<>(
            DefaultPropertyTypeTest.class,
            String.class,
            String.class,
            () -> new StringWrapper(),
            null,
            null,
            "hoge",
            "hoge",
            NamingType.NONE,
            true,
            true,
            isQuoteRequired);
    assertEquals("hoge", propertyType.getColumnName((n, t) -> t));
    assertEquals("[hoge]", propertyType.getColumnName((n, t) -> t, s -> "[" + s + "]"));
  }

  @Test
  public void testIsQuoteRequired_false() throws Exception {
    boolean isQuoteRequired = false;
    DefaultPropertyType<Object, DefaultPropertyTypeTest, String, Object> propertyType =
        new DefaultPropertyType<>(
            DefaultPropertyTypeTest.class,
            String.class,
            String.class,
            () -> new StringWrapper(),
            null,
            null,
            "hoge",
            "hoge",
            NamingType.NONE,
            true,
            true,
            isQuoteRequired);
    assertEquals("hoge", propertyType.getColumnName((n, t) -> t));
    assertEquals("hoge", propertyType.getColumnName((n, t) -> t, s -> "[" + s + "]"));
  }

  @Test
  public void testGetColumnName_naming_columnNameDefined() throws Exception {
    DefaultPropertyType<Object, DefaultPropertyTypeTest, String, Object> propertyType =
        new DefaultPropertyType<>(
            DefaultPropertyTypeTest.class,
            String.class,
            String.class,
            () -> new StringWrapper(),
            null,
            null,
            "hoge",
            "foo",
            NamingType.UPPER_CASE,
            true,
            true,
            false);
    assertEquals("foo", propertyType.getColumnName((namingType, text) -> namingType.apply(text)));
  }

  @Test
  public void testGetColumnName_columnDefined() throws Exception {
    DefaultPropertyType<Object, DefaultPropertyTypeTest, String, Object> propertyType =
        new DefaultPropertyType<>(
            DefaultPropertyTypeTest.class,
            String.class,
            String.class,
            () -> new StringWrapper(),
            null,
            null,
            "hoge",
            "foo",
            NamingType.UPPER_CASE,
            true,
            true,
            false);
    assertEquals("foo", propertyType.getColumnName());
  }

  @Test
  public void testGetColumnName_columnNotDefined() throws Exception {
    DefaultPropertyType<Object, DefaultPropertyTypeTest, String, Object> propertyType =
        new DefaultPropertyType<>(
            DefaultPropertyTypeTest.class,
            String.class,
            String.class,
            () -> new StringWrapper(),
            null,
            null,
            "hoge",
            "",
            NamingType.UPPER_CASE,
            true,
            true,
            false);
    assertEquals("HOGE", propertyType.getColumnName());
  }

  @Test
  public void testGetColumnName_columnNotDefined_embeddableProeprty() throws Exception {
    DefaultPropertyType<Object, DefaultPropertyTypeTest, String, Object> propertyType =
        new DefaultPropertyType<>(
            DefaultPropertyTypeTest.class,
            String.class,
            String.class,
            () -> new StringWrapper(),
            null,
            null,
            "foo.hoge",
            "",
            NamingType.UPPER_CASE,
            true,
            true,
            false);
    assertEquals("HOGE", propertyType.getColumnName());
  }

  @Test
  public void testGetColumnName_quote_quoteRequired() throws Exception {
    DefaultPropertyType<Object, DefaultPropertyTypeTest, String, Object> propertyType =
        new DefaultPropertyType<>(
            DefaultPropertyTypeTest.class,
            String.class,
            String.class,
            () -> new StringWrapper(),
            null,
            null,
            "hoge",
            "",
            NamingType.UPPER_CASE,
            true,
            true,
            true);
    assertEquals("[HOGE]", propertyType.getColumnName(text -> "[" + text + "]"));
  }

  @Test
  public void testGetColumnName_quote_quoteNotRequired() throws Exception {
    DefaultPropertyType<Object, DefaultPropertyTypeTest, String, Object> propertyType =
        new DefaultPropertyType<>(
            DefaultPropertyTypeTest.class,
            String.class,
            String.class,
            () -> new StringWrapper(),
            null,
            null,
            "hoge",
            "",
            NamingType.UPPER_CASE,
            true,
            true,
            false);
    assertEquals("HOGE", propertyType.getColumnName(text -> "[" + text + "]"));
  }

  @Test
  public void testGetColumnName_naiming_columnNotDefined() throws Exception {
    DefaultPropertyType<Object, DefaultPropertyTypeTest, String, Object> propertyType =
        new DefaultPropertyType<>(
            DefaultPropertyTypeTest.class,
            String.class,
            String.class,
            () -> new StringWrapper(),
            null,
            null,
            "hoge",
            "",
            NamingType.UPPER_CASE,
            true,
            true,
            false);
    assertEquals("HOGE", propertyType.getColumnName((namingType, text) -> namingType.apply(text)));
  }

  @Test
  public void testGetColumnName_naiming_quote_quoteRequired() throws Exception {
    DefaultPropertyType<Object, DefaultPropertyTypeTest, String, Object> propertyType =
        new DefaultPropertyType<>(
            DefaultPropertyTypeTest.class,
            String.class,
            String.class,
            () -> new StringWrapper(),
            null,
            null,
            "hoge",
            "",
            NamingType.UPPER_CASE,
            true,
            true,
            true);
    assertEquals(
        "[HOGE]",
        propertyType.getColumnName(
            (namingType, text) -> namingType.apply(text), text -> "[" + text + "]"));
  }

  @Test
  public void testGetColumnName_naiming_quote_quoteNotRequired() throws Exception {
    DefaultPropertyType<Object, DefaultPropertyTypeTest, String, Object> propertyType =
        new DefaultPropertyType<>(
            DefaultPropertyTypeTest.class,
            String.class,
            String.class,
            () -> new StringWrapper(),
            null,
            null,
            "hoge",
            "",
            NamingType.UPPER_CASE,
            true,
            true,
            false);
    assertEquals(
        "HOGE",
        propertyType.getColumnName(
            (namingType, text) -> namingType.apply(text), text -> "[" + text + "]"));
  }

  @Test
  public void testPrimitivePropertyDefaultValue() throws Exception {
    DefaultPropertyType<Object, DefaultPropertyTypeTest, Integer, Object> propertyType =
        new DefaultPropertyType<>(
            DefaultPropertyTypeTest.class,
            Integer.class,
            Integer.class,
            () -> new IntegerWrapper(),
            null,
            null,
            "primitiveInt",
            "primitiveInt",
            NamingType.NONE,
            true,
            true,
            false);
    Property<DefaultPropertyTypeTest, Integer> property = propertyType.createProperty();
    assertEquals(0, property.get());
  }

  @Test
  public void testWrapperPropertyDefaultValue() throws Exception {
    DefaultPropertyType<Object, DefaultPropertyTypeTest, Integer, Object> propertyType =
        new DefaultPropertyType<>(
            DefaultPropertyTypeTest.class,
            Integer.class,
            Integer.class,
            () -> new IntegerWrapper(),
            null,
            null,
            "integer",
            "integer",
            NamingType.NONE,
            true,
            true,
            false);
    Property<DefaultPropertyTypeTest, Integer> property = propertyType.createProperty();
    assertNull(property.get());
  }

  public static class Foo {
    String hoge;
  }
}
