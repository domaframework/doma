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
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;

/** */
public class PropertyFieldTest {

  @Test
  public void testConstructor_simplePath() {
    PropertyField<Person> field = new PropertyField<>("name", Person.class);
    assertEquals(1, field.fields.size());
  }

  @Test
  public void testConstructor_parentPath() {
    PropertyField<Person> field = new PropertyField<>("kind", Person.class);
    assertEquals(1, field.fields.size());
  }

  @Test
  public void testConstructor_grandParentPath() {
    PropertyField<Person> field = new PropertyField<>("weight", Person.class);
    assertEquals(1, field.fields.size());
  }

  @Test
  public void testConstructor_nestedPath() {
    PropertyField<Person> field = new PropertyField<>("address.city", Person.class);
    assertEquals(2, field.fields.size());
  }

  @Test
  public void testConstructor_nestedParentPath() {
    PropertyField<Person> field = new PropertyField<>("address.kind", Person.class);
    assertEquals(2, field.fields.size());
  }

  @Test
  public void testGetValue_simplePath() {
    Person person = new Person();
    person.name = "hoge";
    PropertyField<Person> field = new PropertyField<>("name", Person.class);
    assertEquals("hoge", field.getValue(person));
  }

  @Test
  public void testGetValue_parentPath() {
    Person person = new Person();
    person.kind = "hoge";
    PropertyField<Person> field = new PropertyField<>("kind", Person.class);
    assertEquals("hoge", field.getValue(person));
  }

  @Test
  public void testGetValue_nestedPath() {
    Person person = new Person();
    person.address = new Address("island", "Tokyo", "Yaesu");
    PropertyField<Person> field = new PropertyField<>("address.city", Person.class);
    assertEquals("Tokyo", field.getValue(person));
  }

  @Test
  public void testGetValue_nestedPath_null() {
    Person person = new Person();
    PropertyField<Person> field = new PropertyField<>("address.city", Person.class);
    assertNull(field.getValue(person));
  }

  @Test
  public void testGetValue_nestedParentPath() {
    Person person = new Person();
    person.address = new Address("island", "Tokyo", "Yaesu");
    PropertyField<Person> field = new PropertyField<>("address.kind", Person.class);
    assertEquals("island", field.getValue(person));
  }

  @Test
  public void testSetValue_simplePath() {
    Person person = new Person();
    PropertyField<Person> field = new PropertyField<>("name", Person.class);
    field.setValue(person, "foo");
    assertEquals("foo", person.name);
  }

  @Test
  public void testSetValue_parentPath() {
    Person person = new Person();
    PropertyField<Person> field = new PropertyField<>("kind", Person.class);
    field.setValue(person, "foo");
    assertEquals("foo", person.kind);
  }

  @Test
  public void testSetValue_nestedPath() {
    Person person = new Person();
    PropertyField<Person> field = new PropertyField<>("address.city", Person.class);
    try {
      field.setValue(person, "Kyoto");
      fail();
    } catch (UnsupportedOperationException expected) {
    }
  }

  @Test
  public void testSetValue_nestedParentPath() {
    Person person = new Person();
    PropertyField<Person> field = new PropertyField<>("address.kind", Person.class);
    try {
      field.setValue(person, "island");
      fail();
    } catch (UnsupportedOperationException expected) {
    }
  }

  @Test
  public void testIsPrimitive() {
    PropertyField<Person> field = new PropertyField<>("age", Person.class);
    assertTrue(field.isPrimitive());
    PropertyField<Person> field2 = new PropertyField<>("name", Person.class);
    assertFalse(field2.isPrimitive());
  }

  // Tests for PropertyPath constructor

  @Test
  public void testConstructorWithPropertyPath_simplePath() {
    List<PropertyPathSegment> segments = List.of(new PropertyPathSegment.Default("name"));
    PropertyPath propertyPath = new PropertyPath(segments);
    PropertyField<Person> field = new PropertyField<>(propertyPath, Person.class);
    assertEquals(1, field.fields.size());
  }

  @Test
  public void testConstructorWithPropertyPath_nestedPath() {
    List<PropertyPathSegment> segments =
        Arrays.asList(
            new PropertyPathSegment.Default("address"), new PropertyPathSegment.Default("city"));
    PropertyPath propertyPath = new PropertyPath(segments);
    PropertyField<Person> field = new PropertyField<>(propertyPath, Person.class);
    assertEquals(2, field.fields.size());
  }

  @Test
  public void testConstructorWithPropertyPath_multipleLevels() {
    List<PropertyPathSegment> segments =
        Arrays.asList(
            new PropertyPathSegment.Default("address"), new PropertyPathSegment.Default("kind"));
    PropertyPath propertyPath = new PropertyPath(segments);
    PropertyField<Person> field = new PropertyField<>(propertyPath, Person.class);
    assertEquals(2, field.fields.size());
  }

  @Test
  public void testGetValueWithPropertyPath_simplePath() {
    Person person = new Person();
    person.name = "John";

    List<PropertyPathSegment> segments = List.of(new PropertyPathSegment.Default("name"));
    PropertyPath propertyPath = new PropertyPath(segments);
    PropertyField<Person> field = new PropertyField<>(propertyPath, Person.class);

    assertEquals("John", field.getValue(person));
  }

  @Test
  public void testGetValueWithPropertyPath_simplePath_optional() {
    Person person = new Person();
    person.phoneNumber = Optional.of("123");

    List<PropertyPathSegment> segments =
        List.of(new PropertyPathSegment.Optional("phoneNumber", String.class));
    PropertyPath propertyPath = new PropertyPath(segments);
    PropertyField<Person> field = new PropertyField<>(propertyPath, Person.class);

    assertEquals("123", field.getValue(person));
  }

  @Test
  public void testGetValueWithPropertyPath_nestedPath() {
    Person person = new Person();
    person.address = new Address("home", "Tokyo", "Shibuya");

    List<PropertyPathSegment> segments =
        Arrays.asList(
            new PropertyPathSegment.Default("address"), new PropertyPathSegment.Default("city"));
    PropertyPath propertyPath = new PropertyPath(segments);
    PropertyField<Person> field = new PropertyField<>(propertyPath, Person.class);

    assertEquals("Tokyo", field.getValue(person));
  }

  @Test
  public void testGetValueWithPropertyPath_parentField() {
    Person person = new Person();
    person.kind = "human";

    List<PropertyPathSegment> segments = List.of(new PropertyPathSegment.Default("kind"));
    PropertyPath propertyPath = new PropertyPath(segments);
    PropertyField<Person> field = new PropertyField<>(propertyPath, Person.class);

    assertEquals("human", field.getValue(person));
  }

  @Test
  public void testSetValueWithPropertyPath_simplePath() {
    Person person = new Person();

    List<PropertyPathSegment> segments = List.of(new PropertyPathSegment.Default("name"));
    PropertyPath propertyPath = new PropertyPath(segments);
    PropertyField<Person> field = new PropertyField<>(propertyPath, Person.class);

    field.setValue(person, "Jane");
    assertEquals("Jane", person.name);
  }

  @Test
  public void testSetValueWithPropertyPath_nestedPath_unsupported() {
    Person person = new Person();

    List<PropertyPathSegment> segments =
        Arrays.asList(
            new PropertyPathSegment.Default("address"), new PropertyPathSegment.Default("city"));
    PropertyPath propertyPath = new PropertyPath(segments);
    PropertyField<Person> field = new PropertyField<>(propertyPath, Person.class);

    try {
      field.setValue(person, "Osaka");
      fail();
    } catch (UnsupportedOperationException expected) {
    }
  }
}
