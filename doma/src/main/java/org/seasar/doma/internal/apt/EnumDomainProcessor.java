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

import org.seasar.doma.internal.apt.meta.EnumDomainMeta;
import org.seasar.doma.internal.apt.meta.EnumDomainMetaFactory;

/**
 * @author taedium
 * 
 */
@SupportedAnnotationTypes({ "org.seasar.doma.EnumDomain" })
@SupportedOptions({ Options.TEST, Options.DEBUG })
public class EnumDomainProcessor extends
        AbstractGeneratingProcessor<EnumDomainMeta> {

    @Override
    protected EnumDomainMetaFactory createTypeElementMetaFactory() {
        return new EnumDomainMetaFactory(processingEnv);
    }

    @Override
    protected Generator createGenerator(TypeElement typeElement,
            EnumDomainMeta meta) throws IOException {
        assertNotNull(typeElement, meta);
        return new EnumDomainTypeGenerator(processingEnv, typeElement, meta);
    }

}
