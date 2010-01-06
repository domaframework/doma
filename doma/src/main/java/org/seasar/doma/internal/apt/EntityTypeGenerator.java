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
import org.seasar.doma.internal.jdbc.util.TableUtil;
import org.seasar.doma.internal.util.BoxedPrimitiveUtil;
import org.seasar.doma.internal.util.ClassUtil;
import org.seasar.doma.jdbc.entity.NamingType;

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
        printFields();
        printConstructor();
        printMethods();
        unindent();
        iprint("}%n");
    }

    protected void printFields() {
        printSingletonField();
        printIdGeneratorField();
        printPropertyTypeFields();
        printListenerField();
        printNamingTypeField();
        printCatalogNameField();
        printSchemaNameField();
        printTableNameField();
        printQualifiedTableNameField();
        printNameField();
        printIdPropertyTypesField();
        printEntityPropertyTypesField();
        printEntityPropertyTypeMapField();
    }

    protected void printSingletonField() {
        iprint("private static final %1$s __singleton = new %1$s();%n",
                simpleName);
        print("%n");
    }

    protected void printIdGeneratorField() {
        if (entityMeta.hasGeneratedIdPropertyMeta()) {
            EntityPropertyMeta propertyMeta = entityMeta
                    .getGeneratedIdPropertyMeta();
            IdGeneratorMeta idGeneratorMeta = propertyMeta.getIdGeneratorMeta();
            idGeneratorMeta.accept(new IdGeneratorGenerator(), null);
            print("%n");
        }
    }

    protected void printPropertyTypeFields() {
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
            iprint("/** the %1$s */%n", pm.getName());
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
            printPropertyTypeFields_getWrapperMethod(pm, visitor.wrapperType);
            printPropertyTypeFields_WrapperClass(pm, visitor.wrapperType,
                    visitor.domainType);
            unindent();
            iprint("};%n");
            print("%n");
        }
    }

    protected void printPropertyTypeFields_getWrapperMethod(
            EntityPropertyMeta pm, WrapperType wrapperType) {
        iprint("@Override%n");
        iprint("public %1$s getWrapper(%2$s entity) {%n", wrapperType
                .getTypeName(), entityMeta.getEntityTypeName());
        iprint("    return new Wrapper(entity);%n");
        iprint("}%n");
        print("%n");
    }

    protected void printPropertyTypeFields_WrapperClass(EntityPropertyMeta pm,
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
        iprint("    protected %1$s doGet() {%n", wrapperType.getWrappedType()
                .getTypeNameAsTypeParameter());
        iprint("        if (entity == null) {%n");
        iprint("            return null;%n");
        iprint("        }%n");
        if (pm.isOwnProperty()) {
            if (domainType != null) {
                iprint(
                        "        return %1$s.get().getWrapper(entity.%2$s).get();%n",
                        getPrefixedDomainTypeName(domainType.getTypeName()), pm
                                .getName());
            } else {
                iprint("        return entity.%1$s;%n", pm.getName());
            }
        } else {
            iprint(
                    "        return %1$s.get().%2$s.getWrapper(entity).get();%n",
                    getPrefixedEntityTypeName(pm.getEntityTypeName()), pm
                            .getName());
        }
        iprint("    }%n");
        print("%n");
        iprint("    @Override%n");
        iprint("    protected void doSet(%1$s value) {%n", wrapperType
                .getWrappedType().getTypeNameAsTypeParameter());
        iprint("        if (entity == null) {%n");
        iprint("            return;%n");
        iprint("        }%n");
        if (pm.isOwnProperty()) {
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
                    iprint(
                            "        entity.%1$s = %2$s.get().newDomain(value);%n",
                            pm.getName(), getPrefixedDomainTypeName(domainType
                                    .getTypeName()));
                } else {
                    iprint("        entity.%1$s = value;%n", pm.getName());
                }
            }
        } else {
            iprint("        %1$s.get().%2$s.getWrapper(entity).set(value);%n",
                    getPrefixedEntityTypeName(pm.getEntityTypeName()), pm
                            .getName());
        }
        iprint("    }%n");
        iprint("}%n");
    }

    protected void printListenerField() {
        iprint("private final %1$s __listener;%n", entityMeta
                .getEntityListener());
        print("%n");
    }

    protected void printNamingTypeField() {
        iprint("private final %1$s __namingType;%n", NamingType.class.getName());
        print("%n");
    }

    protected void printCatalogNameField() {
        iprint("private final String __catalogName;%n");
        print("%n");
    }

    protected void printSchemaNameField() {
        iprint("private final String __schemaName;%n");
        print("%n");
    }

    protected void printTableNameField() {
        iprint("private final String __tableName;%n");
        print("%n");
    }

    protected void printQualifiedTableNameField() {
        iprint("private final String __qualifiedTableName;%n");
        print("%n");
    }

    protected void printNameField() {
        iprint("private final String __name;%n");
        print("%n");
    }

    protected void printIdPropertyTypesField() {
        iprint(
                "private final java.util.List<%1$s<%2$s, ?>> __idPropertyTypes;%n",
                EntityPropertyType.class.getName(), entityMeta
                        .getEntityTypeName());
        print("%n");
    }

    protected void printEntityPropertyTypesField() {
        iprint(
                "private final java.util.List<%1$s<%2$s, ?>> __entityPropertyTypes;%n",
                EntityPropertyType.class.getName(), entityMeta
                        .getEntityTypeName());
        print("%n");
    }

    protected void printEntityPropertyTypeMapField() {
        iprint(
                "private final java.util.Map<String, %1$s<%2$s, ?>> __entityPropertyTypeMap;%n",
                EntityPropertyType.class.getName(), entityMeta
                        .getEntityTypeName());
        print("%n");
    }

    protected void printConstructor() {
        iprint("private %1$s() {%n", simpleName);
        iprint("    __listener = new %1$s();%n", entityMeta.getEntityListener());
        iprint("    __namingType = %1$s.%2$s;%n", NamingType.class.getName(),
                entityMeta.getNamingType().name());
        iprint("    __name = \"%1$s\";%n", entityMeta.getEntityName());
        iprint("    __catalogName = \"%1$s\";%n", entityMeta.getCatalogName());
        iprint("    __schemaName = \"%1$s\";%n", entityMeta.getSchemaName());
        iprint("    __tableName = \"%1$s\";%n", entityMeta.getTableName());
        iprint("    __qualifiedTableName = \"%1$s\";%n", TableUtil
                .getQualifiedTableName(entityMeta.getCatalogName(), entityMeta
                        .getSchemaName(), entityMeta.getTableName()));
        iprint(
                "    java.util.List<%1$s<%2$s, ?>> __idList = new java.util.ArrayList<%1$s<%2$s, ?>>();%n",
                EntityPropertyType.class.getName(), entityMeta
                        .getEntityTypeName());
        iprint(
                "    java.util.List<%1$s<%2$s, ?>> __list = new java.util.ArrayList<%1$s<%2$s, ?>>(%3$s);%n",
                EntityPropertyType.class.getName(), entityMeta
                        .getEntityTypeName(), entityMeta.getAllPropertyMetas()
                        .size());
        iprint(
                "    java.util.Map<String, %1$s<%2$s, ?>> __map = new java.util.HashMap<String, %1$s<%2$s, ?>>(%3$s);%n",
                EntityPropertyType.class.getName(), entityMeta
                        .getEntityTypeName(), entityMeta.getAllPropertyMetas()
                        .size());
        for (EntityPropertyMeta pm : entityMeta.getAllPropertyMetas()) {
            if (pm.isId()) {
                iprint("    __idList.add(%1$s);%n", pm.getName());
            }
            iprint("    __list.add(%1$s);%n", pm.getName());
            iprint("    __map.put(\"%1$s\", %1$s);%n", pm.getName());
        }
        iprint("    __idPropertyTypes = java.util.Collections.unmodifiableList(__idList);%n");
        iprint("    __entityPropertyTypes = java.util.Collections.unmodifiableList(__list);%n");
        iprint("    __entityPropertyTypeMap = java.util.Collections.unmodifiableMap(__map);%n");
        iprint("}%n");
        print("%n");
    }

    protected void printMethods() {
        printGetNamingTypeMethod();
        printGetNameMethod();
        printGetCatalogNameMethod();
        printGetSchemaNameMethod();
        printGetTableNameMethod();
        printGetQualifiedTableNameMethod();
        printPreInsertMethod();
        printPreUpdateMethod();
        printPreDeleteMethod();
        printGetEntityPropertyTypesMethod();
        printGetEntityPropertyTypeMethod();
        printGetIdPropertyTypesMethod();
        printGetGeneratedIdPropertyTypeMethod();
        printGetVersionPropertyTypeMethod();
        printNewEntityMethod();
        printGetEntityClassMethod();
        printGetOriginalStatesMethod();
        printSaveCurrentStatesMethod();
        printGetMethod();
    }

    protected void printGetNamingTypeMethod() {
        iprint("@Override%n");
        iprint("public %1$s getNamingType() {%n", NamingType.class.getName());
        iprint("    return __namingType;%n");
        iprint("}%n");
        print("%n");
    }

    protected void printGetNameMethod() {
        iprint("@Override%n");
        iprint("public String getName() {%n");
        iprint("    return __name;%n");
        iprint("}%n");
        print("%n");
    }

    protected void printGetCatalogNameMethod() {
        iprint("@Override%n");
        iprint("public String getCatalogName() {%n");
        iprint("    return __catalogName;%n");
        iprint("}%n");
        print("%n");
    }

    protected void printGetSchemaNameMethod() {
        iprint("@Override%n");
        iprint("public String getSchemaName() {%n");
        iprint("    return __schemaName;%n");
        iprint("}%n");
        print("%n");
    }

    protected void printGetTableNameMethod() {
        iprint("@Override%n");
        iprint("public String getTableName() {%n");
        iprint("    return __tableName;%n");
        iprint("}%n");
        print("%n");
    }

    protected void printGetQualifiedTableNameMethod() {
        iprint("@Override%n");
        iprint("public String getQualifiedTableName() {%n");
        iprint("    return __qualifiedTableName;%n");
        iprint("}%n");
        print("%n");
    }

    protected void printPreInsertMethod() {
        iprint("@Override%n");
        iprint("public void preInsert(%1$s entity) {%n", entityMeta
                .getEntityTypeName());
        iprint("    __listener.preInsert(entity);%n");
        iprint("}%n");
        print("%n");
    }

    protected void printPreUpdateMethod() {
        iprint("@Override%n");
        iprint("public void preUpdate(%1$s entity) {%n", entityMeta
                .getEntityTypeName());
        iprint("    __listener.preUpdate(entity);%n");
        iprint("}%n");
        print("%n");
    }

    protected void printPreDeleteMethod() {
        iprint("@Override%n");
        iprint("public void preDelete(%1$s entity) {%n", entityMeta
                .getEntityTypeName());
        iprint("    __listener.preDelete(entity);%n");
        iprint("}%n");
        print("%n");
    }

    protected void printGetEntityPropertyTypesMethod() {
        iprint("@Override%n");
        iprint(
                "public java.util.List<%1$s<%2$s, ?>> getEntityPropertyTypes() {%n",
                EntityPropertyType.class.getName(), entityMeta
                        .getEntityTypeName());
        iprint("    return __entityPropertyTypes;%n");
        iprint("}%n");
        print("%n");
    }

    protected void printGetEntityPropertyTypeMethod() {
        iprint("@Override%n");
        iprint("public %1$s<%2$s, ?> getEntityPropertyType(String __name) {%n",
                EntityPropertyType.class.getName(), entityMeta
                        .getEntityTypeName());
        iprint("    return __entityPropertyTypeMap.get(__name);%n");
        iprint("}%n");
        print("%n");
    }

    protected void printGetIdPropertyTypesMethod() {
        iprint("@Override%n");
        iprint("public java.util.List<%1$s<%2$s, ?>> getIdPropertyTypes() {%n",
                EntityPropertyType.class.getName(), entityMeta
                        .getEntityTypeName());
        iprint("    return __idPropertyTypes;%n");
        iprint("}%n");
        print("%n");
    }

    protected void printGetGeneratedIdPropertyTypeMethod() {
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

    protected void printGetVersionPropertyTypeMethod() {
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

    protected void printNewEntityMethod() {
        iprint("@Override%n");
        iprint("public %1$s newEntity() {%n", entityMeta.getEntityTypeName());
        iprint("    return new %1$s();%n", entityMeta.getEntityTypeName());
        iprint("}%n");
        print("%n");
    }

    protected void printGetEntityClassMethod() {
        iprint("@Override%n");
        iprint("public Class<%1$s> getEntityClass() {%n", entityMeta
                .getEntityTypeName());
        iprint("    return %1$s.class;%n", entityMeta.getEntityTypeName());
        iprint("}%n");
        print("%n");
    }

    protected void printGetOriginalStatesMethod() {
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

    protected void printSaveCurrentStatesMethod() {
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

    protected void printGetMethod() {
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
