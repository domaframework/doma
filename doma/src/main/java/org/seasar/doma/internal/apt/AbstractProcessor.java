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

import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic.Kind;

import org.seasar.doma.message.Message;

/**
 * @author taedium
 * 
 */
public abstract class AbstractProcessor extends
        javax.annotation.processing.AbstractProcessor {

    protected AbstractProcessor() {
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latest();
    }

    protected void handleTypeElement(TypeElement typeElement,
            TypeElementHandler handler) {
        if (Options.isDebugEnabled(processingEnv)) {
            Notifier.debug(processingEnv, Message.DOMA4090, getClass()
                    .getName(), typeElement.getQualifiedName());
        }
        try {
            handler.handle(typeElement);
        } catch (AptException e) {
            Notifier.notify(processingEnv, e);
        } catch (AptIllegalOptionException e) {
            Notifier.notify(processingEnv, Kind.ERROR, e.getMessage(),
                    typeElement);
            throw new AptTypeHandleException(typeElement, e);
        } catch (AptIllegalStateException e) {
            Notifier.notify(processingEnv, Kind.ERROR, Message.DOMA4039,
                    typeElement);
            throw new AptTypeHandleException(typeElement, e);
        } catch (RuntimeException e) {
            Notifier.notify(processingEnv, Kind.ERROR, Message.DOMA4016,
                    typeElement);
            throw new AptTypeHandleException(typeElement, e);
        }
        if (Options.isDebugEnabled(processingEnv)) {
            Notifier.debug(processingEnv, Message.DOMA4091, getClass()
                    .getName(), typeElement.getQualifiedName());
        }
    }

}
