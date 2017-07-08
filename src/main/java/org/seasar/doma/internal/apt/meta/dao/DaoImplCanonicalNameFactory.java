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
package org.seasar.doma.internal.apt.meta.dao;

import javax.lang.model.element.TypeElement;

import org.seasar.doma.internal.apt.Context;
import org.seasar.doma.internal.apt.meta.AbstractCanonicalNameFactory;

/**
 * @author nakamura
 *
 */
public class DaoImplCanonicalNameFactory extends AbstractCanonicalNameFactory {

    public DaoImplCanonicalNameFactory(Context ctx, TypeElement daoElement) {
        super(ctx, daoElement);
    }

    @Override
    protected String prefix() {
        String daoPackage = ctx.getOptions().getDaoPackage();
        if (daoPackage != null) {
            return daoPackage + ".";
        }

        String packageName = ctx.getElements().getPackageName(typeElement);
        String base = "";
        if (packageName != null && packageName.length() > 0) {
            base = packageName + ".";
        }
        String daoSubpackage = ctx.getOptions().getDaoSubpackage();
        if (daoSubpackage != null) {
            return base + daoSubpackage + ".";
        }
        return base;
    }

    @Override
    protected String suffix() {
        return ctx.getOptions().getDaoSuffix();
    }

}
