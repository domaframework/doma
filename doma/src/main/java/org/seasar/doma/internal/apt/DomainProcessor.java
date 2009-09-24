/*
 * Copyright 2004-2009 the Seasar Foundation and the Others.
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
import org.seasar.doma.internal.util.IOUtil;
import org.seasar.doma.message.DomaMessageCode;

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
                    Notifier.debug(processingEnv, DomaMessageCode.DOMA4090,
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
                            DomaMessageCode.DOMA4039, domainElement);
                    throw e;
                } catch (RuntimeException e) {
                    Notifier.notify(processingEnv, Kind.ERROR,
                            DomaMessageCode.DOMA4016, domainElement);
                    throw e;
                }
                if (Options.isDebugEnabled(processingEnv)) {
                    Notifier.debug(processingEnv, DomaMessageCode.DOMA4091,
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
        DomainTypeFactoryGenerator generator = null;
        try {
            generator = createDomainMetaFactoryGenerator(domainElement,
                    domainMeta);
            generator.generate();
        } catch (IOException e) {
            throw new AptException(DomaMessageCode.DOMA4011, processingEnv,
                    domainElement, e, domainElement.getQualifiedName(), e);
        } finally {
            IOUtil.close(generator);
        }
    }

    protected DomainTypeFactoryGenerator createDomainMetaFactoryGenerator(
            TypeElement domainElement, DomainMeta domainMeta)
            throws IOException {
        return new DomainTypeFactoryGenerator(processingEnv, domainElement,
                domainMeta);
    }

}
