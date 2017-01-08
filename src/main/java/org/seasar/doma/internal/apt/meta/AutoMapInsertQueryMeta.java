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
import javax.lang.model.element.TypeElement;

import org.seasar.doma.internal.apt.mirror.MapInsertMirror;
import org.seasar.doma.jdbc.SqlLogType;

/**
 * @author bakenezumi
 *
 */
public class AutoMapInsertQueryMeta extends AbstractQueryMeta {

    protected MapInsertMirror mapInsertMirror;

    public AutoMapInsertQueryMeta(ExecutableElement method, TypeElement dao) {
        super(method, dao);
    }

    MapInsertMirror getMapInsertMirror() {
        return mapInsertMirror;
    }

    void setMapInsertMirror(MapInsertMirror mapInsertMirror) {
        this.mapInsertMirror = mapInsertMirror;
    }

    public int getQueryTimeout() {
        return mapInsertMirror.getQueryTimeoutValue();
    }

    public SqlLogType getSqlLogType() {
        return mapInsertMirror.getSqlLogValue();
    }

    public String getTableName() {
        return mapInsertMirror.getTableNameValue();
    }

    @Override
    public <R, P> R accept(QueryMetaVisitor<R, P> visitor, P p) {
        return visitor.visitAutoMapInsertQueryMeta(this, p);
    }

}
