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
package org.seasar.doma.internal.apt;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.function.BiFunction;

import javax.lang.model.element.TypeElement;

import org.seasar.doma.internal.apt.cttype.BasicCtType;
import org.seasar.doma.internal.apt.cttype.CtType;
import org.seasar.doma.internal.apt.cttype.HolderCtType;
import org.seasar.doma.internal.apt.cttype.OptionalCtType;
import org.seasar.doma.internal.apt.cttype.OptionalDoubleCtType;
import org.seasar.doma.internal.apt.cttype.OptionalIntCtType;
import org.seasar.doma.internal.apt.cttype.OptionalLongCtType;
import org.seasar.doma.internal.apt.cttype.SimpleCtTypeVisitor;
import org.seasar.doma.internal.apt.meta.entity.EntityMeta;
import org.seasar.doma.internal.apt.meta.entity.EntityPropertyMeta;
import org.seasar.doma.internal.apt.meta.entity.OriginalStatesMeta;
import org.seasar.doma.internal.apt.meta.id.IdGeneratorMeta;
import org.seasar.doma.internal.apt.meta.id.IdGeneratorMetaVisitor;
import org.seasar.doma.internal.apt.meta.id.IdentityIdGeneratorMeta;
import org.seasar.doma.internal.apt.meta.id.SequenceIdGeneratorMeta;
import org.seasar.doma.internal.apt.meta.id.TableIdGeneratorMeta;
import org.seasar.doma.jdbc.entity.AbstractEntityType;
import org.seasar.doma.jdbc.entity.AssignedIdPropertyType;
import org.seasar.doma.jdbc.entity.DefaultPropertyType;
import org.seasar.doma.jdbc.entity.EmbeddedPropertyType;
import org.seasar.doma.jdbc.entity.EntityPropertyType;
import org.seasar.doma.jdbc.entity.GeneratedIdPropertyType;
import org.seasar.doma.jdbc.entity.NamingType;
import org.seasar.doma.jdbc.entity.OriginalStatesAccessor;
import org.seasar.doma.jdbc.entity.PostDeleteContext;
import org.seasar.doma.jdbc.entity.PostInsertContext;
import org.seasar.doma.jdbc.entity.PostUpdateContext;
import org.seasar.doma.jdbc.entity.PreDeleteContext;
import org.seasar.doma.jdbc.entity.PreInsertContext;
import org.seasar.doma.jdbc.entity.PreUpdateContext;
import org.seasar.doma.jdbc.entity.Property;
import org.seasar.doma.jdbc.entity.VersionPropertyType;

/**
 * 
 * @author taedium
 * 
 */
public class EntityTypeGenerator extends AbstractGenerator {

    private final EntityMeta entityMeta;

    public EntityTypeGenerator(Context ctx, TypeElement entityElement, EntityMeta entityMeta)
            throws IOException {
        super(ctx, entityElement, entityMeta.getEntityDescCanonicalName());
        assertNotNull(entityMeta);
        this.entityMeta = entityMeta;
    }

    @Override
    public void generate() {
        printPackage();
        printClass();
    }

    private void printPackage() {
        if (!packageName.isEmpty()) {
            iprint("package %1$s;%n", packageName);
            iprint("%n");
        }
    }

    private void printClass() {
        iprint("/** */%n");
        printGenerated();
        iprint("public final class %1$s extends %2$s<%3$s> {%n",
                // @formatter:off
                /* 1 */simpleName,
                /* 2 */AbstractEntityType.class.getName(),
                /* 3 */entityMeta.getEntityTypeName());
                // @formatter:on
        print("%n");
        indent();
        printValidateVersionStaticInitializer();
        printFields();
        printConstructor();
        printMethods();
        printListenerHolder();
        unindent();
        iprint("}%n");
    }

    private void printFields() {
        printSingletonField();
        printOriginalStatesAccessorField();
        printNamingTypeField();
        printIdGeneratorField();
        printPropertyTypeFields();
        printListenerSupplierField();
        printImmutableField();
        printCatalogNameField();
        printSchemaNameField();
        printTableNameField();
        printIsQuoteRequiredField();
        printNameField();
        printIdPropertyTypesField();
        printEntityPropertyTypesField();
        printEntityPropertyTypeMapField();
    }

