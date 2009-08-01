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

import java.util.ArrayList;
import java.util.List;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.MirroredTypeException;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;
import javax.tools.Diagnostic.Kind;

import org.seasar.doma.Dao;
import org.seasar.doma.DaoMethod;
import org.seasar.doma.internal.apt.AptException;
import org.seasar.doma.internal.apt.AptIllegalStateException;
import org.seasar.doma.internal.apt.Notifier;
import org.seasar.doma.internal.apt.Options;
import org.seasar.doma.internal.apt.TypeUtil;
import org.seasar.doma.message.DomaMessageCode;

/**
 * @author taedium
 * 
 */
public class DaoMetaFactory {

    protected final ProcessingEnvironment env;

    protected final List<QueryMetaFactory> queryMetaFactories = new ArrayList<QueryMetaFactory>();

    public DaoMetaFactory(ProcessingEnvironment env,
            List<QueryMetaFactory> commandMetaFactories) {
        assertNotNull(env, commandMetaFactories);
        this.env = env;
        this.queryMetaFactories.addAll(commandMetaFactories);
    }

    public DaoMeta createDaoMeta(TypeElement daoElement) {
        assertNotNull(daoElement);
        DaoMeta daoMeta = new DaoMeta();
        doDaoElement(daoElement, daoMeta);
        doMethodElements(daoElement, daoMeta);
        return daoMeta;
    }

    protected void doDaoElement(TypeElement daoElement, DaoMeta daoMeta) {
        if (daoElement.getNestingKind().isNested()) {
            throw new AptException(DomaMessageCode.DOMA4017, env, daoElement,
                    daoElement.getQualifiedName());
        }
        if (!daoElement.getKind().isInterface()) {
            throw new AptException(DomaMessageCode.DOMA4014, env, daoElement,
                    daoElement.getQualifiedName());
        }
        if (!daoElement.getInterfaces().isEmpty()) {
            throw new AptException(DomaMessageCode.DOMA4045, env, daoElement);
        }
        String name = daoElement.getSimpleName().toString();
        String suffix = Options.getDaoSuffix(env);
        if (name.endsWith(suffix)) {
            Notifier
                    .notify(env, Kind.WARNING, DomaMessageCode.DOMA4026, daoElement, suffix);
        }
        daoMeta.setName(name);
        daoMeta.setDaoElement(daoElement);
        daoMeta.setDaoType(daoElement.asType());
        Dao daoAnnotation = daoElement.getAnnotation(Dao.class);
        if (!daoElement.getTypeParameters().isEmpty()) {
            throw new AptException(DomaMessageCode.DOMA4059, env, daoElement);
        }
        doConfig(daoAnnotation, daoMeta);
    }

    protected void doConfig(Dao daoAnnotation, DaoMeta daoMeta) {
        TypeMirror configType = getConfigType(daoAnnotation, daoMeta);
        daoMeta.setConfigType(configType);
    }

    protected TypeMirror getConfigType(Dao daoAnnotation, DaoMeta daoMeta) {
        try {
            daoAnnotation.config();
        } catch (MirroredTypeException e) {
            return e.getTypeMirror();
        }
        throw new AptIllegalStateException();
    }

    protected void doMethodElements(TypeElement typeElement, DaoMeta daoMeta) {
        for (ExecutableElement methodElement : ElementFilter
                .methodsIn(typeElement.getEnclosedElements())) {
            try {
                doMethodElement(methodElement, daoMeta);
            } catch (AptException e) {
                Notifier.notify(env, e);
            }
        }
    }

    protected void doMethodElement(ExecutableElement methodElement,
            DaoMeta daoMeta) {
        validateMethod(methodElement, daoMeta);
        QueryMeta queryMeta = createQueryMeta(methodElement, daoMeta);
        daoMeta.addQueryMeta(queryMeta);
    }

    protected void validateMethod(ExecutableElement methodElement,
            DaoMeta daoMeta) {
        TypeElement foundAnnotationTypeElement = null;
        for (AnnotationMirror annotation : methodElement.getAnnotationMirrors()) {
            DeclaredType declaredType = annotation.getAnnotationType();
            TypeElement typeElement = TypeUtil.toTypeElement(declaredType, env);
            if (typeElement.getAnnotation(DaoMethod.class) != null) {
                if (foundAnnotationTypeElement != null) {
                    throw new AptException(DomaMessageCode.DOMA4086, env,
                            methodElement, foundAnnotationTypeElement
                                    .getQualifiedName(), typeElement
                                    .getQualifiedName());
                }
                foundAnnotationTypeElement = typeElement;
            }
        }
    }

    protected QueryMeta createQueryMeta(ExecutableElement method,
            DaoMeta daoMeta) {
        for (QueryMetaFactory factory : queryMetaFactories) {
            QueryMeta queryMeta = factory.createQueryMeta(method, daoMeta);
            if (queryMeta != null) {
                return queryMeta;
            }
        }
        throw new AptException(DomaMessageCode.DOMA4005, env, method);
    }

}
