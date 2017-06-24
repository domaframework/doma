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
package org.seasar.doma.internal.apt.meta;

import java.util.List;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;

import org.seasar.doma.internal.apt.cttype.CtType;
import org.seasar.doma.internal.apt.cttype.EntityCtType;
import org.seasar.doma.internal.apt.cttype.IterableCtType;
import org.seasar.doma.internal.apt.reflection.BatchModifyReflection;
import org.seasar.doma.jdbc.SqlLogType;

/**
 * @author taedium
 * 
 */
public class SqlFileBatchModifyQueryMeta extends AbstractSqlFileQueryMeta {

    private EntityCtType entityCtType;

    private CtType elementCtType;

    private String elementsParameterName;

    private BatchModifyReflection batchModifyReflection;

    public SqlFileBatchModifyQueryMeta(ExecutableElement method, TypeElement dao) {
        super(method, dao);
    }

    public EntityCtType getEntityType() {
        return entityCtType;
    }

    public void setEntityType(EntityCtType entityCtType) {
        this.entityCtType = entityCtType;
    }

    public CtType getElementCtType() {
        return elementCtType;
    }

    public void setElementCtType(CtType elementCtType) {
        this.elementCtType = elementCtType;
    }

    public String getElementsParameterName() {
        return elementsParameterName;
    }

    public void setElementsParameterName(String entitiesParameterName) {
        this.elementsParameterName = entitiesParameterName;
    }

    public BatchModifyReflection getBatchModifyReflection() {
        return batchModifyReflection;
    }

    public void setBatchModifyReflection(BatchModifyReflection batchModifyReflection) {
        this.batchModifyReflection = batchModifyReflection;
    }

    public int getQueryTimeout() {
        return batchModifyReflection.getQueryTimeoutValue();
    }

    public int getBatchSize() {
        return batchModifyReflection.getBatchSizeValue();
    }

    public Boolean getIgnoreVersion() {
        return batchModifyReflection.getIgnoreVersionValue();
    }

    public Boolean getSuppressOptimisticLockException() {
        return batchModifyReflection.getSuppressOptimisticLockExceptionValue();
    }

    public List<String> getInclude() {
        return batchModifyReflection.getIncludeValue();
    }

    public List<String> getExclude() {
        return batchModifyReflection.getExcludeValue();
    }

    public SqlLogType getSqlLogType() {
        return batchModifyReflection.getSqlLogValue();
    }

    public boolean isPopulatable() {
        return entityCtType != null
                && queryKind == QueryKind.SQLFILE_BATCH_UPDATE;
    }

    @Override
    public void addBindableParameterCtType(final String parameterName,
            CtType bindableParameterCtType) {
        bindableParameterCtType.accept(new BindableParameterCtTypeVisitor(
                parameterName) {

            @Override
            public Void visitIterableCtType(IterableCtType ctType, Void p)
                    throws RuntimeException {
                return ctType.getElementCtType().accept(this, p);
            }
        }, null);
    }

    @Override
    public <R, P> R accept(QueryMetaVisitor<R, P> visitor, P p) {
        return visitor.visitSqlFileBatchModifyQueryMeta(this, p);
    }

}
