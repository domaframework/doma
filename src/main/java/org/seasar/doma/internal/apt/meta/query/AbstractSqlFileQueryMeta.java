package org.seasar.doma.internal.apt.meta.query;

import javax.lang.model.element.ExecutableElement;

/**
 * @author taedium
 * 
 */
public abstract class AbstractSqlFileQueryMeta extends AbstractQueryMeta {

    protected AbstractSqlFileQueryMeta(ExecutableElement method) {
        super(method);
    }
}
