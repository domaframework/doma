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

/**
 * 
 * @author taedium
 * 
 */
public class EntityPropertyMeta extends AbstractEntityMethodMeta {

    protected boolean listReturnType;

    protected boolean parameterizedReturnType;

    protected String returnElementTypeName;

    protected boolean id;

    protected boolean trnsient;

    protected boolean version;

    protected ColumnMeta columnMeta;

    protected IdGeneratorMeta idGeneratorMeta;

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

    public boolean isListReturnType() {
        return listReturnType;
    }

    public void setListReturnType(boolean listReturnType) {
        this.listReturnType = listReturnType;
    }

    public String getReturnElementTypeName() {
        return returnElementTypeName;
    }

    public void setReturnElementTypeName(String elementTypeName) {
        this.returnElementTypeName = elementTypeName;
    }

    public boolean isParameterizedReturnType() {
        return parameterizedReturnType;
    }

    public void setParameterizedReturnType(boolean parameterizedReturnType) {
        this.parameterizedReturnType = parameterizedReturnType;
    }

}
