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
package org.seasar.doma.internal.apt.validator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.Map;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.type.TypeMirror;
import org.junit.jupiter.api.Test;
import org.seasar.doma.internal.apt.AbstractCompilerTest;
import org.seasar.doma.internal.apt.AptException;
import org.seasar.doma.internal.apt.RoundContext;
import org.seasar.doma.internal.apt.TestProcessor;
import org.seasar.doma.internal.apt.decl.TypeDeclaration;
import org.seasar.doma.internal.expr.ExpressionParser;
import org.seasar.doma.internal.expr.node.ExpressionNode;
import org.seasar.doma.message.Message;

@SuppressWarnings("ThrowablePrintedToSystemOut")
class ExpressionValidatorTest extends AbstractCompilerTest {

  @Test
  void testVariable_notFound() throws Exception {
    Class<?> target = ExpressionValidationDao.class;
    addCompilationUnit(target);
    addProcessor(
        new TestProcessor() {
          @Override
          protected void run(RoundContext ctx) {
            ExecutableElement methodElement = createMethodElement(target, "testEmp", Emp.class);
            Map<String, TypeMirror> parameterTypeMap = createParameterTypeMap(methodElement);
            ExpressionValidator validator =
                new ExpressionValidator(ctx, methodElement, parameterTypeMap);

            ExpressionNode node = new ExpressionParser("notFound").parse();
            try {
              validator.validate(node);
              fail();
            } catch (AptException expected) {
              System.out.println(expected);
              assertEquals(Message.DOMA4067, expected.getMessageResource());
            }
          }
        });
    compile();
    assertTrue(getCompiledResult());
  }

  @Test
  void testMethod_found() throws Exception {
    Class<?> target = ExpressionValidationDao.class;
    addCompilationUnit(target);
    addProcessor(
        new TestProcessor() {
          @Override
          protected void run(RoundContext ctx) {
            ExecutableElement methodElement = createMethodElement(target, "testEmp", Emp.class);
            Map<String, TypeMirror> parameterTypeMap = createParameterTypeMap(methodElement);
            ExpressionValidator validator =
                new ExpressionValidator(ctx, methodElement, parameterTypeMap);

            ExpressionNode node = new ExpressionParser("emp.equals(emp)").parse();
            TypeDeclaration result = validator.validate(node);
            assertTrue(result.isBooleanType());
          }
        });
    compile();
    assertTrue(getCompiledResult());
  }

  @Test
  void testMethod_overriderFound() throws Exception {
    Class<?> target = ExpressionValidationDao.class;
    addCompilationUnit(target);
    addProcessor(
        new TestProcessor() {
          @Override
          protected void run(RoundContext ctx) {
            ExecutableElement methodElement = createMethodElement(target, "testEmp", Emp.class);
            Map<String, TypeMirror> parameterTypeMap = createParameterTypeMap(methodElement);
            ExpressionValidator validator =
                new ExpressionValidator(ctx, methodElement, parameterTypeMap);

            ExpressionNode node = new ExpressionParser("\"aaa\".equals(\"aaa\")").parse();
            TypeDeclaration result = validator.validate(node);
            assertTrue(result.isBooleanType());
          }
        });
    compile();
    assertTrue(getCompiledResult());
  }

  @Test
  void testMethod_notFound() throws Exception {
    Class<?> target = ExpressionValidationDao.class;
    addCompilationUnit(target);
    addProcessor(
        new TestProcessor() {
          @Override
          protected void run(RoundContext ctx) {
            ExecutableElement methodElement = createMethodElement(target, "testEmp", Emp.class);
            Map<String, TypeMirror> parameterTypeMap = createParameterTypeMap(methodElement);
            ExpressionValidator validator =
                new ExpressionValidator(ctx, methodElement, parameterTypeMap);
            ExpressionNode node = new ExpressionParser("emp.notFound(1, \"aaa\".length())").parse();
            try {
              validator.validate(node);
              fail();
            } catch (AptException expected) {
              System.out.println(expected);
              assertEquals(Message.DOMA4071, expected.getMessageResource());
            }
          }
        });
    compile();
    assertTrue(getCompiledResult());
  }

