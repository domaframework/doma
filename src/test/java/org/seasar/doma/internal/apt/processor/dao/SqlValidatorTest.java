package org.seasar.doma.internal.apt.processor.dao;

import java.util.Iterator;
import java.util.List;
import org.seasar.doma.internal.apt.AptException;
import org.seasar.doma.internal.apt.AptTestCase;
import org.seasar.doma.internal.apt.validator.SqlValidator;
import org.seasar.doma.internal.jdbc.sql.SqlParser;
import org.seasar.doma.message.Message;
import org.seasar.doma.wrapper.StringWrapper;

public class SqlValidatorTest extends AptTestCase {

  @Override
  protected void setUp() throws Exception {
    super.setUp();
    addSourcePath("src/main/java");
    addSourcePath("src/test/java");
  }

  public void testBindVariable() throws Exception {
    Class<?> target = SqlValidationDao.class;
    addCompilationUnit(target);
    addCompilationUnit(StringWrapper.class);
    addProcessor(
        new AptProcessor(
            ctx -> {
              var methodElement =
                  ctx.getElements().getMethodElement(target, "testBindVariable", String.class);
              var parameterTypeMap = ctx.getElements().getParameterTypeMap(methodElement);
              var validator =
                  new SqlValidator(
                      ctx, methodElement, parameterTypeMap, "aaa/bbbDao/ccc.sql", false, false);
              var parser = new SqlParser("select * from emp where name = /* name */'aaa'");
              var sqlNode = parser.parse();
              sqlNode.accept(validator, null);
            }));
    compile();
    assertTrue(getCompiledResult());
  }

  public void testBindVariable_list() throws Exception {
    Class<?> target = SqlValidationDao.class;
    addCompilationUnit(target);
    addCompilationUnit(StringWrapper.class);
    addProcessor(
        new AptProcessor(
            ctx -> {
              var methodElement =
                  ctx.getElements().getMethodElement(target, "testBindVariable_list", List.class);
              var parameterTypeMap = ctx.getElements().getParameterTypeMap(methodElement);
              var validator =
                  new SqlValidator(
                      ctx, methodElement, parameterTypeMap, "aaa/bbbDao/ccc.sql", false, false);
              var parser = new SqlParser("select * from emp where name in /* names */('aaa')");
              var sqlNode = parser.parse();
              sqlNode.accept(validator, null);
            }));
    compile();
    assertTrue(getCompiledResult());
  }

  public void testLiteralVariable() throws Exception {
    Class<?> target = SqlValidationDao.class;
    addCompilationUnit(target);
    addCompilationUnit(StringWrapper.class);
    addProcessor(
        new AptProcessor(
            ctx -> {
              var methodElement =
                  ctx.getElements().getMethodElement(target, "testBindVariable", String.class);
              var parameterTypeMap = ctx.getElements().getParameterTypeMap(methodElement);
              var validator =
                  new SqlValidator(
                      ctx, methodElement, parameterTypeMap, "aaa/bbbDao/ccc.sql", false, false);
              var parser = new SqlParser("select * from emp where name = /*^ name */'aaa'");
              var sqlNode = parser.parse();
              sqlNode.accept(validator, null);
            }));
    compile();
    assertTrue(getCompiledResult());
  }

  public void testLiteralVariable_list() throws Exception {
    Class<?> target = SqlValidationDao.class;
    addCompilationUnit(target);
    addCompilationUnit(StringWrapper.class);
    addProcessor(
        new AptProcessor(
            ctx -> {
              var methodElement =
                  ctx.getElements().getMethodElement(target, "testBindVariable_list", List.class);
              var parameterTypeMap = ctx.getElements().getParameterTypeMap(methodElement);
              var validator =
                  new SqlValidator(
                      ctx, methodElement, parameterTypeMap, "aaa/bbbDao/ccc.sql", false, false);
              var parser = new SqlParser("select * from emp where name in /*^ names */('aaa')");
              var sqlNode = parser.parse();
              sqlNode.accept(validator, null);
            }));
    compile();
    assertTrue(getCompiledResult());
  }

