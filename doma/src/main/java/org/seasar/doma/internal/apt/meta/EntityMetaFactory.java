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

import java.io.Serializable;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.MirroredTypeException;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;
import javax.tools.Diagnostic.Kind;

import org.seasar.doma.Entity;
import org.seasar.doma.EntityField;
import org.seasar.doma.OriginalStates;
import org.seasar.doma.Table;
import org.seasar.doma.Transient;
import org.seasar.doma.internal.apt.AptException;
import org.seasar.doma.internal.apt.AptIllegalStateException;
import org.seasar.doma.internal.apt.Notifier;
import org.seasar.doma.internal.apt.Options;
import org.seasar.doma.internal.apt.util.ElementUtil;
import org.seasar.doma.internal.apt.util.TypeMirrorUtil;
import org.seasar.doma.internal.message.DomaMessageCode;
import org.seasar.doma.jdbc.entity.EntityListener;

/**
 * 
 * @author taedium
 * 
 */
public class EntityMetaFactory {

    protected final ProcessingEnvironment env;

    protected final EntityPropertyMetaFactory propertyMetaFactory;

    public EntityMetaFactory(ProcessingEnvironment env,
            EntityPropertyMetaFactory propertyMetaFactory) {
        assertNotNull(env, propertyMetaFactory);
        this.env = env;
        this.propertyMetaFactory = propertyMetaFactory;
    }

    public EntityMeta createEntityMeta(TypeElement classElement) {
        assertNotNull(classElement);
        EntityMeta entityMeta = new EntityMeta();
        doClassElement(classElement, entityMeta);
        doFieldElements(classElement, entityMeta);
        return entityMeta;
    }

    protected void doClassElement(TypeElement classElement,
            EntityMeta entityMeta) {
        validateClass(classElement);
        validateConstructor(classElement);

        String entityName = classElement.getSimpleName().toString();
        String suffix = Options.getEntitySuffix(env);
        if (entityName.endsWith(suffix)) {
            Notifier.notify(env, Kind.WARNING, DomaMessageCode.DOMA4026,
                    classElement, suffix);
        }
        entityMeta.setEntityName(entityName);
        entityMeta.setEntityTypeName(TypeMirrorUtil.getTypeName(
                classElement.asType(), env));
        Entity entityAnnotation = classElement.getAnnotation(Entity.class);
        if (entityAnnotation == null) {
            throw new AptIllegalStateException("unreachable.");
        }
        doListener(entityAnnotation, classElement, entityMeta);
        doNamingConvention(entityAnnotation, classElement, entityMeta);
        doTableMeta(classElement, entityMeta);
    }

    protected void validateClass(TypeElement classElement) {
        if (classElement.getKind() != ElementKind.CLASS) {
            throw new AptException(DomaMessageCode.DOMA4015, env, classElement);
        }
        if (classElement.getNestingKind().isNested()) {
            throw new AptException(DomaMessageCode.DOMA4018, env, classElement);
        }
        if (classElement.getModifiers().contains(Modifier.PRIVATE)) {
            throw new AptException(DomaMessageCode.DOMA4123, env, classElement);
        }
        if (classElement.getModifiers().contains(Modifier.ABSTRACT)) {
            throw new AptException(DomaMessageCode.DOMA4134, env, classElement);
        }
        if (!classElement.getTypeParameters().isEmpty()) {
            throw new AptException(DomaMessageCode.DOMA4051, env, classElement);
        }
    }

    protected void validateConstructor(TypeElement classElement) {
        if (classElement.getModifiers().contains(Modifier.ABSTRACT)) {
            return;
        }
        for (ExecutableElement constructor : ElementFilter
                .constructorsIn(classElement.getEnclosedElements())) {
            if (!constructor.getModifiers().contains(Modifier.PRIVATE)) {
                if (constructor.getParameters().isEmpty()) {
                    return;
                }
            }
        }
        throw new AptException(DomaMessageCode.DOMA4124, env, classElement);
    }

    protected void doListener(Entity entityAnnotation,
            TypeElement classElement, EntityMeta entityMeta) {
        TypeMirror listenerType = getListenerType(entityAnnotation);
        TypeMirror argumentType = getListenerArgumentType(listenerType);
        assertNotNull(argumentType);
        if (!TypeMirrorUtil.isAssignable(classElement.asType(), argumentType, env)) {
            throw new AptException(DomaMessageCode.DOMA4038, env, classElement,
                    listenerType, argumentType, classElement.getQualifiedName());
        }
        entityMeta.setListenerTypeName(TypeMirrorUtil.getTypeName(listenerType, env));
    }

    protected TypeMirror getListenerType(Entity entityAnnotation) {
        try {
            entityAnnotation.listener();
        } catch (MirroredTypeException e) {
            return e.getTypeMirror();
        }
        throw new AptIllegalStateException("unreachable");
    }

    protected TypeMirror getListenerArgumentType(TypeMirror typeMirror) {
        for (TypeMirror supertype : env.getTypeUtils().directSupertypes(
                typeMirror)) {
            TypeElement superElement = TypeMirrorUtil.toTypeElement(supertype, env);
            if (superElement == null || !superElement.getKind().isInterface()) {
                continue;
            }
            if (superElement.getQualifiedName().contentEquals(
                    EntityListener.class.getName())) {
                DeclaredType declaredType = TypeMirrorUtil.toDeclaredType(supertype,
                        env);
                assertNotNull(declaredType);
                List<? extends TypeMirror> args = declaredType
                        .getTypeArguments();
                assertEquals(1, args.size());
                return args.get(0);
            }
            TypeMirror argumentType = getListenerArgumentType(supertype);
            if (argumentType != null) {
                return argumentType;
            }
        }
        return null;
    }

