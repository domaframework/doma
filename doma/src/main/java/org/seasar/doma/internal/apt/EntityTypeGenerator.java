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
package org.seasar.doma.internal.apt;

import static org.seasar.doma.internal.util.AssertionUtil.*;

import java.io.IOException;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.TypeElement;

import org.seasar.doma.internal.apt.meta.EntityMeta;
import org.seasar.doma.internal.apt.meta.EntityPropertyMeta;
import org.seasar.doma.internal.apt.meta.IdGeneratorMeta;
import org.seasar.doma.internal.apt.meta.IdGeneratorMetaVisitor;
import org.seasar.doma.internal.apt.meta.IdentityIdGeneratorMeta;
import org.seasar.doma.internal.apt.meta.OriginalStatesMeta;
import org.seasar.doma.internal.apt.meta.SequenceIdGeneratorMeta;
import org.seasar.doma.internal.apt.meta.TableIdGeneratorMeta;
import org.seasar.doma.internal.apt.type.BasicType;
import org.seasar.doma.internal.apt.type.DataType;
import org.seasar.doma.internal.apt.type.DomainType;
import org.seasar.doma.internal.apt.type.SimpleDataTypeVisitor;
import org.seasar.doma.internal.apt.type.WrapperType;
import org.seasar.doma.internal.jdbc.entity.AssignedIdPropertyType;
import org.seasar.doma.internal.jdbc.entity.BasicPropertyType;
import org.seasar.doma.internal.jdbc.entity.EntityPropertyType;
import org.seasar.doma.internal.jdbc.entity.EntityType;
import org.seasar.doma.internal.jdbc.entity.GeneratedIdPropertyType;
import org.seasar.doma.internal.jdbc.entity.VersionPropertyType;
import org.seasar.doma.internal.util.BoxedPrimitiveUtil;
import org.seasar.doma.internal.util.ClassUtil;
import org.seasar.doma.internal.util.StringUtil;
import org.seasar.doma.wrapper.Wrapper;

/**
 * 
 * @author taedium
 * 
 */
public class EntityTypeGenerator extends AbstractGenerator {

    protected final EntityMeta entityMeta;

    public EntityTypeGenerator(ProcessingEnvironment env,
            TypeElement entityElement, EntityMeta entityMeta)
            throws IOException {
        super(env, entityElement, null, null, Constants.DEFAULT_ENTITY_PREFIX,
                "");
        assertNotNull(entityMeta);
        this.entityMeta = entityMeta;
    }

    public void generate() {
        printPackage();
        printClass();
    }

    protected void printPackage() {
        if (!packageName.isEmpty()) {
            iprint("package %1$s;%n", packageName);
            iprint("%n");
        }
    }

    protected void printClass() {
        iprint("/** */%n");
        printGenerated();
        iprint("public final class %1$s implements %2$s<%3$s> {%n", simpleName,
                EntityType.class.getName(), entityMeta.getEntityTypeName());
        print("%n");
        indent();
        printTypeClassFields();
        printTypeClassConstructors();
        printTypeClassMethods();
        unindent();
        iprint("}%n");
    }

    protected void printEntityTypeClass() {
        iprint("private static class %1$sType implements %2$s<%3$s> {%n",
                entityMeta.getEntityName(), EntityType.class.getName(),
                entityMeta.getEntityTypeName());
        print("%n");
        indent();
        printTypeClassFields();
        printTypeClassConstructors();
        printTypeClassMethods();
        unindent();
        iprint("}%n");
        print("%n");
    }

    protected void printTypeClassFields() {
        printTypeClassSingletonField();
        printTypeClassGeneratedIdPropertyField();
        printTypeClassPropertyFields();
        printTypeClassListenerField();
        printTypeClassCatalogNameField();
        printTypeClassSchemaNameField();
        printTypeClassTableNameField();
        printTypeClassNameField();
        printTypeClassPropertiesField();
        printTypeClassPropertyMapField();
    }

    protected void printTypeClassSingletonField() {
        iprint("private static final %1$s __singleton = new %1$s();%n",
                simpleName);
        print("%n");
    }

