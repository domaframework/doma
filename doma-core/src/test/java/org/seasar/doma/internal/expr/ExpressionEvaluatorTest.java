package org.seasar.doma.internal.expr;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.OptionalLong;
import org.junit.jupiter.api.Test;
import org.seasar.doma.internal.expr.node.ExpressionLocation;

public class ExpressionEvaluatorTest {

  protected final ExpressionLocation location = new ExpressionLocation("dummy expression", 0);

  @Test
  public void testInvokeMethod() throws Exception {
    Class<?>[] paramTypes = new Class[] {int.class, int.class};
    Method method = String.class.getMethod("substring", paramTypes);
    ExpressionEvaluator evaluator = new ExpressionEvaluator();
    EvaluationResult result =
        evaluator.invokeMethod(
            location, method, "abcde", String.class, paramTypes, new Object[] {2, 4});
    assertEquals("cd", result.getValue());
    assertEquals(String.class, result.getValueClass());
  }

  @Test
  public void testInvokeMethod_optional() throws Exception {
    Method method = Person.class.getMethod("getOptionalName");
    ExpressionEvaluator evaluator = new ExpressionEvaluator();
    Person person = new Person();
    person.optionalName = Optional.of("aaa");
    EvaluationResult result =
        evaluator.invokeMethod(
            location, method, person, Person.class, new Class[] {}, new Object[] {});
    assertEquals("aaa", result.getValue());
    assertEquals(String.class, result.getValueClass());
  }

  @Test
  public void testInvokeMethod_optional_empty() throws Exception {
    Method method = Person.class.getMethod("getOptionalName");
    ExpressionEvaluator evaluator = new ExpressionEvaluator();
    Person person = new Person();
    person.optionalName = Optional.empty();
    EvaluationResult result =
        evaluator.invokeMethod(
            location, method, person, Person.class, new Class[] {}, new Object[] {});
    assertNull(result.getValue());
    assertEquals(String.class, result.getValueClass());
  }

  @Test
  public void testInvokeMethod_static_optional() throws Exception {
    Method method = Person.class.getMethod("getStaticOptionalName");
    ExpressionEvaluator evaluator = new ExpressionEvaluator();
    EvaluationResult result =
        evaluator.invokeMethod(
            location, method, null, Person.class, new Class[] {}, new Object[] {});
    assertEquals("foo", result.getValue());
    assertEquals(String.class, result.getValueClass());
  }

  @Test
  public void testInvokeMethod_optionalInt() throws Exception {
    Method method = Person.class.getMethod("getAge");
    ExpressionEvaluator evaluator = new ExpressionEvaluator();
    Person person = new Person();
    person.age = OptionalInt.of(10);
    EvaluationResult result =
        evaluator.invokeMethod(
            location, method, person, Person.class, new Class[] {}, new Object[] {});
    assertEquals(10, result.getValue());
    assertEquals(Integer.class, result.getValueClass());
  }

  @Test
  public void testInvokeMethod_optionalInt_empty() throws Exception {
    Method method = Person.class.getMethod("getAge");
    ExpressionEvaluator evaluator = new ExpressionEvaluator();
    Person person = new Person();
    person.age = OptionalInt.empty();
    EvaluationResult result =
        evaluator.invokeMethod(
            location, method, person, Person.class, new Class[] {}, new Object[] {});
    assertNull(result.getValue());
    assertEquals(Integer.class, result.getValueClass());
  }

  @Test
  public void testInvokeMethod_static_optionalInt() throws Exception {
    Method method = Person.class.getMethod("getStaticAge");
    Person.staticAge = OptionalInt.of(10);
    ExpressionEvaluator evaluator = new ExpressionEvaluator();
    EvaluationResult result =
        evaluator.invokeMethod(
            location, method, null, Person.class, new Class[] {}, new Object[] {});
    assertEquals(10, result.getValue());
    assertEquals(Integer.class, result.getValueClass());
  }

  @Test
  public void testInvokeMethod_static_optionalInt_empty() throws Exception {
    Method method = Person.class.getMethod("getStaticAge");
    Person.staticAge = OptionalInt.empty();
    ExpressionEvaluator evaluator = new ExpressionEvaluator();
    EvaluationResult result =
        evaluator.invokeMethod(
            location, method, null, Person.class, new Class[] {}, new Object[] {});
    assertNull(result.getValue());
    assertEquals(Integer.class, result.getValueClass());
  }

  @Test
  public void testFindMethod() {
    ExpressionEvaluator evaluator = new ExpressionEvaluator();
    Method method =
        evaluator.findMethod("add", new ArrayList<>(), ArrayList.class, new Class[] {Object.class});
    assertNotNull(method);
    assertEquals(Collection.class, method.getDeclaringClass());
  }

