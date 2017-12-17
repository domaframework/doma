package org.seasar.doma.internal.apt.reflection;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;
import static org.seasar.doma.internal.util.AssertionUtil.assertNotNullValue;

import java.util.Map;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;

import org.seasar.doma.internal.apt.AptIllegalStateException;
import org.seasar.doma.internal.apt.util.AnnotationValueUtil;
import org.seasar.doma.jdbc.entity.NamingType;

/**
 * @author taedium
 * 
 */
public class EntityReflection extends AbstractReflection {

    public static final String LISTENER = "listener";

    public static final String NAMING = "naming";

    public static final String IMMUTABLE = "immutable";

    private final AnnotationValue listener;

    private final AnnotationValue naming;

    private final AnnotationValue immutable;

    EntityReflection(AnnotationMirror annotationMirror, Map<String, AnnotationValue> values) {
        super(annotationMirror);
        assertNotNull(values);
        this.listener = assertNotNullValue(values, LISTENER);
        this.naming = assertNotNullValue(values, NAMING);
        this.immutable = assertNotNullValue(values, IMMUTABLE);
    }

    public AnnotationValue getListener() {
        return listener;
    }

    public AnnotationValue getNaming() {
        return naming;
    }

    public AnnotationValue getImmutable() {
        return immutable;
    }

    public TypeMirror getListenerValue() {
        TypeMirror result = AnnotationValueUtil.toType(listener);
        if (result == null) {
            throw new AptIllegalStateException(LISTENER);
        }
        return result;
    }

    public NamingType getNamingValue() {
        VariableElement enumConstant = AnnotationValueUtil.toEnumConstant(naming);
        if (enumConstant == null) {
            throw new AptIllegalStateException(NAMING);
        }
        return NamingType.valueOf(enumConstant.getSimpleName().toString());
    }

    public boolean getImmutableValue() {
        Boolean result = AnnotationValueUtil.toBoolean(immutable);
        if (result == null) {
            throw new AptIllegalStateException(IMMUTABLE);
        }
        return result.booleanValue();
    }

}
