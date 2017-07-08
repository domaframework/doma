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

import javax.lang.model.element.TypeElement;

import org.seasar.doma.internal.Conventions;
import org.seasar.doma.internal.apt.Context;
import org.seasar.doma.internal.util.ClassUtil;

/**
 * @author nakamura
 *
 */
public abstract class AbstractCanonicalNameFactory {

    protected final Context ctx;

    protected final TypeElement typeElement;

    protected AbstractCanonicalNameFactory(Context ctx, TypeElement typeElement) {
        assertNotNull(ctx, typeElement);
        this.ctx = ctx;
        this.typeElement = typeElement;
    }

    public CanonicalName create() {
        String name = prefix() + infix() + suffix();
        return new CanonicalName(name);
    }

    protected abstract String prefix();

    protected String infix() {
        String binaryName = Conventions
                .normalizeBinaryName(ctx.getElements().getBinaryName(typeElement).toString());
        return ClassUtil.getSimpleName(binaryName);
    }

    protected String suffix() {
        return "";
    }

}
