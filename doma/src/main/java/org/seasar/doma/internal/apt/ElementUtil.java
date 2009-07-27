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

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.ExecutableType;
import javax.lang.model.util.SimpleTypeVisitor6;

import org.seasar.doma.ParameterName;

/**
 * @author taedium
 * 
 */
public class ElementUtil {

    public static String getParameterName(VariableElement variableElement) {
        assertNotNull(variableElement);
        ParameterName parameterName = variableElement
                .getAnnotation(ParameterName.class);
        if (parameterName != null && !parameterName.value().isEmpty()) {
            return parameterName.value();
        }
        return variableElement.getSimpleName().toString();
    }

    public static boolean isEnclosing(Element enclosingElement,
            Element enclosedElement) {
        assertNotNull(enclosingElement, enclosedElement);
        if (enclosingElement.equals(enclosedElement)) {
            return true;
        }
        for (Element e = enclosedElement; e != null; e = e
                .getEnclosingElement()) {
            if (enclosingElement.equals(e)) {
                return true;
            }
        }
        return false;
    }

    public static ExecutableType toExecutableType(ExecutableElement element,
            ProcessingEnvironment env) {
        assertNotNull(element, env);
        return element.asType()
                .accept(new SimpleTypeVisitor6<ExecutableType, Void>() {

                    @Override
                    public ExecutableType visitExecutable(ExecutableType t,
                            Void p) {
                        return t;
                    }
                }, null);
    }
}
