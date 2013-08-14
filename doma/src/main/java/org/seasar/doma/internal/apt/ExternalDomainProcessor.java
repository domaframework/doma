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

import static org.seasar.doma.internal.util.AssertionUtil.*;

import java.io.IOException;

import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedOptions;
import javax.lang.model.element.TypeElement;

import org.seasar.doma.internal.apt.meta.ExternalDomainMeta;
import org.seasar.doma.internal.apt.meta.ExternalDomainMetaFactory;

/**
 * @author taedium
 * 
 */
@SupportedAnnotationTypes({ "org.seasar.doma.ExternalDomain" })
@SupportedOptions({ Options.VERSION_VALIDATION, Options.TEST, Options.DEBUG })
public class ExternalDomainProcessor extends
        AbstractGeneratingProcessor<ExternalDomainMeta> {

    @Override
    protected ExternalDomainMetaFactory createTypeElementMetaFactory() {
        return new ExternalDomainMetaFactory(processingEnv);
    }

    @Override
    protected Generator createGenerator(TypeElement typeElement,
            ExternalDomainMeta meta) throws IOException {
        assertNotNull(typeElement, meta);
        return new ExternalDomainTypeGenerator(processingEnv,
                meta.getDomainElement(), meta);
    }
}