  @Test
  public void testFindMethod_String_is_subtype_of_Object() {
    ExpressionEvaluator evaluator = new ExpressionEvaluator();
    Method method =
        evaluator.findMethod("add", new ArrayList<>(), ArrayList.class, new Class[] {String.class});
    assertNotNull(method);
    assertEquals(Collection.class, method.getDeclaringClass());
  }

  @Test
  public void testFindMethod_List_is_subtype_of_Object() {
    ExpressionEvaluator evaluator = new ExpressionEvaluator();
    Method method =
        evaluator.findMethod("add", new ArrayList<>(), ArrayList.class, new Class[] {List.class});
    assertNotNull(method);
    assertEquals(Collection.class, method.getDeclaringClass());
  }

  @Test
  public void testFindMethod_List_is_subtype_of_Collection() {
    ExpressionEvaluator evaluator = new ExpressionEvaluator();
    Method method =
        evaluator.findMethod(
            "addAll", new ArrayList<>(), ArrayList.class, new Class[] {List.class});
    assertNotNull(method);
    assertEquals(Collection.class, method.getDeclaringClass());
  }

  @Test
  public void testFindMethod_overload_int() {
    ExpressionEvaluator evaluator = new ExpressionEvaluator();
    Method method =
        evaluator.findMethod("indexOf", "string", String.class, new Class[] {int.class});
    assertNotNull(method);
    assertEquals(String.class, method.getDeclaringClass());
  }

  @Test
  public void testFindMethod_overload_string() {
    ExpressionEvaluator evaluator = new ExpressionEvaluator();
    Method method =
        evaluator.findMethod("indexOf", "string", String.class, new Class[] {String.class});
    assertNotNull(method);
    assertEquals(String.class, method.getDeclaringClass());
  }

  @Test
  public void testFindMethod_autoBoxing() {
    ExpressionEvaluator evaluator = new ExpressionEvaluator();
    Method method = evaluator.findMethod("compareTo", 1, Integer.class, new Class[] {int.class});
    assertNotNull(method);
    assertEquals(Integer.class, method.getDeclaringClass());
  }

  @Test
  public void testFindMethod_notFound() {
    ExpressionEvaluator evaluator = new ExpressionEvaluator();
    Method method =
        evaluator.findMethod("inexistentMethod", "aaa", String.class, new Class[] {String.class});
    assertNull(method);
  }

  @Test
  public void testForClassName() {
    ExpressionEvaluator evaluator = new ExpressionEvaluator();
    Class<?> clazz = evaluator.forClassName(location, "java.lang.String");
    assertNotNull(clazz);
  }

  @Test
  public void testForClassName_notFound() {
    ExpressionEvaluator evaluator = new ExpressionEvaluator();
    try {
      evaluator.forClassName(location, "inexistentClass");
    } catch (ExpressionException e) {
      System.out.println(e.getMessage());
    }
  }

  @Test
  public void testFindConstructor() {
    ExpressionEvaluator evaluator = new ExpressionEvaluator();
    Constructor<?> constructor =
        evaluator.findConstructor(location, String.class, char[].class, int.class, int.class);
    assertNotNull(constructor);
  }

  @Test
  public void testFindConstructor_notFound() {
    ExpressionEvaluator evaluator = new ExpressionEvaluator();
    Constructor<?> constructor =
        evaluator.findConstructor(location, String.class, int.class, int.class);
    assertNull(constructor);
  }

  @Test
  public void testInvokeConstructor() {
    ExpressionEvaluator evaluator = new ExpressionEvaluator();
    Constructor<?> constructor =
        evaluator.findConstructor(location, String.class, char[].class, int.class, int.class);
    EvaluationResult result =
        evaluator.invokeConstructor(
            location, String.class, constructor, new char[] {'a', 'b', 'c'}, 1, 2);
    assertEquals("bc", result.getValue());
    assertEquals(String.class, result.getValueClass());
  }

  @Test
  public void testGetFieldValue() throws Exception {
    ExpressionEvaluator evaluator = new ExpressionEvaluator();
    Field field = Person.class.getField("name");
    Person person = new Person();
    person.name = "aaa";
    EvaluationResult result = evaluator.getFieldValue(location, field, person);
    assertEquals("aaa", result.getValue());
    assertEquals(String.class, result.getValueClass());
  }

  @Test
  public void testGetFieldValue_optional() throws Exception {
    ExpressionEvaluator evaluator = new ExpressionEvaluator();
    Field field = Person.class.getField("optionalName");
    Person person = new Person();
    person.optionalName = Optional.of("aaa");
    EvaluationResult result = evaluator.getFieldValue(location, field, person);
    assertEquals("aaa", result.getValue());
    assertEquals(String.class, result.getValueClass());
  }

