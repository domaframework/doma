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
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.TypeParameterElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.type.TypeVariable;
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
import org.seasar.doma.internal.apt.util.AnnotationValueUtil;
import org.seasar.doma.internal.apt.util.ElementUtil;
import org.seasar.doma.internal.apt.util.TypeMirrorUtil;
import org.seasar.doma.jdbc.entity.EntityListener;
import org.seasar.doma.jdbc.entity.NamingType;
import org.seasar.doma.jdbc.entity.NullEntityListener;
import org.seasar.doma.message.Message;

/**
 * 
 * @author taedium
 * 
 */
public class EntityMetaFactory implements TypeElementMetaFactory<EntityMeta> {

    protected final ProcessingEnvironment env;

    protected final EntityPropertyMetaFactory propertyMetaFactory;

    public EntityMetaFactory(ProcessingEnvironment env,
            EntityPropertyMetaFactory propertyMetaFactory) {
        assertNotNull(env, propertyMetaFactory);
        this.env = env;
        this.propertyMetaFactory = propertyMetaFactory;
    }

    @Override
    public EntityMeta createTypeElementMeta(TypeElement classElement) {
        assertNotNull(classElement);
        EntityMirror entityMirror = EntityMirror.newInstance(classElement, env);
        if (entityMirror == null) {
            throw new AptIllegalStateException("entityMirror.");
        }
        EntityMeta entityMeta = new EntityMeta(entityMirror, classElement);
        TypeMirror entityListener = resolveEntityListener(classElement);
        entityMeta.setEntityListener(entityListener);
        TypeElement entityListenerElement = TypeMirrorUtil.toTypeElement(
                entityListener, env);
        if (entityListenerElement == null) {
            throw new AptIllegalStateException("entityListener.");
        }
        entityMeta.setEntityListenerElement(entityListenerElement);
        entityMeta.setGenericEntityListener(!entityListenerElement
                .getTypeParameters().isEmpty());
        NamingType namingType = resolveNamingType(classElement);
        entityMeta.setNamingType(namingType);
        boolean immutable = resolveImmutable(classElement, entityMirror);
        entityMeta.setImmutable(immutable);
        entityMeta.setEntityName(classElement.getSimpleName().toString());
        entityMeta.setEntityTypeName(TypeMirrorUtil.getTypeName(
                classElement.asType(), env));
        doClassElement(classElement, entityMeta);
        doFieldElements(classElement, entityMeta);
        validateGeneratedId(classElement, entityMeta);
        doConstructor(classElement, entityMeta);
        return entityMeta;
    }

    protected TypeMirror resolveEntityListener(TypeElement classElement) {
        TypeMirror result = TypeMirrorUtil.getTypeMirror(
                NullEntityListener.class, env);
        for (AnnotationValue value : getEntityElementValueList(classElement,
                "listener")) {
            if (value != null) {
                TypeMirror listenerType = AnnotationValueUtil.toType(value);
                if (listenerType == null) {
                    throw new AptIllegalStateException("listener");
                }
                result = listenerType;
            }
        }
        return result;
    }

    protected NamingType resolveNamingType(TypeElement classElement) {
        NamingType result = NamingType.NONE;
        for (AnnotationValue value : getEntityElementValueList(classElement,
                "naming")) {
            if (value != null) {
                VariableElement enumConstant = AnnotationValueUtil
                        .toEnumConstant(value);
                if (enumConstant == null) {
                    throw new AptIllegalStateException("naming");
                }
                result = NamingType.valueOf(enumConstant.getSimpleName()
                        .toString());
            }
        }
        return result;
    }

    protected boolean resolveImmutable(TypeElement classElement,
            EntityMirror entityMirror) {
        boolean result = false;
        List<Boolean> resolvedList = new ArrayList<Boolean>();
        for (AnnotationValue value : getEntityElementValueList(classElement,
                "immutable")) {
            if (value != null) {
                Boolean immutable = AnnotationValueUtil.toBoolean(value);
                if (immutable == null) {
                    throw new AptIllegalStateException("immutable");
                }
                result = immutable.booleanValue();
                resolvedList.add(immutable);
            }
        }
        if (resolvedList.contains(Boolean.TRUE)
                && resolvedList.contains(Boolean.FALSE)) {
            throw new AptException(Message.DOMA4226, env, classElement,
                    entityMirror.getAnnotationMirror(),
                    entityMirror.getImmutable());
        }
        return result;
    }

