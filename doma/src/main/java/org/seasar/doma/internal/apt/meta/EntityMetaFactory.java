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
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;

import org.seasar.doma.Entity;
import org.seasar.doma.EntityField;
import org.seasar.doma.OriginalStates;
import org.seasar.doma.Transient;
import org.seasar.doma.internal.apt.AptException;
import org.seasar.doma.internal.apt.AptIllegalStateException;
import org.seasar.doma.internal.apt.Notifier;
import org.seasar.doma.internal.apt.mirror.EntityMirror;
import org.seasar.doma.internal.apt.mirror.TableMirror;
import org.seasar.doma.internal.apt.util.ElementUtil;
import org.seasar.doma.internal.apt.util.TypeMirrorUtil;
import org.seasar.doma.internal.message.Message;
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
        EntityMirror entityMirror = EntityMirror.newInstance(classElement, env);
        if (entityMirror == null) {
            throw new AptIllegalStateException("entityMirror.");
        }
        EntityMeta entityMeta = new EntityMeta(entityMirror, classElement);
        doClassElement(classElement, entityMeta);
        doFieldElements(classElement, entityMeta);
        validateGeneratedId(classElement, entityMeta);
        return entityMeta;
    }

    protected void doClassElement(TypeElement classElement,
            EntityMeta entityMeta) {
        validateClass(classElement, entityMeta);
        validateConstructor(classElement, entityMeta);
        validateEntityListener(classElement, entityMeta);

        String entityName = classElement.getSimpleName().toString();
        entityMeta.setEntityName(entityName);
        entityMeta.setEntityTypeName(TypeMirrorUtil.getTypeName(classElement
                .asType(), env));
        doTable(classElement, entityMeta);
    }

    protected void validateClass(TypeElement classElement, EntityMeta entityMeta) {
        if (classElement.getKind() != ElementKind.CLASS) {
            EntityMirror entityMirror = entityMeta.getEntityMirror();
            throw new AptException(Message.DOMA4015, env, classElement,
                    entityMirror.getAnnotationMirror());
        }
        if (classElement.getNestingKind().isNested()) {
            throw new AptException(Message.DOMA4018, env, classElement);
        }
        if (!classElement.getTypeParameters().isEmpty()) {
            throw new AptException(Message.DOMA4051, env, classElement);
        }
    }

    protected void validateConstructor(TypeElement classElement,
            EntityMeta entityMeta) {
        if (classElement.getModifiers().contains(Modifier.ABSTRACT)) {
            return;
        }
        ExecutableElement constructor = ElementUtil.getNoArgConstructor(
                classElement, env);
        if (constructor == null
                || constructor.getModifiers().contains(Modifier.PRIVATE)) {
            throw new AptException(Message.DOMA4124, env, classElement);
        }
    }

    protected void validateEntityListener(TypeElement classElement,
            EntityMeta entityMeta) {
        EntityMirror entityMirror = entityMeta.getEntityMirror();
        TypeMirror listenerType = entityMirror.getListenerValue();
        TypeMirror argumentType = getListenerArgumentType(listenerType);
        if (argumentType == null) {
            throw new AptIllegalStateException("argumentType");
        }
        if (!TypeMirrorUtil.isAssignable(classElement.asType(), argumentType,
                env)) {
            throw new AptException(Message.DOMA4038, env, classElement,
                    entityMirror.getAnnotationMirror(), entityMirror
                            .getListener(), listenerType, argumentType,
                    classElement.getQualifiedName());
        }
        TypeElement listenerElement = TypeMirrorUtil.toTypeElement(
                listenerType, env);
        if (listenerElement == null) {
            throw new AptIllegalStateException(
                    "failed to convert to TypeElement");
        }
        if (listenerElement.getModifiers().contains(Modifier.ABSTRACT)) {
            throw new AptException(Message.DOMA4166, env, classElement,
                    entityMirror.getAnnotationMirror(), entityMirror
                            .getListener(), listenerElement.getQualifiedName());
        }
        ExecutableElement constructor = ElementUtil.getNoArgConstructor(
                listenerElement, env);
        if (constructor == null
                || !constructor.getModifiers().contains(Modifier.PUBLIC)) {
            throw new AptException(Message.DOMA4167, env, classElement,
                    entityMirror.getAnnotationMirror(), entityMirror
                            .getListener(), listenerElement.getQualifiedName());
        }
    }

    protected TypeMirror getListenerArgumentType(TypeMirror typeMirror) {
        for (TypeMirror supertype : env.getTypeUtils().directSupertypes(
                typeMirror)) {
            if (!TypeMirrorUtil.isAssignable(supertype, EntityListener.class,
                    env)) {
                continue;
            }
            if (TypeMirrorUtil.isSameType(supertype, EntityListener.class, env)) {
                DeclaredType declaredType = TypeMirrorUtil.toDeclaredType(
                        supertype, env);
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

    protected void doTable(TypeElement classElement, EntityMeta entityMeta) {
        TableMirror tableMirror = TableMirror.newInstance(classElement, env);
        if (tableMirror == null) {
            return;
        }
        entityMeta.setTableMirror(tableMirror);
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
                    throw new AptException(Message.DOMA4094, env, fieldElement);
                } else if (fieldElement.getAnnotation(OriginalStates.class) != null) {
                    doOriginalStatesField(classElement, fieldElement,
                            entityMeta);
                } else {
                    doEntityPropertyMeta(fieldElement, entityMeta);
                }
            } catch (AptException e) {
                Notifier.notify(env, e);
                entityMeta.setError(true);
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

    protected void doOriginalStatesField(TypeElement classElement,
            VariableElement fieldElement, EntityMeta entityMeta) {
        if (!classElement.equals(fieldElement.getEnclosingElement())) {
            return;
        }
        if (entityMeta.hasOriginalStatesMeta()) {
            throw new AptException(Message.DOMA4125, env, fieldElement);
        }
        if (!TypeMirrorUtil.isSameType(fieldElement.asType(), classElement
                .asType(), env)) {
            throw new AptException(Message.DOMA4135, env, fieldElement,
                    classElement.getQualifiedName());
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
            TypeElement typeElement = TypeMirrorUtil.toTypeElement(
                    declaredType, env);
            if (typeElement.getAnnotation(EntityField.class) != null) {
                if (foundAnnotationTypeElement != null) {
                    throw new AptException(Message.DOMA4086, env, fieldElement,
                            foundAnnotationTypeElement.getQualifiedName(),
                            typeElement.getQualifiedName());
                }
                foundAnnotationTypeElement = typeElement;
            }
        }
    }

    protected void validateGeneratedId(TypeElement classElement,
            EntityMeta entityMeta) {
        if (entityMeta.hasGeneratedIdPropertyMeta()
                && entityMeta.getIdPropertyMetas().size() > 1) {
            throw new AptException(Message.DOMA4036, env, classElement);
        }
    }
}
