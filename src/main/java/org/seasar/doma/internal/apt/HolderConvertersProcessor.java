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

import java.util.Set;

import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedOptions;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;

import org.seasar.doma.ExternalHolder;
import org.seasar.doma.HolderConverters;
import org.seasar.doma.internal.apt.reflection.HolderConvertersReflection;
import org.seasar.doma.message.Message;

/**
 * @author taedium
 * @since 1.25.0
 */
@SupportedAnnotationTypes({ "org.seasar.doma.HolderConverters" })
@SupportedOptions({ Options.RESOURCES_DIR, Options.TEST, Options.DEBUG })
public class HolderConvertersProcessor extends AbstractProcessor {

    public HolderConvertersProcessor() {
        super(HolderConverters.class);
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations,
            RoundEnvironment roundEnv) {
        if (roundEnv.processingOver()) {
            return true;
        }
        for (TypeElement a : annotations) {
            for (TypeElement typeElement : ElementFilter.typesIn(roundEnv
                    .getElementsAnnotatedWith(a))) {
                handleTypeElement(typeElement, this::validate);
            }
        }
        return true;
    }

    private void validate(TypeElement typeElement) {
        HolderConvertersReflection convertersMirror = ctx.getReflections()
                .newHolderConvertersReflection(typeElement);
        for (TypeMirror convType : convertersMirror.getValueValue()) {
            TypeElement convElement = ctx.getTypes().toTypeElement(convType);
            if (convElement == null) {
                continue;
            }
            if (convElement.getAnnotation(ExternalHolder.class) == null) {
                throw new AptException(Message.DOMA4196, typeElement,
                        convertersMirror.getAnnotationMirror(), new Object[] { convElement.getQualifiedName() });
            }
        }
    }
}
