package org.seasar.doma.internal.jdbc.entity;

import junit.framework.TestCase;

/** */
public class PropertyFieldTest extends TestCase {

  public void testConstructor_simplePath() throws Exception {
    var path = new PropertyField<>("name", Person.class);
    assertEquals(1, path.fields.size());
  }

  public void testConstructor_parentPath() throws Exception {
    var path = new PropertyField<>("kind", Person.class);
    assertEquals(1, path.fields.size());
  }

  public void testConstructor_grandParentPath() throws Exception {
    var path = new PropertyField<>("weight", Person.class);
    assertEquals(1, path.fields.size());
  }

  public void testConstructor_nestedPath() throws Exception {
    var path = new PropertyField<>("address.city", Person.class);
    assertEquals(2, path.fields.size());
  }

  public void testConstructor_nestedParentPath() throws Exception {
    var path = new PropertyField<>("address.kind", Person.class);
    assertEquals(2, path.fields.size());
  }

  public void testGetValue_simplePath() throws Exception {
    var person = new Person();
    person.name = "hoge";
    var path = new PropertyField<>("name", Person.class);
    assertEquals("hoge", path.getValue(person));
  }

  public void testGetValue_parentPath() throws Exception {
    var person = new Person();
    person.kind = "hoge";
    var path = new PropertyField<>("kind", Person.class);
    assertEquals("hoge", path.getValue(person));
  }

  public void testGetValue_nestedPath() throws Exception {
    var person = new Person();
    person.address = new Address("island", "Tokyo", "Yaesu");
    var path = new PropertyField<>("address.city", Person.class);
    assertEquals("Tokyo", path.getValue(person));
  }

  public void testGetValue_nestedPath_null() throws Exception {
    var person = new Person();
    var path = new PropertyField<>("address.city", Person.class);
    assertNull(path.getValue(person));
  }

  public void testGetValue_nestedParentPath() throws Exception {
    var person = new Person();
    person.address = new Address("island", "Tokyo", "Yaesu");
    var path = new PropertyField<>("address.kind", Person.class);
    assertEquals("island", path.getValue(person));
  }

  public void testSetValue_simplePath() throws Exception {
    var person = new Person();
    var path = new PropertyField<>("name", Person.class);
    path.setValue(person, "foo");
    assertEquals("foo", person.name);
  }

  public void testSetValue_parentPath() throws Exception {
    var person = new Person();
    var path = new PropertyField<>("kind", Person.class);
    path.setValue(person, "foo");
    assertEquals("foo", person.kind);
  }

  public void testSetValue_nestedPath() throws Exception {
    var person = new Person();
    var path = new PropertyField<>("address.city", Person.class);
    try {
      path.setValue(person, "Kyoto");
      fail();
    } catch (UnsupportedOperationException expected) {
    }
  }

  public void testSetValue_nestedParentPath() throws Exception {
    var person = new Person();
    var path = new PropertyField<>("address.kind", Person.class);
    try {
      path.setValue(person, "island");
      fail();
    } catch (UnsupportedOperationException expected) {
    }
  }

  public void testIsPrimitive() throws Exception {
    var age = new PropertyField<>("age", Person.class);
    assertTrue(age.isPrimitive());
    var name = new PropertyField<>("name", Person.class);
    assertFalse(name.isPrimitive());
  }
}