  @Test
  public void testGetFieldValue_optional_empty() throws Exception {
    ExpressionEvaluator evaluator = new ExpressionEvaluator();
    Field field = Person.class.getField("optionalName");
    Person person = new Person();
    person.optionalName = Optional.empty();
    EvaluationResult result = evaluator.getFieldValue(location, field, person);
    assertNull(result.getValue());
    assertEquals(String.class, result.getValueClass());
  }

  @Test
  public void testGetFieldValue_static() throws Exception {
    ExpressionEvaluator evaluator = new ExpressionEvaluator();
    Field field = Person.class.getField("staticName");
    EvaluationResult result = evaluator.getFieldValue(location, field, null);
    assertEquals("hoge", result.getValue());
    assertEquals(String.class, result.getValueClass());
  }

  @Test
  public void testGetFieldValue_static_optional() throws Exception {
    ExpressionEvaluator evaluator = new ExpressionEvaluator();
    Field field = Person.class.getField("staticOptionalName");
    EvaluationResult result = evaluator.getFieldValue(location, field, null);
    assertEquals("foo", result.getValue());
    assertEquals(String.class, result.getValueClass());
  }

  @Test
  public void testGetFieldValue_static_optionalInt() throws Exception {
    Person.staticAge = OptionalInt.of(10);
    ExpressionEvaluator evaluator = new ExpressionEvaluator();
    Field field = Person.class.getField("staticAge");
    EvaluationResult result = evaluator.getFieldValue(location, field, null);
    assertEquals(10, result.getValue());
    assertEquals(Integer.class, result.getValueClass());
  }

  @Test
  public void testGetFieldValue_static_optionalInt_empty() throws Exception {
    Person.staticAge = OptionalInt.empty();
    ExpressionEvaluator evaluator = new ExpressionEvaluator();
    Field field = Person.class.getField("staticAge");
    EvaluationResult result = evaluator.getFieldValue(location, field, null);
    assertNull(result.getValue());
    assertEquals(Integer.class, result.getValueClass());
  }

  @Test
  public void testGetFieldValue_optionalInt() throws Exception {
    ExpressionEvaluator evaluator = new ExpressionEvaluator();
    Field field = Person.class.getField("age");
    Person person = new Person();
    person.age = OptionalInt.of(10);
    EvaluationResult result = evaluator.getFieldValue(location, field, person);
    assertEquals(10, result.getValue());
    assertEquals(Integer.class, result.getValueClass());
  }

  @Test
  public void testGetFieldValue_optionalInt_empty() throws Exception {
    ExpressionEvaluator evaluator = new ExpressionEvaluator();
    Field field = Person.class.getField("age");
    Person person = new Person();
    person.age = OptionalInt.empty();
    EvaluationResult result = evaluator.getFieldValue(location, field, person);
    assertNull(result.getValue());
    assertEquals(Integer.class, result.getValueClass());
  }

  @Test
  public void testGetFieldValue_optionalLong() throws Exception {
    ExpressionEvaluator evaluator = new ExpressionEvaluator();
    Field field = Person.class.getField("salary");
    Person person = new Person();
    person.salary = OptionalLong.of(10L);
    EvaluationResult result = evaluator.getFieldValue(location, field, person);
    assertEquals(10L, result.getValue());
    assertEquals(Long.class, result.getValueClass());
  }

  @Test
  public void testGetFieldValue_optionalLong_empty() throws Exception {
    ExpressionEvaluator evaluator = new ExpressionEvaluator();
    Field field = Person.class.getField("salary");
    Person person = new Person();
    person.salary = OptionalLong.empty();
    EvaluationResult result = evaluator.getFieldValue(location, field, person);
    assertNull(result.getValue());
    assertEquals(Long.class, result.getValueClass());
  }

  @Test
  public void testGetFieldValue_optionalDouble() throws Exception {
    ExpressionEvaluator evaluator = new ExpressionEvaluator();
    Field field = Person.class.getField("temperature");
    Person person = new Person();
    person.temperature = OptionalDouble.of(10L);
    EvaluationResult result = evaluator.getFieldValue(location, field, person);
    assertEquals(10d, result.getValue());
    assertEquals(Double.class, result.getValueClass());
  }

  @Test
  public void testGetFieldValue_optionalDouble_empty() throws Exception {
    ExpressionEvaluator evaluator = new ExpressionEvaluator();
    Field field = Person.class.getField("temperature");
    Person person = new Person();
    person.temperature = OptionalDouble.empty();
    EvaluationResult result = evaluator.getFieldValue(location, field, person);
    assertNull(result.getValue());
    assertEquals(Double.class, result.getValueClass());
  }
}