  @Test
  void testMethod_foundFromCandidates() throws Exception {
    Class<?> target = ExpressionValidationDao.class;
    addCompilationUnit(target);
    addProcessor(
        new TestProcessor() {
          @Override
          protected void run(RoundContext ctx) {
            ExecutableElement methodElement = createMethodElement(target, "testEmp", Emp.class);
            Map<String, TypeMirror> parameterTypeMap = createParameterTypeMap(methodElement);
            ExpressionValidator validator =
                new ExpressionValidator(ctx, methodElement, parameterTypeMap);

            ExpressionNode node = new ExpressionParser("emp.hoge(\"aaa\")").parse();
            TypeDeclaration result = validator.validate(node);
            assertTrue(result.isNumberType());
          }
        });
    compile();
    assertTrue(getCompiledResult());
  }

  @Test
  void testMethod_foundWithSupertype() throws Exception {
    Class<?> target = ExpressionValidationDao.class;
    addCompilationUnit(target);
    addProcessor(
        new TestProcessor() {
          @Override
          protected void run(RoundContext ctx) {
            ExecutableElement methodElement = createMethodElement(target, "testEmp", Emp.class);
            Map<String, TypeMirror> parameterTypeMap = createParameterTypeMap(methodElement);
            ExpressionValidator validator =
                new ExpressionValidator(ctx, methodElement, parameterTypeMap);

            ExpressionNode node =
                new ExpressionParser("emp.hoge(new java.lang.Integer(1))").parse();
            TypeDeclaration result = validator.validate(node);
            assertTrue(result.isNumberType());
          }
        });
    compile();
    assertTrue(getCompiledResult());
  }

  @Test
  void testMethod_foundWithTypeParameter() throws Exception {
    Class<?> target = ExpressionValidationDao.class;
    addCompilationUnit(target);
    addProcessor(
        new TestProcessor() {
          @Override
          protected void run(RoundContext ctx) {
            ExecutableElement methodElement = createMethodElement(target, "testEmp", Emp.class);
            Map<String, TypeMirror> parameterTypeMap = createParameterTypeMap(methodElement);
            ExpressionValidator validator =
                new ExpressionValidator(ctx, methodElement, parameterTypeMap);

            ExpressionNode node = new ExpressionParser("emp.foo(new java.lang.Integer(1))").parse();
            TypeDeclaration result = validator.validate(node);
            assertTrue(result.isNumberType());
          }
        });
    compile();
    assertTrue(getCompiledResult());
  }

  @Test
  void testStaticMethod_found() throws Exception {
    Class<?> target = ExpressionValidationDao.class;
    addCompilationUnit(target);
    addProcessor(
        new TestProcessor() {
          @Override
          protected void run(RoundContext ctx) {
            ExecutableElement methodElement = createMethodElement(target, "testEmp", Emp.class);
            Map<String, TypeMirror> parameterTypeMap = createParameterTypeMap(methodElement);
            ExpressionValidator validator =
                new ExpressionValidator(ctx, methodElement, parameterTypeMap);

            String expression = String.format("@%s@staticMethod(\"aaa\")", Emp.class.getName());
            ExpressionNode node = new ExpressionParser(expression).parse();
            TypeDeclaration result = validator.validate(node);
            assertTrue(result.isNumberType());
          }
        });
    compile();
    assertTrue(getCompiledResult());
  }

  @Test
  void testStaticMethod_classNotFound() throws Exception {
    Class<?> target = ExpressionValidationDao.class;
    addCompilationUnit(target);
    addProcessor(
        new TestProcessor() {
          @Override
          protected void run(RoundContext ctx) {
            ExecutableElement methodElement = createMethodElement(target, "testEmp", Emp.class);
            Map<String, TypeMirror> parameterTypeMap = createParameterTypeMap(methodElement);
            ExpressionValidator validator =
                new ExpressionValidator(ctx, methodElement, parameterTypeMap);

            ExpressionNode node = new ExpressionParser("@Xxx@staticMethod(\"aaa\")").parse();
            try {
              validator.validate(node);
              fail();
            } catch (AptException expected) {
              System.out.println(expected);
              assertEquals(Message.DOMA4145, expected.getMessageResource());
            }
          }
        });
    compile();
    assertTrue(getCompiledResult());
  }

