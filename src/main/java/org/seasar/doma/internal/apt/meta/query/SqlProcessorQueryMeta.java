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
package org.seasar.doma.internal.apt.meta.query;

import javax.lang.model.element.ExecutableElement;

import org.seasar.doma.internal.apt.cttype.BiFunctionCtType;
import org.seasar.doma.internal.apt.reflection.SqlProcessorReflection;

/**
 * @author nakamura
 *
 */
public class SqlProcessorQueryMeta extends AbstractSqlFileQueryMeta {

    private SqlProcessorReflection sqlProcessorReflection;

    private String biFunctionParameterName;

    private BiFunctionCtType biFunctionCtType;

    protected SqlProcessorQueryMeta(ExecutableElement method) {
        super(method);
    }

    @Override
    public <R, P> R accept(QueryMetaVisitor<R, P> visitor, P p) {
        return visitor.visitSqlProcessorQueryMeta(this, p);
    }

    public SqlProcessorReflection getSqlProcessorReflection() {
        return sqlProcessorReflection;
    }

    public void setSqlProcessorReflection(SqlProcessorReflection sqlProcessorReflection) {
        this.sqlProcessorReflection = sqlProcessorReflection;
    }

    public String getBiFunctionParameterName() {
        return biFunctionParameterName;
    }

    public void setBiFunctionParameterName(String biFunctionParameterName) {
        this.biFunctionParameterName = biFunctionParameterName;
    }

    public BiFunctionCtType getBiFunctionCtType() {
        return biFunctionCtType;
    }

    public void setBiFunctionCtType(BiFunctionCtType biFunctionCtType) {
        this.biFunctionCtType = biFunctionCtType;
    }

}
