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

import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedOptions;
import javax.lang.model.element.TypeElement;

import org.seasar.doma.ExternalHolder;
import org.seasar.doma.internal.apt.meta.ExternalHolderMeta;
import org.seasar.doma.internal.apt.meta.ExternalHolderMetaFactory;

/**
 * @author taedium
 * 
 */
@SupportedAnnotationTypes({ "org.seasar.doma.ExternalHolder" })
@SupportedOptions({ Options.VERSION_VALIDATION, Options.RESOURCES_DIR,
        Options.TEST, Options.DEBUG })
public class ExternalHolderProcessor extends
        AbstractGeneratingProcessor<ExternalHolderMeta> {

    public ExternalHolderProcessor() {
        super(ExternalHolder.class);
    }

    @Override
    protected ExternalHolderMetaFactory createTypeElementMetaFactory() {
        return new ExternalHolderMetaFactory(processingEnv);
    }

    @Override
    protected Generator createGenerator(TypeElement typeElement,
            ExternalHolderMeta meta) throws IOException {
        assertNotNull(typeElement, meta);
        return new ExternalHolderTypeGenerator(processingEnv,
                meta.getHolderElement(), meta);
    }
}
