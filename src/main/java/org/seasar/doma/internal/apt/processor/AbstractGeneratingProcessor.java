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
package org.seasar.doma.internal.apt.processor;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.lang.annotation.Annotation;
import java.util.Formatter;
import java.util.Set;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.ElementFilter;
import javax.tools.JavaFileObject;

import org.seasar.doma.internal.apt.AptException;
import org.seasar.doma.internal.apt.codespec.CodeSpec;
import org.seasar.doma.internal.apt.generator.Generator;
import org.seasar.doma.internal.apt.meta.TypeElementMeta;
import org.seasar.doma.internal.apt.meta.TypeElementMetaFactory;
import org.seasar.doma.message.Message;

/**
 * @author taedium
 * 
 */
public abstract class AbstractGeneratingProcessor<M extends TypeElementMeta>
        extends AbstractProcessor {

    protected AbstractGeneratingProcessor(Class<? extends Annotation> supportedAnnotationType) {
        super(supportedAnnotationType);
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        if (roundEnv.processingOver()) {
            return true;
        }
        for (TypeElement a : annotations) {
            for (TypeElement typeElement : ElementFilter
                    .typesIn(roundEnv.getElementsAnnotatedWith(a))) {
                handleTypeElement(typeElement, (t) -> {
                    TypeElementMetaFactory<M> factory = createTypeElementMetaFactory(t);
                    M meta = factory.createTypeElementMeta();
                    if (meta != null) {
                        generate(typeElement, meta);
                    }
                });
            }
        }
        return true;
    }

    protected abstract TypeElementMetaFactory<M> createTypeElementMetaFactory(
            TypeElement typeElement);

    protected void generate(TypeElement typeElement, M meta) {
        CodeSpec codeSpec = createCodeSpec(meta);
        try {
            JavaFileObject file = ctx.getResources().createSourceFile(codeSpec, typeElement);
            try (Formatter formatter = new Formatter(new BufferedWriter(file.openWriter()))) {
                Generator generator = createGenerator(meta, codeSpec, formatter);
                generator.generate();
            }
        } catch (IOException | UncheckedIOException e) {
            throw new AptException(Message.DOMA4079, typeElement, e,
                    new Object[] { codeSpec.getQualifiedName(), e });
        }
    }

    protected abstract CodeSpec createCodeSpec(M meta);

    protected abstract Generator createGenerator(M meta, CodeSpec codeSpec, Formatter formatter)
            throws IOException;

}
