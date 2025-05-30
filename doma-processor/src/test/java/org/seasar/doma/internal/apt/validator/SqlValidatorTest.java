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
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.type.TypeMirror;
import org.junit.jupiter.api.Test;
import org.seasar.doma.internal.apt.AbstractCompilerTest;
import org.seasar.doma.internal.apt.AptException;
import org.seasar.doma.internal.apt.RoundContext;
import org.seasar.doma.internal.apt.TestProcessor;
import org.seasar.doma.internal.jdbc.sql.SqlParser;
import org.seasar.doma.jdbc.SqlNode;
import org.seasar.doma.message.Message;

class SqlValidatorTest extends AbstractCompilerTest {

  @Test
  void testFormalTypeParameterResolution() throws Exception {
    Class<?> target = SqlValidationDao.class;
    addCompilationUnit(target);
    addProcessor(
        new TestProcessor() {
          @Override
          protected void run(RoundContext ctx) {
            ExecutableElement methodElement =
                createMethodElement(
                    ctx, target, "testTypeParameterResolution", CriteriaHolder.class);
            LinkedHashMap<String, TypeMirror> parameterTypeMap =
                createParameterTypeMap(methodElement);

            SqlValidator validator =
                new SqlValidator(
                    ctx, methodElement, parameterTypeMap, "aaa/bbbDao/ccc.sql", false, false);
            SqlParser parser =
                new SqlParser(
                    "select * from emp where name in /* criteriaHolder.criteria.list */(0)");
            SqlNode sqlNode = parser.parse();
            sqlNode.accept(validator, null);
          }
        });
    compile();
    assertTrue(getCompiledResult());
  }

  @Test
  void testBindVariable() throws Exception {
    Class<?> target = SqlValidationDao.class;
    addCompilationUnit(target);
    addProcessor(
        new TestProcessor() {
          @Override
          protected void run(RoundContext ctx) {
            ExecutableElement methodElement =
                createMethodElement(ctx, target, "testBindVariable", String.class);
            LinkedHashMap<String, TypeMirror> parameterTypeMap =
                createParameterTypeMap(methodElement);
            SqlValidator validator =
                new SqlValidator(
                    ctx, methodElement, parameterTypeMap, "aaa/bbbDao/ccc.sql", false, false);
            SqlParser parser = new SqlParser("select * from emp where name = /* name */'aaa'");
            SqlNode sqlNode = parser.parse();
            sqlNode.accept(validator, null);
          }
        });
    compile();
    assertTrue(getCompiledResult());
  }

  @Test
  void testBindVariable_list() throws Exception {
    Class<?> target = SqlValidationDao.class;
    addCompilationUnit(target);
    addProcessor(
        new TestProcessor() {
          @Override
          protected void run(RoundContext ctx) {
            ExecutableElement methodElement =
                createMethodElement(ctx, target, "testBindVariable_list", List.class);
            LinkedHashMap<String, TypeMirror> parameterTypeMap =
                createParameterTypeMap(methodElement);
            SqlValidator validator =
                new SqlValidator(
                    ctx, methodElement, parameterTypeMap, "aaa/bbbDao/ccc.sql", false, false);
            SqlParser parser = new SqlParser("select * from emp where name in /* names */('aaa')");
            SqlNode sqlNode = parser.parse();
            sqlNode.accept(validator, null);
          }
        });
    compile();
    assertTrue(getCompiledResult());
  }

  @Test
  void testBindVariable_array() throws Exception {
    Class<?> target = SqlValidationDao.class;
    addCompilationUnit(target);
    addProcessor(
        new TestProcessor() {
          @Override
          protected void run(RoundContext ctx) {
            ExecutableElement methodElement =
                createMethodElement(ctx, target, "testBindVariable_array", String[].class);
            LinkedHashMap<String, TypeMirror> parameterTypeMap =
                createParameterTypeMap(methodElement);
            SqlValidator validator =
                new SqlValidator(
                    ctx, methodElement, parameterTypeMap, "aaa/bbbDao/ccc.sql", false, false);
            SqlParser parser = new SqlParser("select * from emp where name in /* names */('aaa')");
            SqlNode sqlNode = parser.parse();
            sqlNode.accept(validator, null);
          }
        });
    compile();
    assertTrue(getCompiledResult());
  }

