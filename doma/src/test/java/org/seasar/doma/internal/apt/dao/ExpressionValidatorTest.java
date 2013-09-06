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
package org.seasar.doma.internal.apt.dao;

import java.util.Map;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.type.TypeMirror;

import org.seasar.doma.expr.ExpressionFunctions;
import org.seasar.doma.internal.apt.AptException;
import org.seasar.doma.internal.apt.AptTestCase;
import org.seasar.doma.internal.apt.ExpressionValidator;
import org.seasar.doma.internal.apt.decl.TypeDeclaration;
import org.seasar.doma.internal.apt.entity.Emp;
import org.seasar.doma.internal.expr.ExpressionParser;
import org.seasar.doma.internal.expr.node.ExpressionNode;
import org.seasar.doma.message.Message;

public class ExpressionValidatorTest extends AptTestCase {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        addSourcePath("src/main/java");
        addSourcePath("src/test/java");
    }

    public void testVariable_notFound() throws Exception {
        Class<?> target = ExpressionValidationDao.class;
        addCompilationUnit(target);
        compile();

        ExecutableElement methodElement = createMethodElement(target,
                "testEmp", Emp.class);
        Map<String, TypeMirror> parameterTypeMap = createParameterTypeMap(methodElement);
        ExpressionValidator validator = new ExpressionValidator(
                getProcessingEnvironment(), methodElement, parameterTypeMap);

        ExpressionNode node = new ExpressionParser("notFound").parse();
        try {
            validator.validate(node);
            fail();
        } catch (AptException expected) {
            System.out.println(expected);
            assertEquals(Message.DOMA4067, expected.getMessageResource());
        }
    }

    public void testMethod_found() throws Exception {
        Class<?> target = ExpressionValidationDao.class;
        addCompilationUnit(target);
        compile();

        ExecutableElement methodElement = createMethodElement(target,
                "testEmp", Emp.class);
        Map<String, TypeMirror> parameterTypeMap = createParameterTypeMap(methodElement);
        ExpressionValidator validator = new ExpressionValidator(
                getProcessingEnvironment(), methodElement, parameterTypeMap);

        ExpressionNode node = new ExpressionParser("emp.equals(emp)").parse();
        TypeDeclaration result = validator.validate(node);
        assertTrue(result.isBooleanType());
    }

    public void testMethod_overriderFound() throws Exception {
        Class<?> target = ExpressionValidationDao.class;
        addCompilationUnit(target);
        compile();

        ExecutableElement methodElement = createMethodElement(target,
                "testEmp", Emp.class);
        Map<String, TypeMirror> parameterTypeMap = createParameterTypeMap(methodElement);
        ExpressionValidator validator = new ExpressionValidator(
                getProcessingEnvironment(), methodElement, parameterTypeMap);

        ExpressionNode node = new ExpressionParser("\"aaa\".equals(\"aaa\")")
                .parse();
        TypeDeclaration result = validator.validate(node);
        assertTrue(result.isBooleanType());
    }

    public void testMethod_notFound() throws Exception {
        Class<?> target = ExpressionValidationDao.class;
        addCompilationUnit(target);
        compile();

        ExecutableElement methodElement = createMethodElement(target,
                "testEmp", Emp.class);
        Map<String, TypeMirror> parameterTypeMap = createParameterTypeMap(methodElement);
        ExpressionValidator validator = new ExpressionValidator(
                getProcessingEnvironment(), methodElement, parameterTypeMap);

        ExpressionNode node = new ExpressionParser(
                "emp.notFound(1, \"aaa\".length())").parse();
        try {
            validator.validate(node);
            fail();
        } catch (AptException expected) {
            System.out.println(expected);
            assertEquals(Message.DOMA4071, expected.getMessageResource());
        }
    }

    public void testMethod_foundFromCandidates() throws Exception {
        Class<?> target = ExpressionValidationDao.class;
        addCompilationUnit(target);
        compile();

        ExecutableElement methodElement = createMethodElement(target,
                "testEmp", Emp.class);
        Map<String, TypeMirror> parameterTypeMap = createParameterTypeMap(methodElement);
        ExpressionValidator validator = new ExpressionValidator(
                getProcessingEnvironment(), methodElement, parameterTypeMap);

        ExpressionNode node = new ExpressionParser("emp.hoge(\"aaa\")").parse();
        TypeDeclaration result = validator.validate(node);
        assertTrue(result.isNumberType());
    }

    public void testMethod_foundWithSupertype() throws Exception {
        Class<?> target = ExpressionValidationDao.class;
        addCompilationUnit(target);
        compile();

        ExecutableElement methodElement = createMethodElement(target,
                "testEmp", Emp.class);
        Map<String, TypeMirror> parameterTypeMap = createParameterTypeMap(methodElement);
        ExpressionValidator validator = new ExpressionValidator(
                getProcessingEnvironment(), methodElement, parameterTypeMap);

        ExpressionNode node = new ExpressionParser(
                "emp.hoge(new java.lang.Integer(1))").parse();
        TypeDeclaration result = validator.validate(node);
        assertTrue(result.isNumberType());
    }

    public void testMethod_foundWithTypeParameter() throws Exception {
        Class<?> target = ExpressionValidationDao.class;
        addCompilationUnit(target);
        compile();

        ExecutableElement methodElement = createMethodElement(target,
                "testEmp", Emp.class);
        Map<String, TypeMirror> parameterTypeMap = createParameterTypeMap(methodElement);
        ExpressionValidator validator = new ExpressionValidator(
                getProcessingEnvironment(), methodElement, parameterTypeMap);

        ExpressionNode node = new ExpressionParser(
                "emp.foo(new java.lang.Integer(1))").parse();
        TypeDeclaration result = validator.validate(node);
        assertTrue(result.isNumberType());
    }

    public void testStaticMethod_found() throws Exception {
        Class<?> target = ExpressionValidationDao.class;
        addCompilationUnit(target);
        compile();

        ExecutableElement methodElement = createMethodElement(target,
                "testEmp", Emp.class);
        Map<String, TypeMirror> parameterTypeMap = createParameterTypeMap(methodElement);
        ExpressionValidator validator = new ExpressionValidator(
                getProcessingEnvironment(), methodElement, parameterTypeMap);

        String expression = String.format("@%s@staticMethod(\"aaa\")",
                Emp.class.getName());
        ExpressionNode node = new ExpressionParser(expression).parse();
        TypeDeclaration result = validator.validate(node);
        assertTrue(result.isNumberType());
    }

    public void testStaticMethod_classNotFound() throws Exception {
        Class<?> target = ExpressionValidationDao.class;
        addCompilationUnit(target);
        compile();

        ExecutableElement methodElement = createMethodElement(target,
                "testEmp", Emp.class);
        Map<String, TypeMirror> parameterTypeMap = createParameterTypeMap(methodElement);
        ExpressionValidator validator = new ExpressionValidator(
                getProcessingEnvironment(), methodElement, parameterTypeMap);

        ExpressionNode node = new ExpressionParser("@Xxx@staticMethod(\"aaa\")")
                .parse();
        try {
            validator.validate(node);
            fail();
        } catch (AptException expected) {
            System.out.println(expected);
            assertEquals(Message.DOMA4145, expected.getMessageResource());
        }
    }

    public void testStaticMethod_methodNotFound() throws Exception {
        Class<?> target = ExpressionValidationDao.class;
        addCompilationUnit(target);
        compile();

        ExecutableElement methodElement = createMethodElement(target,
                "testEmp", Emp.class);
        Map<String, TypeMirror> parameterTypeMap = createParameterTypeMap(methodElement);
        ExpressionValidator validator = new ExpressionValidator(
                getProcessingEnvironment(), methodElement, parameterTypeMap);

        String expression = String.format("@%s@getName()", Emp.class.getName());
        ExpressionNode node = new ExpressionParser(expression).parse();
        try {
            validator.validate(node);
            fail();
        } catch (AptException expected) {
            System.out.println(expected);
            assertEquals(Message.DOMA4146, expected.getMessageResource());
        }
    }

    public void testStaticField_found() throws Exception {
        Class<?> target = ExpressionValidationDao.class;
        addCompilationUnit(target);
        compile();

        ExecutableElement methodElement = createMethodElement(target,
                "testEmp", Emp.class);
        Map<String, TypeMirror> parameterTypeMap = createParameterTypeMap(methodElement);
        ExpressionValidator validator = new ExpressionValidator(
                getProcessingEnvironment(), methodElement, parameterTypeMap);

        String expression = String.format("@%s@staticField",
                Emp.class.getName());
        ExpressionNode node = new ExpressionParser(expression).parse();
        TypeDeclaration result = validator.validate(node);
        assertTrue(result.isTextType());
    }

    public void testStaticField_classNotFound() throws Exception {
        Class<?> target = ExpressionValidationDao.class;
        addCompilationUnit(target);
        compile();

        ExecutableElement methodElement = createMethodElement(target,
                "testEmp", Emp.class);
        Map<String, TypeMirror> parameterTypeMap = createParameterTypeMap(methodElement);
        ExpressionValidator validator = new ExpressionValidator(
                getProcessingEnvironment(), methodElement, parameterTypeMap);

        ExpressionNode node = new ExpressionParser("@Xxx@staticField").parse();
        try {
            validator.validate(node);
            fail();
        } catch (AptException expected) {
            System.out.println(expected);
            assertEquals(Message.DOMA4145, expected.getMessageResource());
        }
    }

    public void testStaticField_fieldNotFound() throws Exception {
        Class<?> target = ExpressionValidationDao.class;
        addCompilationUnit(target);
        compile();

        ExecutableElement methodElement = createMethodElement(target,
                "testEmp", Emp.class);
        Map<String, TypeMirror> parameterTypeMap = createParameterTypeMap(methodElement);
        ExpressionValidator validator = new ExpressionValidator(
                getProcessingEnvironment(), methodElement, parameterTypeMap);

        String expression = String.format("@%s@name", Emp.class.getName());
        ExpressionNode node = new ExpressionParser(expression).parse();
        try {
            validator.validate(node);
            fail();
        } catch (AptException expected) {
            System.out.println(expected);
            assertEquals(Message.DOMA4148, expected.getMessageResource());
        }
    }

    public void testFunction_found() throws Exception {
        Class<?> target = ExpressionValidationDao.class;
        addCompilationUnit(ExpressionFunctions.class);
        addCompilationUnit(target);
        compile();

        ExecutableElement methodElement = createMethodElement(target,
                "testEmp", Emp.class);
        Map<String, TypeMirror> parameterTypeMap = createParameterTypeMap(methodElement);
        ExpressionValidator validator = new ExpressionValidator(
                getProcessingEnvironment(), methodElement, parameterTypeMap);

        ExpressionNode node = new ExpressionParser("@prefix(emp.name)").parse();
        TypeDeclaration result = validator.validate(node);
        assertTrue(result.isTextType());
    }

    public void testFunction_notFound() throws Exception {
        Class<?> target = ExpressionValidationDao.class;
        addCompilationUnit(ExpressionFunctions.class);
        addCompilationUnit(target);
        compile();

        ExecutableElement methodElement = createMethodElement(target,
                "testEmp", Emp.class);
        Map<String, TypeMirror> parameterTypeMap = createParameterTypeMap(methodElement);
        ExpressionValidator validator = new ExpressionValidator(
                getProcessingEnvironment(), methodElement, parameterTypeMap);

        ExpressionNode node = new ExpressionParser("@hoge(emp.name)").parse();
        try {
            validator.validate(node);
            fail();
        } catch (AptException expected) {
            System.out.println(expected);
            assertEquals(Message.DOMA4072, expected.getMessageResource());
        }
    }

    public void testCustomFunction_found() throws Exception {
        Class<?> target = ExpressionValidationDao.class;
        addCompilationUnit(ExpressionFunctions.class);
        addCompilationUnit(target);
        compile();

        ExecutableElement methodElement = createMethodElement(target,
                "testEmp", Emp.class);
        Map<String, TypeMirror> parameterTypeMap = createParameterTypeMap(methodElement);
        ExpressionValidator validator = new ExpressionValidator(
                getProcessingEnvironment(), methodElement, parameterTypeMap,
                MyExpressionFunctions.class.getName());

        ExpressionNode node = new ExpressionParser("@hello(emp.name)").parse();
        TypeDeclaration result = validator.validate(node);
        assertTrue(result.isTextType());
    }

    public void testCustomFunction_superClassMethodFound() throws Exception {
        Class<?> target = ExpressionValidationDao.class;
        addCompilationUnit(ExpressionFunctions.class);
        addCompilationUnit(target);
        compile();

        ExecutableElement methodElement = createMethodElement(target,
                "testEmp", Emp.class);
        Map<String, TypeMirror> parameterTypeMap = createParameterTypeMap(methodElement);
        ExpressionValidator validator = new ExpressionValidator(
                getProcessingEnvironment(), methodElement, parameterTypeMap,
                MyExpressionFunctions.class.getName());

        ExpressionNode node = new ExpressionParser("@prefix(emp.name)").parse();
        TypeDeclaration result = validator.validate(node);
        assertTrue(result.isTextType());
    }

    public void testCustomFunction_classNotfound() throws Exception {
        Class<?> target = ExpressionValidationDao.class;
        addCompilationUnit(ExpressionFunctions.class);
        addCompilationUnit(target);
        compile();

        ExecutableElement methodElement = createMethodElement(target,
                "testEmp", Emp.class);
        Map<String, TypeMirror> parameterTypeMap = createParameterTypeMap(methodElement);
        ExpressionValidator validator = new ExpressionValidator(
                getProcessingEnvironment(), methodElement, parameterTypeMap,
                "nonExistent");

        ExpressionNode node = new ExpressionParser("@hello(emp.name)").parse();
        try {
            validator.validate(node);
            fail();
        } catch (AptException expected) {
            System.out.println(expected);
            assertEquals(Message.DOMA4189, expected.getMessageResource());
        }
    }

    public void testCustomFunction_classIllegal() throws Exception {
        Class<?> target = ExpressionValidationDao.class;
        addCompilationUnit(ExpressionFunctions.class);
        addCompilationUnit(target);
        compile();

        ExecutableElement methodElement = createMethodElement(target,
                "testEmp", Emp.class);
        Map<String, TypeMirror> parameterTypeMap = createParameterTypeMap(methodElement);
        ExpressionValidator validator = new ExpressionValidator(
                getProcessingEnvironment(), methodElement, parameterTypeMap,
                "java.lang.String");

        ExpressionNode node = new ExpressionParser("@hello(emp.name)").parse();
        try {
            validator.validate(node);
            fail();
        } catch (AptException expected) {
            System.out.println(expected);
            assertEquals(Message.DOMA4190, expected.getMessageResource());
        }
    }

    public void testCustomFunction_notFound() throws Exception {
        Class<?> target = ExpressionValidationDao.class;
        addCompilationUnit(ExpressionFunctions.class);
        addCompilationUnit(target);
        compile();

        ExecutableElement methodElement = createMethodElement(target,
                "testEmp", Emp.class);
        Map<String, TypeMirror> parameterTypeMap = createParameterTypeMap(methodElement);
        ExpressionValidator validator = new ExpressionValidator(
                getProcessingEnvironment(), methodElement, parameterTypeMap,
                MyExpressionFunctions.class.getName());

        ExpressionNode node = new ExpressionParser("@hoge(emp.name)").parse();
        try {
            validator.validate(node);
            fail();
        } catch (AptException expected) {
            System.out.println(expected);
            assertEquals(Message.DOMA4072, expected.getMessageResource());
        }
    }

    public void testConstructor_notFound() throws Exception {
        Class<?> target = ExpressionValidationDao.class;
        addCompilationUnit(target);
        compile();

        ExecutableElement methodElement = createMethodElement(target,
                "testEmp", Emp.class);
        Map<String, TypeMirror> parameterTypeMap = createParameterTypeMap(methodElement);
        ExpressionValidator validator = new ExpressionValidator(
                getProcessingEnvironment(), methodElement, parameterTypeMap);

        ExpressionNode node = new ExpressionParser("new java.lang.String(1, 2)")
                .parse();
        try {
            validator.validate(node);
            fail();
        } catch (AptException expected) {
            System.out.println(expected);
            assertEquals(Message.DOMA4115, expected.getMessageResource());
        }
    }

    public void testFieldAccess() throws Exception {
        Class<?> target = ExpressionValidationDao.class;
        addCompilationUnit(target);
        compile();

        ExecutableElement methodElement = createMethodElement(target,
                "testEmp", Emp.class);
        Map<String, TypeMirror> parameterTypeMap = createParameterTypeMap(methodElement);
        ExpressionValidator validator = new ExpressionValidator(
                getProcessingEnvironment(), methodElement, parameterTypeMap);

        ExpressionNode node = new ExpressionParser("emp.id").parse();
        TypeDeclaration result = validator.validate(node);
        assertFalse(result.isUnknownType());
    }

    public void testMethodAccess() throws Exception {
        Class<?> target = ExpressionValidationDao.class;
        addCompilationUnit(target);
        compile();

        ExecutableElement methodElement = createMethodElement(target,
                "testEmp", Emp.class);
        Map<String, TypeMirror> parameterTypeMap = createParameterTypeMap(methodElement);
        ExpressionValidator validator = new ExpressionValidator(
                getProcessingEnvironment(), methodElement, parameterTypeMap);

        ExpressionNode node = new ExpressionParser("emp.getId()").parse();
        TypeDeclaration result = validator.validate(node);
        assertFalse(result.isUnknownType());
    }

    public void testConstructorAccess() throws Exception {
        Class<?> target = ExpressionValidationDao.class;
        addCompilationUnit(target);
        compile();

        ExecutableElement methodElement = createMethodElement(target,
                "testEmp", Emp.class);
        Map<String, TypeMirror> parameterTypeMap = createParameterTypeMap(methodElement);
        ExpressionValidator validator = new ExpressionValidator(
                getProcessingEnvironment(), methodElement, parameterTypeMap);

        ExpressionNode node = new ExpressionParser(
                "emp.id == new java.lang.Integer(1)").parse();
        TypeDeclaration result = validator.validate(node);
        assertFalse(result.isUnknownType());
    }

    public void testMethodAccess_withArguments() throws Exception {
        Class<?> target = ExpressionValidationDao.class;
        addCompilationUnit(target);
        compile();

        ExecutableElement methodElement = createMethodElement(target,
                "testEmp", Emp.class);
        Map<String, TypeMirror> parameterTypeMap = createParameterTypeMap(methodElement);
        ExpressionValidator validator = new ExpressionValidator(
                getProcessingEnvironment(), methodElement, parameterTypeMap);

        ExpressionNode node = new ExpressionParser("emp.add(2, 3)").parse();
        TypeDeclaration result = validator.validate(node);
        assertFalse(result.isUnknownType());
    }

    public void testEqOperator() throws Exception {
        Class<?> target = ExpressionValidationDao.class;
        addCompilationUnit(target);
        compile();

        ExecutableElement methodElement = createMethodElement(target,
                "testEmp", Emp.class);
        Map<String, TypeMirror> parameterTypeMap = createParameterTypeMap(methodElement);
        ExpressionValidator validator = new ExpressionValidator(
                getProcessingEnvironment(), methodElement, parameterTypeMap);

        ExpressionNode node = new ExpressionParser("emp.add(2, 3) == 5")
                .parse();
        TypeDeclaration result = validator.validate(node);
        assertFalse(result.isUnknownType());
    }

    public void testUnreferencedParameter() throws Exception {
        Class<?> target = ExpressionValidationDao.class;
        addCompilationUnit(target);
        compile();

        ExecutableElement methodElement = createMethodElement(target,
                "testEmp", Emp.class);
        Map<String, TypeMirror> parameterTypeMap = createParameterTypeMap(methodElement);
        ExpressionValidator validator = new ExpressionValidator(
                getProcessingEnvironment(), methodElement, parameterTypeMap);

        ExpressionNode node = new ExpressionParser("true").parse();
        validator.validate(node);
        assertFalse(validator.getValidatedParameterNames().contains("emp"));
    }

    public void testAddOperator_number_number() throws Exception {
        Class<?> target = ExpressionValidationDao.class;
        addCompilationUnit(target);
        compile();

        ExecutableElement methodElement = createMethodElement(target,
                "testEmp", Emp.class);
        Map<String, TypeMirror> parameterTypeMap = createParameterTypeMap(methodElement);
        ExpressionValidator validator = new ExpressionValidator(
                getProcessingEnvironment(), methodElement, parameterTypeMap);

        ExpressionNode node = new ExpressionParser("1 + 2").parse();
        TypeDeclaration result = validator.validate(node);
        assertTrue(result.isNumberType());
    }

    public void testAddOperator_number_text() throws Exception {
        Class<?> target = ExpressionValidationDao.class;
        addCompilationUnit(target);
        compile();

        ExecutableElement methodElement = createMethodElement(target,
                "testEmp", Emp.class);
        Map<String, TypeMirror> parameterTypeMap = createParameterTypeMap(methodElement);
        ExpressionValidator validator = new ExpressionValidator(
                getProcessingEnvironment(), methodElement, parameterTypeMap);

        ExpressionNode node = new ExpressionParser("1 + \"2\"").parse();
        try {
            validator.validate(node);
            fail();
        } catch (AptException expected) {
            System.out.println(expected);
            assertEquals(Message.DOMA4121, expected.getMessageResource());
        }
    }

    public void testAddOperator_text_text() throws Exception {
        Class<?> target = ExpressionValidationDao.class;
        addCompilationUnit(target);
        compile();

        ExecutableElement methodElement = createMethodElement(target,
                "testEmp", Emp.class);
        Map<String, TypeMirror> parameterTypeMap = createParameterTypeMap(methodElement);
        ExpressionValidator validator = new ExpressionValidator(
                getProcessingEnvironment(), methodElement, parameterTypeMap);

        ExpressionNode node = new ExpressionParser("\"1\" + \"2\"").parse();
        TypeDeclaration result = validator.validate(node);
        assertTrue(result.isTextType());
    }

    public void testAddOperator_text_number() throws Exception {
        Class<?> target = ExpressionValidationDao.class;
        addCompilationUnit(target);
        compile();

        ExecutableElement methodElement = createMethodElement(target,
                "testEmp", Emp.class);
        Map<String, TypeMirror> parameterTypeMap = createParameterTypeMap(methodElement);
        ExpressionValidator validator = new ExpressionValidator(
                getProcessingEnvironment(), methodElement, parameterTypeMap);

        ExpressionNode node = new ExpressionParser("\"2\" + 1").parse();
        try {
            validator.validate(node);
            fail();
        } catch (AptException expected) {
            System.out.println(expected);
            assertEquals(Message.DOMA4126, expected.getMessageResource());
        }
    }

}
