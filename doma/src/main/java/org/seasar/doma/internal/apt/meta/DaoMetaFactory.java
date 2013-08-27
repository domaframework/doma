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

import java.io.File;
import java.io.FilenameFilter;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;
import javax.tools.Diagnostic.Kind;
import javax.tools.FileObject;
import javax.tools.StandardLocation;

import org.seasar.doma.DaoMethod;
import org.seasar.doma.Suppress;
import org.seasar.doma.internal.Constants;
import org.seasar.doma.internal.apt.AptException;
import org.seasar.doma.internal.apt.AptIllegalStateException;
import org.seasar.doma.internal.apt.Notifier;
import org.seasar.doma.internal.apt.Options;
import org.seasar.doma.internal.apt.mirror.AnnotateWithMirror;
import org.seasar.doma.internal.apt.mirror.DaoMirror;
import org.seasar.doma.internal.apt.util.ElementUtil;
import org.seasar.doma.internal.apt.util.TypeMirrorUtil;
import org.seasar.doma.internal.jdbc.util.SqlFileUtil;
import org.seasar.doma.message.Message;

/**
 * @author taedium
 * 
 */
public class DaoMetaFactory implements TypeElementMetaFactory<DaoMeta> {

    protected final ProcessingEnvironment env;

    protected final List<QueryMetaFactory> queryMetaFactories = new ArrayList<QueryMetaFactory>();

    public DaoMetaFactory(ProcessingEnvironment env,
            List<QueryMetaFactory> commandMetaFactories) {
        assertNotNull(env, commandMetaFactories);
        this.env = env;
        this.queryMetaFactories.addAll(commandMetaFactories);
    }

    @Override
    public DaoMeta createTypeElementMeta(TypeElement interfaceElement) {
        assertNotNull(interfaceElement);
        DaoMirror daoMirror = DaoMirror.newInstance(interfaceElement, env);
        if (daoMirror == null) {
            throw new AptIllegalStateException("daoMirror");
        }
        DaoMeta daoMeta = new DaoMeta(daoMirror);
        doDaoElement(interfaceElement, daoMeta);
        doMethodElements(interfaceElement, daoMeta);
        validateFiles(interfaceElement, daoMeta);
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
        doAnnotateWith(daoMeta);
        doParentDao(daoMeta);

        DaoMirror daoMirror = daoMeta.getDaoMirror();
        if (daoMirror.hasUserDefinedConfig()) {
            TypeElement configElement = TypeMirrorUtil.toTypeElement(
                    daoMirror.getConfigValue(), env);
            if (configElement == null) {
                throw new AptIllegalStateException(
                        "failed to convert to TypeElement.");
            }
            if (configElement.getModifiers().contains(Modifier.ABSTRACT)) {
                throw new AptException(Message.DOMA4163, env,
                        daoMeta.getDaoElement(),
                        daoMirror.getAnnotationMirror(), daoMirror.getConfig(),
                        configElement.getQualifiedName());
            }
            ExecutableElement constructor = ElementUtil.getNoArgConstructor(
                    configElement, env);
            if (constructor == null
                    || !constructor.getModifiers().contains(Modifier.PUBLIC)) {
                throw new AptException(Message.DOMA4164, env,
                        daoMeta.getDaoElement(),
                        daoMirror.getAnnotationMirror(), daoMirror.getConfig(),
                        configElement.getQualifiedName());
            }
        }
    }

    protected void validateInterface(TypeElement interfaceElement,
            DaoMeta daoMeta) {
        if (!interfaceElement.getKind().isInterface()) {
            DaoMirror daoMirror = daoMeta.getDaoMirror();
            throw new AptException(Message.DOMA4014, env, interfaceElement,
                    daoMirror.getAnnotationMirror());
        }
        if (interfaceElement.getNestingKind().isNested()) {
            throw new AptException(Message.DOMA4017, env, interfaceElement);
        }
        if (!interfaceElement.getTypeParameters().isEmpty()) {
            throw new AptException(Message.DOMA4059, env, interfaceElement);
        }
    }

