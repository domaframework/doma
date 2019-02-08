package org.seasar.doma.internal.apt.processor.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.LinkedHashMap;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.type.TypeMirror;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.seasar.doma.internal.apt.AptException;
import org.seasar.doma.internal.apt.CompilerSupport;
import org.seasar.doma.internal.apt.TestProcessor;
import org.seasar.doma.internal.apt.validator.BatchSqlValidator;
import org.seasar.doma.internal.jdbc.sql.SqlParser;
import org.seasar.doma.jdbc.SqlNode;
import org.seasar.doma.message.Message;

class BatchSqlValidatorTest extends CompilerSupport {

  @BeforeEach
  void beforeEach() {
    addSourcePath("src/main/java");
  }

  @Test
  void testEmbeddedVariable() throws Exception {
    Class<?> target = BatchSqlValidationDao.class;
    addCompilationUnit(target);
    addProcessor(
        new TestProcessor() {
          @Override
          protected void run() {
            ExecutableElement methodElement =
                createMethodElement(target, "testEmbeddedVariable", String.class);
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
          protected void run() {
            ExecutableElement methodElement =
                createMethodElement(target, "testEmbeddedVariableSuppressed", String.class);
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
    assertNoMessage();
  }

  @Test
  void testIf() throws Exception {
    Class<?> target = BatchSqlValidationDao.class;
    addCompilationUnit(target);
    addProcessor(
        new TestProcessor() {
          @Override
          protected void run() {
            ExecutableElement methodElement = createMethodElement(target, "testIf");
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
          protected void run() {
            ExecutableElement methodElement = createMethodElement(target, "testIfSuppressed");
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
    assertNoMessage();
  }

  @Test
  void testIfAndEmbeddedVariable() throws Exception {
    Class<?> target = BatchSqlValidationDao.class;
    addCompilationUnit(target);
    addProcessor(
        new TestProcessor() {
          @Override
          protected void run() {
            ExecutableElement methodElement =
                createMethodElement(target, "testIfAndEmbeddedVariable", String.class);
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
    assertEquals(2, getDiagnostics().size());
  }

  @Test
  void testIfAndEmbeddedVariableSuppressed() throws Exception {
    Class<?> target = BatchSqlValidationDao.class;
    addCompilationUnit(target);
    addProcessor(
        new TestProcessor() {
          @Override
          protected void run() {
            ExecutableElement methodElement =
                createMethodElement(target, "testIfAndEmbeddedVariableSuppressed", String.class);
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
    assertNoMessage();
  }

  @Test
  void testPopulate() throws Exception {
    Class<?> target = BatchSqlValidationDao.class;
    addCompilationUnit(target);
    addProcessor(
        new TestProcessor() {
          @Override
          protected void run() {
            ExecutableElement methodElement =
                createMethodElement(target, "testPopulate", String.class);
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
    assertNoMessage();
  }

  @Test
  void testPopulate_noPopulatable() throws Exception {
    Class<?> target = BatchSqlValidationDao.class;
    addCompilationUnit(target);
    addProcessor(
        new TestProcessor() {
          @Override
          protected void run() {
            ExecutableElement methodElement =
                createMethodElement(target, "testPopulate", String.class);
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
