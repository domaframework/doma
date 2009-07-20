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

import org.seasar.doma.DomaIllegalArgumentException;
import org.seasar.doma.internal.apt.meta.AbstractCreateQueryMeta;
import org.seasar.doma.internal.apt.meta.ArrayCreateQueryMeta;
import org.seasar.doma.internal.apt.meta.AutoBatchModifyQueryMeta;
import org.seasar.doma.internal.apt.meta.AutoFunctionQueryMeta;
import org.seasar.doma.internal.apt.meta.AutoModifyQueryMeta;
import org.seasar.doma.internal.apt.meta.AutoProcedureQueryMeta;
import org.seasar.doma.internal.apt.meta.CallableSqlParameterMeta;
import org.seasar.doma.internal.apt.meta.CallableSqlParameterMetaVisitor;
import org.seasar.doma.internal.apt.meta.DaoMeta;
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
import org.seasar.doma.internal.jdbc.sql.SqlFiles;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.DomaAbstractDao;

/**
 * 
 * @author taedium
 * 
 */
public class DaoGenerator extends AbstractGenerator {

    protected final DaoMeta daoMeta;

    public DaoGenerator(ProcessingEnvironment env, TypeElement daoElement,
            DaoMeta daoMeta) throws IOException {
        super(env, daoElement);
        assertNotNull(daoMeta);
        this.daoMeta = daoMeta;
    }

    public void generate() {
        printPackage();
        printClass();
    }

    protected void printPackage() {
        if (!packageName.isEmpty()) {
            print("package %1$s;%n", packageName);
            print("%n");
        }
    }

    protected void printClass() {
        printGenerated();
        if (daoMeta.getImplementedByElement().getQualifiedName()
                .contentEquals(DomaAbstractDao.class.getName())) {
            print("public class %1$s extends %2$s implements %3$s {%n", simpleName, DomaAbstractDao.class
                    .getName(), daoMeta.getDaoType());
        } else {
            print("public class %1$s extends %2$s {%n", simpleName, daoMeta
                    .getImplementedByType());
        }
        put("%n");
        indent();
        printFields();
        printConstructors();
        printMethods();
        unindent();
        put("}%n");
    }

    protected void printFields() {
    }

    protected void printConstructors() {
        print("public %1$s() {%n", simpleName);
        indent();
        print("super(new %1$s(), null);%n", daoMeta.getConfigType());
        unindent();
        print("}%n");
        put("%n");

        print("public %1$s(%2$s dataSource) {%n", simpleName, DataSource.class
                .getName());
        indent();
        print("super(new %1$s(), dataSource);%n", daoMeta.getConfigType());
        unindent();
        print("}%n");
        put("%n");

        print("protected %1$s(%2$s config) {%n", simpleName, Config.class
                .getName());
        indent();
        print("super(config, config.dataSource());%n");
        unindent();
        print("}%n");
        put("%n");
    }

    protected void printMethods() {
        MethodBodyGenerator generator = new MethodBodyGenerator();
        for (QueryMeta queryMeta : daoMeta.getQueryMetas()) {
            printMethod(generator, queryMeta);
        }
    }

    protected void printMethod(MethodBodyGenerator generator, QueryMeta m) {
        print("@Override%n");
        print("public ");
        if (m.getTypeParameterNames().hasNext()) {
            put("<");
            for (Iterator<String> it = m.getTypeParameterNames(); it.hasNext();) {
                put("%1$s", it.next());
                if (it.hasNext()) {
                    put(", ");
                }
            }
            put("> ");
        }
        put("%1$s %2$s(", m.getReturnTypeName(), m.getName());
        for (Iterator<Map.Entry<String, String>> it = m.getMethodParameters(); it
                .hasNext();) {
            Map.Entry<String, String> entry = it.next();
            put("%1$s %2$s", entry.getValue(), entry.getKey());
            if (it.hasNext()) {
                put(", ");
            }
        }
        put(") ");
        if (m.getThrownTypeNames().hasNext()) {
            put("throws ");
            for (Iterator<String> it = m.getThrownTypeNames(); it.hasNext();) {
                put("%1$s", it.next());
                if (it.hasNext()) {
                    put(", ");
                }
            }
            put(" ");
        }
        put("{%n");
        indent();
        m.accept(generator, null);
        unindent();
        print("}%n");
        put("%n");
    }

