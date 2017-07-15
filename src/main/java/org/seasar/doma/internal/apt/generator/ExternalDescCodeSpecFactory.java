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

import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;

import org.seasar.doma.internal.Conventions;
import org.seasar.doma.internal.apt.Context;

/**
 * @author nakamura
 *
 */
public class ExternalDescCodeSpecFactory extends DescCodeSpecFactory {

    public ExternalDescCodeSpecFactory(Context ctx, TypeElement holderElement) {
        super(ctx, holderElement);
    }

    @Override
    protected String createDescClassName(Name binaryName) {
        return Conventions.createExternalDescClassName(binaryName);
    }

}