    private void printSingletonField() {
        iprint("private static final %1$s __singleton = new %1$s();%n",
                // @formatter:off
                /* 1 */simpleName);
                // @formatter:on
        print("%n");
    }

    private void printOriginalStatesAccessorField() {
        if (!entityMeta.isAbstract() && entityMeta.hasOriginalStatesMeta()) {
            OriginalStatesMeta osm = entityMeta.getOriginalStatesMeta();
            iprint("private static final %1$s<%2$s> __originalStatesAccessor = new %1$s<>(%3$s.class, \"%4$s\");%n",
                    // @formatter:off
                    /* 1 */OriginalStatesAccessor.class.getName(),
                    /* 2 */osm.getTypeElement().getQualifiedName(),
                    /* 3 */osm.getFieldEnclosingElement().getQualifiedName(),
                    /* 4 */osm.getFieldElement().getSimpleName());
                    // @formatter:on
            print("%n");
        }
    }

    private void printIdGeneratorField() {
        if (entityMeta.hasGeneratedIdPropertyMeta()) {
            EntityPropertyMeta propertyMeta = entityMeta.getGeneratedIdPropertyMeta();
            IdGeneratorMeta idGeneratorMeta = propertyMeta.getIdGeneratorMeta();
            idGeneratorMeta.accept(new IdGeneratorGenerator(), null);
            print("%n");
        }
    }

    private void printPropertyTypeFields() {
        for (EntityPropertyMeta pm : entityMeta.getAllPropertyMetas()) {
            iprint("/** the %1$s */%n", pm.getName());
            if (pm.isEmbedded()) {
                iprint("public final %1$s<%2$s, %3$s> %4$s = "
                        + "new %1$s<>(\"%5$s\", %2$s.class, %6$s.getSingletonInternal().getEmbeddablePropertyTypes"
                        + "(\"%7$s\", %2$s.class, __namingType));%n",
                        // @formatter:off
                        /* 1 */EmbeddedPropertyType.class.getName(),
                        /* 2 */entityMeta.getEntityTypeName(), 
                        /* 3 */pm.getTypeName(),
                        /* 4 */pm.getFieldName(), 
                        /* 5 */pm.getName(),
                        /* 6 */pm.getEmbeddableMetaTypeName(), 
                        /* 7 */pm.getName());
                        // @formatter:on
            } else {
                Object[] args = createPropertyFormatArgs(pm);
                Object className = args[0];
                if (GeneratedIdPropertyType.class.getName().equals(className)) {
                    iprint("public final %1$s<%2$s, %3$s, %4$s> %5$s = "
                            + "new %1$s<>(%6$s, %7$s, %8$s, %9$s, %10$s, __namingType, %11$s, __idGenerator);%n",
                            args);
                } else if (AssignedIdPropertyType.class.getName().equals(className)) {
                    iprint("public final %1$s<%2$s, %3$s, %4$s> %5$s = "
                            + "new %1$s<>(%6$s, %7$s, %8$s, %9$s, %10$s, __namingType, %11$s);%n",
                            args);
                } else if (VersionPropertyType.class.getName().equals(className)) {
                    iprint("public final %1$s<%2$s, %3$s, %4$s> %5$s = "
                            + "new %1$s<>(%6$s, %7$s, %8$s, %9$s, %10$s, __namingType, %11$s);%n",
                            args);
                } else {
                    iprint("public final %1$s<%2$s, %3$s, %4$s> %5$s = "
                            + "new %1$s<>(%6$s, %7$s, %8$s, %9$s, %10$s, __namingType, %12$s, %13$s, %11$s);%n",
                            args);
                }
            }
            print("%n");
        }
    }

