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
package org.seasar.doma.internal.apt.type;

import static org.seasar.doma.internal.util.AssertionUtil.*;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;

import org.seasar.doma.internal.apt.util.ElementUtil;
import org.seasar.doma.internal.apt.util.MetaUtil;
import org.seasar.doma.internal.apt.util.TypeMirrorUtil;

/**
 * @author taedium
 * 
 */
public abstract class AbstractDataType implements DataType {

    protected final TypeMirror typeMirror;

    protected final ProcessingEnvironment env;

    protected final String typeName;

    protected final String typeNameAsTypeParameter;

    protected final String metaTypeName;

    protected final String metaTypeNameAsTypeParameter;

    protected final TypeElement typeElement;

    protected final String packageName;

    protected final String packageExcludedBinaryName;

    protected String qualifiedName;

    protected AbstractDataType(TypeMirror typeMirror, ProcessingEnvironment env) {
        assertNotNull(typeMirror, env);
        this.typeMirror = typeMirror;
        this.env = env;
        this.typeName = TypeMirrorUtil.getTypeName(typeMirror, env);
        this.typeNameAsTypeParameter = TypeMirrorUtil
                .getTypeNameAsTypeParameter(typeMirror, env);
        this.metaTypeName = MetaUtil.getMetaTypeName(typeName);
        this.metaTypeNameAsTypeParameter = MetaUtil
                .getMetaTypeName(typeNameAsTypeParameter);
        this.typeElement = TypeMirrorUtil.toTypeElement(typeMirror, env);
        if (typeElement != null) {
            qualifiedName = typeElement.getQualifiedName().toString();
            packageName = ElementUtil.getPackageName(typeElement, env);
            packageExcludedBinaryName = ElementUtil
                    .getPackageExcludedBinaryName(typeElement, env);
        } else {
            qualifiedName = typeName;
            packageName = "";
            packageExcludedBinaryName = typeName;
        }
    }

    @Override
    public TypeMirror getTypeMirror() {
        return typeMirror;
    }

    @Override
    public TypeElement getTypeElement() {
        return typeElement;
    }

    @Override
    public String getTypeName() {
        return typeName;
    }

    @Override
    public String getTypeNameAsTypeParameter() {
        return typeNameAsTypeParameter;
    }

    @Override
    public String getMetaTypeName() {
        return metaTypeName;
    }

    @Override
    public String getMetaTypeNameAsTypeParameter() {
        return metaTypeNameAsTypeParameter;
    }

    @Override
    public String getQualifiedName() {
        return qualifiedName;
    }

    @Override
    public String getPackageName() {
        return packageName;
    }

    @Override
    public String getPackageExcludedBinaryName() {
        return packageExcludedBinaryName;
    }

    @Override
    public boolean isEnum() {
        return typeElement != null && typeElement.getKind() == ElementKind.ENUM;
    }

    @Override
    public boolean isPrimitive() {
        return typeMirror.getKind().isPrimitive();
    }

}
