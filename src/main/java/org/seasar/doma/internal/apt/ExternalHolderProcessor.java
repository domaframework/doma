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
package org.seasar.doma.internal.apt;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.io.IOException;
import java.util.Formatter;

import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedOptions;
import javax.lang.model.element.TypeElement;

import org.seasar.doma.ExternalHolder;
import org.seasar.doma.internal.apt.generator.CodeSpec;
import org.seasar.doma.internal.apt.generator.CodeSpecFactory;
import org.seasar.doma.internal.apt.generator.ExternalDescCodeSpecFactory;
import org.seasar.doma.internal.apt.generator.ExternalHolderDescGenerator;
import org.seasar.doma.internal.apt.generator.Generator;
import org.seasar.doma.internal.apt.meta.holder.ExternalHolderMeta;
import org.seasar.doma.internal.apt.meta.holder.ExternalHolderMetaFactory;

/**
 * @author taedium
 * 
 */
@SupportedAnnotationTypes({ "org.seasar.doma.ExternalHolder" })
@SupportedOptions({ Options.VERSION_VALIDATION, Options.RESOURCES_DIR, Options.TEST,
        Options.DEBUG })
public class ExternalHolderProcessor extends AbstractGeneratingProcessor<ExternalHolderMeta> {

    public ExternalHolderProcessor() {
        super(ExternalHolder.class);
    }

    @Override
    protected ExternalHolderMetaFactory createTypeElementMetaFactory(TypeElement typeElement) {
        return new ExternalHolderMetaFactory(ctx, typeElement);
    }

    @Override
    protected CodeSpecFactory createCodeSpecFactory(ExternalHolderMeta meta) {
        return new ExternalDescCodeSpecFactory(ctx, meta.getHolderElement());
    }

    @Override
    protected Generator createGenerator(ExternalHolderMeta meta, CodeSpec codeSpec, Formatter formatter) throws IOException {
        assertNotNull(meta, codeSpec);
        return new ExternalHolderDescGenerator(ctx, meta, codeSpec, formatter);
    }
}
