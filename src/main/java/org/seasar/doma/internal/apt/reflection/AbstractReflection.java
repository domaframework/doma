package org.seasar.doma.internal.apt.reflection;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import javax.lang.model.element.AnnotationMirror;

public abstract class AbstractReflection {

    private final AnnotationMirror annotationMirror;

    AbstractReflection(AnnotationMirror annotationMirror) {
        assertNotNull(annotationMirror);
        this.annotationMirror = annotationMirror;
    }

    public AnnotationMirror getAnnotationMirror() {
        return annotationMirror;
    }

}