    protected void printTypeClassGeneratedIdPropertyField() {
        if (entityMeta.hasGeneratedIdPropertyMeta()) {
            EntityPropertyMeta propertyMeta = entityMeta
                    .getGeneratedIdPropertyMeta();
            IdGeneratorMeta idGeneratorMeta = propertyMeta.getIdGeneratorMeta();
            idGeneratorMeta.accept(new IdGeneratorGenerator(), null);
            print("%n");
        }
    }

    protected void printTypeClassListenerField() {
        iprint("private final %1$s __listener;%n", entityMeta
                .getEntityListener());
        print("%n");
    }

    protected void printTypeClassCatalogNameField() {
        iprint("private final String __catalogName;%n");
        print("%n");
    }

    protected void printTypeClassSchemaNameField() {
        iprint("private final String __schemaName;%n");
        print("%n");
    }

    protected void printTypeClassTableNameField() {
        iprint("private final String __tableName;%n");
        print("%n");
    }

    protected void printTypeClassPropertyFields() {
        class Visitor extends
                SimpleDataTypeVisitor<Void, Void, RuntimeException> {

            WrapperType wrapperType;

            DomainType domainType;

            @Override
            protected Void defaultAction(DataType dataType, Void p)
                    throws RuntimeException {
                return assertUnreachable();
            }

            @Override
            public Void visitBasicType(BasicType basicType, Void p)
                    throws RuntimeException {
                this.wrapperType = basicType.getWrapperType();
                return null;
            }

            @Override
            public Void visitDomainType(DomainType domainType, Void p)
                    throws RuntimeException {
                this.domainType = domainType;
                this.wrapperType = domainType.getBasicType().getWrapperType();
                return null;
            }
        }

        for (EntityPropertyMeta pm : entityMeta.getAllPropertyMetas()) {
            Visitor visitor = new Visitor();
            pm.getDataType().accept(visitor, null);
            if (pm.isId()) {
                if (pm.getIdGeneratorMeta() != null) {
                    iprint(
                            "public final %1$s<%2$s, %3$s> %4$s = new %1$s<%2$s, %3$s>(\"%4$s\", \"%5$s\", __idGenerator) {%n", /* 1 */
                            GeneratedIdPropertyType.class.getName(), /* 2 */
                            entityMeta.getEntityTypeName(), /* 3 */
                            visitor.wrapperType.getWrappedType()
                                    .getTypeNameAsTypeParameter(), /* 4 */
                            pm.getName(), /* 5 */
                            pm.getColumnName());
                } else {
                    iprint(
                            "public final %1$s<%2$s, %3$s> %4$s = new %1$s<%2$s, %3$s>(\"%4$s\", \"%5$s\") {%n", /* 1 */
                            AssignedIdPropertyType.class.getName(), /* 2 */
                            entityMeta.getEntityTypeName(), /* 3 */
                            visitor.wrapperType.getWrappedType()
                                    .getTypeNameAsTypeParameter(), /* 4 */
                            pm.getName(), /* 5 */
                            pm.getColumnName());
                }
            } else if (pm.isVersion()) {
                iprint(
                        "public final %1$s<%2$s, %3$s> %4$s = new %1$s<%2$s, %3$s>(\"%4$s\", \"%5$s\") {%n", /* 1 */
                        VersionPropertyType.class.getName(), /* 2 */
                        entityMeta.getEntityTypeName(), /* 3 */
                        visitor.wrapperType.getWrappedType()
                                .getTypeNameAsTypeParameter(), /* 4 */
                        pm.getName(), /* 5 */
                        pm.getColumnName());
            } else {
                iprint(
                        "public final %1$s<%2$s, %3$s> %4$s = new %1$s<%2$s, %3$s>(\"%4$s\", \"%5$s\", %6$s, %7$s) {%n", /* 1 */
                        BasicPropertyType.class.getName(), /* 2 */
                        entityMeta.getEntityTypeName(), /* 3 */
                        visitor.wrapperType.getWrappedType()
                                .getTypeNameAsTypeParameter(), /* 4 */
                        pm.getName(), /* 5 */
                        pm.getColumnName(), /* 6 */pm.isColumnInsertable(), /* 7 */
                        pm.isColumnUpdatable());
            }
            indent();
            printEntityPropertyType_getWrapperMethod(pm, visitor.wrapperType);
            printEntityPropertyType_wrapperClass(pm, visitor.wrapperType,
                    visitor.domainType);
            unindent();
            iprint("};%n");
            print("%n");
        }
    }