    private Object[] createPropertyFormatArgs(EntityPropertyMeta pm) {
        EntityPropertyCtTypeVisitor visitor = new EntityPropertyCtTypeVisitor();
        pm.getCtType().accept(visitor, null);
        BasicCtType basicCtType = visitor.basicCtType;
        HolderCtType holderCtType = visitor.holderCtType;

        class Args {
            String _1_propertyTypeClass;
            String _2_ENTITY;
            String _3_BASIC;
            String _4_HOLDER;
            String _5_fieldName;
            String _6_entityClass;
            String _7_wrapperSupplier;
            String _8_holderType;
            String _9_name;
            String _10_columnName;
            boolean _11_quoteRequired;
            boolean _12_insertable;
            boolean _13_updatable;

            Object[] toArray() {
                return new Object[] { _1_propertyTypeClass, _2_ENTITY, _3_BASIC, _4_HOLDER,
                        _5_fieldName, _6_entityClass, _7_wrapperSupplier, _8_holderType, _9_name,
                        _10_columnName, _11_quoteRequired, _12_insertable, _13_updatable };
            }
        }

        Args args = new Args();
        if (pm.isId()) {
            if (pm.getIdGeneratorMeta() != null) {
                args._1_propertyTypeClass = GeneratedIdPropertyType.class.getName();
            } else {
                args._1_propertyTypeClass = AssignedIdPropertyType.class.getName();
            }
        } else if (pm.isVersion()) {
            args._1_propertyTypeClass = VersionPropertyType.class.getName();
        } else {
            args._1_propertyTypeClass = DefaultPropertyType.class.getName();
        }
        args._2_ENTITY = entityMeta.getEntityTypeName();
        args._3_BASIC = box(basicCtType);
        args._4_HOLDER = holderCtType == null ? "Object" : holderCtType.getTypeName();
        args._5_fieldName = pm.getFieldName();
        args._6_entityClass = entityMeta.getEntityTypeName() + ".class";
        args._7_wrapperSupplier = supply(basicCtType);
        args._8_holderType = holderCtType == null ? "null" : holderCtType.getInstantiationCommand();
        args._9_name = "\"" + pm.getName() + "\"";
        args._10_columnName = "\"" + pm.getColumnName() + "\"";
        args._11_quoteRequired = pm.isColumnQuoteRequired();
        args._12_insertable = pm.isColumnInsertable();
        args._13_updatable = pm.isColumnUpdatable();
        return args.toArray();
    }

    private void printListenerSupplierField() {
        if (entityMeta.hasGenericListener()) {
            iprint("private final java.util.function.Supplier<%1$s<%2$s>> __listenerSupplier;%n",
                    // @formatter:off
                    /* 1 */entityMeta.getListenerElement().getQualifiedName(),
                    /* 2 */entityMeta.getEntityTypeName());
                    // @formatter:on
        } else {
            iprint("private final java.util.function.Supplier<%1$s> __listenerSupplier;%n",
                    entityMeta.getListenerElement().getQualifiedName());
        }
        print("%n");
    }

    private void printNamingTypeField() {
        NamingType namingType = entityMeta.getNamingType();
        if (namingType == null) {
            iprint("private final %1$s __namingType = null;%n",
                    // @formatter:off
                    /* 1 */NamingType.class.getName());
                    // @formatter:on
        } else {
            iprint("private final %1$s __namingType = %1$s.%2$s;%n",
                    // @formatter:off
                    /* 1 */NamingType.class.getName(),
                    /* 2 */namingType.name());
                    // @formatter:on
        }
        print("%n");
    }

    private void printImmutableField() {
        iprint("private final boolean __immutable;%n");
        print("%n");
    }

    private void printCatalogNameField() {
        iprint("private final String __catalogName;%n");
        print("%n");
    }

    private void printSchemaNameField() {
        iprint("private final String __schemaName;%n");
        print("%n");
    }

    private void printTableNameField() {
        iprint("private final String __tableName;%n");
        print("%n");
    }

    private void printIsQuoteRequiredField() {
        iprint("private final boolean __isQuoteRequired;%n");
        print("%n");
    }

    private void printNameField() {
        iprint("private final String __name;%n");
        print("%n");
    }

    private void printIdPropertyTypesField() {
        iprint("private final java.util.List<%1$s<%2$s, ?>> __idPropertyTypes;%n",
                // @formatter:off
                /* 1 */EntityPropertyType.class.getName(), 
                /* 2 */entityMeta.getEntityTypeName());
                // @formatter:on
        print("%n");
    }

    private void printEntityPropertyTypesField() {
        iprint("private final java.util.List<%1$s<%2$s, ?>> __entityPropertyTypes;%n",
                // @formatter:off
                /* 1 */EntityPropertyType.class.getName(), 
                /* 2 */entityMeta.getEntityTypeName());
                // @formatter:on
        print("%n");
    }

