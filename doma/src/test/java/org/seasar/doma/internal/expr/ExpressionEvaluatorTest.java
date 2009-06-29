package org.seasar.doma.internal.expr;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import org.seasar.doma.domain.StringDomain;
import org.seasar.doma.internal.expr.EvaluationResult;
import org.seasar.doma.internal.expr.ExpressionEvaluator;
import org.seasar.doma.internal.expr.ExpressionException;
import org.seasar.doma.internal.expr.node.ExpressionLocation;

import junit.framework.TestCase;

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
        EvaluationResult result = evaluator
                .invokeMethod(location, method, "abcde", paramTypes, new Object[] {
                        2, 4 });
        assertEquals("cd", result.getValue());
        assertEquals(String.class, result.getValueClass());
    }

    public void testFindMethod() throws Exception {
        ExpressionEvaluator evaluator = new ExpressionEvaluator();
        Method method = evaluator.findMethod(location, "eq", new StringDomain(
                "aaa"), new Class[] { String.class });
        assertNotNull(method);
    }

    public void testFindMethod_notFound() throws Exception {
        ExpressionEvaluator evaluator = new ExpressionEvaluator();
        try {
            evaluator
                    .findMethod(location, "inexistentMethod", new StringDomain(
                            "aaa"), new Class[] { String.class });
            fail();
        } catch (ExpressionException ignored) {
            System.out.println(ignored.getMessage());
        }
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
        Constructor<?> constructor = evaluator
                .findConstructor(location, String.class, char[].class, int.class, int.class);
        assertNotNull(constructor);
    }

    public void testFindConstructor_notFound() throws Exception {
        ExpressionEvaluator evaluator = new ExpressionEvaluator();
        try {
            evaluator
                    .findConstructor(location, String.class, int.class, int.class);
        } catch (ExpressionException ignored) {
            System.out.println(ignored.getMessage());
        }
    }

    public void testInvokeConstructor() throws Exception {
        ExpressionEvaluator evaluator = new ExpressionEvaluator();
        Constructor<?> constructor = evaluator
                .findConstructor(location, String.class, char[].class, int.class, int.class);
        EvaluationResult result = evaluator
                .invokeConstructor(location, String.class, constructor, new char[] {
                        'a', 'b', 'c' }, 1, 2);
        assertEquals("bc", result.getValue());
        assertEquals(String.class, result.getValueClass());
    }
}
