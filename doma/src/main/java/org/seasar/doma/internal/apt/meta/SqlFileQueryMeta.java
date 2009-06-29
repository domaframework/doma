package org.seasar.doma.internal.apt.meta;

import javax.lang.model.type.TypeMirror;

/**
 * @author taedium
 * 
 */
public interface SqlFileQueryMeta {

    TypeMirror getParameterType(String parameterName);
}
