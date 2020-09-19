package org.seasar.doma.jdbc.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;
import org.seasar.doma.internal.jdbc.scalar.BasicScalar;
import org.seasar.doma.wrapper.IntegerWrapper;
import org.seasar.doma.wrapper.PrimitiveIntWrapper;
import org.seasar.doma.wrapper.StringWrapper;

public class DefaultPropertyTypeTest {

  private String hoge;

  private Foo foo;

  private int primitiveInt;

  private Integer integer;

  @SuppressWarnings("ConstantConditions")
  @Test
  public void testIsQuoteRequired_true() {
    boolean isQuoteRequired = true;
    DefaultPropertyType<DefaultPropertyTypeTest, String, String> propertyType =
        new DefaultPropertyType<>(
            DefaultPropertyTypeTest.class,
            () -> new BasicScalar<>(StringWrapper::new),
            "hoge",
            "hoge",
            NamingType.NONE,
            true,
            true,
            isQuoteRequired);
    assertEquals("hoge", propertyType.getColumnName((n, t) -> t));
    assertEquals("[hoge]", propertyType.getColumnName((n, t) -> t, s -> "[" + s + "]"));
  }

  @SuppressWarnings("ConstantConditions")
  @Test
  public void testIsQuoteRequired_false() {
    boolean isQuoteRequired = false;
    DefaultPropertyType<DefaultPropertyTypeTest, String, String> propertyType =
        new DefaultPropertyType<>(
            DefaultPropertyTypeTest.class,
            () -> new BasicScalar<>(StringWrapper::new),
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
  public void testGetColumnName_naming_columnNameDefined() {
    DefaultPropertyType<DefaultPropertyTypeTest, String, String> propertyType =
        new DefaultPropertyType<>(
            DefaultPropertyTypeTest.class,
            () -> new BasicScalar<>(StringWrapper::new),
            "hoge",
            "foo",
            NamingType.UPPER_CASE,
            true,
            true,
            false);
    assertEquals("foo", propertyType.getColumnName(NamingType::apply));
  }

  @Test
  public void testGetColumnName_columnDefined() {
    DefaultPropertyType<DefaultPropertyTypeTest, String, String> propertyType =
        new DefaultPropertyType<>(
            DefaultPropertyTypeTest.class,
            () -> new BasicScalar<>(StringWrapper::new),
            "hoge",
            "foo",
            NamingType.UPPER_CASE,
            true,
            true,
            false);
    assertEquals("foo", propertyType.getColumnName());
  }

  @Test
  public void testGetColumnName_columnNotDefined() {
    DefaultPropertyType<DefaultPropertyTypeTest, String, String> propertyType =
        new DefaultPropertyType<>(
            DefaultPropertyTypeTest.class,
            () -> new BasicScalar<>(StringWrapper::new),
            "hoge",
            "",
            NamingType.UPPER_CASE,
            true,
            true,
            false);
    assertEquals("HOGE", propertyType.getColumnName());
  }

  @Test
  public void testGetColumnName_columnNotDefined_embeddableProperty() {
    DefaultPropertyType<DefaultPropertyTypeTest, String, String> propertyType =
        new DefaultPropertyType<>(
            DefaultPropertyTypeTest.class,
            () -> new BasicScalar<>(StringWrapper::new),
            "foo.hoge",
            "",
            NamingType.UPPER_CASE,
            true,
            true,
            false);
    assertEquals("HOGE", propertyType.getColumnName());
  }

  @Test
  public void testGetColumnName_quote_quoteRequired() {
    DefaultPropertyType<DefaultPropertyTypeTest, String, String> propertyType =
        new DefaultPropertyType<>(
            DefaultPropertyTypeTest.class,
            () -> new BasicScalar<>(StringWrapper::new),
            "hoge",
            "",
            NamingType.UPPER_CASE,
            true,
            true,
            true);
    assertEquals("[HOGE]", propertyType.getColumnName(text -> "[" + text + "]"));
  }

  @Test
  public void testGetColumnName_quote_quoteNotRequired() {
    DefaultPropertyType<DefaultPropertyTypeTest, String, String> propertyType =
        new DefaultPropertyType<>(
            DefaultPropertyTypeTest.class,
            () -> new BasicScalar<>(StringWrapper::new),
            "hoge",
            "",
            NamingType.UPPER_CASE,
            true,
            true,
            false);
    assertEquals("HOGE", propertyType.getColumnName(text -> "[" + text + "]"));
  }

  @Test
  public void testGetColumnName_naming_columnNotDefined() {
    DefaultPropertyType<DefaultPropertyTypeTest, String, String> propertyType =
        new DefaultPropertyType<>(
            DefaultPropertyTypeTest.class,
            () -> new BasicScalar<>(StringWrapper::new),
            "hoge",
            "",
            NamingType.UPPER_CASE,
            true,
            true,
            false);
    assertEquals("HOGE", propertyType.getColumnName(NamingType::apply));
  }

  @Test
  public void testGetColumnName_naming_quote_quoteRequired() {
    DefaultPropertyType<DefaultPropertyTypeTest, String, String> propertyType =
        new DefaultPropertyType<>(
            DefaultPropertyTypeTest.class,
            () -> new BasicScalar<>(StringWrapper::new),
            "hoge",
            "",
            NamingType.UPPER_CASE,
            true,
            true,
            true);
    assertEquals("[HOGE]", propertyType.getColumnName(NamingType::apply, text -> "[" + text + "]"));
  }

  @Test
  public void testGetColumnName_naming_quote_quoteNotRequired() {
    DefaultPropertyType<DefaultPropertyTypeTest, String, String> propertyType =
        new DefaultPropertyType<>(
            DefaultPropertyTypeTest.class,
            () -> new BasicScalar<>(StringWrapper::new),
            "hoge",
            "",
            NamingType.UPPER_CASE,
            true,
            true,
            false);
    assertEquals("HOGE", propertyType.getColumnName(NamingType::apply, text -> "[" + text + "]"));
  }

  @Test
  public void testPrimitivePropertyDefaultValue() {
    DefaultPropertyType<DefaultPropertyTypeTest, Integer, Integer> propertyType =
        new DefaultPropertyType<>(
            DefaultPropertyTypeTest.class,
            () -> new BasicScalar<>(PrimitiveIntWrapper::new),
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
  public void testWrapperPropertyDefaultValue() {
    DefaultPropertyType<DefaultPropertyTypeTest, Integer, Integer> propertyType =
        new DefaultPropertyType<>(
            DefaultPropertyTypeTest.class,
            () -> new BasicScalar<>(IntegerWrapper::new),
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