    protected void doNamingConvention(Entity entityAnnotation,
            TypeElement classElement, EntityMeta entityMeta) {
        TypeMirror mamingConventionType = getNamingConventionType(entityAnnotation);
        entityMeta.setNamingConventionTypeName(TypeMirrorUtil.getTypeName(
                mamingConventionType, env));
    }

    protected TypeMirror getNamingConventionType(Entity entityAnnotation) {
        try {
            entityAnnotation.namingConvention();
        } catch (MirroredTypeException e) {
            return e.getTypeMirror();
        }
        throw new AptIllegalStateException("unreachable");
    }

    protected void doTableMeta(TypeElement classElement, EntityMeta entityMeta) {
        TableMeta tableMeta = new TableMeta();
        Table table = classElement.getAnnotation(Table.class);
        if (table != null) {
            if (!table.catalog().isEmpty()) {
                tableMeta.setCatalog(table.catalog());
            }
            if (!table.schema().isEmpty()) {
                tableMeta.setSchema(table.schema());
            }
            if (!table.name().isEmpty()) {
                tableMeta.setName(table.name());
            }
        }
        entityMeta.setTableMeta(tableMeta);
    }

    protected void doFieldElements(TypeElement classElement,
            EntityMeta entityMeta) {
        for (VariableElement fieldElement : getFieldElements(classElement)) {
            try {
                if (fieldElement.getAnnotation(Transient.class) != null) {
                    continue;
                } else if (fieldElement.getModifiers()
                        .contains(Modifier.STATIC)) {
                    continue;
                } else if (fieldElement.getModifiers().contains(
                        Modifier.PRIVATE)) {
                    throw new AptException(DomaMessageCode.DOMA4094, env,
                            fieldElement);
                } else if (fieldElement.getAnnotation(OriginalStates.class) != null) {
                    doOriginalStatesField(fieldElement, entityMeta);
                } else {
                    doEntityPropertyMeta(fieldElement, entityMeta);
                }
            } catch (AptException e) {
                Notifier.notify(env, e);
            }
        }
    }

    protected List<VariableElement> getFieldElements(TypeElement classElement) {
        List<VariableElement> results = new LinkedList<VariableElement>();
        for (TypeElement t = classElement; t != null
                && t.asType().getKind() != TypeKind.NONE; t = TypeMirrorUtil
                .toTypeElement(t.getSuperclass(), env)) {
            if (t.getAnnotation(Entity.class) == null) {
                continue;
            }
            List<VariableElement> fields = new LinkedList<VariableElement>();
            for (VariableElement field : ElementFilter.fieldsIn(t
                    .getEnclosedElements())) {
                fields.add(field);
            }
            Collections.reverse(fields);
            results.addAll(fields);
        }
        Collections.reverse(results);

        List<VariableElement> hiderFields = new LinkedList<VariableElement>(
                results);
        for (Iterator<VariableElement> it = results.iterator(); it.hasNext();) {
            VariableElement hidden = it.next();
            for (VariableElement hider : hiderFields) {
                if (env.getElementUtils().hides(hider, hidden)) {
                    it.remove();
                }
            }
        }
        return results;
    }

    protected void doOriginalStatesField(VariableElement fieldElement,
            EntityMeta entityMeta) {
        if (entityMeta.hasOriginalStatesMeta()) {
            throw new AptException(DomaMessageCode.DOMA4125, env, fieldElement);
        }
        if (!TypeMirrorUtil
                .isSameType(fieldElement.asType(), Serializable.class, env)) {
            throw new AptException(DomaMessageCode.DOMA4135, env, fieldElement);
        }
        TypeElement entityElement = ElementUtil.toTypeElement(fieldElement
                .getEnclosingElement(), env);
        if (entityElement == null) {
            throw new AptIllegalStateException(fieldElement.toString());
        }
        OriginalStatesMeta changedPropertiesMeta = new OriginalStatesMeta(
                entityElement, fieldElement, env);
        entityMeta.setOriginalStatesMeta(changedPropertiesMeta);
    }

    protected void doEntityPropertyMeta(VariableElement fieldElement,
            EntityMeta entityMeta) {
        validateFieldAnnotation(fieldElement, entityMeta);
        EntityPropertyMeta propertyMeta = propertyMetaFactory
                .createEntityPropertyMeta(fieldElement, entityMeta);
        entityMeta.addPropertyMeta(propertyMeta);
    }

    protected void validateFieldAnnotation(VariableElement fieldElement,
            EntityMeta entityMeta) {
        TypeElement foundAnnotationTypeElement = null;
        for (AnnotationMirror annotation : fieldElement.getAnnotationMirrors()) {
            DeclaredType declaredType = annotation.getAnnotationType();
            TypeElement typeElement = TypeMirrorUtil.toTypeElement(declaredType, env);
            if (typeElement.getAnnotation(EntityField.class) != null) {
                if (foundAnnotationTypeElement != null) {
                    throw new AptException(DomaMessageCode.DOMA4086, env,
                            fieldElement, foundAnnotationTypeElement
                                    .getQualifiedName(), typeElement
                                    .getQualifiedName());
                }
                foundAnnotationTypeElement = typeElement;
            }
        }
    }

}
