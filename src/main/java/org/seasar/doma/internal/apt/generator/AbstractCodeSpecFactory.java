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
package org.seasar.doma.internal.apt.generator;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.util.stream.Collectors;

import javax.lang.model.element.TypeElement;
import javax.lang.model.element.TypeParameterElement;
import javax.lang.model.type.TypeMirror;

import org.seasar.doma.internal.apt.Context;

/**
 * @author nakamura
 *
 */
public abstract class AbstractCodeSpecFactory implements CodeSpecFactory {

    protected final Context ctx;

    protected final TypeElement typeElement;

    protected AbstractCodeSpecFactory(Context ctx, TypeElement typeElement) {
        assertNotNull(ctx, typeElement);
        this.ctx = ctx;
        this.typeElement = typeElement;
    }

    protected static String createTypeParamsName(TypeElement typeElement) {
        return typeElement.getTypeParameters()
                .stream()
                .map(TypeParameterElement::asType)
                .map(TypeMirror::toString)
                .collect(Collectors.joining(", "));
    }

}
