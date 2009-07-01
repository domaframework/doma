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

import static org.seasar.doma.internal.util.Assertions.*;

import java.util.Iterator;
import java.util.Map;

import javax.annotation.Generated;
import javax.annotation.processing.ProcessingEnvironment;
import javax.sql.DataSource;

import org.seasar.doma.DomaIllegalArgumentException;
import org.seasar.doma.internal.ProductInfo;
import org.seasar.doma.internal.apt.meta.ArrayCreateQueryMeta;
import org.seasar.doma.internal.apt.meta.AutoBatchModifyQueryMeta;
import org.seasar.doma.internal.apt.meta.AutoFunctionQueryMeta;
import org.seasar.doma.internal.apt.meta.AutoModifyQueryMeta;
import org.seasar.doma.internal.apt.meta.AutoProcedureQueryMeta;
import org.seasar.doma.internal.apt.meta.CallableStatementParameterMeta;
import org.seasar.doma.internal.apt.meta.CallableStatementParameterMetaVisitor;
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
public class DaoGenerator implements Generator,
        QueryMetaVisitor<Void, Printer>,
        CallableStatementParameterMetaVisitor<Void, Printer> {

    protected final ProcessingEnvironment env;

    protected final String daoImplQualifiedName;

    protected final String daoImplPackageName;

    protected final String daoImplSimpleName;

    protected final DaoMeta daoMeta;

    public DaoGenerator(ProcessingEnvironment env, String daoQualifiedName,
            DaoMeta daoMeta) {
        assertNotNull(env, daoQualifiedName, daoMeta);
        this.env = env;
        this.daoImplQualifiedName = daoQualifiedName;
        int pos = daoQualifiedName.lastIndexOf('.');
        if (pos > -1) {
            this.daoImplPackageName = daoQualifiedName.substring(0, pos);
            this.daoImplSimpleName = daoQualifiedName.substring(pos + 1);
        } else {
            this.daoImplPackageName = "";
            this.daoImplSimpleName = daoQualifiedName;
        }
        this.daoMeta = daoMeta;
    }

    public void generate(Printer p) {
        doPackage(p);
        doClass(p);
    }

    protected void doPackage(Printer p) {
        if (!daoImplPackageName.isEmpty()) {
            p.p("package %s;%n", daoImplPackageName);
            p.p("%n");
        }
    }

    protected void doClass(Printer p) {
        p
                .p("@%s(value = { \"%s\", \"%s\" }, date = \"%tF %<tT\")%n", Generated.class
                        .getName(), ProductInfo.getName(), ProductInfo
                        .getVersion(), Options.getDate(env));
        if (daoMeta.getImplementedByElement().getQualifiedName()
                .contentEquals(DomaAbstractDao.class.getName())) {
            p
                    .p("public class %1$s extends %2$s implements %3$s {%n", daoImplSimpleName, DomaAbstractDao.class
                            .getName(), daoMeta.getDaoType());
        } else {
            p
                    .p("public class %1$s extends %2$s {%n", daoImplSimpleName, daoMeta
                            .getImplementedByType());
        }
        p.p("%n");
        p.indent();
        doFields(p);
        doConstructors(p);
        doMethods(p);
        p.unindent();
        p.p("}%n");
    }

    protected void doFields(Printer p) {
    }

    protected void doConstructors(Printer p) {
        p.p("public %s() {%n", daoImplSimpleName);
        p.indent();
        p.p("super(new %s(), null);%n", daoMeta.getConfigType());
        p.unindent();
        p.p("}%n");
        p.p("%n");

        p.p("public %s(%s dataSource) {%n", daoImplSimpleName, DataSource.class
                .getName());
        p.indent();
        p.p("super(new %s(), dataSource);%n", daoMeta.getConfigType());
        p.unindent();
        p.p("}%n");
        p.p("%n");

        p.p("protected %s(%s config) {%n", daoImplSimpleName, Config.class
                .getName());
        p.indent();
        p.p("super(config, config.dataSource());%n");
        p.unindent();
        p.p("}%n");
        p.p("%n");
    }

    protected void doMethods(Printer p) {
        for (QueryMeta queryMeta : daoMeta.getQueryMetas()) {
            queryMeta.accept(this, p);
            p.p("%n");
        }
    }

    @Override
    public Void visistAutoModifyQueryMeta(AutoModifyQueryMeta m, Printer p) {
        p.p("@Override%n");
        p.p("public ");
        if (m.getTypeParameterNames().hasNext()) {
            p.pp("<");
            for (Iterator<String> it = m.getTypeParameterNames(); it.hasNext();) {
                p.pp("%s", it.next());
                if (it.hasNext()) {
                    p.pp(", ");
                }
            }
            p.pp("> ");
        }
        p.pp("%s %s(", m.getReturnTypeName(), m.getName());
        p.pp("%s %s) ", m.getEntityTypeName(), m.getEntityName());
        if (m.getThrownTypeNames().hasNext()) {
            p.pp("throws ");
            for (Iterator<String> it = m.getThrownTypeNames(); it.hasNext();) {
                p.pp("%s", it.next());
                if (it.hasNext()) {
                    p.pp(", ");
                }
            }
            p.pp(" ");
        }
        p.pp("{%n");
        p.indent();

        p.p("entering(\"%1$s\", \"%2$s\", %3$s);%n", daoImplQualifiedName, m
                .getName(), m.getEntityName());
        p.p("if (%s == null) {%n", m.getEntityName());
        p
                .p("    throw new %1$s(\"%2$s\", %2$s);%n", DomaIllegalArgumentException.class
                        .getName(), m.getEntityName());
        p.p("}%n");
        p
                .p("%1$s<%2$s, %2$s%3$s> query = new %1$s<%2$s, %2$s%3$s>(%2$s%3$s.class);%n", m
                        .getQueryClass().getName(), m.getEntityTypeName(), Options
                        .getSuffix(env));
        p.p("query.setConfig(config);%n");
        p.p("query.setEntity(%1$s);%n", m.getEntityName());
        p.p("query.setCallerClassName(\"%s\");%n", daoImplQualifiedName);
        p.p("query.setCallerMethodName(\"%s\");%n", m.getName());
        if (m.getQueryTimeout() != null) {
            p.p("query.setQueryTimeout(%1$s);%n", m.getQueryTimeout());
        }
        if (m.isNullExcluded() != null) {
            p.p("query.setNullExcluded(%1$s);%n", m.isNullExcluded());
        }
        if (m.isVersionIncluded() != null) {
            p.p("query.setVersionIncluded(%1$s);%n", m.isVersionIncluded());
        }
        if (m.isVersionIgnored() != null) {
            p.p("query.setVersionIgnored(%1$s);%n", m.isVersionIgnored());
        }
        if (m.isOptimisticLockExceptionSuppressed() != null) {
            p.p("query.setOptimisticLockExceptionSuppressed(%1$s);%n", m
                    .isOptimisticLockExceptionSuppressed());
        }
        p.p("query.compile();%n");
        p.p("%1$s command = new %s(query);%n", m.getCommandClass().getName());
        p.p("%1$s result = command.execute();%n", m.getReturnTypeName());
        p.p("exiting(\"%1$s\", \"%2$s\", result);%n", daoImplQualifiedName, m
                .getName());
        p.p("return result;%n");
        p.unindent();
        p.p("}%n");
        return null;
    }

    @Override
    public Void visistSqlFileModifyQueryMeta(SqlFileModifyQueryMeta m, Printer p) {
        p.p("@Override%n");
        p.p("public ");
        if (m.getTypeParameterNames().hasNext()) {
            p.pp("<");
            for (Iterator<String> it = m.getTypeParameterNames(); it.hasNext();) {
                p.pp("%s", it.next());
                if (it.hasNext()) {
                    p.pp(", ");
                }
            }
            p.pp("> ");
        }
        p.pp("%s %s(", m.getReturnTypeName(), m.getName());
        for (Iterator<Map.Entry<String, String>> it = m.getParameters(); it
                .hasNext();) {
            Map.Entry<String, String> entry = it.next();
            p.pp("%s %s", entry.getValue(), entry.getKey());
            if (it.hasNext()) {
                p.pp(", ");
            }
        }
        p.pp(") ");
        if (m.getThrownTypeNames().hasNext()) {
            p.pp("throws ");
            for (Iterator<String> it = m.getThrownTypeNames(); it.hasNext();) {
                p.pp("%s", it.next());
                if (it.hasNext()) {
                    p.pp(", ");
                }
            }
            p.pp(" ");
        }
        p.pp("{%n");
        p.indent();

        p.p("entering(\"%1$s\", \"%2$s\"", daoImplQualifiedName, m.getName());
        for (Iterator<Map.Entry<String, String>> it = m.getParameters(); it
                .hasNext();) {
            Map.Entry<String, String> entry = it.next();
            p.pp(", %s", entry.getKey());
        }
        p.pp(");%n");

        for (Iterator<Map.Entry<String, String>> it = m.getParameters(); it
                .hasNext();) {
            Map.Entry<String, String> entry = it.next();
            String parameter = entry.getKey();
            p.p("if (%s == null) {%n", parameter);
            p
                    .p("    throw new %1$s(\"%2$s\", %2$s);%n", DomaIllegalArgumentException.class
                            .getName(), parameter);
            p.p("}%n");
        }

        p.p("%1$s query = new %1$s();%n", m.getQueryClass().getName());
        p.p("query.setConfig(config);%n");
        p
                .p("query.setSqlFilePath(%1$s.buildPath(\"%2$s\", \"%3$s\"));%n", SqlFiles.class
                        .getName(), daoMeta.getDaoElement().getQualifiedName(), m
                        .getName());
        for (Iterator<Map.Entry<String, String>> it = m.getParameters(); it
                .hasNext();) {
            Map.Entry<String, String> entry = it.next();
            p.p("query.addParameter(\"%1$s\", %1$s);%n", entry.getKey());
        }
        p.p("query.setCallerClassName(\"%s\");%n", daoImplQualifiedName);
        p.p("query.setCallerMethodName(\"%s\");%n", m.getName());
        if (m.getQueryTimeout() != null) {
            p.p("query.setQueryTimeout(%1$s);%n", m.getQueryTimeout());
        }
        p.p("query.compile();%n");
        p.p("%1$s command = new %s(query);%n", m.getCommandClass().getName());
        p.p("%1$s result = command.execute();%n", m.getReturnTypeName());
        p.p("exiting(\"%1$s\", \"%2$s\", result);%n", daoImplQualifiedName, m
                .getName());
        p.p("return result;%n");
        p.unindent();
        p.p("}%n");
        return null;
    }

    @Override
    public Void visistSqlFileSelectQueryMeta(SqlFileSelectQueryMeta m, Printer p) {
        p.p("@Override%n");
        p.p("public ");
        if (m.getTypeParameterNames().hasNext()) {
            p.pp("<");
            for (Iterator<String> it = m.getTypeParameterNames(); it.hasNext();) {
                p.pp("%s", it.next());
                if (it.hasNext()) {
                    p.pp(", ");
                }
            }
            p.pp("> ");
        }
        p.pp("%s %s(", m.getReturnTypeName(), m.getName());
        for (Iterator<Map.Entry<String, String>> it = m.getParameters(); it
                .hasNext();) {
            Map.Entry<String, String> entry = it.next();
            p.pp("%s %s", entry.getValue(), entry.getKey());
            if (it.hasNext()) {
                p.pp(", ");
            }
        }
        p.pp(") ");
        if (m.getThrownTypeNames().hasNext()) {
            p.pp("throws ");
            for (Iterator<String> it = m.getThrownTypeNames(); it.hasNext();) {
                p.pp("%s", it.next());
                if (it.hasNext()) {
                    p.pp(", ");
                }
            }
            p.pp(" ");
        }
        p.pp("{%n");
        p.indent();

        p.p("entering(\"%1$s\", \"%2$s\"", daoImplQualifiedName, m.getName());
        for (Iterator<Map.Entry<String, String>> it = m.getParameters(); it
                .hasNext();) {
            Map.Entry<String, String> entry = it.next();
            p.pp(", %s", entry.getKey());
        }
        p.pp(");%n");
        for (Iterator<Map.Entry<String, String>> it = m.getParameters(); it
                .hasNext();) {
            Map.Entry<String, String> entry = it.next();
            String paramName = entry.getKey();
            p.p("if (%s == null) {%n", paramName);
            p
                    .p("    throw new %1$s(\"%2$s\", %2$s);%n", DomaIllegalArgumentException.class
                            .getName(), paramName);
            p.p("}%n");
        }
        p.p("%1$s query = new %1$s();%n", m.getQueryClass().getName());
        p.p("query.setConfig(config);%n");
        p
                .p("query.setSqlFilePath(%1$s.buildPath(\"%2$s\", \"%3$s\"));%n", SqlFiles.class
                        .getName(), daoMeta.getDaoElement().getQualifiedName(), m
                        .getName());
        if (m.getOptionsName() != null) {
            p.p("query.setOptions(%1$s);%n", m.getOptionsName());
        }
        for (Iterator<Map.Entry<String, String>> it = m.getParameters(); it
                .hasNext();) {
            Map.Entry<String, String> entry = it.next();
            String paramName = entry.getKey();
            p.p("query.addParameter(\"%1$s\", %1$s);%n", paramName);
        }
        p.p("query.setCallerClassName(\"%s\");%n", daoImplQualifiedName);
        p.p("query.setCallerMethodName(\"%s\");%n", m.getName());
        if (m.getQueryTimeout() != null) {
            p.p("query.setQueryTimeout(%1$s);%n", m.getQueryTimeout());
        }
        if (m.getMaxRows() != null) {
            p.p("query.setMaxRows(%1$s);%n", m.getMaxRows());
        }
        if (m.getFetchSize() != null) {
            p.p("query.setFetchSize(%1$s);%n", m.getFetchSize());
        }
        p.p("query.compile();%n");
        if (m.isIteration()) {
            if (m.getEntityTypeName() != null) {
                p
                        .p("%1$s<%2$s> command = new %1$s<%2$s>(query, new %3$s<%2$s, %4$s, %4$s%5$s>(%4$s%5$s.class, %6$s));%n", m
                                .getCommandClass().getName(), m
                                .getIterationCallbackResultType(), EntityIterationHandler.class
                                .getName(), m.getEntityTypeName(), Options
                                .getSuffix(env), m.getIterationCallbackName());
            } else {
                p
                        .p("%1$s<%2$s> command = new %1$s<%2$s>(query, new %3$s<%2$s, %4$s>(%4$s.class, %5$s));%n", m
                                .getCommandClass().getName(), m
                                .getReturnTypeName(), DomainIterationHandler.class
                                .getName(), m.getDomainTypeName(), m
                                .getIterationCallbackName());
            }
            if ("void".equals(m.getReturnTypeName())) {
                p.p("command.execute();%n");
                p
                        .p("exiting(\"%1$s\", \"%2$s\", null);%n", daoImplQualifiedName, m
                                .getName());
            } else {
                p
                        .p("%1$s result = command.execute();%n", m
                                .getReturnTypeName());
                p
                        .p("exiting(\"%1$s\", \"%2$s\", result);%n", daoImplQualifiedName, m
                                .getName());
                p.p("return result;%n");
            }

        } else {
            if (m.getEntityTypeName() != null) {
                Class<?> handlerClass = m.isSingleResult() ? EntitySingleResultHandler.class
                        : EntityResultListHandler.class;
                p
                        .p("%1$s<%2$s> command = new %1$s<%2$s>(query, new %3$s<%4$s, %4$s%5$s>(%4$s%5$s.class));%n", m
                                .getCommandClass().getName(), m
                                .getReturnTypeName(), handlerClass.getName(), m
                                .getEntityTypeName(), Options.getSuffix(env));
            } else {
                Class<?> handlerClass = m.isSingleResult() ? DomainSingleResultHandler.class
                        : DomainResultListHandler.class;
                p
                        .p("%1$s<%2$s> command = new %1$s<%2$s>(query, new %3$s<%4$s>(%4$s.class));%n", m
                                .getCommandClass().getName(), m
                                .getReturnTypeName(), handlerClass.getName(), m
                                .getDomainTypeName());
            }
            p.p("%1$s result = command.execute();%n", m.getReturnTypeName());
            p
                    .p("exiting(\"%1$s\", \"%2$s\", result);%n", daoImplQualifiedName, m
                            .getName());
            p.p("return result;%n");
        }
        p.unindent();
        p.p("}%n");
        return null;
    }

    @Override
    public Void visitAutoBatchModifyQueryMeta(AutoBatchModifyQueryMeta m,
            Printer p) {
        p.p("@Override%n");
        p.p("public ");
        if (m.getTypeParameterNames().hasNext()) {
            p.pp("<");
            for (Iterator<String> it = m.getTypeParameterNames(); it.hasNext();) {
                p.pp("%s", it.next());
                if (it.hasNext()) {
                    p.pp(", ");
                }
            }
            p.pp("> ");
        }
        p.pp("%s %s(", m.getReturnTypeName(), m.getName());
        p.pp("%s %s) ", m.getEntityListTypeName(), m.getEntityListName());
        if (m.getThrownTypeNames().hasNext()) {
            p.pp("throws ");
            for (Iterator<String> it = m.getThrownTypeNames(); it.hasNext();) {
                p.pp("%s", it.next());
                if (it.hasNext()) {
                    p.pp(", ");
                }
            }
            p.pp(" ");
        }
        p.pp("{%n");
        p.indent();

        p.p("entering(\"%1$s\", \"%2$s\", %3$s);%n", daoImplQualifiedName, m
                .getName(), m.getEntityListName());
        p.p("if (%s == null) {%n", m.getEntityListName());
        p
                .p("    throw new %1$s(\"%2$s\", %2$s);%n", DomaIllegalArgumentException.class
                        .getName(), m.getEntityListName());
        p.p("}%n");
        p
                .p("%1$s<%2$s, %2$s%3$s> query = new %1$s<%2$s, %2$s%3$s>(%2$s%3$s.class);%n", m
                        .getQueryClass().getName(), m.getElementTypeName(), Options
                        .getSuffix(env));
        p.p("query.setConfig(config);%n");
        p.p("query.setEntities(%1$s);%n", m.getEntityListName());
        p.p("query.setCallerClassName(\"%s\");%n", daoImplQualifiedName);
        p.p("query.setCallerMethodName(\"%s\");%n", m.getName());
        if (m.getQueryTimeout() != null) {
            p.p("query.setQueryTimeout(%1$s);%n", m.getQueryTimeout());
        }
        if (m.isVersionIncluded() != null) {
            p.p("query.setVersionIncluded(%1$s);%n", m.isVersionIncluded());
        }
        if (m.isVersionIgnored() != null) {
            p.p("query.setVersionIgnored(%1$s);%n", m.isVersionIgnored());
        }
        if (m.isOptimisticLockExceptionSuppressed() != null) {
            p.p("query.setOptimisticLockExceptionSuppressed(%1$s);%n", m
                    .isOptimisticLockExceptionSuppressed());
        }
        p.p("query.compile();%n");
        p.p("%1$s command = new %s(query);%n", m.getCommandClass().getName());
        p.p("%1$s result = command.execute();%n", m.getReturnTypeName());
        p.p("exiting(\"%1$s\", \"%2$s\", result);%n", daoImplQualifiedName, m
                .getName());
        p.p("return result;%n");
        p.unindent();
        p.p("}%n");
        return null;
    }

    @Override
    public Void visitSqlFileBatchModifyQueryMeta(SqlFileBatchModifyQueryMeta m,
            Printer p) {
        p.p("@Override%n");
        p.p("public ");
        if (m.getTypeParameterNames().hasNext()) {
            p.pp("<");
            for (Iterator<String> it = m.getTypeParameterNames(); it.hasNext();) {
                p.pp("%s", it.next());
                if (it.hasNext()) {
                    p.pp(", ");
                }
            }
            p.pp("> ");
        }
        p.pp("%s %s(", m.getReturnTypeName(), m.getName());
        for (Iterator<Map.Entry<String, String>> it = m.getParameters(); it
                .hasNext();) {
            Map.Entry<String, String> entry = it.next();
            p.pp("%s %s", entry.getValue(), entry.getKey());
            if (it.hasNext()) {
                p.pp(", ");
            }
        }
        p.pp(") ");
        if (m.getThrownTypeNames().hasNext()) {
            p.pp("throws ");
            for (Iterator<String> it = m.getThrownTypeNames(); it.hasNext();) {
                p.pp("%s", it.next());
                if (it.hasNext()) {
                    p.pp(", ");
                }
            }
            p.pp(" ");
        }
        p.pp("{%n");
        p.indent();

        p.p("entering(\"%1$s\", \"%2$s\", %3$s);%n", daoImplQualifiedName, m
                .getName(), m.getEntityListName());
        p.p("if (%s == null) {%n", m.getEntityListName());
        p
                .p("    throw new %1$s(\"%2$s\", %2$s);%n", DomaIllegalArgumentException.class
                        .getName(), m.getEntityListName());
        p.p("}%n");
        p
                .p("%1$s<%2$s, %2$s%3$s> query = new %1$s<%2$s, %2$s%3$s>(%2$s%3$s.class);%n", m
                        .getQueryClass().getName(), m.getElementTypeName(), Options
                        .getSuffix(env));
        p.p("query.setConfig(config);%n");
        p.p("query.setEntities(%1$s);%n", m.getEntityListName());
        p.p("query.setCallerClassName(\"%s\");%n", daoImplQualifiedName);
        p.p("query.setCallerMethodName(\"%s\");%n", m.getName());
        if (m.getQueryTimeout() != null) {
            p.p("query.setQueryTimeout(%1$s);%n", m.getQueryTimeout());
        }
        p.p("query.compile();%n");
        p.p("%1$s command = new %s(query);%n", m.getCommandClass().getName());
        p.p("%1$s result = command.execute();%n", m.getReturnTypeName());
        p.p("exiting(\"%1$s\", \"%2$s\", result);%n", daoImplQualifiedName, m
                .getName());
        p.p("return result;%n");
        p.unindent();
        p.p("}%n");
        return null;
    }

    @Override
    public Void visitAutoFunctionQueryMeta(AutoFunctionQueryMeta m, Printer p) {
        p.p("@Override%n");
        p.p("public ");
        if (m.getTypeParameterNames().hasNext()) {
            p.pp("<");
            for (Iterator<String> it = m.getTypeParameterNames(); it.hasNext();) {
                p.pp("%s", it.next());
                if (it.hasNext()) {
                    p.pp(", ");
                }
            }
            p.pp("> ");
        }
        p.pp("%s %s(", m.getReturnTypeName(), m.getName());
        for (Iterator<CallableStatementParameterMeta> it = m
                .getCallableStatementParameterMetas().iterator(); it.hasNext();) {
            CallableStatementParameterMeta parameterMeta = it.next();
            p.pp("%s %s", parameterMeta.getTypeName(), parameterMeta.getName());
            if (it.hasNext()) {
                p.pp(", ");
            }
        }
        p.pp(") ");
        if (m.getThrownTypeNames().hasNext()) {
            p.pp("throws ");
            for (Iterator<String> it = m.getThrownTypeNames(); it.hasNext();) {
                p.pp("%s", it.next());
                if (it.hasNext()) {
                    p.pp(", ");
                }
            }
            p.pp(" ");
        }
        p.pp("{%n");
        p.indent();

        p.p("entering(\"%1$s\", \"%2$s\"", daoImplQualifiedName, m.getName());
        for (Iterator<CallableStatementParameterMeta> it = m
                .getCallableStatementParameterMetas().iterator(); it.hasNext();) {
            CallableStatementParameterMeta parameterMeta = it.next();
            p.pp(", %s", parameterMeta.getName());
        }
        p.pp(");%n");
        for (Iterator<CallableStatementParameterMeta> it = m
                .getCallableStatementParameterMetas().iterator(); it.hasNext();) {
            CallableStatementParameterMeta parameterMeta = it.next();
            p.p("if (%s == null) {%n", parameterMeta.getName());
            p
                    .p("    throw new %1$s(\"%2$s\", %2$s);%n", DomaIllegalArgumentException.class
                            .getName(), parameterMeta.getName());
            p.p("}%n");
        }
        p.p("%1$s<%2$s> query = new %1$s<%2$s>();%n", m.getQueryClass()
                .getName(), m.getReturnTypeName());
        p.p("query.setConfig(config);%n");
        p.p("query.setFunctionName(\"%s\");%n", m.getFunctionName());
        m.getResultParameterMeta().accept(this, p);
        for (CallableStatementParameterMeta parameterMeta : m
                .getCallableStatementParameterMetas()) {
            parameterMeta.accept(this, p);
        }
        p.p("query.setCallerClassName(\"%s\");%n", daoImplQualifiedName);
        p.p("query.setCallerMethodName(\"%s\");%n", m.getName());
        if (m.getQueryTimeout() != null) {
            p.p("query.setQueryTimeout(%1$s);%n", m.getQueryTimeout());
        }
        p.p("query.compile();%n");
        p.p("%1$s<%2$s> command = new %1$s<%2$s>(query);%n", m
                .getCommandClass().getName(), m.getReturnTypeName());
        p.p("%1$s result = command.execute();%n", m.getReturnTypeName());
        p.p("exiting(\"%1$s\", \"%2$s\", result);%n", daoImplQualifiedName, m
                .getName());
        p.p("return result;%n");
        p.unindent();
        p.p("}%n");
        return null;
    }

    @Override
    public Void visitAutoProcedureQueryMeta(AutoProcedureQueryMeta m, Printer p) {
        p.p("@Override%n");
        p.p("public ");
        if (m.getTypeParameterNames().hasNext()) {
            p.pp("<");
            for (Iterator<String> it = m.getTypeParameterNames(); it.hasNext();) {
                p.pp("%s", it.next());
                if (it.hasNext()) {
                    p.pp(", ");
                }
            }
            p.pp("> ");
        }
        p.pp("%s %s(", m.getReturnTypeName(), m.getName());
        for (Iterator<CallableStatementParameterMeta> it = m
                .getCallableStatementParameterMetas().iterator(); it.hasNext();) {
            CallableStatementParameterMeta parameterMeta = it.next();
            p.pp("%s %s", parameterMeta.getTypeName(), parameterMeta.getName());
            if (it.hasNext()) {
                p.pp(", ");
            }
        }
        p.pp(") ");
        if (m.getThrownTypeNames().hasNext()) {
            p.pp("throws ");
            for (Iterator<String> it = m.getThrownTypeNames(); it.hasNext();) {
                p.pp("%s", it.next());
                if (it.hasNext()) {
                    p.pp(", ");
                }
            }
            p.pp(" ");
        }
        p.pp("{%n");
        p.indent();

        p.p("entering(\"%1$s\", \"%2$s\"", daoImplQualifiedName, m.getName());
        for (Iterator<CallableStatementParameterMeta> it = m
                .getCallableStatementParameterMetas().iterator(); it.hasNext();) {
            CallableStatementParameterMeta parameterMeta = it.next();
            p.pp(", %s", parameterMeta.getName());
        }
        p.pp(");%n");
        for (Iterator<CallableStatementParameterMeta> it = m
                .getCallableStatementParameterMetas().iterator(); it.hasNext();) {
            CallableStatementParameterMeta parameterMeta = it.next();
            p.p("if (%s == null) {%n", parameterMeta.getName());
            p
                    .p("    throw new %1$s(\"%2$s\", %2$s);%n", DomaIllegalArgumentException.class
                            .getName(), parameterMeta.getName());
            p.p("}%n");
        }
        p.p("%1$s query = new %1$s();%n", m.getQueryClass().getName());
        p.p("query.setConfig(config);%n");
        p.p("query.setProcedureName(\"%s\");%n", m.getProcedureName());
        for (CallableStatementParameterMeta parameterMeta : m
                .getCallableStatementParameterMetas()) {
            parameterMeta.accept(this, p);
        }
        p.p("query.setCallerClassName(\"%s\");%n", daoImplQualifiedName);
        p.p("query.setCallerMethodName(\"%s\");%n", m.getName());
        if (m.getQueryTimeout() != null) {
            p.p("query.setQueryTimeout(%1$s);%n", m.getQueryTimeout());
        }
        p.p("query.compile();%n");
        p.p("%1$s command = new %1$s(query);%n", m.getCommandClass().getName());
        p.p("command.execute();%n");
        p.p("exiting(\"%1$s\", \"%2$s\", null);%n", daoImplQualifiedName, m
                .getName());
        p.unindent();
        p.p("}%n");
        return null;
    }

    @Override
    public Void visitArrayCreateQueryMeta(ArrayCreateQueryMeta m, Printer p) {
        p.p("@Override%n");
        p.p("public ");
        if (m.getTypeParameterNames().hasNext()) {
            p.pp("<");
            for (Iterator<String> it = m.getTypeParameterNames(); it.hasNext();) {
                p.pp("%s", it.next());
                if (it.hasNext()) {
                    p.pp(", ");
                }
            }
            p.pp("> ");
        }
        p.pp("%1$s %2$s(%3$s %4$s) ", m.getReturnTypeName(), m.getName(), m
                .getArrayTypeName(), m.getArrayName());
        if (m.getThrownTypeNames().hasNext()) {
            p.pp("throws ");
            for (Iterator<String> it = m.getThrownTypeNames(); it.hasNext();) {
                p.pp("%s", it.next());
                if (it.hasNext()) {
                    p.pp(", ");
                }
            }
            p.pp(" ");
        }
        p.pp("{%n");
        p.indent();

        p
                .p("entering(\"%1$s\", \"%2$s\", (Object)%3$s);%n", daoImplQualifiedName, m
                        .getName(), m.getArrayName());
        p.p("if (%s == null) {%n", m.getArrayName());
        p
                .p("    throw new %1$s(\"%2$s\", %2$s);%n", DomaIllegalArgumentException.class
                        .getName(), m.getArrayName());
        p.p("}%n");
        p.p("%1$s<%2$s> query = new %1$s<%2$s>();%n", m.getQueryClass()
                .getName(), m.getReturnTypeName());
        p.p("query.setConfig(config);%n");
        p.p("query.setCallerClassName(\"%s\");%n", daoImplQualifiedName);
        p.p("query.setCallerMethodName(\"%s\");%n", m.getName());
        p.p("query.setTypeName(\"%s\");%n", m.getJdbcTypeName());
        p.p("query.setElements(%s);%n", m.getArrayName());
        p.p("query.setResult(new %s());%n", m.getReturnTypeName());
        p.p("query.compile();%n");
        p.p("%1$s<%2$s> command = new %1$s<%2$s>(query);%n", m
                .getCommandClass().getName(), m.getReturnTypeName());
        p.p("%1$s result = command.execute();%n", m.getReturnTypeName());
        p.p("exiting(\"%1$s\", \"%2$s\", result);%n", daoImplQualifiedName, m
                .getName());
        p.p("return result;%n");
        p.unindent();
        p.p("}%n");
        return null;
    }

    @Override
    public Void visistDomainListParameterMeta(DomainListParameterMeta m,
            Printer p) {
        p
                .p("query.addParameter(new %1$s(%2$s.class, %3%s));%n", DomainListParameter.class
                        .getName(), m.getDomainTypeName(), m.getName());
        return null;
    }

    @Override
    public Void visistEntityListParameterMeta(EntityListParameterMeta m,
            Printer p) {
        p
                .p("query.addParameter(new %1$s<%2$s, %2$s%3$s>(%2$s%3$s.class, %4$s));%n", EntityListParameter.class
                        .getName(), m.getEntityTypeName(), Options
                        .getSuffix(env), m.getName());
        return null;
    }

    @Override
    public Void visistInOutParameterMeta(InOutParameterMeta m, Printer p) {
        p.p("query.addParameter(new %1$s(%2$s));%n", InOutParameter.class
                .getName(), m.getName());
        return null;
    }

    @Override
    public Void visistOutParameterMeta(OutParameterMeta m, Printer p) {
        p.p("query.addParameter(new %1$s(%2$s));%n", OutParameter.class
                .getName(), m.getName());
        return null;
    }

    @Override
    public Void visitInParameterMeta(InParameterMeta m, Printer p) {
        p.p("query.addParameter(new %1$s(%2$s));%n", InParameter.class
                .getName(), m.getName());
        return null;
    }

    @Override
    public Void visistDomainResultParameterMeta(DomainResultParameterMeta m,
            Printer p) {
        p
                .p("query.setResultParameter(new %1$s<%2$s>(%2$s.class));%n", DomainResultParameter.class
                        .getName(), m.getDomainTypeName());
        return null;
    }

    @Override
    public Void visistDomainListResultParameterMeta(
            DomainListResultParameterMeta m, Printer p) {
        p
                .p("query.setResultParameter(new %1$s<%2$s>(%2$s.class));%n", DomainListResultParameter.class
                        .getName(), m.getDomainTypeName());
        return null;
    }

    @Override
    public Void visistEntityListResultParameterMeta(
            EntityListResultParameterMeta m, Printer p) {
        p
                .p("query.setResultParameter(new %1$s<%2$s, %2$s%3$s>(%2$s%3$s.class));%n", EntityListResultParameter.class
                        .getName(), m.getEntityTypeName(), Options
                        .getSuffix(env));

        return null;
    }

}