    protected class MethodBodyGenerator implements QueryMetaVisitor<Void, Void> {

        @Override
        public Void visistSqlFileSelectQueryMeta(SqlFileSelectQueryMeta m,
                Void p) {
            print("entering(\"%1$s\", \"%2$s\"", qualifiedName, m.getName());
            for (Iterator<Map.Entry<String, String>> it = m
                    .getMethodParameters(); it.hasNext();) {
                Map.Entry<String, String> entry = it.next();
                put(", %1$s", entry.getKey());
            }
            put(");%n");
            for (Iterator<Map.Entry<String, String>> it = m
                    .getMethodParameters(); it.hasNext();) {
                Map.Entry<String, String> entry = it.next();
                String paramName = entry.getKey();
                print("if (%1$s == null) {%n", paramName);
                print("    throw new %1$s(\"%2$s\", %2$s);%n", DomaIllegalArgumentException.class
                        .getName(), paramName);
                print("}%n");
            }
            print("%1$s query = new %1$s();%n", m.getQueryClass().getName());
            print("query.setConfig(config);%n");
            print("query.setSqlFilePath(%1$s.buildPath(\"%2$s\", \"%3$s\"));%n", SqlFiles.class
                    .getName(), daoMeta.getDaoElement().getQualifiedName(), m
                    .getName());
            if (m.getOptionsName() != null) {
                print("query.setOptions(%1$s);%n", m.getOptionsName());
            }
            for (Iterator<Map.Entry<String, String>> it = m
                    .getMethodParameters(); it.hasNext();) {
                Map.Entry<String, String> entry = it.next();
                String paramName = entry.getKey();
                print("query.addParameter(\"%1$s\", %1$s);%n", paramName);
            }
            print("query.setCallerClassName(\"%1$s\");%n", qualifiedName);
            print("query.setCallerMethodName(\"%1$s\");%n", m.getName());
            if (m.getQueryTimeout() != null) {
                print("query.setQueryTimeout(%1$s);%n", m.getQueryTimeout());
            }
            if (m.getMaxRows() != null) {
                print("query.setMaxRows(%1$s);%n", m.getMaxRows());
            }
            if (m.getFetchSize() != null) {
                print("query.setFetchSize(%1$s);%n", m.getFetchSize());
            }
            print("query.compile();%n");
            if (m.isIteration()) {
                if (m.getEntityTypeName() != null) {
                    print("%1$s<%2$s> command = new %1$s<%2$s>(query, new %3$s<%2$s, %4$s, %4$s%5$s>(%4$s%5$s.class, %6$s));%n", m
                            .getCommandClass().getName(), m
                            .getIterationCallbackResultType(), EntityIterationHandler.class
                            .getName(), m.getEntityTypeName(), Options
                            .getSuffix(env), m.getIterationCallbackName());
                } else {
                    print("%1$s<%2$s> command = new %1$s<%2$s>(query, new %3$s<%2$s, %4$s>(%4$s.class, %5$s));%n", m
                            .getCommandClass().getName(), m.getReturnTypeName(), DomainIterationHandler.class
                            .getName(), m.getDomainTypeName(), m
                            .getIterationCallbackName());
                }
                if ("void".equals(m.getReturnTypeName())) {
                    print("command.execute();%n");
                    print("exiting(\"%1$s\", \"%2$s\", null);%n", qualifiedName, m
                            .getName());
                } else {
                    print("%1$s result = command.execute();%n", m
                            .getReturnTypeName());
                    print("exiting(\"%1$s\", \"%2$s\", result);%n", qualifiedName, m
                            .getName());
                    print("return result;%n");
                }

            } else {
                if (m.getEntityTypeName() != null) {
                    Class<?> handlerClass = m.isSingleResult() ? EntitySingleResultHandler.class
                            : EntityResultListHandler.class;
                    print("%1$s<%2$s> command = new %1$s<%2$s>(query, new %3$s<%4$s, %4$s%5$s>(%4$s%5$s.class));%n", m
                            .getCommandClass().getName(), m.getReturnTypeName(), handlerClass
                            .getName(), m.getEntityTypeName(), Options
                            .getSuffix(env));
                } else {
                    Class<?> handlerClass = m.isSingleResult() ? DomainSingleResultHandler.class
                            : DomainResultListHandler.class;
                    print("%1$s<%2$s> command = new %1$s<%2$s>(query, new %3$s<%4$s>(%4$s.class));%n", m
                            .getCommandClass().getName(), m.getReturnTypeName(), handlerClass
                            .getName(), m.getDomainTypeName());
                }
                print("%1$s result = command.execute();%n", m
                        .getReturnTypeName());
                print("exiting(\"%1$s\", \"%2$s\", result);%n", qualifiedName, m
                        .getName());
                print("return result;%n");
            }
            return null;
        }

