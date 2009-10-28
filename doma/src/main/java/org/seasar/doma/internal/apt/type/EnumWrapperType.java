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
package org.seasar.doma.internal.apt.type;

import static org.seasar.doma.internal.util.AssertionUtil.*;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;

import org.seasar.doma.internal.apt.ElementUtil;
import org.seasar.doma.internal.apt.TypeUtil;
import org.seasar.doma.wrapper.EnumWrapper;

/**
 * @author taedium
 * 
 */
public class EnumWrapperType extends WrapperType {

    public EnumWrapperType(TypeMirror type, ProcessingEnvironment env) {
        super(type, env);
    }

    public static EnumWrapperType newInstance(TypeMirror wrappedType,
            ProcessingEnvironment env) {
        assertNotNull(wrappedType, env);
        TypeElement wrappedTypeElement = TypeUtil.toTypeElement(wrappedType,
                env);
        if (wrappedTypeElement == null
                || wrappedTypeElement.getKind() != ElementKind.ENUM) {
            return null;
        }
        TypeElement typeElement = ElementUtil.getTypeElement(EnumWrapper.class,
                env);
        DeclaredType declaredType = env.getTypeUtils().getDeclaredType(
                typeElement, wrappedType);
        EnumWrapperType wrapperType = new EnumWrapperType(declaredType, env);
        return wrapperType;
    }
}