  public void testEmbeddedVariable() throws Exception {
    Class<?> target = SqlValidationDao.class;
    addCompilationUnit(target);
    addProcessor(
        new AptProcessor(
            ctx -> {
              var methodElement =
                  ctx.getElements().getMethodElement(target, "testEmbeddedVariable", String.class);
              var parameterTypeMap = ctx.getElements().getParameterTypeMap(methodElement);
              var validator =
                  new SqlValidator(
                      ctx, methodElement, parameterTypeMap, "aaa/bbbDao/ccc.sql", false, false);
              var parser = new SqlParser("select * from emp /*# orderBy */");
              var sqlNode = parser.parse();
              sqlNode.accept(validator, null);
            }));
    compile();
    assertTrue(getCompiledResult());
  }

  public void testFor() throws Exception {
    Class<?> target = SqlValidationDao.class;
    addCompilationUnit(target);
    addCompilationUnit(StringWrapper.class);
    addProcessor(
        new AptProcessor(
            ctx -> {
              var methodElement = ctx.getElements().getMethodElement(target, "testFor", List.class);
              var parameterTypeMap = ctx.getElements().getParameterTypeMap(methodElement);
              var validator =
                  new SqlValidator(
                      ctx, methodElement, parameterTypeMap, "aaa/bbbDao/ccc.sql", false, false);
              var parser =
                  new SqlParser(
                      "select * from emp where name = /*%for e : names*/ /*e*/'aaa' /*%if e_has_next*/or/*%end*//*%end*/");
              var sqlNode = parser.parse();
              sqlNode.accept(validator, null);
            }));
    compile();
    assertTrue(getCompiledResult());
  }

  public void testFor_identifier() throws Exception {
    Class<?> target = SqlValidationDao.class;
    addCompilationUnit(target);
    addProcessor(
        new AptProcessor(
            ctx -> {
              var methodElement = ctx.getElements().getMethodElement(target, "testFor", List.class);
              var parameterTypeMap = ctx.getElements().getParameterTypeMap(methodElement);
              var validator =
                  new SqlValidator(
                      ctx, methodElement, parameterTypeMap, "aaa/bbbDao/ccc.sql", false, false);
              var parser =
                  new SqlParser(
                      "select * from emp where name = /*%for e : names*/ /*x*/'aaa' /*%if e_has_next*/or/*%end*//*%end*/");
              var sqlNode = parser.parse();
              try {
                sqlNode.accept(validator, null);
                fail();
              } catch (AptException expected) {
                System.out.println(expected.getMessage());
                assertEquals(Message.DOMA4092, expected.getMessageResource());
              }
            }));
    compile();
    assertTrue(getCompiledResult());
  }

  public void testFor_notIterable() throws Exception {
    Class<?> target = SqlValidationDao.class;
    addCompilationUnit(target);
    addProcessor(
        new AptProcessor(
            ctx -> {
              var methodElement =
                  ctx.getElements().getMethodElement(target, "testFor_notIterable", Iterator.class);
              var parameterTypeMap = ctx.getElements().getParameterTypeMap(methodElement);
              var validator =
                  new SqlValidator(
                      ctx, methodElement, parameterTypeMap, "aaa/bbbDao/ccc.sql", false, false);
              var parser =
                  new SqlParser(
                      "select * from emp where name = /*%for e : names*/ /*e*/'aaa' /*%if e_has_next*/or/*%end*//*%end*/");
              var sqlNode = parser.parse();
              try {
                sqlNode.accept(validator, null);
                fail();
              } catch (AptException expected) {
                System.out.println(expected.getMessage());
                assertEquals(Message.DOMA4149, expected.getMessageResource());
              }
            }));
    compile();
    assertTrue(getCompiledResult());
  }

