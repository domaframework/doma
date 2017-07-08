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
package org.seasar.doma.internal.apt.meta.entity;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import javax.lang.model.element.VariableElement;

import org.seasar.doma.internal.apt.Context;
import org.seasar.doma.internal.apt.cttype.CtType;
import org.seasar.doma.internal.apt.reflection.ColumnReflection;

/**
 * @author nakamura-to
 *
 */
public class EmbeddablePropertyMeta extends AbstractPropertyMeta {

    public EmbeddablePropertyMeta(Context ctx, VariableElement fieldElement,
            String name, CtType ctType, ColumnReflection columnReflection) {
        super(fieldElement, name, ctType, columnReflection);
        assertNotNull(ctx, fieldElement);
    }

}
