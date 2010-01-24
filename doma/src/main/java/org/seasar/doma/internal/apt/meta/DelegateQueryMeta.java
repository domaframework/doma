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
import javax.lang.model.type.TypeMirror;

import org.seasar.doma.internal.apt.mirror.DelegateMirror;

/**
 * @author taedium
 * 
 */
public class DelegateQueryMeta extends AbstractQueryMeta {

    protected DelegateMirror delegateMirror;

    protected boolean daoAware;

    public DelegateQueryMeta(ExecutableElement method) {
        super(method);
    }

    DelegateMirror getDelegateMirror() {
        return delegateMirror;
    }

    public void setDelegateMirror(DelegateMirror delegateMirror) {
        this.delegateMirror = delegateMirror;
    }

    public boolean isDaoAware() {
        return daoAware;
    }

    public void setDaoAware(boolean daoAware) {
        this.daoAware = daoAware;
    }

    public TypeMirror getTo() {
        return delegateMirror.getToValue();
    }

    @Override
    public <R, P> R accept(QueryMetaVisitor<R, P> visitor, P p) {
        return visitor.visitDelegateQueryMeta(this, p);
    }

}
