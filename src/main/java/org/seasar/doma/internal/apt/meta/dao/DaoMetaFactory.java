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
package org.seasar.doma.internal.apt.meta.dao;

import static java.util.stream.Collectors.toList;
import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.io.File;
import java.io.FilenameFilter;
import java.net.URI;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;
import javax.tools.Diagnostic.Kind;
import javax.tools.FileObject;

import org.seasar.doma.DaoMethod;
import org.seasar.doma.SingletonConfig;
import org.seasar.doma.internal.Constants;
import org.seasar.doma.internal.apt.AptException;
import org.seasar.doma.internal.apt.AptIllegalStateException;
import org.seasar.doma.internal.apt.Context;
import org.seasar.doma.internal.apt.meta.TypeElementMetaFactory;
import org.seasar.doma.internal.apt.meta.query.ArrayCreateQueryMetaFactory;
import org.seasar.doma.internal.apt.meta.query.AutoBatchModifyQueryMetaFactory;
import org.seasar.doma.internal.apt.meta.query.AutoFunctionQueryMetaFactory;
import org.seasar.doma.internal.apt.meta.query.AutoModifyQueryMetaFactory;
import org.seasar.doma.internal.apt.meta.query.AutoProcedureQueryMetaFactory;
import org.seasar.doma.internal.apt.meta.query.BlobCreateQueryMetaFactory;
import org.seasar.doma.internal.apt.meta.query.ClobCreateQueryMetaFactory;
import org.seasar.doma.internal.apt.meta.query.DefaultQueryMetaFactory;
import org.seasar.doma.internal.apt.meta.query.NClobCreateQueryMetaFactory;
import org.seasar.doma.internal.apt.meta.query.QueryMeta;
import org.seasar.doma.internal.apt.meta.query.QueryMetaFactory;
import org.seasar.doma.internal.apt.meta.query.SQLXMLCreateQueryMetaFactory;
import org.seasar.doma.internal.apt.meta.query.SqlFileBatchModifyQueryMetaFactory;
import org.seasar.doma.internal.apt.meta.query.SqlFileModifyQueryMetaFactory;
import org.seasar.doma.internal.apt.meta.query.SqlFileScriptQueryMetaFactory;
import org.seasar.doma.internal.apt.meta.query.SqlFileSelectQueryMetaFactory;
import org.seasar.doma.internal.apt.meta.query.SqlProcessorQueryMetaFactory;
import org.seasar.doma.internal.apt.reflection.AnnotateWithReflection;
import org.seasar.doma.internal.apt.reflection.DaoReflection;
import org.seasar.doma.internal.apt.reflection.SuppressReflection;
import org.seasar.doma.internal.jdbc.util.SqlFileUtil;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.message.Message;

/**
 * @author taedium
 * 
 */
public class DaoMetaFactory implements TypeElementMetaFactory<DaoMeta> {

    private static final String SINGLETON_CONFIG_FIELD_NAME = "INSTANCE";

    private final Context ctx;

    private final TypeElement daoElement;

    private final DaoReflection daoReflection;

    private final SuppressReflection suppressReflection;

    private boolean error;

    public DaoMetaFactory(Context ctx, TypeElement interfaceElement) {
        assertNotNull(ctx, interfaceElement);
        this.ctx = ctx;
        this.daoElement = interfaceElement;
        daoReflection = ctx.getReflections().newDaoReflection(interfaceElement);
        if (daoReflection == null) {
            throw new AptIllegalStateException("daoReflection");
        }
        suppressReflection = ctx.getReflections().newSuppressReflection(interfaceElement);
    }

    @Override
    public DaoMeta createTypeElementMeta() {
        validateInterface();
        validateName();

        DaoMeta daoMeta = new DaoMeta(daoReflection, daoElement);
        doAnnotateWith(daoMeta);
        doParentDao(daoMeta);
        doConfig(daoMeta);
        doMethods(daoMeta);
        validateFiles(daoMeta);
        return error ? null : daoMeta;
    }

    private void validateInterface() {
        if (!daoElement.getKind().isInterface()) {
            throw new AptException(Message.DOMA4014, daoElement,
                    daoReflection.getAnnotationMirror());
        }
        if (daoElement.getNestingKind().isNested()) {
            throw new AptException(Message.DOMA4017, daoElement);
        }
        if (!daoElement.getTypeParameters().isEmpty()) {
            throw new AptException(Message.DOMA4059, daoElement);
        }
    }

    private void validateName() {
        String name = daoElement.getSimpleName().toString();
        String suffix = ctx.getOptions().getDaoSuffix();
        if (name.endsWith(suffix)) {
            ctx.getNotifier().send(Kind.WARNING, Message.DOMA4026, daoElement,
                    new Object[] { suffix });
        }
    }

