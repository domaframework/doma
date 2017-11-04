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

import static java.util.stream.Collectors.toList;
import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.io.File;
import java.io.FilenameFilter;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;
import javax.tools.Diagnostic.Kind;
import javax.tools.FileObject;

import org.seasar.doma.DaoMethod;
import org.seasar.doma.SingletonConfig;
import org.seasar.doma.Suppress;
import org.seasar.doma.internal.Constants;
import org.seasar.doma.internal.apt.AptException;
import org.seasar.doma.internal.apt.AptIllegalStateException;
import org.seasar.doma.internal.apt.Notifier;
import org.seasar.doma.internal.apt.Options;
import org.seasar.doma.internal.apt.mirror.AnnotateWithMirror;
import org.seasar.doma.internal.apt.mirror.DaoMirror;
import org.seasar.doma.internal.apt.util.ElementUtil;
import org.seasar.doma.internal.apt.util.ResourceUtil;
import org.seasar.doma.internal.apt.util.TypeMirrorUtil;
import org.seasar.doma.internal.jdbc.util.SqlFileUtil;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.message.Message;

/**
 * @author taedium
 * 
 */
public class DaoMetaFactory implements TypeElementMetaFactory<DaoMeta> {

    protected static final String SINGLETON_CONFIG_FIELD_NAME = "INSTANCE";

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
            Notifier.notify(
                    env,
                    Kind.WARNING,
                    Message.DOMA4026,
                    interfaceElement,
                    new Object[] { suffix, interfaceElement.getQualifiedName() });
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
            validateUserDefinedConfig(configElement, daoMeta, daoMirror);
        }
    }

    protected void validateUserDefinedConfig(TypeElement configElement,
            DaoMeta daoMeta, DaoMirror daoMirror) {
        SingletonConfig singletonConfig = configElement
                .getAnnotation(SingletonConfig.class);
        if (singletonConfig == null) {
            if (configElement.getModifiers().contains(Modifier.ABSTRACT)) {
                throw new AptException(Message.DOMA4163, env,
                        daoMeta.getDaoElement(),
                        daoMirror.getAnnotationMirror(), daoMirror.getConfig(),
                        new Object[] { configElement.getQualifiedName() });
            }
            ExecutableElement constructor = ElementUtil.getNoArgConstructor(
                    configElement, env);
            if (constructor == null
                    || !constructor.getModifiers().contains(Modifier.PUBLIC)) {
                Optional<VariableElement> field = ElementFilter
                        .fieldsIn(configElement.getEnclosedElements())
                        .stream()
                        .filter(e -> e.getSimpleName().contentEquals(
                                SINGLETON_CONFIG_FIELD_NAME))
                        .filter(e -> e.getModifiers().containsAll(
                                EnumSet.of(Modifier.STATIC, Modifier.PUBLIC,
                                        Modifier.FINAL)))
                        .filter(e -> TypeMirrorUtil.isAssignable(e.asType(),
                                Config.class, env)).findFirst();
                if (field.isPresent()) {
                    daoMeta.setSingletonFieldName(SINGLETON_CONFIG_FIELD_NAME);
                } else {
                    throw new AptException(Message.DOMA4164, env,
                            daoMeta.getDaoElement(),
                            daoMirror.getAnnotationMirror(),
                            daoMirror.getConfig(),
                            new Object[] { configElement.getQualifiedName() });
                }
            }
        } else {
            String methodName = singletonConfig.method();
            boolean present = ElementFilter
                    .methodsIn(configElement.getEnclosedElements())
                    .stream()
                    .filter(m -> m.getModifiers().containsAll(
                            EnumSet.of(Modifier.STATIC, Modifier.PUBLIC)))
                    .filter(m -> TypeMirrorUtil.isAssignable(m.getReturnType(),
                            Config.class, env))
                    .filter(m -> m.getParameters().isEmpty())
                    .filter(m -> m.getSimpleName().toString()
                            .equals(methodName)).findAny().isPresent();
            if (present) {
                daoMeta.setSingletonMethodName(methodName);
            } else {
                throw new AptException(Message.DOMA4255, env,
                        daoMeta.getDaoElement(),
                        daoMirror.getAnnotationMirror(), daoMirror.getConfig(),
                        new Object[] { configElement.getQualifiedName(),
                                methodName });
            }
        }
    }

    protected void validateInterface(TypeElement interfaceElement,
            DaoMeta daoMeta) {
        if (!interfaceElement.getKind().isInterface()) {
            DaoMirror daoMirror = daoMeta.getDaoMirror();
            throw new AptException(Message.DOMA4014, env, interfaceElement,
                    daoMirror.getAnnotationMirror(),
                    new Object[] { interfaceElement.getQualifiedName() });
        }
        if (interfaceElement.getNestingKind().isNested()) {
            throw new AptException(Message.DOMA4017, env, interfaceElement,
                    new Object[] { interfaceElement.getQualifiedName() });
        }
        if (!interfaceElement.getTypeParameters().isEmpty()) {
            throw new AptException(Message.DOMA4059, env, interfaceElement,
                    new Object[] { interfaceElement.getQualifiedName() });
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
        List<TypeElement> interfaces = daoMeta.getDaoElement()
                .getInterfaces()
                .stream().map(type -> TypeMirrorUtil.toTypeElement(type, env))
                .peek(element -> {
                    if (element == null) {
                        throw new AptIllegalStateException(
                                "failed to convert to TypeElement.");
                    }
                }).collect(toList());
        for (TypeElement typeElement : interfaces) {
            DaoMirror daoMirror = DaoMirror.newInstance(typeElement, env);
            if (daoMirror == null) {
                ExecutableElement nonDefaultMethod = findNonDefaultMethod(
                        typeElement);
                if (nonDefaultMethod == null) {
                    continue;
                }
                throw new AptException(Message.DOMA4440, env,
                        daoMeta.getDaoElement(),
                        new Object[] { nonDefaultMethod.getSimpleName(),
                                daoMeta.getDaoElement().getQualifiedName() });
            }
            if (daoMeta.getParentDaoMeta() != null) {
                throw new AptException(Message.DOMA4188, env,
                        daoMeta.getDaoElement(),
                        new Object[] {
                                daoMeta.getDaoElement().getQualifiedName() });
            }
            ParentDaoMeta parentDaoMeta = new ParentDaoMeta(daoMirror);
            parentDaoMeta.setDaoType(typeElement.asType());
            parentDaoMeta.setDaoElement(typeElement);
            daoMeta.setParentDaoMeta(parentDaoMeta);
        }
    }

    protected ExecutableElement findNonDefaultMethod(
            TypeElement interfaceElement) {
        Optional<ExecutableElement> method = ElementFilter
                .methodsIn(interfaceElement.getEnclosedElements()).stream()
                .filter(m -> !m.isDefault()).findAny();
        if (method.isPresent()) {
            return method.get();
        }
        for (TypeMirror typeMirror : interfaceElement.getInterfaces()) {
            TypeElement i = TypeMirrorUtil.toTypeElement(typeMirror, env);
            if (i == null) {
                throw new AptIllegalStateException(
                        "failed to convert to TypeElement.");
            }
            ExecutableElement m = findNonDefaultMethod(i);
            if (m != null) {
                return m;
            }
        }
        return null;
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
        Set<Modifier> modifiers = methodElement.getModifiers();
        if (modifiers.contains(Modifier.STATIC)
                || modifiers.contains(Modifier.PRIVATE)) {
            return;
        }
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
                    throw new AptException(Message.DOMA4087, env,
                            methodElement,
                            new Object[] {
                                    foundAnnotationTypeElement
                                            .getQualifiedName(),
                                    typeElement.getQualifiedName(),
                                    daoMeta.getDaoElement().getQualifiedName(),
                                    methodElement.getSimpleName() });
                }
                if (methodElement.isDefault()) {
                    throw new AptException(Message.DOMA4252, env,
                            methodElement, new Object[] {
                                    typeElement.getQualifiedName(),
                                    daoMeta.getDaoElement().getQualifiedName(),
                                    methodElement.getSimpleName() });
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
        throw new AptException(Message.DOMA4005, env, method, new Object[] {
                daoMeta.getDaoElement().getQualifiedName(),
                method.getSimpleName() });
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
                        new Object[] { dirPath + "/" + fileName });
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
        try {
            return ResourceUtil.getResource(path, env);
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
