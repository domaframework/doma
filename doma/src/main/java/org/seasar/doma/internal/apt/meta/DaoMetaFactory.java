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

import java.util.ArrayList;
import java.util.List;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.util.ElementFilter;
import javax.tools.Diagnostic.Kind;

import org.seasar.doma.DaoMethod;
import org.seasar.doma.internal.apt.AptException;
import org.seasar.doma.internal.apt.AptIllegalStateException;
import org.seasar.doma.internal.apt.Notifier;
import org.seasar.doma.internal.apt.Options;
import org.seasar.doma.internal.apt.mirror.AnnotateWithMirror;
import org.seasar.doma.internal.apt.mirror.DaoMirror;
import org.seasar.doma.internal.apt.util.TypeMirrorUtil;
import org.seasar.doma.internal.message.Message;
import org.seasar.doma.jdbc.ConfigProxy;

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

    public DaoMeta createDaoMeta(TypeElement interfaceElement) {
        assertNotNull(interfaceElement);
        DaoMirror daoMirror = DaoMirror.newInstance(interfaceElement, env);
        if (daoMirror == null) {
            throw new AptIllegalStateException("daoMirror");
        }
        DaoMeta daoMeta = new DaoMeta(daoMirror);
        doDaoElement(interfaceElement, daoMeta);
        doMethodElements(interfaceElement, daoMeta);
        return daoMeta;
    }

    protected void doDaoElement(TypeElement interfaceElement, DaoMeta daoMeta) {
        validateInterface(interfaceElement, daoMeta);

        String name = interfaceElement.getSimpleName().toString();
        String suffix = Options.getDaoSuffix(env);
        if (name.endsWith(suffix)) {
            Notifier.notify(env, Kind.WARNING, Message.DOMA4026,
                    interfaceElement, suffix);
        }
        daoMeta.setName(name);
        daoMeta.setDaoElement(interfaceElement);
        daoMeta.setDaoType(interfaceElement.asType());
        if (TypeMirrorUtil.isSameType(daoMeta.getConfigType(),
                ConfigProxy.class, env)) {
            daoMeta.setConfigProxied(true);
        }
        doAnnotateWith(daoMeta);
    }

    protected void validateInterface(TypeElement interfaceElement,
            DaoMeta daoMeta) {
        if (!interfaceElement.getKind().isInterface()) {
            DaoMirror daoMirror = daoMeta.getDaoMirror();
            throw new AptException(Message.DOMA4014, env,
                    interfaceElement, daoMirror.getAnnotationMirror());
        }
        if (interfaceElement.getNestingKind().isNested()) {
            throw new AptException(Message.DOMA4017, env,
                    interfaceElement);
        }
        if (!interfaceElement.getInterfaces().isEmpty()) {
            throw new AptException(Message.DOMA4045, env,
                    interfaceElement);
        }
        if (!interfaceElement.getTypeParameters().isEmpty()) {
            throw new AptException(Message.DOMA4059, env,
                    interfaceElement);
        }
    }

    protected void doAnnotateWith(DaoMeta daoMeta) {
        AnnotateWithMirror annotateWithMirror = AnnotateWithMirror.newInstance(
                daoMeta.getDaoElement(), env);
        if (annotateWithMirror == null) {
            return;
        }
        if (daoMeta.isConfigProxied()) {
            daoMeta.setAnnotateWithMirror(annotateWithMirror);
        } else {
            DaoMirror daoMirror = daoMeta.getDaoMirror();
            throw new AptException(Message.DOMA4142, env, daoMeta
                    .getDaoElement(), daoMirror.getAnnotationMirror(),
                    daoMirror.getConfig());
        }
    }

    protected void doMethodElements(TypeElement interfaceElement,
            DaoMeta daoMeta) {
        for (ExecutableElement methodElement : ElementFilter
                .methodsIn(interfaceElement.getEnclosedElements())) {
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
            TypeElement typeElement = TypeMirrorUtil.toTypeElement(
                    declaredType, env);
            if (typeElement.getAnnotation(DaoMethod.class) != null) {
                if (foundAnnotationTypeElement != null) {
                    throw new AptException(Message.DOMA4086, env,
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
        throw new AptException(Message.DOMA4005, env, method);
    }

}