    protected void printEntityPropertyType_getWrapperMethod(
            EntityPropertyMeta pm, WrapperType wrapperType) {
        iprint("@Override%n");
        iprint("public %1$s getWrapper(%2$s entity) {%n", wrapperType
                .getTypeName(), entityMeta.getEntityTypeName());
        iprint("    return new Wrapper(entity);%n");
        iprint("}%n");
        print("%n");
    }

    protected void printEntityPropertyType_wrapperClass(EntityPropertyMeta pm,
            WrapperType wrapperType, DomainType domainType) {
        iprint("class Wrapper extends %1$s {%n", wrapperType.getTypeName());
        print("%n");
        iprint("    private final %1$s entity;%n", entityMeta
                .getEntityTypeName());
        print("%n");
        iprint("    private Wrapper(%1$s entity) {%n", entityMeta
                .getEntityTypeName());
        if (wrapperType.getWrappedType().isEnum()) {
            iprint("        super(%1$s.class);%n", wrapperType.getWrappedType()
                    .getQualifiedName());
        }
        iprint("        this.entity = entity;%n");
        iprint("    }%n");
        print("%n");
        iprint("    @Override%n");
        iprint("    public %1$s get() {%n", wrapperType.getWrappedType()
                .getTypeNameAsTypeParameter());
        iprint("        if (entity == null) {%n");
        iprint("            return null;%n");
        iprint("        }%n");
        if (domainType != null) {
            iprint(
                    "        return %1$s.get().getWrapper(entity.%2$s).get();%n",
                    getPrefixedDomainTypeName(domainType.getTypeName()), pm
                            .getName());
        } else {
            iprint("        return entity.%1$s;%n", pm.getName());
        }
        iprint("    }%n");
        print("%n");
        iprint("    @Override%n");
        iprint("    public void set(%1$s value) {%n", wrapperType
                .getWrappedType().getTypeNameAsTypeParameter());
        iprint("        if (entity == null) {%n");
        iprint("            return;%n");
        iprint("        }%n");
        if (wrapperType.getWrappedType().isPrimitive()) {
            if (domainType != null) {
                iprint(
                        "        entity.%1$s = %2$s.get().newDomain(%3$s.unbox(value));%n",
                        pm.getName(), getPrefixedDomainTypeName(domainType
                                .getTypeName()), BoxedPrimitiveUtil.class
                                .getName());
            } else {
                iprint("        entity.%1$s = %2$s.unbox(value);%n", pm
                        .getName(), BoxedPrimitiveUtil.class.getName());
            }
        } else {
            if (domainType != null) {
                iprint("        entity.%1$s = %2$s.get().newDomain(value);%n",
                        pm.getName(), getPrefixedDomainTypeName(domainType
                                .getTypeName()));
            } else {
                iprint("        entity.%1$s = value;%n", pm.getName());
            }
        }
        iprint("    }%n");
        iprint("}%n");
    }

    protected void printTypeClassNameField() {
        iprint("private final String __name;%n");
        print("%n");
    }

    protected void printTypeClassPropertiesField() {
        iprint(
                "private final java.util.List<%1$s<%2$s, ?>> __entityPropertyTypes;%n",
                EntityPropertyType.class.getName(), entityMeta
                        .getEntityTypeName());
        print("%n");
    }

    protected void printTypeClassPropertyMapField() {
        iprint(
                "private final java.util.Map<String, %1$s<%2$s, ?>> __entityPropertyTypeMap;%n",
                EntityPropertyType.class.getName(), entityMeta
                        .getEntityTypeName());
        print("%n");
    }