    private void printEntityPropertyTypeMapField() {
        iprint("private final java.util.Map<String, %1$s<%2$s, ?>> __entityPropertyTypeMap;%n",
                // @formatter:off
                /* 1 */EntityPropertyType.class.getName(),
                /* 2 */entityMeta.getEntityTypeName());
                // @formatter:on
        print("%n");
    }

    private void printConstructor() {
        iprint("private %1$s() {%n", simpleName);
        iprint("    __listenerSupplier = () -> ListenerHolder.listener;%n");
        iprint("    __immutable = %1$s;%n", entityMeta.isImmutable());
        iprint("    __name = \"%1$s\";%n", entityMeta.getEntityName());
        iprint("    __catalogName = \"%1$s\";%n", entityMeta.getCatalogName());
        iprint("    __schemaName = \"%1$s\";%n", entityMeta.getSchemaName());
        iprint("    __tableName = \"%1$s\";%n", entityMeta.getTableName());
        iprint("    __isQuoteRequired = %1$s;%n", entityMeta.isQuoteRequired());
        iprint("    java.util.List<%1$s<%2$s, ?>> __idList = new java.util.ArrayList<>();%n",
                // @formatter:off
                /* 1 */EntityPropertyType.class.getName(),
                /* 2 */entityMeta.getEntityTypeName());
                // @formatter:on
        iprint("    java.util.List<%1$s<%2$s, ?>> __list = new java.util.ArrayList<>(%3$s);%n",
                // @formatter:off
                /* 1 */EntityPropertyType.class.getName(),
                /* 2 */entityMeta.getEntityTypeName(),
                /* 3 */entityMeta.getAllPropertyMetas().size());
                // @formatter:on
        iprint("    java.util.Map<String, %1$s<%2$s, ?>> __map = new java.util.HashMap<>(%3$s);%n",
                // @formatter:off
                /* 1 */EntityPropertyType.class.getName(),
                /* 2 */entityMeta.getEntityTypeName(),
                /* 3 */entityMeta.getAllPropertyMetas().size());
                // @formatter:off
        for (EntityPropertyMeta pm : entityMeta.getAllPropertyMetas()) {
            if (pm.isEmbedded()) {
                iprint("    __list.addAll(%1$s.getEmbeddablePropertyTypes());%n",
                        pm.getFieldName());
                iprint("    __map.putAll(%1$s.getEmbeddablePropertyTypeMap());%n",
                        pm.getFieldName());
            } else {
                if (pm.isId()) {
                    iprint("    __idList.add(%1$s);%n", pm.getFieldName());
                }
                iprint("    __list.add(%1$s);%n", pm.getFieldName());
                iprint("    __map.put(\"%1$s\", %2$s);%n", 
                        // @formatter:off
                        /* 1 */pm.getName(),
                        /* 2 */pm.getFieldName());
                        // @formatter:on
            }
        }
        iprint("    __idPropertyTypes = java.util.Collections.unmodifiableList(__idList);%n");
        iprint("    __entityPropertyTypes = java.util.Collections.unmodifiableList(__list);%n");
        iprint("    __entityPropertyTypeMap = java.util.Collections.unmodifiableMap(__map);%n");
        iprint("}%n");
        print("%n");
    }

    private void printMethods() {
        printGetNamingTypeMethod();
        printIsImmutableMethod();
        printGetNameMethod();
        printGetCatalogNameMethod();
        printGetSchemaNameMethod();
        printGetTableNameMethod();
        printIsQuoteRequiredMethod();
        printPreInsertMethod();
        printPreUpdateMethod();
        printPreDeleteMethod();
        printPostInsertMethod();
        printPostUpdateMethod();
        printPostDeleteMethod();
        printGetEntityPropertyTypesMethod();
        printGetEntityPropertyTypeMethod();
        printGetIdPropertyTypesMethod();
        printGetGeneratedIdPropertyTypeMethod();
        printGetVersionPropertyTypeMethod();
        printNewEntityMethod();
        printGetEntityClassMethod();
        printGetOriginalStatesMethod();
        printSaveCurrentStatesMethod();
        printGetSingletonInternalMethod();
        printNewInstanceMethod();
    }

    private void printGetNamingTypeMethod() {
        iprint("@Override%n");
        iprint("public %1$s getNamingType() {%n", NamingType.class.getName());
        iprint("    return __namingType;%n");
        iprint("}%n");
        print("%n");
    }

