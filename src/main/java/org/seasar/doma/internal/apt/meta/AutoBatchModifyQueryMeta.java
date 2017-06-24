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

import org.seasar.doma.internal.apt.cttype.EntityCtType;
import org.seasar.doma.internal.apt.reflection.BatchModifyReflection;
import org.seasar.doma.jdbc.SqlLogType;

/**
 * @author taedium
 * 
 */
public class AutoBatchModifyQueryMeta extends AbstractQueryMeta {

    private EntityCtType entityCtType;

    private String entitiesParameterName;

    private BatchModifyReflection batchModifyReflection;

    public AutoBatchModifyQueryMeta(ExecutableElement method, TypeElement dao) {
        super(method, dao);
    }

    public EntityCtType getEntityCtType() {
        return entityCtType;
    }

    public void setEntityCtType(EntityCtType entityCtType) {
        this.entityCtType = entityCtType;
    }

    public String getEntitiesParameterName() {
        return entitiesParameterName;
    }

    public void setEntitiesParameterName(String entitiesParameterName) {
        this.entitiesParameterName = entitiesParameterName;
    }

    BatchModifyReflection getBatchModifyReflection() {
        return batchModifyReflection;
    }

    void setBatchModifyReflection(BatchModifyReflection batchModifyReflection) {
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

    @Override
    public <R, P> R accept(QueryMetaVisitor<R, P> visitor, P p) {
        return visitor.visitAutoBatchModifyQueryMeta(this, p);
    }

}
