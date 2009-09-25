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

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;

import org.seasar.doma.In;
import org.seasar.doma.InOut;
import org.seasar.doma.Out;
import org.seasar.doma.ResultSet;
import org.seasar.doma.internal.apt.AptException;
import org.seasar.doma.internal.apt.AptIllegalStateException;
import org.seasar.doma.internal.apt.type.CollectionType;
import org.seasar.doma.internal.apt.type.DomainType;
import org.seasar.doma.internal.apt.type.EntityType;
import org.seasar.doma.internal.apt.type.ReferenceType;
import org.seasar.doma.internal.apt.type.ValueType;
import org.seasar.doma.message.DomaMessageCode;

/**
 * @author taedium
 * 
 */
public abstract class AutoModuleQueryMetaFactory<M extends AutoModuleQueryMeta>
        extends AbstractQueryMetaFactory<M> {

    public AutoModuleQueryMetaFactory(ProcessingEnvironment env) {
        super(env);
    }

    @Override
    protected void doParameters(M queryMeta, ExecutableElement method,
            DaoMeta daoMeta) {
        for (VariableElement parameter : method.getParameters()) {
            QueryParameterMeta parameterMeta = createParameterMeta(parameter);
            queryMeta.addParameterMetas(parameterMeta);

            CallableSqlParameterMeta callableSqlParameterMeta = createParameterMeta(parameterMeta);
            queryMeta.addCallableSqlParameterMeta(callableSqlParameterMeta);

            queryMeta.addExpressionParameterType(parameterMeta.getName(),
                    parameterMeta.getType());
        }
    }

    protected CallableSqlParameterMeta createParameterMeta(
            QueryParameterMeta parameterMeta) {
        if (parameterMeta.isAnnotated(ResultSet.class)) {
            CollectionType collectionType = parameterMeta.getCollectionType();
            if (collectionType == null) {
                throw new AptException(DomaMessageCode.DOMA4062, env,
                        parameterMeta.getElement());
            }
            EntityType entityType = collectionType.getEntityType();
            if (entityType != null) {
                return new EntityListParameterMeta(parameterMeta.getName(),
                        entityType);
            }
            DomainType domainType = collectionType.getDomainType();
            if (domainType != null) {
                return new DomainListParameterMeta(parameterMeta.getName(),
                        domainType);
            }
            ValueType valueType = collectionType.getValueType();
            if (valueType != null) {
                return new ValueListParameterMeta(parameterMeta.getName(),
                        valueType);
            }
            throw new AptIllegalStateException();
        }
        if (parameterMeta.isAnnotated(Out.class)) {
            ReferenceType referenceType = parameterMeta.getReferenceType();
            if (referenceType == null) {
                throw new AptException(DomaMessageCode.DOMA4098, env,
                        parameterMeta.getElement());
            }
            ValueType valueType = referenceType.getReferentValueType();
            if (valueType == null) {
                throw new AptException(DomaMessageCode.DOMA4100, env,
                        parameterMeta.getElement(), referenceType
                                .getReferentType());
            }
            return new OutParameterMeta(parameterMeta.getName(), valueType);
        }
        if (parameterMeta.isAnnotated(InOut.class)) {
            ReferenceType referenceType = parameterMeta.getReferenceType();
            if (referenceType == null) {
                throw new AptException(DomaMessageCode.DOMA4111, env,
                        parameterMeta.getElement());
            }
            ValueType valueType = referenceType.getReferentValueType();
            if (valueType == null) {
                throw new AptException(DomaMessageCode.DOMA4100, env,
                        parameterMeta.getElement(), referenceType
                                .getReferentType());
            }
            return new InOutParameterMeta(parameterMeta.getName(), valueType);
        }
        if (parameterMeta.isAnnotated(In.class)) {
            ValueType valueType = parameterMeta.getValueType();
            if (valueType != null) {
                return new InParameterMeta(parameterMeta.getName(), valueType);
            }
            throw new AptException(DomaMessageCode.DOMA4101, env, parameterMeta
                    .getElement(), parameterMeta.getType());
        }
        throw new AptException(DomaMessageCode.DOMA4066, env, parameterMeta
                .getElement());
    }
}
