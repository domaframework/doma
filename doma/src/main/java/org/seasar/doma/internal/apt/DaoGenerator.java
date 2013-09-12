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
package org.seasar.doma.internal.apt;

import static org.seasar.doma.internal.util.AssertionUtil.*;

import java.io.IOException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.util.Iterator;
import java.util.List;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.TypeElement;
import javax.sql.DataSource;

import org.seasar.doma.AnnotationTarget;
import org.seasar.doma.DomaNullPointerException;
import org.seasar.doma.MapKeyNamingType;
import org.seasar.doma.internal.apt.meta.AbstractCreateQueryMeta;
import org.seasar.doma.internal.apt.meta.ArrayCreateQueryMeta;
import org.seasar.doma.internal.apt.meta.AutoBatchModifyQueryMeta;
import org.seasar.doma.internal.apt.meta.AutoFunctionQueryMeta;
import org.seasar.doma.internal.apt.meta.AutoModifyQueryMeta;
import org.seasar.doma.internal.apt.meta.AutoModuleQueryMeta;
import org.seasar.doma.internal.apt.meta.AutoProcedureQueryMeta;
import org.seasar.doma.internal.apt.meta.BasicInOutParameterMeta;
import org.seasar.doma.internal.apt.meta.BasicInParameterMeta;
import org.seasar.doma.internal.apt.meta.BasicListParameterMeta;
import org.seasar.doma.internal.apt.meta.BasicListResultParameterMeta;
import org.seasar.doma.internal.apt.meta.BasicOutParameterMeta;
import org.seasar.doma.internal.apt.meta.BasicResultParameterMeta;
import org.seasar.doma.internal.apt.meta.CallableSqlParameterMeta;
import org.seasar.doma.internal.apt.meta.CallableSqlParameterMetaVisitor;
import org.seasar.doma.internal.apt.meta.DaoMeta;
import org.seasar.doma.internal.apt.meta.DelegateQueryMeta;
import org.seasar.doma.internal.apt.meta.DomainInOutParameterMeta;
import org.seasar.doma.internal.apt.meta.DomainInParameterMeta;
import org.seasar.doma.internal.apt.meta.DomainListParameterMeta;
import org.seasar.doma.internal.apt.meta.DomainListResultParameterMeta;
import org.seasar.doma.internal.apt.meta.DomainOutParameterMeta;
import org.seasar.doma.internal.apt.meta.DomainResultParameterMeta;
import org.seasar.doma.internal.apt.meta.EntityListParameterMeta;
import org.seasar.doma.internal.apt.meta.EntityListResultParameterMeta;
import org.seasar.doma.internal.apt.meta.MapListParameterMeta;
import org.seasar.doma.internal.apt.meta.MapListResultParameterMeta;
import org.seasar.doma.internal.apt.meta.ParentDaoMeta;
import org.seasar.doma.internal.apt.meta.QueryMeta;
import org.seasar.doma.internal.apt.meta.QueryMetaVisitor;
import org.seasar.doma.internal.apt.meta.QueryParameterMeta;
import org.seasar.doma.internal.apt.meta.QueryReturnMeta;
import org.seasar.doma.internal.apt.meta.SqlFileBatchModifyQueryMeta;
import org.seasar.doma.internal.apt.meta.SqlFileModifyQueryMeta;
import org.seasar.doma.internal.apt.meta.SqlFileScriptQueryMeta;
import org.seasar.doma.internal.apt.meta.SqlFileSelectQueryMeta;
import org.seasar.doma.internal.apt.mirror.AnnotationMirror;
import org.seasar.doma.internal.apt.type.BasicType;
import org.seasar.doma.internal.apt.type.DomainType;
import org.seasar.doma.internal.apt.type.EntityType;
import org.seasar.doma.internal.apt.type.EnumWrapperType;
import org.seasar.doma.internal.apt.type.IterableType;
import org.seasar.doma.internal.apt.type.IterationCallbackType;
import org.seasar.doma.internal.apt.type.MapType;
import org.seasar.doma.internal.apt.type.SimpleDataTypeVisitor;
import org.seasar.doma.internal.apt.type.WrapperType;
import org.seasar.doma.internal.jdbc.command.BasicIterationHandler;
import org.seasar.doma.internal.jdbc.command.BasicResultListHandler;
import org.seasar.doma.internal.jdbc.command.BasicSingleResultHandler;
import org.seasar.doma.internal.jdbc.command.DomainIterationHandler;
import org.seasar.doma.internal.jdbc.command.DomainResultListHandler;
import org.seasar.doma.internal.jdbc.command.DomainSingleResultHandler;
import org.seasar.doma.internal.jdbc.command.EntityIterationHandler;
import org.seasar.doma.internal.jdbc.command.EntityResultListHandler;
import org.seasar.doma.internal.jdbc.command.EntitySingleResultHandler;
import org.seasar.doma.internal.jdbc.command.MapIterationHandler;
import org.seasar.doma.internal.jdbc.command.MapResultListHandler;
import org.seasar.doma.internal.jdbc.command.MapSingleResultHandler;
import org.seasar.doma.internal.jdbc.dao.AbstractDao;
import org.seasar.doma.internal.jdbc.sql.BasicInOutParameter;
import org.seasar.doma.internal.jdbc.sql.BasicInParameter;
import org.seasar.doma.internal.jdbc.sql.BasicListParameter;
import org.seasar.doma.internal.jdbc.sql.BasicListResultParameter;
import org.seasar.doma.internal.jdbc.sql.BasicOutParameter;
import org.seasar.doma.internal.jdbc.sql.BasicResultParameter;
import org.seasar.doma.internal.jdbc.sql.DomainInOutParameter;
import org.seasar.doma.internal.jdbc.sql.DomainInParameter;
import org.seasar.doma.internal.jdbc.sql.DomainListParameter;
import org.seasar.doma.internal.jdbc.sql.DomainListResultParameter;
import org.seasar.doma.internal.jdbc.sql.DomainOutParameter;
import org.seasar.doma.internal.jdbc.sql.DomainResultParameter;
import org.seasar.doma.internal.jdbc.sql.EntityListParameter;
import org.seasar.doma.internal.jdbc.sql.EntityListResultParameter;
import org.seasar.doma.internal.jdbc.sql.MapListParameter;
import org.seasar.doma.internal.jdbc.sql.MapListResultParameter;
import org.seasar.doma.internal.jdbc.util.ScriptFileUtil;
import org.seasar.doma.internal.jdbc.util.SqlFileUtil;
import org.seasar.doma.jdbc.Config;

/**
 * 
 * @author taedium
 * 
 */
public class DaoGenerator extends AbstractGenerator {

    protected final DaoMeta daoMeta;

    public DaoGenerator(ProcessingEnvironment env, TypeElement daoElement,
            DaoMeta daoMeta) throws IOException {
        super(env, daoElement, Options.getDaoPackage(env), Options
                .getDaoSubpackage(env), "", Options.getDaoSuffix(env));
        assertNotNull(daoMeta);
        this.daoMeta = daoMeta;
    }

    @Override
    public void generate() {
        printPackage();
        printClass();
    }

    protected void printPackage() {
        if (!packageName.isEmpty()) {
            iprint("package %1$s;%n", packageName);
            iprint("%n");
        }
    }

    protected void printClass() {
        iprint("/** */%n");
        for (AnnotationMirror annotation : daoMeta
                .getAnnotationMirrors(AnnotationTarget.CLASS)) {
            iprint("@%1$s(%2$s)%n", annotation.getTypeValue(),
                    annotation.getElementsValue());
        }
        printGenerated();
        String parentClassName = AbstractDao.class.getName();
        ParentDaoMeta parentDaoMeta = daoMeta.getParentDaoMeta();
        if (parentDaoMeta != null) {
            TypeElement parentDaotElement = parentDaoMeta.getDaoElement();
            parentClassName = createQualifiedName(env, parentDaotElement,
                    fullpackage, subpackage, prefix, suffix);
        }
        iprint("public class %1$s extends %2$s implements %3$s {%n",
                simpleName, parentClassName, daoMeta.getDaoType());
        print("%n");
        indent();
        printValidateVersionStaticInitializer();
        printStaticFields();
        printConstructors();
        printMethods();
        unindent();
        print("}%n");
    }