        @Override
        public Void visistAutoModifyQueryMeta(AutoModifyQueryMeta m, Void p) {
            print("entering(\"%1$s\", \"%2$s\", %3$s);%n", qualifiedName, m
                    .getName(), m.getEntityName());
            print("if (%1$s == null) {%n", m.getEntityName());
            print("    throw new %1$s(\"%2$s\", %2$s);%n", DomaIllegalArgumentException.class
                    .getName(), m.getEntityName());
            print("}%n");
            print("%1$s<%2$s, %2$s%3$s> query = new %1$s<%2$s, %2$s%3$s>(%2$s%3$s.class);%n", m
                    .getQueryClass().getName(), m.getEntityTypeName(), Options
                    .getSuffix(env));
            print("query.setConfig(config);%n");
            print("query.setEntity(%1$s);%n", m.getEntityName());
            print("query.setCallerClassName(\"%1$s\");%n", qualifiedName);
            print("query.setCallerMethodName(\"%1$s\");%n", m.getName());
            if (m.getQueryTimeout() != null) {
                print("query.setQueryTimeout(%1$s);%n", m.getQueryTimeout());
            }
            if (m.isNullExcluded() != null) {
                print("query.setNullExcluded(%1$s);%n", m.isNullExcluded());
            }
            if (m.isVersionIncluded() != null) {
                print("query.setVersionIncluded(%1$s);%n", m
                        .isVersionIncluded());
            }
            if (m.isVersionIgnored() != null) {
                print("query.setVersionIgnored(%1$s);%n", m.isVersionIgnored());
            }
            if (m.isOptimisticLockExceptionSuppressed() != null) {
                print("query.setOptimisticLockExceptionSuppressed(%1$s);%n", m
                        .isOptimisticLockExceptionSuppressed());
            }
            print("query.compile();%n");
            print("%1$s command = new %1$s(query);%n", m.getCommandClass()
                    .getName());
            print("%1$s result = command.execute();%n", m.getReturnTypeName());
            print("exiting(\"%1$s\", \"%2$s\", result);%n", qualifiedName, m
                    .getName());
            print("return result;%n");
            return null;
        }

        @Override
        public Void visistSqlFileModifyQueryMeta(SqlFileModifyQueryMeta m,
                Void p) {
            print("entering(\"%1$s\", \"%2$s\"", qualifiedName, m.getName());
            for (Iterator<Map.Entry<String, String>> it = m
                    .getMethodParameters(); it.hasNext();) {
                Map.Entry<String, String> entry = it.next();
                put(", %1$s", entry.getKey());
            }
            put(");%n");

            for (Iterator<Map.Entry<String, String>> it = m
                    .getMethodParameters(); it.hasNext();) {
                Map.Entry<String, String> entry = it.next();
                String parameter = entry.getKey();
                print("if (%1$s == null) {%n", parameter);
                print("    throw new %1$s(\"%2$s\", %2$s);%n", DomaIllegalArgumentException.class
                        .getName(), parameter);
                print("}%n");
            }

            print("%1$s query = new %1$s();%n", m.getQueryClass().getName());
            print("query.setConfig(config);%n");
            print("query.setSqlFilePath(%1$s.buildPath(\"%2$s\", \"%3$s\"));%n", SqlFiles.class
                    .getName(), daoMeta.getDaoElement().getQualifiedName(), m
                    .getName());
            for (Iterator<Map.Entry<String, String>> it = m
                    .getMethodParameters(); it.hasNext();) {
                Map.Entry<String, String> entry = it.next();
                print("query.addParameter(\"%1$s\", %1$s);%n", entry.getKey());
            }
            print("query.setCallerClassName(\"%1$s\");%n", qualifiedName);
            print("query.setCallerMethodName(\"%1$s\");%n", m.getName());
            if (m.getQueryTimeout() != null) {
                print("query.setQueryTimeout(%1$s);%n", m.getQueryTimeout());
            }
            print("query.compile();%n");
            print("%1$s command = new %1$s(query);%n", m.getCommandClass()
                    .getName());
            print("%1$s result = command.execute();%n", m.getReturnTypeName());
            print("exiting(\"%1$s\", \"%2$s\", result);%n", qualifiedName, m
                    .getName());
            print("return result;%n");
            return null;
        }

