package org.seasar.doma.internal.apt.reflection;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;
import static org.seasar.doma.internal.util.AssertionUtil.assertNotNullValue;

import java.util.Map;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.VariableElement;

import org.seasar.doma.internal.apt.AptIllegalStateException;
import org.seasar.doma.internal.apt.util.AnnotationValueUtil;

/**
 * 
 * @author nakamura-to
 *
 */
public class AllArgsConstructorReflection extends AbstractReflection {

    public static final String STATIC_NAME = "staticName";

    public static final String ACCESS = "access";

    private final AnnotationValue staticName;

    private final AnnotationValue access;

    AllArgsConstructorReflection(AnnotationMirror annotationMirror,
            Map<String, AnnotationValue> values) {
        super(annotationMirror);
        assertNotNull(values);
        this.staticName = assertNotNullValue(values, STATIC_NAME);
        this.access = assertNotNullValue(values, ACCESS);
    }

    public AnnotationValue getStaticName() {
        return staticName;
    }

    public AnnotationValue getAccess() {
        return access;
    }

    public String getStaticNameValue() {
        String value = AnnotationValueUtil.toString(staticName);
        if (value == null) {
            throw new AptIllegalStateException("staticConstructor");
        }
        return value;
    }

    public boolean isAccessPrivate() {
        VariableElement enumConstant = AnnotationValueUtil.toEnumConstant(access);
        if (enumConstant == null) {
            throw new AptIllegalStateException(ACCESS);
        }
        return "PRIVATE".equals(enumConstant.getSimpleName().toString());
    }

    public boolean isAccessNone() {
        VariableElement enumConstant = AnnotationValueUtil.toEnumConstant(access);
        if (enumConstant == null) {
            throw new AptIllegalStateException(ACCESS);
        }
        return "NONE".equals(enumConstant.getSimpleName().toString());
    }

}
