package org.seasar.doma.internal.apt.processor.dao;

import org.seasar.doma.internal.apt.AptException;
import org.seasar.doma.internal.apt.AptTestCase;
import org.seasar.doma.internal.apt.validator.BatchSqlValidator;
import org.seasar.doma.internal.jdbc.sql.SqlParser;
import org.seasar.doma.message.Message;

public class BatchSqlValidatorTest extends AptTestCase {

  @Override
  protected void setUp() throws Exception {
    super.setUp();
    addSourcePath("src/main/java");
    addSourcePath("src/test/java");
  }

  public void testEmbeddedVariable() throws Exception {
    Class<?> target = BatchSqlValidationDao.class;
    addCompilationUnit(target);
    addProcessor(
        new AptProcessor(
            ctx -> {
              var methodElement =
                  ctx.getElements().getMethodElement(target, "testEmbeddedVariable", String.class);
              var parameterTypeMap = ctx.getElements().getParameterTypeMap(methodElement);
              var validator =
                  new BatchSqlValidator(
                      ctx, methodElement, parameterTypeMap, "aaa/bbbDao/ccc.sql", false, false);
              var parser = new SqlParser("select * from emp /*# orderBy */");
              var sqlNode = parser.parse();
              sqlNode.accept(validator, null);
            }));
    compile();
    assertTrue(getCompiledResult());
    assertMessage(Message.DOMA4181);
  }

  public void testEmbeddedVariableSuppressed() throws Exception {
    Class<?> target = BatchSqlValidationDao.class;
    addCompilationUnit(target);
    addProcessor(
        new AptProcessor(
            ctx -> {
              var methodElement =
                  ctx.getElements()
                      .getMethodElement(target, "testEmbeddedVariableSuppressed", String.class);
              var parameterTypeMap = ctx.getElements().getParameterTypeMap(methodElement);
              var validator =
                  new BatchSqlValidator(
                      ctx, methodElement, parameterTypeMap, "aaa/bbbDao/ccc.sql", false, false);
              var parser = new SqlParser("select * from emp /*# orderBy */");
              var sqlNode = parser.parse();
              sqlNode.accept(validator, null);
            }));
    compile();
    assertTrue(getCompiledResult());
    assertNoMessage();
  }

  public void testIf() throws Exception {
    Class<?> target = BatchSqlValidationDao.class;
    addCompilationUnit(target);
    addProcessor(
        new AptProcessor(
            ctx -> {
              var methodElement = ctx.getElements().getMethodElement(target, "testIf");
              var parameterTypeMap = ctx.getElements().getParameterTypeMap(methodElement);
              var validator =
                  new BatchSqlValidator(
                      ctx, methodElement, parameterTypeMap, "aaa/bbbDao/ccc.sql", false, false);
              var parser = new SqlParser("select * from emp where /*%if true*/ id = 1 /*%end */");
              var sqlNode = parser.parse();
              sqlNode.accept(validator, null);
            }));
    compile();
    assertTrue(getCompiledResult());
    assertMessage(Message.DOMA4182);
  }

  public void testIfSuppressed() throws Exception {
    Class<?> target = BatchSqlValidationDao.class;
    addCompilationUnit(target);
    addProcessor(
        new AptProcessor(
            ctx -> {
              var methodElement = ctx.getElements().getMethodElement(target, "testIfSuppressed");
              var parameterTypeMap = ctx.getElements().getParameterTypeMap(methodElement);
              var validator =
                  new BatchSqlValidator(
                      ctx, methodElement, parameterTypeMap, "aaa/bbbDao/ccc.sql", false, false);
              var parser = new SqlParser("select * from emp where /*%if true*/ id = 1 /*%end */");
              var sqlNode = parser.parse();
              sqlNode.accept(validator, null);
            }));
    compile();
    assertTrue(getCompiledResult());
    assertNoMessage();
  }

  public void testIfAndEmbeddedVariable() throws Exception {
    Class<?> target = BatchSqlValidationDao.class;
    addCompilationUnit(target);
    addProcessor(
        new AptProcessor(
            ctx -> {
              var methodElement =
                  ctx.getElements()
                      .getMethodElement(target, "testIfAndEmbeddedVariable", String.class);
              var parameterTypeMap = ctx.getElements().getParameterTypeMap(methodElement);
              var validator =
                  new BatchSqlValidator(
                      ctx, methodElement, parameterTypeMap, "aaa/bbbDao/ccc.sql", false, false);
              var parser =
                  new SqlParser(
                      "select * from emp where /*%if true*/ id = 1 /*%end */ /*# orderBy */");
              var sqlNode = parser.parse();
              sqlNode.accept(validator, null);
            }));
    compile();
    assertTrue(getCompiledResult());
    assertEquals(2, getDiagnostics().size());
  }

  public void testIfAndEmbeddedVariableSuppressed() throws Exception {
    Class<?> target = BatchSqlValidationDao.class;
    addCompilationUnit(target);
    addProcessor(
        new AptProcessor(
            ctx -> {
              var methodElement =
                  ctx.getElements()
                      .getMethodElement(
                          target, "testIfAndEmbeddedVariableSuppressed", String.class);
              var parameterTypeMap = ctx.getElements().getParameterTypeMap(methodElement);
              var validator =
                  new BatchSqlValidator(
                      ctx, methodElement, parameterTypeMap, "aaa/bbbDao/ccc.sql", false, false);
              var parser =
                  new SqlParser(
                      "select * from emp where /*%if true*/ id = 1 /*%end */ /*# orderBy */");
              var sqlNode = parser.parse();
              sqlNode.accept(validator, null);
            }));
    compile();
    assertTrue(getCompiledResult());
    assertNoMessage();
  }

  public void testPopulate() throws Exception {
    Class<?> target = BatchSqlValidationDao.class;
    addCompilationUnit(target);
    addProcessor(
        new AptProcessor(
            ctx -> {
              var methodElement =
                  ctx.getElements().getMethodElement(target, "testPopulate", String.class);
              var parameterTypeMap = ctx.getElements().getParameterTypeMap(methodElement);
              var validator =
                  new BatchSqlValidator(
                      ctx, methodElement, parameterTypeMap, "aaa/bbbDao/ccc.sql", false, true);
              var parser = new SqlParser("update emp set /*%populate*/ id = id");
              var sqlNode = parser.parse();
              sqlNode.accept(validator, null);
            }));
    compile();
    assertTrue(getCompiledResult());
    assertNoMessage();
  }

  public void testPopulate_noPopulatable() throws Exception {
    Class<?> target = BatchSqlValidationDao.class;
    addCompilationUnit(target);
    addProcessor(
        new AptProcessor(
            ctx -> {
              var methodElement =
                  ctx.getElements().getMethodElement(target, "testPopulate", String.class);
              var parameterTypeMap = ctx.getElements().getParameterTypeMap(methodElement);
              var validator =
                  new BatchSqlValidator(
                      ctx, methodElement, parameterTypeMap, "aaa/bbbDao/ccc.sql", false, false);
              var parser = new SqlParser("update emp set /*%populate*/ id = id");
              var sqlNode = parser.parse();
              try {
                sqlNode.accept(validator, null);
                fail();
              } catch (AptException expected) {
                System.out.println(expected.getMessage());
                assertEquals(Message.DOMA4270, expected.getMessageResource());
              }
            }));
    compile();
    assertTrue(getCompiledResult());
  }
}
