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
import java.util.Map;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.TypeElement;
import javax.sql.DataSource;

import org.seasar.doma.DomaNullPointerException;
import org.seasar.doma.internal.apt.meta.AbstractCreateQueryMeta;
import org.seasar.doma.internal.apt.meta.ArrayCreateQueryMeta;
import org.seasar.doma.internal.apt.meta.AutoBatchModifyQueryMeta;
import org.seasar.doma.internal.apt.meta.AutoFunctionQueryMeta;
import org.seasar.doma.internal.apt.meta.AutoModifyQueryMeta;
import org.seasar.doma.internal.apt.meta.AutoProcedureQueryMeta;
import org.seasar.doma.internal.apt.meta.CallableSqlParameterMeta;
import org.seasar.doma.internal.apt.meta.CallableSqlParameterMetaVisitor;
import org.seasar.doma.internal.apt.meta.DaoMeta;
import org.seasar.doma.internal.apt.meta.DelegateQueryMeta;
import org.seasar.doma.internal.apt.meta.DomainListParameterMeta;
import org.seasar.doma.internal.apt.meta.DomainListResultParameterMeta;
import org.seasar.doma.internal.apt.meta.DomainResultParameterMeta;
import org.seasar.doma.internal.apt.meta.EntityListParameterMeta;
import org.seasar.doma.internal.apt.meta.EntityListResultParameterMeta;
import org.seasar.doma.internal.apt.meta.InOutParameterMeta;
import org.seasar.doma.internal.apt.meta.InParameterMeta;
import org.seasar.doma.internal.apt.meta.OutParameterMeta;
import org.seasar.doma.internal.apt.meta.QueryMeta;
import org.seasar.doma.internal.apt.meta.QueryMetaVisitor;
import org.seasar.doma.internal.apt.meta.SqlFileBatchModifyQueryMeta;
import org.seasar.doma.internal.apt.meta.SqlFileModifyQueryMeta;
import org.seasar.doma.internal.apt.meta.SqlFileSelectQueryMeta;
import org.seasar.doma.internal.jdbc.command.DomainIterationHandler;
import org.seasar.doma.internal.jdbc.command.DomainResultListHandler;
import org.seasar.doma.internal.jdbc.command.DomainSingleResultHandler;
import org.seasar.doma.internal.jdbc.command.EntityIterationHandler;
import org.seasar.doma.internal.jdbc.command.EntityResultListHandler;
import org.seasar.doma.internal.jdbc.command.EntitySingleResultHandler;
import org.seasar.doma.internal.jdbc.sql.DomainListParameter;
import org.seasar.doma.internal.jdbc.sql.DomainListResultParameter;
import org.seasar.doma.internal.jdbc.sql.DomainResultParameter;
import org.seasar.doma.internal.jdbc.sql.EntityListParameter;
import org.seasar.doma.internal.jdbc.sql.EntityListResultParameter;
import org.seasar.doma.internal.jdbc.sql.InOutParameter;
import org.seasar.doma.internal.jdbc.sql.InParameter;
import org.seasar.doma.internal.jdbc.sql.OutParameter;
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

    public DaoGenerator(ProcessingEnvironment env, TypeElement daoElement,
            DaoMeta daoMeta) throws IOException {
        super(env, daoElement, createQualifiedName(env, daoElement, Options
                .getDaoSubpackage(env), Options.getDaoSuffix(env)));
        assertNotNull(daoMeta);
        this.daoMeta = daoMeta;
        this.entitySuffix = Options.getEntitySuffix(env);
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
        iprint("public %1$s() {%n", simpleName);
        indent();
        iprint("super(new %1$s(), null);%n", daoMeta.getConfigType());
        unindent();
        iprint("}%n");
        print("%n");

        iprint("public %1$s(%2$s dataSource) {%n", simpleName, DataSource.class
                .getName());
        indent();
        iprint("super(new %1$s(), dataSource);%n", daoMeta.getConfigType());
        unindent();
        iprint("}%n");
        print("%n");

        iprint("protected %1$s(%2$s config) {%n", simpleName, Config.class
                .getName());
        indent();
        iprint("super(config, config.dataSource());%n");
        unindent();
        iprint("}%n");
        print("%n");
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
        if (m.getTypeParameterNames().hasNext()) {
            print("<");
            for (Iterator<String> it = m.getTypeParameterNames(); it.hasNext();) {
                print("%1$s", it.next());
                if (it.hasNext()) {
                    print(", ");
                }
            }
            print("> ");
        }
        print("%1$s %2$s(", m.getReturnTypeName(), m.getName());
        for (Iterator<Map.Entry<String, String>> it = m.getMethodParameters(); it
                .hasNext();) {
            Map.Entry<String, String> entry = it.next();
            print("%1$s %2$s", entry.getValue(), entry.getKey());
            if (it.hasNext()) {
                print(", ");
            }
        }
        print(") ");
        if (m.getThrownTypeNames().hasNext()) {
            print("throws ");
            for (Iterator<String> it = m.getThrownTypeNames(); it.hasNext();) {
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
            iprint("entering(\"%1$s\", \"%2$s\"", qualifiedName, m.getName());
            for (Iterator<Map.Entry<String, String>> it = m
                    .getMethodParameters(); it.hasNext();) {
                Map.Entry<String, String> entry = it.next();
                print(", %1$s", entry.getKey());
            }
            print(");%n");
            for (Iterator<Map.Entry<String, String>> it = m
                    .getMethodParameters(); it.hasNext();) {
                Map.Entry<String, String> entry = it.next();
                String paramName = entry.getKey();
                iprint("if (%1$s == null) {%n", paramName);
                iprint("    throw new %1$s(\"%2$s\");%n",
                        DomaNullPointerException.class.getName(), paramName);
                iprint("}%n");
            }
            iprint("%1$s query = new %1$s();%n", m.getQueryClass().getName());
            iprint("query.setConfig(config);%n");
            iprint(
                    "query.setSqlFilePath(%1$s.buildPath(\"%2$s\", \"%3$s\"));%n",
                    SqlFileUtil.class.getName(), daoMeta.getDaoElement()
                            .getQualifiedName(), m.getName());
            if (m.getOptionsName() != null) {
                iprint("query.setOptions(%1$s);%n", m.getOptionsName());
            }
            for (Iterator<Map.Entry<String, String>> it = m
                    .getMethodParameters(); it.hasNext();) {
                Map.Entry<String, String> entry = it.next();
                String paramName = entry.getKey();
                iprint("query.addParameter(\"%1$s\", %1$s);%n", paramName);
            }
            iprint("query.setCallerClassName(\"%1$s\");%n", qualifiedName);
            iprint("query.setCallerMethodName(\"%1$s\");%n", m.getName());
            if (m.getQueryTimeout() != null) {
                iprint("query.setQueryTimeout(%1$s);%n", m.getQueryTimeout());
            }
            if (m.getMaxRows() != null) {
                iprint("query.setMaxRows(%1$s);%n", m.getMaxRows());
            }
            if (m.getFetchSize() != null) {
                iprint("query.setFetchSize(%1$s);%n", m.getFetchSize());
            }
            iprint("query.compile();%n");
            if (m.isIterated()) {
                if (m.getEntityTypeName() != null) {
                    iprint(
                            "%1$s<%2$s> command = new %1$s<%2$s>(query, new %3$s<%2$s, %4$s, %4$s%5$s>(%4$s%5$s.class, %6$s));%n",
                            m.getCommandClass().getName(), m
                                    .getIterationCallbackResultType(),
                            EntityIterationHandler.class.getName(), m
                                    .getEntityTypeName(), entitySuffix, m
                                    .getIterationCallbackName());
                } else {
                    iprint(
                            "%1$s<%2$s> command = new %1$s<%2$s>(query, new %3$s<%2$s, %4$s>(%4$s.class, %5$s));%n",
                            m.getCommandClass().getName(), m
                                    .getReturnTypeName(),
                            DomainIterationHandler.class.getName(), m
                                    .getDomainTypeName(), m
                                    .getIterationCallbackName());
                }
                if ("void".equals(m.getReturnTypeName())) {
                    iprint("command.execute();%n");
                    iprint("exiting(\"%1$s\", \"%2$s\", null);%n",
                            qualifiedName, m.getName());
                } else {
                    iprint("%1$s result = command.execute();%n", m
                            .getReturnTypeName());
                    iprint("exiting(\"%1$s\", \"%2$s\", result);%n",
                            qualifiedName, m.getName());
                    iprint("return result;%n");
                }

            } else {
                if (m.getEntityTypeName() != null) {
                    Class<?> handlerClass = m.isSingleResult() ? EntitySingleResultHandler.class
                            : EntityResultListHandler.class;
                    iprint(
                            "%1$s<%2$s> command = new %1$s<%2$s>(query, new %3$s<%4$s, %4$s%5$s>(%4$s%5$s.class));%n",
                            m.getCommandClass().getName(), m
                                    .getReturnTypeName(), handlerClass
                                    .getName(), m.getEntityTypeName(),
                            entitySuffix);
                } else {
                    Class<?> handlerClass = m.isSingleResult() ? DomainSingleResultHandler.class
                            : DomainResultListHandler.class;
                    iprint(
                            "%1$s<%2$s> command = new %1$s<%2$s>(query, new %3$s<%4$s>(%4$s.class));%n",
                            m.getCommandClass().getName(), m
                                    .getReturnTypeName(), handlerClass
                                    .getName(), m.getDomainTypeName());
                }
                iprint("%1$s result = command.execute();%n", m
                        .getReturnTypeName());
                iprint("exiting(\"%1$s\", \"%2$s\", result);%n", qualifiedName,
                        m.getName());
                iprint("return result;%n");
            }
            return null;
        }

        @Override
        public Void visistAutoModifyQueryMeta(AutoModifyQueryMeta m, Void p) {
            iprint("entering(\"%1$s\", \"%2$s\", %3$s);%n", qualifiedName, m
                    .getName(), m.getEntityName());
            iprint("if (%1$s == null) {%n", m.getEntityName());
            iprint("    throw new %1$s(\"%2$s\");%n",
                    DomaNullPointerException.class.getName(), m.getEntityName());
            iprint("}%n");
            iprint(
                    "%1$s<%2$s, %2$s%3$s> query = new %1$s<%2$s, %2$s%3$s>(%2$s%3$s.class);%n",
                    m.getQueryClass().getName(), m.getEntityTypeName(),
                    entitySuffix);
            iprint("query.setConfig(config);%n");
            iprint("query.setEntity(%1$s);%n", m.getEntityName());
            iprint("query.setCallerClassName(\"%1$s\");%n", qualifiedName);
            iprint("query.setCallerMethodName(\"%1$s\");%n", m.getName());
            if (m.getQueryTimeout() != null) {
                iprint("query.setQueryTimeout(%1$s);%n", m.getQueryTimeout());
            }
            if (m.isNullExcluded() != null) {
                iprint("query.setNullExcluded(%1$s);%n", m.isNullExcluded());
            }
            if (m.isVersionIncluded() != null) {
                iprint("query.setVersionIncluded(%1$s);%n", m
                        .isVersionIncluded());
            }
            if (m.isVersionIgnored() != null) {
                iprint("query.setVersionIgnored(%1$s);%n", m.isVersionIgnored());
            }
            if (m.getIncludedPropertyNames() != null) {
                String s = formatStringArray(m.getIncludedPropertyNames());
                iprint("query.setIncludedPropertyNames(%1$s);%n", s);
            }
            if (m.getExcludedPropertyNames() != null) {
                String s = formatStringArray(m.getExcludedPropertyNames());
                iprint("query.setExcludedPropertyNames(%1$s);%n", s);
            }
            if (m.isUnchangedPropertyIncluded() != null) {
                iprint("query.setUnchangedPropertyIncluded(%1$s);%n", m
                        .isUnchangedPropertyIncluded());
            }
            if (m.isOptimisticLockExceptionSuppressed() != null) {
                iprint("query.setOptimisticLockExceptionSuppressed(%1$s);%n", m
                        .isOptimisticLockExceptionSuppressed());
            }
            iprint("query.compile();%n");
            iprint("%1$s command = new %1$s(query);%n", m.getCommandClass()
                    .getName());
            iprint("%1$s result = command.execute();%n", m.getReturnTypeName());
            iprint("exiting(\"%1$s\", \"%2$s\", result);%n", qualifiedName, m
                    .getName());
            iprint("return result;%n");
            return null;
        }

        @Override
        public Void visistSqlFileModifyQueryMeta(SqlFileModifyQueryMeta m,
                Void p) {
            iprint("entering(\"%1$s\", \"%2$s\"", qualifiedName, m.getName());
            for (Iterator<Map.Entry<String, String>> it = m
                    .getMethodParameters(); it.hasNext();) {
                Map.Entry<String, String> entry = it.next();
                print(", %1$s", entry.getKey());
            }
            print(");%n");

            for (Iterator<Map.Entry<String, String>> it = m
                    .getMethodParameters(); it.hasNext();) {
                Map.Entry<String, String> entry = it.next();
                String parameter = entry.getKey();
                iprint("if (%1$s == null) {%n", parameter);
                iprint("    throw new %1$s(\"%2$s\");%n",
                        DomaNullPointerException.class.getName(), parameter);
                iprint("}%n");
            }

            iprint("%1$s query = new %1$s();%n", m.getQueryClass().getName());
            iprint("query.setConfig(config);%n");
            iprint(
                    "query.setSqlFilePath(%1$s.buildPath(\"%2$s\", \"%3$s\"));%n",
                    SqlFileUtil.class.getName(), daoMeta.getDaoElement()
                            .getQualifiedName(), m.getName());
            for (Iterator<Map.Entry<String, String>> it = m
                    .getMethodParameters(); it.hasNext();) {
                Map.Entry<String, String> entry = it.next();
                iprint("query.addParameter(\"%1$s\", %1$s);%n", entry.getKey());
            }
            iprint("query.setCallerClassName(\"%1$s\");%n", qualifiedName);
            iprint("query.setCallerMethodName(\"%1$s\");%n", m.getName());
            if (m.getQueryTimeout() != null) {
                iprint("query.setQueryTimeout(%1$s);%n", m.getQueryTimeout());
            }
            iprint("query.compile();%n");
            iprint("%1$s command = new %1$s(query);%n", m.getCommandClass()
                    .getName());
            iprint("%1$s result = command.execute();%n", m.getReturnTypeName());
            iprint("exiting(\"%1$s\", \"%2$s\", result);%n", qualifiedName, m
                    .getName());
            iprint("return result;%n");
            return null;
        }

        @Override
        public Void visitAutoBatchModifyQueryMeta(AutoBatchModifyQueryMeta m,
                Void p) {
            iprint("entering(\"%1$s\", \"%2$s\", %3$s);%n", qualifiedName, m
                    .getName(), m.getEntityListName());
            iprint("if (%1$s == null) {%n", m.getEntityListName());
            iprint("    throw new %1$s(\"%2$s\");%n",
                    DomaNullPointerException.class.getName(), m
                            .getEntityListName());
            iprint("}%n");
            iprint(
                    "%1$s<%2$s, %2$s%3$s> query = new %1$s<%2$s, %2$s%3$s>(%2$s%3$s.class);%n",
                    m.getQueryClass().getName(), m.getElementTypeName(),
                    entitySuffix);
            iprint("query.setConfig(config);%n");
            iprint("query.setEntities(%1$s);%n", m.getEntityListName());
            iprint("query.setCallerClassName(\"%1$s\");%n", qualifiedName);
            iprint("query.setCallerMethodName(\"%1$s\");%n", m.getName());
            if (m.getQueryTimeout() != null) {
                iprint("query.setQueryTimeout(%1$s);%n", m.getQueryTimeout());
            }
            if (m.isVersionIncluded() != null) {
                iprint("query.setVersionIncluded(%1$s);%n", m
                        .isVersionIncluded());
            }
            if (m.isVersionIgnored() != null) {
                iprint("query.setVersionIgnored(%1$s);%n", m.isVersionIgnored());
            }
            if (m.getIncludedPropertyNames() != null) {
                String s = formatStringArray(m.getIncludedPropertyNames());
                iprint("query.setIncludedPropertyNames(%1$s);%n", s);
            }
            if (m.getExcludedPropertyNames() != null) {
                String s = formatStringArray(m.getExcludedPropertyNames());
                iprint("query.setExcludedPropertyNames(%1$s);%n", s);
            }
            if (m.isOptimisticLockExceptionSuppressed() != null) {
                iprint("query.setOptimisticLockExceptionSuppressed(%1$s);%n", m
                        .isOptimisticLockExceptionSuppressed());
            }
            iprint("query.compile();%n");
            iprint("%1$s command = new %1$s(query);%n", m.getCommandClass()
                    .getName());
            iprint("%1$s result = command.execute();%n", m.getReturnTypeName());
            iprint("exiting(\"%1$s\", \"%2$s\", result);%n", qualifiedName, m
                    .getName());
            iprint("return result;%n");
            return null;
        }

        @Override
        public Void visitSqlFileBatchModifyQueryMeta(
                SqlFileBatchModifyQueryMeta m, Void p) {
            iprint("entering(\"%1$s\", \"%2$s\", %3$s);%n", qualifiedName, m
                    .getName(), m.getEntityListName());
            iprint("if (%1$s == null) {%n", m.getEntityListName());
            iprint("    throw new %1$s(\"%2$s\");%n",
                    DomaNullPointerException.class.getName(), m
                            .getEntityListName());
            iprint("}%n");
            iprint(
                    "%1$s<%2$s, %2$s%3$s> query = new %1$s<%2$s, %2$s%3$s>(%2$s%3$s.class);%n",
                    m.getQueryClass().getName(), m.getElementTypeName(),
                    entitySuffix);
            iprint("query.setConfig(config);%n");
            iprint("query.setEntities(%1$s);%n", m.getEntityListName());
            iprint(
                    "query.setSqlFilePath(%1$s.buildPath(\"%2$s\", \"%3$s\"));%n",
                    SqlFileUtil.class.getName(), daoMeta.getDaoElement()
                            .getQualifiedName(), m.getName());
            iprint("query.setParameterName(\"%1$s\");%n", m.getEntityListName());
            iprint("query.setCallerClassName(\"%1$s\");%n", qualifiedName);
            iprint("query.setCallerMethodName(\"%1$s\");%n", m.getName());
            if (m.getQueryTimeout() != null) {
                iprint("query.setQueryTimeout(%1$s);%n", m.getQueryTimeout());
            }
            iprint("query.compile();%n");
            iprint("%1$s command = new %1$s(query);%n", m.getCommandClass()
                    .getName());
            iprint("%1$s result = command.execute();%n", m.getReturnTypeName());
            iprint("exiting(\"%1$s\", \"%2$s\", result);%n", qualifiedName, m
                    .getName());
            iprint("return result;%n");
            return null;
        }

        @Override
        public Void visitAutoFunctionQueryMeta(AutoFunctionQueryMeta m, Void p) {
            iprint("entering(\"%1$s\", \"%2$s\"", qualifiedName, m.getName());
            for (Iterator<CallableSqlParameterMeta> it = m
                    .getCallableSqlParameterMetas().iterator(); it.hasNext();) {
                CallableSqlParameterMeta parameterMeta = it.next();
                print(", %1$s", parameterMeta.getName());
            }
            print(");%n");
            for (Iterator<CallableSqlParameterMeta> it = m
                    .getCallableSqlParameterMetas().iterator(); it.hasNext();) {
                CallableSqlParameterMeta parameterMeta = it.next();
                iprint("if (%1$s == null) {%n", parameterMeta.getName());
                iprint("    throw new %1$s(\"%2$s\");%n",
                        DomaNullPointerException.class.getName(), parameterMeta
                                .getName());
                iprint("}%n");
            }
            iprint("%1$s<%2$s> query = new %1$s<%2$s>();%n", m.getQueryClass()
                    .getName(), m.getReturnTypeName());
            iprint("query.setConfig(config);%n");
            iprint("query.setFunctionName(\"%1$s\");%n", m.getFunctionName());
            AddCallableSqlParameterGenerator parameterGenerator = new AddCallableSqlParameterGenerator();
            m.getResultParameterMeta().accept(parameterGenerator, p);
            for (CallableSqlParameterMeta parameterMeta : m
                    .getCallableSqlParameterMetas()) {
                parameterMeta.accept(parameterGenerator, p);
            }
            iprint("query.setCallerClassName(\"%1$s\");%n", qualifiedName);
            iprint("query.setCallerMethodName(\"%1$s\");%n", m.getName());
            if (m.getQueryTimeout() != null) {
                iprint("query.setQueryTimeout(%1$s);%n", m.getQueryTimeout());
            }
            iprint("query.compile();%n");
            iprint("%1$s<%2$s> command = new %1$s<%2$s>(query);%n", m
                    .getCommandClass().getName(), m.getReturnTypeName());
            iprint("%1$s result = command.execute();%n", m.getReturnTypeName());
            iprint("exiting(\"%1$s\", \"%2$s\", result);%n", qualifiedName, m
                    .getName());
            iprint("return result;%n");
            return null;
        }

        @Override
        public Void visitAutoProcedureQueryMeta(AutoProcedureQueryMeta m, Void p) {
            iprint("entering(\"%1$s\", \"%2$s\"", qualifiedName, m.getName());
            for (Iterator<CallableSqlParameterMeta> it = m
                    .getCallableSqlParameterMetas().iterator(); it.hasNext();) {
                CallableSqlParameterMeta parameterMeta = it.next();
                print(", %1$s", parameterMeta.getName());
            }
            print(");%n");
            for (Iterator<CallableSqlParameterMeta> it = m
                    .getCallableSqlParameterMetas().iterator(); it.hasNext();) {
                CallableSqlParameterMeta parameterMeta = it.next();
                iprint("if (%1$s == null) {%n", parameterMeta.getName());
                iprint("    throw new %1$s(\"%2$s\");%n",
                        DomaNullPointerException.class.getName(), parameterMeta
                                .getName());
                iprint("}%n");
            }
            iprint("%1$s query = new %1$s();%n", m.getQueryClass().getName());
            iprint("query.setConfig(config);%n");
            iprint("query.setProcedureName(\"%1$s\");%n", m.getProcedureName());
            AddCallableSqlParameterGenerator parameterGenerator = new AddCallableSqlParameterGenerator();
            for (CallableSqlParameterMeta parameterMeta : m
                    .getCallableSqlParameterMetas()) {
                parameterMeta.accept(parameterGenerator, p);
            }
            iprint("query.setCallerClassName(\"%1$s\");%n", qualifiedName);
            iprint("query.setCallerMethodName(\"%1$s\");%n", m.getName());
            if (m.getQueryTimeout() != null) {
                iprint("query.setQueryTimeout(%1$s);%n", m.getQueryTimeout());
            }
            iprint("query.compile();%n");
            iprint("%1$s command = new %1$s(query);%n", m.getCommandClass()
                    .getName());
            iprint("command.execute();%n");
            iprint("exiting(\"%1$s\", \"%2$s\", null);%n", qualifiedName, m
                    .getName());
            return null;
        }

        @Override
        public Void visitAbstractCreateQueryMeta(AbstractCreateQueryMeta m,
                Void p) {
            iprint("entering(\"%1$s\", \"%2$s\");%n", qualifiedName, m
                    .getName());
            iprint("%1$s<%2$s> query = new %1$s<%2$s>();%n", m.getQueryClass()
                    .getName(), m.getReturnTypeName());
            iprint("query.setConfig(config);%n");
            iprint("query.setCallerClassName(\"%1$s\");%n", qualifiedName);
            iprint("query.setCallerMethodName(\"%1$s\");%n", m.getName());
            iprint("query.setResult(new %1$s());%n", m.getReturnTypeName());
            iprint("query.compile();%n");
            iprint("%1$s<%2$s> command = new %1$s<%2$s>(query);%n", m
                    .getCommandClass().getName(), m.getReturnTypeName());
            iprint("%1$s result = command.execute();%n", m.getReturnTypeName());
            iprint("exiting(\"%1$s\", \"%2$s\", result);%n", qualifiedName, m
                    .getName());
            iprint("return result;%n");
            return null;
        }

        @Override
        public Void visitArrayCreateQueryMeta(ArrayCreateQueryMeta m, Void p) {
            iprint("entering(\"%1$s\", \"%2$s\", (Object)%3$s);%n",
                    qualifiedName, m.getName(), m.getArrayName());
            iprint("if (%1$s == null) {%n", m.getArrayName());
            iprint("    throw new %1$s(\"%2$s\");%n",
                    DomaNullPointerException.class.getName(), m.getArrayName());
            iprint("}%n");
            iprint("%1$s<%2$s> query = new %1$s<%2$s>();%n", m.getQueryClass()
                    .getName(), m.getReturnTypeName());
            iprint("query.setConfig(config);%n");
            iprint("query.setCallerClassName(\"%1$s\");%n", qualifiedName);
            iprint("query.setCallerMethodName(\"%1$s\");%n", m.getName());
            iprint("query.setTypeName(\"%1$s\");%n", m.getJdbcTypeName());
            iprint("query.setElements(%1$s);%n", m.getArrayName());
            iprint("query.setResult(new %1$s());%n", m.getReturnTypeName());
            iprint("query.compile();%n");
            iprint("%1$s<%2$s> command = new %1$s<%2$s>(query);%n", m
                    .getCommandClass().getName(), m.getReturnTypeName());
            iprint("%1$s result = command.execute();%n", m.getReturnTypeName());
            iprint("exiting(\"%1$s\", \"%2$s\", result);%n", qualifiedName, m
                    .getName());
            iprint("return result;%n");
            return null;
        }

        @Override
        public Void visitDelegateQueryMeta(DelegateQueryMeta m, Void p) {
            iprint("entering(\"%1$s\", \"%2$s\");%n", qualifiedName, m
                    .getName());
            iprint("%1$s delegate = new %1$s(config);%n", m.getTargetType());
            if ("void".equals(m.getReturnTypeName())) {
                iprint("Object result = null;%n");
                iprint("");
            } else {
                iprint("%1$s result = ", m.getReturnTypeName());
            }
            print("delegate.%1$s(", m.getName());
            for (Iterator<Map.Entry<String, String>> it = m
                    .getMethodParameters(); it.hasNext();) {
                Map.Entry<String, String> entry = it.next();
                String parameter = entry.getKey();
                print("%1$s", parameter);
                if (it.hasNext()) {
                    print(", ");
                }
            }
            print(");%n");
            iprint("exiting(\"%1$s\", \"%2$s\", result);%n", qualifiedName, m
                    .getName());
            iprint("return result;%n");
            return null;
        }

    }

    protected class AddCallableSqlParameterGenerator implements
            CallableSqlParameterMetaVisitor<Void, Void> {

        @Override
        public Void visistDomainListParameterMeta(DomainListParameterMeta m,
                Void p) {
            iprint("query.addParameter(new %1$s(%2$s.class, %3$s));%n",
                    DomainListParameter.class.getName(), m.getDomainTypeName(),
                    m.getName());
            return null;
        }

        @Override
        public Void visistEntityListParameterMeta(EntityListParameterMeta m,
                Void p) {
            iprint(
                    "query.addParameter(new %1$s<%2$s, %2$s%3$s>(%2$s%3$s.class, %4$s));%n",
                    EntityListParameter.class.getName(), m.getEntityTypeName(),
                    entitySuffix, m.getName());
            return null;
        }

        @Override
        public Void visistInOutParameterMeta(InOutParameterMeta m, Void p) {
            iprint("query.addParameter(new %1$s(%2$s));%n",
                    InOutParameter.class.getName(), m.getName());
            return null;
        }

        @Override
        public Void visistOutParameterMeta(OutParameterMeta m, Void p) {
            iprint("query.addParameter(new %1$s(%2$s));%n", OutParameter.class
                    .getName(), m.getName());
            return null;
        }

        @Override
        public Void visitInParameterMeta(InParameterMeta m, Void p) {
            iprint("query.addParameter(new %1$s(%2$s));%n", InParameter.class
                    .getName(), m.getName());
            return null;
        }

        @Override
        public Void visistDomainResultParameterMeta(
                DomainResultParameterMeta m, Void p) {
            iprint("query.setResultParameter(new %1$s<%2$s>(%2$s.class));%n",
                    DomainResultParameter.class.getName(), m
                            .getDomainTypeName());
            return null;
        }

        @Override
        public Void visistDomainListResultParameterMeta(
                DomainListResultParameterMeta m, Void p) {
            iprint("query.setResultParameter(new %1$s<%2$s>(%2$s.class));%n",
                    DomainListResultParameter.class.getName(), m
                            .getDomainTypeName());
            return null;
        }

        @Override
        public Void visistEntityListResultParameterMeta(
                EntityListResultParameterMeta m, Void p) {
            iprint(
                    "query.setResultParameter(new %1$s<%2$s, %2$s%3$s>(%2$s%3$s.class));%n",
                    EntityListResultParameter.class.getName(), m
                            .getEntityTypeName(), entitySuffix);
            return null;
        }
    }
}
