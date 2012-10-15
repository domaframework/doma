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
package org.seasar.doma.internal.apt.meta;

import static org.seasar.doma.internal.util.AssertionUtil.*;

import java.util.List;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;

import org.seasar.doma.internal.apt.util.TypeMirrorUtil;
import org.seasar.doma.jdbc.domain.DomainConverter;

/**
 * @author taedium
 * 
 */
public class ExternalDomainMetaFactory implements
        TypeElementMetaFactory<ExternalDomainMeta> {

    private final ProcessingEnvironment env;

    public ExternalDomainMetaFactory(ProcessingEnvironment env) {
        assertNotNull(env);
        this.env = env;
    }

    @Override
    public ExternalDomainMeta createTypeElementMeta(TypeElement typeElement) {
        // TODO Auto-generated method stub

        TypeMirror[] argumentTypes = getConverterArgumentTypes(typeElement
                .asType());
        if (argumentTypes == null) {
            // TODO error. typeElement is not instanceof DomainConverter
        }

        TypeMirror domainType = argumentTypes[0];
        TypeMirror valueType = argumentTypes[1];

        // get domain type

        // get value type

        // check if value type is basic type

        return null;
    }

    protected TypeMirror[] getConverterArgumentTypes(TypeMirror typeMirror) {
        for (TypeMirror supertype : env.getTypeUtils().directSupertypes(
                typeMirror)) {
            if (!TypeMirrorUtil.isAssignable(supertype, DomainConverter.class,
                    env)) {
                continue;
            }
            if (TypeMirrorUtil
                    .isSameType(supertype, DomainConverter.class, env)) {
                DeclaredType declaredType = TypeMirrorUtil.toDeclaredType(
                        supertype, env);
                assertNotNull(declaredType);
                List<? extends TypeMirror> args = declaredType
                        .getTypeArguments();
                assertEquals(2, args.size());
                return new TypeMirror[] { args.get(0), args.get(1) };
            }
            TypeMirror[] argumentTypes = getConverterArgumentTypes(supertype);
            if (argumentTypes != null) {
                return argumentTypes;
            }
        }
        return null;
    }
}