  @Test
  void testStaticMethod_methodNotFound() throws Exception {
    Class<?> target = ExpressionValidationDao.class;
    addCompilationUnit(target);
    addProcessor(
        new TestProcessor() {
          @Override
          protected void run(RoundContext ctx) {
            ExecutableElement methodElement = createMethodElement(target, "testEmp", Emp.class);
            Map<String, TypeMirror> parameterTypeMap = createParameterTypeMap(methodElement);
            ExpressionValidator validator =
                new ExpressionValidator(ctx, methodElement, parameterTypeMap);

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
        });
    compile();
    assertTrue(getCompiledResult());
  }

  @Test
  void testStaticField_found() throws Exception {
    Class<?> target = ExpressionValidationDao.class;
    addCompilationUnit(target);
    addProcessor(
        new TestProcessor() {
          @Override
          protected void run(RoundContext ctx) {
            ExecutableElement methodElement = createMethodElement(target, "testEmp", Emp.class);
            Map<String, TypeMirror> parameterTypeMap = createParameterTypeMap(methodElement);
            ExpressionValidator validator =
                new ExpressionValidator(ctx, methodElement, parameterTypeMap);

            String expression = String.format("@%s@staticField", Emp.class.getName());
            ExpressionNode node = new ExpressionParser(expression).parse();
            TypeDeclaration result = validator.validate(node);
            assertTrue(result.isTextType());
          }
        });
    compile();
    assertTrue(getCompiledResult());
  }

  @Test
  void testStaticField_classNotFound() throws Exception {
    Class<?> target = ExpressionValidationDao.class;
    addCompilationUnit(target);
    addProcessor(
        new TestProcessor() {
          @Override
          protected void run(RoundContext ctx) {
            ExecutableElement methodElement = createMethodElement(target, "testEmp", Emp.class);
            Map<String, TypeMirror> parameterTypeMap = createParameterTypeMap(methodElement);
            ExpressionValidator validator =
                new ExpressionValidator(ctx, methodElement, parameterTypeMap);

            ExpressionNode node = new ExpressionParser("@Xxx@staticField").parse();
            try {
              validator.validate(node);
              fail();
            } catch (AptException expected) {
              System.out.println(expected);
              assertEquals(Message.DOMA4145, expected.getMessageResource());
            }
          }
        });
    compile();
    assertTrue(getCompiledResult());
  }

  @Test
  void testStaticField_fieldNotFound() throws Exception {
    Class<?> target = ExpressionValidationDao.class;
    addCompilationUnit(target);
    addProcessor(
        new TestProcessor() {
          @Override
          protected void run(RoundContext ctx) {
            ExecutableElement methodElement = createMethodElement(target, "testEmp", Emp.class);
            Map<String, TypeMirror> parameterTypeMap = createParameterTypeMap(methodElement);
            ExpressionValidator validator =
                new ExpressionValidator(ctx, methodElement, parameterTypeMap);

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
        });
    compile();
    assertTrue(getCompiledResult());
  }

  @Test
  void testFunction_found() throws Exception {
    Class<?> target = ExpressionValidationDao.class;
    addCompilationUnit(target);
    addProcessor(
        new TestProcessor() {
          @Override
          protected void run(RoundContext ctx) {
            ExecutableElement methodElement = createMethodElement(target, "testEmp", Emp.class);
            Map<String, TypeMirror> parameterTypeMap = createParameterTypeMap(methodElement);
            ExpressionValidator validator =
                new ExpressionValidator(ctx, methodElement, parameterTypeMap);

            ExpressionNode node = new ExpressionParser("@prefix(emp.name)").parse();
            TypeDeclaration result = validator.validate(node);
            assertTrue(result.isTextType());
          }
        });
    compile();
    assertTrue(getCompiledResult());
  }

  @Test
  void testFunction_notFound() throws Exception {
    Class<?> target = ExpressionValidationDao.class;
    addCompilationUnit(target);
    addProcessor(
        new TestProcessor() {
          @Override
          protected void run(RoundContext ctx) {
            ExecutableElement methodElement = createMethodElement(target, "testEmp", Emp.class);
            Map<String, TypeMirror> parameterTypeMap = createParameterTypeMap(methodElement);
            ExpressionValidator validator =
                new ExpressionValidator(ctx, methodElement, parameterTypeMap);

            ExpressionNode node = new ExpressionParser("@hoge(emp.name)").parse();
            try {
              validator.validate(node);
              fail();
            } catch (AptException expected) {
              System.out.println(expected);
              assertEquals(Message.DOMA4072, expected.getMessageResource());
            }
          }
        });
    compile();
    assertTrue(getCompiledResult());
  }

  @Test
  void testCustomFunction_found() throws Exception {
    Class<?> target = ExpressionValidationDao.class;
    addCompilationUnit(target);
    addProcessor(
        new TestProcessor() {
          @Override
          protected void run(RoundContext ctx) {
            ExecutableElement methodElement = createMethodElement(target, "testEmp", Emp.class);
            Map<String, TypeMirror> parameterTypeMap = createParameterTypeMap(methodElement);
            ExpressionValidator validator =
                new ExpressionValidator(
                    ctx, methodElement, parameterTypeMap, MyExpressionFunctions.class.getName());

            ExpressionNode node = new ExpressionParser("@hello(emp.name)").parse();
            TypeDeclaration result = validator.validate(node);
            assertTrue(result.isTextType());
          }
        });
    compile();
    assertTrue(getCompiledResult());
  }

  @Test
  void testCustomFunction_superClassMethodFound() throws Exception {
    Class<?> target = ExpressionValidationDao.class;
    addCompilationUnit(target);
    addProcessor(
        new TestProcessor() {
          @Override
          protected void run(RoundContext ctx) {
            ExecutableElement methodElement = createMethodElement(target, "testEmp", Emp.class);
            Map<String, TypeMirror> parameterTypeMap = createParameterTypeMap(methodElement);
            ExpressionValidator validator =
                new ExpressionValidator(
                    ctx, methodElement, parameterTypeMap, MyExpressionFunctions.class.getName());

            ExpressionNode node = new ExpressionParser("@prefix(emp.name)").parse();
            TypeDeclaration result = validator.validate(node);
            assertTrue(result.isTextType());
          }
        });
    compile();
    assertTrue(getCompiledResult());
  }

  @Test
  void testCustomFunction_classNotFound() throws Exception {
    Class<?> target = ExpressionValidationDao.class;
    addCompilationUnit(target);
    addProcessor(
        new TestProcessor() {
          @Override
          protected void run(RoundContext ctx) {
            ExecutableElement methodElement = createMethodElement(target, "testEmp", Emp.class);
            Map<String, TypeMirror> parameterTypeMap = createParameterTypeMap(methodElement);
            ExpressionValidator validator =
                new ExpressionValidator(ctx, methodElement, parameterTypeMap, "nonExistent");

            ExpressionNode node = new ExpressionParser("@hello(emp.name)").parse();
            try {
              validator.validate(node);
              fail();
            } catch (AptException expected) {
              System.out.println(expected);
              assertEquals(Message.DOMA4189, expected.getMessageResource());
            }
          }
        });
    compile();
    assertTrue(getCompiledResult());
  }

  @Test
  void testCustomFunction_classIllegal() throws Exception {
    Class<?> target = ExpressionValidationDao.class;
    addCompilationUnit(target);
    addProcessor(
        new TestProcessor() {
          @Override
          protected void run(RoundContext ctx) {
            ExecutableElement methodElement = createMethodElement(target, "testEmp", Emp.class);
            Map<String, TypeMirror> parameterTypeMap = createParameterTypeMap(methodElement);
            ExpressionValidator validator =
                new ExpressionValidator(ctx, methodElement, parameterTypeMap, "java.lang.String");

            ExpressionNode node = new ExpressionParser("@hello(emp.name)").parse();
            try {
              validator.validate(node);
              fail();
            } catch (AptException expected) {
              System.out.println(expected);
              assertEquals(Message.DOMA4190, expected.getMessageResource());
            }
          }
        });
    compile();
    assertTrue(getCompiledResult());
  }

  @Test
  void testCustomFunction_notFound() throws Exception {
    Class<?> target = ExpressionValidationDao.class;
    addCompilationUnit(target);
    addProcessor(
        new TestProcessor() {
          @Override
          protected void run(RoundContext ctx) {
            ExecutableElement methodElement = createMethodElement(target, "testEmp", Emp.class);
            Map<String, TypeMirror> parameterTypeMap = createParameterTypeMap(methodElement);
            ExpressionValidator validator =
                new ExpressionValidator(
                    ctx, methodElement, parameterTypeMap, MyExpressionFunctions.class.getName());

            ExpressionNode node = new ExpressionParser("@hoge(emp.name)").parse();
            try {
              validator.validate(node);
              fail();
            } catch (AptException expected) {
              System.out.println(expected);
              assertEquals(Message.DOMA4072, expected.getMessageResource());
            }
          }
        });
    compile();
    assertTrue(getCompiledResult());
  }

  @Test
  void testConstructor_notFound() throws Exception {
    Class<?> target = ExpressionValidationDao.class;
    addCompilationUnit(target);
    addProcessor(
        new TestProcessor() {
          @Override
          protected void run(RoundContext ctx) {
            ExecutableElement methodElement = createMethodElement(target, "testEmp", Emp.class);
            Map<String, TypeMirror> parameterTypeMap = createParameterTypeMap(methodElement);
            ExpressionValidator validator =
                new ExpressionValidator(ctx, methodElement, parameterTypeMap);

            ExpressionNode node = new ExpressionParser("new java.lang.String(1, 2)").parse();
            try {
              validator.validate(node);
              fail();
            } catch (AptException expected) {
              System.out.println(expected);
              assertEquals(Message.DOMA4115, expected.getMessageResource());
            }
          }
        });
    compile();
    assertTrue(getCompiledResult());
  }

  @Test
  void testFieldAccess() throws Exception {
    Class<?> target = ExpressionValidationDao.class;
    addCompilationUnit(target);
    addProcessor(
        new TestProcessor() {
          @Override
          protected void run(RoundContext ctx) {
            ExecutableElement methodElement = createMethodElement(target, "testEmp", Emp.class);
            Map<String, TypeMirror> parameterTypeMap = createParameterTypeMap(methodElement);
            ExpressionValidator validator =
                new ExpressionValidator(ctx, methodElement, parameterTypeMap);

            ExpressionNode node = new ExpressionParser("emp.id").parse();
            TypeDeclaration result = validator.validate(node);
            assertFalse(result.isUnknownType());
          }
        });
    compile();
    assertTrue(getCompiledResult());
  }

  @Test
  void testFieldAccess_optional() throws Exception {
    Class<?> target = ExpressionValidationDao.class;
    addCompilationUnit(target);
    addProcessor(
        new TestProcessor() {
          @Override
          protected void run(RoundContext ctx) {
            ExecutableElement methodElement =
                createMethodElement(target, "testPerson", Person.class);
            Map<String, TypeMirror> parameterTypeMap = createParameterTypeMap(methodElement);
            ExpressionValidator validator =
                new ExpressionValidator(ctx, methodElement, parameterTypeMap);

            ExpressionNode node = new ExpressionParser("person.name").parse();
            TypeDeclaration result = validator.validate(node);
            assertTrue(result.isTextType());
          }
        });
    compile();
    assertTrue(getCompiledResult());
  }

  @Test
  void testFieldAccess_optionalInt() throws Exception {
    Class<?> target = ExpressionValidationDao.class;
    addCompilationUnit(target);
    addProcessor(
        new TestProcessor() {
          @Override
          protected void run(RoundContext ctx) {
            ExecutableElement methodElement =
                createMethodElement(target, "testPerson", Person.class);
            Map<String, TypeMirror> parameterTypeMap = createParameterTypeMap(methodElement);
            ExpressionValidator validator =
                new ExpressionValidator(ctx, methodElement, parameterTypeMap);

            ExpressionNode node = new ExpressionParser("person.age").parse();
            TypeDeclaration result = validator.validate(node);
            assertTrue(result.isNumberType());
          }
        });
    compile();
    assertTrue(getCompiledResult());
  }

  @Test
  void testFieldAccess_optionalLong() throws Exception {
    Class<?> target = ExpressionValidationDao.class;
    addCompilationUnit(target);
    addProcessor(
        new TestProcessor() {
          @Override
          protected void run(RoundContext ctx) {
            ExecutableElement methodElement =
                createMethodElement(target, "testPerson", Person.class);
            Map<String, TypeMirror> parameterTypeMap = createParameterTypeMap(methodElement);
            ExpressionValidator validator =
                new ExpressionValidator(ctx, methodElement, parameterTypeMap);

            ExpressionNode node = new ExpressionParser("person.salary").parse();
            TypeDeclaration result = validator.validate(node);
            assertTrue(result.isNumberType());
          }
        });
    compile();
    assertTrue(getCompiledResult());
  }

  @Test
  void testFieldAccess_optionalDouble() throws Exception {
    Class<?> target = ExpressionValidationDao.class;
    addCompilationUnit(target);
    addProcessor(
        new TestProcessor() {
          @Override
          protected void run(RoundContext ctx) {
            ExecutableElement methodElement =
                createMethodElement(target, "testPerson", Person.class);
            Map<String, TypeMirror> parameterTypeMap = createParameterTypeMap(methodElement);
            ExpressionValidator validator =
                new ExpressionValidator(ctx, methodElement, parameterTypeMap);

            ExpressionNode node = new ExpressionParser("person.temperature").parse();
            TypeDeclaration result = validator.validate(node);
            assertTrue(result.isNumberType());
          }
        });
    compile();
    assertTrue(getCompiledResult());
  }

  @Test
  void testFieldAccess_static_optional() throws Exception {
    Class<?> target = ExpressionValidationDao.class;
    addCompilationUnit(target);
    addProcessor(
        new TestProcessor() {
          @Override
          protected void run(RoundContext ctx) {
            ExecutableElement methodElement =
                createMethodElement(target, "testPerson", Person.class);
            Map<String, TypeMirror> parameterTypeMap = createParameterTypeMap(methodElement);
            ExpressionValidator validator =
                new ExpressionValidator(ctx, methodElement, parameterTypeMap);

            ExpressionNode node = new ExpressionParser("person.staticName").parse();
            TypeDeclaration result = validator.validate(node);
            assertTrue(result.isTextType());
          }
        });
    compile();
    assertTrue(getCompiledResult());
  }

  @Test
  void testFieldAccess_static_optionalInt() throws Exception {
    Class<?> target = ExpressionValidationDao.class;
    addCompilationUnit(target);
    addProcessor(
        new TestProcessor() {
          @Override
          protected void run(RoundContext ctx) {
            ExecutableElement methodElement =
                createMethodElement(target, "testPerson", Person.class);
            Map<String, TypeMirror> parameterTypeMap = createParameterTypeMap(methodElement);
            ExpressionValidator validator =
                new ExpressionValidator(ctx, methodElement, parameterTypeMap);

            ExpressionNode node = new ExpressionParser("person.staticAge").parse();
            TypeDeclaration result = validator.validate(node);
            assertTrue(result.isNumberType());
          }
        });
    compile();
    assertTrue(getCompiledResult());
  }

  @Test
  void testMethodAccess() throws Exception {
    Class<?> target = ExpressionValidationDao.class;
    addCompilationUnit(target);
    addProcessor(
        new TestProcessor() {
          @Override
          protected void run(RoundContext ctx) {
            ExecutableElement methodElement = createMethodElement(target, "testEmp", Emp.class);
            Map<String, TypeMirror> parameterTypeMap = createParameterTypeMap(methodElement);
            ExpressionValidator validator =
                new ExpressionValidator(ctx, methodElement, parameterTypeMap);

            ExpressionNode node = new ExpressionParser("emp.getId()").parse();
            TypeDeclaration result = validator.validate(node);
            assertFalse(result.isUnknownType());
          }
        });
    compile();
    assertTrue(getCompiledResult());
  }

  @Test
  void testMethodAccess_optional() throws Exception {
    Class<?> target = ExpressionValidationDao.class;
    addCompilationUnit(target);
    addProcessor(
        new TestProcessor() {
          @Override
          protected void run(RoundContext ctx) {
            ExecutableElement methodElement =
                createMethodElement(target, "testPerson", Person.class);
            Map<String, TypeMirror> parameterTypeMap = createParameterTypeMap(methodElement);
            ExpressionValidator validator =
                new ExpressionValidator(ctx, methodElement, parameterTypeMap);

            ExpressionNode node = new ExpressionParser("person.getName()").parse();
            TypeDeclaration result = validator.validate(node);
            assertTrue(result.isTextType());
          }
        });
    compile();
    assertTrue(getCompiledResult());
  }

  @Test
  void testMethodAccess_optionalInt() throws Exception {
    Class<?> target = ExpressionValidationDao.class;
    addCompilationUnit(target);
    addProcessor(
        new TestProcessor() {
          @Override
          protected void run(RoundContext ctx) {
            ExecutableElement methodElement =
                createMethodElement(target, "testPerson", Person.class);
            Map<String, TypeMirror> parameterTypeMap = createParameterTypeMap(methodElement);
            ExpressionValidator validator =
                new ExpressionValidator(ctx, methodElement, parameterTypeMap);

            ExpressionNode node = new ExpressionParser("person.getAge()").parse();
            TypeDeclaration result = validator.validate(node);
            assertTrue(result.isNumberType());
          }
        });
    compile();
    assertTrue(getCompiledResult());
  }

  @Test
  void testMethodAccess_static_optional() throws Exception {
    Class<?> target = ExpressionValidationDao.class;
    addCompilationUnit(target);
    addProcessor(
        new TestProcessor() {
          @Override
          protected void run(RoundContext ctx) {
            ExecutableElement methodElement =
                createMethodElement(target, "testPerson", Person.class);
            Map<String, TypeMirror> parameterTypeMap = createParameterTypeMap(methodElement);
            ExpressionValidator validator =
                new ExpressionValidator(ctx, methodElement, parameterTypeMap);

            ExpressionNode node = new ExpressionParser("person.getStaticName()").parse();
            TypeDeclaration result = validator.validate(node);
            assertTrue(result.isTextType());
          }
        });
    compile();
    assertTrue(getCompiledResult());
  }

  @Test
  void testMethodAccess_static_optionalInt() throws Exception {
    Class<?> target = ExpressionValidationDao.class;
    addCompilationUnit(target);
    addProcessor(
        new TestProcessor() {
          @Override
          protected void run(RoundContext ctx) {
            ExecutableElement methodElement =
                createMethodElement(target, "testPerson", Person.class);
            Map<String, TypeMirror> parameterTypeMap = createParameterTypeMap(methodElement);
            ExpressionValidator validator =
                new ExpressionValidator(ctx, methodElement, parameterTypeMap);

            ExpressionNode node = new ExpressionParser("person.getStaticAge()").parse();
            TypeDeclaration result = validator.validate(node);
            assertTrue(result.isNumberType());
          }
        });
    compile();
    assertTrue(getCompiledResult());
  }

  @Test
  void testConstructorAccess() throws Exception {
    Class<?> target = ExpressionValidationDao.class;
    addCompilationUnit(target);
    addProcessor(
        new TestProcessor() {
          @Override
          protected void run(RoundContext ctx) {
            ExecutableElement methodElement = createMethodElement(target, "testEmp", Emp.class);
            Map<String, TypeMirror> parameterTypeMap = createParameterTypeMap(methodElement);
            ExpressionValidator validator =
                new ExpressionValidator(ctx, methodElement, parameterTypeMap);

            ExpressionNode node =
                new ExpressionParser("emp.id == new java.lang.Integer(1)").parse();
            TypeDeclaration result = validator.validate(node);
            assertFalse(result.isUnknownType());
          }
        });
    compile();
    assertTrue(getCompiledResult());
  }

  @Test
  void testConstructorAccess_multiCandidates() throws Exception {
    Class<?> target = ExpressionValidationDao.class;
    addCompilationUnit(target);
    addProcessor(
        new TestProcessor() {
          @Override
          protected void run(RoundContext ctx) {
            ExecutableElement methodElement = createMethodElement(target, "testEmp", Emp.class);
            Map<String, TypeMirror> parameterTypeMap = createParameterTypeMap(methodElement);
            ExpressionValidator validator =
                new ExpressionValidator(ctx, methodElement, parameterTypeMap);

            String expression = String.format("new %s(\"test\")", Job.class.getName());
            ExpressionNode node = new ExpressionParser(expression).parse();
            TypeDeclaration result = validator.validate(node);
            assertTrue(ctx.getMoreTypes().isSameTypeWithErasure(result.getType(), Job.class));
          }
        });
    compile();
    assertTrue(getCompiledResult());
  }

  @Test
  void testMethodAccess_withArguments() throws Exception {
    Class<?> target = ExpressionValidationDao.class;
    addCompilationUnit(target);
    addProcessor(
        new TestProcessor() {
          @Override
          protected void run(RoundContext ctx) {
            ExecutableElement methodElement = createMethodElement(target, "testEmp", Emp.class);
            Map<String, TypeMirror> parameterTypeMap = createParameterTypeMap(methodElement);
            ExpressionValidator validator =
                new ExpressionValidator(ctx, methodElement, parameterTypeMap);

            ExpressionNode node = new ExpressionParser("emp.add(2, 3)").parse();
            TypeDeclaration result = validator.validate(node);
            assertFalse(result.isUnknownType());
          }
        });
    compile();
    assertTrue(getCompiledResult());
  }

  @Test
  void testEqOperator() throws Exception {
    Class<?> target = ExpressionValidationDao.class;
    addCompilationUnit(target);
    addProcessor(
        new TestProcessor() {
          @Override
          protected void run(RoundContext ctx) {
            ExecutableElement methodElement = createMethodElement(target, "testEmp", Emp.class);
            Map<String, TypeMirror> parameterTypeMap = createParameterTypeMap(methodElement);
            ExpressionValidator validator =
                new ExpressionValidator(ctx, methodElement, parameterTypeMap);

            ExpressionNode node = new ExpressionParser("emp.add(2, 3) == 5").parse();
            TypeDeclaration result = validator.validate(node);
            assertFalse(result.isUnknownType());
          }
        });
    compile();
    assertTrue(getCompiledResult());
  }

  @Test
  void testUnreferencedParameter() throws Exception {
    Class<?> target = ExpressionValidationDao.class;
    addCompilationUnit(target);
    addProcessor(
        new TestProcessor() {
          @Override
          protected void run(RoundContext ctx) {
            ExecutableElement methodElement = createMethodElement(target, "testEmp", Emp.class);
            Map<String, TypeMirror> parameterTypeMap = createParameterTypeMap(methodElement);
            ExpressionValidator validator =
                new ExpressionValidator(ctx, methodElement, parameterTypeMap);

            ExpressionNode node = new ExpressionParser("true").parse();
            validator.validate(node);
            assertFalse(validator.getValidatedParameterNames().contains("emp"));
          }
        });
    compile();
    assertTrue(getCompiledResult());
  }

  @Test
  void testAddOperator_number_number() throws Exception {
    Class<?> target = ExpressionValidationDao.class;
    addCompilationUnit(target);
    addProcessor(
        new TestProcessor() {
          @Override
          protected void run(RoundContext ctx) {
            ExecutableElement methodElement = createMethodElement(target, "testEmp", Emp.class);
            Map<String, TypeMirror> parameterTypeMap = createParameterTypeMap(methodElement);
            ExpressionValidator validator =
                new ExpressionValidator(ctx, methodElement, parameterTypeMap);

            ExpressionNode node = new ExpressionParser("1 + 2").parse();
            TypeDeclaration result = validator.validate(node);
            assertTrue(result.isNumberType());
          }
        });
    compile();
    assertTrue(getCompiledResult());
  }

  @Test
  void testAddOperator_number_text() throws Exception {
    Class<?> target = ExpressionValidationDao.class;
    addCompilationUnit(target);
    addProcessor(
        new TestProcessor() {
          @Override
          protected void run(RoundContext ctx) {
            ExecutableElement methodElement = createMethodElement(target, "testEmp", Emp.class);
            Map<String, TypeMirror> parameterTypeMap = createParameterTypeMap(methodElement);
            ExpressionValidator validator =
                new ExpressionValidator(ctx, methodElement, parameterTypeMap);

            ExpressionNode node = new ExpressionParser("1 + \"2\"").parse();
            try {
              validator.validate(node);
              fail();
            } catch (AptException expected) {
              System.out.println(expected);
              assertEquals(Message.DOMA4121, expected.getMessageResource());
            }
          }
        });
    compile();
    assertTrue(getCompiledResult());
  }

  @Test
  void testAddOperator_text_text() throws Exception {
    Class<?> target = ExpressionValidationDao.class;
    addCompilationUnit(target);
    addProcessor(
        new TestProcessor() {
          @Override
          protected void run(RoundContext ctx) {
            ExecutableElement methodElement = createMethodElement(target, "testEmp", Emp.class);
            Map<String, TypeMirror> parameterTypeMap = createParameterTypeMap(methodElement);
            ExpressionValidator validator =
                new ExpressionValidator(ctx, methodElement, parameterTypeMap);

            ExpressionNode node = new ExpressionParser("\"1\" + \"2\"").parse();
            TypeDeclaration result = validator.validate(node);
            assertTrue(result.isTextType());
          }
        });
    compile();
    assertTrue(getCompiledResult());
  }

  @Test
  void testAddOperator_text_number() throws Exception {
    Class<?> target = ExpressionValidationDao.class;
    addCompilationUnit(target);
    addProcessor(
        new TestProcessor() {
          @Override
          protected void run(RoundContext ctx) {
            ExecutableElement methodElement = createMethodElement(target, "testEmp", Emp.class);
            Map<String, TypeMirror> parameterTypeMap = createParameterTypeMap(methodElement);
            ExpressionValidator validator =
                new ExpressionValidator(ctx, methodElement, parameterTypeMap);

            ExpressionNode node = new ExpressionParser("\"2\" + 1").parse();
            try {
              validator.validate(node);
              fail();
            } catch (AptException expected) {
              System.out.println(expected);
              assertEquals(Message.DOMA4126, expected.getMessageResource());
            }
          }
        });
    compile();
    assertTrue(getCompiledResult());
  }
}
