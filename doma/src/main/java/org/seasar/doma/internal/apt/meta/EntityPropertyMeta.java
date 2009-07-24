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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.lang.model.element.ExecutableElement;

/**
 * 
 * @author taedium
 * 
 */
public class EntityPropertyMeta {

    protected String name;

    protected ExecutableElement executableElement;

    protected List<String> typeParameterNames = new ArrayList<String>();

    protected boolean listReturnType;

    protected String returnTypeName;

    protected boolean parameterizedReturnType;

    protected String returnElementTypeName;

    protected List<String> thrownTypeNames = new ArrayList<String>();

    protected boolean id;

    protected boolean trnsient;

    protected boolean version;

    protected ColumnMeta columnMeta;

    protected IdGeneratorMeta idGeneratorMeta;

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

    public ExecutableElement getExecutableElement() {
        return executableElement;
    }

    public void setExecutableElement(ExecutableElement executableElement) {
        this.executableElement = executableElement;
    }

    public void addTypeParameterName(String typeParameterName) {
        typeParameterNames.add(typeParameterName);
    }

    public Iterator<String> getTypeParameterNames() {
        return typeParameterNames.iterator();
    }

    public void addThrownTypeName(String thrownTypeName) {
        thrownTypeNames.add(thrownTypeName);
    }

    public Iterator<String> getThrownTypeNames() {
        return thrownTypeNames.iterator();
    }

    public String getReturnTypeName() {
        return returnTypeName;
    }

    public void setReturnTypeName(String returnTypeName) {
        this.returnTypeName = returnTypeName;
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
