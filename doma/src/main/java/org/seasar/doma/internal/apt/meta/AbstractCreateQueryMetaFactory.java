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

import java.util.List;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;

import org.seasar.doma.domain.Domain;
import org.seasar.doma.internal.apt.Models;

/**
 * @author taedium
 * 
 */
public abstract class AbstractCreateQueryMetaFactory extends
        AbstractQueryMetaFactory {

    public AbstractCreateQueryMetaFactory(ProcessingEnvironment env) {
        super(env);
    }

    protected TypeMirror getDomainValueType(TypeMirror domainType) {
        for (TypeMirror supertype : env.getTypeUtils()
                .directSupertypes(domainType)) {
            TypeElement typeElement = Models.toTypeElement(supertype, env);
            if (typeElement == null) {
                continue;
            }
            if (typeElement.getQualifiedName().contentEquals(Domain.class
                    .getName())) {
                DeclaredType declaredType = Models
                        .toDeclaredType(supertype, env);
                if (declaredType == null) {
                    continue;
                }
                List<? extends TypeMirror> args = declaredType
                        .getTypeArguments();
                return args.get(0);
            }
            TypeMirror domainValueType = getDomainValueType(supertype);
            if (domainValueType != null) {
                return domainValueType;
            }
        }
        return null;
    }

}