    private void printIsImmutableMethod() {
        iprint("@Override%n");
        iprint("public boolean isImmutable() {%n");
        iprint("    return __immutable;%n");
        iprint("}%n");
        print("%n");
    }

    private void printGetNameMethod() {
        iprint("@Override%n");
        iprint("public String getName() {%n");
        iprint("    return __name;%n");
        iprint("}%n");
        print("%n");
    }

    private void printGetCatalogNameMethod() {
        iprint("@Override%n");
        iprint("public String getCatalogName() {%n");
        iprint("    return __catalogName;%n");
        iprint("}%n");
        print("%n");
    }

    private void printGetSchemaNameMethod() {
        iprint("@Override%n");
        iprint("public String getSchemaName() {%n");
        iprint("    return __schemaName;%n");
        iprint("}%n");
        print("%n");
    }

    private void printGetTableNameMethod() {
        iprint("@Override%n");
        iprint("public String getTableName(%1$s<%2$s, String, String> namingFunction) {%n",
                // @formatter:off
                /* 1 */BiFunction.class.getName(),
                /* 2 */NamingType.class.getName());
                // @formatter:on
        iprint("    if (__tableName.isEmpty()) {%n");
        iprint("        return namingFunction.apply(__namingType, __name);%n");
        iprint("    }%n");
        iprint("    return __tableName;%n");
        iprint("}%n");
        print("%n");
    }

    private void printIsQuoteRequiredMethod() {
        iprint("@Override%n");
        iprint("public boolean isQuoteRequired() {%n");
        iprint("    return __isQuoteRequired;%n");
        iprint("}%n");
        print("%n");
    }

    private void printPreInsertMethod() {
        iprint("@SuppressWarnings({\"rawtypes\", \"unchecked\"})%n");
        iprint("@Override%n");
        iprint("public void preInsert(%1$s entity, %2$s<%1$s> context) {%n",
                // @formatter:off
                /* 1 */entityMeta.getEntityTypeName(),
                /* 2 */PreInsertContext.class.getName());
                // @formatter:on
        printDeclareListener();
        iprint("    __listener.preInsert(entity, context);%n");
        iprint("}%n");
        print("%n");
    }

    private void printPreUpdateMethod() {
        iprint("@SuppressWarnings({\"rawtypes\", \"unchecked\"})%n");
        iprint("@Override%n");
        iprint("public void preUpdate(%1$s entity, %2$s<%1$s> context) {%n",
                // @formatter:off
                /* 1 */entityMeta.getEntityTypeName(),
                /* 2 */PreUpdateContext.class.getName());
                // @formatter:on
        printDeclareListener();
        iprint("    __listener.preUpdate(entity, context);%n");
        iprint("}%n");
        print("%n");
    }

    private void printPreDeleteMethod() {
        iprint("@SuppressWarnings({\"rawtypes\", \"unchecked\"})%n");
        iprint("@Override%n");
        iprint("public void preDelete(%1$s entity, %2$s<%1$s> context) {%n",
                // @formatter:off
                /* 1 */entityMeta.getEntityTypeName(),
                /* 2 */PreDeleteContext.class.getName());
                // @formatter:on
        printDeclareListener();
        iprint("    __listener.preDelete(entity, context);%n");
        iprint("}%n");
        print("%n");
    }

    private void printPostInsertMethod() {
        iprint("@SuppressWarnings({\"rawtypes\", \"unchecked\"})%n");
        iprint("@Override%n");
        iprint("public void postInsert(%1$s entity, %2$s<%1$s> context) {%n",
                // @formatter:off
                /* 1 */entityMeta.getEntityTypeName(),
                /* 2 */PostInsertContext.class.getName());
                // @formatter:on
        printDeclareListener();
        iprint("    __listener.postInsert(entity, context);%n");
        iprint("}%n");
        print("%n");
    }

    private void printPostUpdateMethod() {
        iprint("@SuppressWarnings({\"rawtypes\", \"unchecked\"})%n");
        iprint("@Override%n");
        iprint("public void postUpdate(%1$s entity, %2$s<%1$s> context) {%n",
                // @formatter:off
                /* 1 */entityMeta.getEntityTypeName(), 
                /* 2 */PostUpdateContext.class.getName());
                // @formatter:on
        printDeclareListener();
        iprint("    __listener.postUpdate(entity, context);%n");
        iprint("}%n");
        print("%n");
    }

