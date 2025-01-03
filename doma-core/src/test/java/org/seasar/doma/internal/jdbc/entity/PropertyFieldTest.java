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

import org.junit.jupiter.api.Test;

/** */
public class PropertyFieldTest {

  @Test
  public void testConstructor_simplePath() {
    PropertyField<Person> path = new PropertyField<>("name", Person.class);
    assertEquals(1, path.fields.size());
  }

  @Test
  public void testConstructor_parentPath() {
    PropertyField<Person> path = new PropertyField<>("kind", Person.class);
    assertEquals(1, path.fields.size());
  }

  @Test
  public void testConstructor_grandParentPath() {
    PropertyField<Person> path = new PropertyField<>("weight", Person.class);
    assertEquals(1, path.fields.size());
  }

  @Test
  public void testConstructor_nestedPath() {
    PropertyField<Person> path = new PropertyField<>("address.city", Person.class);
    assertEquals(2, path.fields.size());
  }

  @Test
  public void testConstructor_nestedParentPath() {
    PropertyField<Person> path = new PropertyField<>("address.kind", Person.class);
    assertEquals(2, path.fields.size());
  }

  @Test
  public void testGetValue_simplePath() {
    Person person = new Person();
    person.name = "hoge";
    PropertyField<Person> path = new PropertyField<>("name", Person.class);
    assertEquals("hoge", path.getValue(person));
  }

  @Test
  public void testGetValue_parentPath() {
    Person person = new Person();
    person.kind = "hoge";
    PropertyField<Person> path = new PropertyField<>("kind", Person.class);
    assertEquals("hoge", path.getValue(person));
  }

  @Test
  public void testGetValue_nestedPath() {
    Person person = new Person();
    person.address = new Address("island", "Tokyo", "Yaesu");
    PropertyField<Person> path = new PropertyField<>("address.city", Person.class);
    assertEquals("Tokyo", path.getValue(person));
  }

  @Test
  public void testGetValue_nestedPath_null() {
    Person person = new Person();
    PropertyField<Person> path = new PropertyField<>("address.city", Person.class);
    assertNull(path.getValue(person));
  }

  @Test
  public void testGetValue_nestedParentPath() {
    Person person = new Person();
    person.address = new Address("island", "Tokyo", "Yaesu");
    PropertyField<Person> path = new PropertyField<>("address.kind", Person.class);
    assertEquals("island", path.getValue(person));
  }

  @Test
  public void testSetValue_simplePath() {
    Person person = new Person();
    PropertyField<Person> path = new PropertyField<>("name", Person.class);
    path.setValue(person, "foo");
    assertEquals("foo", person.name);
  }

  @Test
  public void testSetValue_parentPath() {
    Person person = new Person();
    PropertyField<Person> path = new PropertyField<>("kind", Person.class);
    path.setValue(person, "foo");
    assertEquals("foo", person.kind);
  }

  @Test
  public void testSetValue_nestedPath() {
    Person person = new Person();
    PropertyField<Person> path = new PropertyField<>("address.city", Person.class);
    try {
      path.setValue(person, "Kyoto");
      fail();
    } catch (UnsupportedOperationException expected) {
    }
  }

  @Test
  public void testSetValue_nestedParentPath() {
    Person person = new Person();
    PropertyField<Person> path = new PropertyField<>("address.kind", Person.class);
    try {
      path.setValue(person, "island");
      fail();
    } catch (UnsupportedOperationException expected) {
    }
  }

  @Test
  public void testIsPrimitive() {
    PropertyField<Person> age = new PropertyField<>("age", Person.class);
    assertTrue(age.isPrimitive());
    PropertyField<Person> name = new PropertyField<>("name", Person.class);
    assertFalse(name.isPrimitive());
  }
}