  @Test
  void testLiteralVariable() throws Exception {
    Class<?> target = SqlValidationDao.class;
    addCompilationUnit(target);
    addProcessor(
        new TestProcessor() {
          @Override
          protected void run(RoundContext ctx) {
            ExecutableElement methodElement =
                createMethodElement(ctx, target, "testBindVariable", String.class);
            LinkedHashMap<String, TypeMirror> parameterTypeMap =
                createParameterTypeMap(methodElement);
            SqlValidator validator =
                new SqlValidator(
                    ctx, methodElement, parameterTypeMap, "aaa/bbbDao/ccc.sql", false, false);
            SqlParser parser = new SqlParser("select * from emp where name = /*^ name */'aaa'");
            SqlNode sqlNode = parser.parse();
            sqlNode.accept(validator, null);
          }
        });
    compile();
    assertTrue(getCompiledResult());
  }

  @Test
  void testLiteralVariable_list() throws Exception {
    Class<?> target = SqlValidationDao.class;
    addCompilationUnit(target);
    addProcessor(
        new TestProcessor() {
          @Override
          protected void run(RoundContext ctx) {
            ExecutableElement methodElement =
                createMethodElement(ctx, target, "testBindVariable_list", List.class);
            LinkedHashMap<String, TypeMirror> parameterTypeMap =
                createParameterTypeMap(methodElement);
            SqlValidator validator =
                new SqlValidator(
                    ctx, methodElement, parameterTypeMap, "aaa/bbbDao/ccc.sql", false, false);
            SqlParser parser = new SqlParser("select * from emp where name in /*^ names */('aaa')");
            SqlNode sqlNode = parser.parse();
            sqlNode.accept(validator, null);
          }
        });
    compile();
    assertTrue(getCompiledResult());
  }

  @Test
  void testLiteralVariable_array() throws Exception {
    Class<?> target = SqlValidationDao.class;
    addCompilationUnit(target);
    addProcessor(
        new TestProcessor() {
          @Override
          protected void run(RoundContext ctx) {
            ExecutableElement methodElement =
                createMethodElement(ctx, target, "testBindVariable_array", String[].class);
            LinkedHashMap<String, TypeMirror> parameterTypeMap =
                createParameterTypeMap(methodElement);
            SqlValidator validator =
                new SqlValidator(
                    ctx, methodElement, parameterTypeMap, "aaa/bbbDao/ccc.sql", false, false);
            SqlParser parser = new SqlParser("select * from emp where name in /*^ names */('aaa')");
            SqlNode sqlNode = parser.parse();
            sqlNode.accept(validator, null);
          }
        });
    compile();
    assertTrue(getCompiledResult());
  }

  @Test
  void testEmbeddedVariable() throws Exception {
    Class<?> target = SqlValidationDao.class;
    addCompilationUnit(target);
    addProcessor(
        new TestProcessor() {
          @Override
          protected void run(RoundContext ctx) {
            ExecutableElement methodElement =
                createMethodElement(ctx, target, "testEmbeddedVariable", String.class);
            LinkedHashMap<String, TypeMirror> parameterTypeMap =
                createParameterTypeMap(methodElement);
            SqlValidator validator =
                new SqlValidator(
                    ctx, methodElement, parameterTypeMap, "aaa/bbbDao/ccc.sql", false, false);
            SqlParser parser = new SqlParser("select * from emp /*# orderBy */");
            SqlNode sqlNode = parser.parse();
            sqlNode.accept(validator, null);
          }
        });
    compile();
    assertTrue(getCompiledResult());
  }

  @Test
  void testFor() throws Exception {
    Class<?> target = SqlValidationDao.class;
    addCompilationUnit(target);
    addProcessor(
        new TestProcessor() {
          @Override
          protected void run(RoundContext ctx) {
            ExecutableElement methodElement =
                createMethodElement(ctx, target, "testFor", List.class);
            LinkedHashMap<String, TypeMirror> parameterTypeMap =
                createParameterTypeMap(methodElement);
            SqlValidator validator =
                new SqlValidator(
                    ctx, methodElement, parameterTypeMap, "aaa/bbbDao/ccc.sql", false, false);
            SqlParser parser =
                new SqlParser(
                    "select * from emp where name = /*%for e : names*/ /*e*/'aaa' /*%if e_has_next*/or/*%end*//*%end*/");
            SqlNode sqlNode = parser.parse();
            sqlNode.accept(validator, null);
          }
        });
    compile();
    assertTrue(getCompiledResult());
  }

