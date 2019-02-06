package org.seasar.doma.internal.apt.processor.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.type.TypeMirror;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.seasar.doma.internal.apt.AptException;
import org.seasar.doma.internal.apt.AptTestProcessor;
import org.seasar.doma.internal.apt.CompilerSupport;
import org.seasar.doma.internal.apt.validator.SqlValidator;
import org.seasar.doma.internal.jdbc.sql.SqlParser;
import org.seasar.doma.jdbc.SqlNode;
import org.seasar.doma.message.Message;
import org.seasar.doma.wrapper.StringWrapper;

public class SqlValidatorTest extends CompilerSupport {

  @BeforeEach
  protected void setUp() throws Exception {
    addSourcePath("src/main/java");
    addSourcePath("src/test/java");
  }

  @Test
  public void testBindVariable() throws Exception {
    Class<?> target = SqlValidationDao.class;
    addCompilationUnit(target);
    addCompilationUnit(StringWrapper.class);
    addProcessor(
        new AptTestProcessor() {
          @Override
          protected void run() {
            ExecutableElement methodElement =
                createMethodElement(target, "testBindVariable", String.class);
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
  public void testBindVariable_list() throws Exception {
    Class<?> target = SqlValidationDao.class;
    addCompilationUnit(target);
    addCompilationUnit(StringWrapper.class);
    addProcessor(
        new AptTestProcessor() {
          @Override
          protected void run() {
            ExecutableElement methodElement =
                createMethodElement(target, "testBindVariable_list", List.class);
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
  public void testBindVariable_array() throws Exception {
    Class<?> target = SqlValidationDao.class;
    addCompilationUnit(target);
    addCompilationUnit(StringWrapper.class);
    addProcessor(
        new AptTestProcessor() {
          @Override
          protected void run() {
            ExecutableElement methodElement =
                createMethodElement(target, "testBindVariable_array", String[].class);
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
  public void testLiteralVariable() throws Exception {
    Class<?> target = SqlValidationDao.class;
    addCompilationUnit(target);
    addCompilationUnit(StringWrapper.class);
    addProcessor(
        new AptTestProcessor() {
          @Override
          protected void run() {
            ExecutableElement methodElement =
                createMethodElement(target, "testBindVariable", String.class);
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
  public void testLiteralVariable_list() throws Exception {
    Class<?> target = SqlValidationDao.class;
    addCompilationUnit(target);
    addCompilationUnit(StringWrapper.class);
    addProcessor(
        new AptTestProcessor() {
          @Override
          protected void run() {
            ExecutableElement methodElement =
                createMethodElement(target, "testBindVariable_list", List.class);
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
  public void testLiteralVariable_array() throws Exception {
    Class<?> target = SqlValidationDao.class;
    addCompilationUnit(target);
    addCompilationUnit(StringWrapper.class);
    addProcessor(
        new AptTestProcessor() {
          @Override
          protected void run() {
            ExecutableElement methodElement =
                createMethodElement(target, "testBindVariable_array", String[].class);
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
  public void testEmbeddedVariable() throws Exception {
    Class<?> target = SqlValidationDao.class;
    addCompilationUnit(target);
    addProcessor(
        new AptTestProcessor() {
          @Override
          protected void run() {
            ExecutableElement methodElement =
                createMethodElement(target, "testEmbeddedVariable", String.class);
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
  public void testFor() throws Exception {
    Class<?> target = SqlValidationDao.class;
    addCompilationUnit(target);
    addCompilationUnit(StringWrapper.class);
    addProcessor(
        new AptTestProcessor() {
          @Override
          protected void run() {
            ExecutableElement methodElement = createMethodElement(target, "testFor", List.class);
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
  public void testFor_array() throws Exception {
    Class<?> target = SqlValidationDao.class;
    addCompilationUnit(target);
    addCompilationUnit(StringWrapper.class);
    addProcessor(
        new AptTestProcessor() {
          @Override
          protected void run() {
            ExecutableElement methodElement =
                createMethodElement(target, "testFor_array", String[].class);
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
  public void testFor_identifier() throws Exception {
    Class<?> target = SqlValidationDao.class;
    addCompilationUnit(target);
    addProcessor(
        new AptTestProcessor() {
          @Override
          protected void run() {
            ExecutableElement methodElement = createMethodElement(target, "testFor", List.class);
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
  public void testFor_notIterable() throws Exception {
    Class<?> target = SqlValidationDao.class;
    addCompilationUnit(target);
    addProcessor(
        new AptTestProcessor() {
          @Override
          protected void run() {
            ExecutableElement methodElement =
                createMethodElement(target, "testFor_notIterable", Iterator.class);
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
  public void testFor_noTypeArgument() throws Exception {
    Class<?> target = SqlValidationDao.class;
    addCompilationUnit(target);
    addProcessor(
        new AptTestProcessor() {
          @Override
          protected void run() {
            ExecutableElement methodElement =
                createMethodElement(target, "testFor_noTypeArgument", List.class);
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
  public void testExpand() throws Exception {
    Class<?> target = SqlValidationDao.class;
    addCompilationUnit(target);
    addCompilationUnit(StringWrapper.class);
    addProcessor(
        new AptTestProcessor() {
          @Override
          protected void run() {
            ExecutableElement methodElement =
                createMethodElement(target, "testExpand", String.class);
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
  public void testExpand_notExpandable() throws Exception {
    Class<?> target = SqlValidationDao.class;
    addCompilationUnit(target);
    addCompilationUnit(StringWrapper.class);
    addProcessor(
        new AptTestProcessor() {
          @Override
          protected void run() {
            ExecutableElement methodElement =
                createMethodElement(target, "testExpand", String.class);
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
  public void testPopulate() throws Exception {
    Class<?> target = SqlValidationDao.class;
    addCompilationUnit(target);
    addCompilationUnit(StringWrapper.class);
    addProcessor(
        new AptTestProcessor() {
          @Override
          protected void run() {
            ExecutableElement methodElement =
                createMethodElement(target, "testPopulate", String.class);
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
  public void testPopulate_notPopulatable() throws Exception {
    Class<?> target = SqlValidationDao.class;
    addCompilationUnit(target);
    addCompilationUnit(StringWrapper.class);
    addProcessor(
        new AptTestProcessor() {
          @Override
          protected void run() {
            ExecutableElement methodElement =
                createMethodElement(target, "testPopulate", String.class);
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
