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
package org.seasar.doma.internal.apt.reflection;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;

/**
 * @author taedium
 * 
 */
public class AnnotateWithReflection extends AbstractReflection {

    private final AnnotationValue annotations;

    private final List<AnnotationReflection> annotationsValue;

    AnnotateWithReflection(AnnotationMirror annotationMirror, AnnotationValue annotations,
            ArrayList<AnnotationReflection> annotationsValues) {
        super(annotationMirror);
        assertNotNull(annotations, annotationsValues);
        this.annotations = annotations;
        this.annotationsValue = Collections.unmodifiableList(annotationsValues);
    }

    public AnnotationValue getAnnotations() {
        return annotations;
    }

    public List<AnnotationReflection> getAnnotationsValue() {
        return annotationsValue;
    }
}
