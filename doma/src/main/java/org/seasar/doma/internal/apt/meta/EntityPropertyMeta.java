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
package org.seasar.doma.internal.apt.meta;

import static org.seasar.doma.internal.util.AssertionUtil.*;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.type.TypeMirror;

import org.seasar.doma.internal.apt.TypeUtil;

/**
 * 
 * @author taedium
 * 
 */
public class EntityPropertyMeta {

    protected final TypeMirror type;

    protected final String typeName;

    protected String name;

    protected String wrapperTypeName;

    protected boolean id;

    protected boolean trnsient;

    protected boolean version;

    protected ColumnMeta columnMeta;

    protected IdGeneratorMeta idGeneratorMeta;

    protected DomainMeta domainMeta;

    public EntityPropertyMeta(TypeMirror type, ProcessingEnvironment env) {
        assertNotNull(type, env);
        this.type = type;
        this.typeName = TypeUtil.getTypeName(type, env);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isId() {
        return id;
    }

    public void setId(boolean id) {
        this.id = id;
    }

    public boolean isTrnsient() {
        return trnsient;
    }

    public void setTrnsient(boolean trnsient) {
        this.trnsient = trnsient;
    }

    public boolean isVersion() {
        return version;
    }

    public void setVersion(boolean version) {
        this.version = version;
    }

    public ColumnMeta getColumnMeta() {
        return columnMeta;
    }

    public void setColumnMeta(ColumnMeta columnMeta) {
        this.columnMeta = columnMeta;
    }

    public IdGeneratorMeta getIdGeneratorMeta() {
        return idGeneratorMeta;
    }

    public void setIdGeneratorMeta(IdGeneratorMeta idGeneratorMeta) {
        this.idGeneratorMeta = idGeneratorMeta;
    }

    public void setWrapperTypeName(String wrapperTypeName) {
        this.wrapperTypeName = wrapperTypeName;
    }

    public String getWrapperTypeName() {
        return wrapperTypeName;
    }

    public DomainMeta getDomainMeta() {
        return domainMeta;
    }

    public void setDomainMeta(DomainMeta domainMeta) {
        this.domainMeta = domainMeta;
    }

    public TypeMirror getType() {
        return type;
    }

    public String getTypeName() {
        return typeName;
    }

    public boolean isDomain() {
        return domainMeta != null;
    }

}