    protected void printTypeClassConstructors() {
        iprint("private %1$s() {%n", simpleName);
        iprint("    __listener = new %1$s();%n", entityMeta.getEntityListener());
        iprint("    __name = \"%1$s\";%n", entityMeta.getEntityName());
        iprint("    __catalogName = \"%1$s\";%n", entityMeta.getCatalogName());
        iprint("    __schemaName = \"%1$s\";%n", entityMeta.getSchemaName());
        iprint("    __tableName = \"%1$s\";%n", entityMeta.getTableName());
        iprint(
                "    java.util.List<%1$s<%2$s, ?>> __list = new java.util.ArrayList<%1$s<%2$s, ?>>();%n",
                EntityPropertyType.class.getName(), entityMeta
                        .getEntityTypeName());
        iprint(
                "    java.util.Map<String, %1$s<%2$s, ?>> __map = new java.util.HashMap<String, %1$s<%2$s, ?>>();%n",
                EntityPropertyType.class.getName(), entityMeta
                        .getEntityTypeName());
        for (EntityPropertyMeta pm : entityMeta.getAllPropertyMetas()) {
            iprint("    __list.add(%1$s);%n", pm.getName());
            iprint("    __map.put(\"%1$s\", %1$s);%n", pm.getName());
        }
        iprint("    __entityPropertyTypes = java.util.Collections.unmodifiableList(__list);%n");
        iprint("    __entityPropertyTypeMap = java.util.Collections.unmodifiableMap(__map);%n");
        iprint("}%n");
        print("%n");
    }

    protected void printTypeClassMethods() {
        printTypeClassGetNameMethod();
        printTypeClassGetCatalogNameMethod();
        printTypeClassGetSchemaNameMethod();
        printTypeClassGetTableNameMethod();
        printTypeClassPreInsertMethod();
        printTypeClassPreUpdateMethod();
        printTypeClassPreDeleteMethod();
        printTypeClassGetPropertyMetasMethod();
        printTypeClassGetPropertyMetaMethod();
        printTypeClassGetGeneratedIdPropertyMethod();
        printTypeClassGetVersionPropertyMethod();
        printTypeClassNewEntityMethod();
        printTypeClassGetEntityClassMethod();
        printTypeClassGetOriginalStatesMethod();
        printTypeClassSaveCurrentStatesMethod();
        printTypeClassGetMethod();
    }

    protected void printTypeClassGetNameMethod() {
        iprint("@Override%n");
        iprint("public String getName() {%n");
        iprint("    return __name;%n");
        iprint("}%n");
        print("%n");
    }

    protected void printTypeClassGetCatalogNameMethod() {
        iprint("@Override%n");
        iprint("public String getCatalogName() {%n");
        iprint("    return __catalogName;%n");
        iprint("}%n");
        print("%n");
    }

    protected void printTypeClassGetSchemaNameMethod() {
        iprint("@Override%n");
        iprint("public String getSchemaName() {%n");
        iprint("    return __schemaName;%n");
        iprint("}%n");
        print("%n");
    }

    protected void printTypeClassGetTableNameMethod() {
        iprint("@Override%n");
        iprint("public String getTableName() {%n");
        iprint("    return __tableName;%n");
        iprint("}%n");
        print("%n");
    }

    protected void printTypeClassPreInsertMethod() {
        iprint("@Override%n");
        iprint("public void preInsert(%1$s entity) {%n", entityMeta
                .getEntityTypeName());
        iprint("    __listener.preInsert(entity);%n");
        iprint("}%n");
        print("%n");
    }

    protected void printTypeClassPreUpdateMethod() {
        iprint("@Override%n");
        iprint("public void preUpdate(%1$s entity) {%n", entityMeta
                .getEntityTypeName());
        iprint("    __listener.preUpdate(entity);%n");
        iprint("}%n");
        print("%n");
    }

    protected void printTypeClassPreDeleteMethod() {
        iprint("@Override%n");
        iprint("public void preDelete(%1$s entity) {%n", entityMeta
                .getEntityTypeName());
        iprint("    __listener.preDelete(entity);%n");
        iprint("}%n");
        print("%n");
    }

