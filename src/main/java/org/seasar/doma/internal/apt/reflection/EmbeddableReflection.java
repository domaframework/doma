package org.seasar.doma.internal.apt.reflection;

import javax.lang.model.element.AnnotationMirror;

/**
 * @author nakamura-to
 *
 */
public class EmbeddableReflection extends AbstractReflection {

    EmbeddableReflection(AnnotationMirror annotationMirror) {
        super(annotationMirror);
    }

}