    protected void printStaticFields() {
        int i = 0;
        for (QueryMeta queryMeta : daoMeta.getQueryMetas()) {
            if (queryMeta.getQueryKind().isTrigger()) {
                iprint("private static final %1$s __method%2$s = %3$s.__getDeclaredMethod(%4$s.class, \"%5$s\"",
                        Method.class.getName(), i, AbstractDao.class.getName(),
                        daoMeta.getDaoType(), queryMeta.getName());
                for (QueryParameterMeta parameterMeta : queryMeta
                        .getParameterMetas()) {
                    print(", %1$s.class", parameterMeta.getQualifiedName());
                }
                print(");%n");
                print("%n");
            }
            i++;
        }
    }

    protected void printConstructors() {
        if (daoMeta.hasUserDefinedConfig()) {
            iprint("/** */%n");
            iprint("public %1$s() {%n", simpleName);
            indent();
            iprint("super(new %1$s());%n", daoMeta.getConfigType());
            unindent();
            iprint("}%n");
            print("%n");
            if (daoMeta.getAnnotateWithMirror() == null) {
                ParentDaoMeta parentDaoMeta = daoMeta.getParentDaoMeta();
                boolean jdbcConstructorsNecessary = parentDaoMeta == null
                        || parentDaoMeta.hasUserDefinedConfig();
                if (jdbcConstructorsNecessary) {
                    iprint("/**%n");
                    iprint(" * @param connection the connection%n");
                    iprint(" */%n");
                    iprint("public %1$s(%2$s connection) {%n", simpleName,
                            Connection.class.getName());
                    indent();
                    iprint("super(new %1$s(), connection);%n",
                            daoMeta.getConfigType());
                    unindent();
                    iprint("}%n");
                    print("%n");
                    iprint("/**%n");
                    iprint(" * @param dataSource the dataSource%n");
                    iprint(" */%n");
                    iprint("public %1$s(%2$s dataSource) {%n", simpleName,
                            DataSource.class.getName());
                    indent();
                    iprint("super(new %1$s(), dataSource);%n",
                            daoMeta.getConfigType());
                    unindent();
                    iprint("}%n");
                    print("%n");
                }
                iprint("/**%n");
                iprint(" * @param config the configuration%n");
                iprint(" */%n");
                iprint("protected %1$s(%2$s config) {%n", simpleName,
                        Config.class.getName());
                indent();
                iprint("super(config);%n", daoMeta.getConfigType());
                unindent();
                iprint("}%n");
                print("%n");
                if (jdbcConstructorsNecessary) {
                    iprint("/**%n");
                    iprint(" * @param config the configuration%n");
                    iprint(" * @param connection the connection%n");
                    iprint(" */%n");
                    iprint("protected %1$s(%2$s config, %3$s connection) {%n",
                            simpleName, Config.class.getName(),
                            Connection.class.getName());
                    indent();
                    iprint("super(config, connection);%n",
                            daoMeta.getConfigType());
                    unindent();
                    iprint("}%n");
                    print("%n");
                    iprint("/**%n");
                    iprint(" * @param config the configuration%n");
                    iprint(" * @param dataSource the dataSource%n");
                    iprint(" */%n");
                    iprint("protected %1$s(%2$s config, %3$s dataSource) {%n",
                            simpleName, Config.class.getName(),
                            DataSource.class.getName());
                    indent();
                    iprint("super(config, dataSource);%n",
                            daoMeta.getConfigType());
                    unindent();
                    iprint("}%n");
                    print("%n");
                }
            }
        }
        if (!daoMeta.hasUserDefinedConfig()
                || daoMeta.getAnnotateWithMirror() != null) {
            iprint("/**%n");
            iprint(" * @param config the config%n");
            iprint(" */%n");
            for (AnnotationMirror annotation : daoMeta
                    .getAnnotationMirrors(AnnotationTarget.CONSTRUCTOR)) {
                iprint("@%1$s(%2$s)%n", annotation.getTypeValue(),
                        annotation.getElementsValue());
            }
            iprint("public %1$s(", simpleName);
            for (AnnotationMirror annotation : daoMeta
                    .getAnnotationMirrors(AnnotationTarget.CONSTRUCTOR_PARAMETER)) {
                print("@%1$s(%2$s) ", annotation.getTypeValue(),
                        annotation.getElementsValue());
            }
            print("%1$s config) {%n", Config.class.getName());
            indent();
            iprint("super(config);%n");
            unindent();
            iprint("}%n");
            print("%n");
        }
    }

    protected boolean isJdbcConstructoNecessary() {
        ParentDaoMeta parentDaoMeta = daoMeta.getParentDaoMeta();
        return parentDaoMeta == null || parentDaoMeta.hasUserDefinedConfig();
    }

    protected void printMethods() {
        MethodBodyGenerator generator = new MethodBodyGenerator();
        int i = 0;
        for (QueryMeta queryMeta : daoMeta.getQueryMetas()) {
            printMethod(generator, queryMeta, i);
            i++;
        }
    }

    protected void printMethod(MethodBodyGenerator generator, QueryMeta m,
            int index) {
        iprint("@Override%n");
        iprint("public ");
        if (!m.getTypeParameterNames().isEmpty()) {
            print("<");
            for (Iterator<String> it = m.getTypeParameterNames().iterator(); it
                    .hasNext();) {
                print("%1$s", it.next());
                if (it.hasNext()) {
                    print(", ");
                }
            }
            print("> ");
        }
        print("%1$s %2$s(", m.getReturnMeta().getTypeName(), m.getName());
        for (Iterator<QueryParameterMeta> it = m.getParameterMetas().iterator(); it
                .hasNext();) {
            QueryParameterMeta parameterMeta = it.next();
            String parameterTypeName = parameterMeta.getTypeName();
            if (!it.hasNext() && m.isVarArgs()) {
                parameterTypeName = parameterTypeName.replace("[]", "...");
            }
            print("%1$s %2$s", parameterTypeName, parameterMeta.getName());
            if (it.hasNext()) {
                print(", ");
            }
        }
        print(") ");
        if (!m.getThrownTypeNames().isEmpty()) {
            print("throws ");
            for (Iterator<String> it = m.getThrownTypeNames().iterator(); it
                    .hasNext();) {
                print("%1$s", it.next());
                if (it.hasNext()) {
                    print(", ");
                }
            }
            print(" ");
        }
        print("{%n");
        indent();
        m.accept(generator, index);
        unindent();
        iprint("}%n");
        print("%n");
    }