    protected void printTypeClassGetPropertyMetasMethod() {
        iprint("@Override%n");
        iprint(
                "public java.util.List<%1$s<%2$s, ?>> getEntityPropertyTypes() {%n",
                EntityPropertyType.class.getName(), entityMeta
                        .getEntityTypeName());
        iprint("    return __entityPropertyTypes;%n");
        iprint("}%n");
        print("%n");
    }

    protected void printTypeClassGetPropertyMetaMethod() {
        iprint("@Override%n");
        iprint("public %1$s<%2$s, ?> getEntityPropertyType(String __name) {%n",
                EntityPropertyType.class.getName(), entityMeta
                        .getEntityTypeName());
        iprint("    return __entityPropertyTypeMap.get(__name);%n");
        iprint("}%n");
        print("%n");
    }

    protected void printTypeClassGetGeneratedIdPropertyMethod() {
        iprint("@Override%n");
        iprint("public %1$s<%2$s, ?> getGeneratedIdPropertyType() {%n",
                GeneratedIdPropertyType.class.getName(), entityMeta
                        .getEntityTypeName());
        String idName = "null";
        if (entityMeta.hasGeneratedIdPropertyMeta()) {
            EntityPropertyMeta pm = entityMeta.getGeneratedIdPropertyMeta();
            idName = pm.getName();
        }
        iprint("    return %1$s;%n", idName);
        iprint("}%n");
        print("%n");
    }

    protected void printTypeClassGetVersionPropertyMethod() {
        iprint("@Override%n");
        iprint("public %1$s<%2$s, ?> getVersionPropertyType() {%n",
                VersionPropertyType.class.getName(), entityMeta
                        .getEntityTypeName());
        String versionName = "null";
        if (entityMeta.hasVersionPropertyMeta()) {
            EntityPropertyMeta pm = entityMeta.getVersionPropertyMeta();
            versionName = pm.getName();
        }
        iprint("    return %1$s;%n", versionName);
        iprint("}%n");
        print("%n");
    }

    protected void printTypeClassRefreshEntityMethod() {
        iprint("@Override%n");
        iprint("public void refreshEntity() {%n");
        iprint("    refreshEntityInternal();%n");
        iprint("}%n");
        print("%n");
    }

    protected void printTypeClassRefreshEntityInternalMethod() {
        iprint("private void refreshEntityInternal() {%n");
        for (final EntityPropertyMeta pm : entityMeta.getAllPropertyMetas()) {
            pm.getDataType().accept(
                    new SimpleDataTypeVisitor<Void, Void, RuntimeException>() {

                        @Override
                        protected Void defaultAction(DataType dataType, Void p)
                                throws RuntimeException {
                            return assertUnreachable();
                        }

                        @Override
                        public Void visitBasicType(BasicType basicType, Void p)
                                throws RuntimeException {
                            if (basicType.isPrimitive()) {
                                iprint(
                                        "    %1$s.%2$sAccessor.set%3$s(__entity, %4$s.unbox(%5$s.getWrapper().get()));%n",
                                        getPrefixedEntityTypeName(pm
                                                .getEntityTypeName()), pm
                                                .getEntityName(), StringUtil
                                                .capitalize(pm.getName()),
                                        BoxedPrimitiveUtil.class.getName(), pm
                                                .getName());
                            } else {
                                iprint(
                                        "    %1$s.%2$sAccessor.set%3$s(__entity, %4$s.getWrapper().get());%n",
                                        getPrefixedEntityTypeName(pm
                                                .getEntityTypeName()), pm
                                                .getEntityName(), StringUtil
                                                .capitalize(pm.getName()), pm
                                                .getName());
                            }
                            return null;
                        }

                        @Override
                        public Void visitDomainType(DomainType domainType,
                                Void p) throws RuntimeException {
                            iprint(
                                    "    %1$s.%2$sAccessor.set%3$s(__entity, %4$s.getWrapper().get());%n",
                                    getPrefixedEntityTypeName(pm
                                            .getEntityTypeName()), pm
                                            .getEntityName(), StringUtil
                                            .capitalize(pm.getName()), pm
                                            .getName());
                            return null;
                        }

                    }, null);
        }
        if (entityMeta.hasOriginalStatesMeta()) {
            OriginalStatesMeta osm = entityMeta.getOriginalStatesMeta();
            iprint(
                    "    java.util.HashMap<String, %1$s<?>> originalStates = null;%n",
                    Wrapper.class.getName());
            iprint("    if (__originalStates != null) {%n");
            iprint("        __originalStates.clear();%n");
            iprint("        originalStates = __originalStates;%n");
            iprint("    } else {%n");
            iprint(
                    "        originalStates = new java.util.HashMap<String, %1$s<?>>();%n",
                    Wrapper.class.getName());
            iprint("    }%n");
            for (EntityPropertyMeta pm : entityMeta.getAllPropertyMetas()) {
                iprint(
                        "    originalStates.put(\"%1$s\", %1$s.getWrapper().copy());%n",
                        pm.getName());
            }
            iprint(
                    "    %1$s.%2$sAccessor.set%3$s(__entity, originalStates);%n",
                    getPrefixedEntityTypeName(osm.getEntityTypeName()), osm
                            .getEntityName(), StringUtil.capitalize(osm
                            .getName()));
        }
        iprint("}%n");
        print("%n");
    }

