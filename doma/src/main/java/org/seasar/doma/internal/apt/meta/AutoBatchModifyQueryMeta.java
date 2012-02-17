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

import org.seasar.doma.internal.apt.mirror.BatchModifyMirror;
import org.seasar.doma.internal.apt.type.EntityType;

/**
 * @author taedium
 * 
 */
public class AutoBatchModifyQueryMeta extends AbstractQueryMeta {

    protected EntityType entityType;

    protected String entitiesParameterName;

    protected BatchModifyMirror batchModifyMirror;

    public AutoBatchModifyQueryMeta(ExecutableElement method) {
        super(method);
    }

    public EntityType getEntityType() {
        return entityType;
    }

    public void setEntityType(EntityType entityType) {
        this.entityType = entityType;
    }

    public String getEntitiesParameterName() {
        return entitiesParameterName;
    }

    public void setEntitiesParameterName(String entitiesParameterName) {
        this.entitiesParameterName = entitiesParameterName;
    }

    BatchModifyMirror getBatchModifyMirror() {
        return batchModifyMirror;
    }

    void setBatchModifyMirror(BatchModifyMirror batchModifyMirror) {
        this.batchModifyMirror = batchModifyMirror;
    }

    public int getQueryTimeout() {
        return batchModifyMirror.getQueryTimeoutValue();
    }

    public int getBatchSize() {
        return batchModifyMirror.getBatchSizeValue();
    }

    public Boolean getIgnoreVersion() {
        return batchModifyMirror.getIgnoreVersionValue();
    }

    public Boolean getIncludeVersion() {
        return batchModifyMirror.getIncludeVersionValue();
    }

    public Boolean getSuppressOptimisticLockException() {
        return batchModifyMirror.getSuppressOptimisticLockExceptionValue();
    }

    public List<String> getInclude() {
        return batchModifyMirror.getIncludeValue();
    }

    public List<String> getExclude() {
        return batchModifyMirror.getExcludeValue();
    }

    @Override
    public <R, P> R accept(QueryMetaVisitor<R, P> visitor, P p) {
        return visitor.visitAutoBatchModifyQueryMeta(this, p);
    }

}
