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

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import javax.annotation.processing.Messager;
import javax.lang.model.element.Element;
import javax.tools.Diagnostic.Kind;

import org.seasar.doma.message.MessageResource;

public final class Notifier {

    private final Messager messager;

    public Notifier(Context ctx) {
        assertNotNull(ctx);
        this.messager = ctx.getEnv().getMessager();
    }

    public void debug(MessageResource messageResource, Object[] args) {
        assertNotNull(messageResource, args);
        messager.printMessage(Kind.OTHER, messageResource.getMessage(args));
    }

    public void send(Kind kind, MessageResource messageResource, Element element, Object[] args) {
        assertNotNull(kind, element, args);
        messager.printMessage(kind, messageResource.getMessage(args), element);
    }

    public void send(Kind kind, String message, Element element) {
        assertNotNull(kind, message, element);
        messager.printMessage(kind, message, element);
    }

    public void send(AptException e) {
        assertNotNull(e);
        messager.printMessage(e.getKind(), e.getMessage(), e.getElement(), e.getAnnotationMirror(),
                e.getAnnotationValue());
    }

}
