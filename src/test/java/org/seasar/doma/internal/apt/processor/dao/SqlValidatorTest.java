package org.seasar.doma.internal.apt.processor.dao;

import java.util.Iterator;
import java.util.List;
import org.seasar.doma.internal.apt.AptTestCase;
import org.seasar.doma.internal.apt.expr.ExpressionValidator;
import org.seasar.doma.internal.apt.sql.SqlTemplate;
import org.seasar.doma.internal.apt.sql.SqlValidator;
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
              var parser = new SqlParser("select * from emp where name = /* name */'aaa'");
              var sqlNode = parser.parse();
              var sqlTemplate = new SqlTemplate(ctx, methodElement, "aaa/bbbDao/ccc.sql", sqlNode);
              var validator =
                  new SqlValidator(
                      ctx,
                      methodElement,
                      sqlTemplate,
                      new ExpressionValidator(ctx, parameterTypeMap),
                      false,
                      false);
              validator.validate();
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
              var parser = new SqlParser("select * from emp where name in /* names */('aaa')");
              var sqlNode = parser.parse();
              var sqlTemplate = new SqlTemplate(ctx, methodElement, "aaa/bbbDao/ccc.sql", sqlNode);
              var validator =
                  new SqlValidator(
                      ctx,
                      methodElement,
                      sqlTemplate,
                      new ExpressionValidator(ctx, parameterTypeMap),
                      false,
                      false);
              validator.validate();
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
              var parser = new SqlParser("select * from emp where name = /*^ name */'aaa'");
              var sqlNode = parser.parse();
              var sqlTemplate = new SqlTemplate(ctx, methodElement, "aaa/bbbDao/ccc.sql", sqlNode);
              var validator =
                  new SqlValidator(
                      ctx,
                      methodElement,
                      sqlTemplate,
                      new ExpressionValidator(ctx, parameterTypeMap),
                      false,
                      false);
              validator.validate();
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
              var parser = new SqlParser("select * from emp where name in /*^ names */('aaa')");
              var sqlNode = parser.parse();
              var sqlTemplate = new SqlTemplate(ctx, methodElement, "aaa/bbbDao/ccc.sql", sqlNode);
              var validator =
                  new SqlValidator(
                      ctx,
                      methodElement,
                      sqlTemplate,
                      new ExpressionValidator(ctx, parameterTypeMap),
                      false,
                      false);
              validator.validate();
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
              var parser = new SqlParser("select * from emp /*# orderBy */");
              var sqlNode = parser.parse();
              var sqlTemplate = new SqlTemplate(ctx, methodElement, "aaa/bbbDao/ccc.sql", sqlNode);
              var validator =
                  new SqlValidator(
                      ctx,
                      methodElement,
                      sqlTemplate,
                      new ExpressionValidator(ctx, parameterTypeMap),
                      false,
                      false);
              validator.validate();
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
              var parser =
                  new SqlParser(
                      "select * from emp where name = /*%for e : names*/ /*e*/'aaa' /*%if e_has_next*/or/*%end*//*%end*/");
              var sqlNode = parser.parse();
              var sqlTemplate = new SqlTemplate(ctx, methodElement, "aaa/bbbDao/ccc.sql", sqlNode);
              var validator =
                  new SqlValidator(
                      ctx,
                      methodElement,
                      sqlTemplate,
                      new ExpressionValidator(ctx, parameterTypeMap),
                      false,
                      false);
              validator.validate();
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
              var parser =
                  new SqlParser(
                      "select * from emp where name = /*%for e : names*/ /*x*/'aaa' /*%if e_has_next*/or/*%end*//*%end*/");
              var sqlNode = parser.parse();
              var sqlTemplate = new SqlTemplate(ctx, methodElement, "aaa/bbbDao/ccc.sql", sqlNode);
              var validator =
                  new SqlValidator(
                      ctx,
                      methodElement,
                      sqlTemplate,
                      new ExpressionValidator(ctx, parameterTypeMap),
                      false,
                      false);
              validator.validate();
            }));
    compile();
    assertFalse(getCompiledResult());
    assertEquals(Message.DOMA4092, getMessageCode());
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
              var parser =
                  new SqlParser(
                      "select * from emp where name = /*%for e : names*/ /*e*/'aaa' /*%if e_has_next*/or/*%end*//*%end*/");
              var sqlNode = parser.parse();
              var sqlTemplate = new SqlTemplate(ctx, methodElement, "aaa/bbbDao/ccc.sql", sqlNode);
              var validator =
                  new SqlValidator(
                      ctx,
                      methodElement,
                      sqlTemplate,
                      new ExpressionValidator(ctx, parameterTypeMap),
                      false,
                      false);
              validator.validate();
            }));
    compile();
    assertFalse(getCompiledResult());
    assertEquals(Message.DOMA4149, getMessageCode());
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
              var parser =
                  new SqlParser(
                      "select * from emp where name = /*%for e : names*/ /*e*/'aaa' /*%if e_has_next*/or/*%end*//*%end*/");
              var sqlNode = parser.parse();
              var sqlTemplate = new SqlTemplate(ctx, methodElement, "aaa/bbbDao/ccc.sql", sqlNode);
              var validator =
                  new SqlValidator(
                      ctx,
                      methodElement,
                      sqlTemplate,
                      new ExpressionValidator(ctx, parameterTypeMap),
                      false,
                      false);
              validator.validate();
            }));
    compile();
    assertFalse(getCompiledResult());
    assertEquals(Message.DOMA4150, getMessageCode());
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
              var parser =
                  new SqlParser("select /*%expand*/* from emp where name = /* name */'aaa'");
              var sqlNode = parser.parse();
              var sqlTemplate = new SqlTemplate(ctx, methodElement, "aaa/bbbDao/ccc.sql", sqlNode);
              var validator =
                  new SqlValidator(
                      ctx,
                      methodElement,
                      sqlTemplate,
                      new ExpressionValidator(ctx, parameterTypeMap),
                      true,
                      false);
              validator.validate();
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
              var parser =
                  new SqlParser("select /*%expand*/* from emp where name = /* name */'aaa'");
              var sqlNode = parser.parse();
              var sqlTemplate = new SqlTemplate(ctx, methodElement, "aaa/bbbDao/ccc.sql", sqlNode);
              var validator =
                  new SqlValidator(
                      ctx,
                      methodElement,
                      sqlTemplate,
                      new ExpressionValidator(ctx, parameterTypeMap),
                      false,
                      false);
              validator.validate();
            }));
    compile();
    assertFalse(getCompiledResult());
    assertEquals(Message.DOMA4257, getMessageCode());
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
              var parser = new SqlParser("update emp set /*%populate*/ id = id");
              var sqlNode = parser.parse();
              var sqlTemplate = new SqlTemplate(ctx, methodElement, "aaa/bbbDao/ccc.sql", sqlNode);
              var validator =
                  new SqlValidator(
                      ctx,
                      methodElement,
                      sqlTemplate,
                      new ExpressionValidator(ctx, parameterTypeMap),
                      false,
                      true);
              validator.validate();
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
              var parser = new SqlParser("update emp set /*%populate*/ id = id");
              var sqlNode = parser.parse();
              var sqlTemplate = new SqlTemplate(ctx, methodElement, "aaa/bbbDao/ccc.sql", sqlNode);
              var validator =
                  new SqlValidator(
                      ctx,
                      methodElement,
                      sqlTemplate,
                      new ExpressionValidator(ctx, parameterTypeMap),
                      false,
                      false);
              validator.validate();
            }));
    compile();
    assertFalse(getCompiledResult());
    assertEquals(Message.DOMA4270, getMessageCode());
  }
}
