/*
 * Copyright Doma Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.seasar.doma.internal.apt.generator;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.util.Iterator;
import java.util.Map;
import java.util.function.BiFunction;
import javax.lang.model.element.TypeElement;
import org.seasar.doma.EntityTypeImplementation;
import org.seasar.doma.internal.ClassName;
import org.seasar.doma.internal.apt.AptIllegalStateException;
import org.seasar.doma.internal.apt.RoundContext;
import org.seasar.doma.internal.apt.meta.entity.AssociationPropertyMeta;
import org.seasar.doma.internal.apt.meta.entity.EmbeddedMeta;
import org.seasar.doma.internal.apt.meta.entity.EntityFieldMeta;
import org.seasar.doma.internal.apt.meta.entity.EntityMeta;
import org.seasar.doma.internal.apt.meta.entity.EntityPropertyMeta;
import org.seasar.doma.internal.apt.meta.entity.OriginalStatesMeta;
import org.seasar.doma.internal.apt.meta.id.IdGeneratorMeta;
import org.seasar.doma.internal.apt.meta.id.IdGeneratorMetaVisitor;
import org.seasar.doma.internal.apt.meta.id.IdentityIdGeneratorMeta;
import org.seasar.doma.internal.apt.meta.id.SequenceIdGeneratorMeta;
import org.seasar.doma.internal.apt.meta.id.TableIdGeneratorMeta;
import org.seasar.doma.internal.jdbc.entity.NullEntityListenerSuppliers;
import org.seasar.doma.jdbc.entity.AbstractEntityType;
import org.seasar.doma.jdbc.entity.AssociationPropertyType;
import org.seasar.doma.jdbc.entity.DefaultAssociationPropertyType;
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
import org.seasar.doma.jdbc.entity.TenantIdPropertyType;
import org.seasar.doma.jdbc.entity.VersionPropertyType;

public class EntityTypeGenerator extends AbstractGenerator {

  private final EntityMeta entityMeta;

  public EntityTypeGenerator(
      RoundContext ctx, ClassName className, Printer printer, EntityMeta entityMeta) {
    super(ctx, className, printer);
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
    printEntityTypeImplementation();
    iprint(
        "public final class %1$s extends %2$s<%3$s> {%n",
        /* 1 */ simpleName, /* 2 */ AbstractEntityType.class, /* 3 */ entityMeta.getType());
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

  private void printEntityTypeImplementation() {
    iprint("@%1$s%n", EntityTypeImplementation.class);
  }

  private void printFields() {
    printSingletonField();
    printOriginalStatesAccessorField();
    printNamingTypeField();
    printIdGeneratorField();
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
    printEmbeddedPropertyTypeMapField();
    printAssociationPropertyTypesField();
  }

  private void printSingletonField() {
    iprint("private static final %1$s __singleton = new %1$s();%n", simpleName);
    print("%n");
  }

  private void printOriginalStatesAccessorField() {
    if (!entityMeta.isAbstract() && entityMeta.hasOriginalStatesMeta()) {
      OriginalStatesMeta osm = entityMeta.getOriginalStatesMeta();
      iprint(
          "private static final %1$s<%2$s> __originalStatesAccessor = new %1$s<>(%3$s.class, \"%4$s\");%n",
          /* 1 */ OriginalStatesAccessor.class,
          /* 2 */ osm.getTypeElement(),
          /* 3 */ osm.getFieldEnclosingElement(),
          /* 4 */ osm.getFieldElement());
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

  private void printListenerSupplierField() {
    if (entityMeta.isGenericEntityListener()) {
      iprint(
          "private final java.util.function.Supplier<%1$s<%2$s>> __listenerSupplier;%n",
          entityMeta.getEntityListenerElement(), entityMeta.getType());
    } else {
      iprint(
          "private final java.util.function.Supplier<%1$s> __listenerSupplier;%n",
          entityMeta.getEntityListenerElement());
    }
    print("%n");
  }

  private void printNamingTypeField() {
    NamingType namingType = entityMeta.getNamingType();
    if (namingType == null) {
      iprint("private final %1$s __namingType = null;%n", NamingType.class);
    } else {
      iprint("private final %1$s __namingType = %1$s.%2$s;%n", NamingType.class, namingType.name());
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
    iprint(
        "private final java.util.List<%1$s<%2$s, ?>> __idPropertyTypes;%n",
        EntityPropertyType.class, entityMeta.getType());
    print("%n");
  }

  private void printEntityPropertyTypeMapField() {
    iprint(
        "private final java.util.Map<String, %1$s<%2$s, ?>> __entityPropertyTypeMap;%n",
        EntityPropertyType.class, entityMeta.getType());
    print("%n");
  }

  private void printEmbeddedPropertyTypeMapField() {
    if (!entityMeta.hasEmbeddedFields()) {
      iprint("@SuppressWarnings(\"unused\")%n");
    }
    iprint(
        "private final java.util.Map<String, %1$s<%2$s, ?>> __embeddedPropertyTypeMap;%n",
        EmbeddedPropertyType.class, entityMeta.getType());
    print("%n");
  }

  private void printEntityPropertyTypesField() {
    iprint(
        "private final java.util.List<%1$s<%2$s, ?>> __entityPropertyTypes;%n",
        EntityPropertyType.class, entityMeta.getType());
    print("%n");
  }

  private void printAssociationPropertyTypesField() {
    if (entityMeta.getAssociationPropertyMetas().isEmpty()) {
      return;
    }
    iprint(
        "private final java.util.List<%1$s> __associationPropertyTypes;%n",
        AssociationPropertyType.class);
    print("%n");
  }

  private void printConstructor() {
    iprint("private %1$s() {%n", simpleName);
    if (entityMeta.isNullEntityListener()) {
      iprint("    __listenerSupplier = %1$s.of();%n", NullEntityListenerSuppliers.class);
    } else {
      if (entityMeta.isGenericEntityListener()) {
        iprint(
            "    __listenerSupplier = new java.util.function.Supplier<%1$s<%2$s>>() { "
                + "@Override public %1$s<%2$s> get() { return ListenerHolder.listener; } };%n",
            entityMeta.getEntityListenerElement(), entityMeta.getType());
      } else {
        iprint(
            "    __listenerSupplier = new java.util.function.Supplier<%1$s>() { "
                + "@Override public %1$s get() { return ListenerHolder.listener; } };%n",
            entityMeta.getEntityListenerElement());
      }
    }
    iprint("    __immutable = %1$s;%n", entityMeta.isImmutable());
    iprint("    __name = \"%1$s\";%n", entityMeta.getEntityName());
    iprint("    __catalogName = \"%1$s\";%n", entityMeta.getCatalogName());
    iprint("    __schemaName = \"%1$s\";%n", entityMeta.getSchemaName());
    iprint("    __tableName = \"%1$s\";%n", entityMeta.getTableName());
    iprint("    __isQuoteRequired = %1$s;%n", entityMeta.isQuoteRequired());
    iprint(
        "    java.util.List<%1$s<%2$s, ?>> __idList = new java.util.ArrayList<>();%n",
        EntityPropertyType.class, entityMeta.getType());
    iprint(
        "    java.util.List<%1$s<%2$s, ?>> __list = new java.util.ArrayList<>(%3$s);%n",
        /* 1 */ EntityPropertyType.class,
        /* 2 */ entityMeta.getType(),
        /* 3 */ entityMeta.getAllFieldMetas().size());
    iprint(
        "    java.util.Map<String, %1$s<%2$s, ?>> __map = new java.util.LinkedHashMap<>(%3$s);%n",
        /* 1 */ EntityPropertyType.class,
        /* 2 */ entityMeta.getType(),
        /* 3 */ entityMeta.getAllFieldMetas().size());
    iprint(
        "    java.util.Map<String, %1$s<%2$s, ?>> __embeddedMap = new java.util.LinkedHashMap<>(%3$s);%n",
        /* 1 */ EmbeddedPropertyType.class,
        /* 2 */ entityMeta.getType(),
        /* 3 */ entityMeta.getAllFieldMetas().size());
    iprint("    initializeMaps(__map, __embeddedMap);%n");
    iprint("    initializeIdList(__map, __idList);%n");
    iprint("    initializeList(__map, __list);%n");
    iprint("    __idPropertyTypes = java.util.Collections.unmodifiableList(__idList);%n");
    iprint("    __entityPropertyTypes = java.util.Collections.unmodifiableList(__list);%n");
    iprint("    __entityPropertyTypeMap = java.util.Collections.unmodifiableMap(__map);%n");
    iprint(
        "    __embeddedPropertyTypeMap = java.util.Collections.unmodifiableMap(__embeddedMap);%n");
    if (!entityMeta.getAssociationPropertyMetas().isEmpty()) {
      iprint(
          "    java.util.List<%1$s> __associationList = new java.util.ArrayList<>(%2$s);%n",
          /* 1 */ AssociationPropertyType.class,
          /* 2 */ entityMeta.getAssociationPropertyMetas().size());
      iprint("    initializeAssociationList(__associationList);%n");
      iprint(
          "    __associationPropertyTypes = java.util.Collections.unmodifiableList(__associationList);%n");
    }
    iprint("}%n");
    print("%n");
  }

  private void printMethods() {
    printInitializeMapsMethod();
    printInitializeIdListMethod();
    printInitializeListMethod();
    printInitializeAssociationListMethod();
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
    printGetAssociationPropertyTypesMethod();
    printGetEntityPropertyTypesMethod();
    printGetEntityPropertyTypeMethod();
    printGetIdPropertyTypesMethod();
    printGetGeneratedIdPropertyTypeMethod();
    printGetVersionPropertyTypeMethod();
    printGetTenantIdPropertyTypeMethod();
    printNewEntityMethod();
    printGetEntityClassMethod();
    printGetOriginalStatesMethod();
    printSaveCurrentStatesMethod();
    printGetSingletonInternalMethod();
    printNewInstanceMethod();
  }

  private void printInitializeMapsMethod() {
    iprint(
        "private void initializeMaps(java.util.Map<String, %1$s<%2$s, ?>> __map, java.util.Map<String, %3$s<%2$s, ?>> __embeddedMap) {%n",
        /* 1 */ EntityPropertyType.class,
        /* 2 */ entityMeta.getType(),
        /* 3 */ EmbeddedPropertyType.class);
    indent();
    for (EntityFieldMeta fieldMeta : entityMeta.getAllFieldMetas()) {
      EntityTypePropertyGenerator propertyGenerator =
          new EntityTypePropertyGenerator(ctx, className, printer, entityMeta, fieldMeta);
      if (fieldMeta instanceof EmbeddedMeta) {
        iprint("__embeddedMap.put(\"%1$s\", ", fieldMeta.getName());
        propertyGenerator.generate();
        print(");%n");
        iprint(
            "__map.putAll(__embeddedMap.get(\"%1$s\").getEmbeddablePropertyTypeMap());%n",
            fieldMeta.getName());
      } else if (fieldMeta instanceof EntityPropertyMeta) {
        iprint("__map.put(\"%1$s\", ", fieldMeta.getName());
        propertyGenerator.generate();
        print(");%n");
      }
    }
    unindent();
    iprint("}%n");
    print("%n");
  }

  private void printInitializeIdListMethod() {
    iprint(
        "private void initializeIdList(java.util.Map<String, %1$s<%2$s, ?>> __map, java.util.List<%1$s<%2$s, ?>> __idList) {%n",
        EntityPropertyType.class, entityMeta.getType());
    indent();
    for (EntityPropertyMeta pm : entityMeta.getIdPropertyMetas()) {
      iprint("__idList.add(__map.get(\"%1$s\"));%n", pm.getName());
    }
    unindent();
    iprint("}%n");
    print("%n");
  }

  private void printInitializeListMethod() {
    iprint(
        "private void initializeList(java.util.Map<String, %1$s<%2$s, ?>> __map, java.util.List<%1$s<%2$s, ?>> __list) {%n",
        EntityPropertyType.class, entityMeta.getType());
    indent();
    iprint("__list.addAll(__map.values());%n");
    unindent();
    iprint("}%n");
    print("%n");
  }

  private void printInitializeAssociationListMethod() {
    if (entityMeta.getAssociationPropertyMetas().isEmpty()) {
      return;
    }
    iprint(
        "private void initializeAssociationList(java.util.List<%1$s> __associationList) {%n",
        AssociationPropertyType.class, entityMeta.getType());
    indent();
    for (AssociationPropertyMeta m : entityMeta.getAssociationPropertyMetas()) {
      iprint(
          "__associationList.add(new %1$s(\"%2$s\"));%n",
          DefaultAssociationPropertyType.class, m.getName());
    }
    unindent();
    iprint("}%n");
    print("%n");
  }

  private void printGetNamingTypeMethod() {
    iprint("@Override%n");
    iprint("public %1$s getNamingType() {%n", NamingType.class);
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
    iprint(
        "public String getTableName(%1$s<%2$s, String, String> namingFunction) {%n",
        BiFunction.class, NamingType.class);
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
    iprint(
        "public void preInsert(%1$s entity, %2$s<%1$s> context) {%n",
        entityMeta.getType(), PreInsertContext.class);
    printDeclareListener();
    iprint("    __listener.preInsert(entity, context);%n");
    iprint("}%n");
    print("%n");
  }

  private void printPreUpdateMethod() {
    iprint("@SuppressWarnings({\"rawtypes\", \"unchecked\"})%n");
    iprint("@Override%n");
    iprint(
        "public void preUpdate(%1$s entity, %2$s<%1$s> context) {%n",
        entityMeta.getType(), PreUpdateContext.class);
    printDeclareListener();
    iprint("    __listener.preUpdate(entity, context);%n");
    iprint("}%n");
    print("%n");
  }

  private void printPreDeleteMethod() {
    iprint("@SuppressWarnings({\"rawtypes\", \"unchecked\"})%n");
    iprint("@Override%n");
    iprint(
        "public void preDelete(%1$s entity, %2$s<%1$s> context) {%n",
        entityMeta.getType(), PreDeleteContext.class);
    printDeclareListener();
    iprint("    __listener.preDelete(entity, context);%n");
    iprint("}%n");
    print("%n");
  }

  private void printPostInsertMethod() {
    iprint("@SuppressWarnings({\"rawtypes\", \"unchecked\"})%n");
    iprint("@Override%n");
    iprint(
        "public void postInsert(%1$s entity, %2$s<%1$s> context) {%n",
        entityMeta.getType(), PostInsertContext.class);
    printDeclareListener();
    iprint("    __listener.postInsert(entity, context);%n");
    iprint("}%n");
    print("%n");
  }

  private void printPostUpdateMethod() {
    iprint("@SuppressWarnings({\"rawtypes\", \"unchecked\"})%n");
    iprint("@Override%n");
    iprint(
        "public void postUpdate(%1$s entity, %2$s<%1$s> context) {%n",
        entityMeta.getType(), PostUpdateContext.class);
    printDeclareListener();
    iprint("    __listener.postUpdate(entity, context);%n");
    iprint("}%n");
    print("%n");
  }

  private void printPostDeleteMethod() {
    iprint("@SuppressWarnings({\"rawtypes\", \"unchecked\"})%n");
    iprint("@Override%n");
    iprint(
        "public void postDelete(%1$s entity, %2$s<%1$s> context) {%n",
        entityMeta.getType(), PostDeleteContext.class);
    printDeclareListener();
    iprint("    __listener.postDelete(entity, context);%n");
    iprint("}%n");
    print("%n");
  }

  private void printGetAssociationPropertyTypesMethod() {
    if (entityMeta.getAssociationPropertyMetas().isEmpty()) {
      return;
    }
    iprint("@Override%n");
    iprint(
        "public java.util.List<%1$s> getAssociationPropertyTypes() {%n",
        AssociationPropertyType.class);
    iprint("    return __associationPropertyTypes;%n");
    iprint("}%n");
    print("%n");
  }

  private void printGetEntityPropertyTypesMethod() {
    iprint("@Override%n");
    iprint(
        "public java.util.List<%1$s<%2$s, ?>> getEntityPropertyTypes() {%n",
        EntityPropertyType.class, entityMeta.getType());
    iprint("    return __entityPropertyTypes;%n");
    iprint("}%n");
    print("%n");
  }

  private void printGetEntityPropertyTypeMethod() {
    iprint("@Override%n");
    iprint(
        "public %1$s<%2$s, ?> getEntityPropertyType(String __name) {%n",
        EntityPropertyType.class, entityMeta.getType());
    iprint("    return __entityPropertyTypeMap.get(__name);%n");
    iprint("}%n");
    print("%n");
  }

  private void printGetIdPropertyTypesMethod() {
    iprint("@Override%n");
    iprint(
        "public java.util.List<%1$s<%2$s, ?>> getIdPropertyTypes() {%n",
        EntityPropertyType.class, entityMeta.getType());
    iprint("    return __idPropertyTypes;%n");
    iprint("}%n");
    print("%n");
  }

  private void printGetGeneratedIdPropertyTypeMethod() {
    String idName = "null";
    if (entityMeta.hasGeneratedIdPropertyMeta()) {
      EntityPropertyMeta pm = entityMeta.getGeneratedIdPropertyMeta();
      idName = pm.getName();
    }
    iprint("@Override%n");
    iprint(
        "public %1$s<%2$s, ?, ?> getGeneratedIdPropertyType() {%n",
        GeneratedIdPropertyType.class, entityMeta.getType());
    iprint(
        "    return (%1$s<%2$s, ?, ?>)__entityPropertyTypeMap.get(\"%3$s\");%n",
        GeneratedIdPropertyType.class, entityMeta.getType(), idName);
    iprint("}%n");
    print("%n");
  }

  private void printGetVersionPropertyTypeMethod() {
    String versionName = "null";
    if (entityMeta.hasVersionPropertyMeta()) {
      EntityPropertyMeta pm = entityMeta.getVersionPropertyMeta();
      versionName = pm.getName();
    }
    iprint("@Override%n");
    iprint(
        "public %1$s<%2$s, ?, ?> getVersionPropertyType() {%n",
        VersionPropertyType.class, entityMeta.getType());
    iprint(
        "    return (%1$s<%2$s, ?, ?>)__entityPropertyTypeMap.get(\"%3$s\");%n",
        VersionPropertyType.class, entityMeta.getType(), versionName);
    iprint("}%n");
    print("%n");
  }

  private void printGetTenantIdPropertyTypeMethod() {
    String tenantIdName = "null";
    if (entityMeta.hasTenantIdPropertyMeta()) {
      EntityPropertyMeta pm = entityMeta.getTenantIdPropertyMeta();
      tenantIdName = pm.getName();
    }
    iprint("@Override%n");
    iprint(
        "public %1$s<%2$s, ?, ?> getTenantIdPropertyType() {%n",
        TenantIdPropertyType.class, entityMeta.getType());
    iprint(
        "    return (%1$s<%2$s, ?, ?>)__entityPropertyTypeMap.get(\"%3$s\");%n",
        TenantIdPropertyType.class, entityMeta.getType(), tenantIdName);
    iprint("}%n");
    print("%n");
  }

  private void printNewEntityMethod() {
    if (hasGenericTypeProperty() || (!entityMeta.isImmutable() && entityMeta.hasEmbeddedFields())) {
      iprint("@SuppressWarnings(\"unchecked\")%n");
    }
    iprint("@Override%n");
    iprint(
        "public %1$s newEntity(%2$s<String, %3$s<%1$s, ?>> __args) {%n",
        entityMeta.getType(), Map.class, Property.class);
    if (entityMeta.isAbstract()) {
      iprint("    return null;%n");
    } else {
      if (entityMeta.isImmutable()) {
        iprint("    return new %1$s(%n", entityMeta.getType());
        for (Iterator<EntityFieldMeta> it = entityMeta.getAllFieldMetas().iterator();
            it.hasNext(); ) {
          EntityFieldMeta fieldMeta = it.next();
          if (fieldMeta instanceof EmbeddedMeta embeddedMeta) {
            if (embeddedMeta.optional()) {
              iprint(
                  "        java.util.Optional.ofNullable(%1$s.newEmbeddable(\"%2$s\", __args, true))",
                  embeddedMeta.embeddableCtType().getTypeCode(), fieldMeta.getName());
            } else {
              iprint(
                  "        %1$s.newEmbeddable(\"%2$s\", __args, false)",
                  embeddedMeta.embeddableCtType().getTypeCode(), fieldMeta.getName());
            }
          } else if (fieldMeta instanceof EntityPropertyMeta propertyMeta) {
            iprint(
                "        (%1$s)(__args.get(\"%2$s\") != null ? __args.get(\"%2$s\").get() : null)",
                propertyMeta.getBoxedType(), propertyMeta.getName());
          } else {
            throw new AptIllegalStateException(fieldMeta.getName());
          }
          if (it.hasNext()) {
            print(",%n");
          }
        }
        print(");%n");
      } else {
        iprint("    %1$s entity = new %1$s();%n", entityMeta.getType());
        for (EntityFieldMeta fieldMeta : entityMeta.getAllFieldMetas()) {
          if (fieldMeta instanceof EmbeddedMeta embeddedMeta) {
            iprint(
                "    ((%4$s<%5$s, %6$s>)__embeddedPropertyTypeMap.get(\"%1$s\")).save(entity, %2$s.newEmbeddable(\"%3$s\", __args, %7$s));%n",
                /* 1 */ fieldMeta.getName(),
                /* 2 */ embeddedMeta.embeddableCtType().getTypeCode(),
                /* 3 */ fieldMeta.getName(),
                /* 4 */ EmbeddedPropertyType.class,
                /* 5 */ entityMeta.getType(),
                /* 6 */ embeddedMeta.embeddableCtType().getType(),
                /* 7 */ embeddedMeta.optional());

          } else if (fieldMeta instanceof EntityPropertyMeta propertyMeta) {
            iprint(
                "    if (__args.get(\"%1$s\") != null) __args.get(\"%1$s\").save(entity);%n",
                propertyMeta.getName());
          } else {
            throw new AptIllegalStateException(fieldMeta.getName());
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
      for (EntityFieldMeta fieldMeta : entityMeta.getAllFieldMetas()) {
        TypeElement element = ctx.getMoreTypes().toTypeElement(fieldMeta.getCtType().getType());
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
    iprint("public Class<%1$s> getEntityClass() {%n", entityMeta.getType());
    iprint("    return %1$s.class;%n", entityMeta.getType());
    iprint("}%n");
    print("%n");
  }

  private void printGetOriginalStatesMethod() {
    iprint("@Override%n");
    iprint("public %1$s getOriginalStates(%1$s __entity) {%n", entityMeta.getType());
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
    iprint("public void saveCurrentStates(%1$s __entity) {%n", entityMeta.getType());
    if (!entityMeta.isAbstract() && entityMeta.hasOriginalStatesMeta()) {
      iprint("    %1$s __currentStates = new %1$s();%n", entityMeta.getType());
      for (EntityFieldMeta fieldMeta : entityMeta.getAllFieldMetas()) {
        iprint(
            "    (__entityPropertyTypeMap.get(\"%1$s\")).copy(__currentStates, __entity);%n",
            fieldMeta.getName());
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
    if (entityMeta.isNullEntityListener()) {
      return;
    }
    iprint("private static class ListenerHolder {%n");
    if (entityMeta.isGenericEntityListener()) {
      iprint(
          "    private static %1$s<%2$s> listener = new %1$s<>();%n",
          entityMeta.getEntityListenerElement(), entityMeta.getType());
    } else {
      iprint(
          "    private static %1$s listener = new %1$s();%n",
          entityMeta.getEntityListenerElement());
    }
    iprint("}%n");
    print("%n");
  }

  private void printDeclareListener() {
    iprint("    Class __listenerClass = %1$s.class;%n", entityMeta.getEntityListenerElement());
    iprint(
        "    %1$s __listener = context.getConfig().getEntityListenerProvider().get(__listenerClass, __listenerSupplier);%n",
        entityMeta.getEntityListenerElement());
  }

  protected class IdGeneratorGenerator implements IdGeneratorMetaVisitor<Void, Void> {

    @Override
    public Void visitIdentityIdGeneratorMeta(IdentityIdGeneratorMeta m, Void p) {
      iprint("private final %1$s __idGenerator = new %1$s();%n", m.getIdGeneratorClassName());
      return null;
    }

    @Override
    public Void visitSequenceIdGeneratorMeta(SequenceIdGeneratorMeta m, Void p) {
      iprint("private final %1$s __idGenerator = new %1$s();%n", m.getIdGeneratorClassName());
      iprint("{%n");
      iprint(
          "    __idGenerator.setQualifiedSequenceName(\"%1$s\");%n", m.getQualifiedSequenceName());
      iprint("    __idGenerator.setInitialValue(%1$s);%n", m.getInitialValue());
      iprint("    __idGenerator.setAllocationSize(%1$s);%n", m.getAllocationSize());
      iprint("    __idGenerator.initialize();%n");
      iprint("}%n");
      return null;
    }

    @Override
    public Void visitTableIdGeneratorMeta(TableIdGeneratorMeta m, Void p) {
      iprint("private final %1$s __idGenerator = new %1$s();%n", m.getIdGeneratorClassName());
      iprint("{%n");
      iprint("    __idGenerator.setQualifiedTableName(\"%1$s\");%n", m.getQualifiedTableName());
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
}
