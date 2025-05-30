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

import java.util.LinkedHashMap;
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

class BatchSqlValidatorTest extends AbstractCompilerTest {

  @Test
  void testEmbeddedVariable() throws Exception {
    Class<?> target = BatchSqlValidationDao.class;
    addCompilationUnit(target);
    addProcessor(
        new TestProcessor() {
          @Override
          protected void run(RoundContext ctx) {
            ExecutableElement methodElement =
                createMethodElement(ctx, target, "testEmbeddedVariable", String.class);
            LinkedHashMap<String, TypeMirror> parameterTypeMap =
                createParameterTypeMap(methodElement);
            BatchSqlValidator validator =
                new BatchSqlValidator(
                    ctx, methodElement, parameterTypeMap, "aaa/bbbDao/ccc.sql", false, false);
            SqlParser parser = new SqlParser("select * from emp /*# orderBy */");
            SqlNode sqlNode = parser.parse();
            sqlNode.accept(validator, null);
          }
        });
    compile();
    assertTrue(getCompiledResult());
    assertMessage(Message.DOMA4181);
  }

  @Test
  void testEmbeddedVariableSuppressed() throws Exception {
    Class<?> target = BatchSqlValidationDao.class;
    addCompilationUnit(target);
    addProcessor(
        new TestProcessor() {
          @Override
          protected void run(RoundContext ctx) {
            ExecutableElement methodElement =
                createMethodElement(ctx, target, "testEmbeddedVariableSuppressed", String.class);
            LinkedHashMap<String, TypeMirror> parameterTypeMap =
                createParameterTypeMap(methodElement);
            BatchSqlValidator validator =
                new BatchSqlValidator(
                    ctx, methodElement, parameterTypeMap, "aaa/bbbDao/ccc.sql", false, false);
            SqlParser parser = new SqlParser("select * from emp /*# orderBy */");
            SqlNode sqlNode = parser.parse();
            sqlNode.accept(validator, null);
          }
        });
    compile();
    assertTrue(getCompiledResult());
    assertNoError();
  }

  @Test
  void testIf() throws Exception {
    Class<?> target = BatchSqlValidationDao.class;
    addCompilationUnit(target);
    addProcessor(
        new TestProcessor() {
          @Override
          protected void run(RoundContext ctx) {
            ExecutableElement methodElement = createMethodElement(ctx, target, "testIf");
            LinkedHashMap<String, TypeMirror> parameterTypeMap =
                createParameterTypeMap(methodElement);
            BatchSqlValidator validator =
                new BatchSqlValidator(
                    ctx, methodElement, parameterTypeMap, "aaa/bbbDao/ccc.sql", false, false);
            SqlParser parser =
                new SqlParser("select * from emp where /*%if true*/ id = 1 /*%end */");
            SqlNode sqlNode = parser.parse();
            sqlNode.accept(validator, null);
          }
        });
    compile();
    assertTrue(getCompiledResult());
    assertMessage(Message.DOMA4182);
  }

  @Test
  void testIfSuppressed() throws Exception {
    Class<?> target = BatchSqlValidationDao.class;
    addCompilationUnit(target);
    addProcessor(
        new TestProcessor() {
          @Override
          protected void run(RoundContext ctx) {
            ExecutableElement methodElement = createMethodElement(ctx, target, "testIfSuppressed");
            LinkedHashMap<String, TypeMirror> parameterTypeMap =
                createParameterTypeMap(methodElement);
            BatchSqlValidator validator =
                new BatchSqlValidator(
                    ctx, methodElement, parameterTypeMap, "aaa/bbbDao/ccc.sql", false, false);
            SqlParser parser =
                new SqlParser("select * from emp where /*%if true*/ id = 1 /*%end */");
            SqlNode sqlNode = parser.parse();
            sqlNode.accept(validator, null);
          }
        });
    compile();
    assertTrue(getCompiledResult());
    assertNoError();
  }

  @Test
  void testIfAndEmbeddedVariable() throws Exception {
    Class<?> target = BatchSqlValidationDao.class;
    addCompilationUnit(target);
    addProcessor(
        new TestProcessor() {
          @Override
          protected void run(RoundContext ctx) {
            ExecutableElement methodElement =
                createMethodElement(ctx, target, "testIfAndEmbeddedVariable", String.class);
            LinkedHashMap<String, TypeMirror> parameterTypeMap =
                createParameterTypeMap(methodElement);
            BatchSqlValidator validator =
                new BatchSqlValidator(
                    ctx, methodElement, parameterTypeMap, "aaa/bbbDao/ccc.sql", false, false);
            SqlParser parser =
                new SqlParser(
                    "select * from emp where /*%if true*/ id = 1 /*%end */ /*# orderBy */");
            SqlNode sqlNode = parser.parse();
            sqlNode.accept(validator, null);
          }
        });
    compile();
    assertTrue(getCompiledResult());
    assertMessage(Message.DOMA4181);
    assertMessage(Message.DOMA4182);
    assertNoError();
  }

  @Test
  void testIfAndEmbeddedVariableSuppressed() throws Exception {
    Class<?> target = BatchSqlValidationDao.class;
    addCompilationUnit(target);
    addProcessor(
        new TestProcessor() {
          @Override
          protected void run(RoundContext ctx) {
            ExecutableElement methodElement =
                createMethodElement(
                    ctx, target, "testIfAndEmbeddedVariableSuppressed", String.class);
            LinkedHashMap<String, TypeMirror> parameterTypeMap =
                createParameterTypeMap(methodElement);
            BatchSqlValidator validator =
                new BatchSqlValidator(
                    ctx, methodElement, parameterTypeMap, "aaa/bbbDao/ccc.sql", false, false);
            SqlParser parser =
                new SqlParser(
                    "select * from emp where /*%if true*/ id = 1 /*%end */ /*# orderBy */");
            SqlNode sqlNode = parser.parse();
            sqlNode.accept(validator, null);
          }
        });
    compile();
    assertTrue(getCompiledResult());
    assertNoError();
  }

  @Test
  void testPopulate() throws Exception {
    Class<?> target = BatchSqlValidationDao.class;
    addCompilationUnit(target);
    addProcessor(
        new TestProcessor() {
          @Override
          protected void run(RoundContext ctx) {
            ExecutableElement methodElement =
                createMethodElement(ctx, target, "testPopulate", String.class);
            LinkedHashMap<String, TypeMirror> parameterTypeMap =
                createParameterTypeMap(methodElement);
            BatchSqlValidator validator =
                new BatchSqlValidator(
                    ctx, methodElement, parameterTypeMap, "aaa/bbbDao/ccc.sql", false, true);
            SqlParser parser = new SqlParser("update emp set /*%populate*/ id = id");
            SqlNode sqlNode = parser.parse();
            sqlNode.accept(validator, null);
          }
        });
    compile();
    assertTrue(getCompiledResult());
    assertNoError();
  }

  @Test
  void testPopulate_noPopulatable() throws Exception {
    Class<?> target = BatchSqlValidationDao.class;
    addCompilationUnit(target);
    addProcessor(
        new TestProcessor() {
          @Override
          protected void run(RoundContext ctx) {
            ExecutableElement methodElement =
                createMethodElement(ctx, target, "testPopulate", String.class);
            LinkedHashMap<String, TypeMirror> parameterTypeMap =
                createParameterTypeMap(methodElement);
            BatchSqlValidator validator =
                new BatchSqlValidator(
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
