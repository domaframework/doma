package org.seasar.doma.jdbc.entity;

import junit.framework.TestCase;
import org.seasar.doma.internal.jdbc.scalar.BasicScalar;
import org.seasar.doma.wrapper.IntegerWrapper;
import org.seasar.doma.wrapper.StringWrapper;

public class DefaultPropertyDescTest extends TestCase {

  @SuppressWarnings("unused")
  private String hoge;

  @SuppressWarnings("unused")
  private Foo foo;

  @SuppressWarnings("unused")
  private int primitiveInt;

  @SuppressWarnings("unused")
  private Integer integer;

  public void testIsQuoteRequired_true() throws Exception {
    var isQuoteRequired = true;
    var propertyDesc =
        new DefaultPropertyDesc<>(
            DefaultPropertyDescTest.class,
            () -> new BasicScalar<>(new StringWrapper(), false),
            "hoge",
            "hoge",
            NamingType.NONE,
            true,
            true,
            isQuoteRequired);
    assertEquals("hoge", propertyDesc.getColumnName((n, t) -> t));
    assertEquals("[hoge]", propertyDesc.getColumnName((n, t) -> t, s -> "[" + s + "]"));
  }

  public void testIsQuoteRequired_false() throws Exception {
    var isQuoteRequired = false;
    var propertyDesc =
        new DefaultPropertyDesc<>(
            DefaultPropertyDescTest.class,
            () -> new BasicScalar<>(new StringWrapper(), false),
            "hoge",
            "hoge",
            NamingType.NONE,
            true,
            true,
            isQuoteRequired);
    assertEquals("hoge", propertyDesc.getColumnName((n, t) -> t));
    assertEquals("hoge", propertyDesc.getColumnName((n, t) -> t, s -> "[" + s + "]"));
  }

  public void testGetColumnName_naming_columnNameDefined() throws Exception {
    var propertyDesc =
        new DefaultPropertyDesc<>(
            DefaultPropertyDescTest.class,
            () -> new BasicScalar<>(new StringWrapper(), false),
            "hoge",
            "foo",
            NamingType.UPPER_CASE,
            true,
            true,
            false);
    assertEquals("foo", propertyDesc.getColumnName(NamingType::apply));
  }

  public void testGetColumnName_naiming_columnNotDefined() throws Exception {
    var propertyDesc =
        new DefaultPropertyDesc<>(
            DefaultPropertyDescTest.class,
            () -> new BasicScalar<>(new StringWrapper(), false),
            "hoge",
            "",
            NamingType.UPPER_CASE,
            true,
            true,
            false);
    assertEquals("HOGE", propertyDesc.getColumnName(NamingType::apply));
  }

  public void testGetColumnName_naiming_quote_quoteRequired() throws Exception {
    var propertyDesc =
        new DefaultPropertyDesc<>(
            DefaultPropertyDescTest.class,
            () -> new BasicScalar<>(new StringWrapper(), false),
            "hoge",
            "",
            NamingType.UPPER_CASE,
            true,
            true,
            true);
    assertEquals("[HOGE]", propertyDesc.getColumnName(NamingType::apply, text -> "[" + text + "]"));
  }

  public void testGetColumnName_naiming_quote_quoteNotRequired() throws Exception {
    var propertyDesc =
        new DefaultPropertyDesc<>(
            DefaultPropertyDescTest.class,
            () -> new BasicScalar<>(new StringWrapper(), false),
            "hoge",
            "",
            NamingType.UPPER_CASE,
            true,
            true,
            false);
    assertEquals("HOGE", propertyDesc.getColumnName(NamingType::apply, text -> "[" + text + "]"));
  }

  public void testPrimitivePropertyDefaultValue() throws Exception {
    var propertyDesc =
        new DefaultPropertyDesc<>(
            DefaultPropertyDescTest.class,
            () -> new BasicScalar<>(new IntegerWrapper(), true),
            "primitiveInt",
            "primitiveInt",
            NamingType.NONE,
            true,
            true,
            false);
    var property = propertyDesc.createProperty();
    assertEquals(0, property.get());
  }

  public void testWrapperPropertyDefaultValue() throws Exception {
    var propertyDesc =
        new DefaultPropertyDesc<>(
            DefaultPropertyDescTest.class,
            () -> new BasicScalar<>(new IntegerWrapper(), false),
            "integer",
            "integer",
            NamingType.NONE,
            true,
            true,
            false);
    var property = propertyDesc.createProperty();
    assertNull(property.get());
  }

  public static class Foo {
    String hoge;
  }
}
