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

import org.seasar.doma.internal.apt.cttype.CtType;
import org.seasar.doma.internal.apt.cttype.EntityCtType;
import org.seasar.doma.internal.apt.cttype.IterableCtType;
import org.seasar.doma.internal.apt.mirror.BatchModifyMirror;

/**
 * @author taedium
 * 
 */
public class SqlFileBatchModifyQueryMeta extends AbstractSqlFileQueryMeta {

    protected EntityCtType entityCtType;

    protected CtType elementCtType;

    protected String elementsParameterName;

    protected BatchModifyMirror batchModifyMirror;

    public SqlFileBatchModifyQueryMeta(ExecutableElement method) {
        super(method);
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

    public Boolean getSuppressOptimisticLockException() {
        return batchModifyMirror.getSuppressOptimisticLockExceptionValue();
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