    protected void doAnnotateWith(DaoMeta daoMeta) {
        AnnotateWithMirror annotateWithMirror = AnnotateWithMirror.newInstance(
                daoMeta.getDaoElement(), env);
        if (annotateWithMirror != null) {
            daoMeta.setAnnotateWithMirror(annotateWithMirror);
        }
    }

    protected void doParentDao(DaoMeta daoMeta) {
        List<? extends TypeMirror> interfaces = daoMeta.getDaoElement()
                .getInterfaces();
        int size = interfaces.size();
        if (size == 0) {
            return;
        }
        if (size > 1) {
            throw new AptException(Message.DOMA4187, env,
                    daoMeta.getDaoElement());
        }
        TypeMirror parentMirror = interfaces.get(0);
        TypeElement parentElement = TypeMirrorUtil.toTypeElement(parentMirror,
                env);
        if (parentElement == null) {
            throw new AptIllegalStateException(
                    "failed to convert to TypeElement.");
        }
        DaoMirror daoMirror = DaoMirror.newInstance(parentElement, env);
        if (daoMirror == null) {
            throw new AptException(Message.DOMA4188, env,
                    daoMeta.getDaoElement(), parentElement.getQualifiedName());
        }
        ParentDaoMeta parentDaoMeta = new ParentDaoMeta(daoMirror);
        parentDaoMeta.setDaoType(parentMirror);
        parentDaoMeta.setDaoElement(parentElement);
        daoMeta.setParentDaoMeta(parentDaoMeta);
    }

    protected void doMethodElements(TypeElement interfaceElement,
            DaoMeta daoMeta) {
        for (ExecutableElement methodElement : ElementFilter
                .methodsIn(interfaceElement.getEnclosedElements())) {
            try {
                doMethodElement(methodElement, daoMeta);
            } catch (AptException e) {
                Notifier.notify(env, e);
                daoMeta.setError(true);
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
                            methodElement,
                            foundAnnotationTypeElement.getQualifiedName(),
                            typeElement.getQualifiedName());
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

    protected void validateFiles(TypeElement interfaceElement, DaoMeta daoMeta) {
        if (daoMeta.isError()) {
            return;
        }
        if (!Options.getSqlValidation(env)) {
            return;
        }
        String dirPath = SqlFileUtil.buildPath(interfaceElement
                .getQualifiedName().toString());
        Set<String> fileNames = getFileNames(dirPath);
        for (QueryMeta queryMeta : daoMeta.getQueryMetas()) {
            for (String fileName : queryMeta.getFileNames()) {
                fileNames.remove(fileName);
            }
        }
        Suppress suppress = interfaceElement.getAnnotation(Suppress.class);
        Message message = Message.DOMA4220;
        if (!isSuppressed(suppress, message)) {
            for (String fileName : fileNames) {
                Notifier.notify(env, Kind.WARNING, message, interfaceElement,
                        dirPath + "/" + fileName);
            }
        }
    }

    protected Set<String> getFileNames(String dirPath) {
        File dir = getDir(dirPath);
        if (dir == null) {
            return Collections.emptySet();
        }
        String[] fileNames = dir.list(new FilenameFilter() {

            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(Constants.SQL_PATH_SUFFIX)
                        || name.endsWith(Constants.SCRIPT_PATH_SUFFIX);
            }
        });
        return new HashSet<String>(Arrays.asList(fileNames));
    }

    protected File getDir(String dirPath) {
        FileObject fileObject = getFileObject(dirPath);
        if (fileObject == null) {
            return null;
        }
        URI uri = fileObject.toUri();
        if (!uri.isAbsolute()) {
            uri = new File(".").toURI().resolve(uri);
        }
        File dir = new File(uri);
        if (dir.exists() && dir.isDirectory()) {
            return dir;
        }
        return null;
    }

    protected FileObject getFileObject(String path) {
        Filer filer = env.getFiler();
        try {
            return filer.getResource(StandardLocation.CLASS_OUTPUT, "", path);
        } catch (Exception ignored) {
            // Ignore, in case the Filer implementation doesn't support
            // directory path.
            return null;
        }
    }

    protected boolean isSuppressed(Suppress suppress, Message message) {
        if (suppress != null) {
            for (Message suppressMessage : suppress.messages()) {
                if (suppressMessage == message) {
                    return true;
                }
            }
        }
        return false;
    }
}
