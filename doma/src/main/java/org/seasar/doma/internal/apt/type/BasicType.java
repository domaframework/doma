/*
 * Copyright 2004-2010 the Seasar Foundation and the Others.
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
package org.seasar.doma.internal.apt.type;

import static org.seasar.doma.internal.util.AssertionUtil.*;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.type.TypeMirror;

public class BasicType extends AbstractDataType {

    protected WrapperType wrapperType;

    public BasicType(TypeMirror type, ProcessingEnvironment env) {
        super(type, env);
    }

    public WrapperType getWrapperType() {
        return wrapperType;
    }

    public String getDefaultValue() {
        switch (typeMirror.getKind()) {
        case BOOLEAN:
            return String.valueOf(false);
        case BYTE:
        case SHORT:
        case INT:
        case LONG:
        case FLOAT:
        case DOUBLE:
        case CHAR:
            return "0";
        default:
            return "null";
        }
    }

    public static BasicType newInstance(TypeMirror type,
            ProcessingEnvironment env) {
        assertNotNull(type, env);
        BasicType basicType = new BasicType(type, env);
        WrapperType wrapperType = WrapperType.newInstance(basicType, env);
        if (wrapperType == null) {
            return null;
        }
        basicType.wrapperType = wrapperType;
        return basicType;
    }

    @Override
    public <R, P, TH extends Throwable> R accept(
            DataTypeVisitor<R, P, TH> visitor, P p) throws TH {
        return visitor.visitBasicType(this, p);
    }

}
