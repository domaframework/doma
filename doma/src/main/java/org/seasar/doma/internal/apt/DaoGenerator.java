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
import org.seasar.doma.internal.apt.meta.ValueInOutParameterMeta;
import org.seasar.doma.internal.apt.meta.ValueInParameterMeta;
import org.seasar.doma.internal.apt.meta.ValueListParameterMeta;
import org.seasar.doma.internal.apt.meta.ValueListResultParameterMeta;
import org.seasar.doma.internal.apt.meta.ValueOutParameterMeta;
import org.seasar.doma.internal.apt.meta.ValueResultParameterMeta;
import org.seasar.doma.internal.apt.type.DomainType;
import org.seasar.doma.internal.apt.type.EntityType;
import org.seasar.doma.internal.apt.type.IterationCallbackType;
import org.seasar.doma.internal.apt.type.ListType;
import org.seasar.doma.internal.apt.type.ValueType;
import org.seasar.doma.internal.jdbc.command.DomainIterationHandler;
import org.seasar.doma.internal.jdbc.command.DomainResultListHandler;
import org.seasar.doma.internal.jdbc.command.DomainSingleResultHandler;
import org.seasar.doma.internal.jdbc.command.EntityIterationHandler;
import org.seasar.doma.internal.jdbc.command.EntityResultListHandler;
import org.seasar.doma.internal.jdbc.command.EntitySingleResultHandler;
import org.seasar.doma.internal.jdbc.command.ValueIterationHandler;
import org.seasar.doma.internal.jdbc.command.ValueResultListHandler;
import org.seasar.doma.internal.jdbc.command.ValueSingleResultHandler;
import org.seasar.doma.internal.jdbc.sql.DomainInOutParameter;
import org.seasar.doma.internal.jdbc.sql.DomainInParameter;
import org.seasar.doma.internal.jdbc.sql.DomainListParameter;
import org.seasar.doma.internal.jdbc.sql.DomainListResultParameter;
import org.seasar.doma.internal.jdbc.sql.DomainOutParameter;
import org.seasar.doma.internal.jdbc.sql.DomainResultParameter;
import org.seasar.doma.internal.jdbc.sql.EntityListParameter;
import org.seasar.doma.internal.jdbc.sql.EntityListResultParameter;
import org.seasar.doma.internal.jdbc.sql.SqlFileUtil;
import org.seasar.doma.internal.jdbc.sql.ValueInOutParameter;
import org.seasar.doma.internal.jdbc.sql.ValueInParameter;
import org.seasar.doma.internal.jdbc.sql.ValueListParameter;
import org.seasar.doma.internal.jdbc.sql.ValueListResultParameter;
import org.seasar.doma.internal.jdbc.sql.ValueOutParameter;
import org.seasar.doma.internal.jdbc.sql.ValueResultParameter;
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

            iprint("%1$s query = new %1$s();%n", m.getQueryClass().getName());
            iprint("query.setConfig(config);%n");
            iprint(
                    "query.setSqlFilePath(%1$s.buildPath(\"%2$s\", \"%3$s\"));%n",
                    SqlFileUtil.class.getName(), daoMeta.getDaoElement()
                            .getQualifiedName(), m.getName());
            if (m.getSelectOptionsType() != null) {
                iprint("query.setOptions(%1$s);%n", m
                        .getSelectOptionsParameterName());
            }
            for (Iterator<QueryParameterMeta> it = m.getParameterMetas()
                    .iterator(); it.hasNext();) {
                QueryParameterMeta parameterMeta = it.next();
                if (parameterMeta.isBindable()) {
                    iprint("query.addParameter(\"%1$s\", %2$s.class, %1$s);%n",
                            parameterMeta.getName(), parameterMeta
                                    .getQualifiedName());
                }
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
            iprint("query.prepare();%n");
            QueryReturnMeta resultMeta = m.getReturnMeta();
            String commandClassName = m.getCommandClass().getName();
            if (m.isIterated()) {
                IterationCallbackType callbackType = m
                        .getIterationCallbackType();
                if (callbackType.getEntityType() != null) {
                    EntityType entityType = callbackType.getEntityType();
                    iprint(
                            "%1$s<%2$s> command = new %1$s<%2$s>(query, new %3$s<%2$s, %4$s>(new %4$s%5$s(), %6$s));%n",
                            commandClassName, resultMeta.getTypeName(),
                            EntityIterationHandler.class.getName(), entityType
                                    .getTypeName(), entitySuffix, m
                                    .getIterationCallbackPrameterName());
                } else if (callbackType.getDomainType() != null) {
                    DomainType domainType = callbackType.getDomainType();
                    ValueType valueType = domainType.getValueType();
                    iprint(
                            "%1$s<%2$s> command = new %1$s<%2$s>(query, new %3$s<%2$s, %4$s, %5$s>(new %5$s%6$s(), %7$s));%n",
                            commandClassName, resultMeta.getTypeName(),
                            DomainIterationHandler.class.getName(), valueType
                                    .getTypeName(), domainType.getTypeName(),
                            domainSuffix, m.getIterationCallbackPrameterName());
                } else {
                    ValueType valueType = callbackType.getValueType();
                    iprint(
                            "%1$s<%2$s> command = new %1$s<%2$s>(query, new %3$s<%2$s, %4$s>(new %5$s(), %6$s));%n",
                            commandClassName, resultMeta.getTypeName(),
                            ValueIterationHandler.class.getName(), valueType
                                    .getTypeName(), valueType.getWrapperType()
                                    .getTypeName(), m
                                    .getIterationCallbackPrameterName());
                }
                if ("void".equals(resultMeta.getTypeName())) {
                    iprint("command.execute();%n");
                    iprint("exiting(\"%1$s\", \"%2$s\", null);%n",
                            qualifiedName, m.getName());
                } else {
                    iprint("%1$s result = command.execute();%n", resultMeta
                            .getTypeName());
                    iprint("exiting(\"%1$s\", \"%2$s\", result);%n",
                            qualifiedName, m.getName());
                    iprint("return result;%n");
                }
            } else {
                if (m.getReturnMeta().getCollectionType() != null) {
                    ListType listType = m.getReturnMeta().getCollectionType();
                    if (listType.getEntityType() != null) {
                        EntityType entityType = listType.getEntityType();
                        iprint(
                                "%1$s<%2$s> command = new %1$s<%2$s>(query, new %3$s<%4$s>(new %4$s%5$s()));%n",
                                commandClassName, listType.getTypeName(),
                                EntityResultListHandler.class.getName(),
                                entityType.getTypeName(), entitySuffix);
                    } else if (listType.getDomainType() != null) {
                        DomainType domainType = listType.getDomainType();
                        ValueType valueType = domainType.getValueType();
                        iprint(
                                "%1$s<%2$s> command = new %1$s<%2$s>(query, new %3$s<%4$s, %5$s>(new %5$s%6$s()));%n",
                                commandClassName, listType.getTypeName(),
                                DomainResultListHandler.class.getName(),
                                valueType.getTypeName(), domainType
                                        .getTypeName(), domainSuffix);
                    } else {
                        ValueType valueType = listType.getValueType();
                        iprint(
                                "%1$s<%2$s> command = new %1$s<%2$s>(query, new %3$s<%4$s>(new %5$s()));%n",
                                commandClassName, listType.getTypeName(),
                                ValueResultListHandler.class.getName(),
                                valueType.getTypeName(), valueType
                                        .getWrapperType().getTypeName());
                    }
                } else {
                    if (m.getReturnMeta().getEntityType() != null) {
                        EntityType entityType = m.getReturnMeta()
                                .getEntityType();
                        iprint(
                                "%1$s<%2$s> command = new %1$s<%2$s>(query, new %3$s<%2$s>(new %4$s%5$s()));%n",
                                commandClassName, entityType.getTypeName(),
                                EntitySingleResultHandler.class.getName(),
                                entityType.getTypeName(), entitySuffix);
                    } else if (m.getReturnMeta().getDomainType() != null) {
                        DomainType domainType = m.getReturnMeta()
                                .getDomainType();
                        ValueType valueType = domainType.getValueType();
                        iprint(
                                "%1$s<%2$s> command = new %1$s<%2$s>(query, new %3$s<%4$s, %2$s>(new %2$s%5$s()));%n",
                                commandClassName, domainType.getTypeName(),
                                DomainSingleResultHandler.class.getName(),
                                valueType.getTypeName(), domainSuffix);
                    } else {
                        ValueType valueType = m.getReturnMeta().getValueType();
                        iprint(
                                "%1$s<%2$s> command = new %1$s<%2$s>(query, new %3$s<%2$s>(new %4$s()));%n",
                                commandClassName, valueType.getTypeName(),
                                ValueSingleResultHandler.class.getName(),
                                valueType.getWrapperType().getTypeName());
                    }
                }
                iprint("%1$s result = command.execute();%n", resultMeta
                        .getTypeName());
                iprint("exiting(\"%1$s\", \"%2$s\", result);%n", qualifiedName,
                        m.getName());
                iprint("return result;%n");
            }
            return null;
        }

        @Override
        public Void visistAutoModifyQueryMeta(AutoModifyQueryMeta m, Void p) {
            printEnteringStatement(m);
            printPrerequisiteStatements(m);

            iprint("%1$s<%2$s> query = new %1$s<%2$s>(new %2$s%3$s());%n", m
                    .getQueryClass().getName(),
                    m.getEntityType().getTypeName(), entitySuffix);
            iprint("query.setConfig(config);%n");
            iprint("query.setEntity(%1$s);%n", m.getEntityParameterName());
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
            iprint("query.prepare();%n");
            iprint("%1$s command = new %1$s(query);%n", m.getCommandClass()
                    .getName());
            iprint("%1$s result = command.execute();%n", m.getReturnMeta()
                    .getTypeName());
            iprint("exiting(\"%1$s\", \"%2$s\", result);%n", qualifiedName, m
                    .getName());
            iprint("return result;%n");
            return null;
        }

        @Override
        public Void visistSqlFileModifyQueryMeta(SqlFileModifyQueryMeta m,
                Void p) {
            printEnteringStatement(m);
            printPrerequisiteStatements(m);

            iprint("%1$s query = new %1$s();%n", m.getQueryClass().getName());
            iprint("query.setConfig(config);%n");
            iprint(
                    "query.setSqlFilePath(%1$s.buildPath(\"%2$s\", \"%3$s\"));%n",
                    SqlFileUtil.class.getName(), daoMeta.getDaoElement()
                            .getQualifiedName(), m.getName());
            for (Iterator<QueryParameterMeta> it = m.getParameterMetas()
                    .iterator(); it.hasNext();) {
                QueryParameterMeta parameterMeta = it.next();
                if (parameterMeta.isBindable()) {
                    iprint("query.addParameter(\"%1$s\", %2$s.class, %1$s);%n",
                            parameterMeta.getName(), parameterMeta
                                    .getQualifiedName());
                }
            }
            iprint("query.setCallerClassName(\"%1$s\");%n", qualifiedName);
            iprint("query.setCallerMethodName(\"%1$s\");%n", m.getName());
            if (m.getQueryTimeout() != null) {
                iprint("query.setQueryTimeout(%1$s);%n", m.getQueryTimeout());
            }
            iprint("query.prepare();%n");
            iprint("%1$s command = new %1$s(query);%n", m.getCommandClass()
                    .getName());
            iprint("%1$s result = command.execute();%n", m.getReturnMeta()
                    .getTypeName());
            iprint("exiting(\"%1$s\", \"%2$s\", result);%n", qualifiedName, m
                    .getName());
            iprint("return result;%n");
            return null;
        }

        @Override
        public Void visitAutoBatchModifyQueryMeta(AutoBatchModifyQueryMeta m,
                Void p) {
            printEnteringStatement(m);
            printPrerequisiteStatements(m);

            iprint("%1$s<%2$s> query = new %1$s<%2$s>(new %2$s%3$s());%n", m
                    .getQueryClass().getName(),
                    m.getEntityType().getTypeName(), entitySuffix);
            iprint("query.setConfig(config);%n");
            iprint("query.setEntities(%1$s);%n", m.getEntitiesParameterName());
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
            iprint("query.prepare();%n");
            iprint("%1$s command = new %1$s(query);%n", m.getCommandClass()
                    .getName());
            iprint("%1$s result = command.execute();%n", m.getReturnMeta()
                    .getTypeName());
            iprint("exiting(\"%1$s\", \"%2$s\", result);%n", qualifiedName, m
                    .getName());
            iprint("return result;%n");
            return null;
        }

        @Override
        public Void visitSqlFileBatchModifyQueryMeta(
                SqlFileBatchModifyQueryMeta m, Void p) {
            printEnteringStatement(m);
            printPrerequisiteStatements(m);

            iprint("%1$s<%2$s> query = new %1$s<%2$s>(new %2$s%3$s());%n", m
                    .getQueryClass().getName(),
                    m.getEntityType().getTypeName(), entitySuffix);
            iprint("query.setConfig(config);%n");
            iprint("query.setEntities(%1$s);%n", m.getEntitiesParameterName());
            iprint(
                    "query.setSqlFilePath(%1$s.buildPath(\"%2$s\", \"%3$s\"));%n",
                    SqlFileUtil.class.getName(), daoMeta.getDaoElement()
                            .getQualifiedName(), m.getName());
            iprint("query.setParameterName(\"%1$s\");%n", m
                    .getEntitiesParameterName());
            iprint("query.setCallerClassName(\"%1$s\");%n", qualifiedName);
            iprint("query.setCallerMethodName(\"%1$s\");%n", m.getName());
            if (m.getQueryTimeout() != null) {
                iprint("query.setQueryTimeout(%1$s);%n", m.getQueryTimeout());
            }
            iprint("query.prepare();%n");
            iprint("%1$s command = new %1$s(query);%n", m.getCommandClass()
                    .getName());
            iprint("%1$s result = command.execute();%n", m.getReturnMeta()
                    .getTypeName());
            iprint("exiting(\"%1$s\", \"%2$s\", result);%n", qualifiedName, m
                    .getName());
            iprint("return result;%n");
            return null;
        }

        @Override
        public Void visitAutoFunctionQueryMeta(AutoFunctionQueryMeta m, Void p) {
            printEnteringStatement(m);
            printPrerequisiteStatements(m);

            QueryReturnMeta resultMeta = m.getReturnMeta();
            iprint("%1$s<%2$s> query = new %1$s<%2$s>();%n", m.getQueryClass()
                    .getName(), resultMeta.getTypeName());
            iprint("query.setConfig(config);%n");
            iprint("query.setFunctionName(\"%1$s\");%n", m.getFunctionName());
            CallableSqlParameterStatementGenerator parameterGenerator = new CallableSqlParameterStatementGenerator();
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
            iprint("query.prepare();%n");
            iprint("%1$s<%2$s> command = new %1$s<%2$s>(query);%n", m
                    .getCommandClass().getName(), resultMeta.getTypeName());
            iprint("%1$s result = command.execute();%n", resultMeta
                    .getTypeName());
            iprint("exiting(\"%1$s\", \"%2$s\", result);%n", qualifiedName, m
                    .getName());
            iprint("return result;%n");
            return null;
        }

        @Override
        public Void visitAutoProcedureQueryMeta(AutoProcedureQueryMeta m, Void p) {
            printEnteringStatement(m);
            printPrerequisiteStatements(m);

            iprint("%1$s query = new %1$s();%n", m.getQueryClass().getName());
            iprint("query.setConfig(config);%n");
            iprint("query.setProcedureName(\"%1$s\");%n", m.getProcedureName());
            CallableSqlParameterStatementGenerator parameterGenerator = new CallableSqlParameterStatementGenerator();
            for (CallableSqlParameterMeta parameterMeta : m
                    .getCallableSqlParameterMetas()) {
                parameterMeta.accept(parameterGenerator, p);
            }
            iprint("query.setCallerClassName(\"%1$s\");%n", qualifiedName);
            iprint("query.setCallerMethodName(\"%1$s\");%n", m.getName());
            if (m.getQueryTimeout() != null) {
                iprint("query.setQueryTimeout(%1$s);%n", m.getQueryTimeout());
            }
            iprint("query.prepare();%n");
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
            printEnteringStatement(m);
            printPrerequisiteStatements(m);

            QueryReturnMeta resultMeta = m.getReturnMeta();
            iprint("%1$s query = new %1$s();%n", m.getQueryClass().getName(),
                    resultMeta.getTypeName());
            iprint("query.setConfig(config);%n");
            iprint("query.setCallerClassName(\"%1$s\");%n", qualifiedName);
            iprint("query.setCallerMethodName(\"%1$s\");%n", m.getName());
            iprint("query.prepare();%n");
            iprint("%1$s<%2$s> command = new %1$s<%2$s>(query);%n", m
                    .getCommandClass().getName(), resultMeta.getTypeName());
            iprint("%1$s result = command.execute();%n", resultMeta
                    .getTypeName());
            iprint("exiting(\"%1$s\", \"%2$s\", result);%n", qualifiedName, m
                    .getName());
            iprint("return result;%n");
            return null;
        }

        @Override
        public Void visitArrayCreateQueryMeta(ArrayCreateQueryMeta m, Void p) {
            iprint("entering(\"%1$s\", \"%2$s\", (Object)%3$s);%n",
                    qualifiedName, m.getName(), m.getParameterName());
            printPrerequisiteStatements(m);

            QueryReturnMeta resultMeta = m.getReturnMeta();
            iprint("%1$s query = new %1$s();%n", m.getQueryClass().getName());
            iprint("query.setConfig(config);%n");
            iprint("query.setCallerClassName(\"%1$s\");%n", qualifiedName);
            iprint("query.setCallerMethodName(\"%1$s\");%n", m.getName());
            iprint("query.setTypeName(\"%1$s\");%n", m.getJdbcTypeName());
            iprint("query.setElements(%1$s);%n", m.getParameterName());
            iprint("query.prepare();%n");
            iprint("%1$s<%2$s> command = new %1$s<%2$s>(query);%n", m
                    .getCommandClass().getName(), resultMeta.getTypeName());
            iprint("%1$s result = command.execute();%n", resultMeta
                    .getTypeName());
            iprint("exiting(\"%1$s\", \"%2$s\", result);%n", qualifiedName, m
                    .getName());
            iprint("return result;%n");
            return null;
        }

        @Override
        public Void visitDelegateQueryMeta(DelegateQueryMeta m, Void p) {
            printEnteringStatement(m);

            iprint("%1$s delegate = new %1$s(config);%n", m.getTargetType());
            QueryReturnMeta resultMeta = m.getReturnMeta();
            if ("void".equals(resultMeta.getTypeName())) {
                iprint("Object result = null;%n");
                iprint("");
            } else {
                iprint("%1$s result = ", resultMeta.getTypeName());
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
            iprint("exiting(\"%1$s\", \"%2$s\", result);%n", qualifiedName, m
                    .getName());
            iprint("return result;%n");
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
        public Void visistValueListParameterMeta(ValueListParameterMeta m,
                Void p) {
            ValueType valueType = m.getValueType();
            iprint("query.addParameter(new %1$s<%2$s>(new %3$s(), %4$s));%n",
                    ValueListParameter.class.getName(),
                    valueType.getTypeName(), valueType.getWrapperType()
                            .getTypeName(), m.getName());
            return null;
        }

        @Override
        public Void visistDomainListParameterMeta(DomainListParameterMeta m,
                Void p) {
            DomainType domainType = m.getDomainType();
            ValueType valueType = domainType.getValueType();
            iprint(
                    "query.addParameter(new %1$s<%2$s, %3$s>(new %3$s%4$s(), %5$s));%n",
                    DomainListParameter.class.getName(), valueType
                            .getTypeName(), domainType.getTypeName(),
                    domainSuffix, m.getName());
            return null;
        }

        @Override
        public Void visistEntityListParameterMeta(EntityListParameterMeta m,
                Void p) {
            EntityType entityType = m.getEntityType();
            iprint(
                    "query.addParameter(new %1$s<%2$s>(new %2$s%3$s(), %4$s));%n",
                    EntityListParameter.class.getName(), entityType
                            .getTypeName(), entitySuffix, m.getName());
            return null;
        }

        @Override
        public Void visistValueInOutParameterMeta(ValueInOutParameterMeta m,
                Void p) {
            ValueType valueType = m.getValueType();
            iprint("query.addParameter(new %1$s<%2$s>(new %3$s(), %4$s));%n",
                    ValueInOutParameter.class.getName(), valueType
                            .getTypeName(), valueType.getWrapperType()
                            .getTypeName(), m.getName());
            return null;
        }

        @Override
        public Void visistDomainInOutParameterMeta(DomainInOutParameterMeta m,
                Void p) {
            DomainType domainType = m.getDomainType();
            ValueType valueType = domainType.getValueType();
            iprint(
                    "query.addParameter(new %1$s<%2$s, %3$s>(new %3$s%4$s(), %5$s));%n",
                    DomainInOutParameter.class.getName(), valueType
                            .getTypeName(), domainType.getTypeName(),
                    domainSuffix, m.getName());
            return null;
        }

        @Override
        public Void visistValueOutParameterMeta(ValueOutParameterMeta m, Void p) {
            ValueType valueType = m.getValueType();
            iprint("query.addParameter(new %1$s<%2$s>(new %3$s(), %4$s));%n",
                    ValueOutParameter.class.getName(), valueType.getTypeName(),
                    valueType.getWrapperType().getTypeName(), m.getName());
            return null;
        }

        @Override
        public Void visistDomainOutParameterMeta(DomainOutParameterMeta m,
                Void p) {
            DomainType domainType = m.getDomainType();
            ValueType valueType = domainType.getValueType();
            iprint(
                    "query.addParameter(new %1$s<%2$s, %3$s>(new %3$s%4$s(), %5$s));%n",
                    DomainOutParameter.class.getName(),
                    valueType.getTypeName(), domainType.getTypeName(),
                    domainSuffix, m.getName());
            return null;
        }

        @Override
        public Void visitValueInParameterMeta(ValueInParameterMeta m, Void p) {
            ValueType valueType = m.getValueType();
            iprint("query.addParameter(new %1$s(new %2$s(%3$s)));%n",
                    ValueInParameter.class.getName(), valueType
                            .getWrapperType().getTypeName(), m.getName());
            return null;
        }

        @Override
        public Void visitDomainInParameterMeta(DomainInParameterMeta m, Void p) {
            DomainType domainType = m.getDomainType();
            ValueType valueType = domainType.getValueType();
            iprint(
                    "query.addParameter(new %1$s<%2$s, %3$s>(new %3$s%4$s(), %5$s));%n",
                    DomainInParameter.class.getName(), valueType.getTypeName(),
                    domainType.getTypeName(), domainSuffix, m.getName());
            return null;
        }

        @Override
        public Void visistValueListResultParameterMeta(
                ValueListResultParameterMeta m, Void p) {
            ValueType valueType = m.getValueType();
            iprint("query.setResultParameter(new %1$s<%2$s>(new %3$s()));%n",
                    ValueListResultParameter.class.getName(), valueType
                            .getTypeName(), valueType.getWrapperType()
                            .getTypeName());
            return null;
        }

        @Override
        public Void visistDomainListResultParameterMeta(
                DomainListResultParameterMeta m, Void p) {
            DomainType domainType = m.getDomainType();
            ValueType valueType = domainType.getValueType();
            iprint(
                    "query.setResultParameter(new %1$s<%2$s, %3$s>(new %3$s%4$s()));%n",
                    DomainListResultParameter.class.getName(), valueType
                            .getTypeName(), domainType.getTypeName(),
                    domainSuffix);
            return null;
        }

        @Override
        public Void visistEntityListResultParameterMeta(
                EntityListResultParameterMeta m, Void p) {
            EntityType entityType = m.getEntityType();
            iprint(
                    "query.setResultParameter(new %1$s<%2$s>(new %2$s%3$s()));%n",
                    EntityListResultParameter.class.getName(), entityType
                            .getTypeName(), entitySuffix);
            return null;
        }

        @Override
        public Void visistValueResultParameterMeta(ValueResultParameterMeta m,
                Void p) {
            ValueType valueType = m.getValueType();
            iprint("query.setResultParameter(new %1$s<%2$s>(new %3$s()));%n",
                    ValueResultParameter.class.getName(), valueType
                            .getTypeName(), valueType.getWrapperType()
                            .getTypeName());
            return null;
        }

        @Override
        public Void visistDomainResultParameterMeta(
                DomainResultParameterMeta m, Void p) {
            DomainType domainType = m.getDomainType();
            ValueType valueType = domainType.getValueType();
            iprint(
                    "query.setResultParameter(new %1$s<%2$s, %3$s>(new %3$s%4$s()));%n",
                    DomainResultParameter.class.getName(), valueType
                            .getTypeName(), domainType.getTypeName(),
                    domainSuffix);
            return null;
        }
    }
}
