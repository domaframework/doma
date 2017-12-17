package org.seasar.doma.internal.apt.reflection;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import javax.lang.model.element.AnnotationMirror;

/**
 * @author nakamura
 *
 */
public class SqlProcessorReflection extends AbstractReflection {

    SqlProcessorReflection(AnnotationMirror annotationMirror) {
        super(annotationMirror);
        assertNotNull(annotationMirror);
    }

}
