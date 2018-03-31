package org.seasar.doma.internal.expr;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.OptionalLong;
import junit.framework.TestCase;
import org.seasar.doma.internal.expr.node.ExpressionLocation;

public class ExpressionEvaluatorTest extends TestCase {

  protected ExpressionLocation location = new ExpressionLocation("dummy expression", 0);

  public void testInvokeMethod() throws Exception {
    Class<?>[] paramTypes = new Class[] {int.class, int.class};
    var method = String.class.getMethod("substring", paramTypes);
    var evaluator = new ExpressionEvaluator();
    var result =
        evaluator.invokeMethod(
            location, method, "abcde", String.class, paramTypes, new Object[] {2, 4});
    assertEquals("cd", result.getValue());
    assertEquals(String.class, result.getValueClass());
  }

  public void testInvokeMethod_optional() throws Exception {
    var method = Person.class.getMethod("getOptionalName");
    var evaluator = new ExpressionEvaluator();
    var person = new Person();
    person.optionalName = Optional.of("aaa");
    var result =
        evaluator.invokeMethod(
            location, method, person, Person.class, new Class[] {}, new Object[] {});
    assertEquals("aaa", result.getValue());
    assertEquals(String.class, result.getValueClass());
  }

  public void testInvokeMethod_optional_empty() throws Exception {
    var method = Person.class.getMethod("getOptionalName");
    var evaluator = new ExpressionEvaluator();
    var person = new Person();
    person.optionalName = Optional.empty();
    var result =
        evaluator.invokeMethod(
            location, method, person, Person.class, new Class[] {}, new Object[] {});
    assertEquals(null, result.getValue());
    assertEquals(String.class, result.getValueClass());
  }

  public void testInvokeMethod_static_optional() throws Exception {
    var method = Person.class.getMethod("getStaticOptionalName");
    var evaluator = new ExpressionEvaluator();
    var result =
        evaluator.invokeMethod(
            location, method, null, Person.class, new Class[] {}, new Object[] {});
    assertEquals("foo", result.getValue());
    assertEquals(String.class, result.getValueClass());
  }

  public void testInvokeMethod_optionalInt() throws Exception {
    var method = Person.class.getMethod("getAge");
    var evaluator = new ExpressionEvaluator();
    var person = new Person();
    person.age = OptionalInt.of(10);
    var result =
        evaluator.invokeMethod(
            location, method, person, Person.class, new Class[] {}, new Object[] {});
    assertEquals(Integer.valueOf(10), result.getValue());
    assertEquals(Integer.class, result.getValueClass());
  }

  public void testInvokeMethod_optionalInt_empty() throws Exception {
    var method = Person.class.getMethod("getAge");
    var evaluator = new ExpressionEvaluator();
    var person = new Person();
    person.age = OptionalInt.empty();
    var result =
        evaluator.invokeMethod(
            location, method, person, Person.class, new Class[] {}, new Object[] {});
    assertNull(result.getValue());
    assertEquals(Integer.class, result.getValueClass());
  }

  public void testInvokeMethod_static_optionalInt() throws Exception {
    var method = Person.class.getMethod("getStaticAge");
    Person.staticAge = OptionalInt.of(10);
    var evaluator = new ExpressionEvaluator();
    var result =
        evaluator.invokeMethod(
            location, method, null, Person.class, new Class[] {}, new Object[] {});
    assertEquals(Integer.valueOf(10), result.getValue());
    assertEquals(Integer.class, result.getValueClass());
  }

  public void testInvokeMethod_static_optionalInt_empty() throws Exception {
    var method = Person.class.getMethod("getStaticAge");
    Person.staticAge = OptionalInt.empty();
    var evaluator = new ExpressionEvaluator();
    var result =
        evaluator.invokeMethod(
            location, method, null, Person.class, new Class[] {}, new Object[] {});
    assertNull(result.getValue());
    assertEquals(Integer.class, result.getValueClass());
  }

  public void testFindMethod() throws Exception {
    var evaluator = new ExpressionEvaluator();
    var method =
        evaluator.findMethod(
            "add", new ArrayList<Object>(), ArrayList.class, new Class[] {Object.class});
    assertNotNull(method);
    assertEquals(Collection.class, method.getDeclaringClass());
  }