    private void doConfig(DaoMeta daoMeta) {
        if (!daoReflection.hasUserDefinedConfig()) {
            return;
        }
        TypeElement configElement = ctx.getTypes().toTypeElement(daoReflection.getConfigValue());
        if (configElement == null) {
            throw new AptIllegalStateException("failed to convert to TypeElement.");
        }
        ConfigMeta configMeta = createConfigMeta(configElement);
        daoMeta.setConfigMeta(configMeta);
    }

    private ConfigMeta createConfigMeta(TypeElement configElement) {
        SingletonConfig singletonConfig = configElement.getAnnotation(SingletonConfig.class);
        if (singletonConfig == null) {
            if (configElement.getModifiers().contains(Modifier.ABSTRACT)) {
                throw new AptException(Message.DOMA4163, daoElement,
                        daoReflection.getAnnotationMirror(), daoReflection.getConfig(),
                        new Object[] { configElement.getQualifiedName() });
            }
            ExecutableElement constructor = ctx.getElements().getNoArgConstructor(configElement);
            if (constructor != null && constructor.getModifiers().contains(Modifier.PUBLIC)) {
                return ConfigMeta.byConstructor(configElement.asType());
            }
            return ElementFilter.fieldsIn(configElement.getEnclosedElements())
                    .stream()
                    .filter(e -> e.getSimpleName().contentEquals(SINGLETON_CONFIG_FIELD_NAME))
                    .filter(e -> e.getModifiers().containsAll(
                            EnumSet.of(Modifier.STATIC, Modifier.PUBLIC, Modifier.FINAL)))
                    .filter(e -> ctx.getTypes().isAssignable(e.asType(), Config.class))
                    .findAny()
                    .map(f -> ConfigMeta.byField(configElement.asType(), f))
                    .orElseThrow(() -> {
                        throw new AptException(Message.DOMA4164, daoElement,
                                daoReflection.getAnnotationMirror(), daoReflection.getConfig(),
                                new Object[] { configElement.getQualifiedName() });
                    });
        }
        String methodName = singletonConfig.method();
        return ElementFilter.methodsIn(configElement.getEnclosedElements())
                .stream()
                .filter(m -> m.getModifiers()
                        .containsAll(EnumSet.of(Modifier.STATIC, Modifier.PUBLIC)))
                .filter(m -> ctx.getTypes().isAssignable(m.getReturnType(), Config.class))
                .filter(m -> m.getParameters().isEmpty())
                .filter(m -> m.getSimpleName().toString().equals(methodName))
                .findAny()
                .map(m -> ConfigMeta.byMethod(configElement.asType(), m))
                .orElseThrow(() -> {
                    throw new AptException(Message.DOMA4255, daoElement,
                            daoReflection.getAnnotationMirror(), daoReflection.getConfig(),
                            new Object[] { configElement.getQualifiedName(), methodName });
                });
    }

    private void doAnnotateWith(DaoMeta daoMeta) {
        AnnotateWithReflection annotateWithReflection = ctx.getReflections()
                .newAnnotateWithReflection(daoElement);
        if (annotateWithReflection != null) {
            daoMeta.setAnnotateWithReflection(annotateWithReflection);
        }
    }

    private void doParentDao(DaoMeta daoMeta) {
        List<TypeElement> interfaces = daoElement.getInterfaces()
                .stream()
                .map(ctx.getTypes()::toTypeElement)
                .peek(element -> {
                    if (element == null) {
                        throw new AptIllegalStateException("failed to convert to TypeElement.");
                    }
                })
                .collect(toList());
        for (TypeElement typeElement : interfaces) {
            DaoReflection daoReflection = ctx.getReflections().newDaoReflection(typeElement);
            if (daoReflection == null) {
                ExecutableElement nonDefaultMethod = findNonDefaultMethod(typeElement);
                if (nonDefaultMethod == null) {
                    continue;
                }
                throw new AptException(Message.DOMA4440, daoElement,
                        new Object[] { nonDefaultMethod.getSimpleName() });
            }
            if (daoMeta.getParentDaoMeta() != null) {
                throw new AptException(Message.DOMA4188, daoElement);
            }
            ParentDaoMeta parentDaoMeta = new ParentDaoMeta(daoReflection, typeElement);
            daoMeta.setParentDaoMeta(parentDaoMeta);
        }
    }

    private ExecutableElement findNonDefaultMethod(TypeElement interfaceElement) {
        Optional<ExecutableElement> method = ElementFilter
                .methodsIn(interfaceElement.getEnclosedElements())
                .stream()
                .filter(m -> !m.isDefault())
                .findAny();
        if (method.isPresent()) {
            return method.get();
        }
        for (TypeMirror typeMirror : interfaceElement.getInterfaces()) {
            TypeElement i = ctx.getTypes().toTypeElement(typeMirror);
            if (i == null) {
                throw new AptIllegalStateException("failed to convert to TypeElement.");
            }
            ExecutableElement m = findNonDefaultMethod(i);
            if (m != null) {
                return m;
            }
        }
        return null;
    }