    private void printPostDeleteMethod() {
        iprint("@SuppressWarnings({\"rawtypes\", \"unchecked\"})%n");
        iprint("@Override%n");
        iprint("public void postDelete(%1$s entity, %2$s<%1$s> context) {%n",
                // @formatter:off
                /* 1 */entityMeta.getEntityTypeName(),
                /* 2 */PostDeleteContext.class.getName());
                // @formatter:on
        printDeclareListener();
        iprint("    __listener.postDelete(entity, context);%n");
        iprint("}%n");
        print("%n");
    }

    private void printGetEntityPropertyTypesMethod() {
        iprint("@Override%n");
        iprint("public java.util.List<%1$s<%2$s, ?>> getEntityPropertyTypes() {%n",
                // @formatter:off
                /* 1 */EntityPropertyType.class.getName(), 
                /* 2 */entityMeta.getEntityTypeName());
                // @formatter:on
        iprint("    return __entityPropertyTypes;%n");
        iprint("}%n");
        print("%n");
    }

    private void printGetEntityPropertyTypeMethod() {
        iprint("@Override%n");
        iprint("public %1$s<%2$s, ?> getEntityPropertyType(String __name) {%n",
                // @formatter:off
                /* 1 */EntityPropertyType.class.getName(),
                /* 2 */entityMeta.getEntityTypeName());
                // @formatter:on
        iprint("    return __entityPropertyTypeMap.get(__name);%n");
        iprint("}%n");
        print("%n");
    }

    private void printGetIdPropertyTypesMethod() {
        iprint("@Override%n");
        iprint("public java.util.List<%1$s<%2$s, ?>> getIdPropertyTypes() {%n",
                // @formatter:off
                /* 1 */EntityPropertyType.class.getName(),
                /* 2 */entityMeta.getEntityTypeName());
                // @formatter:on
        iprint("    return __idPropertyTypes;%n");
        iprint("}%n");
        print("%n");
    }

    private void printGetGeneratedIdPropertyTypeMethod() {
        String idName = "null";
        if (entityMeta.hasGeneratedIdPropertyMeta()) {
            EntityPropertyMeta pm = entityMeta.getGeneratedIdPropertyMeta();
            idName = pm.getFieldName();
        }
        iprint("@Override%n");
        iprint("public %1$s<%2$s, ?, ?> getGeneratedIdPropertyType() {%n",
                // @formatter:off
                /* 1 */GeneratedIdPropertyType.class.getName(),
                /* 2 */entityMeta.getEntityTypeName());
                // @formatter:on
        iprint("    return %1$s;%n", idName);
        iprint("}%n");
        print("%n");
    }

    private void printGetVersionPropertyTypeMethod() {
        String versionName = "null";
        if (entityMeta.hasVersionPropertyMeta()) {
            EntityPropertyMeta pm = entityMeta.getVersionPropertyMeta();
            versionName = pm.getFieldName();
        }
        iprint("@Override%n");
        iprint("public %1$s<%2$s, ?, ?> getVersionPropertyType() {%n",
                // @formatter:off
                /* 1 */VersionPropertyType.class.getName(),
                /* 2 */entityMeta.getEntityTypeName());
                // @formatter:on
        iprint("    return %1$s;%n", versionName);
        iprint("}%n");
        print("%n");
    }

