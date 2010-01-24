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

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedOptions;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.ElementFilter;
import javax.tools.Diagnostic.Kind;

import org.seasar.doma.internal.apt.meta.DomainMeta;
import org.seasar.doma.internal.apt.meta.DomainMetaFactory;
import org.seasar.doma.internal.message.Message;
import org.seasar.doma.internal.util.IOUtil;

/**
 * @author taedium
 * 
 */
@SupportedSourceVersion(SourceVersion.RELEASE_6)
@SupportedAnnotationTypes( { "org.seasar.doma.Domain" })
@SupportedOptions( { Options.TEST, Options.DEBUG })
public class DomainProcessor extends AbstractProcessor {

    @Override
    public boolean process(Set<? extends TypeElement> annotations,
            RoundEnvironment roundEnv) {
        if (roundEnv.processingOver()) {
            return true;
        }
        for (TypeElement a : annotations) {
            DomainMetaFactory domainMetaFactory = createDomainMetaFactory();
            for (TypeElement domainElement : ElementFilter.typesIn(roundEnv
                    .getElementsAnnotatedWith(a))) {
                if (Options.isDebugEnabled(processingEnv)) {
                    Notifier.debug(processingEnv, Message.DOMA4090,
                            getClass().getName(), domainElement
                                    .getQualifiedName());
                }
                try {
                    DomainMeta domainMeta = domainMetaFactory
                            .createDomainMeta(domainElement);
                    generateDomain(domainElement, domainMeta);
                } catch (AptException e) {
                    Notifier.notify(processingEnv, e);
                } catch (AptIllegalStateException e) {
                    Notifier.notify(processingEnv, Kind.ERROR,
                            Message.DOMA4039, domainElement);
                    throw e;
                } catch (RuntimeException e) {
                    Notifier.notify(processingEnv, Kind.ERROR,
                            Message.DOMA4016, domainElement);
                    throw e;
                }
                if (Options.isDebugEnabled(processingEnv)) {
                    Notifier.debug(processingEnv, Message.DOMA4091,
                            getClass().getName(), domainElement
                                    .getQualifiedName());
                }
            }
        }
        return true;
    }

    protected DomainMetaFactory createDomainMetaFactory() {
        return new DomainMetaFactory(processingEnv);
    }

    protected void generateDomain(TypeElement domainElement,
            DomainMeta domainMeta) {
        DomainTypeGenerator generator = null;
        try {
            generator = createDomainMetaFactoryGenerator(domainElement,
                    domainMeta);
            generator.generate();
        } catch (IOException e) {
            throw new AptException(Message.DOMA4011, processingEnv,
                    domainElement, e, domainElement.getQualifiedName(), e);
        } finally {
            IOUtil.close(generator);
        }
    }

    protected DomainTypeGenerator createDomainMetaFactoryGenerator(
            TypeElement domainElement, DomainMeta domainMeta)
            throws IOException {
        return new DomainTypeGenerator(processingEnv, domainElement,
                domainMeta);
    }

}
