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

import static org.seasar.doma.internal.util.AssertionUtil.*;

import java.io.IOException;
import java.util.Iterator;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.TypeElement;
import javax.sql.DataSource;

import org.seasar.doma.AnnotationTarget;
import org.seasar.doma.DomaNullPointerException;
import org.seasar.doma.internal.apt.meta.AbstractCreateQueryMeta;
import org.seasar.doma.internal.apt.meta.AnnotateWithMeta;
import org.seasar.doma.internal.apt.meta.AnnotationMeta;
import org.seasar.doma.internal.apt.meta.ArrayCreateQueryMeta;
import org.seasar.doma.internal.apt.meta.AutoBatchModifyQueryMeta;
import org.seasar.doma.internal.apt.meta.AutoFunctionQueryMeta;
import org.seasar.doma.internal.apt.meta.AutoModifyQueryMeta;
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
import org.seasar.doma.internal.apt.meta.QueryMeta;
import org.seasar.doma.internal.apt.meta.QueryMetaVisitor;
import org.seasar.doma.internal.apt.meta.QueryParameterMeta;
import org.seasar.doma.internal.apt.meta.QueryReturnMeta;
import org.seasar.doma.internal.apt.meta.SqlFileBatchModifyQueryMeta;
import org.seasar.doma.internal.apt.meta.SqlFileModifyQueryMeta;
import org.seasar.doma.internal.apt.meta.SqlFileSelectQueryMeta;
import org.seasar.doma.internal.apt.mirror.ArrayFactoryMirror;
import org.seasar.doma.internal.apt.mirror.BatchModifyMirror;
import org.seasar.doma.internal.apt.mirror.DelegateMirror;
import org.seasar.doma.internal.apt.mirror.FunctionMirror;
import org.seasar.doma.internal.apt.mirror.ModifyMirror;
import org.seasar.doma.internal.apt.mirror.ProcedureMirror;
import org.seasar.doma.internal.apt.mirror.SelectMirror;
import org.seasar.doma.internal.apt.type.BasicType;
import org.seasar.doma.internal.apt.type.DomainType;
import org.seasar.doma.internal.apt.type.EntityType;
import org.seasar.doma.internal.apt.type.EnumWrapperType;
import org.seasar.doma.internal.apt.type.IterationCallbackType;
import org.seasar.doma.internal.apt.type.ListType;
import org.seasar.doma.internal.apt.type.SimpleDataTypeVisitor;
import org.seasar.doma.internal.apt.type.WrapperType;
import org.seasar.doma.internal.apt.util.AnnotationValueUtil;
import org.seasar.doma.internal.jdbc.command.BasicIterationHandler;
import org.seasar.doma.internal.jdbc.command.BasicResultListHandler;
import org.seasar.doma.internal.jdbc.command.BasicSingleResultHandler;
import org.seasar.doma.internal.jdbc.command.DomainIterationHandler;
import org.seasar.doma.internal.jdbc.command.DomainResultListHandler;
import org.seasar.doma.internal.jdbc.command.DomainSingleResultHandler;
import org.seasar.doma.internal.jdbc.command.EntityIterationHandler;
import org.seasar.doma.internal.jdbc.command.EntityResultListHandler;
import org.seasar.doma.internal.jdbc.command.EntitySingleResultHandler;
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
import org.seasar.doma.internal.jdbc.sql.SqlFileUtil;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.DomaAbstractDao;

/**
 * 
 * @author taedium
 * 
 */
public class DaoGenerator extends AbstractGenerator {

    protected final DaoMeta daoMeta;

    protected final String entitySuffix;

    protected final String domainSuffix;

