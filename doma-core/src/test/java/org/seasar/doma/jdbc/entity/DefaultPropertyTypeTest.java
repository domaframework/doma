/*
 * Copyright Doma Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.seasar.doma.jdbc.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collections;
import java.util.Map;
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

  @Test
  public void testColumnNamePrefix_columnDefined() {
    DefaultPropertyType<DefaultPropertyTypeTest, String, String> propertyType =
        new DefaultPropertyType<>(
            DefaultPropertyTypeTest.class,
            () -> new BasicScalar<>(StringWrapper::new),
            "hoge",
            "foo",
            NamingType.UPPER_CASE,
            true,
            true,
            false,
            new EmbeddedType("prefix_", Collections.emptyMap()));
    assertEquals("prefix_foo", propertyType.getColumnName());
  }

  @Test
  public void testColumnNamePrefix_columnNotDefined() {
    DefaultPropertyType<DefaultPropertyTypeTest, String, String> propertyType =
        new DefaultPropertyType<>(
            DefaultPropertyTypeTest.class,
            () -> new BasicScalar<>(StringWrapper::new),
            "hoge",
            "",
            NamingType.UPPER_CASE,
            true,
            true,
            false,
            new EmbeddedType("prefix_", Collections.emptyMap()));
    assertEquals("prefix_HOGE", propertyType.getColumnName());
  }

  @Test
  public void testColumnNamePrefix_emptyString() {
    DefaultPropertyType<DefaultPropertyTypeTest, String, String> propertyType =
        new DefaultPropertyType<>(
            DefaultPropertyTypeTest.class,
            () -> new BasicScalar<>(StringWrapper::new),
            "hoge",
            "foo",
            NamingType.UPPER_CASE,
            true,
            true,
            false,
            new EmbeddedType("", Collections.emptyMap()));
    assertEquals("foo", propertyType.getColumnName());
  }

  @Test
  public void testColumnNamePrefix_quoteRequired() {
    DefaultPropertyType<DefaultPropertyTypeTest, String, String> propertyType =
        new DefaultPropertyType<>(
            DefaultPropertyTypeTest.class,
            () -> new BasicScalar<>(StringWrapper::new),
            "hoge",
            "foo",
            NamingType.UPPER_CASE,
            true,
            true,
            true,
            new EmbeddedType("prefix_", Collections.emptyMap()));
    assertEquals("[prefix_foo]", propertyType.getColumnName(text -> "[" + text + "]"));
  }

  @Test
  public void testColumnNamePrefix_quoteNotRequired() {
    DefaultPropertyType<DefaultPropertyTypeTest, String, String> propertyType =
        new DefaultPropertyType<>(
            DefaultPropertyTypeTest.class,
            () -> new BasicScalar<>(StringWrapper::new),
            "hoge",
            "foo",
            NamingType.UPPER_CASE,
            true,
            true,
            false,
            new EmbeddedType("prefix_", Collections.emptyMap()));
    assertEquals("prefix_foo", propertyType.getColumnName(text -> "[" + text + "]"));
  }

  @Test
  public void testColumnNamePrefix_embeddableProperty() {
    DefaultPropertyType<DefaultPropertyTypeTest, String, String> propertyType =
        new DefaultPropertyType<>(
            DefaultPropertyTypeTest.class,
            () -> new BasicScalar<>(StringWrapper::new),
            "foo.hoge",
            "",
            NamingType.UPPER_CASE,
            true,
            true,
            false,
            new EmbeddedType("prefix_", Collections.emptyMap()));
    assertEquals("prefix_HOGE", propertyType.getColumnName());
  }

  @Test
  public void testColumnNamePrefix_namingFunction() {
    DefaultPropertyType<DefaultPropertyTypeTest, String, String> propertyType =
        new DefaultPropertyType<>(
            DefaultPropertyTypeTest.class,
            () -> new BasicScalar<>(StringWrapper::new),
            "hoge",
            "",
            NamingType.UPPER_CASE,
            true,
            true,
            false,
            new EmbeddedType("prefix_", Collections.emptyMap()));
    assertEquals("prefix_HOGE", propertyType.getColumnName(NamingType::apply));
  }

  @Test
  public void testColumnNamePrefix_namingAndQuoteFunction() {
    DefaultPropertyType<DefaultPropertyTypeTest, String, String> propertyType =
        new DefaultPropertyType<>(
            DefaultPropertyTypeTest.class,
            () -> new BasicScalar<>(StringWrapper::new),
            "hoge",
            "",
            NamingType.UPPER_CASE,
            true,
            true,
            true,
            new EmbeddedType("prefix_", Collections.emptyMap()));
    assertEquals(
        "[prefix_HOGE]", propertyType.getColumnName(NamingType::apply, text -> "[" + text + "]"));
  }

  @Test
  public void testColumnTypeMap_overridesColumnName() {
    ColumnType columnType = new ColumnType("CUSTOM_COLUMN", null, null, null);
    Map<String, ColumnType> columnTypeMap = Map.of("foo.hoge", columnType);
    DefaultPropertyType<DefaultPropertyTypeTest, String, String> propertyType =
        new DefaultPropertyType<>(
            DefaultPropertyTypeTest.class,
            () -> new BasicScalar<>(StringWrapper::new),
            "foo.hoge",
            "original_column",
            NamingType.UPPER_CASE,
            true,
            true,
            false,
            new EmbeddedType("prefix_", columnTypeMap));
    assertEquals("CUSTOM_COLUMN", propertyType.getColumnName());
  }

  @Test
  public void testColumnTypeMap_overridesInsertable() {
    ColumnType columnType = new ColumnType("CUSTOM_COLUMN", false, null, null);
    Map<String, ColumnType> columnTypeMap = Map.of("foo.hoge", columnType);
    DefaultPropertyType<DefaultPropertyTypeTest, String, String> propertyType =
        new DefaultPropertyType<>(
            DefaultPropertyTypeTest.class,
            () -> new BasicScalar<>(StringWrapper::new),
            "foo.hoge",
            "original_column",
            NamingType.UPPER_CASE,
            true,
            true,
            false,
            new EmbeddedType("", columnTypeMap));
    assertFalse(propertyType.isInsertable());
  }

  @Test
  public void testColumnTypeMap_overridesUpdatable() {
    ColumnType columnType = new ColumnType("CUSTOM_COLUMN", null, false, null);
    Map<String, ColumnType> columnTypeMap = Map.of("foo.hoge", columnType);
    DefaultPropertyType<DefaultPropertyTypeTest, String, String> propertyType =
        new DefaultPropertyType<>(
            DefaultPropertyTypeTest.class,
            () -> new BasicScalar<>(StringWrapper::new),
            "foo.hoge",
            "original_column",
            NamingType.UPPER_CASE,
            true,
            true,
            false,
            new EmbeddedType("", columnTypeMap));
    assertFalse(propertyType.isUpdatable());
  }

  @Test
  public void testColumnTypeMap_overridesQuote() {
    ColumnType columnType = new ColumnType("CUSTOM_COLUMN", null, null, true);
    Map<String, ColumnType> columnTypeMap = Map.of("foo.hoge", columnType);
    DefaultPropertyType<DefaultPropertyTypeTest, String, String> propertyType =
        new DefaultPropertyType<>(
            DefaultPropertyTypeTest.class,
            () -> new BasicScalar<>(StringWrapper::new),
            "foo.hoge",
            "original_column",
            NamingType.UPPER_CASE,
            true,
            true,
            false,
            new EmbeddedType("", columnTypeMap));
    assertEquals("[CUSTOM_COLUMN]", propertyType.getColumnName(text -> "[" + text + "]"));
  }

  @Test
  public void testColumnTypeMap_ignoresPrefixWhenOverridden() {
    ColumnType columnType = new ColumnType("CUSTOM_COLUMN", null, null, null);
    Map<String, ColumnType> columnTypeMap = Map.of("foo.hoge", columnType);
    DefaultPropertyType<DefaultPropertyTypeTest, String, String> propertyType =
        new DefaultPropertyType<>(
            DefaultPropertyTypeTest.class,
            () -> new BasicScalar<>(StringWrapper::new),
            "foo.hoge",
            "",
            NamingType.UPPER_CASE,
            true,
            true,
            false,
            new EmbeddedType("prefix_", columnTypeMap));
    // Column override takes precedence over prefix
    assertEquals("CUSTOM_COLUMN", propertyType.getColumnName());
  }

  @Test
  public void testColumnTypeMap_noOverrideForProperty() {
    Map<String, ColumnType> columnTypeMap =
        Map.of("other", new ColumnType("OTHER_COLUMN", null, null, null));
    DefaultPropertyType<DefaultPropertyTypeTest, String, String> propertyType =
        new DefaultPropertyType<>(
            DefaultPropertyTypeTest.class,
            () -> new BasicScalar<>(StringWrapper::new),
            "foo.hoge",
            "original_column",
            NamingType.UPPER_CASE,
            true,
            true,
            false,
            new EmbeddedType("prefix_", columnTypeMap));
    // No override for "hoge", so prefix should be applied
    assertEquals("prefix_original_column", propertyType.getColumnName());
  }

  @Test
  public void testColumnTypeMap_multipleOverrides() {
    ColumnType columnType = new ColumnType("OVERRIDDEN_NAME", false, true, true);
    Map<String, ColumnType> columnTypeMap = Map.of("foo.hoge", columnType);
    DefaultPropertyType<DefaultPropertyTypeTest, String, String> propertyType =
        new DefaultPropertyType<>(
            DefaultPropertyTypeTest.class,
            () -> new BasicScalar<>(StringWrapper::new),
            "foo.hoge",
            "original_column",
            NamingType.UPPER_CASE,
            true,
            true,
            false,
            new EmbeddedType("prefix_", columnTypeMap));
    assertEquals("OVERRIDDEN_NAME", propertyType.getColumnName());
    assertFalse(propertyType.isInsertable());
    assertTrue(propertyType.isUpdatable());
    assertEquals("[OVERRIDDEN_NAME]", propertyType.getColumnName(text -> "[" + text + "]"));
  }

  public static class Foo {
    String hoge;
  }
}
