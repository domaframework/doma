/*
 * Copyright 2004-2010 the Seasar Foundation and the Others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.seasar.doma.internal.expr;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import junit.framework.TestCase;

import org.seasar.doma.internal.expr.node.ExpressionLocation;

/**
 * @author taedium
 * 
 */
public class ExpressionEvaluatorTest extends TestCase {

    protected ExpressionLocation location = new ExpressionLocation(
            "dummy expression", 0);

    public void testInvokeMethod() throws Exception {
        Class<?>[] paramTypes = new Class[] { int.class, int.class };
        Method method = String.class.getMethod("substring", paramTypes);
        ExpressionEvaluator evaluator = new ExpressionEvaluator();
        EvaluationResult result = evaluator.invokeMethod(location, method,
                "abcde", String.class, paramTypes, new Object[] { 2, 4 });
        assertEquals("cd", result.getValue());
        assertEquals(String.class, result.getValueClass());
    }

    public void testFindMethod() throws Exception {
        ExpressionEvaluator evaluator = new ExpressionEvaluator();
        Method method = evaluator.findMethod("add", new ArrayList<Object>(),
                ArrayList.class, new Class[] { Object.class });
        assertNotNull(method);
        assertEquals(Collection.class, method.getDeclaringClass());
    }

    public void testFindMethod_String_is_subtype_of_Object() throws Exception {
        ExpressionEvaluator evaluator = new ExpressionEvaluator();
        Method method = evaluator.findMethod("add", new ArrayList<Object>(),
                ArrayList.class, new Class[] { String.class });
        assertNotNull(method);
        assertEquals(Collection.class, method.getDeclaringClass());
    }

    public void testFindMethod_List_is_subtype_of_Object() throws Exception {
        ExpressionEvaluator evaluator = new ExpressionEvaluator();
        Method method = evaluator.findMethod("add", new ArrayList<Object>(),
                ArrayList.class, new Class[] { List.class });
        assertNotNull(method);
        assertEquals(Collection.class, method.getDeclaringClass());
    }

    public void testFindMethod_List_is_subtype_of_Collection() throws Exception {
        ExpressionEvaluator evaluator = new ExpressionEvaluator();
        Method method = evaluator.findMethod("addAll", new ArrayList<Object>(),
                ArrayList.class, new Class[] { List.class });
        assertNotNull(method);
        assertEquals(Collection.class, method.getDeclaringClass());
    }

    public void testFindMethod_overload_int() throws Exception {
        ExpressionEvaluator evaluator = new ExpressionEvaluator();
        Method method = evaluator.findMethod("indexOf", "string", String.class,
                new Class[] { int.class });
        assertNotNull(method);
        assertEquals(String.class, method.getDeclaringClass());
    }

    public void testFindMethod_overload_string() throws Exception {
        ExpressionEvaluator evaluator = new ExpressionEvaluator();
        Method method = evaluator.findMethod("indexOf", "string", String.class,
                new Class[] { String.class });
        assertNotNull(method);
        assertEquals(String.class, method.getDeclaringClass());
    }

    public void testFindMethod_autoBoxing() throws Exception {
        ExpressionEvaluator evaluator = new ExpressionEvaluator();
        Method method = evaluator.findMethod("compareTo", Integer.valueOf(1),
                Integer.class, new Class[] { int.class });
        assertNotNull(method);
        assertEquals(Integer.class, method.getDeclaringClass());
    }

    public void testFindMethod_notFound() throws Exception {
        ExpressionEvaluator evaluator = new ExpressionEvaluator();
        Method method = evaluator.findMethod("inexistentMethod", "aaa",
                String.class, new Class[] { String.class });
        assertNull(method);
    }

    public void testForClassName() throws Exception {
        ExpressionEvaluator evaluator = new ExpressionEvaluator();
        Class<?> clazz = evaluator.forClassName(location, "java.lang.String");
        assertNotNull(clazz);
    }

    public void testForClassName_notFound() throws Exception {
        ExpressionEvaluator evaluator = new ExpressionEvaluator();
        try {
            evaluator.forClassName(location, "inexistentClass");
        } catch (ExpressionException ignored) {
            System.out.println(ignored.getMessage());
        }
    }

    public void testFindConstructor() throws Exception {
        ExpressionEvaluator evaluator = new ExpressionEvaluator();
        Constructor<?> constructor = evaluator.findConstructor(location,
                String.class, char[].class, int.class, int.class);
        assertNotNull(constructor);
    }

    public void testFindConstructor_notFound() throws Exception {
        ExpressionEvaluator evaluator = new ExpressionEvaluator();
        Constructor<?> constructor = evaluator.findConstructor(location,
                String.class, int.class, int.class);
        assertNull(constructor);
    }

    public void testInvokeConstructor() throws Exception {
        ExpressionEvaluator evaluator = new ExpressionEvaluator();
        Constructor<?> constructor = evaluator.findConstructor(location,
                String.class, char[].class, int.class, int.class);
        EvaluationResult result = evaluator.invokeConstructor(location,
                String.class, constructor, new char[] { 'a', 'b', 'c' }, 1, 2);
        assertEquals("bc", result.getValue());
        assertEquals(String.class, result.getValueClass());
    }
}