  public void testFindMethod_String_is_subtype_of_Object() throws Exception {
    var evaluator = new ExpressionEvaluator();
    var method =
        evaluator.findMethod(
            "add", new ArrayList<Object>(), ArrayList.class, new Class[] {String.class});
    assertNotNull(method);
    assertEquals(Collection.class, method.getDeclaringClass());
  }

  public void testFindMethod_List_is_subtype_of_Object() throws Exception {
    var evaluator = new ExpressionEvaluator();
    var method =
        evaluator.findMethod(
            "add", new ArrayList<Object>(), ArrayList.class, new Class[] {List.class});
    assertNotNull(method);
    assertEquals(Collection.class, method.getDeclaringClass());
  }

  public void testFindMethod_List_is_subtype_of_Collection() throws Exception {
    var evaluator = new ExpressionEvaluator();
    var method =
        evaluator.findMethod(
            "addAll", new ArrayList<Object>(), ArrayList.class, new Class[] {List.class});
    assertNotNull(method);
    assertEquals(Collection.class, method.getDeclaringClass());
  }

  public void testFindMethod_overload_int() throws Exception {
    var evaluator = new ExpressionEvaluator();
    var method = evaluator.findMethod("indexOf", "string", String.class, new Class[] {int.class});
    assertNotNull(method);
    assertEquals(String.class, method.getDeclaringClass());
  }

  public void testFindMethod_overload_string() throws Exception {
    var evaluator = new ExpressionEvaluator();
    var method =
        evaluator.findMethod("indexOf", "string", String.class, new Class[] {String.class});
    assertNotNull(method);
    assertEquals(String.class, method.getDeclaringClass());
  }

  public void testFindMethod_autoBoxing() throws Exception {
    var evaluator = new ExpressionEvaluator();
    var method =
        evaluator.findMethod(
            "compareTo", Integer.valueOf(1), Integer.class, new Class[] {int.class});
    assertNotNull(method);
    assertEquals(Integer.class, method.getDeclaringClass());
  }

  public void testFindMethod_notFound() throws Exception {
    var evaluator = new ExpressionEvaluator();
    var method =
        evaluator.findMethod("inexistentMethod", "aaa", String.class, new Class[] {String.class});
    assertNull(method);
  }

  public void testForClassName() throws Exception {
    var evaluator = new ExpressionEvaluator();
    var clazz = evaluator.forClassName(location, "java.lang.String");
    assertNotNull(clazz);
  }

  public void testForClassName_notFound() throws Exception {
    var evaluator = new ExpressionEvaluator();
    try {
      evaluator.forClassName(location, "inexistentClass");
    } catch (ExpressionException ignored) {
      System.out.println(ignored.getMessage());
    }
  }

  public void testFindConstructor() throws Exception {
    var evaluator = new ExpressionEvaluator();
    var constructor =
        evaluator.findConstructor(location, String.class, char[].class, int.class, int.class);
    assertNotNull(constructor);
  }

  public void testFindConstructor_notFound() throws Exception {
    var evaluator = new ExpressionEvaluator();
    var constructor = evaluator.findConstructor(location, String.class, int.class, int.class);
    assertNull(constructor);
  }

  public void testInvokeConstructor() throws Exception {
    var evaluator = new ExpressionEvaluator();
    var constructor =
        evaluator.findConstructor(location, String.class, char[].class, int.class, int.class);
    var result =
        evaluator.invokeConstructor(
            location, String.class, constructor, new char[] {'a', 'b', 'c'}, 1, 2);
    assertEquals("bc", result.getValue());
    assertEquals(String.class, result.getValueClass());
  }

  public void testGetFieldValue() throws Exception {
    var evaluator = new ExpressionEvaluator();
    var field = Person.class.getField("name");
    var person = new Person();
    person.name = "aaa";
    var result = evaluator.getFieldValue(location, field, person);
    assertEquals("aaa", result.getValue());
    assertEquals(String.class, result.getValueClass());
  }

  public void testGetFieldValue_optional() throws Exception {
    var evaluator = new ExpressionEvaluator();
    var field = Person.class.getField("optionalName");
    var person = new Person();
    person.optionalName = Optional.of("aaa");
    var result = evaluator.getFieldValue(location, field, person);
    assertEquals("aaa", result.getValue());
    assertEquals(String.class, result.getValueClass());
  }