        @Override
        public Void visitAutoBatchModifyQueryMeta(AutoBatchModifyQueryMeta m,
                Void p) {
            print("entering(\"%1$s\", \"%2$s\", %3$s);%n", qualifiedName, m
                    .getName(), m.getEntityListName());
            print("if (%1$s == null) {%n", m.getEntityListName());
            print("    throw new %1$s(\"%2$s\", %2$s);%n", DomaIllegalArgumentException.class
                    .getName(), m.getEntityListName());
            print("}%n");
            print("%1$s<%2$s, %2$s%3$s> query = new %1$s<%2$s, %2$s%3$s>(%2$s%3$s.class);%n", m
                    .getQueryClass().getName(), m.getElementTypeName(), Options
                    .getSuffix(env));
            print("query.setConfig(config);%n");
            print("query.setEntities(%1$s);%n", m.getEntityListName());
            print("query.setCallerClassName(\"%1$s\");%n", qualifiedName);
            print("query.setCallerMethodName(\"%1$s\");%n", m.getName());
            if (m.getQueryTimeout() != null) {
                print("query.setQueryTimeout(%1$s);%n", m.getQueryTimeout());
            }
            if (m.isVersionIncluded() != null) {
                print("query.setVersionIncluded(%1$s);%n", m
                        .isVersionIncluded());
            }
            if (m.isVersionIgnored() != null) {
                print("query.setVersionIgnored(%1$s);%n", m.isVersionIgnored());
            }
            if (m.isOptimisticLockExceptionSuppressed() != null) {
                print("query.setOptimisticLockExceptionSuppressed(%1$s);%n", m
                        .isOptimisticLockExceptionSuppressed());
            }
            print("query.compile();%n");
            print("%1$s command = new %1$s(query);%n", m.getCommandClass()
                    .getName());
            print("%1$s result = command.execute();%n", m.getReturnTypeName());
            print("exiting(\"%1$s\", \"%2$s\", result);%n", qualifiedName, m
                    .getName());
            print("return result;%n");
            return null;
        }

        @Override
        public Void visitSqlFileBatchModifyQueryMeta(
                SqlFileBatchModifyQueryMeta m, Void p) {
            print("entering(\"%1$s\", \"%2$s\", %3$s);%n", qualifiedName, m
                    .getName(), m.getEntityListName());
            print("if (%1$s == null) {%n", m.getEntityListName());
            print("    throw new %1$s(\"%2$s\", %2$s);%n", DomaIllegalArgumentException.class
                    .getName(), m.getEntityListName());
            print("}%n");
            print("%1$s<%2$s, %2$s%3$s> query = new %1$s<%2$s, %2$s%3$s>(%2$s%3$s.class);%n", m
                    .getQueryClass().getName(), m.getElementTypeName(), Options
                    .getSuffix(env));
            print("query.setConfig(config);%n");
            print("query.setEntities(%1$s);%n", m.getEntityListName());
            print("query.setCallerClassName(\"%1$s\");%n", qualifiedName);
            print("query.setCallerMethodName(\"%1$s\");%n", m.getName());
            if (m.getQueryTimeout() != null) {
                print("query.setQueryTimeout(%1$s);%n", m.getQueryTimeout());
            }
            print("query.compile();%n");
            print("%1$s command = new %1$s(query);%n", m.getCommandClass()
                    .getName());
            print("%1$s result = command.execute();%n", m.getReturnTypeName());
            print("exiting(\"%1$s\", \"%2$s\", result);%n", qualifiedName, m
                    .getName());
            print("return result;%n");
            return null;
        }