    protected void printTypeClassRefreshEntityTypeInternalMethod() {
        iprint("private void refreshEntityTypeInternal() {%n");
        for (final EntityPropertyMeta pm : entityMeta.getAllPropertyMetas()) {
            pm.getDataType().accept(
                    new SimpleDataTypeVisitor<Void, Void, RuntimeException>() {

                        @Override
                        protected Void defaultAction(DataType dataType, Void p)
                                throws RuntimeException {
                            return assertUnreachable();
                        }

                        @Override
                        public Void visitBasicType(BasicType basicType, Void p)
                                throws RuntimeException {
                            iprint(
                                    "    %1$s.getWrapper().set(%2$s.%3$sAccessor.get%4$s(__entity));%n",
                                    pm.getName(), getPrefixedEntityTypeName(pm
                                            .getEntityTypeName()), pm
                                            .getEntityName(), StringUtil
                                            .capitalize(pm.getName()));
                            return null;
                        }

                        @Override
                        public Void visitDomainType(DomainType domainType,
                                Void p) throws RuntimeException {
                            iprint(
                                    "    %1$s.getWrapper().set(%2$s.%3$sAccessor.get%4$s(__entity));%n",
                                    pm.getName(), getPrefixedEntityTypeName(pm
                                            .getEntityTypeName()), pm
                                            .getEntityName(), StringUtil
                                            .capitalize(pm.getName()),
                                    domainType.getAccessorMetod());
                            return null;
                        }

                    }, null);
        }
        iprint("}%n");
        print("%n");
    }

    protected void printTypeClassNewEntityMethod() {
        iprint("@Override%n");
        iprint("public %1$s newEntity() {%n", entityMeta.getEntityTypeName());
        iprint("    return new %1$s();%n", entityMeta.getEntityTypeName());
        iprint("}%n");
        print("%n");
    }

    protected void printTypeClassGetEntityClassMethod() {
        iprint("@Override%n");
        iprint("public Class<%1$s> getEntityClass() {%n", entityMeta
                .getEntityTypeName());
        iprint("    return %1$s.class;%n", entityMeta.getEntityTypeName());
        iprint("}%n");
        print("%n");
    }

    protected void printTypeClassGetOriginalStatesMethod() {
        iprint("@Override%n");
        iprint("public %1$s getOriginalStates(%1$s __entity) {%n", entityMeta
                .getEntityTypeName());
        if (entityMeta.hasOriginalStatesMeta()) {
            OriginalStatesMeta osm = entityMeta.getOriginalStatesMeta();
            iprint("    if (__entity.%1$s instanceof %2$s) {%n", osm.getName(),
                    entityMeta.getEntityTypeName());
            iprint("        return (%1$s) __entity.%2$s;%n", entityMeta
                    .getEntityName(), osm.getName());
            iprint("    }%n");
        }
        iprint("    return null;%n");
        iprint("}%n");
        print("%n");
    }