    private void doMethods(DaoMeta daoMeta) {
        for (ExecutableElement methodElement : ElementFilter
                .methodsIn(daoElement.getEnclosedElements())) {
            try {
                doMethod(methodElement, daoMeta);
            } catch (AptException e) {
                ctx.getNotifier().send(e);
                error = true;
            }
        }
    }

    private void doMethod(ExecutableElement methodElement, DaoMeta daoMeta) {
        Set<Modifier> modifiers = methodElement.getModifiers();
        if (modifiers.contains(Modifier.STATIC) || modifiers.contains(Modifier.PRIVATE)) {
            return;
        }
        validateMethod(methodElement, daoMeta);
        QueryMeta queryMeta = createQueryMeta(methodElement, daoMeta);
        daoMeta.addQueryMeta(queryMeta);
    }

    private void validateMethod(ExecutableElement methodElement, DaoMeta daoMeta) {
        TypeElement foundAnnotationTypeElement = null;
        for (AnnotationMirror annotation : methodElement.getAnnotationMirrors()) {
            DeclaredType declaredType = annotation.getAnnotationType();
            TypeElement typeElement = ctx.getTypes().toTypeElement(declaredType);
            if (typeElement.getAnnotation(DaoMethod.class) != null) {
                if (foundAnnotationTypeElement != null) {
                    throw new AptException(Message.DOMA4087, methodElement,
                            new Object[] { foundAnnotationTypeElement.getQualifiedName(),
                                    typeElement.getQualifiedName() });
                }
                if (methodElement.isDefault()) {
                    throw new AptException(Message.DOMA4252, methodElement,
                            new Object[] { typeElement.getQualifiedName() });
                }
                foundAnnotationTypeElement = typeElement;
            }
        }
    }

    private QueryMeta createQueryMeta(ExecutableElement methodElement, DaoMeta daoMeta) {
        for (Supplier<QueryMetaFactory> supplier : createQueryMetaFactories(methodElement)) {
            QueryMetaFactory factory = supplier.get();
            QueryMeta queryMeta = factory.createQueryMeta();
            if (queryMeta != null) {
                return queryMeta;
            }
        }
        throw new AptException(Message.DOMA4005, methodElement, new Object[] {});
    }

    private List<Supplier<QueryMetaFactory>> createQueryMetaFactories(
            ExecutableElement methodElement) {
        return List.of(() -> new SqlFileSelectQueryMetaFactory(ctx, methodElement),
                () -> new AutoModifyQueryMetaFactory(ctx, methodElement),
                () -> new AutoBatchModifyQueryMetaFactory(ctx, methodElement),
                () -> new AutoFunctionQueryMetaFactory(ctx, methodElement),
                () -> new AutoProcedureQueryMetaFactory(ctx, methodElement),
                () -> new SqlFileModifyQueryMetaFactory(ctx, methodElement),
                () -> new SqlFileBatchModifyQueryMetaFactory(ctx, methodElement),
                () -> new SqlFileScriptQueryMetaFactory(ctx, methodElement),
                () -> new DefaultQueryMetaFactory(ctx, methodElement),
                () -> new ArrayCreateQueryMetaFactory(ctx, methodElement),
                () -> new BlobCreateQueryMetaFactory(ctx, methodElement),
                () -> new ClobCreateQueryMetaFactory(ctx, methodElement),
                () -> new NClobCreateQueryMetaFactory(ctx, methodElement),
                () -> new SQLXMLCreateQueryMetaFactory(ctx, methodElement),
                () -> new SqlProcessorQueryMetaFactory(ctx, methodElement));

    }

    private void validateFiles(DaoMeta daoMeta) {
        if (error) {
            return;
        }
        if (!ctx.getOptions().getSqlValidation()) {
            return;
        }
        String dirPath = SqlFileUtil.buildPath(daoElement.getQualifiedName().toString());
        Set<String> fileNames = getFileNames(dirPath);
        for (QueryMeta queryMeta : daoMeta.getQueryMetas()) {
            for (String fileName : queryMeta.getFileNames()) {
                fileNames.remove(fileName);
            }
        }
        if (!isSuppressed(Message.DOMA4220)) {
            for (String fileName : fileNames) {
                ctx.getNotifier().send(Kind.WARNING, Message.DOMA4220, daoElement,
                        new Object[] { dirPath + "/" + fileName });
            }
        }
    }

    private Set<String> getFileNames(String dirPath) {
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

    private File getDir(String dirPath) {
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

    private FileObject getFileObject(String path) {
        try {
            return ctx.getResources().getResource(path);
        } catch (Exception ignored) {
            // Ignore, in case the Filer implementation doesn't support
            // directory path.
            return null;
        }
    }

    private boolean isSuppressed(Message message) {
        if (suppressReflection != null) {
            return suppressReflection.isSuppressed(message);
        }
        return false;
    }

}
