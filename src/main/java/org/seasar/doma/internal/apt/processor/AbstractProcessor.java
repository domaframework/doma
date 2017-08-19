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

import java.lang.annotation.Annotation;
import java.util.function.Consumer;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic.Kind;

import org.seasar.doma.internal.apt.AptException;
import org.seasar.doma.internal.apt.AptIllegalOptionException;
import org.seasar.doma.internal.apt.AptIllegalStateException;
import org.seasar.doma.internal.apt.AptTypeHandleException;
import org.seasar.doma.internal.apt.Context;
import org.seasar.doma.message.Message;

/**
 * @author taedium
 * 
 */
public abstract class AbstractProcessor extends
        javax.annotation.processing.AbstractProcessor {

    protected Class<? extends Annotation> supportedAnnotationType;

    protected Context ctx;

    protected AbstractProcessor(
            Class<? extends Annotation> supportedAnnotationType) {
        this.supportedAnnotationType = supportedAnnotationType;
    }

    @Override
    public synchronized void init(ProcessingEnvironment env) {
        super.init(env);
        this.ctx = new Context(env);
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latest();
    }

    protected void handleTypeElement(TypeElement typeElement,
            Consumer<TypeElement> handler) {
        Annotation annotation = typeElement
                .getAnnotation(supportedAnnotationType);
        if (annotation == null) {
            return;
        }
        if (ctx.getOptions().isDebugEnabled()) {
            ctx.getNotifier().debug(Message.DOMA4090, new Object[] {
                    getClass().getName(), typeElement.getQualifiedName() });
        }
        try {
            handler.accept(typeElement);
        } catch (AptException e) {
            ctx.getNotifier().send(e);
        } catch (AptIllegalOptionException e) {
            ctx.getNotifier().send(Kind.ERROR, e.getMessage(),
                    typeElement);
            throw new AptTypeHandleException(typeElement, e);
        } catch (AptIllegalStateException e) {
            ctx.getNotifier().send(Kind.ERROR, Message.DOMA4039,
                    typeElement, new Object[] {});
            throw new AptTypeHandleException(typeElement, e);
        } catch (RuntimeException e) {
            ctx.getNotifier().send(Kind.ERROR, Message.DOMA4016,
                    typeElement, new Object[] {});
            throw new AptTypeHandleException(typeElement, e);
        }
        if (ctx.getOptions().isDebugEnabled()) {
            ctx.getNotifier().debug(Message.DOMA4091, new Object[] {
                    getClass().getName(), typeElement.getQualifiedName() });
        }
    }
}