    protected void printTypeClassSaveCurrentStatesMethod() {
        iprint("@Override%n");
        iprint("public void saveCurrentStates(%1$s __entity) {%n", entityMeta
                .getEntityTypeName());
        if (entityMeta.hasOriginalStatesMeta()) {
            iprint("    %1$s __currentStates = new %1$s();%n", entityMeta
                    .getEntityTypeName());
            for (EntityPropertyMeta pm : entityMeta.getAllPropertyMetas()) {
                iprint(
                        "    %1$s.getWrapper(__currentStates).set(%1$s.getWrapper(__entity).getCopy());%n",
                        pm.getName());
            }
            OriginalStatesMeta osm = entityMeta.getOriginalStatesMeta();
            iprint("    __entity.%1$s = __currentStates;%n", osm.getName());
        }
        iprint("}%n");
        print("%n");
    }

    protected void printTypeClassGetMethod() {
        iprint("/**%n");
        iprint(" * @return the singleton%n");
        iprint(" */%n");
        iprint("public static %1$s get() {%n", simpleName);
        iprint("    return __singleton;%n");
        iprint("}%n");
        print("%n");
    }

    protected String getPrefixedEntityTypeName(String entityTypeName) {
        return ClassUtil.getPackageName(entityTypeName) + "." + prefix
                + ClassUtil.getSimpleName(entityTypeName);
    }

    protected String getPrefixedDomainTypeName(String domainTypeName) {
        return ClassUtil.getPackageName(domainTypeName) + "."
                + Constants.DEFAULT_DOMAIN_PREFIX
                + ClassUtil.getSimpleName(domainTypeName);
    }

    protected class IdGeneratorGenerator implements
            IdGeneratorMetaVisitor<Void, Void> {

        @Override
        public Void visistIdentityIdGeneratorMeta(IdentityIdGeneratorMeta m,
                Void p) {
            iprint("private final %1$s __idGenerator = new %1$s();%n", m
                    .getIdGeneratorClassName());
            return null;
        }

        @Override
        public Void visistSequenceIdGeneratorMeta(SequenceIdGeneratorMeta m,
                Void p) {
            iprint("private final %1$s __idGenerator = new %1$s();%n", m
                    .getIdGeneratorClassName());
            iprint("{%n");
            iprint("    __idGenerator.setQualifiedSequenceName(\"%1$s\");%n", m
                    .getQualifiedSequenceName());
            iprint("    __idGenerator.setInitialValue(%1$s);%n", m
                    .getInitialValue());
            iprint("    __idGenerator.setAllocationSize(%1$s);%n", m
                    .getAllocationSize());
            iprint("    __idGenerator.initialize();%n");
            iprint("}%n");
            return null;
        }

        @Override
        public Void visistTableIdGeneratorMeta(TableIdGeneratorMeta m, Void p) {
            iprint("private final %1$s __idGenerator = new %1$s();%n", m
                    .getIdGeneratorClassName());
            iprint("{%n");
            iprint("    __idGenerator.setQualifiedTableName(\"%1$s\");%n", m
                    .getQualifiedTableName());
            iprint("    __idGenerator.setInitialValue(%1$s);%n", m
                    .getInitialValue());
            iprint("    __idGenerator.setAllocationSize(%1$s);%n", m
                    .getAllocationSize());
            iprint("    __idGenerator.setPkColumnName(\"%1$s\");%n", m
                    .getPkColumnName());
            iprint("    __idGenerator.setPkColumnValue(\"%1$s\");%n", m
                    .getPkColumnValue());
            iprint("    __idGenerator.setValueColumnName(\"%1$s\");%n", m
                    .getValueColumnName());
            iprint("    __idGenerator.initialize();%n");
            iprint("}%n");
            return null;
        }
    }

}
