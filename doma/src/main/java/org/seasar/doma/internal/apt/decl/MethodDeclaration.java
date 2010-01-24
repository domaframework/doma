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
package org.seasar.doma.internal.apt.decl;

import static org.seasar.doma.internal.util.AssertionUtil.*;

import java.util.List;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.type.TypeMirror;

public class MethodDeclaration {

    protected ExecutableElement element;

    protected List<TypeParameterDeclaration> typeParameterDeclarations;

    protected ProcessingEnvironment env;

    protected MethodDeclaration() {
    }

    public ExecutableElement getElement() {
        return element;
    }

    public TypeDeclaration getReturnTypeDeclaration() {
        TypeMirror returnType = resolveTypeParameter(element.getReturnType());
        return TypeDeclaration.newTypeDeclaration(returnType, env);
    }

    public boolean isStatic() {
        return element.getModifiers().contains(Modifier.STATIC);
    }

    protected TypeMirror resolveTypeParameter(TypeMirror formalType) {
        for (TypeParameterDeclaration typeParameterDecl : typeParameterDeclarations) {
            if (formalType.equals(typeParameterDecl.getFormalType())) {
                return typeParameterDecl.getActualType();
            }
        }
        return formalType;
    }

    public static MethodDeclaration newInstance(
            ExecutableElement methodElement,
            List<TypeParameterDeclaration> typeParameterDeclarations,
            ProcessingEnvironment env) {
        assertNotNull(methodElement, typeParameterDeclarations, env);
        assertTrue(methodElement.getKind() == ElementKind.METHOD);
        MethodDeclaration methodDeclaration = new MethodDeclaration();
        methodDeclaration.element = methodElement;
        methodDeclaration.typeParameterDeclarations = typeParameterDeclarations;
        methodDeclaration.env = env;
        return methodDeclaration;
    }

}