  public void testFor_noTypeArgument() throws Exception {
    Class<?> target = SqlValidationDao.class;
    addCompilationUnit(target);
    addProcessor(
        new AptProcessor(
            ctx -> {
              var methodElement =
                  ctx.getElements().getMethodElement(target, "testFor_noTypeArgument", List.class);
              var parameterTypeMap = ctx.getElements().getParameterTypeMap(methodElement);
              var validator =
                  new SqlValidator(
                      ctx, methodElement, parameterTypeMap, "aaa/bbbDao/ccc.sql", false, false);
              var parser =
                  new SqlParser(
                      "select * from emp where name = /*%for e : names*/ /*e*/'aaa' /*%if e_has_next*/or/*%end*//*%end*/");
              var sqlNode = parser.parse();
              try {
                sqlNode.accept(validator, null);
                fail();
              } catch (AptException expected) {
                System.out.println(expected.getMessage());
                assertEquals(Message.DOMA4150, expected.getMessageResource());
              }
            }));
    compile();
    assertTrue(getCompiledResult());
  }

  public void testExpand() throws Exception {
    Class<?> target = SqlValidationDao.class;
    addCompilationUnit(target);
    addCompilationUnit(StringWrapper.class);
    addProcessor(
        new AptProcessor(
            ctx -> {
              var methodElement =
                  ctx.getElements().getMethodElement(target, "testExpand", String.class);
              var parameterTypeMap = ctx.getElements().getParameterTypeMap(methodElement);
              var validator =
                  new SqlValidator(
                      ctx, methodElement, parameterTypeMap, "aaa/bbbDao/ccc.sql", true, false);
              var parser =
                  new SqlParser("select /*%expand*/* from emp where name = /* name */'aaa'");
              var sqlNode = parser.parse();
              sqlNode.accept(validator, null);
            }));
    compile();
    assertTrue(getCompiledResult());
  }

  public void testExpand_notExpandable() throws Exception {
    Class<?> target = SqlValidationDao.class;
    addCompilationUnit(target);
    addCompilationUnit(StringWrapper.class);
    addProcessor(
        new AptProcessor(
            ctx -> {
              var methodElement =
                  ctx.getElements().getMethodElement(target, "testExpand", String.class);
              var parameterTypeMap = ctx.getElements().getParameterTypeMap(methodElement);
              var validator =
                  new SqlValidator(
                      ctx, methodElement, parameterTypeMap, "aaa/bbbDao/ccc.sql", false, false);
              var parser =
                  new SqlParser("select /*%expand*/* from emp where name = /* name */'aaa'");
              var sqlNode = parser.parse();
              try {
                sqlNode.accept(validator, null);
                fail();
              } catch (AptException expected) {
                System.out.println(expected.getMessage());
                assertEquals(Message.DOMA4257, expected.getMessageResource());
              }
            }));
    compile();
    assertTrue(getCompiledResult());
  }

  public void testPopulate() throws Exception {
    Class<?> target = SqlValidationDao.class;
    addCompilationUnit(target);
    addCompilationUnit(StringWrapper.class);
    addProcessor(
        new AptProcessor(
            ctx -> {
              var methodElement =
                  ctx.getElements().getMethodElement(target, "testPopulate", String.class);
              var parameterTypeMap = ctx.getElements().getParameterTypeMap(methodElement);
              var validator =
                  new SqlValidator(
                      ctx, methodElement, parameterTypeMap, "aaa/bbbDao/ccc.sql", false, true);
              var parser = new SqlParser("update emp set /*%populate*/ id = id");
              var sqlNode = parser.parse();
              sqlNode.accept(validator, null);
            }));
    compile();
    assertTrue(getCompiledResult());
  }

  public void testPopulate_notPopulatable() throws Exception {
    Class<?> target = SqlValidationDao.class;
    addCompilationUnit(target);
    addCompilationUnit(StringWrapper.class);
    addProcessor(
        new AptProcessor(
            ctx -> {
              var methodElement =
                  ctx.getElements().getMethodElement(target, "testPopulate", String.class);
              var parameterTypeMap = ctx.getElements().getParameterTypeMap(methodElement);
              var validator =
                  new SqlValidator(
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