        @Override
        public Void visitAutoFunctionQueryMeta(AutoFunctionQueryMeta m, Void p) {
            print("entering(\"%1$s\", \"%2$s\"", qualifiedName, m.getName());
            for (Iterator<CallableSqlParameterMeta> it = m
                    .getCallableSqlParameterMetas().iterator(); it.hasNext();) {
                CallableSqlParameterMeta parameterMeta = it.next();
                put(", %1$s", parameterMeta.getName());
            }
            put(");%n");
            for (Iterator<CallableSqlParameterMeta> it = m
                    .getCallableSqlParameterMetas().iterator(); it.hasNext();) {
                CallableSqlParameterMeta parameterMeta = it.next();
                print("if (%1$s == null) {%n", parameterMeta.getName());
                print("    throw new %1$s(\"%2$s\", %2$s);%n", DomaIllegalArgumentException.class
                        .getName(), parameterMeta.getName());
                print("}%n");
            }
            print("%1$s<%2$s> query = new %1$s<%2$s>();%n", m.getQueryClass()
                    .getName(), m.getReturnTypeName());
            print("query.setConfig(config);%n");
            print("query.setFunctionName(\"%1$s\");%n", m.getFunctionName());
            AddCallableSqlParameterGenerator parameterGenerator = new AddCallableSqlParameterGenerator();
            m.getResultParameterMeta().accept(parameterGenerator, p);
            for (CallableSqlParameterMeta parameterMeta : m
                    .getCallableSqlParameterMetas()) {
                parameterMeta.accept(parameterGenerator, p);
            }
            print("query.setCallerClassName(\"%1$s\");%n", qualifiedName);
            print("query.setCallerMethodName(\"%1$s\");%n", m.getName());
            if (m.getQueryTimeout() != null) {
                print("query.setQueryTimeout(%1$s);%n", m.getQueryTimeout());
            }
            print("query.compile();%n");
            print("%1$s<%2$s> command = new %1$s<%2$s>(query);%n", m
                    .getCommandClass().getName(), m.getReturnTypeName());
            print("%1$s result = command.execute();%n", m.getReturnTypeName());
            print("exiting(\"%1$s\", \"%2$s\", result);%n", qualifiedName, m
                    .getName());
            print("return result;%n");
            return null;
        }

        @Override
        public Void visitAutoProcedureQueryMeta(AutoProcedureQueryMeta m, Void p) {
            print("entering(\"%1$s\", \"%2$s\"", qualifiedName, m.getName());
            for (Iterator<CallableSqlParameterMeta> it = m
                    .getCallableSqlParameterMetas().iterator(); it.hasNext();) {
                CallableSqlParameterMeta parameterMeta = it.next();
                put(", %1$s", parameterMeta.getName());
            }
            put(");%n");
            for (Iterator<CallableSqlParameterMeta> it = m
                    .getCallableSqlParameterMetas().iterator(); it.hasNext();) {
                CallableSqlParameterMeta parameterMeta = it.next();
                print("if (%1$s == null) {%n", parameterMeta.getName());
                print("    throw new %1$s(\"%2$s\", %2$s);%n", DomaIllegalArgumentException.class
                        .getName(), parameterMeta.getName());
                print("}%n");
            }
            print("%1$s query = new %1$s();%n", m.getQueryClass().getName());
            print("query.setConfig(config);%n");
            print("query.setProcedureName(\"%1$s\");%n", m.getProcedureName());
            AddCallableSqlParameterGenerator parameterGenerator = new AddCallableSqlParameterGenerator();
            for (CallableSqlParameterMeta parameterMeta : m
                    .getCallableSqlParameterMetas()) {
                parameterMeta.accept(parameterGenerator, p);
            }
            print("query.setCallerClassName(\"%1$s\");%n", qualifiedName);
            print("query.setCallerMethodName(\"%1$s\");%n", m.getName());
            if (m.getQueryTimeout() != null) {
                print("query.setQueryTimeout(%1$s);%n", m.getQueryTimeout());
            }
            print("query.compile();%n");
            print("%1$s command = new %1$s(query);%n", m.getCommandClass()
                    .getName());
            print("command.execute();%n");
            print("exiting(\"%1$s\", \"%2$s\", null);%n", qualifiedName, m
                    .getName());
            return null;
        }

        @Override
        public Void visitAbstractCreateQueryMeta(AbstractCreateQueryMeta m,
                Void p) {
            print("entering(\"%1$s\", \"%2$s\");%n", qualifiedName, m.getName());
            print("%1$s<%2$s> query = new %1$s<%2$s>();%n", m.getQueryClass()
                    .getName(), m.getReturnTypeName());
            print("query.setConfig(config);%n");
            print("query.setCallerClassName(\"%1$s\");%n", qualifiedName);
            print("query.setCallerMethodName(\"%1$s\");%n", m.getName());
            print("query.setResult(new %1$s());%n", m.getReturnTypeName());
            print("query.compile();%n");
            print("%1$s<%2$s> command = new %1$s<%2$s>(query);%n", m
                    .getCommandClass().getName(), m.getReturnTypeName());
            print("%1$s result = command.execute();%n", m.getReturnTypeName());
            print("exiting(\"%1$s\", \"%2$s\", result);%n", qualifiedName, m
                    .getName());
            print("return result;%n");
            return null;
        }

