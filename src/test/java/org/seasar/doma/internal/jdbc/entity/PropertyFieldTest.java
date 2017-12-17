package org.seasar.doma.internal.jdbc.entity;

import junit.framework.TestCase;

/**
 *
 */
public class PropertyFieldTest extends TestCase {

    public void testConstructor_simplePath() throws Exception {
        PropertyField<Person> path = new PropertyField<>("name", Person.class);
        assertEquals(1, path.fields.size());
    }

    public void testConstructor_parentPath() throws Exception {
        PropertyField<Person> path = new PropertyField<>("kind", Person.class);
        assertEquals(1, path.fields.size());
    }

    public void testConstructor_grandParentPath() throws Exception {
        PropertyField<Person> path = new PropertyField<>("weight", Person.class);
        assertEquals(1, path.fields.size());
    }

    public void testConstructor_nestedPath() throws Exception {
        PropertyField<Person> path = new PropertyField<>("address.city", Person.class);
        assertEquals(2, path.fields.size());
    }

    public void testConstructor_nestedParentPath() throws Exception {
        PropertyField<Person> path = new PropertyField<>("address.kind", Person.class);
        assertEquals(2, path.fields.size());
    }

    public void testGetValue_simplePath() throws Exception {
        Person person = new Person();
        person.name = "hoge";
        PropertyField<Person> path = new PropertyField<>("name", Person.class);
        assertEquals("hoge", path.getValue(person));
    }

    public void testGetValue_parentPath() throws Exception {
        Person person = new Person();
        person.kind = "hoge";
        PropertyField<Person> path = new PropertyField<>("kind", Person.class);
        assertEquals("hoge", path.getValue(person));
    }

    public void testGetValue_nestedPath() throws Exception {
        Person person = new Person();
        person.address = new Address("island", "Tokyo", "Yaesu");
        PropertyField<Person> path = new PropertyField<>("address.city", Person.class);
        assertEquals("Tokyo", path.getValue(person));
    }

    public void testGetValue_nestedPath_null() throws Exception {
        Person person = new Person();
        PropertyField<Person> path = new PropertyField<>("address.city", Person.class);
        assertNull(path.getValue(person));
    }

    public void testGetValue_nestedParentPath() throws Exception {
        Person person = new Person();
        person.address = new Address("island", "Tokyo", "Yaesu");
        PropertyField<Person> path = new PropertyField<>("address.kind", Person.class);
        assertEquals("island", path.getValue(person));
    }

    public void testSetValue_simplePath() throws Exception {
        Person person = new Person();
        PropertyField<Person> path = new PropertyField<>("name", Person.class);
        path.setValue(person, "foo");
        assertEquals("foo", person.name);
    }

    public void testSetValue_parentPath() throws Exception {
        Person person = new Person();
        PropertyField<Person> path = new PropertyField<>("kind", Person.class);
        path.setValue(person, "foo");
        assertEquals("foo", person.kind);
    }

    public void testSetValue_nestedPath() throws Exception {
        Person person = new Person();
        PropertyField<Person> path = new PropertyField<>("address.city", Person.class);
        try {
            path.setValue(person, "Kyoto");
            fail();
        } catch (UnsupportedOperationException expected) {
        }
    }

    public void testSetValue_nestedParentPath() throws Exception {
        Person person = new Person();
        PropertyField<Person> path = new PropertyField<>("address.kind", Person.class);
        try {
            path.setValue(person, "island");
            fail();
        } catch (UnsupportedOperationException expected) {
        }
    }

    public void testIsPrimitive() throws Exception {
        PropertyField<Person> age = new PropertyField<>("age", Person.class);
        assertTrue(age.isPrimitive());
        PropertyField<Person> name = new PropertyField<>("name", Person.class);
        assertFalse(name.isPrimitive());
    }
}
