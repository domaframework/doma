/*
 * Copyright 2004-2009 the Seasar Foundation and the Others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.seasar.doma.internal.apt.meta;

import java.util.Map;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.ExecutableElement;

import org.seasar.doma.internal.apt.util.AnnotationValueUtil;

/**
 * @author taedium
 * 
 */
public class AutoProcedureQueryMeta extends AutoModuleQueryMeta {

    protected String procedureName;

    public String getProcedureName() {
        return procedureName;
    }

    @Override
    public <R, P> R accept(QueryMetaVisitor<R, P> visitor, P p) {
        return visitor.visitAutoProcedureQueryMeta(this, p);
    }

    public void setProcedureName(AnnotationMirror mirror,
            ProcessingEnvironment env) {
        super.setAnnotationMirror(mirror, env);

        String catalog = null;
        String schema = null;
        String name = null;

        for (Map.Entry<? extends ExecutableElement, ? extends AnnotationValue> entry : mirror
                .getElementValues().entrySet()) {
            String elementName = entry.getKey().getSimpleName().toString();
            if ("catalog".equals(elementName)) {
                catalog = AnnotationValueUtil.toString(entry.getValue());
            } else if ("schema".equals(elementName)) {
                schema = AnnotationValueUtil.toString(entry.getValue());
            } else if ("name".equals(elementName)) {
                name = AnnotationValueUtil.toString(entry.getValue());
            }
        }

        StringBuilder buf = new StringBuilder();
        if (catalog != null && !catalog.isEmpty()) {
            buf.append(catalog);
            buf.append(".");
        }
        if (schema != null && !schema.isEmpty()) {
            buf.append(schema);
            buf.append(".");
        }
        if (name != null && !name.isEmpty()) {
            buf.append(name);
        } else {
            buf.append(this.name);
        }

        procedureName = buf.toString();
    }

}