    private void printNewEntityMethod() {
        if (hasGenericTypeProperty()) {
            iprint("@SuppressWarnings(\"unchecked\")%n");
        }
        iprint("@Override%n");
        iprint("public %1$s newEntity(%2$s<String, %3$s<%1$s, ?>> __args) {%n",
                // @formatter:off
                /* 1 */entityMeta.getEntityTypeName(),
                /* 2 */Map.class.getName(),
                /* 3 */Property.class.getName());
                // @formatter:on
        if (entityMeta.isAbstract()) {
            iprint("    return null;%n");
        } else {
            if (entityMeta.isImmutable()) {
                iprint("    return new %1$s(%n", entityMeta.getEntityTypeName());
                for (Iterator<EntityPropertyMeta> it = entityMeta.getAllPropertyMetas()
                        .iterator(); it.hasNext();) {
                    EntityPropertyMeta propertyMeta = it.next();
                    if (propertyMeta.isEmbedded()) {
                        iprint("        %1$s.getSingletonInternal().newEmbeddable(\"%2$s\", __args)",
                                // @formatter:off
                                /* 1 */propertyMeta.getEmbeddableMetaTypeName(),
                                /* 2 */propertyMeta.getName());
                                // @formatter:on
                    } else {
                        iprint("        (%1$s)(__args.get(\"%2$s\") != null ? __args.get(\"%2$s\").get() : null)",
                                // @formatter:off
                                /* 1 */ctx.getTypes().boxIfPrimitive(propertyMeta.getType()),
                                /* 2 */propertyMeta.getName());
                                // @formatter:on
                    }
                    if (it.hasNext()) {
                        print(",%n");
                    }
                }
                print(");%n");
            } else {
                iprint("    %1$s entity = new %1$s();%n", entityMeta.getEntityTypeName());
                for (EntityPropertyMeta propertyMeta : entityMeta.getAllPropertyMetas()) {
                    if (propertyMeta.isEmbedded()) {
                        iprint("    %1$s.save(entity, %2$s.getSingletonInternal().newEmbeddable(\"%3$s\", __args));%n",
                                // @formatter:off
                                /* 1 */propertyMeta.getFieldName(),
                                /* 2 */propertyMeta.getEmbeddableMetaTypeName(),
                                /* 3 */propertyMeta.getName());
                                // @formatter:off
                    } else {
                        iprint("    if (__args.get(\"%1$s\") != null) __args.get(\"%1$s\").save(entity);%n",
                                // @formatter:off
                                /* 1 */propertyMeta.getName());
                                // @formatter:on
                    }
                }
                iprint("    return entity;%n");
            }
        }
        iprint("}%n");
        print("%n");
    }