        @Override
        public Void visitArrayCreateQueryMeta(ArrayCreateQueryMeta m, Void p) {
            print("entering(\"%1$s\", \"%2$s\", (Object)%3$s);%n", qualifiedName, m
                    .getName(), m.getArrayName());
            print("if (%1$s == null) {%n", m.getArrayName());
            print("    throw new %1$s(\"%2$s\", %2$s);%n", DomaIllegalArgumentException.class
                    .getName(), m.getArrayName());
            print("}%n");
            print("%1$s<%2$s> query = new %1$s<%2$s>();%n", m.getQueryClass()
                    .getName(), m.getReturnTypeName());
            print("query.setConfig(config);%n");
            print("query.setCallerClassName(\"%1$s\");%n", qualifiedName);
            print("query.setCallerMethodName(\"%1$s\");%n", m.getName());
            print("query.setTypeName(\"%1$s\");%n", m.getJdbcTypeName());
            print("query.setElements(%1$s);%n", m.getArrayName());
            print("query.setResult(new %1$s());%n", m.getReturnTypeName());
            print("query.compile();%n");
            print("%1$s<%2$s> command = new %1$s<%2$s>(query);%n", m
                    .getCommandClass().getName(), m.getReturnTypeName());
            print("%1$s result = command.execute();%n", m.getReturnTypeName());
            print("exiting(\"%1$s\", \"%2$s\", result);%n", qualifiedName, m
                    .getName());
            print("return result;%n");
            return null;
        }

    }

    protected class AddCallableSqlParameterGenerator implements
            CallableSqlParameterMetaVisitor<Void, Void> {

        @Override
        public Void visistDomainListParameterMeta(DomainListParameterMeta m,
                Void p) {
            print("query.addParameter(new %1$s(%2$s.class, %3$s));%n", DomainListParameter.class
                    .getName(), m.getDomainTypeName(), m.getName());
            return null;
        }

        @Override
        public Void visistEntityListParameterMeta(EntityListParameterMeta m,
                Void p) {
            print("query.addParameter(new %1$s<%2$s, %2$s%3$s>(%2$s%3$s.class, %4$s));%n", EntityListParameter.class
                    .getName(), m.getEntityTypeName(), Options.getSuffix(env), m
                    .getName());
            return null;
        }

        @Override
        public Void visistInOutParameterMeta(InOutParameterMeta m, Void p) {
            print("query.addParameter(new %1$s(%2$s));%n", InOutParameter.class
                    .getName(), m.getName());
            return null;
        }

        @Override
        public Void visistOutParameterMeta(OutParameterMeta m, Void p) {
            print("query.addParameter(new %1$s(%2$s));%n", OutParameter.class
                    .getName(), m.getName());
            return null;
        }

        @Override
        public Void visitInParameterMeta(InParameterMeta m, Void p) {
            print("query.addParameter(new %1$s(%2$s));%n", InParameter.class
                    .getName(), m.getName());
            return null;
        }

        @Override
        public Void visistDomainResultParameterMeta(
                DomainResultParameterMeta m, Void p) {
            print("query.setResultParameter(new %1$s<%2$s>(%2$s.class));%n", DomainResultParameter.class
                    .getName(), m.getDomainTypeName());
            return null;
        }

        @Override
        public Void visistDomainListResultParameterMeta(
                DomainListResultParameterMeta m, Void p) {
            print("query.setResultParameter(new %1$s<%2$s>(%2$s.class));%n", DomainListResultParameter.class
                    .getName(), m.getDomainTypeName());
            return null;
        }

        @Override
        public Void visistEntityListResultParameterMeta(
                EntityListResultParameterMeta m, Void p) {
            print("query.setResultParameter(new %1$s<%2$s, %2$s%3$s>(%2$s%3$s.class));%n", EntityListResultParameter.class
                    .getName(), m.getEntityTypeName(), Options.getSuffix(env));
            return null;
        }
    }
}
