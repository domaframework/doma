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

import static org.seasar.doma.internal.util.AssertionUtil.*;

import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.tools.Diagnostic.Kind;

import org.seasar.doma.MessageCode;

/**
 * @author taedium
 * 
 */
public final class Notifier {

    public static void debug(ProcessingEnvironment env,
            MessageCode messageCode, Object... args) {
        assertNotNull(env, messageCode, args);
        Messager messager = env.getMessager();
        messager.printMessage(Kind.OTHER, messageCode.getMessage(args));
    }

    public static void debug(ProcessingEnvironment env, CharSequence message) {
        assertNotNull(env, message);
        Messager messager = env.getMessager();
        messager.printMessage(Kind.OTHER, message);
    }

    public static void notify(ProcessingEnvironment env, Kind kind,
            MessageCode messageCode, Object... args) {
        assertNotNull(env, messageCode, args);
        Messager messager = env.getMessager();
        messager.printMessage(kind, messageCode.getMessage(args));
    }

    public static void notify(ProcessingEnvironment env, Kind kind,
            MessageCode messageCode, Element element, Object... args) {
        assertNotNull(env, kind, element, args);
        Messager messager = env.getMessager();
        messager.printMessage(kind, messageCode.getMessage(args), element);
    }

    public static void notify(ProcessingEnvironment env, AptException e) {
        assertNotNull(env, e);
        Messager messager = env.getMessager();
        messager.printMessage(e.getKind(), e.getMessage(), e.getElement(), e
                .getAnnotationMirror(), e.getAnnotationValue());
    }

}