    private boolean hasGenericTypeProperty() {
        if (entityMeta.isImmutable()) {
            for (EntityPropertyMeta propertyMeta : entityMeta.getAllPropertyMetas()) {
                TypeElement element = ctx.getTypes().toTypeElement(propertyMeta.getType());
                if (element != null) {
                    if (!element.getTypeParameters().isEmpty()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private void printGetEntityClassMethod() {
        iprint("@Override%n");
        iprint("public Class<%1$s> getEntityClass() {%n", entityMeta.getEntityTypeName());
        iprint("    return %1$s.class;%n", entityMeta.getEntityTypeName());
        iprint("}%n");
        print("%n");
    }

    private void printGetOriginalStatesMethod() {
        iprint("@Override%n");
        iprint("public %1$s getOriginalStates(%1$s __entity) {%n", entityMeta.getEntityTypeName());
        if (!entityMeta.isAbstract() && entityMeta.hasOriginalStatesMeta()) {
            iprint("    return __originalStatesAccessor.get(__entity);%n");
        } else {
            iprint("    return null;%n");
        }
        iprint("}%n");
        print("%n");
    }

    private void printSaveCurrentStatesMethod() {
        iprint("@Override%n");
        iprint("public void saveCurrentStates(%1$s __entity) {%n", entityMeta.getEntityTypeName());
        if (!entityMeta.isAbstract() && entityMeta.hasOriginalStatesMeta()) {
            iprint("    %1$s __currentStates = new %1$s();%n", entityMeta.getEntityTypeName());
            for (EntityPropertyMeta pm : entityMeta.getAllPropertyMetas()) {
                iprint("    %1$s.copy(__currentStates, __entity);%n", pm.getFieldName());
            }
            iprint("    __originalStatesAccessor.set(__entity, __currentStates);%n");
        }
        iprint("}%n");
        print("%n");
    }

    private void printGetSingletonInternalMethod() {
        iprint("/**%n");
        iprint(" * @return the singleton%n");
        iprint(" */%n");
        iprint("public static %1$s getSingletonInternal() {%n", simpleName);
        iprint("    return __singleton;%n");
        iprint("}%n");
        print("%n");
    }

    private void printNewInstanceMethod() {
        iprint("/**%n");
        iprint(" * @return the new instance%n");
        iprint(" */%n");
        iprint("public static %1$s newInstance() {%n", simpleName);
        iprint("    return new %1$s();%n", simpleName);
        iprint("}%n");
        print("%n");
    }

    private void printListenerHolder() {
        iprint("private static class ListenerHolder {%n");
        if (entityMeta.hasGenericListener()) {
            iprint("    private static %1$s<%2$s> listener = new %1$s<>();%n",
                    // @formatter:off
                    /* 1 */entityMeta.getListenerElement().getQualifiedName(),
                    /* 2 */entityMeta.getEntityTypeName());
                    // @formatter:on
        } else {
            iprint("    private static %1$s listener = new %1$s();%n",
                    entityMeta.getListenerElement().getQualifiedName());
        }
        iprint("}%n");
        print("%n");
    }

    private void printDeclareListener() {
        iprint("    Class __listenerClass = %1$s.class;%n",
                entityMeta.getListenerElement().getQualifiedName());
        iprint("    %1$s __listener = context.getConfig().getEntityListenerProvider().get(__listenerClass, __listenerSupplier);%n",
                entityMeta.getListenerElement().getQualifiedName());
    }

    private class IdGeneratorGenerator implements IdGeneratorMetaVisitor<Void, Void> {

        @Override
        public Void visistIdentityIdGeneratorMeta(IdentityIdGeneratorMeta m, Void p) {
            iprint("private final %1$s __idGenerator = new %1$s();%n", m.getIdGeneratorClassName());
            return null;
        }

        @Override
        public Void visistSequenceIdGeneratorMeta(SequenceIdGeneratorMeta m, Void p) {
            iprint("private final %1$s __idGenerator = new %1$s();%n", m.getIdGeneratorClassName());
            iprint("{%n");
            iprint("    __idGenerator.setQualifiedSequenceName(\"%1$s\");%n",
                    m.getQualifiedSequenceName());
            iprint("    __idGenerator.setInitialValue(%1$s);%n", m.getInitialValue());
            iprint("    __idGenerator.setAllocationSize(%1$s);%n", m.getAllocationSize());
            iprint("    __idGenerator.initialize();%n");
            iprint("}%n");
            return null;
        }

        @Override
        public Void visistTableIdGeneratorMeta(TableIdGeneratorMeta m, Void p) {
            iprint("private final %1$s __idGenerator = new %1$s();%n", m.getIdGeneratorClassName());
            iprint("{%n");
            iprint("    __idGenerator.setQualifiedTableName(\"%1$s\");%n",
                    m.getQualifiedTableName());
            iprint("    __idGenerator.setInitialValue(%1$s);%n", m.getInitialValue());
            iprint("    __idGenerator.setAllocationSize(%1$s);%n", m.getAllocationSize());
            iprint("    __idGenerator.setPkColumnName(\"%1$s\");%n", m.getPkColumnName());
            iprint("    __idGenerator.setPkColumnValue(\"%1$s\");%n", m.getPkColumnValue());
            iprint("    __idGenerator.setValueColumnName(\"%1$s\");%n", m.getValueColumnName());
            iprint("    __idGenerator.initialize();%n");
            iprint("}%n");
            return null;
        }
    }

    private class EntityPropertyCtTypeVisitor
            extends SimpleCtTypeVisitor<Void, Void, RuntimeException> {

        private BasicCtType basicCtType;

        private HolderCtType holderCtType;

        @Override
        protected Void defaultAction(CtType ctType, Void p) throws RuntimeException {
            assertNotNull(basicCtType);
            return null;
        }

        @Override
        public Void visitOptionalCtType(OptionalCtType ctType, Void p) throws RuntimeException {
            return ctType.getElementCtType().accept(this, p);
        }

        @Override
        public Void visitOptionalIntCtType(OptionalIntCtType ctType, Void p)
                throws RuntimeException {
            return ctType.getElementCtType().accept(this, p);
        }

        @Override
        public Void visitOptionalLongCtType(OptionalLongCtType ctType, Void p)
                throws RuntimeException {
            return ctType.getElementCtType().accept(this, p);
        }

        @Override
        public Void visitOptionalDoubleCtType(OptionalDoubleCtType ctType, Void p)
                throws RuntimeException {
            return ctType.getElementCtType().accept(this, p);
        }

        @Override
        public Void visitBasicCtType(BasicCtType basicCtType, Void p) throws RuntimeException {
            this.basicCtType = basicCtType;
            return defaultAction(basicCtType, p);
        }

        @Override
        public Void visitHolderCtType(HolderCtType holderCtType, Void p) throws RuntimeException {
            this.holderCtType = holderCtType;
            return visitBasicCtType(holderCtType.getBasicCtType(), p);
        }
    }

}
