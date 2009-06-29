package org.seasar.doma.internal.apt.meta;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.lang.model.type.TypeMirror;

/**
 * @author taedium
 * 
 */
public abstract class AbstractSqlFileQueryMeta extends AbstractQueryMeta
        implements SqlFileQueryMeta {

    protected Map<String, String> parameters = new LinkedHashMap<String, String>();

    protected Map<String, TypeMirror> parameterTypes = new LinkedHashMap<String, TypeMirror>();

    public Iterator<Map.Entry<String, String>> getParameters() {
        return parameters.entrySet().iterator();
    }

    public void addParameter(String parameterName, String parameterTypeName) {
        this.parameters.put(parameterName, parameterTypeName);
    }

    public TypeMirror getParameterType(String parameterName) {
        return parameterTypes.get(parameterName);
    }

    public void addParameterType(String parameterName, TypeMirror parameterType) {
        this.parameterTypes.put(parameterName, parameterType);
    }

}