    protected class MethodBodyGenerator implements
            QueryMetaVisitor<Void, Integer> {

        @Override
        public Void visitSqlFileSelectQueryMeta(final SqlFileSelectQueryMeta m,
                Integer p) {
            printEnteringStatements(m);
            printPrerequisiteStatements(m);

            iprint("%1$s __query = new %1$s();%n", m.getQueryClass().getName());
            if (m.isTrigger()) {
                iprint("__query.setMethod(__method%1$s);%n", p);
            }
            iprint("__query.setConfig(config);%n");
            iprint("__query.setSqlFilePath(\"%1$s\");%n",
                    SqlFileUtil.buildPath(daoMeta.getDaoElement()
                            .getQualifiedName().toString(), m.getName()));
            if (m.getSelectOptionsType() != null) {
                iprint("__query.setOptions(%1$s);%n",
                        m.getSelectOptionsParameterName());
            }
            for (Iterator<QueryParameterMeta> it = m.getParameterMetas()
                    .iterator(); it.hasNext();) {
                QueryParameterMeta parameterMeta = it.next();
                if (parameterMeta.isBindable()) {
                    iprint("__query.addParameter(\"%1$s\", %2$s.class, %1$s);%n",
                            parameterMeta.getName(),
                            parameterMeta.getQualifiedName());
                }
            }
            iprint("__query.setCallerClassName(\"%1$s\");%n", qualifiedName);
            iprint("__query.setCallerMethodName(\"%1$s\");%n", m.getName());
            iprint("__query.setResultEnsured(%1$s);%n", m.getEnsureResult());
            iprint("__query.setResultMappingEnsured(%1$s);%n",
                    m.getEnsureResultMapping());
            iprint("__query.setQueryTimeout(%1$s);%n", m.getQueryTimeout());
            iprint("__query.setMaxRows(%1$s);%n", m.getMaxRows());
            iprint("__query.setFetchSize(%1$s);%n", m.getFetchSize());
            iprint("__query.prepare();%n");
            final QueryReturnMeta resultMeta = m.getReturnMeta();
            final String commandClassName = m.getCommandClass().getName();
            if (m.getIterate()) {
                IterationCallbackType callbackType = m
                        .getIterationCallbackType();
                final String callbackParamName = m
                        .getIterationCallbackPrameterName();
                callbackType
                        .getTargetType()
                        .accept(new SimpleDataTypeVisitor<Void, Void, RuntimeException>() {

                            @Override
                            public Void visitBasicType(BasicType dataType,
                                    Void p) throws RuntimeException {
                                dataType.getWrapperType()
                                        .accept(new SimpleDataTypeVisitor<Void, Void, RuntimeException>() {

                                            @Override
                                            public Void visitEnumWrapperType(
                                                    EnumWrapperType dataType,
                                                    Void p)
                                                    throws RuntimeException {
                                                iprint("%1$s<%2$s> __command = new %1$s<%2$s>(__query, new %3$s<%2$s, %4$s>(new %5$s(%6$s.class), %7$s));%n",
                                                        commandClassName,
                                                        resultMeta
                                                                .getTypeNameAsTypeParameter(),
                                                        BasicIterationHandler.class
                                                                .getName(),
                                                        dataType.getWrappedType()
                                                                .getTypeNameAsTypeParameter(),
                                                        dataType.getTypeName(),
                                                        dataType.getWrappedType()
                                                                .getQualifiedName(),
                                                        callbackParamName);
                                                return null;
                                            }

                                            @Override
                                            public Void visitWrapperType(
                                                    WrapperType dataType, Void p)
                                                    throws RuntimeException {
                                                iprint("%1$s<%2$s> __command = new %1$s<%2$s>(__query, new %3$s<%2$s, %4$s>(new %5$s(), %6$s));%n",
                                                        commandClassName,
                                                        resultMeta
                                                                .getTypeNameAsTypeParameter(),
                                                        BasicIterationHandler.class
                                                                .getName(),
                                                        dataType.getWrappedType()
                                                                .getTypeNameAsTypeParameter(),
                                                        dataType.getTypeName(),
                                                        callbackParamName);
                                                return null;
                                            }

                                        }, null);

                                return null;
                            }

                            @Override
                            public Void visitDomainType(DomainType dataType,
                                    Void p) throws RuntimeException {
                                iprint("%1$s<%2$s> __command = new %1$s<%2$s>(__query, new %3$s<%2$s, %4$s>(%5$s, %6$s));%n",
                                        commandClassName,
                                        resultMeta.getTypeNameAsTypeParameter(),
                                        DomainIterationHandler.class.getName(),
                                        dataType.getTypeNameAsTypeParameter(),
                                        dataType.getInstantiationCommand(),
                                        callbackParamName);
                                return null;
                            }

                            @Override
                            public Void visitMapType(MapType dataType, Void p)
                                    throws RuntimeException {
                                MapKeyNamingType namingType = m
                                        .getMapKeyNamingType();
                                iprint("%1$s<%2$s> __command = new %1$s<%2$s>(__query, new %3$s<%2$s>(%4$s.%5$s, %6$s));%n",
                                        commandClassName, resultMeta
                                                .getTypeNameAsTypeParameter(),
                                        MapIterationHandler.class.getName(),
                                        namingType.getDeclaringClass()
                                                .getName(), namingType.name(),
                                        callbackParamName);
                                return null;
                            }

                            @Override
                            public Void visitEntityType(EntityType dataType,
                                    Void p) throws RuntimeException {
                                iprint("%1$s<%2$s> __command = new %1$s<%2$s>(__query, new %3$s<%2$s, %4$s>(%5$s.getSingletonInternal(), %6$s));%n",
                                        commandClassName,
                                        resultMeta.getTypeNameAsTypeParameter(),
                                        EntityIterationHandler.class.getName(),
                                        dataType.getTypeName(),
                                        dataType.getMetaTypeName(),
                                        callbackParamName);
                                return null;
                            }

                        }, null);
                if ("void".equals(resultMeta.getTypeName())) {
                    iprint("__command.execute();%n");
                    iprint("__query.complete();%n");
                    iprint("exiting(\"%1$s\", \"%2$s\", null);%n",
                            qualifiedName, m.getName());
                } else {
                    iprint("%1$s __result = __command.execute();%n",
                            resultMeta.getTypeName());
                    iprint("__query.complete();%n");
                    iprint("exiting(\"%1$s\", \"%2$s\", __result);%n",
                            qualifiedName, m.getName());
                    iprint("return __result;%n");
                }
            } else {
                m.getReturnMeta()
                        .getDataType()
                        .accept(new SimpleDataTypeVisitor<Void, Void, RuntimeException>() {

                            @Override
                            public Void visitBasicType(
                                    final BasicType basicType, Void p)
                                    throws RuntimeException {
                                basicType
                                        .getWrapperType()
                                        .accept(new SimpleDataTypeVisitor<Void, Void, RuntimeException>() {

                                            @Override
                                            public Void visitEnumWrapperType(
                                                    EnumWrapperType dataType,
                                                    Void p)
                                                    throws RuntimeException {
                                                iprint("%1$s<%2$s> __command = new %1$s<%2$s>(__query, new %3$s<%2$s>(new %4$s(%5$s.class), false));%n",
                                                        commandClassName,
                                                        dataType.getWrappedType()
                                                                .getTypeNameAsTypeParameter(),
                                                        BasicSingleResultHandler.class
                                                                .getName(),
                                                        dataType.getTypeName(),
                                                        dataType.getWrappedType()
                                                                .getQualifiedName());
                                                return null;
                                            }

                                            @Override
                                            public Void visitWrapperType(
                                                    WrapperType dataType, Void p)
                                                    throws RuntimeException {
                                                iprint("%1$s<%2$s> __command = new %1$s<%2$s>(__query, new %3$s<%2$s>(new %4$s(), %5$s));%n",
                                                        commandClassName,
                                                        dataType.getWrappedType()
                                                                .getTypeNameAsTypeParameter(),
                                                        BasicSingleResultHandler.class
                                                                .getName(),
                                                        dataType.getTypeName(),
                                                        basicType.isPrimitive());
                                                return null;
                                            }

                                        }, null);

                                return null;
                            }

                            @Override
                            public Void visitDomainType(DomainType dataType,
                                    Void p) throws RuntimeException {
                                iprint("%1$s<%2$s> __command = new %1$s<%2$s>(__query, new %3$s<%2$s>(%4$s));%n",
                                        commandClassName, dataType
                                                .getTypeNameAsTypeParameter(),
                                        DomainSingleResultHandler.class
                                                .getName(), dataType
                                                .getInstantiationCommand());
                                return null;
                            }

                            @Override
                            public Void visitMapType(MapType dataType, Void p)
                                    throws RuntimeException {
                                MapKeyNamingType namingType = m
                                        .getMapKeyNamingType();
                                iprint("%1$s<%2$s> __command = new %1$s<%2$s>(__query, new %3$s(%4$s.%5$s));%n",
                                        commandClassName, dataType
                                                .getTypeNameAsTypeParameter(),
                                        MapSingleResultHandler.class.getName(),
                                        namingType.getDeclaringClass()
                                                .getName(), namingType.name());
                                return null;
                            }

                            @Override
                            public Void visitEntityType(EntityType dataType,
                                    Void p) throws RuntimeException {
                                iprint("%1$s<%2$s> __command = new %1$s<%2$s>(__query, new %3$s<%2$s>(%4$s.getSingletonInternal()));%n",
                                        commandClassName, dataType
                                                .getTypeName(),
                                        EntitySingleResultHandler.class
                                                .getName(), dataType
                                                .getMetaTypeName());
                                return null;
                            }

                            @Override
                            public Void visitIterableType(
                                    final IterableType iterableType, Void p)
                                    throws RuntimeException {
                                iterableType
                                        .getElementType()
                                        .accept(new SimpleDataTypeVisitor<Void, Void, RuntimeException>() {

                                            @Override
                                            public Void visitBasicType(
                                                    BasicType dataType, Void p)
                                                    throws RuntimeException {
                                                dataType.getWrapperType()
                                                        .accept(new SimpleDataTypeVisitor<Void, Void, RuntimeException>() {

                                                            @Override
                                                            public Void visitEnumWrapperType(
                                                                    EnumWrapperType dataType,
                                                                    Void p)
                                                                    throws RuntimeException {
                                                                iprint("%1$s<%2$s> __command = new %1$s<%2$s>(__query, new %3$s<%4$s>(new %5$s(%6$s.class)));%n",
                                                                        commandClassName,
                                                                        iterableType
                                                                                .getTypeName(),
                                                                        BasicResultListHandler.class
                                                                                .getName(),
                                                                        dataType.getWrappedType()
                                                                                .getTypeNameAsTypeParameter(),
                                                                        dataType.getTypeName(),
                                                                        dataType.getWrappedType()
                                                                                .getQualifiedName());
                                                                return null;
                                                            }

                                                            @Override
                                                            public Void visitWrapperType(
                                                                    WrapperType dataType,
                                                                    Void p)
                                                                    throws RuntimeException {
                                                                iprint("%1$s<%2$s> __command = new %1$s<%2$s>(__query, new %3$s<%4$s>(new %5$s()));%n",
                                                                        commandClassName,
                                                                        iterableType
                                                                                .getTypeNameAsTypeParameter(),
                                                                        BasicResultListHandler.class
                                                                                .getName(),
                                                                        dataType.getWrappedType()
                                                                                .getTypeNameAsTypeParameter(),
                                                                        dataType.getTypeName());
                                                                return null;
                                                            }

                                                        }, null);

                                                return null;
                                            }

                                            @Override
                                            public Void visitDomainType(
                                                    DomainType dataType, Void p)
                                                    throws RuntimeException {
                                                iprint("%1$s<%2$s> __command = new %1$s<%2$s>(__query, new %3$s<%4$s>(%5$s));%n",
                                                        commandClassName,
                                                        iterableType
                                                                .getTypeName(),
                                                        DomainResultListHandler.class
                                                                .getName(),
                                                        dataType.getTypeNameAsTypeParameter(),
                                                        dataType.getInstantiationCommand());
                                                return null;
                                            }

                                            @Override
                                            public Void visitMapType(
                                                    MapType dataType, Void p)
                                                    throws RuntimeException {
                                                MapKeyNamingType namingType = m
                                                        .getMapKeyNamingType();
                                                iprint("%1$s<%2$s> __command = new %1$s<%2$s>(__query, new %3$s(%4$s.%5$s));%n",
                                                        commandClassName,
                                                        iterableType
                                                                .getTypeName(),
                                                        MapResultListHandler.class
                                                                .getName(),
                                                        namingType
                                                                .getDeclaringClass()
                                                                .getName(),
                                                        namingType.name());
                                                return null;
                                            }

                                            @Override
                                            public Void visitEntityType(
                                                    EntityType dataType, Void p)
                                                    throws RuntimeException {
                                                iprint("%1$s<%2$s> __command = new %1$s<%2$s>(__query, new %3$s<%4$s>(%5$s.getSingletonInternal()));%n",
                                                        commandClassName,
                                                        iterableType
                                                                .getTypeName(),
                                                        EntityResultListHandler.class
                                                                .getName(),
                                                        dataType.getTypeName(),
                                                        dataType.getMetaTypeName());
                                                return null;
                                            }

                                        }, null);
                                return null;
                            }

                        }, null);
                iprint("%1$s __result = __command.execute();%n",
                        resultMeta.getTypeName());
                iprint("__query.complete();%n");
                iprint("exiting(\"%1$s\", \"%2$s\", __result);%n",
                        qualifiedName, m.getName());
                iprint("return __result;%n");
            }

            printThrowingStatements(m);
            return null;
        }

        @Override
        public Void visitSqlFileScriptQueryMeta(SqlFileScriptQueryMeta m,
                Integer p) {
            printEnteringStatements(m);
            printPrerequisiteStatements(m);

            iprint("%1$s __query = new %1$s();%n", m.getQueryClass().getName());
            if (m.isTrigger()) {
                iprint("__query.setMethod(__method%1$s);%n", p);
            }
            iprint("__query.setConfig(config);%n");
            iprint("__query.setScriptFilePath(\"%1$s\");%n",
                    ScriptFileUtil.buildPath(daoMeta.getDaoElement()
                            .getQualifiedName().toString(), m.getName()));
            iprint("__query.setCallerClassName(\"%1$s\");%n", qualifiedName);
            iprint("__query.setCallerMethodName(\"%1$s\");%n", m.getName());
            iprint("__query.setBlockDelimiter(\"%1$s\");%n",
                    m.getBlockDelimiter());
            iprint("__query.setHaltOnError(%1$s);%n", m.getHaltOnError());
            iprint("__query.prepare();%n");
            iprint("%1$s __command = new %1$s(__query);%n", m.getCommandClass()
                    .getName());
            iprint("__command.execute();%n");
            iprint("__query.complete();%n");
            iprint("exiting(\"%1$s\", \"%2$s\", null);%n", qualifiedName,
                    m.getName());

            printThrowingStatements(m);
            return null;
        }

        @Override
        public Void visitAutoModifyQueryMeta(AutoModifyQueryMeta m, Integer p) {
            printEnteringStatements(m);
            printPrerequisiteStatements(m);

            iprint("%1$s<%2$s> __query = new %1$s<%2$s>(%3$s.getSingletonInternal());%n",
                    m.getQueryClass().getName(), m.getEntityType()
                            .getTypeNameAsTypeParameter(), m.getEntityType()
                            .getMetaTypeNameAsTypeParameter());
            if (m.isTrigger()) {
                iprint("__query.setMethod(__method%1$s);%n", p);
            }
            iprint("__query.setConfig(config);%n");
            iprint("__query.setEntity(%1$s);%n", m.getEntityParameterName());
            iprint("__query.setCallerClassName(\"%1$s\");%n", qualifiedName);
            iprint("__query.setCallerMethodName(\"%1$s\");%n", m.getName());
            iprint("__query.setQueryTimeout(%1$s);%n", m.getQueryTimeout());

            Boolean excludeNull = m.getExcludeNull();
            if (excludeNull != null) {
                iprint("__query.setNullExcluded(%1$s);%n", excludeNull);
            }

            Boolean includeVersion = m.getIncludeVersion();
            if (includeVersion != null) {
                iprint("__query.setVersionIncluded(%1$s);%n", includeVersion);
            }

            Boolean ignoreVersion = m.getIgnoreVersion();
            if (ignoreVersion != null) {
                iprint("__query.setVersionIgnored(%1$s);%n", ignoreVersion);
            }

            List<String> include = m.getInclude();
            if (include != null) {
                iprint("__query.setIncludedPropertyNames(%1$s);%n",
                        toCSVFormat(include));
            }

            List<String> exclude = m.getExclude();
            if (exclude != null) {
                iprint("__query.setExcludedPropertyNames(%1$s);%n",
                        toCSVFormat(m.getExclude()));
            }

            Boolean includeUnchanged = m.getIncludeUnchanged();
            if (includeUnchanged != null) {
                iprint("__query.setUnchangedPropertyIncluded(%1$s);%n",
                        includeUnchanged);
            }

            Boolean suppressOptimisticLockException = m
                    .getSuppressOptimisticLockException();
            if (suppressOptimisticLockException != null) {
                iprint("__query.setOptimisticLockExceptionSuppressed(%1$s);%n",
                        suppressOptimisticLockException);
            }

            iprint("__query.prepare();%n");
            iprint("%1$s __command = new %1$s(__query);%n", m.getCommandClass()
                    .getName());

            EntityType entityType = m.getEntityType();
            if (entityType != null && entityType.isImmutable()) {
                iprint("int __count = __command.execute();%n");
                iprint("__query.complete();%n");
                iprint("%1$s __result = new %1$s(__count, __query.getEntity());%n",
                        m.getReturnMeta().getTypeName());
            } else {
                iprint("%1$s __result = __command.execute();%n", m
                        .getReturnMeta().getTypeName());
                iprint("__query.complete();%n");
            }

            iprint("exiting(\"%1$s\", \"%2$s\", __result);%n", qualifiedName,
                    m.getName());
            iprint("return __result;%n");

            printThrowingStatements(m);
            return null;
        }

        @Override
        public Void visitSqlFileModifyQueryMeta(SqlFileModifyQueryMeta m,
                Integer p) {
            printEnteringStatements(m);
            printPrerequisiteStatements(m);

            iprint("%1$s __query = new %1$s();%n", m.getQueryClass().getName());
            if (m.isTrigger()) {
                iprint("__query.setMethod(__method%1$s);%n", p);
            }
            iprint("__query.setConfig(config);%n");
            iprint("__query.setSqlFilePath(\"%1$s\");%n",
                    SqlFileUtil.buildPath(daoMeta.getDaoElement()
                            .getQualifiedName().toString(), m.getName()));
            for (Iterator<QueryParameterMeta> it = m.getParameterMetas()
                    .iterator(); it.hasNext();) {
                QueryParameterMeta parameterMeta = it.next();
                if (parameterMeta.isBindable()) {
                    iprint("__query.addParameter(\"%1$s\", %2$s.class, %1$s);%n",
                            parameterMeta.getName(),
                            parameterMeta.getQualifiedName());
                }
            }
            iprint("__query.setCallerClassName(\"%1$s\");%n", qualifiedName);
            iprint("__query.setCallerMethodName(\"%1$s\");%n", m.getName());
            iprint("__query.setQueryTimeout(%1$s);%n", m.getQueryTimeout());

            if (m.getEntityParameterName() != null && m.getEntityType() != null) {
                iprint("__query.setEntityAndEntityType(\"%1$s\", %2$s, %3$s.getSingletonInternal());%n",
                        m.getEntityParameterName(), m.getEntityParameterName(),
                        m.getEntityType().getMetaTypeNameAsTypeParameter());
            }

            Boolean includeVersion = m.getIncludeVersion();
            if (includeVersion != null) {
                iprint("__query.setVersionIncluded(%1$s);%n", includeVersion);
            }

            Boolean ignoreVersion = m.getIgnoreVersion();
            if (ignoreVersion != null) {
                iprint("__query.setVersionIgnored(%1$s);%n", ignoreVersion);
            }

            Boolean suppressOptimisticLockException = m
                    .getSuppressOptimisticLockException();
            if (suppressOptimisticLockException != null) {
                iprint("__query.setOptimisticLockExceptionSuppressed(%1$s);%n",
                        suppressOptimisticLockException);
            }

            iprint("__query.prepare();%n");
            iprint("%1$s __command = new %1$s(__query);%n", m.getCommandClass()
                    .getName());

            EntityType entityType = m.getEntityType();
            if (entityType != null && entityType.isImmutable()) {
                iprint("int __count = __command.execute();%n");
                iprint("__query.complete();%n");
                iprint("%1$s __result = new %1$s(__count, __query.getEntity(%2$s.class));%n",
                        m.getReturnMeta().getTypeName(),
                        entityType.getTypeNameAsTypeParameter());
            } else {
                iprint("%1$s __result = __command.execute();%n", m
                        .getReturnMeta().getTypeName());
                iprint("__query.complete();%n");
            }

            iprint("exiting(\"%1$s\", \"%2$s\", __result);%n", qualifiedName,
                    m.getName());
            iprint("return __result;%n");

            printThrowingStatements(m);
            return null;
        }

        @Override
        public Void visitAutoBatchModifyQueryMeta(AutoBatchModifyQueryMeta m,
                Integer p) {
            printEnteringStatements(m);
            printPrerequisiteStatements(m);

            iprint("%1$s<%2$s> __query = new %1$s<%2$s>(%3$s.getSingletonInternal());%n",
                    m.getQueryClass().getName(), m.getEntityType()
                            .getTypeNameAsTypeParameter(), m.getEntityType()
                            .getMetaTypeNameAsTypeParameter());
            if (m.isTrigger()) {
                iprint("__query.setMethod(__method%1$s);%n", p);
            }
            iprint("__query.setConfig(config);%n");
            iprint("__query.setEntities(%1$s);%n", m.getEntitiesParameterName());
            iprint("__query.setCallerClassName(\"%1$s\");%n", qualifiedName);
            iprint("__query.setCallerMethodName(\"%1$s\");%n", m.getName());
            iprint("__query.setQueryTimeout(%1$s);%n", m.getQueryTimeout());
            iprint("__query.setBatchSize(%1$s);%n", m.getBatchSize());

            Boolean includeVersion = m.getIncludeVersion();
            if (includeVersion != null) {
                iprint("__query.setVersionIncluded(%1$s);%n", includeVersion);
            }

            Boolean ignoreVersion = m.getIgnoreVersion();
            if (ignoreVersion != null) {
                iprint("__query.setVersionIgnored(%1$s);%n", ignoreVersion);
            }

            List<String> include = m.getInclude();
            if (include != null) {
                iprint("__query.setIncludedPropertyNames(%1$s);%n",
                        toCSVFormat(include));
            }

            List<String> exclude = m.getExclude();
            if (exclude != null) {
                iprint("__query.setExcludedPropertyNames(%1$s);%n",
                        toCSVFormat(exclude));
            }

            Boolean suppressOptimisticLockException = m
                    .getSuppressOptimisticLockException();
            if (suppressOptimisticLockException != null) {
                iprint("__query.setOptimisticLockExceptionSuppressed(%1$s);%n",
                        suppressOptimisticLockException);
            }

            iprint("__query.prepare();%n");
            iprint("%1$s __command = new %1$s(__query);%n", m.getCommandClass()
                    .getName());

            EntityType entityType = m.getEntityType();
            if (entityType != null && entityType.isImmutable()) {
                iprint("int[] __counts = __command.execute();%n");
                iprint("__query.complete();%n");
                iprint("%1$s __result = new %1$s(__counts, __query.getEntities());%n",
                        m.getReturnMeta().getTypeName());
            } else {
                iprint("%1$s __result = __command.execute();%n", m
                        .getReturnMeta().getTypeName());
                iprint("__query.complete();%n");
            }

            iprint("exiting(\"%1$s\", \"%2$s\", __result);%n", qualifiedName,
                    m.getName());
            iprint("return __result;%n");

            printThrowingStatements(m);
            return null;
        }

        @Override
        public Void visitSqlFileBatchModifyQueryMeta(
                SqlFileBatchModifyQueryMeta m, Integer p) {
            printEnteringStatements(m);
            printPrerequisiteStatements(m);

            iprint("%1$s<%2$s> __query = new %1$s<%2$s>(%3$s.class);%n", m
                    .getQueryClass().getName(), m.getElementType()
                    .getTypeNameAsTypeParameter(), m.getElementType()
                    .getQualifiedName());
            if (m.isTrigger()) {
                iprint("__query.setMethod(__method%1$s);%n", p);
            }
            iprint("__query.setConfig(config);%n");
            iprint("__query.setElements(%1$s);%n", m.getElementsParameterName());
            iprint("__query.setSqlFilePath(\"%1$s\");%n",
                    SqlFileUtil.buildPath(daoMeta.getDaoElement()
                            .getQualifiedName().toString(), m.getName()));
            iprint("__query.setParameterName(\"%1$s\");%n",
                    m.getElementsParameterName());
            iprint("__query.setCallerClassName(\"%1$s\");%n", qualifiedName);
            iprint("__query.setCallerMethodName(\"%1$s\");%n", m.getName());
            iprint("__query.setQueryTimeout(%1$s);%n", m.getQueryTimeout());
            iprint("__query.setBatchSize(%1$s);%n", m.getBatchSize());

            if (m.getEntityType() != null) {
                iprint("__query.setEntityType(%1$s.getSingletonInternal());%n",
                        m.getEntityType().getMetaTypeNameAsTypeParameter());
            }

            Boolean includeVersion = m.getIncludeVersion();
            if (includeVersion != null) {
                iprint("__query.setVersionIncluded(%1$s);%n", includeVersion);
            }

            Boolean ignoreVersion = m.getIgnoreVersion();
            if (ignoreVersion != null) {
                iprint("__query.setVersionIgnored(%1$s);%n", ignoreVersion);
            }

            Boolean suppressOptimisticLockException = m
                    .getSuppressOptimisticLockException();
            if (suppressOptimisticLockException != null) {
                iprint("__query.setOptimisticLockExceptionSuppressed(%1$s);%n",
                        suppressOptimisticLockException);
            }

            iprint("__query.prepare();%n");
            iprint("%1$s __command = new %1$s(__query);%n", m.getCommandClass()
                    .getName());

            EntityType entityType = m.getEntityType();
            if (entityType != null && entityType.isImmutable()) {
                iprint("int[] __counts = __command.execute();%n");
                iprint("__query.complete();%n");
                iprint("%1$s __result = new %1$s(__counts, __query.getEntities());%n",
                        m.getReturnMeta().getTypeName());
            } else {
                iprint("%1$s __result = __command.execute();%n", m
                        .getReturnMeta().getTypeName());
                iprint("__query.complete();%n");
            }

            iprint("exiting(\"%1$s\", \"%2$s\", __result);%n", qualifiedName,
                    m.getName());
            iprint("return __result;%n");

            printThrowingStatements(m);
            return null;
        }

        @Override
        public Void visitAutoFunctionQueryMeta(AutoFunctionQueryMeta m,
                Integer p) {
            printEnteringStatements(m);
            printPrerequisiteStatements(m);

            QueryReturnMeta resultMeta = m.getReturnMeta();
            iprint("%1$s<%2$s> __query = new %1$s<%2$s>();%n", m
                    .getQueryClass().getName(),
                    resultMeta.getTypeNameAsTypeParameter());
            if (m.isTrigger()) {
                iprint("__query.setMethod(__method%1$s);%n", p);
            }
            iprint("__query.setConfig(config);%n");
            iprint("__query.setFunctionName(\"%1$s\");%n", m.getFunctionName());
            CallableSqlParameterStatementGenerator parameterGenerator = new CallableSqlParameterStatementGenerator();
            m.getResultParameterMeta().accept(parameterGenerator, m);
            for (CallableSqlParameterMeta parameterMeta : m
                    .getCallableSqlParameterMetas()) {
                parameterMeta.accept(parameterGenerator, m);
            }
            iprint("__query.setCallerClassName(\"%1$s\");%n", qualifiedName);
            iprint("__query.setCallerMethodName(\"%1$s\");%n", m.getName());
            iprint("__query.setQueryTimeout(%1$s);%n", m.getQueryTimeout());
            iprint("__query.prepare();%n");
            iprint("%1$s<%2$s> __command = new %1$s<%2$s>(__query);%n", m
                    .getCommandClass().getName(),
                    resultMeta.getTypeNameAsTypeParameter());
            iprint("%1$s __result = __command.execute();%n",
                    resultMeta.getTypeName());
            iprint("__query.complete();%n");
            iprint("exiting(\"%1$s\", \"%2$s\", __result);%n", qualifiedName,
                    m.getName());
            iprint("return __result;%n");

            printThrowingStatements(m);
            return null;
        }

        @Override
        public Void visitAutoProcedureQueryMeta(AutoProcedureQueryMeta m,
                Integer p) {
            printEnteringStatements(m);
            printPrerequisiteStatements(m);

            iprint("%1$s __query = new %1$s();%n", m.getQueryClass().getName());
            if (m.isTrigger()) {
                iprint("__query.setMethod(__method%1$s);%n", p);
            }
            iprint("__query.setConfig(config);%n");
            iprint("__query.setProcedureName(\"%1$s\");%n",
                    m.getProcedureName());
            CallableSqlParameterStatementGenerator parameterGenerator = new CallableSqlParameterStatementGenerator();
            for (CallableSqlParameterMeta parameterMeta : m
                    .getCallableSqlParameterMetas()) {
                parameterMeta.accept(parameterGenerator, m);
            }
            iprint("__query.setCallerClassName(\"%1$s\");%n", qualifiedName);
            iprint("__query.setCallerMethodName(\"%1$s\");%n", m.getName());
            iprint("__query.setQueryTimeout(%1$s);%n", m.getQueryTimeout());
            iprint("__query.prepare();%n");
            iprint("%1$s __command = new %1$s(__query);%n", m.getCommandClass()
                    .getName());
            iprint("__command.execute();%n");
            iprint("__query.complete();%n");
            iprint("exiting(\"%1$s\", \"%2$s\", null);%n", qualifiedName,
                    m.getName());

            printThrowingStatements(m);
            return null;
        }

        @Override
        public Void visitAbstractCreateQueryMeta(AbstractCreateQueryMeta m,
                Integer p) {
            printEnteringStatements(m);
            printPrerequisiteStatements(m);

            QueryReturnMeta resultMeta = m.getReturnMeta();
            iprint("%1$s __query = new %1$s();%n", m.getQueryClass().getName(),
                    resultMeta.getTypeName());
            if (m.isTrigger()) {
                iprint("__query.setMethod(__method%1$s);%n", p);
            }
            iprint("__query.setConfig(config);%n");
            iprint("__query.setCallerClassName(\"%1$s\");%n", qualifiedName);
            iprint("__query.setCallerMethodName(\"%1$s\");%n", m.getName());
            iprint("__query.prepare();%n");
            iprint("%1$s<%2$s> __command = new %1$s<%2$s>(__query);%n", m
                    .getCommandClass().getName(), resultMeta.getTypeName());
            iprint("%1$s __result = __command.execute();%n",
                    resultMeta.getTypeName());
            iprint("__query.complete();%n");
            iprint("exiting(\"%1$s\", \"%2$s\", __result);%n", qualifiedName,
                    m.getName());
            iprint("return __result;%n");

            printThrowingStatements(m);
            return null;
        }

        @Override
        public Void visitArrayCreateQueryMeta(ArrayCreateQueryMeta m, Integer p) {
            printArrayCreateEnteringStatements(m);
            printPrerequisiteStatements(m);

            QueryReturnMeta resultMeta = m.getReturnMeta();
            iprint("%1$s __query = new %1$s();%n", m.getQueryClass().getName());
            if (m.isTrigger()) {
                iprint("__query.setMethod(__method%1$s);%n", p);
            }
            iprint("__query.setConfig(config);%n");
            iprint("__query.setCallerClassName(\"%1$s\");%n", qualifiedName);
            iprint("__query.setCallerMethodName(\"%1$s\");%n", m.getName());
            iprint("__query.setTypeName(\"%1$s\");%n", m.getArrayTypeName());
            iprint("__query.setElements(%1$s);%n", m.getParameterName());
            iprint("__query.prepare();%n");
            iprint("%1$s<%2$s> __command = new %1$s<%2$s>(__query);%n", m
                    .getCommandClass().getName(),
                    resultMeta.getTypeNameAsTypeParameter());
            iprint("%1$s __result = __command.execute();%n",
                    resultMeta.getTypeName());
            iprint("__query.complete();%n");
            iprint("exiting(\"%1$s\", \"%2$s\", __result);%n", qualifiedName,
                    m.getName());
            iprint("return __result;%n");

            printThrowingStatements(m);
            return null;
        }

        @Override
        public Void visitDelegateQueryMeta(DelegateQueryMeta m, Integer p) {
            printEnteringStatements(m);

            iprint("%1$s __delegate = new %1$s(config", m.getTo());
            if (m.isDaoAware()) {
                print(", this);%n");
            } else {
                print(");%n");
            }
            QueryReturnMeta resultMeta = m.getReturnMeta();
            if ("void".equals(resultMeta.getTypeName())) {
                iprint("Object __result = null;%n");
                iprint("");
            } else {
                iprint("%1$s __result = ", resultMeta.getTypeName());
            }
            print("__delegate.%1$s(", m.getName());
            for (Iterator<QueryParameterMeta> it = m.getParameterMetas()
                    .iterator(); it.hasNext();) {
                QueryParameterMeta parameterMeta = it.next();
                print("%1$s", parameterMeta.getName());
                if (it.hasNext()) {
                    print(", ");
                }
            }
            print(");%n");
            iprint("exiting(\"%1$s\", \"%2$s\", __result);%n", qualifiedName,
                    m.getName());
            if (!"void".equals(resultMeta.getTypeName())) {
                iprint("return __result;%n");
            }

            printThrowingStatements(m);
            return null;
        }

        protected void printEnteringStatements(QueryMeta m) {
            iprint("entering(\"%1$s\", \"%2$s\"", qualifiedName, m.getName());
            for (Iterator<QueryParameterMeta> it = m.getParameterMetas()
                    .iterator(); it.hasNext();) {
                QueryParameterMeta parameterMeta = it.next();
                print(", %1$s", parameterMeta.getName());
            }
            print(");%n");
            iprint("try {%n");
            indent();
        }

        protected void printArrayCreateEnteringStatements(ArrayCreateQueryMeta m) {
            iprint("entering(\"%1$s\", \"%2$s\", (Object)%3$s);%n",
                    qualifiedName, m.getName(), m.getParameterName());
            iprint("try {%n");
            indent();
        }

        protected void printThrowingStatements(QueryMeta m) {
            unindent();
            iprint("} catch (%1$s __e) {%n", RuntimeException.class.getName());
            indent();
            iprint("throwing(\"%1$s\", \"%2$s\", __e);%n", qualifiedName,
                    m.getName());
            iprint("throw __e;%n");
            unindent();
            iprint("}%n");
        }

        protected void printPrerequisiteStatements(QueryMeta m) {
            for (Iterator<QueryParameterMeta> it = m.getParameterMetas()
                    .iterator(); it.hasNext();) {
                QueryParameterMeta parameterMeta = it.next();
                if (parameterMeta.isNullable()) {
                    continue;
                }
                String paramName = parameterMeta.getName();
                iprint("if (%1$s == null) {%n", paramName);
                iprint("    throw new %1$s(\"%2$s\");%n",
                        DomaNullPointerException.class.getName(), paramName);
                iprint("}%n");
            }
        }

    }

