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

import org.seasar.doma.internal.Constants;
import org.seasar.doma.internal.apt.cttype.BasicCtType;
import org.seasar.doma.internal.apt.cttype.CtType;
import org.seasar.doma.internal.apt.cttype.HolderCtType;
import org.seasar.doma.internal.apt.cttype.OptionalCtType;
import org.seasar.doma.internal.apt.cttype.OptionalDoubleCtType;
import org.seasar.doma.internal.apt.cttype.OptionalIntCtType;
import org.seasar.doma.internal.apt.cttype.OptionalLongCtType;
import org.seasar.doma.internal.apt.cttype.SimpleCtTypeVisitor;
import org.seasar.doma.internal.apt.cttype.WrapperCtType;
import org.seasar.doma.internal.apt.meta.EntityMeta;
import org.seasar.doma.internal.apt.meta.EntityPropertyMeta;
import org.seasar.doma.internal.apt.meta.IdGeneratorMeta;
import org.seasar.doma.internal.apt.meta.IdGeneratorMetaVisitor;
import org.seasar.doma.internal.apt.meta.IdentityIdGeneratorMeta;
import org.seasar.doma.internal.apt.meta.OriginalStatesMeta;
import org.seasar.doma.internal.apt.meta.SequenceIdGeneratorMeta;
import org.seasar.doma.internal.apt.meta.TableIdGeneratorMeta;
import org.seasar.doma.jdbc.Naming;
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

    protected static final String NULL = "null";

    protected final EntityMeta entityMeta;

    public EntityTypeGenerator(Context ctx,
            TypeElement entityElement, EntityMeta entityMeta)
            throws IOException {
        super(ctx, entityElement, null, null, Constants.METATYPE_PREFIX, "");
        assertNotNull(entityMeta);
        this.entityMeta = entityMeta;
    }

    @Override
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
        iprint("public final class %1$s extends %2$s<%3$s> {%n",
        /* 1 */simpleName, /* 2 */AbstractEntityType.class.getName(),
        /* 3 */entityMeta.getEntityTypeName());
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

    protected void printFields() {
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

    protected void printSingletonField() {
        iprint("private static final %1$s __singleton = new %1$s();%n",
                simpleName);
        print("%n");
    }

    protected void printOriginalStatesAccessorField() {
        if (!entityMeta.isAbstract() && entityMeta.hasOriginalStatesMeta()) {
            OriginalStatesMeta osm = entityMeta.getOriginalStatesMeta();
            iprint("private static final %1$s<%2$s> __originalStatesAccessor = new %1$s<>(%3$s.class, \"%4$s\");%n",
            /* 1 */OriginalStatesAccessor.class.getName(),
            /* 2 */osm.getTypeElement().getQualifiedName(),
            /* 3 */osm.getFieldEnclosingElement().getQualifiedName(),
            /* 4 */osm.getFieldElement().getSimpleName());
            print("%n");
        }
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
        for (EntityPropertyMeta pm : entityMeta.getAllPropertyMetas()) {
            iprint("/** the %1$s */%n", pm.getName());
            if (pm.isEmbedded()) {
                iprint("public final %1$s<%2$s, %3$s> %4$s = new %1$s<>(\"%5$s\", %2$s.class, %6$s.getSingletonInternal().getEmbeddablePropertyTypes(\"%7$s\", %2$s.class, __namingType));%n",
                /* 1 */EmbeddedPropertyType.class.getName(),
                /* 2 */entityMeta.getEntityTypeName(),
                /* 3 */pm.getTypeName(), /* 4 */pm.getFieldName(),
                /* 5 */pm.getName(),
                /* 6 */pm.getEmbeddableMetaTypeName(),
                /* 7 */pm.getName());
            } else {
                EntityPropertyCtTypeVisitor visitor = new EntityPropertyCtTypeVisitor();
                pm.getCtType().accept(visitor, null);
                BasicCtType basicCtType = visitor.basicCtType;
                WrapperCtType wrapperCtType = visitor.wrapperCtType;
                HolderCtType holderCtType = visitor.holderCtType;

                String newWrapperExpr;
                if (basicCtType.isEnum()) {
                    newWrapperExpr = String.format("new %s(%s.class)",
                            wrapperCtType.getTypeName(),
                            basicCtType.getBoxedTypeName());
                } else {
                    newWrapperExpr = String.format("new %s()",
                            wrapperCtType.getTypeName());
                }
                String holderType = "null";
                String holderTypeName = "Object";
                if (holderCtType != null) {
                    holderType = holderCtType.getInstantiationCommand();
                    holderTypeName = holderCtType.getTypeName();
                }
                if (pm.isId()) {
                    if (pm.getIdGeneratorMeta() != null) {
                        iprint("public final %1$s<%11$s, %2$s, %3$s, %14$s> %12$s = new %1$s<>(%6$s.class, %13$s.class, %3$s.class, () -> %7$s, %10$s, %8$s, \"%4$s\", \"%5$s\", __namingType, %15$s, __idGenerator);%n",
                        /* 1 */GeneratedIdPropertyType.class.getName(),
                        /* 2 */entityMeta.getEntityTypeName(),
                        /* 3 */basicCtType.getBoxedTypeName(),
                        /* 4 */pm.getName(), /* 5 */pm.getColumnName(),
                        /* 6 */entityMeta.getEntityTypeName(),
                        /* 7 */newWrapperExpr, /* 8 */holderType,
                        /* 9 */pm.getBoxedTypeName(),
                        /* 10 */NULL,
                        /* 11 */Object.class.getName(),
                        /* 12 */pm.getFieldName(),
                        /* 13 */pm.getBoxedClassName(),
                        /* 14 */holderTypeName,
                        /* 15 */pm.isColumnQuoteRequired());
                    } else {
                        iprint("public final %1$s<%11$s, %2$s, %3$s, %14$s> %12$s = new %1$s<>(%6$s.class, %13$s.class, %3$s.class, () -> %7$s, %10$s, %8$s, \"%4$s\", \"%5$s\", __namingType, %15$s);%n",
                        /* 1 */AssignedIdPropertyType.class.getName(),
                        /* 2 */entityMeta.getEntityTypeName(),
                        /* 3 */basicCtType.getBoxedTypeName(),
                        /* 4 */pm.getName(), /* 5 */pm.getColumnName(),
                        /* 6 */entityMeta.getEntityTypeName(),
                        /* 7 */newWrapperExpr, /* 8 */holderType,
                        /* 9 */pm.getBoxedTypeName(),
                        /* 10 */NULL,
                        /* 11 */Object.class.getName(),
                        /* 12 */pm.getFieldName(),
                        /* 13 */pm.getBoxedClassName(),
                        /* 14 */holderTypeName,
                        /* 15 */pm.isColumnQuoteRequired());
                    }
                } else if (pm.isVersion()) {
                    iprint("public final %1$s<%11$s, %2$s, %3$s, %14$s> %12$s = new %1$s<>(%6$s.class,  %13$s.class, %3$s.class, () -> %7$s, %10$s, %8$s, \"%4$s\", \"%5$s\", __namingType, %15$s);%n",
                    /* 1 */VersionPropertyType.class.getName(),
                    /* 2 */entityMeta.getEntityTypeName(),
                    /* 3 */basicCtType.getBoxedTypeName(),
                    /* 4 */pm.getName(), /* 5 */pm.getColumnName(),
                    /* 6 */entityMeta.getEntityTypeName(),
                    /* 7 */newWrapperExpr, /* 8 */holderType,
                    /* 9 */pm.getBoxedTypeName(),
                    /* 10 */NULL,
                    /* 11 */Object.class.getName(),
                    /* 12 */pm.getFieldName(),
                    /* 13 */pm.getBoxedClassName(),
                    /* 14 */holderTypeName,
                    /* 15 */pm.isColumnQuoteRequired());
                } else {
                    iprint("public final %1$s<%13$s, %2$s, %3$s, %16$s> %14$s = new %1$s<>(%8$s.class, %15$s.class, %3$s.class, () -> %9$s, %12$s, %10$s, \"%4$s\", \"%5$s\", __namingType, %6$s, %7$s, %17$s);%n",
                    /* 1 */DefaultPropertyType.class.getName(),
                    /* 2 */entityMeta.getEntityTypeName(),
                    /* 3 */basicCtType.getBoxedTypeName(),
                    /* 4 */pm.getName(), /* 5 */pm.getColumnName(),
                    /* 6 */pm.isColumnInsertable(),
                    /* 7 */pm.isColumnUpdatable(),
                    /* 8 */entityMeta.getEntityTypeName(),
                    /* 9 */newWrapperExpr, /* 10 */holderType,
                    /* 11 */pm.getBoxedTypeName(),
                    /* 12 */NULL,
                    /* 13 */Object.class.getName(),
                    /* 14 */pm.getFieldName(),
                    /* 15 */pm.getBoxedClassName(),
                    /* 16 */holderTypeName,
                    /* 17 */pm.isColumnQuoteRequired());
                }
            }
            print("%n");
        }
    }

    protected String getNamingTypeExpression(boolean defined) {
        if (defined) {
            return NamingType.class.getName() + "." + NamingType.NONE;
        }
        return "__namingType";
    }

    protected void printListenerSupplierField() {
        if (entityMeta.isGenericEntityListener()) {
            iprint("private final java.util.function.Supplier<%1$s<%2$s>> __listenerSupplier;%n",
                    entityMeta.getEntityListenerElement().getQualifiedName(),
                    entityMeta.getEntityTypeName());
        } else {
            iprint("private final java.util.function.Supplier<%1$s> __listenerSupplier;%n",
                    entityMeta.getEntityListenerElement().getQualifiedName());
        }
        print("%n");
    }

    protected void printNamingTypeField() {
        NamingType namingType = entityMeta.getNamingType();
        if (namingType == null) {
            iprint("private final %1$s __namingType = null;%n",
                    NamingType.class.getName());
        } else {
            iprint("private final %1$s __namingType = %1$s.%2$s;%n",
                    NamingType.class.getName(), namingType.name());
        }
        print("%n");
    }

    protected void printImmutableField() {
        iprint("private final boolean __immutable;%n");
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

    protected void printIsQuoteRequiredField() {
        iprint("private final boolean __isQuoteRequired;%n");
        print("%n");
    }

    protected void printNameField() {
        iprint("private final String __name;%n");
        print("%n");
    }

    protected void printIdPropertyTypesField() {
        iprint("private final java.util.List<%1$s<%2$s, ?>> __idPropertyTypes;%n",
                EntityPropertyType.class.getName(),
                entityMeta.getEntityTypeName());
        print("%n");
    }

    protected void printEntityPropertyTypesField() {
        iprint("private final java.util.List<%1$s<%2$s, ?>> __entityPropertyTypes;%n",
                EntityPropertyType.class.getName(),
                entityMeta.getEntityTypeName());
        print("%n");
    }

    protected void printEntityPropertyTypeMapField() {
        iprint("private final java.util.Map<String, %1$s<%2$s, ?>> __entityPropertyTypeMap;%n",
                EntityPropertyType.class.getName(),
                entityMeta.getEntityTypeName());
        print("%n");
    }

    protected void printConstructor() {
        iprint("private %1$s() {%n", simpleName);
        iprint("    __listenerSupplier = () -> ListenerHolder.listener;%n");
        iprint("    __immutable = %1$s;%n", entityMeta.isImmutable());
        iprint("    __name = \"%1$s\";%n", entityMeta.getEntityName());
        iprint("    __catalogName = \"%1$s\";%n", entityMeta.getCatalogName());
        iprint("    __schemaName = \"%1$s\";%n", entityMeta.getSchemaName());
        iprint("    __tableName = \"%1$s\";%n", entityMeta.getTableName());
        iprint("    __isQuoteRequired = %1$s;%n", entityMeta.isQuoteRequired());
        iprint("    java.util.List<%1$s<%2$s, ?>> __idList = new java.util.ArrayList<>();%n",
                EntityPropertyType.class.getName(),
                entityMeta.getEntityTypeName());
        iprint("    java.util.List<%1$s<%2$s, ?>> __list = new java.util.ArrayList<>(%3$s);%n",
        /* 1 */EntityPropertyType.class.getName(),
        /* 2 */entityMeta.getEntityTypeName(),
        /* 3 */entityMeta.getAllPropertyMetas().size());
        iprint("    java.util.Map<String, %1$s<%2$s, ?>> __map = new java.util.HashMap<>(%3$s);%n",
        /* 1 */EntityPropertyType.class.getName(),
        /* 2 */entityMeta.getEntityTypeName(),
        /* 3 */entityMeta.getAllPropertyMetas().size());
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
                iprint("    __map.put(\"%1$s\", %2$s);%n", pm.getName(),
                        pm.getFieldName());
            }
        }
        iprint("    __idPropertyTypes = java.util.Collections.unmodifiableList(__idList);%n");
        iprint("    __entityPropertyTypes = java.util.Collections.unmodifiableList(__list);%n");
        iprint("    __entityPropertyTypeMap = java.util.Collections.unmodifiableMap(__map);%n");
        iprint("}%n");
        print("%n");
    }

    protected void printMethods() {
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

    protected void printGetNamingTypeMethod() {
        iprint("@Override%n");
        iprint("public %1$s getNamingType() {%n", NamingType.class.getName());
        iprint("    return __namingType;%n");
        iprint("}%n");
        print("%n");
    }

    protected void printIsImmutableMethod() {
        iprint("@Override%n");
        iprint("public boolean isImmutable() {%n");
        iprint("    return __immutable;%n");
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
        iprint("    return getTableName(%1$s.DEFAULT::apply);%n",
                Naming.class.getName());
        iprint("}%n");
        print("%n");
        iprint("@Override%n");
        iprint("public String getTableName(%1$s<%2$s, String, String> namingFunction) {%n",
                BiFunction.class.getName(), NamingType.class.getName());
        iprint("    if (__tableName.isEmpty()) {%n");
        iprint("        return namingFunction.apply(__namingType, __name);%n");
        iprint("    }%n");
        iprint("    return __tableName;%n");
        iprint("}%n");
        print("%n");
    }

    protected void printIsQuoteRequiredMethod() {
        iprint("@Override%n");
        iprint("public boolean isQuoteRequired() {%n");
        iprint("    return __isQuoteRequired;%n");
        iprint("}%n");
        print("%n");
    }

    protected void printPreInsertMethod() {
        iprint("@SuppressWarnings({\"rawtypes\", \"unchecked\"})%n");
        iprint("@Override%n");
        iprint("public void preInsert(%1$s entity, %2$s<%1$s> context) {%n",
                entityMeta.getEntityTypeName(),
                PreInsertContext.class.getName());
        printDeclareListener();
        iprint("    __listener.preInsert(entity, context);%n");
        iprint("}%n");
        print("%n");
    }

    protected void printPreUpdateMethod() {
        iprint("@SuppressWarnings({\"rawtypes\", \"unchecked\"})%n");
        iprint("@Override%n");
        iprint("public void preUpdate(%1$s entity, %2$s<%1$s> context) {%n",
                entityMeta.getEntityTypeName(),
                PreUpdateContext.class.getName());
        printDeclareListener();
        iprint("    __listener.preUpdate(entity, context);%n");
        iprint("}%n");
        print("%n");
    }

    protected void printPreDeleteMethod() {
        iprint("@SuppressWarnings({\"rawtypes\", \"unchecked\"})%n");
        iprint("@Override%n");
        iprint("public void preDelete(%1$s entity, %2$s<%1$s> context) {%n",
                entityMeta.getEntityTypeName(),
                PreDeleteContext.class.getName());
        printDeclareListener();
        iprint("    __listener.preDelete(entity, context);%n");
        iprint("}%n");
        print("%n");
    }

    protected void printPostInsertMethod() {
        iprint("@SuppressWarnings({\"rawtypes\", \"unchecked\"})%n");
        iprint("@Override%n");
        iprint("public void postInsert(%1$s entity, %2$s<%1$s> context) {%n",
                entityMeta.getEntityTypeName(),
                PostInsertContext.class.getName());
        printDeclareListener();
        iprint("    __listener.postInsert(entity, context);%n");
        iprint("}%n");
        print("%n");
    }

    protected void printPostUpdateMethod() {
        iprint("@SuppressWarnings({\"rawtypes\", \"unchecked\"})%n");
        iprint("@Override%n");
        iprint("public void postUpdate(%1$s entity, %2$s<%1$s> context) {%n",
                entityMeta.getEntityTypeName(),
                PostUpdateContext.class.getName());
        printDeclareListener();
        iprint("    __listener.postUpdate(entity, context);%n");
        iprint("}%n");
        print("%n");
    }

    protected void printPostDeleteMethod() {
        iprint("@SuppressWarnings({\"rawtypes\", \"unchecked\"})%n");
        iprint("@Override%n");
        iprint("public void postDelete(%1$s entity, %2$s<%1$s> context) {%n",
                entityMeta.getEntityTypeName(),
                PostDeleteContext.class.getName());
        printDeclareListener();
        iprint("    __listener.postDelete(entity, context);%n");
        iprint("}%n");
        print("%n");
    }

    protected void printGetEntityPropertyTypesMethod() {
        iprint("@Override%n");
        iprint("public java.util.List<%1$s<%2$s, ?>> getEntityPropertyTypes() {%n",
                EntityPropertyType.class.getName(),
                entityMeta.getEntityTypeName());
        iprint("    return __entityPropertyTypes;%n");
        iprint("}%n");
        print("%n");
    }

    protected void printGetEntityPropertyTypeMethod() {
        iprint("@Override%n");
        iprint("public %1$s<%2$s, ?> getEntityPropertyType(String __name) {%n",
                EntityPropertyType.class.getName(),
                entityMeta.getEntityTypeName());
        iprint("    return __entityPropertyTypeMap.get(__name);%n");
        iprint("}%n");
        print("%n");
    }

    protected void printGetIdPropertyTypesMethod() {
        iprint("@Override%n");
        iprint("public java.util.List<%1$s<%2$s, ?>> getIdPropertyTypes() {%n",
                EntityPropertyType.class.getName(),
                entityMeta.getEntityTypeName());
        iprint("    return __idPropertyTypes;%n");
        iprint("}%n");
        print("%n");
    }

    protected void printGetGeneratedIdPropertyTypeMethod() {
        String idName = "null";
        if (entityMeta.hasGeneratedIdPropertyMeta()) {
            EntityPropertyMeta pm = entityMeta.getGeneratedIdPropertyMeta();
            idName = pm.getFieldName();
        }
        iprint("@Override%n");
        iprint("public %1$s<%3$s, %2$s, ?, ?> getGeneratedIdPropertyType() {%n",
                GeneratedIdPropertyType.class.getName(),
                entityMeta.getEntityTypeName(), Object.class.getName());
        iprint("    return %1$s;%n", idName);
        iprint("}%n");
        print("%n");
    }

    protected void printGetVersionPropertyTypeMethod() {
        String versionName = "null";
        if (entityMeta.hasVersionPropertyMeta()) {
            EntityPropertyMeta pm = entityMeta.getVersionPropertyMeta();
            versionName = pm.getFieldName();
        }
        iprint("@Override%n");
        iprint("public %1$s<%3$s, %2$s, ?, ?> getVersionPropertyType() {%n",
                VersionPropertyType.class.getName(),
                entityMeta.getEntityTypeName(), Object.class.getName());
        iprint("    return %1$s;%n", versionName);
        iprint("}%n");
        print("%n");
    }

    protected void printNewEntityMethod() {
        if (hasGenericTypeProperty()) {
            iprint("@SuppressWarnings(\"unchecked\")%n");
        }
        iprint("@Override%n");
        iprint("public %1$s newEntity(%2$s<String, %3$s<%1$s, ?>> __args) {%n",
                entityMeta.getEntityTypeName(), Map.class.getName(),
                Property.class.getName());
        if (entityMeta.isAbstract()) {
            iprint("    return null;%n");
        } else {
            if (entityMeta.isImmutable()) {
                iprint("    return new %1$s(%n", entityMeta.getEntityTypeName());
                for (Iterator<EntityPropertyMeta> it = entityMeta
                        .getAllPropertyMetas().iterator(); it.hasNext();) {
                    EntityPropertyMeta propertyMeta = it.next();
                    if (propertyMeta.isEmbedded()) {
                        iprint("        %1$s.getSingletonInternal().newEmbeddable(\"%2$s\", __args)",
                                propertyMeta.getEmbeddableMetaTypeName(),
                                propertyMeta.getName());
                    } else {
                        iprint("        (%1$s)(__args.get(\"%2$s\") != null ? __args.get(\"%2$s\").get() : null)",
                                ctx.getTypes()
                                        .boxIfPrimitive(propertyMeta.getType()),
                                propertyMeta.getName());
                    }
                    if (it.hasNext()) {
                        print(",%n");
                    }
                }
                print(");%n");
            } else {
                iprint("    %1$s entity = new %1$s();%n",
                        entityMeta.getEntityTypeName());
                for (EntityPropertyMeta propertyMeta : entityMeta
                        .getAllPropertyMetas()) {
                    if (propertyMeta.isEmbedded()) {
                        iprint("    %1$s.save(entity, %2$s.getSingletonInternal().newEmbeddable(\"%3$s\", __args));%n",
                                propertyMeta.getFieldName(),
                                propertyMeta.getEmbeddableMetaTypeName(),
                                propertyMeta.getName());
                    } else {
                        iprint("    if (__args.get(\"%1$s\") != null) __args.get(\"%1$s\").save(entity);%n",
                                propertyMeta.getName());
                    }
                }
                iprint("    return entity;%n");
            }
        }
        iprint("}%n");
        print("%n");
    }

    protected boolean hasGenericTypeProperty() {
        if (entityMeta.isImmutable()) {
            for (EntityPropertyMeta propertyMeta : entityMeta
                    .getAllPropertyMetas()) {
                TypeElement element = ctx.getTypes()
                        .toTypeElement(propertyMeta.getType());
                if (element != null) {
                    if (!element.getTypeParameters().isEmpty()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    protected void printGetEntityClassMethod() {
        iprint("@Override%n");
        iprint("public Class<%1$s> getEntityClass() {%n",
                entityMeta.getEntityTypeName());
        iprint("    return %1$s.class;%n", entityMeta.getEntityTypeName());
        iprint("}%n");
        print("%n");
    }

    protected void printGetOriginalStatesMethod() {
        iprint("@Override%n");
        iprint("public %1$s getOriginalStates(%1$s __entity) {%n",
                entityMeta.getEntityTypeName());
        if (!entityMeta.isAbstract() && entityMeta.hasOriginalStatesMeta()) {
            iprint("    return __originalStatesAccessor.get(__entity);%n");
        } else {
            iprint("    return null;%n");
        }
        iprint("}%n");
        print("%n");
    }

    protected void printSaveCurrentStatesMethod() {
        iprint("@Override%n");
        iprint("public void saveCurrentStates(%1$s __entity) {%n",
                entityMeta.getEntityTypeName());
        if (!entityMeta.isAbstract() && entityMeta.hasOriginalStatesMeta()) {
            iprint("    %1$s __currentStates = new %1$s();%n",
                    entityMeta.getEntityTypeName());
            for (EntityPropertyMeta pm : entityMeta.getAllPropertyMetas()) {
                iprint("    %1$s.copy(__currentStates, __entity);%n",
                        pm.getFieldName());
            }
            iprint("    __originalStatesAccessor.set(__entity, __currentStates);%n");
        }
        iprint("}%n");
        print("%n");
    }

    protected void printGetSingletonInternalMethod() {
        iprint("/**%n");
        iprint(" * @return the singleton%n");
        iprint(" */%n");
        iprint("public static %1$s getSingletonInternal() {%n", simpleName);
        iprint("    return __singleton;%n");
        iprint("}%n");
        print("%n");
    }

    protected void printNewInstanceMethod() {
        iprint("/**%n");
        iprint(" * @return the new instance%n");
        iprint(" */%n");
        iprint("public static %1$s newInstance() {%n", simpleName);
        iprint("    return new %1$s();%n", simpleName);
        iprint("}%n");
        print("%n");
    }

    protected void printListenerHolder() {
        iprint("private static class ListenerHolder {%n");
        if (entityMeta.isGenericEntityListener()) {
            iprint("    private static %1$s<%2$s> listener = new %1$s<>();%n",
                    entityMeta.getEntityListenerElement().getQualifiedName(),
                    entityMeta.getEntityTypeName());
        } else {
            iprint("    private static %1$s listener = new %1$s();%n",
                    entityMeta.getEntityListenerElement().getQualifiedName());
        }
        iprint("}%n");
        print("%n");
    }

    private void printDeclareListener() {
        iprint("    Class __listenerClass = %1$s.class;%n", entityMeta
                .getEntityListenerElement().getQualifiedName());
        iprint("    %1$s __listener = context.getConfig().getEntityListenerProvider().get(__listenerClass, __listenerSupplier);%n",
                entityMeta.getEntityListenerElement().getQualifiedName());
    }

    protected class IdGeneratorGenerator implements
            IdGeneratorMetaVisitor<Void, Void> {

        @Override
        public Void visistIdentityIdGeneratorMeta(IdentityIdGeneratorMeta m,
                Void p) {
            iprint("private final %1$s __idGenerator = new %1$s();%n",
                    m.getIdGeneratorClassName());
            return null;
        }

        @Override
        public Void visistSequenceIdGeneratorMeta(SequenceIdGeneratorMeta m,
                Void p) {
            iprint("private final %1$s __idGenerator = new %1$s();%n",
                    m.getIdGeneratorClassName());
            iprint("{%n");
            iprint("    __idGenerator.setQualifiedSequenceName(\"%1$s\");%n",
                    m.getQualifiedSequenceName());
            iprint("    __idGenerator.setInitialValue(%1$s);%n",
                    m.getInitialValue());
            iprint("    __idGenerator.setAllocationSize(%1$s);%n",
                    m.getAllocationSize());
            iprint("    __idGenerator.initialize();%n");
            iprint("}%n");
            return null;
        }

        @Override
        public Void visistTableIdGeneratorMeta(TableIdGeneratorMeta m, Void p) {
            iprint("private final %1$s __idGenerator = new %1$s();%n",
                    m.getIdGeneratorClassName());
            iprint("{%n");
            iprint("    __idGenerator.setQualifiedTableName(\"%1$s\");%n",
                    m.getQualifiedTableName());
            iprint("    __idGenerator.setInitialValue(%1$s);%n",
                    m.getInitialValue());
            iprint("    __idGenerator.setAllocationSize(%1$s);%n",
                    m.getAllocationSize());
            iprint("    __idGenerator.setPkColumnName(\"%1$s\");%n",
                    m.getPkColumnName());
            iprint("    __idGenerator.setPkColumnValue(\"%1$s\");%n",
                    m.getPkColumnValue());
            iprint("    __idGenerator.setValueColumnName(\"%1$s\");%n",
                    m.getValueColumnName());
            iprint("    __idGenerator.initialize();%n");
            iprint("}%n");
            return null;
        }
    }

    protected class EntityPropertyCtTypeVisitor extends
            SimpleCtTypeVisitor<Void, Void, RuntimeException> {

        protected BasicCtType basicCtType;

        protected WrapperCtType wrapperCtType;

        protected HolderCtType holderCtType;

        @Override
        protected Void defaultAction(CtType ctType, Void p)
                throws RuntimeException {
            assertNotNull(basicCtType);
            assertNotNull(wrapperCtType);
            return null;
        }

        @Override
        public Void visitOptionalCtType(OptionalCtType ctType, Void p)
                throws RuntimeException {
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
        public Void visitOptionalDoubleCtType(OptionalDoubleCtType ctType,
                Void p) throws RuntimeException {
            return ctType.getElementCtType().accept(this, p);
        }

        @Override
        public Void visitBasicCtType(BasicCtType basicCtType, Void p)
                throws RuntimeException {
            this.basicCtType = basicCtType;
            this.wrapperCtType = basicCtType.getWrapperCtType();
            return defaultAction(basicCtType, p);
        }

        @Override
        public Void visitHolderCtType(HolderCtType holderCtType, Void p)
                throws RuntimeException {
            this.holderCtType = holderCtType;
            return visitBasicCtType(holderCtType.getBasicCtType(), p);
        }
    }

}
