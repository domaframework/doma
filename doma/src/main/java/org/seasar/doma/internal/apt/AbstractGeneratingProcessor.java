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

import java.io.IOException;
import java.util.Set;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.ElementFilter;

import org.seasar.doma.internal.apt.meta.TypeElementMeta;
import org.seasar.doma.internal.apt.meta.TypeElementMetaFactory;
import org.seasar.doma.internal.util.IOUtil;
import org.seasar.doma.message.Message;

/**
 * @author taedium
 * 
 */
public abstract class AbstractGeneratingProcessor<M extends TypeElementMeta>
        extends AbstractProcessor {

    protected AbstractGeneratingProcessor() {
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations,
            RoundEnvironment roundEnv) {
        if (roundEnv.processingOver()) {
            return true;
        }
        for (TypeElement a : annotations) {
            final TypeElementMetaFactory<M> factory = createTypeElementMetaFactory();
            TypeElementHandler handler = new TypeElementHandler() {

                @Override
                public void handle(TypeElement typeElement) {
                    M meta = factory.createTypeElementMeta(typeElement);
                    if (!meta.isError()) {
                        generate(typeElement, meta);
                    }
                }
            };
            for (TypeElement typeElement : ElementFilter.typesIn(roundEnv
                    .getElementsAnnotatedWith(a))) {
                handleTypeElement(typeElement, handler);
            }
        }
        return true;
    }

    protected abstract TypeElementMetaFactory<M> createTypeElementMetaFactory();

    protected void generate(TypeElement typeElement, M meta) {
        Generator generator = null;
        try {
            generator = createGenerator(typeElement, meta);
            generator.generate();
        } catch (IOException e) {
            throw new AptException(Message.DOMA4011, processingEnv,
                    typeElement, e, typeElement.getQualifiedName(), e);
        } finally {
            IOUtil.close(generator);
        }
    }

    protected abstract Generator createGenerator(TypeElement typeElement, M meta)
            throws IOException;
}