    public DaoGenerator(ProcessingEnvironment env, TypeElement daoElement,
            DaoMeta daoMeta) throws IOException {
        super(env, daoElement, Options.getDaoPackage(env), Options
                .getDaoSubpackage(env), Options.getDaoSuffix(env));
        assertNotNull(daoMeta);
        this.daoMeta = daoMeta;
        this.entitySuffix = Options.getEntitySuffix(env);
        this.domainSuffix = Options.getDomainSuffix(env);
    }

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
        AnnotateWithMeta annotateWithMeta = daoMeta.getAnnotateWithMeta();
        if (annotateWithMeta != null) {
            for (AnnotationMeta annotationMeta : annotateWithMeta
                    .getAnnotationMetas()) {
                if (annotationMeta.getTarget() == AnnotationTarget.CLASS) {
                    iprint("@%1$s(%2$s)%n", annotationMeta.getTypeName(),
                            annotationMeta.getElements());
                }
            }
        }
        printGenerated();
        iprint("public class %1$s extends %2$s implements %3$s {%n",
                simpleName, DomaAbstractDao.class.getName(), daoMeta
                        .getDaoType());
        print("%n");
        indent();
        printFields();
        printConstructors();
        printMethods();
        unindent();
        print("}%n");
    }

    protected void printFields() {
    }

    protected void printConstructors() {
        if (daoMeta.isConfigAdapter()) {
            AnnotateWithMeta annotateWithMeta = daoMeta.getAnnotateWithMeta();
            if (annotateWithMeta != null) {
                for (AnnotationMeta annotationMeta : annotateWithMeta
                        .getAnnotationMetas()) {
                    if (annotationMeta.getTarget() == AnnotationTarget.CONSTRUCTOR) {
                        iprint("@%1$s(%2$s)%n", annotationMeta.getTypeName(),
                                annotationMeta.getElements());
                    }
                }
            }
            iprint("public %1$s(", simpleName);
            if (annotateWithMeta != null) {
                for (AnnotationMeta annotationMeta : annotateWithMeta
                        .getAnnotationMetas()) {
                    if (annotationMeta.getTarget() == AnnotationTarget.CONSTRUCTOR_PARAMETER) {
                        print("@%1$s(%2$s) ", annotationMeta.getTypeName(),
                                annotationMeta.getElements());
                    }
                }
            }
            print("%1$s config) {%n", Config.class.getName());
            indent();
            iprint("super(new %1$s(config));%n", daoMeta.getConfigType());
            unindent();
            iprint("}%n");
            print("%n");
        } else {
            iprint("public %1$s() {%n", simpleName);
            indent();
            iprint("super(new %1$s());%n", daoMeta.getConfigType());
            unindent();
            iprint("}%n");
            print("%n");
            iprint("public %1$s(%2$s dataSource) {%n", simpleName,
                    DataSource.class.getName());
            indent();
            iprint("super(new %1$s(), dataSource);%n", daoMeta.getConfigType());
            unindent();
            iprint("}%n");
            print("%n");
        }
    }

    protected void printMethods() {
        MethodBodyGenerator generator = new MethodBodyGenerator();
        for (QueryMeta queryMeta : daoMeta.getQueryMetas()) {
            printMethod(generator, queryMeta);
        }
    }

    protected void printMethod(MethodBodyGenerator generator, QueryMeta m) {
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
            print("%1$s %2$s", parameterMeta.getTypeName(), parameterMeta
                    .getName());
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
        m.accept(generator, null);
        unindent();
        iprint("}%n");
        print("%n");
    }

    protected class MethodBodyGenerator implements QueryMetaVisitor<Void, Void> {

        @Override
        public Void visistSqlFileSelectQueryMeta(SqlFileSelectQueryMeta m,
                Void p) {
            printEnteringStatement(m);
            printPrerequisiteStatements(m);

            iprint("%1$s __query = new %1$s();%n", m.getQueryClass().getName());
            iprint("__query.setConfig(config);%n");
            iprint(
                    "__query.setSqlFilePath(%1$s.buildPath(\"%2$s\", \"%3$s\"));%n",
                    SqlFileUtil.class.getName(), daoMeta.getDaoElement()
                            .getQualifiedName(), m.getName());
            if (m.getSelectOptionsType() != null) {
                iprint("__query.setOptions(%1$s);%n", m
                        .getSelectOptionsParameterName());
            }
            for (Iterator<QueryParameterMeta> it = m.getParameterMetas()
                    .iterator(); it.hasNext();) {
                QueryParameterMeta parameterMeta = it.next();
                if (parameterMeta.isBindable()) {
                    iprint(
                            "__query.addParameter(\"%1$s\", %2$s.class, %1$s);%n",
                            parameterMeta.getName(), parameterMeta
                                    .getQualifiedName());
                }
            }
            iprint("__query.setCallerClassName(\"%1$s\");%n", qualifiedName);
            iprint("__query.setCallerMethodName(\"%1$s\");%n", m.getName());
            SelectMirror mirror = m.getSelectMirror();
            if (mirror.getQueryTimeout() != null) {
                iprint("__query.setQueryTimeout(%1$s);%n", mirror
                        .getQueryTimeout());
            }
            if (mirror.getMaxRows() != null) {
                iprint("__query.setMaxRows(%1$s);%n", mirror.getMaxRows());
            }
            if (mirror.getFetchSize() != null) {
                iprint("__query.setFetchSize(%1$s);%n", mirror.getFetchSize());
            }
            iprint("__query.prepare();%n");
            final QueryReturnMeta resultMeta = m.getReturnMeta();
            final String commandClassName = m.getCommandClass().getName();
            if (AnnotationValueUtil.isEqual(Boolean.TRUE, mirror.getIterate())) {
                IterationCallbackType callbackType = m
                        .getIterationCallbackType();
                final String callbackParamName = m
                        .getIterationCallbackPrameterName();
                callbackType
                        .getTargetType()
                        .accept(
                                new SimpleDataTypeVisitor<Void, Void, RuntimeException>() {

                                    @Override
                                    public Void visitBasicType(
                                            BasicType dataType, Void p)
                                            throws RuntimeException {
                                        dataType
                                                .getWrapperType()
                                                .accept(
                                                        new SimpleDataTypeVisitor<Void, Void, RuntimeException>() {

                                                            @Override
                                                            public Void visitEnumWrapperType(
                                                                    EnumWrapperType dataType,
                                                                    Void p)
                                                                    throws RuntimeException {
                                                                iprint(
                                                                        "%1$s<%2$s> __command = new %1$s<%2$s>(__query, new %3$s<%2$s, %4$s>(new %5$s(%6$s.class), %7$s));%n",
                                                                        commandClassName,
                                                                        resultMeta
                                                                                .getTypeNameAsTypeParameter(),
                                                                        BasicIterationHandler.class
                                                                                .getName(),
                                                                        dataType
                                                                                .getWrappedType()
                                                                                .getTypeNameAsTypeParameter(),
                                                                        dataType
                                                                                .getTypeName(),
                                                                        dataType
                                                                                .getWrappedType()
                                                                                .getQualifiedName(),
                                                                        callbackParamName);
                                                                return null;
                                                            }

                                                            @Override
                                                            public Void visitWrapperType(
                                                                    WrapperType dataType,
                                                                    Void p)
                                                                    throws RuntimeException {
                                                                iprint(
                                                                        "%1$s<%2$s> __command = new %1$s<%2$s>(__query, new %3$s<%2$s, %4$s>(new %5$s(), %6$s));%n",
                                                                        commandClassName,
                                                                        resultMeta
                                                                                .getTypeNameAsTypeParameter(),
                                                                        BasicIterationHandler.class
                                                                                .getName(),
                                                                        dataType
                                                                                .getWrappedType()
                                                                                .getTypeNameAsTypeParameter(),
                                                                        dataType
                                                                                .getTypeName(),
                                                                        callbackParamName);
                                                                return null;
                                                            }

                                                        }, null);

                                        return null;
                                    }

                                    @Override
                                    public Void visitDomainType(
                                            DomainType dataType, Void p)
                                            throws RuntimeException {
                                        BasicType basicType = dataType
                                                .getBasicType();
                                        iprint(
                                                "%1$s<%2$s> __command = new %1$s<%2$s>(__query, new %3$s<%2$s, %4$s, %5$s>(new %5$s%6$s(), %7$s));%n",
                                                commandClassName,
                                                resultMeta
                                                        .getTypeNameAsTypeParameter(),
                                                DomainIterationHandler.class
                                                        .getName(), basicType
                                                        .getTypeName(),
                                                dataType.getTypeName(),
                                                domainSuffix, callbackParamName);
                                        return null;
                                    }

                                    @Override
                                    public Void visitEntityType(
                                            EntityType dataType, Void p)
                                            throws RuntimeException {
                                        iprint(
                                                "%1$s<%2$s> __command = new %1$s<%2$s>(__query, new %3$s<%2$s, %4$s>(new %4$s%5$s(), %6$s));%n",
                                                commandClassName,
                                                resultMeta
                                                        .getTypeNameAsTypeParameter(),
                                                EntityIterationHandler.class
                                                        .getName(), dataType
                                                        .getTypeName(),
                                                entitySuffix, callbackParamName);
                                        return null;
                                    }

                                }, null);
                if ("void".equals(resultMeta.getTypeName())) {
                    iprint("__command.execute();%n");
                    iprint("exiting(\"%1$s\", \"%2$s\", null);%n",
                            qualifiedName, m.getName());
                } else {
                    iprint("%1$s __result = __command.execute();%n", resultMeta
                            .getTypeName());
                    iprint("exiting(\"%1$s\", \"%2$s\", __result);%n",
                            qualifiedName, m.getName());
                    iprint("return __result;%n");
                }
            } else {
                m
                        .getReturnMeta()
                        .getDataType()
                        .accept(
                                new SimpleDataTypeVisitor<Void, Void, RuntimeException>() {

                                    @Override
                                    public Void visitBasicType(
                                            BasicType dataType, Void p)
                                            throws RuntimeException {
                                        dataType
                                                .getWrapperType()
                                                .accept(
                                                        new SimpleDataTypeVisitor<Void, Void, RuntimeException>() {

                                                            @Override
                                                            public Void visitEnumWrapperType(
                                                                    EnumWrapperType dataType,
                                                                    Void p)
                                                                    throws RuntimeException {
                                                                iprint(
                                                                        "%1$s<%2$s> __command = new %1$s<%2$s>(__query, new %3$s<%2$s>(new %4$s(%5$s.class)));%n",
                                                                        commandClassName,
                                                                        dataType
                                                                                .getWrappedType()
                                                                                .getTypeNameAsTypeParameter(),
                                                                        BasicSingleResultHandler.class
                                                                                .getName(),
                                                                        dataType
                                                                                .getTypeName(),
                                                                        dataType
                                                                                .getWrappedType()
                                                                                .getQualifiedName());
                                                                return null;
                                                            }

                                                            @Override
                                                            public Void visitWrapperType(
                                                                    WrapperType dataType,
                                                                    Void p)
                                                                    throws RuntimeException {
                                                                iprint(
                                                                        "%1$s<%2$s> __command = new %1$s<%2$s>(__query, new %3$s<%2$s>(new %4$s()));%n",
                                                                        commandClassName,
                                                                        dataType
                                                                                .getWrappedType()
                                                                                .getTypeNameAsTypeParameter(),
                                                                        BasicSingleResultHandler.class
                                                                                .getName(),
                                                                        dataType
                                                                                .getTypeName());
                                                                return null;
                                                            }

                                                        }, null);

                                        return null;
                                    }

                                    @Override
                                    public Void visitDomainType(
                                            DomainType dataType, Void p)
                                            throws RuntimeException {
                                        BasicType basicType = dataType
                                                .getBasicType();
                                        iprint(
                                                "%1$s<%2$s> __command = new %1$s<%2$s>(__query, new %3$s<%4$s, %2$s>(new %2$s%5$s()));%n",
                                                commandClassName, dataType
                                                        .getTypeName(),
                                                DomainSingleResultHandler.class
                                                        .getName(), basicType
                                                        .getTypeName(),
                                                domainSuffix);
                                        return null;
                                    }

                                    @Override
                                    public Void visitEntityType(
                                            EntityType dataType, Void p)
                                            throws RuntimeException {
                                        iprint(
                                                "%1$s<%2$s> __command = new %1$s<%2$s>(__query, new %3$s<%2$s>(new %4$s%5$s()));%n",
                                                commandClassName, dataType
                                                        .getTypeName(),
                                                EntitySingleResultHandler.class
                                                        .getName(), dataType
                                                        .getTypeName(),
                                                entitySuffix);
                                        return null;
                                    }

                                    @Override
                                    public Void visitListType(
                                            final ListType listType, Void p)
                                            throws RuntimeException {
                                        listType
                                                .getElementType()
                                                .accept(
                                                        new SimpleDataTypeVisitor<Void, Void, RuntimeException>() {

                                                            @Override
                                                            public Void visitBasicType(
                                                                    BasicType dataType,
                                                                    Void p)
                                                                    throws RuntimeException {
                                                                dataType
                                                                        .getWrapperType()
                                                                        .accept(
                                                                                new SimpleDataTypeVisitor<Void, Void, RuntimeException>() {

                                                                                    @Override
                                                                                    public Void visitEnumWrapperType(
                                                                                            EnumWrapperType dataType,
                                                                                            Void p)
                                                                                            throws RuntimeException {
                                                                                        iprint(
                                                                                                "%1$s<%2$s> __command = new %1$s<%2$s>(__query, new %3$s<%4$s>(new %5$s(%6$s.class)));%n",
                                                                                                commandClassName,
                                                                                                listType
                                                                                                        .getTypeName(),
                                                                                                BasicResultListHandler.class
                                                                                                        .getName(),
                                                                                                dataType
                                                                                                        .getWrappedType()
                                                                                                        .getTypeNameAsTypeParameter(),
                                                                                                dataType
                                                                                                        .getTypeName(),
                                                                                                dataType
                                                                                                        .getWrappedType()
                                                                                                        .getQualifiedName());
                                                                                        return null;
                                                                                    }

                                                                                    @Override
                                                                                    public Void visitWrapperType(
                                                                                            WrapperType dataType,
                                                                                            Void p)
                                                                                            throws RuntimeException {
                                                                                        iprint(
                                                                                                "%1$s<%2$s> __command = new %1$s<%2$s>(__query, new %3$s<%4$s>(new %5$s()));%n",
                                                                                                commandClassName,
                                                                                                listType
                                                                                                        .getTypeNameAsTypeParameter(),
                                                                                                BasicResultListHandler.class
                                                                                                        .getName(),
                                                                                                dataType
                                                                                                        .getWrappedType()
                                                                                                        .getTypeNameAsTypeParameter(),
                                                                                                dataType
                                                                                                        .getTypeName());
                                                                                        return null;
                                                                                    }

                                                                                },
                                                                                null);

                                                                return null;
                                                            }

                                                            @Override
                                                            public Void visitDomainType(
                                                                    DomainType dataType,
                                                                    Void p)
                                                                    throws RuntimeException {
                                                                BasicType basicType = dataType
                                                                        .getBasicType();
                                                                iprint(
                                                                        "%1$s<%2$s> __command = new %1$s<%2$s>(__query, new %3$s<%4$s, %5$s>(new %5$s%6$s()));%n",
                                                                        commandClassName,
                                                                        listType
                                                                                .getTypeName(),
                                                                        DomainResultListHandler.class
                                                                                .getName(),
                                                                        basicType
                                                                                .getTypeName(),
                                                                        dataType
                                                                                .getTypeName(),
                                                                        domainSuffix);
                                                                return null;
                                                            }

                                                            @Override
                                                            public Void visitEntityType(
                                                                    EntityType dataType,
                                                                    Void p)
                                                                    throws RuntimeException {
                                                                iprint(
                                                                        "%1$s<%2$s> __command = new %1$s<%2$s>(__query, new %3$s<%4$s>(new %4$s%5$s()));%n",
                                                                        commandClassName,
                                                                        listType
                                                                                .getTypeName(),
                                                                        EntityResultListHandler.class
                                                                                .getName(),
                                                                        dataType
                                                                                .getTypeName(),
                                                                        entitySuffix);
                                                                return null;
                                                            }

                                                        }, null);
                                        return null;
                                    }

                                }, null);
                iprint("%1$s __result = __command.execute();%n", resultMeta
                        .getTypeName());
                iprint("exiting(\"%1$s\", \"%2$s\", __result);%n",
                        qualifiedName, m.getName());
                iprint("return __result;%n");
            }
            return null;
        }

        @Override
        public Void visistAutoModifyQueryMeta(AutoModifyQueryMeta m, Void p) {
            printEnteringStatement(m);
            printPrerequisiteStatements(m);

            iprint("%1$s<%2$s> __query = new %1$s<%2$s>(new %2$s%3$s());%n", m
                    .getQueryClass().getName(), m.getEntityType()
                    .getTypeNameAsTypeParameter(), entitySuffix);
            iprint("__query.setConfig(config);%n");
            iprint("__query.setEntity(%1$s);%n", m.getEntityParameterName());
            iprint("__query.setCallerClassName(\"%1$s\");%n", qualifiedName);
            iprint("__query.setCallerMethodName(\"%1$s\");%n", m.getName());
            ModifyMirror mirror = m.getModifyMirror();
            if (mirror.getQueryTimeout() != null) {
                iprint("__query.setQueryTimeout(%1$s);%n", mirror
                        .getQueryTimeout());
            }
            if (mirror.getExcludeNull() != null) {
                iprint("__query.setNullExcluded(%1$s);%n", mirror
                        .getExcludeNull());
            }
            if (mirror.getIncludeVersion() != null) {
                iprint("__query.setVersionIncluded(%1$s);%n", mirror
                        .getIncludeVersion());
            }
            if (mirror.getIgnoreVersion() != null) {
                iprint("__query.setVersionIgnored(%1$s);%n", mirror
                        .getIgnoreVersion());
            }
            if (mirror.getInclude() != null) {
                String s = AnnotationValueUtil.toCSVFormat(mirror.getInclude());
                iprint("__query.setIncludedPropertyNames(%1$s);%n", s);
            }
            if (mirror.getExclude() != null) {
                String s = AnnotationValueUtil.toCSVFormat(mirror.getExclude());
                iprint("__query.setExcludedPropertyNames(%1$s);%n", s);
            }
            if (mirror.getIncludeUnchanged() != null) {
                iprint("__query.setUnchangedPropertyIncluded(%1$s);%n", mirror
                        .getIncludeUnchanged());
            }
            if (mirror.getSuppressOptimisticLockException() != null) {
                iprint("__query.setOptimisticLockExceptionSuppressed(%1$s);%n",
                        mirror.getSuppressOptimisticLockException());
            }
            iprint("__query.prepare();%n");
            iprint("%1$s __command = new %1$s(__query);%n", m.getCommandClass()
                    .getName());
            iprint("%1$s __result = __command.execute();%n", m.getReturnMeta()
                    .getTypeName());
            iprint("exiting(\"%1$s\", \"%2$s\", __result);%n", qualifiedName, m
                    .getName());
            iprint("return __result;%n");
            return null;
        }

        @Override
        public Void visistSqlFileModifyQueryMeta(SqlFileModifyQueryMeta m,
                Void p) {
            printEnteringStatement(m);
            printPrerequisiteStatements(m);

            iprint("%1$s __query = new %1$s();%n", m.getQueryClass().getName());
            iprint("__query.setConfig(config);%n");
            iprint(
                    "__query.setSqlFilePath(%1$s.buildPath(\"%2$s\", \"%3$s\"));%n",
                    SqlFileUtil.class.getName(), daoMeta.getDaoElement()
                            .getQualifiedName(), m.getName());
            for (Iterator<QueryParameterMeta> it = m.getParameterMetas()
                    .iterator(); it.hasNext();) {
                QueryParameterMeta parameterMeta = it.next();
                if (parameterMeta.isBindable()) {
                    iprint(
                            "__query.addParameter(\"%1$s\", %2$s.class, %1$s);%n",
                            parameterMeta.getName(), parameterMeta
                                    .getQualifiedName());
                }
            }
            iprint("__query.setCallerClassName(\"%1$s\");%n", qualifiedName);
            iprint("__query.setCallerMethodName(\"%1$s\");%n", m.getName());
            ModifyMirror mirror = m.getModifyMirror();
            if (mirror.getQueryTimeout() != null) {
                iprint("__query.setQueryTimeout(%1$s);%n", mirror
                        .getQueryTimeout());
            }
            iprint("__query.prepare();%n");
            iprint("%1$s __command = new %1$s(__query);%n", m.getCommandClass()
                    .getName());
            iprint("%1$s __result = __command.execute();%n", m.getReturnMeta()
                    .getTypeName());
            iprint("exiting(\"%1$s\", \"%2$s\", __result);%n", qualifiedName, m
                    .getName());
            iprint("return __result;%n");
            return null;
        }

        @Override
        public Void visitAutoBatchModifyQueryMeta(AutoBatchModifyQueryMeta m,
                Void p) {
            printEnteringStatement(m);
            printPrerequisiteStatements(m);

            iprint("%1$s<%2$s> __query = new %1$s<%2$s>(new %2$s%3$s());%n", m
                    .getQueryClass().getName(), m.getEntityType()
                    .getTypeNameAsTypeParameter(), entitySuffix);
            iprint("__query.setConfig(config);%n");
            iprint("__query.setEntities(%1$s);%n", m.getEntitiesParameterName());
            iprint("__query.setCallerClassName(\"%1$s\");%n", qualifiedName);
            iprint("__query.setCallerMethodName(\"%1$s\");%n", m.getName());
            BatchModifyMirror mirror = m.getBatchModifyMirror();
            if (mirror.getQueryTimeout() != null) {
                iprint("__query.setQueryTimeout(%1$s);%n", mirror
                        .getQueryTimeout());
            }
            if (mirror.getIncludeVersion() != null) {
                iprint("__query.setVersionIncluded(%1$s);%n", mirror
                        .getIncludeVersion());
            }
            if (mirror.getIgnoreVersion() != null) {
                iprint("__query.setVersionIgnored(%1$s);%n", mirror
                        .getIgnoreVersion());
            }
            if (mirror.getInclude() != null) {
                String s = AnnotationValueUtil.toCSVFormat(mirror.getInclude());
                iprint("__query.setIncludedPropertyNames(%1$s);%n", s);
            }
            if (mirror.getExclude() != null) {
                String s = AnnotationValueUtil.toCSVFormat(mirror.getExclude());
                iprint("__query.setExcludedPropertyNames(%1$s);%n", s);
            }
            if (mirror.getSuppressOptimisticLockException() != null) {
                iprint("__query.setOptimisticLockExceptionSuppressed(%1$s);%n",
                        mirror.getSuppressOptimisticLockException());
            }
            iprint("__query.prepare();%n");
            iprint("%1$s __command = new %1$s(__query);%n", m.getCommandClass()
                    .getName());
            iprint("%1$s __result = __command.execute();%n", m.getReturnMeta()
                    .getTypeName());
            iprint("exiting(\"%1$s\", \"%2$s\", __result);%n", qualifiedName, m
                    .getName());
            iprint("return __result;%n");
            return null;
        }

        @Override
        public Void visitSqlFileBatchModifyQueryMeta(
                SqlFileBatchModifyQueryMeta m, Void p) {
            printEnteringStatement(m);
            printPrerequisiteStatements(m);

            iprint("%1$s<%2$s> __query = new %1$s<%2$s>(new %2$s%3$s());%n", m
                    .getQueryClass().getName(),
                    m.getEntityType().getTypeName(), entitySuffix);
            iprint("__query.setConfig(config);%n");
            iprint("__query.setEntities(%1$s);%n", m.getEntitiesParameterName());
            iprint(
                    "__query.setSqlFilePath(%1$s.buildPath(\"%2$s\", \"%3$s\"));%n",
                    SqlFileUtil.class.getName(), daoMeta.getDaoElement()
                            .getQualifiedName(), m.getName());
            iprint("__query.setParameterName(\"%1$s\");%n", m
                    .getEntitiesParameterName());
            iprint("__query.setCallerClassName(\"%1$s\");%n", qualifiedName);
            iprint("__query.setCallerMethodName(\"%1$s\");%n", m.getName());
            BatchModifyMirror mirror = m.getBatchModifyMirror();
            if (mirror.getQueryTimeout() != null) {
                iprint("__query.setQueryTimeout(%1$s);%n", mirror
                        .getQueryTimeout());
            }
            iprint("__query.prepare();%n");
            iprint("%1$s __command = new %1$s(__query);%n", m.getCommandClass()
                    .getName());
            iprint("%1$s __result = __command.execute();%n", m.getReturnMeta()
                    .getTypeName());
            iprint("exiting(\"%1$s\", \"%2$s\", __result);%n", qualifiedName, m
                    .getName());
            iprint("return __result;%n");
            return null;
        }

        @Override
        public Void visitAutoFunctionQueryMeta(AutoFunctionQueryMeta m, Void p) {
            printEnteringStatement(m);
            printPrerequisiteStatements(m);

            QueryReturnMeta resultMeta = m.getReturnMeta();
            iprint("%1$s<%2$s> __query = new %1$s<%2$s>();%n", m
                    .getQueryClass().getName(), resultMeta
                    .getTypeNameAsTypeParameter());
            iprint("__query.setConfig(config);%n");
            iprint("__query.setFunctionName(\"%1$s\");%n", m.getFunctionName());
            CallableSqlParameterStatementGenerator parameterGenerator = new CallableSqlParameterStatementGenerator();
            m.getResultParameterMeta().accept(parameterGenerator, p);
            for (CallableSqlParameterMeta parameterMeta : m
                    .getCallableSqlParameterMetas()) {
                parameterMeta.accept(parameterGenerator, p);
            }
            iprint("__query.setCallerClassName(\"%1$s\");%n", qualifiedName);
            iprint("__query.setCallerMethodName(\"%1$s\");%n", m.getName());
            FunctionMirror mirror = m.getFunctionMirror();
            if (mirror.getQueryTimeout() != null) {
                iprint("__query.setQueryTimeout(%1$s);%n", mirror
                        .getQueryTimeout());
            }
            iprint("__query.prepare();%n");
            iprint("%1$s<%2$s> __command = new %1$s<%2$s>(__query);%n", m
                    .getCommandClass().getName(), resultMeta
                    .getTypeNameAsTypeParameter());
            iprint("%1$s __result = __command.execute();%n", resultMeta
                    .getTypeName());
            iprint("exiting(\"%1$s\", \"%2$s\", __result);%n", qualifiedName, m
                    .getName());
            iprint("return __result;%n");
            return null;
        }

        @Override
        public Void visitAutoProcedureQueryMeta(AutoProcedureQueryMeta m, Void p) {
            printEnteringStatement(m);
            printPrerequisiteStatements(m);

            iprint("%1$s __query = new %1$s();%n", m.getQueryClass().getName());
            iprint("__query.setConfig(config);%n");
            iprint("__query.setProcedureName(\"%1$s\");%n", m
                    .getProcedureName());
            CallableSqlParameterStatementGenerator parameterGenerator = new CallableSqlParameterStatementGenerator();
            for (CallableSqlParameterMeta parameterMeta : m
                    .getCallableSqlParameterMetas()) {
                parameterMeta.accept(parameterGenerator, p);
            }
            iprint("__query.setCallerClassName(\"%1$s\");%n", qualifiedName);
            iprint("__query.setCallerMethodName(\"%1$s\");%n", m.getName());
            ProcedureMirror mirror = m.getProcedureMirror();
            if (mirror.getQueryTimeout() != null) {
                iprint("__query.setQueryTimeout(%1$s);%n", mirror
                        .getQueryTimeout());
            }
            iprint("__query.prepare();%n");
            iprint("%1$s __command = new %1$s(__query);%n", m.getCommandClass()
                    .getName());
            iprint("__command.execute();%n");
            iprint("exiting(\"%1$s\", \"%2$s\", null);%n", qualifiedName, m
                    .getName());
            return null;
        }

        @Override
        public Void visitAbstractCreateQueryMeta(AbstractCreateQueryMeta m,
                Void p) {
            printEnteringStatement(m);
            printPrerequisiteStatements(m);

            QueryReturnMeta resultMeta = m.getReturnMeta();
            iprint("%1$s __query = new %1$s();%n", m.getQueryClass().getName(),
                    resultMeta.getTypeName());
            iprint("__query.setConfig(config);%n");
            iprint("__query.setCallerClassName(\"%1$s\");%n", qualifiedName);
            iprint("__query.setCallerMethodName(\"%1$s\");%n", m.getName());
            iprint("__query.prepare();%n");
            iprint("%1$s<%2$s> __command = new %1$s<%2$s>(__query);%n", m
                    .getCommandClass().getName(), resultMeta.getTypeName());
            iprint("%1$s __result = __command.execute();%n", resultMeta
                    .getTypeName());
            iprint("exiting(\"%1$s\", \"%2$s\", __result);%n", qualifiedName, m
                    .getName());
            iprint("return __result;%n");
            return null;
        }

        @Override
        public Void visitArrayCreateQueryMeta(ArrayCreateQueryMeta m, Void p) {
            iprint("entering(\"%1$s\", \"%2$s\", (Object)%3$s);%n",
                    qualifiedName, m.getName(), m.getParameterName());
            printPrerequisiteStatements(m);

            QueryReturnMeta resultMeta = m.getReturnMeta();
            ArrayFactoryMirror mirror = m.getArrayFactoryMirror();
            iprint("%1$s __query = new %1$s();%n", m.getQueryClass().getName());
            iprint("__query.setConfig(config);%n");
            iprint("__query.setCallerClassName(\"%1$s\");%n", qualifiedName);
            iprint("__query.setCallerMethodName(\"%1$s\");%n", m.getName());
            iprint("__query.setTypeName(\"%1$s\");%n", mirror
                    .getTypeNameValue());
            iprint("__query.setElements(%1$s);%n", m.getParameterName());
            iprint("__query.prepare();%n");
            iprint("%1$s<%2$s> __command = new %1$s<%2$s>(__query);%n", m
                    .getCommandClass().getName(), resultMeta
                    .getTypeNameAsTypeParameter());
            iprint("%1$s __result = __command.execute();%n", resultMeta
                    .getTypeName());
            iprint("exiting(\"%1$s\", \"%2$s\", __result);%n", qualifiedName, m
                    .getName());
            iprint("return __result;%n");
            return null;
        }

        @Override
        public Void visitDelegateQueryMeta(DelegateQueryMeta m, Void p) {
            printEnteringStatement(m);

            DelegateMirror mirror = m.getDelegateMirror();
            iprint("%1$s delegate = new %1$s(config", mirror.getToValue());
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
            print("delegate.%1$s(", m.getName());
            for (Iterator<QueryParameterMeta> it = m.getParameterMetas()
                    .iterator(); it.hasNext();) {
                QueryParameterMeta parameterMeta = it.next();
                print("%1$s", parameterMeta.getName());
                if (it.hasNext()) {
                    print(", ");
                }
            }
            print(");%n");
            iprint("exiting(\"%1$s\", \"%2$s\", __result);%n", qualifiedName, m
                    .getName());
            iprint("return __result;%n");
            return null;
        }

        protected void printEnteringStatement(QueryMeta m) {
            iprint("entering(\"%1$s\", \"%2$s\"", qualifiedName, m.getName());
            for (Iterator<QueryParameterMeta> it = m.getParameterMetas()
                    .iterator(); it.hasNext();) {
                QueryParameterMeta parameterMeta = it.next();
                print(", %1$s", parameterMeta.getName());
            }
            print(");%n");
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
            CallableSqlParameterMetaVisitor<Void, Void> {

        @Override
        public Void visitBasicListParameterMeta(final BasicListParameterMeta m,
                Void p) {
            BasicType basicType = m.getBasicType();
            basicType.getWrapperType().accept(
                    new SimpleDataTypeVisitor<Void, Void, RuntimeException>() {

                        @Override
                        public Void visitEnumWrapperType(
                                EnumWrapperType dataType, Void p)
                                throws RuntimeException {
                            iprint(
                                    "__query.addParameter(new %1$s<%2$s>(new %3$s(%4$s.class), %5$s));%n",
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
                            iprint(
                                    "__query.addParameter(new %1$s<%2$s>(new %3$s(), %4$s));%n",
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
                Void p) {
            DomainType domainType = m.getDomainType();
            BasicType basicType = domainType.getBasicType();
            iprint(
                    "__query.addParameter(new %1$s<%2$s, %3$s>(new %3$s%4$s(), %5$s));%n",
                    DomainListParameter.class.getName(), basicType
                            .getTypeName(), domainType.getTypeName(),
                    domainSuffix, m.getName());
            return null;
        }

        @Override
        public Void visitEntityListParameterMeta(EntityListParameterMeta m,
                Void p) {
            EntityType entityType = m.getEntityType();
            iprint(
                    "__query.addParameter(new %1$s<%2$s>(new %2$s%3$s(), %4$s));%n",
                    EntityListParameter.class.getName(), entityType
                            .getTypeName(), entitySuffix, m.getName());
            return null;
        }

        @Override
        public Void visitBasicInOutParameterMeta(
                final BasicInOutParameterMeta m, Void p) {
            BasicType basicType = m.getBasicType();
            basicType.getWrapperType().accept(
                    new SimpleDataTypeVisitor<Void, Void, RuntimeException>() {

                        @Override
                        public Void visitEnumWrapperType(
                                EnumWrapperType dataType, Void p)
                                throws RuntimeException {
                            iprint(
                                    "__query.addParameter(new %1$s<%2$s>(new %3$s(%4$s.class), %5$s));%n",
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
                            iprint(
                                    "__query.addParameter(new %1$s<%2$s>(new %3$s(), %4$s));%n",
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
                Void p) {
            DomainType domainType = m.getDomainType();
            BasicType basicType = domainType.getBasicType();
            iprint(
                    "__query.addParameter(new %1$s<%2$s, %3$s>(new %3$s%4$s(), %5$s));%n",
                    DomainInOutParameter.class.getName(), basicType
                            .getTypeNameAsTypeParameter(), domainType
                            .getTypeName(), domainSuffix, m.getName());
            return null;
        }

        @Override
        public Void visitBasicOutParameterMeta(final BasicOutParameterMeta m,
                Void p) {
            BasicType basicType = m.getBasicType();
            basicType.getWrapperType().accept(
                    new SimpleDataTypeVisitor<Void, Void, RuntimeException>() {

                        @Override
                        public Void visitEnumWrapperType(
                                EnumWrapperType dataType, Void p)
                                throws RuntimeException {
                            iprint(
                                    "__query.addParameter(new %1$s<%2$s>(new %3$s(%4$s.class), %5$s));%n",
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
                            iprint(
                                    "__query.addParameter(new %1$s<%2$s>(new %3$s(), %4$s));%n",
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
        public Void visitDomainOutParameterMeta(DomainOutParameterMeta m, Void p) {
            DomainType domainType = m.getDomainType();
            BasicType basicType = domainType.getBasicType();
            iprint(
                    "__query.addParameter(new %1$s<%2$s, %3$s>(new %3$s%4$s(), %5$s));%n",
                    DomainOutParameter.class.getName(),
                    basicType.getTypeName(), domainType.getTypeName(),
                    domainSuffix, m.getName());
            return null;
        }

        @Override
        public Void visitBasicInParameterMeta(final BasicInParameterMeta m,
                Void p) {
            BasicType basicType = m.getBasicType();
            basicType.getWrapperType().accept(
                    new SimpleDataTypeVisitor<Void, Void, RuntimeException>() {

                        @Override
                        public Void visitEnumWrapperType(
                                EnumWrapperType dataType, Void p)
                                throws RuntimeException {
                            iprint(
                                    "__query.addParameter(new %1$s(new %2$s(%3$s.class, %4$s)));%n",
                                    BasicInParameter.class.getName(), dataType
                                            .getTypeName(), dataType
                                            .getWrappedType().getTypeName(), m
                                            .getName());
                            return null;
                        }

                        @Override
                        public Void visitWrapperType(WrapperType dataType,
                                Void p) throws RuntimeException {
                            iprint(
                                    "__query.addParameter(new %1$s(new %2$s(%3$s)));%n",
                                    BasicInParameter.class.getName(), dataType
                                            .getTypeName(), m.getName());
                            return null;
                        }

                    }, null);

            return null;
        }

        @Override
        public Void visitDomainInParameterMeta(DomainInParameterMeta m, Void p) {
            DomainType domainType = m.getDomainType();
            BasicType basicType = domainType.getBasicType();
            iprint(
                    "__query.addParameter(new %1$s<%2$s, %3$s>(new %3$s%4$s(), %5$s));%n",
                    DomainInParameter.class.getName(), basicType.getTypeName(),
                    domainType.getTypeName(), domainSuffix, m.getName());
            return null;
        }

        @Override
        public Void visitBasicListResultParameterMeta(
                BasicListResultParameterMeta m, Void p) {
            BasicType basicType = m.getBasicType();
            basicType.getWrapperType().accept(
                    new SimpleDataTypeVisitor<Void, Void, RuntimeException>() {

                        @Override
                        public Void visitEnumWrapperType(
                                EnumWrapperType dataType, Void p)
                                throws RuntimeException {
                            iprint(
                                    "__query.setResultParameter(new %1$s<%2$s>(new %3$s(%4$s.class)));%n",
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
                            iprint(
                                    "__query.setResultParameter(new %1$s<%2$s>(new %3$s()));%n",
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
                DomainListResultParameterMeta m, Void p) {
            DomainType domainType = m.getDomainType();
            BasicType basicType = domainType.getBasicType();
            iprint(
                    "__query.setResultParameter(new %1$s<%2$s, %3$s>(new %3$s%4$s()));%n",
                    DomainListResultParameter.class.getName(), basicType
                            .getTypeName(), domainType.getTypeName(),
                    domainSuffix);
            return null;
        }

        @Override
        public Void visitEntityListResultParameterMeta(
                EntityListResultParameterMeta m, Void p) {
            EntityType entityType = m.getEntityType();
            iprint(
                    "__query.setResultParameter(new %1$s<%2$s>(new %2$s%3$s()));%n",
                    EntityListResultParameter.class.getName(), entityType
                            .getTypeName(), entitySuffix);
            return null;
        }

        @Override
        public Void visitBasicResultParameterMeta(BasicResultParameterMeta m,
                Void p) {
            BasicType basicType = m.getBasicType();
            basicType.getWrapperType().accept(
                    new SimpleDataTypeVisitor<Void, Void, RuntimeException>() {

                        @Override
                        public Void visitEnumWrapperType(
                                EnumWrapperType dataType, Void p)
                                throws RuntimeException {
                            iprint(
                                    "__query.setResultParameter(new %1$s<%2$s>(new %3$s(%4$s.class)));%n",
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
                            iprint(
                                    "__query.setResultParameter(new %1$s<%2$s>(new %3$s()));%n",
                                    BasicResultParameter.class.getName(),
                                    dataType.getWrappedType()
                                            .getTypeNameAsTypeParameter(),
                                    dataType.getTypeName());
                            return null;
                        }

                    }, null);
            return null;
        }

        @Override
        public Void visitDomainResultParameterMeta(DomainResultParameterMeta m,
                Void p) {
            DomainType domainType = m.getDomainType();
            BasicType basicType = domainType.getBasicType();
            iprint(
                    "__query.setResultParameter(new %1$s<%2$s, %3$s>(new %3$s%4$s()));%n",
                    DomainResultParameter.class.getName(), basicType
                            .getTypeName(), domainType.getTypeName(),
                    domainSuffix);
            return null;
        }
    }
}