    protected class CallableSqlParameterStatementGenerator implements
            CallableSqlParameterMetaVisitor<Void, AutoModuleQueryMeta> {

        @Override
        public Void visitBasicListParameterMeta(final BasicListParameterMeta m,
                AutoModuleQueryMeta p) {
            BasicType basicType = m.getBasicType();
            basicType.getWrapperType().accept(
                    new SimpleDataTypeVisitor<Void, Void, RuntimeException>() {

                        @Override
                        public Void visitEnumWrapperType(
                                EnumWrapperType dataType, Void p)
                                throws RuntimeException {
                            iprint("__query.addParameter(new %1$s<%2$s>(new %3$s(%4$s.class), %5$s, \"%5$s\"));%n",
                                    BasicListParameter.class.getName(),
                                    dataType.getWrappedType()
                                            .getTypeNameAsTypeParameter(),
                                    dataType.getTypeName(), dataType
                                            .getWrappedType()
                                            .getQualifiedName(), m.getName());
                            return null;
                        }

                        @Override
                        public Void visitWrapperType(WrapperType dataType,
                                Void p) throws RuntimeException {
                            iprint("__query.addParameter(new %1$s<%2$s>(new %3$s(), %4$s, \"%4$s\"));%n",
                                    BasicListParameter.class.getName(),
                                    dataType.getWrappedType()
                                            .getTypeNameAsTypeParameter(),
                                    dataType.getTypeName(), m.getName());
                            return null;
                        }

                    }, null);
            return null;
        }

        @Override
        public Void visitDomainListParameterMeta(DomainListParameterMeta m,
                AutoModuleQueryMeta p) {
            DomainType domainType = m.getDomainType();
            BasicType basicType = domainType.getBasicType();
            iprint("__query.addParameter(new %1$s<%2$s, %3$s>(%4$s, %5$s, \"%5$s\"));%n",
                    DomainListParameter.class.getName(),
                    basicType.getTypeName(), domainType.getTypeName(),
                    domainType.getInstantiationCommand(), m.getName());
            return null;
        }

        @Override
        public Void visitEntityListParameterMeta(EntityListParameterMeta m,
                AutoModuleQueryMeta p) {
            EntityType entityType = m.getEntityType();
            iprint("__query.addParameter(new %1$s<%2$s>(%3$s.getSingletonInternal(), %4$s, \"%4$s\", %5$s));%n",
                    EntityListParameter.class.getName(),
                    entityType.getTypeName(), entityType.getMetaTypeName(),
                    m.getName(), m.getEnsureResultMapping());
            return null;
        }

        @Override
        public Void visitMapListParameterMeta(MapListParameterMeta m,
                AutoModuleQueryMeta p) {
            MapKeyNamingType namingType = p.getMapKeyNamingType();
            iprint("__query.addParameter(new %1$s(%2$s.%3$s, %4$s, \"%4$s\"));%n",
                    MapListParameter.class.getName(), namingType
                            .getDeclaringClass().getName(), namingType.name(),
                    m.getName());
            return null;
        }

        @Override
        public Void visitBasicInOutParameterMeta(
                final BasicInOutParameterMeta m, AutoModuleQueryMeta p) {
            BasicType basicType = m.getBasicType();
            basicType.getWrapperType().accept(
                    new SimpleDataTypeVisitor<Void, Void, RuntimeException>() {

                        @Override
                        public Void visitEnumWrapperType(
                                EnumWrapperType dataType, Void p)
                                throws RuntimeException {
                            iprint("__query.addParameter(new %1$s<%2$s>(new %3$s(%4$s.class), %5$s));%n",
                                    BasicInOutParameter.class.getName(),
                                    dataType.getWrappedType()
                                            .getTypeNameAsTypeParameter(),
                                    dataType.getTypeName(), dataType
                                            .getWrappedType()
                                            .getQualifiedName(), m.getName());
                            return null;
                        }

                        @Override
                        public Void visitWrapperType(WrapperType dataType,
                                Void p) throws RuntimeException {
                            iprint("__query.addParameter(new %1$s<%2$s>(new %3$s(), %4$s));%n",
                                    BasicInOutParameter.class.getName(),
                                    dataType.getWrappedType()
                                            .getTypeNameAsTypeParameter(),
                                    dataType.getTypeName(), m.getName());
                            return null;
                        }

                    }, null);
            return null;
        }

        @Override
        public Void visitDomainInOutParameterMeta(DomainInOutParameterMeta m,
                AutoModuleQueryMeta p) {
            DomainType domainType = m.getDomainType();
            BasicType basicType = domainType.getBasicType();
            iprint("__query.addParameter(new %1$s<%2$s, %3$s>(%4$s, %5$s));%n",
                    DomainInOutParameter.class.getName(),
                    basicType.getTypeNameAsTypeParameter(),
                    domainType.getTypeName(),
                    domainType.getInstantiationCommand(), m.getName());
            return null;
        }

        @Override
        public Void visitBasicOutParameterMeta(final BasicOutParameterMeta m,
                AutoModuleQueryMeta p) {
            BasicType basicType = m.getBasicType();
            basicType.getWrapperType().accept(
                    new SimpleDataTypeVisitor<Void, Void, RuntimeException>() {

                        @Override
                        public Void visitEnumWrapperType(
                                EnumWrapperType dataType, Void p)
                                throws RuntimeException {
                            iprint("__query.addParameter(new %1$s<%2$s>(new %3$s(%4$s.class), %5$s));%n",
                                    BasicOutParameter.class.getName(), dataType
                                            .getWrappedType()
                                            .getTypeNameAsTypeParameter(),
                                    dataType.getTypeName(), dataType
                                            .getWrappedType()
                                            .getQualifiedName(), m.getName());
                            return null;
                        }

                        @Override
                        public Void visitWrapperType(WrapperType dataType,
                                Void p) throws RuntimeException {
                            iprint("__query.addParameter(new %1$s<%2$s>(new %3$s(), %4$s));%n",
                                    BasicOutParameter.class.getName(), dataType
                                            .getWrappedType()
                                            .getTypeNameAsTypeParameter(),
                                    dataType.getTypeName(), m.getName());
                            return null;
                        }

                    }, null);

            return null;
        }

        @Override
        public Void visitDomainOutParameterMeta(DomainOutParameterMeta m,
                AutoModuleQueryMeta p) {
            DomainType domainType = m.getDomainType();
            BasicType basicType = domainType.getBasicType();
            iprint("__query.addParameter(new %1$s<%2$s, %3$s>(%4$s, %5$s));%n",
                    DomainOutParameter.class.getName(),
                    basicType.getTypeName(), domainType.getTypeName(),
                    domainType.getInstantiationCommand(), m.getName());
            return null;
        }

        @Override
        public Void visitBasicInParameterMeta(final BasicInParameterMeta m,
                AutoModuleQueryMeta p) {
            BasicType basicType = m.getBasicType();
            basicType.getWrapperType().accept(
                    new SimpleDataTypeVisitor<Void, Void, RuntimeException>() {

                        @Override
                        public Void visitEnumWrapperType(
                                EnumWrapperType dataType, Void p)
                                throws RuntimeException {
                            iprint("__query.addParameter(new %1$s(new %2$s(%3$s.class, %4$s)));%n",
                                    BasicInParameter.class.getName(), dataType
                                            .getTypeName(), dataType
                                            .getWrappedType().getTypeName(), m
                                            .getName());
                            return null;
                        }

                        @Override
                        public Void visitWrapperType(WrapperType dataType,
                                Void p) throws RuntimeException {
                            iprint("__query.addParameter(new %1$s(new %2$s(%3$s)));%n",
                                    BasicInParameter.class.getName(),
                                    dataType.getTypeName(), m.getName());
                            return null;
                        }

                    }, null);

            return null;
        }

        @Override
        public Void visitDomainInParameterMeta(DomainInParameterMeta m,
                AutoModuleQueryMeta p) {
            DomainType domainType = m.getDomainType();
            BasicType basicType = domainType.getBasicType();
            iprint("__query.addParameter(new %1$s<%2$s, %3$s>(%4$s, %5$s));%n",
                    DomainInParameter.class.getName(), basicType.getTypeName(),
                    domainType.getTypeName(),
                    domainType.getInstantiationCommand(), m.getName());
            return null;
        }

        @Override
        public Void visitBasicListResultParameterMeta(
                BasicListResultParameterMeta m, AutoModuleQueryMeta p) {
            BasicType basicType = m.getBasicType();
            basicType.getWrapperType().accept(
                    new SimpleDataTypeVisitor<Void, Void, RuntimeException>() {

                        @Override
                        public Void visitEnumWrapperType(
                                EnumWrapperType dataType, Void p)
                                throws RuntimeException {
                            iprint("__query.setResultParameter(new %1$s<%2$s>(new %3$s(%4$s.class)));%n",
                                    BasicListResultParameter.class.getName(),
                                    dataType.getWrappedType()
                                            .getTypeNameAsTypeParameter(),
                                    dataType.getTypeName(), dataType
                                            .getWrappedType()
                                            .getQualifiedName());
                            return null;
                        }

                        @Override
                        public Void visitWrapperType(WrapperType dataType,
                                Void p) throws RuntimeException {
                            iprint("__query.setResultParameter(new %1$s<%2$s>(new %3$s()));%n",
                                    BasicListResultParameter.class.getName(),
                                    dataType.getWrappedType()
                                            .getTypeNameAsTypeParameter(),
                                    dataType.getTypeName());
                            return null;
                        }

                    }, null);
            return null;
        }

        @Override
        public Void visitDomainListResultParameterMeta(
                DomainListResultParameterMeta m, AutoModuleQueryMeta p) {
            DomainType domainType = m.getDomainType();
            BasicType basicType = domainType.getBasicType();
            iprint("__query.setResultParameter(new %1$s<%2$s, %3$s>(%4$s));%n",
                    DomainListResultParameter.class.getName(),
                    basicType.getTypeName(), domainType.getTypeName(),
                    domainType.getInstantiationCommand());
            return null;
        }

        @Override
        public Void visitEntityListResultParameterMeta(
                EntityListResultParameterMeta m, AutoModuleQueryMeta p) {
            EntityType entityType = m.getEntityType();
            iprint("__query.setResultParameter(new %1$s<%2$s>(%3$s.getSingletonInternal(), %4$s));%n",
                    EntityListResultParameter.class.getName(),
                    entityType.getTypeName(), entityType.getMetaTypeName(),
                    m.getEnsureResultMapping());
            return null;
        }

        @Override
        public Void visitMapListResultParameterMeta(
                MapListResultParameterMeta m, AutoModuleQueryMeta p) {
            MapKeyNamingType namingType = p.getMapKeyNamingType();
            iprint("__query.setResultParameter(new %1$s(%2$s.%3$s));%n",
                    MapListResultParameter.class.getName(), namingType
                            .getDeclaringClass().getName(), namingType.name());
            return null;
        }

        @Override
        public Void visitBasicResultParameterMeta(BasicResultParameterMeta m,
                AutoModuleQueryMeta p) {
            final BasicType basicType = m.getBasicType();
            basicType.getWrapperType().accept(
                    new SimpleDataTypeVisitor<Void, Void, RuntimeException>() {

                        @Override
                        public Void visitEnumWrapperType(
                                EnumWrapperType dataType, Void p)
                                throws RuntimeException {
                            iprint("__query.setResultParameter(new %1$s<%2$s>(new %3$s(%4$s.class), false));%n",
                                    BasicResultParameter.class.getName(),
                                    dataType.getWrappedType()
                                            .getTypeNameAsTypeParameter(),
                                    dataType.getTypeName(), dataType
                                            .getWrappedType()
                                            .getQualifiedName());
                            return null;
                        }

                        @Override
                        public Void visitWrapperType(WrapperType dataType,
                                Void p) throws RuntimeException {
                            iprint("__query.setResultParameter(new %1$s<%2$s>(new %3$s(), %4$s));%n",
                                    BasicResultParameter.class.getName(),
                                    dataType.getWrappedType()
                                            .getTypeNameAsTypeParameter(),
                                    dataType.getTypeName(), basicType
                                            .isPrimitive());
                            return null;
                        }

                    }, null);
            return null;
        }

        @Override
        public Void visitDomainResultParameterMeta(DomainResultParameterMeta m,
                AutoModuleQueryMeta p) {
            DomainType domainType = m.getDomainType();
            BasicType basicType = domainType.getBasicType();
            iprint("__query.setResultParameter(new %1$s<%2$s, %3$s>(%4$s));%n",
                    DomainResultParameter.class.getName(),
                    basicType.getTypeName(), domainType.getTypeName(),
                    domainType.getInstantiationCommand());
            return null;
        }
    }

    public String toCSVFormat(List<String> values) {
        final StringBuilder buf = new StringBuilder();
        if (values.size() > 0) {
            for (String value : values) {
                buf.append("\"");
                buf.append(value);
                buf.append("\", ");
            }
            buf.setLength(buf.length() - 2);
        }
        return buf.toString();
    }

}
