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

import javax.lang.model.element.ExecutableElement;

import org.seasar.doma.internal.apt.mirror.BatchModifyMirror;
import org.seasar.doma.internal.apt.type.DataType;
import org.seasar.doma.internal.apt.type.EntityType;

/**
 * @author taedium
 * 
 */
public class SqlFileBatchModifyQueryMeta extends AbstractSqlFileQueryMeta {

    protected EntityType entityType;

    protected DataType elementType;

    protected String elementsParameterName;

    protected BatchModifyMirror batchModifyMirror;

    public SqlFileBatchModifyQueryMeta(ExecutableElement method) {
        super(method);
    }

    public EntityType getEntityType() {
        return entityType;
    }

    public void setEntityType(EntityType entityType) {
        this.entityType = entityType;
    }

    public DataType getElementType() {
        return elementType;
    }

    public void setElementType(DataType elementType) {
        this.elementType = elementType;
    }

    public String getElementsParameterName() {
        return elementsParameterName;
    }

    public void setElementsParameterName(String entitiesParameterName) {
        this.elementsParameterName = entitiesParameterName;
    }

    public BatchModifyMirror getBatchModifyMirror() {
        return batchModifyMirror;
    }

    public void setBatchModifyMirror(BatchModifyMirror batchModifyMirror) {
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

    @Override
    public <R, P> R accept(QueryMetaVisitor<R, P> visitor, P p) {
        return visitor.visitSqlFileBatchModifyQueryMeta(this, p);
    }

}
