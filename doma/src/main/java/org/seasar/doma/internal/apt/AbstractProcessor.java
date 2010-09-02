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
import javax.tools.Diagnostic.Kind;

import org.seasar.doma.internal.apt.meta.TypeElementMeta;
import org.seasar.doma.internal.apt.meta.TypeElementMetaFactory;
import org.seasar.doma.internal.util.IOUtil;
import org.seasar.doma.message.Message;

/**
 * @author taedium
 * 
 */
public abstract class AbstractProcessor<M extends TypeElementMeta> extends
        javax.annotation.processing.AbstractProcessor {

    protected AbstractProcessor() {
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations,
            RoundEnvironment roundEnv) {
        if (roundEnv.processingOver()) {
            return true;
        }
        for (TypeElement a : annotations) {
            TypeElementMetaFactory<M> factory = createTypeElementMetaFactory();
            for (TypeElement entityElement : ElementFilter.typesIn(roundEnv
                    .getElementsAnnotatedWith(a))) {
                if (Options.isDebugEnabled(processingEnv)) {
                    Notifier.debug(processingEnv, Message.DOMA4090, getClass()
                            .getName(), entityElement.getQualifiedName());
                }
                try {
                    M meta = factory.createTypeElementMeta(entityElement);
                    if (!meta.isError()) {
                        generate(entityElement, meta);
                    }
                } catch (AptException e) {
                    Notifier.notify(processingEnv, e);
                } catch (AptIllegalStateException e) {
                    Notifier.notify(processingEnv, Kind.ERROR,
                            Message.DOMA4039, entityElement);
                    throw e;
                } catch (RuntimeException e) {
                    Notifier.notify(processingEnv, Kind.ERROR,
                            Message.DOMA4016, entityElement);
                    throw e;
                }
                if (Options.isDebugEnabled(processingEnv)) {
                    Notifier.debug(processingEnv, Message.DOMA4091, getClass()
                            .getName(), entityElement.getQualifiedName());
                }
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
