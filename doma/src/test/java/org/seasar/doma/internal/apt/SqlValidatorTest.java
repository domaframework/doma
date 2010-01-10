/*
 * Copyright 2004-2009 the Seasar Foundation and the Others.
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
package org.seasar.doma.internal.apt;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;

import org.seasar.doma.internal.apt.dao.SqlValidationDao;
import org.seasar.doma.internal.apt.util.ElementUtil;
import org.seasar.doma.internal.apt.util.TypeMirrorUtil;
import org.seasar.doma.internal.jdbc.sql.SqlParser;
import org.seasar.doma.internal.message.Message;
import org.seasar.doma.jdbc.SqlNode;
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
        compile();

        ExecutableElement methodElement = createMethodElement(target,
                "testBindVariable", String.class);
        Map<String, TypeMirror> parameterTypeMap = createParameterTypeMap(methodElement);
        SqlValidator validator = new SqlValidator(getProcessingEnvironment(),
                methodElement, parameterTypeMap, "aaa/bbbDao/ccc.sql");
        SqlParser parser = new SqlParser(
                "select * from emp where name = /* name */'aaa'");
        SqlNode sqlNode = parser.parse();
        sqlNode.accept(validator, null);
    }

    public void testBindVariable_list() throws Exception {
        Class<?> target = SqlValidationDao.class;
        addCompilationUnit(target);
        addCompilationUnit(StringWrapper.class);
        compile();

        ExecutableElement methodElement = createMethodElement(target,
                "testBindVariable_list", List.class);
        Map<String, TypeMirror> parameterTypeMap = createParameterTypeMap(methodElement);
        SqlValidator validator = new SqlValidator(getProcessingEnvironment(),
                methodElement, parameterTypeMap, "aaa/bbbDao/ccc.sql");
        SqlParser parser = new SqlParser(
                "select * from emp where name in /* names */('aaa')");
        SqlNode sqlNode = parser.parse();
        sqlNode.accept(validator, null);
    }

    public void testEmbeddedVariable() throws Exception {
        Class<?> target = SqlValidationDao.class;
        addCompilationUnit(target);
        compile();

        ExecutableElement methodElement = createMethodElement(target,
                "testEmbeddedVariable", String.class);
        Map<String, TypeMirror> parameterTypeMap = createParameterTypeMap(methodElement);
        SqlValidator validator = new SqlValidator(getProcessingEnvironment(),
                methodElement, parameterTypeMap, "aaa/bbbDao/ccc.sql");
        SqlParser parser = new SqlParser("select * from emp /*# orderBy */");
        SqlNode sqlNode = parser.parse();
        sqlNode.accept(validator, null);
    }

    public void testFor() throws Exception {
        Class<?> target = SqlValidationDao.class;
        addCompilationUnit(target);
        addCompilationUnit(StringWrapper.class);
        compile();

        ExecutableElement methodElement = createMethodElement(target,
                "testFor", List.class);
        Map<String, TypeMirror> parameterTypeMap = createParameterTypeMap(methodElement);
        SqlValidator validator = new SqlValidator(getProcessingEnvironment(),
                methodElement, parameterTypeMap, "aaa/bbbDao/ccc.sql");
        SqlParser parser = new SqlParser(
                "select * from emp where name = /*%for e : names*/ /*e*/'aaa' /*%if e_has_next*/or/*%end*//*%end*/");
        SqlNode sqlNode = parser.parse();
        sqlNode.accept(validator, null);
    }

    public void testFor_identifier() throws Exception {
        Class<?> target = SqlValidationDao.class;
        addCompilationUnit(target);
        compile();

        ExecutableElement methodElement = createMethodElement(target,
                "testFor", List.class);
        Map<String, TypeMirror> parameterTypeMap = createParameterTypeMap(methodElement);
        SqlValidator validator = new SqlValidator(getProcessingEnvironment(),
                methodElement, parameterTypeMap, "aaa/bbbDao/ccc.sql");
        SqlParser parser = new SqlParser(
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

    public void testFor_notIterable() throws Exception {
        Class<?> target = SqlValidationDao.class;
        addCompilationUnit(target);
        compile();

        ExecutableElement methodElement = createMethodElement(target,
                "testFor_notIterable", Iterator.class);
        Map<String, TypeMirror> parameterTypeMap = createParameterTypeMap(methodElement);
        SqlValidator validator = new SqlValidator(getProcessingEnvironment(),
                methodElement, parameterTypeMap, "aaa/bbbDao/ccc.sql");
        SqlParser parser = new SqlParser(
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

    public void testFor_noTypeArgument() throws Exception {
        Class<?> target = SqlValidationDao.class;
        addCompilationUnit(target);
        compile();

        ExecutableElement methodElement = createMethodElement(target,
                "testFor_noTypeArgument", List.class);
        Map<String, TypeMirror> parameterTypeMap = createParameterTypeMap(methodElement);
        SqlValidator validator = new SqlValidator(getProcessingEnvironment(),
                methodElement, parameterTypeMap, "aaa/bbbDao/ccc.sql");
        SqlParser parser = new SqlParser(
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

    protected ExecutableElement createMethodElement(Class<?> clazz,
            String methodName, Class<?>... parameterClasses) {
        ProcessingEnvironment env = getProcessingEnvironment();
        TypeElement typeElement = ElementUtil.getTypeElement(clazz, env);
        for (TypeElement t = typeElement; t != null
                && t.asType().getKind() != TypeKind.NONE; t = TypeMirrorUtil
                .toTypeElement(t.getSuperclass(), env)) {
            for (ExecutableElement methodElement : ElementFilter.methodsIn(t
                    .getEnclosedElements())) {
                if (!methodElement.getSimpleName().contentEquals(methodName)) {
                    continue;
                }
                List<? extends VariableElement> parameterElements = methodElement
                        .getParameters();
                if (parameterElements.size() != parameterClasses.length) {
                    continue;
                }
                for (int i = 0; i < parameterElements.size(); i++) {
                    TypeMirror parameterType = parameterElements.get(i)
                            .asType();
                    Class<?> parameterClass = parameterClasses[i];
                    if (!TypeMirrorUtil.isSameType(parameterType,
                            parameterClass, env)) {
                        return null;
                    }
                }
                return methodElement;
            }
        }
        return null;
    }

    protected Map<String, TypeMirror> createParameterTypeMap(
            ExecutableElement methodElement) {
        Map<String, TypeMirror> result = new HashMap<String, TypeMirror>();
        for (VariableElement parameter : methodElement.getParameters()) {
            String name = parameter.getSimpleName().toString();
            TypeMirror type = parameter.asType();
            result.put(name, type);
        }
        return result;
    }

}
