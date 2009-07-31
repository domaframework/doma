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

import static org.seasar.doma.internal.util.AssertionUtil.*;

import java.util.List;
import java.util.Set;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.TypeParameterElement;
import javax.lang.model.type.ArrayType;
import javax.lang.model.type.NoType;
import javax.lang.model.type.PrimitiveType;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.TypeKindVisitor6;

import org.seasar.doma.Entity;
import org.seasar.doma.domain.Domain;
import org.seasar.doma.internal.apt.AptException;
import org.seasar.doma.internal.apt.TypeUtil;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.IterationCallback;
import org.seasar.doma.jdbc.Options;
import org.seasar.doma.message.DomaMessageCode;

/**
 * @author taedium
 * 
 */
public abstract class AbstractQueryMetaFactory<M extends AbstractQueryMeta>
        implements QueryMetaFactory {

    protected final ProcessingEnvironment env;

    public AbstractQueryMetaFactory(ProcessingEnvironment env) {
        assertNotNull(env);
        this.env = env;
    }

    protected void doTypeParameters(M queryMeta, ExecutableElement method,
            DaoMeta daoMeta) {
        for (TypeParameterElement element : method.getTypeParameters()) {
            String name = TypeUtil.getTypeName(element.asType(), daoMeta
                    .getTypeParameterMap(), env);
            queryMeta.addTypeParameterName(name);
        }
    }

    protected abstract void doReturnType(M queryMeta, ExecutableElement method,
            DaoMeta daoMeta);

    protected abstract void doParameters(M queryMeta, ExecutableElement method,
            DaoMeta daoMeta);

    protected void doThrowTypes(M queryMeta, ExecutableElement method,
            DaoMeta daoMeta) {
        for (TypeMirror thrownType : method.getThrownTypes()) {
            queryMeta
                    .addThrownTypeName(TypeUtil.getTypeName(thrownType, daoMeta
                            .getTypeParameterMap(), env));
        }
    }

    protected boolean isPrimitiveInt(TypeMirror typeMirror) {
        return typeMirror.accept(new TypeKindVisitor6<Boolean, Void>(false) {

            @Override
            public Boolean visitPrimitiveAsInt(PrimitiveType t, Void p) {
                return true;
            }
        }, null);
    }

    protected boolean isPrimitiveIntArray(TypeMirror typeMirror) {
        return typeMirror.accept(new TypeKindVisitor6<Boolean, Void>(false) {

            @Override
            public Boolean visitArray(ArrayType t, Void p) {
                return isPrimitiveInt(t.getComponentType());
            }

        }, null);
    }

    protected boolean isPrimitiveVoid(TypeMirror typeMirror) {
        return typeMirror.accept(new TypeKindVisitor6<Boolean, Void>(false) {

            @Override
            public Boolean visitNoTypeAsVoid(NoType t, Void p) {
                return true;
            }
        }, null);
    }

    protected boolean isVoid(TypeMirror typeMirror) {
        return TypeUtil.isAssignable(typeMirror, Void.class, env);
    }

    protected boolean isEntity(TypeMirror typeMirror, DaoMeta daoMeta) {
        TypeElement typeElement = TypeUtil.toTypeElement(typeMirror, env);
        return typeElement != null
                && typeElement.getAnnotation(Entity.class) != null;
    }

    protected boolean isDomain(TypeMirror typeMirror) {
        return TypeUtil.isAssignable(typeMirror, Domain.class, env);
    }

    protected boolean isConfig(TypeMirror typeMirror) {
        return TypeUtil.isAssignable(typeMirror, Config.class, env);
    }

    protected boolean isAbstract(TypeMirror typeMirror) {
        TypeElement typeElement = TypeUtil.toTypeElement(typeMirror, env);
        return typeElement != null
                && typeElement.getModifiers().contains(Modifier.ABSTRACT);
    }

    protected boolean isList(TypeMirror typeMirror) {
        TypeElement typeElement = TypeUtil.toTypeElement(typeMirror, env);
        if (typeElement != null) {
            return typeElement.getQualifiedName().contentEquals(List.class
                    .getName());
        }
        return false;
    }

    protected boolean isOptions(TypeMirror typeMirror,
            Class<? extends Options> optionsClass) {
        TypeElement typeElement = TypeUtil.toTypeElement(typeMirror, env);
        if (typeElement != null) {
            TypeElement optionTypeElement = env.getElementUtils()
                    .getTypeElement(optionsClass.getName());
            if (typeElement.equals(optionTypeElement)) {
                return true;
            }
        }
        return false;
    }

    protected boolean isIterationCallback(TypeMirror typeMirror) {
        return TypeUtil.isAssignable(typeMirror, IterationCallback.class, env);
    }

    protected void validateEntityPropertyNames(TypeMirror entityType,
            ExecutableElement method, M queryMeta) {
        String[] includedPropertyNames = queryMeta.getIncludedPropertyNames();
        String[] excludedPropertyNames = queryMeta.getExcludedPropertyNames();
        if (includedPropertyNames != null && includedPropertyNames.length > 0
                || excludedPropertyNames != null
                && excludedPropertyNames.length > 0) {
            EntityPropertyNameCollector collector = new EntityPropertyNameCollector(
                    env);
            Set<String> names = collector.collect(entityType);
            for (String included : queryMeta.getIncludedPropertyNames()) {
                if (!names.contains(included)) {
                    throw new AptException(DomaMessageCode.DOMA4084, env,
                            method, included, entityType);
                }
            }
            for (String excluded : queryMeta.getExcludedPropertyNames()) {
                if (!names.contains(excluded)) {
                    throw new AptException(DomaMessageCode.DOMA4085, env,
                            method, excluded, entityType);
                }
            }
        }
    }
}
