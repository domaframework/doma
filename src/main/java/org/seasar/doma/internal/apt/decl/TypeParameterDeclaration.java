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

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.type.TypeMirror;

public class TypeParameterDeclaration {

    protected TypeMirror formalType;

    protected TypeMirror actualType;

    protected ProcessingEnvironment env;

    protected TypeParameterDeclaration() {
    }

    public TypeMirror getFormalType() {
        return formalType;
    }

    public TypeMirror getActualType() {
        return actualType;
    }

    public static TypeParameterDeclaration newInstance(TypeMirror formalType,
            TypeMirror actualType, ProcessingEnvironment env) {
        assertNotNull(formalType, actualType, env);
        TypeParameterDeclaration typeParameterDeclaration = new TypeParameterDeclaration();
        typeParameterDeclaration.formalType = formalType;
        typeParameterDeclaration.actualType = actualType;
        typeParameterDeclaration.env = env;
        return typeParameterDeclaration;
    }
}
