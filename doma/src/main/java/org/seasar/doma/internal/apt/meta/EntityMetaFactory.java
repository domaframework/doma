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

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.ElementKind;
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
import org.seasar.doma.ModifiedProperties;
import org.seasar.doma.Table;
import org.seasar.doma.Transient;
import org.seasar.doma.internal.apt.AptException;
import org.seasar.doma.internal.apt.AptIllegalStateException;
import org.seasar.doma.internal.apt.Notifier;
import org.seasar.doma.internal.apt.Options;
import org.seasar.doma.internal.apt.TypeUtil;
import org.seasar.doma.jdbc.entity.EntityListener;
import org.seasar.doma.message.DomaMessageCode;

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

    protected void doClassElement(TypeElement entityClassElement,
            EntityMeta entityMeta) {
        if (entityClassElement.getNestingKind().isNested()) {
            throw new AptException(DomaMessageCode.DOMA4018, env,
                    entityClassElement, entityClassElement.getQualifiedName());
        }
        if (entityClassElement.getKind() != ElementKind.CLASS) {
            throw new AptException(DomaMessageCode.DOMA4015, env,
                    entityClassElement, entityClassElement.getQualifiedName());
        }
        String entityName = entityClassElement.getSimpleName().toString();
        String suffix = Options.getEntitySuffix(env);
        if (entityName.endsWith(suffix)) {
            Notifier.notify(env, Kind.WARNING, DomaMessageCode.DOMA4026,
                    entityClassElement, suffix);
        }
        entityMeta.setEntityName(entityName);
        entityMeta.setEntityTypeName(TypeUtil.getTypeName(entityClassElement
                .asType(), env));
        Entity entityAnnotation = entityClassElement
                .getAnnotation(Entity.class);
        if (entityAnnotation == null) {
            throw new AptIllegalStateException();
        }
        if (!entityClassElement.getTypeParameters().isEmpty()) {
            throw new AptException(DomaMessageCode.DOMA4051, env,
                    entityClassElement);
        }
        doListener(entityAnnotation, entityClassElement, entityMeta);
        doTableMeta(entityClassElement, entityMeta);
    }

    protected void doListener(Entity entityAnnotation,
            TypeElement entityClassElement, EntityMeta entityMeta) {
        TypeMirror listenerType = getListenerType(entityAnnotation);
        TypeMirror argumentType = getListenerArgumentType(listenerType);
        assertNotNull(argumentType);
        if (!TypeUtil.isAssignable(entityClassElement.asType(), argumentType,
                env)) {
            throw new AptException(DomaMessageCode.DOMA4038, env,
                    entityClassElement, listenerType, argumentType,
                    entityClassElement.getQualifiedName());
        }
        entityMeta.setListenerTypeName(TypeUtil.getTypeName(listenerType, env));
    }

    protected TypeMirror getListenerType(Entity entityAnnotation) {
        try {
            entityAnnotation.listener();
        } catch (MirroredTypeException e) {
            return e.getTypeMirror();
        }
        throw new AptIllegalStateException();
    }

    protected TypeMirror getListenerArgumentType(TypeMirror typeMirror) {
        for (TypeMirror supertype : env.getTypeUtils().directSupertypes(
                typeMirror)) {
            TypeElement superElement = TypeUtil.toTypeElement(supertype, env);
            if (superElement == null || !superElement.getKind().isInterface()) {
                continue;
            }
            if (superElement.getQualifiedName().contentEquals(
                    EntityListener.class.getName())) {
                DeclaredType declaredType = TypeUtil.toDeclaredType(supertype,
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

    protected void doTableMeta(TypeElement entityClassElement,
            EntityMeta entityMeta) {
        TableMeta tableMeta = new TableMeta();
        Table table = entityClassElement.getAnnotation(Table.class);
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

    protected void doFieldElements(TypeElement entityClassElement,
            EntityMeta entityMeta) {
        for (VariableElement fieldElement : getFieldElements(entityClassElement)) {
            try {
                if (fieldElement.getAnnotation(ModifiedProperties.class) != null) {
                    doModifiedPropertiesField(fieldElement, entityMeta);
                } else {
                    doEntityPropertyMeta(fieldElement, entityMeta);
                }
            } catch (AptException e) {
                Notifier.notify(env, e);
            }
        }
    }

    protected List<VariableElement> getFieldElements(
            TypeElement entityClassElement) {
        List<VariableElement> results = new LinkedList<VariableElement>();
        for (TypeElement t = entityClassElement; t != null
                && t.asType().getKind() != TypeKind.NONE; t = TypeUtil
                .toTypeElement(t.getSuperclass(), env)) {
            for (VariableElement field : ElementFilter.fieldsIn(t
                    .getEnclosedElements())) {
                if (field.getAnnotation(Transient.class) != null) {
                    continue;
                }
                if (field.getModifiers().contains(Modifier.STATIC)) {
                    continue;
                }
                if (field.getModifiers().contains(Modifier.PRIVATE)) {
                    throw new AptException(DomaMessageCode.DOMA4094, env, field);
                }
                results.add(field);
            }
        }
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

    protected void doModifiedPropertiesField(VariableElement fieldElement,
            EntityMeta entityMeta) {
        if (!TypeUtil.isAssignable(fieldElement.asType(), Set.class, env)) {
            throw new AptException(DomaMessageCode.DOMA4095, env, fieldElement);
        }
        entityMeta.setModifiedPropertiesFieldName(fieldElement.getSimpleName()
                .toString());
    }

    protected void doEntityPropertyMeta(VariableElement fieldElement,
            EntityMeta entityMeta) {
        validateFieldAnnotation(fieldElement, entityMeta);
        EntityPropertyMeta propertyMeta = createEntityPropertyMeta(
                fieldElement, entityMeta);
        entityMeta.addPropertyMeta(propertyMeta);
    }

    protected void validateFieldAnnotation(VariableElement fieldElement,
            EntityMeta entityMeta) {
        TypeElement foundAnnotationTypeElement = null;
        for (AnnotationMirror annotation : fieldElement.getAnnotationMirrors()) {
            DeclaredType declaredType = annotation.getAnnotationType();
            TypeElement typeElement = TypeUtil.toTypeElement(declaredType, env);
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

    protected EntityPropertyMeta createEntityPropertyMeta(
            VariableElement fieldElement, EntityMeta entityMeta) {
        return propertyMetaFactory.createEntityPropertyMeta(fieldElement,
                entityMeta);
    }

}
