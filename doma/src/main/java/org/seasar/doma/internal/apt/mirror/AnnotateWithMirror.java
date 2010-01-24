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
package org.seasar.doma.internal.apt.mirror;

import static org.seasar.doma.internal.util.AssertionUtil.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;

import org.seasar.doma.AnnotateWith;
import org.seasar.doma.internal.apt.util.AnnotationValueUtil;
import org.seasar.doma.internal.apt.util.ElementUtil;

/**
 * @author taedium
 * 
 */
public class AnnotateWithMirror {

    protected final javax.lang.model.element.AnnotationMirror annotationMirror;

    protected AnnotationValue annotations;

    protected List<AnnotationMirror> annotationsValue;

    protected AnnotateWithMirror(
            javax.lang.model.element.AnnotationMirror annotationMirror) {
        assertNotNull(annotationMirror);
        this.annotationMirror = annotationMirror;
    }

    public static AnnotateWithMirror newInstance(TypeElement clazz,
            ProcessingEnvironment env) {
        assertNotNull(env);
        javax.lang.model.element.AnnotationMirror annotationMirror = ElementUtil
                .getAnnotationMirror(clazz, AnnotateWith.class, env);
        if (annotationMirror == null) {
            return null;
        }
        AnnotateWithMirror result = new AnnotateWithMirror(annotationMirror);
        for (Map.Entry<? extends ExecutableElement, ? extends AnnotationValue> entry : env
                .getElementUtils().getElementValuesWithDefaults(
                        annotationMirror).entrySet()) {
            String name = entry.getKey().getSimpleName().toString();
            AnnotationValue value = entry.getValue();
            if ("annotations".equals(name)) {
                result.annotations = value;
                result.annotationsValue = new ArrayList<AnnotationMirror>();
                for (javax.lang.model.element.AnnotationMirror a : AnnotationValueUtil
                        .toAnnotationList(value)) {
                    result.annotationsValue.add(AnnotationMirror.newInstance(a,
                            env));
                }
            }
        }
        return result;
    }

    public javax.lang.model.element.AnnotationMirror getAnnotationMirror() {
        return annotationMirror;
    }

    public AnnotationValue getAnnotations() {
        return annotations;
    }

    public List<AnnotationMirror> getAnnotationsValue() {
        return annotationsValue;
    }
}