  public void testGetFieldValue_optional_empty() throws Exception {
    var evaluator = new ExpressionEvaluator();
    var field = Person.class.getField("optionalName");
    var person = new Person();
    person.optionalName = Optional.empty();
    var result = evaluator.getFieldValue(location, field, person);
    assertEquals(null, result.getValue());
    assertEquals(String.class, result.getValueClass());
  }

  public void testGetFieldValue_static() throws Exception {
    var evaluator = new ExpressionEvaluator();
    var field = Person.class.getField("staticName");
    var result = evaluator.getFieldValue(location, field, null);
    assertEquals("hoge", result.getValue());
    assertEquals(String.class, result.getValueClass());
  }

  public void testGetFieldValue_static_optional() throws Exception {
    var evaluator = new ExpressionEvaluator();
    var field = Person.class.getField("staticOptionalName");
    var result = evaluator.getFieldValue(location, field, null);
    assertEquals("foo", result.getValue());
    assertEquals(String.class, result.getValueClass());
  }

  public void testGetFieldValue_static_optionalInt() throws Exception {
    Person.staticAge = OptionalInt.of(10);
    var evaluator = new ExpressionEvaluator();
    var field = Person.class.getField("staticAge");
    var result = evaluator.getFieldValue(location, field, null);
    assertEquals(Integer.valueOf(10), result.getValue());
    assertEquals(Integer.class, result.getValueClass());
  }

  public void testGetFieldValue_static_optionalInt_empty() throws Exception {
    Person.staticAge = OptionalInt.empty();
    var evaluator = new ExpressionEvaluator();
    var field = Person.class.getField("staticAge");
    var result = evaluator.getFieldValue(location, field, null);
    assertNull(result.getValue());
    assertEquals(Integer.class, result.getValueClass());
  }

  public void testGetFieldValue_optionalInt() throws Exception {
    var evaluator = new ExpressionEvaluator();
    var field = Person.class.getField("age");
    var person = new Person();
    person.age = OptionalInt.of(10);
    var result = evaluator.getFieldValue(location, field, person);
    assertEquals(Integer.valueOf(10), result.getValue());
    assertEquals(Integer.class, result.getValueClass());
  }

  public void testGetFieldValue_optionalInt_empty() throws Exception {
    var evaluator = new ExpressionEvaluator();
    var field = Person.class.getField("age");
    var person = new Person();
    person.age = OptionalInt.empty();
    var result = evaluator.getFieldValue(location, field, person);
    assertNull(result.getValue());
    assertEquals(Integer.class, result.getValueClass());
  }

  public void testGetFieldValue_optionalLong() throws Exception {
    var evaluator = new ExpressionEvaluator();
    var field = Person.class.getField("salary");
    var person = new Person();
    person.salary = OptionalLong.of(10L);
    var result = evaluator.getFieldValue(location, field, person);
    assertEquals(Long.valueOf(10L), result.getValue());
    assertEquals(Long.class, result.getValueClass());
  }

  public void testGetFieldValue_optionalLong_empty() throws Exception {
    var evaluator = new ExpressionEvaluator();
    var field = Person.class.getField("salary");
    var person = new Person();
    person.salary = OptionalLong.empty();
    var result = evaluator.getFieldValue(location, field, person);
    assertNull(result.getValue());
    assertEquals(Long.class, result.getValueClass());
  }

  public void testGetFieldValue_optionalDouble() throws Exception {
    var evaluator = new ExpressionEvaluator();
    var field = Person.class.getField("temperature");
    var person = new Person();
    person.temperature = OptionalDouble.of(10L);
    var result = evaluator.getFieldValue(location, field, person);
    assertEquals(Double.valueOf(10d), result.getValue());
    assertEquals(Double.class, result.getValueClass());
  }

  public void testGetFieldValue_optionalDouble_empty() throws Exception {
    var evaluator = new ExpressionEvaluator();
    var field = Person.class.getField("temperature");
    var person = new Person();
    person.temperature = OptionalDouble.empty();
    var result = evaluator.getFieldValue(location, field, person);
    assertNull(result.getValue());
    assertEquals(Double.class, result.getValueClass());
  }
}
