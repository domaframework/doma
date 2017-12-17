package org.seasar.doma.internal.apt.reflection;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;
import static org.seasar.doma.internal.util.AssertionUtil.assertNotNullValue;

import java.util.Map;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;

import org.seasar.doma.AccessLevel;
import org.seasar.doma.internal.apt.AptIllegalStateException;
import org.seasar.doma.internal.apt.util.AnnotationValueUtil;

/**
 * @author taedium
 * 
 */
public class DaoReflection extends AbstractReflection {

    public static final String ACCESS_LEVEL = "accessLevel";

    public static final String CONFIG = "config";

    private final AnnotationValue config;

    private final AnnotationValue accessLevel;

    private final boolean hasUserDefinedConfig;

    DaoReflection(AnnotationMirror annotationMirror, Map<String, AnnotationValue> values) {
        super(annotationMirror);
        assertNotNull(values);
        this.config = assertNotNullValue(values, CONFIG);
        this.accessLevel = assertNotNullValue(values, ACCESS_LEVEL);
        this.hasUserDefinedConfig = annotationMirror.getElementValues().keySet().stream().anyMatch(
                e -> e.getSimpleName().contentEquals(CONFIG));
    }

    public AnnotationValue getConfig() {
        return config;
    }

    public AnnotationValue getAccessLevel() {
        return accessLevel;
    }

    public TypeMirror getConfigValue() {
        TypeMirror value = AnnotationValueUtil.toType(config);
        if (value == null) {
            throw new AptIllegalStateException(CONFIG);
        }
        return value;
    }

    public AccessLevel getAccessLevelValue() {
        VariableElement enumConstant = AnnotationValueUtil.toEnumConstant(accessLevel);
        if (enumConstant == null) {
            throw new AptIllegalStateException(ACCESS_LEVEL);
        }
        return AccessLevel.valueOf(enumConstant.getSimpleName().toString());
    }

    public boolean hasUserDefinedConfig() {
        return hasUserDefinedConfig;
    }

}
