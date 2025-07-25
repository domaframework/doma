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
package org.seasar.doma.internal.jdbc.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Field;
import java.util.Optional;
import org.junit.jupiter.api.Test;

public class PropertyPathSegmentTest {

  @Test
  public void testDefaultSegment() {
    PropertyPathSegment segment = new PropertyPathSegment.Default("firstName");

    assertNotNull(segment);
    assertEquals("firstName", segment.name());
  }

  @Test
  public void testOptionalSegment() {
    PropertyPathSegment.Optional segment =
        new PropertyPathSegment.Optional("address", Address.class);

    assertNotNull(segment);
    assertEquals("address", segment.name());
    assertEquals(Address.class, segment.elementClass());
  }

  @Test
  public void testOptionalSegment_withPrimitiveClass() {
    PropertyPathSegment.Optional segment = new PropertyPathSegment.Optional("count", int.class);

    assertNotNull(segment);
    assertEquals("count", segment.name());
    assertEquals(int.class, segment.elementClass());
  }

  @Test
  public void testOptionalSegment_withStringClass() {
    PropertyPathSegment.Optional segment =
        new PropertyPathSegment.Optional("description", String.class);

    assertNotNull(segment);
    assertEquals("description", segment.name());
    assertEquals(String.class, segment.elementClass());
  }

  @Test
  public void testPolymorphism() {
    PropertyPathSegment defaultSegment = new PropertyPathSegment.Default("name");
    PropertyPathSegment optionalSegment = new PropertyPathSegment.Optional("value", Object.class);

    // Both types should be assignable to PropertyPathSegment interface
    assertNotNull(defaultSegment);
    assertNotNull(optionalSegment);
    assertEquals("name", defaultSegment.name());
    assertEquals("value", optionalSegment.name());
  }

  @Test
  public void testDefaultSegment_wrapField() throws Exception {
    PropertyPathSegment.Default segment = new PropertyPathSegment.Default("name");
    Field field = TestEntity.class.getDeclaredField("name");

    FieldWrapper wrapper = segment.wrapField(field);

    assertNotNull(wrapper);
    assertEquals(String.class, wrapper.getType());

    TestEntity entity = new TestEntity();
    entity.name = "John";

    assertEquals("John", wrapper.get(entity));

    wrapper.set(entity, "Jane");
    assertEquals("Jane", entity.name);
  }

  @Test
  public void testDefaultSegment_wrapPrivateField() throws Exception {
    PropertyPathSegment.Default segment = new PropertyPathSegment.Default("privateField");
    Field field = TestEntity.class.getDeclaredField("privateField");

    FieldWrapper wrapper = segment.wrapField(field);

    assertNotNull(wrapper);
    assertEquals(String.class, wrapper.getType());

    TestEntity entity = new TestEntity();
    wrapper.set(entity, "private value");
    assertEquals("private value", wrapper.get(entity));
  }

  @Test
  public void testOptionalSegment_wrapField_withValue() throws Exception {
    PropertyPathSegment.Optional segment =
        new PropertyPathSegment.Optional("optionalName", String.class);
    Field field = TestEntity.class.getDeclaredField("optionalName");

    FieldWrapper wrapper = segment.wrapField(field);

    assertNotNull(wrapper);
    assertEquals(String.class, wrapper.getType());

    TestEntity entity = new TestEntity();
    entity.optionalName = Optional.of("test value");

    assertEquals("test value", wrapper.get(entity));
  }

  @Test
  public void testOptionalSegment_wrapField_withEmptyOptional() throws Exception {
    PropertyPathSegment.Optional segment =
        new PropertyPathSegment.Optional("optionalName", String.class);
    Field field = TestEntity.class.getDeclaredField("optionalName");

    FieldWrapper wrapper = segment.wrapField(field);

    TestEntity entity = new TestEntity();
    entity.optionalName = Optional.empty();

    assertNull(wrapper.get(entity));
  }

  @Test
  public void testOptionalSegment_wrapField_withNullOptional() throws Exception {
    PropertyPathSegment.Optional segment =
        new PropertyPathSegment.Optional("optionalName", String.class);
    Field field = TestEntity.class.getDeclaredField("optionalName");

    FieldWrapper wrapper = segment.wrapField(field);

    TestEntity entity = new TestEntity();
    entity.optionalName = null;

    assertNull(wrapper.get(entity));
  }

