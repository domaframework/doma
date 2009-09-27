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
package org.seasar.doma.internal.apt.declaration;

import static org.seasar.doma.internal.util.AssertionUtil.*;

import java.util.List;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;

import org.seasar.doma.internal.apt.TypeUtil;

public class FieldDeclaration {

    protected VariableElement element;

    protected List<TypeParameterDeclaration> typeParameterDeclarations;

    protected ProcessingEnvironment env;

    protected FieldDeclaration() {
    }

    public static FieldDeclaration newInstance(TypeMirror type,
            ProcessingEnvironment env) {
        return null;
    }

    public VariableElement getElement() {
        return element;
    }

    public TypeDeclaration getTypeDeclaration() {
        TypeMirror returnType = resolveTypeParameter(element.asType());
        TypeElement typeElement = TypeUtil.toTypeElement(returnType, env);
        if (typeElement == null) {
            return null;
        }
        return TypeDeclaration.newInstance(typeElement, env);
    }

    public TypeMirror resolveTypeParameter(TypeMirror formalType) {
        for (TypeParameterDeclaration typeParameterDecl : typeParameterDeclarations) {
            if (formalType.equals(typeParameterDecl.getFormalType())) {
                return typeParameterDecl.getActualType();
            }
        }
        return formalType;
    }

    public static FieldDeclaration newInstance(VariableElement fieldElement,
            List<TypeParameterDeclaration> typeParameterDeclarations,
            ProcessingEnvironment env) {
        assertNotNull(fieldElement, typeParameterDeclarations, env);
        assertTrue(fieldElement.getKind() == ElementKind.FIELD);
        FieldDeclaration fieldDeclaration = new FieldDeclaration();
        fieldDeclaration.element = fieldElement;
        fieldDeclaration.typeParameterDeclarations = typeParameterDeclarations;
        fieldDeclaration.env = env;
        return fieldDeclaration;
    }
}