    protected List<AnnotationValue> getEntityElementValueList(
            TypeElement classElement, String entityElementName) {
        List<AnnotationValue> list = new LinkedList<AnnotationValue>();
        for (TypeElement t = classElement; t != null
                && t.asType().getKind() != TypeKind.NONE; t = TypeMirrorUtil
                .toTypeElement(t.getSuperclass(), env)) {
            AnnotationMirror annMirror = ElementUtil.getAnnotationMirror(t,
                    Entity.class, env);
            if (annMirror == null) {
                continue;
            }
            AnnotationValue value = null;
            for (Map.Entry<? extends ExecutableElement, ? extends AnnotationValue> entry : annMirror
                    .getElementValues().entrySet()) {
                ExecutableElement element = entry.getKey();
                if (entityElementName
                        .equals(element.getSimpleName().toString())) {
                    value = entry.getValue();
                    break;
                }
            }
            list.add(value);
        }
        Collections.reverse(list);
        return list;
    }

    protected void doClassElement(TypeElement classElement,
            EntityMeta entityMeta) {
        validateClass(classElement, entityMeta);
        validateEntityListener(classElement, entityMeta);

        doTable(classElement, entityMeta);
    }

    protected void validateClass(TypeElement classElement, EntityMeta entityMeta) {
        EntityMirror entityMirror = entityMeta.getEntityMirror();
        if (classElement.getKind() != ElementKind.CLASS) {
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

    protected void validateEntityListener(TypeElement classElement,
            EntityMeta entityMeta) {
        EntityMirror entityMirror = entityMeta.getEntityMirror();
        TypeMirror listenerType = entityMirror.getListenerValue();
        TypeElement listenerElement = TypeMirrorUtil.toTypeElement(
                listenerType, env);
        if (listenerElement == null) {
            throw new AptIllegalStateException(
                    "failed to convert to TypeElement");
        }

        if (listenerElement.getModifiers().contains(Modifier.ABSTRACT)) {
            throw new AptException(Message.DOMA4166, env, classElement,
                    entityMirror.getAnnotationMirror(),
                    entityMirror.getListener(),
                    listenerElement.getQualifiedName());
        }

        ExecutableElement constructor = ElementUtil.getNoArgConstructor(
                listenerElement, env);
        if (constructor == null
                || !constructor.getModifiers().contains(Modifier.PUBLIC)) {
            throw new AptException(Message.DOMA4167, env, classElement,
                    entityMirror.getAnnotationMirror(),
                    entityMirror.getListener(),
                    listenerElement.getQualifiedName());
        }
        if (listenerElement.getTypeParameters().size() > 0) {
            validateGenericEntityListener(classElement, entityMeta,
                    listenerElement);
        } else {
            validateNonGenericEntityListener(classElement, entityMeta,
                    listenerType);
        }

        TypeElement inheritedListenerElement = entityMeta
                .getEntityListenerElement();
        if (!TypeMirrorUtil.isSameType(listenerType,
                inheritedListenerElement.asType(), env)) {
            validateInheritedEntityListener(classElement, entityMeta,
                    inheritedListenerElement);
        }
    }

    protected void validateGenericEntityListener(TypeElement classElement,
            EntityMeta entityMeta, TypeElement listenerElement) {
        EntityMirror entityMirror = entityMeta.getEntityMirror();
        List<? extends TypeParameterElement> typeParams = listenerElement
                .getTypeParameters();
        if (typeParams.size() == 0) {
            throw new AptIllegalStateException(
                    "typeParams size should be more than 0");
        }
        if (typeParams.size() > 1) {
            throw new AptException(Message.DOMA4227, env, classElement,
                    entityMirror.getAnnotationMirror(),
                    entityMirror.getListener());
        }
        TypeParameterElement typeParam = typeParams.get(0);
        for (TypeMirror bound : typeParam.getBounds()) {
            if (!TypeMirrorUtil.isAssignable(classElement.asType(), bound, env)) {
                throw new AptException(Message.DOMA4229, env, classElement,
                        entityMirror.getAnnotationMirror(),
                        entityMirror.getListener(), typeParam.getSimpleName(),
                        bound, classElement.getQualifiedName());
            }
        }
        if (findListenerTypeParam(listenerElement, 0) == null) {
            throw new AptException(Message.DOMA4228, env, classElement,
                    entityMirror.getAnnotationMirror(),
                    entityMirror.getListener(), typeParam.getSimpleName());
        }
    }

    protected TypeParameterElement findListenerTypeParam(
            TypeElement listenerElement, int typeParamIndex) {
        TypeParameterElement typeParam = listenerElement.getTypeParameters()
                .get(typeParamIndex);

        for (TypeMirror interfase : listenerElement.getInterfaces()) {
            DeclaredType declaredType = TypeMirrorUtil.toDeclaredType(
                    interfase, env);
            if (declaredType == null) {
                continue;
            }
            int i = -1;
            for (TypeMirror typeArg : declaredType.getTypeArguments()) {
                i++;
                TypeVariable typeVariable = TypeMirrorUtil.toTypeVariable(
                        typeArg, env);
                if (typeVariable == null) {
                    continue;
                }
                if (typeParam.getSimpleName().equals(
                        typeVariable.asElement().getSimpleName())) {
                    if (TypeMirrorUtil.isSameType(declaredType,
                            EntityListener.class, env)) {
                        return typeParam;
                    }
                    TypeElement typeElement = TypeMirrorUtil.toTypeElement(
                            declaredType, env);
                    if (typeElement == null) {
                        throw new AptIllegalStateException(
                                declaredType.toString());
                    }
                    TypeParameterElement candidate = findListenerTypeParam(
                            typeElement, i);
                    if (candidate != null) {
                        return candidate;
                    }
                }
            }
        }

        TypeMirror superclass = listenerElement.getSuperclass();
        DeclaredType declaredType = TypeMirrorUtil.toDeclaredType(superclass,
                env);
        if (declaredType == null) {
            return null;
        }
        int i = -1;
        for (TypeMirror typeArg : declaredType.getTypeArguments()) {
            i++;
            TypeVariable typeVariable = TypeMirrorUtil.toTypeVariable(typeArg,
                    env);
            if (typeVariable == null) {
                continue;
            }
            if (typeParam.getSimpleName().equals(
                    typeVariable.asElement().getSimpleName())) {
                if (TypeMirrorUtil.isSameType(declaredType,
                        EntityListener.class, env)) {
                    return typeParam;
                }
                TypeElement typeElement = TypeMirrorUtil.toTypeElement(
                        declaredType, env);
                if (typeElement == null) {
                    throw new AptIllegalStateException(declaredType.toString());
                }
                TypeParameterElement candidate = findListenerTypeParam(
                        typeElement, i);
                if (candidate != null) {
                    return candidate;
                }
            }
        }

        return null;
    }

    protected void validateNonGenericEntityListener(TypeElement classElement,
            EntityMeta entityMeta, TypeMirror listenerType) {
        EntityMirror entityMirror = entityMeta.getEntityMirror();
        TypeMirror argumentType = getListenerArgumentType(listenerType);
        if (argumentType == null) {
            throw new AptException(Message.DOMA4202, env, classElement,
                    entityMirror.getAnnotationMirror(),
                    entityMirror.getListener());
        }
        if (!TypeMirrorUtil.isAssignable(classElement.asType(), argumentType,
                env)) {
            throw new AptException(Message.DOMA4038, env, classElement,
                    entityMirror.getAnnotationMirror(),
                    entityMirror.getListener(), listenerType, argumentType,
                    classElement.getQualifiedName());
        }
    }

    protected void validateInheritedEntityListener(TypeElement classElement,
            EntityMeta entityMeta, TypeElement inheritedListenerElement) {
        EntityMirror entityMirror = entityMeta.getEntityMirror();
        List<? extends TypeParameterElement> typeParams = inheritedListenerElement
                .getTypeParameters();
        if (typeParams.size() == 0) {
            throw new AptException(Message.DOMA4230, env, classElement,
                    entityMirror.getAnnotationMirror(),
                    inheritedListenerElement.getQualifiedName(),
                    classElement.getQualifiedName());
        }
        TypeParameterElement typeParam = typeParams.get(0);
        for (TypeMirror bound : typeParam.getBounds()) {
            if (!TypeMirrorUtil.isAssignable(classElement.asType(), bound, env)) {
                throw new AptException(Message.DOMA4231, env, classElement,
                        entityMirror.getAnnotationMirror(),
                        inheritedListenerElement.getQualifiedName(),
                        typeParam.getSimpleName(), bound,
                        classElement.getQualifiedName());
            }
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
                if (declaredType == null) {
                    throw new AptIllegalStateException("declaredType");
                }
                List<? extends TypeMirror> args = declaredType
                        .getTypeArguments();
                if (args.size() != 1) {
                    return null;
                }
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
                } else if (fieldElement.getAnnotation(OriginalStates.class) != null) {
                    doOriginalStatesField(classElement, fieldElement,
                            entityMeta);
                } else {
                    doEntityPropertyMeta(fieldElement, entityMeta);
                    if (entityMeta.isImmutable()
                            && !fieldElement.getModifiers().contains(
                                    Modifier.FINAL)) {
                        throw new AptException(Message.DOMA4225, env,
                                fieldElement);
                    }
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
        if (entityMeta.hasOriginalStatesMeta()) {
            throw new AptException(Message.DOMA4125, env, fieldElement);
        }
        if (classElement.equals(fieldElement.getEnclosingElement())) {
            if (!TypeMirrorUtil.isSameType(fieldElement.asType(),
                    classElement.asType(), env)) {
                throw new AptException(Message.DOMA4135, env, fieldElement,
                        classElement.getQualifiedName());
            }
        }
        TypeElement enclosingElement = ElementUtil.toTypeElement(
                fieldElement.getEnclosingElement(), env);
        if (enclosingElement == null) {
            throw new AptIllegalStateException(fieldElement.toString());
        }
        if (entityMeta.isImmutable() && classElement.equals(enclosingElement)) {
            throw new AptException(Message.DOMA4224, env, fieldElement);
        }
        OriginalStatesMeta originalStatesMeta = new OriginalStatesMeta(
                classElement, fieldElement, enclosingElement, env);
        entityMeta.setOriginalStatesMeta(originalStatesMeta);
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

    protected void doConstructor(TypeElement classElement, EntityMeta entityMeta) {
        if (classElement.getModifiers().contains(Modifier.ABSTRACT)) {
            return;
        }
        if (entityMeta.isImmutable()) {
            ExecutableElement constructor = getSuitableConstructor(
                    classElement, entityMeta);
            if (constructor == null
                    || constructor.getModifiers().contains(Modifier.PRIVATE)) {
                throw new AptException(Message.DOMA4221, env, classElement);
            }
            entityMeta.setConstructor(constructor);
        } else {
            ExecutableElement constructor = ElementUtil.getNoArgConstructor(
                    classElement, env);
            if (constructor == null
                    || constructor.getModifiers().contains(Modifier.PRIVATE)) {
                throw new AptException(Message.DOMA4124, env, classElement);
            }
        }
    }

    protected ExecutableElement getSuitableConstructor(
            TypeElement classElement, EntityMeta entityMeta) {
        Map<String, TypeMirror> types = new HashMap<String, TypeMirror>();
        for (EntityPropertyMeta propertyMeta : entityMeta.getAllPropertyMetas()) {
            types.put(propertyMeta.getName(), propertyMeta.getType());
        }
        outer: for (ExecutableElement constructor : ElementFilter
                .constructorsIn(classElement.getEnclosedElements())) {
            int validCount = 0;
            for (VariableElement param : constructor.getParameters()) {
                String name = param.getSimpleName().toString();
                TypeMirror paramType = param.asType();
                TypeMirror propertyType = types.get(name);
                if (propertyType == null) {
                    continue outer;
                }
                if (!TypeMirrorUtil.isSameType(paramType, propertyType, env)) {
                    continue outer;
                }
                validCount++;
            }
            if (types.size() == validCount) {
                return constructor;
            }
        }
        return null;
    }

}
