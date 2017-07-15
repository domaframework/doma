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

import javax.lang.model.element.TypeElement;

import org.seasar.doma.internal.apt.Context;

/**
 * @author nakamura
 *
 */
public class DaoImplCodeSpecFactory extends AbstractCodeSpecFactory {

    private final TypeElement parentDaoElement;

    public DaoImplCodeSpecFactory(Context ctx, TypeElement daoElement,
            TypeElement parentDaoElement) {
        super(ctx, daoElement);
        this.parentDaoElement = parentDaoElement;
    }

    @Override
    public CodeSpec create() {
        CodeSpec parentCodeSpec = createParentCodeSpec(ctx, parentDaoElement);
        String name = prefix(ctx, typeElement) + infix(ctx, typeElement) + suffix(ctx);
        String typeParamsName = createTypeParamsName(typeElement);
        return new CodeSpec(name, typeParamsName, parentCodeSpec);
    }

    private static CodeSpec createParentCodeSpec(Context ctx, TypeElement parentDaoElement) {
        if (parentDaoElement == null) {
            return null;
        }
        String name = prefix(ctx, parentDaoElement) + infix(ctx, parentDaoElement) + suffix(ctx);
        String typeParamsName = createTypeParamsName(parentDaoElement);
        return new CodeSpec(name, typeParamsName);
    }

    private static String prefix(Context ctx, TypeElement daoElement) {
        String daoPackage = ctx.getOptions().getDaoPackage();
        if (daoPackage != null) {
            return daoPackage + ".";
        }

        String packageName = ctx.getElements().getPackageName(daoElement);
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

    private static String infix(Context ctx, TypeElement daoElement) {
        return daoElement.getSimpleName().toString();
    }

    private static String suffix(Context ctx) {
        return ctx.getOptions().getDaoSuffix();
    }

}
