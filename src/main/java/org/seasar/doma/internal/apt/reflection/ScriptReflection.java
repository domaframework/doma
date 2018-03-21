package org.seasar.doma.internal.apt.reflection;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;
import static org.seasar.doma.internal.util.AssertionUtil.assertNotNullValue;

import java.util.Map;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.VariableElement;

import org.seasar.doma.internal.apt.AptIllegalStateException;
import org.seasar.doma.internal.apt.util.AnnotationValueUtil;
import org.seasar.doma.jdbc.SqlLogType;

/**
 * @author taedium
 * 
 */
public class ScriptReflection extends AbstractReflection {

    public static final String SQL_LOG = "sqlLog";

    public static final String BLOCK_DELIMITER = "blockDelimiter";

    public static final String HALT_ON_ERROR = "haltOnError";

    private final AnnotationValue haltOnError;

    private final AnnotationValue blockDelimiter;

    private final AnnotationValue sqlLog;

    ScriptReflection(AnnotationMirror annotationMirror, Map<String, AnnotationValue> values) {
        super(annotationMirror);
        assertNotNull(values);
        this.haltOnError = assertNotNullValue(values, HALT_ON_ERROR);
        this.blockDelimiter = assertNotNullValue(values, BLOCK_DELIMITER);
        this.sqlLog = assertNotNullValue(values, SQL_LOG);
    }

    public AnnotationValue getHaltOnError() {
        return haltOnError;
    }

    public AnnotationValue getBlockDelimiter() {
        return blockDelimiter;
    }

    public AnnotationValue getSqlLog() {
        return sqlLog;
    }

    public boolean getHaltOnErrorValue() {
        Boolean value = AnnotationValueUtil.toBoolean(haltOnError);
        if (value == null) {
            throw new AptIllegalStateException(HALT_ON_ERROR);
        }
        return value;
    }

    public String getBlockDelimiterValue() {
        String value = AnnotationValueUtil.toString(blockDelimiter);
        if (value == null) {
            throw new AptIllegalStateException(BLOCK_DELIMITER);
        }
        return value;
    }

    public SqlLogType getSqlLogValue() {
        VariableElement enumConstant = AnnotationValueUtil.toEnumConstant(sqlLog);
        if (enumConstant == null) {
            throw new AptIllegalStateException(SQL_LOG);
        }
        return SqlLogType.valueOf(enumConstant.getSimpleName().toString());
    }

}
