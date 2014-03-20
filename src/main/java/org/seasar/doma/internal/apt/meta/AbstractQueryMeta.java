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

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.type.TypeMirror;

import org.seasar.doma.internal.apt.cttype.CtType;
import org.seasar.doma.internal.apt.cttype.OptionalCtType;
import org.seasar.doma.internal.apt.cttype.OptionalDoubleCtType;
import org.seasar.doma.internal.apt.cttype.OptionalIntCtType;
import org.seasar.doma.internal.apt.cttype.OptionalLongCtType;
import org.seasar.doma.internal.apt.cttype.SimpleCtTypeVisitor;
import org.seasar.doma.jdbc.command.Command;
import org.seasar.doma.jdbc.query.Query;

/**
 * @author taedium
 * 
 */
public abstract class AbstractQueryMeta implements QueryMeta {

    protected String name;

    protected ExecutableElement executableElement;

    protected List<String> typeParameterNames = new ArrayList<String>();

    protected List<String> thrownTypeNames = new ArrayList<String>();

    protected QueryKind queryKind;

    protected Map<String, TypeMirror> bindableParameterTypeMap = new LinkedHashMap<String, TypeMirror>();

    protected QueryReturnMeta returnMeta;

    protected List<QueryParameterMeta> parameterMetas = new ArrayList<QueryParameterMeta>();

    protected List<String> fileNames = new ArrayList<String>();

    protected AbstractQueryMeta(ExecutableElement method) {
        assertNotNull(method);
        this.name = method.getSimpleName().toString();
        this.executableElement = method;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public ExecutableElement getExecutableElement() {
        return executableElement;
    }

    public void addTypeParameterName(String typeParameterName) {
        typeParameterNames.add(typeParameterName);
    }

    @Override
    public List<String> getTypeParameterNames() {
        return typeParameterNames;
    }

    public void addThrownTypeName(String thrownTypeName) {
        thrownTypeNames.add(thrownTypeName);
    }

    @Override
    public List<String> getThrownTypeNames() {
        return thrownTypeNames;
    }

    public Class<? extends Query> getQueryClass() {
        if (queryKind == null) {
            return null;
        }
        return queryKind.getQueryClass();
    }

    @SuppressWarnings("rawtypes")
    public Class<?> getCommandClass() {
        if (queryKind == null) {
            return null;
        }
        return (Class<? extends Command>) queryKind.getCommandClass();
    }

    @Override
    public QueryKind getQueryKind() {
        return queryKind;
    }

    public void setQueryKind(QueryKind queryKind) {
        this.queryKind = queryKind;
    }

    @Override
    public Map<String, TypeMirror> getBindableParameterTypeMap() {
        return bindableParameterTypeMap;
    }

    public void addBindableParameterCtType(final String parameterName,
            CtType bindableParameterCtType) {
        bindableParameterCtType.accept(new BindableParameterCtTypeVisitor(
                parameterName), null);
    }

    @Override
    public QueryReturnMeta getReturnMeta() {
        return returnMeta;
    }

    public void setReturnMeta(QueryReturnMeta returnMeta) {
        this.returnMeta = returnMeta;
    }

    @Override
    public List<QueryParameterMeta> getParameterMetas() {
        return parameterMetas;
    }

    public void addParameterMeta(QueryParameterMeta queryParameterMeta) {
        this.parameterMetas.add(queryParameterMeta);
    }

    @Override
    public List<String> getFileNames() {
        return fileNames;
    }

    public void addFileName(String fileName) {
        this.fileNames.add(fileName);
    }

    @Override
    public boolean isVarArgs() {
        return this.executableElement.isVarArgs();
    }

    protected class BindableParameterCtTypeVisitor extends
            SimpleCtTypeVisitor<Void, Void, RuntimeException> {

        protected final String parameterName;

        protected BindableParameterCtTypeVisitor(String parameterName) {
            this.parameterName = parameterName;
        }

        @Override
        protected Void defaultAction(CtType ctType, Void p)
                throws RuntimeException {
            bindableParameterTypeMap.put(parameterName, ctType.getTypeMirror());
            return null;
        }

        @Override
        public Void visitOptionalCtType(OptionalCtType ctType, Void p)
                throws RuntimeException {
            return ctType.getElementCtType().accept(this, p);
        }

        @Override
        public Void visitOptionalIntCtType(OptionalIntCtType ctType, Void p)
                throws RuntimeException {
            return ctType.getElementCtType().accept(this, p);
        }

        @Override
        public Void visitOptionalLongCtType(OptionalLongCtType ctType, Void p)
                throws RuntimeException {
            return ctType.getElementCtType().accept(this, p);
        }

        @Override
        public Void visitOptionalDoubleCtType(OptionalDoubleCtType ctType,
                Void p) throws RuntimeException {
            return ctType.getElementCtType().accept(this, p);
        }

    }
}
