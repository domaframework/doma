package org.seasar.doma.internal.apt.reflection;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;
import static org.seasar.doma.internal.util.AssertionUtil.assertNotNullValue;

import java.util.Map;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;

import org.seasar.doma.internal.apt.AptIllegalStateException;
import org.seasar.doma.internal.apt.util.AnnotationValueUtil;

/**
 * @author taedium
 * 
 */
public class TableReflection extends AbstractReflection {

    public static final String QUOTE = "quote";

    public static final String NAME = "name";

    public static final String SCHEMA = "schema";

    public static final String CATALOG = "catalog";

    private final AnnotationValue catalog;

    private final AnnotationValue schema;

    private final AnnotationValue name;

    private final AnnotationValue quote;

    TableReflection(AnnotationMirror annotationMirror, Map<String, AnnotationValue> values) {
        super(annotationMirror);
        assertNotNull(values);
        this.catalog = assertNotNullValue(values, CATALOG);
        this.schema = assertNotNullValue(values, SCHEMA);
        this.name = assertNotNullValue(values, NAME);
        this.quote = assertNotNullValue(values, QUOTE);
    }

    public String getCatalogValue() {
        String value = AnnotationValueUtil.toString(catalog);
        if (value == null) {
            throw new AptIllegalStateException(CATALOG);
        }
        return value;
    }

    public String getSchemaValue() {
        String value = AnnotationValueUtil.toString(schema);
        if (value == null) {
            throw new AptIllegalStateException(CATALOG);
        }
        return value;
    }

    public String getNameValue() {
        String value = AnnotationValueUtil.toString(name);
        if (value == null) {
            throw new AptIllegalStateException(NAME);
        }
        return value;
    }

    public boolean getQuoteValue() {
        Boolean value = AnnotationValueUtil.toBoolean(quote);
        if (value == null) {
            throw new AptIllegalStateException(QUOTE);
        }
        return value;
    }

}