  @Test
  public void testOptionalSegment_wrapField_setOperation() throws Exception {
    PropertyPathSegment.Optional segment =
        new PropertyPathSegment.Optional("optionalName", String.class);
    Field field = TestEntity.class.getDeclaredField("optionalName");

    FieldWrapper wrapper = segment.wrapField(field);

    TestEntity entity = new TestEntity();
    wrapper.set(entity, Optional.of("new value"));

    assertEquals(Optional.of("new value"), entity.optionalName);
  }

  @Test
  public void testOptionalSegment_wrapField_withComplexType() throws Exception {
    PropertyPathSegment.Optional segment =
        new PropertyPathSegment.Optional("optionalAddress", Address.class);
    Field field = TestEntity.class.getDeclaredField("optionalAddress");

    FieldWrapper wrapper = segment.wrapField(field);

    assertNotNull(wrapper);
    assertEquals(Address.class, wrapper.getType());

    TestEntity entity = new TestEntity();
    Address address = new Address();
    entity.optionalAddress = Optional.of(address);

    assertEquals(address, wrapper.get(entity));
  }

  @Test
  public void testOptionalSegment_wrapField_illegalArgumentException() throws Exception {
    PropertyPathSegment.Optional segment = new PropertyPathSegment.Optional("name", String.class);
    Field field = TestEntity.class.getDeclaredField("name"); // This is not an Optional field

    assertThrows(IllegalArgumentException.class, () -> segment.wrapField(field));
  }

  @Test
  public void testDefaultSegment_withNullName() {
    assertThrows(NullPointerException.class, () -> new PropertyPathSegment.Default(null));
  }

  @Test
  public void testOptionalSegment_withNullName() {
    assertThrows(
        NullPointerException.class, () -> new PropertyPathSegment.Optional(null, String.class));
  }

  @Test
  public void testOptionalSegment_withNullElementClass() {
    assertThrows(NullPointerException.class, () -> new PropertyPathSegment.Optional("test", null));
  }

  @Test
  public void testDefaultSegment_equals() {
    PropertyPathSegment.Default segment1 = new PropertyPathSegment.Default("name");
    PropertyPathSegment.Default segment2 = new PropertyPathSegment.Default("name");
    PropertyPathSegment.Default segment3 = new PropertyPathSegment.Default("other");

    assertEquals(segment1, segment2);
    assertFalse(segment1.equals(segment3));
  }

  @Test
  public void testOptionalSegment_equals() {
    PropertyPathSegment.Optional segment1 = new PropertyPathSegment.Optional("name", String.class);
    PropertyPathSegment.Optional segment2 = new PropertyPathSegment.Optional("name", String.class);
    PropertyPathSegment.Optional segment3 = new PropertyPathSegment.Optional("name", Integer.class);
    PropertyPathSegment.Optional segment4 = new PropertyPathSegment.Optional("other", String.class);

    assertEquals(segment1, segment2);
    assertFalse(segment1.equals(segment3));
    assertFalse(segment1.equals(segment4));
  }

  @Test
  public void testDefaultSegment_hashCode() {
    PropertyPathSegment.Default segment1 = new PropertyPathSegment.Default("name");
    PropertyPathSegment.Default segment2 = new PropertyPathSegment.Default("name");

    assertEquals(segment1.hashCode(), segment2.hashCode());
  }

  @Test
  public void testOptionalSegment_hashCode() {
    PropertyPathSegment.Optional segment1 = new PropertyPathSegment.Optional("name", String.class);
    PropertyPathSegment.Optional segment2 = new PropertyPathSegment.Optional("name", String.class);

    assertEquals(segment1.hashCode(), segment2.hashCode());
  }

  @Test
  public void testDefaultSegment_toString() {
    PropertyPathSegment.Default segment = new PropertyPathSegment.Default("testName");
    String result = segment.toString();

    assertNotNull(result);
    assertTrue(result.contains("testName"));
  }

  @Test
  public void testOptionalSegment_toString() {
    PropertyPathSegment.Optional segment =
        new PropertyPathSegment.Optional("testName", String.class);
    String result = segment.toString();

    assertNotNull(result);
    assertTrue(result.contains("testName"));
    assertTrue(result.contains("String"));
  }

  // Test entity class for comprehensive testing
  public static class TestEntity {
    public String name;
    private String privateField;
    public Optional<String> optionalName;
    public Optional<Address> optionalAddress;
  }

  // Dummy class for testing
  public static class Address {}
}