  @Test
  void testFor_array() throws Exception {
    Class<?> target = SqlValidationDao.class;
    addCompilationUnit(target);
    addProcessor(
        new TestProcessor() {
          @Override
          protected void run(RoundContext ctx) {
            ExecutableElement methodElement =
                createMethodElement(ctx, target, "testFor_array", String[].class);
            LinkedHashMap<String, TypeMirror> parameterTypeMap =
                createParameterTypeMap(methodElement);
            SqlValidator validator =
                new SqlValidator(
                    ctx, methodElement, parameterTypeMap, "aaa/bbbDao/ccc.sql", false, false);
            SqlParser parser =
                new SqlParser(
                    "select * from emp where name = /*%for e : names*/ /*e*/'aaa' /*%if e_has_next*/or/*%end*//*%end*/");
            SqlNode sqlNode = parser.parse();
            sqlNode.accept(validator, null);
          }
        });
    compile();
    assertTrue(getCompiledResult());
  }

  @Test
  void testFor_identifier() throws Exception {
    Class<?> target = SqlValidationDao.class;
    addCompilationUnit(target);
    addProcessor(
        new TestProcessor() {
          @Override
          protected void run(RoundContext ctx) {
            ExecutableElement methodElement =
                createMethodElement(ctx, target, "testFor", List.class);
            LinkedHashMap<String, TypeMirror> parameterTypeMap =
                createParameterTypeMap(methodElement);
            SqlValidator validator =
                new SqlValidator(
                    ctx, methodElement, parameterTypeMap, "aaa/bbbDao/ccc.sql", false, false);
            SqlParser parser =
                new SqlParser(
                    "select * from emp where name = /*%for e : names*/ /*x*/'aaa' /*%if e_has_next*/or/*%end*//*%end*/");
            SqlNode sqlNode = parser.parse();
            try {
              sqlNode.accept(validator, null);
              fail();
            } catch (AptException expected) {
              System.out.println(expected.getMessage());
              assertEquals(Message.DOMA4092, expected.getMessageResource());
            }
          }
        });
    compile();
    assertTrue(getCompiledResult());
  }

  @Test
  void testFor_notIterable() throws Exception {
    Class<?> target = SqlValidationDao.class;
    addCompilationUnit(target);
    addProcessor(
        new TestProcessor() {
          @Override
          protected void run(RoundContext ctx) {
            ExecutableElement methodElement =
                createMethodElement(ctx, target, "testFor_notIterable", Iterator.class);
            LinkedHashMap<String, TypeMirror> parameterTypeMap =
                createParameterTypeMap(methodElement);
            SqlValidator validator =
                new SqlValidator(
                    ctx, methodElement, parameterTypeMap, "aaa/bbbDao/ccc.sql", false, false);
            SqlParser parser =
                new SqlParser(
                    "select * from emp where name = /*%for e : names*/ /*e*/'aaa' /*%if e_has_next*/or/*%end*//*%end*/");
            SqlNode sqlNode = parser.parse();
            try {
              sqlNode.accept(validator, null);
              fail();
            } catch (AptException expected) {
              System.out.println(expected.getMessage());
              assertEquals(Message.DOMA4149, expected.getMessageResource());
            }
          }
        });
    compile();
    assertTrue(getCompiledResult());
  }

  @Test
  void testFor_noTypeArgument() throws Exception {
    Class<?> target = SqlValidationDao.class;
    addCompilationUnit(target);
    addProcessor(
        new TestProcessor() {
          @Override
          protected void run(RoundContext ctx) {
            ExecutableElement methodElement =
                createMethodElement(ctx, target, "testFor_noTypeArgument", List.class);
            LinkedHashMap<String, TypeMirror> parameterTypeMap =
                createParameterTypeMap(methodElement);
            SqlValidator validator =
                new SqlValidator(
                    ctx, methodElement, parameterTypeMap, "aaa/bbbDao/ccc.sql", false, false);
            SqlParser parser =
                new SqlParser(
                    "select * from emp where name = /*%for e : names*/ /*e*/'aaa' /*%if e_has_next*/or/*%end*//*%end*/");
            SqlNode sqlNode = parser.parse();
            try {
              sqlNode.accept(validator, null);
              fail();
            } catch (AptException expected) {
              System.out.println(expected.getMessage());
              assertEquals(Message.DOMA4150, expected.getMessageResource());
            }
          }
        });
    compile();
    assertTrue(getCompiledResult());
  }

  @Test
  void testExpand() throws Exception {
    Class<?> target = SqlValidationDao.class;
    addCompilationUnit(target);
    addProcessor(
        new TestProcessor() {
          @Override
          protected void run(RoundContext ctx) {
            ExecutableElement methodElement =
                createMethodElement(ctx, target, "testExpand", String.class);
            LinkedHashMap<String, TypeMirror> parameterTypeMap =
                createParameterTypeMap(methodElement);
            SqlValidator validator =
                new SqlValidator(
                    ctx, methodElement, parameterTypeMap, "aaa/bbbDao/ccc.sql", true, false);
            SqlParser parser =
                new SqlParser("select /*%expand*/* from emp where name = /* name */'aaa'");
            SqlNode sqlNode = parser.parse();
            sqlNode.accept(validator, null);
          }
        });
    compile();
    assertTrue(getCompiledResult());
  }

  @Test
  void testExpand_notExpandable() throws Exception {
    Class<?> target = SqlValidationDao.class;
    addCompilationUnit(target);
    addProcessor(
        new TestProcessor() {
          @Override
          protected void run(RoundContext ctx) {
            ExecutableElement methodElement =
                createMethodElement(ctx, target, "testExpand", String.class);
            LinkedHashMap<String, TypeMirror> parameterTypeMap =
                createParameterTypeMap(methodElement);
            SqlValidator validator =
                new SqlValidator(
                    ctx, methodElement, parameterTypeMap, "aaa/bbbDao/ccc.sql", false, false);
            SqlParser parser =
                new SqlParser("select /*%expand*/* from emp where name = /* name */'aaa'");
            SqlNode sqlNode = parser.parse();
            try {
              sqlNode.accept(validator, null);
              fail();
            } catch (AptException expected) {
              System.out.println(expected.getMessage());
              assertEquals(Message.DOMA4257, expected.getMessageResource());
            }
          }
        });
    compile();
    assertTrue(getCompiledResult());
  }

  @Test
  void testPopulate() throws Exception {
    Class<?> target = SqlValidationDao.class;
    addCompilationUnit(target);
    addProcessor(
        new TestProcessor() {
          @Override
          protected void run(RoundContext ctx) {
            ExecutableElement methodElement =
                createMethodElement(ctx, target, "testPopulate", String.class);
            LinkedHashMap<String, TypeMirror> parameterTypeMap =
                createParameterTypeMap(methodElement);
            SqlValidator validator =
                new SqlValidator(
                    ctx, methodElement, parameterTypeMap, "aaa/bbbDao/ccc.sql", false, true);
            SqlParser parser = new SqlParser("update emp set /*%populate*/ id = id");
            SqlNode sqlNode = parser.parse();
            sqlNode.accept(validator, null);
          }
        });
    compile();
    assertTrue(getCompiledResult());
  }

  @Test
  void testPopulate_notPopulatable() throws Exception {
    Class<?> target = SqlValidationDao.class;
    addCompilationUnit(target);
    addProcessor(
        new TestProcessor() {
          @Override
          protected void run(RoundContext ctx) {
            ExecutableElement methodElement =
                createMethodElement(ctx, target, "testPopulate", String.class);
            LinkedHashMap<String, TypeMirror> parameterTypeMap =
                createParameterTypeMap(methodElement);
            SqlValidator validator =
                new SqlValidator(
                    ctx, methodElement, parameterTypeMap, "aaa/bbbDao/ccc.sql", false, false);
            SqlParser parser = new SqlParser("update emp set /*%populate*/ id = id");
            SqlNode sqlNode = parser.parse();
            try {
              sqlNode.accept(validator, null);
              fail();
            } catch (AptException expected) {
              System.out.println(expected.getMessage());
              assertEquals(Message.DOMA4270, expected.getMessageResource());
            }
          }
        });
    compile();
    assertTrue(getCompiledResult());
  }
}
