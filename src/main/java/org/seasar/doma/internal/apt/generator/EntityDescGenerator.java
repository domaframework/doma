package org.seasar.doma.internal.apt.generator;

import static org.seasar.doma.internal.apt.generator.CodeHelper.embeddableDesc;
import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;
import static org.seasar.doma.internal.util.SupplierUtil.iife;

import java.util.Map;
import java.util.function.BiFunction;
import org.seasar.doma.internal.apt.Context;
import org.seasar.doma.internal.apt.codespec.CodeSpec;
import org.seasar.doma.internal.apt.meta.entity.EntityMeta;
import org.seasar.doma.internal.apt.meta.entity.EntityPropertyMeta;
import org.seasar.doma.internal.apt.meta.id.IdGeneratorMetaVisitor;
import org.seasar.doma.internal.apt.meta.id.IdentityIdGeneratorMeta;
import org.seasar.doma.internal.apt.meta.id.SequenceIdGeneratorMeta;
import org.seasar.doma.internal.apt.meta.id.TableIdGeneratorMeta;
import org.seasar.doma.jdbc.entity.AbstractEntityDesc;
import org.seasar.doma.jdbc.entity.AssignedIdPropertyDesc;
import org.seasar.doma.jdbc.entity.DefaultPropertyDesc;
import org.seasar.doma.jdbc.entity.EmbeddedPropertyDesc;
import org.seasar.doma.jdbc.entity.EntityPropertyDesc;
import org.seasar.doma.jdbc.entity.GeneratedIdPropertyDesc;
import org.seasar.doma.jdbc.entity.NamingType;
import org.seasar.doma.jdbc.entity.OriginalStatesAccessor;
import org.seasar.doma.jdbc.entity.PostDeleteContext;
import org.seasar.doma.jdbc.entity.PostInsertContext;
import org.seasar.doma.jdbc.entity.PostUpdateContext;
import org.seasar.doma.jdbc.entity.PreDeleteContext;
import org.seasar.doma.jdbc.entity.PreInsertContext;
import org.seasar.doma.jdbc.entity.PreUpdateContext;
import org.seasar.doma.jdbc.entity.Property;
import org.seasar.doma.jdbc.entity.VersionPropertyDesc;

public class EntityDescGenerator implements Generator {

  private final CodeSpec codeSpec;

  private final Printer printer;

  private final EntityMeta entityMeta;

  private final Context ctx;

  public EntityDescGenerator(
      CodeSpec codeSpec, Printer printer, EntityMeta entityMeta, Context ctx) {
    assertNotNull(codeSpec, printer, entityMeta, ctx);
    this.codeSpec = codeSpec;
    this.printer = printer;
    this.entityMeta = entityMeta;
    this.ctx = ctx;
  }

  @Override
  public void generate() {
    printClass();
  }

  private void printClass() {
    printer.printPackage();
    printer.iprint("/** */%n");
    printer.printGenerated();
    printer.iprint(
        "public final class %1$s extends %2$s<%3$s> {%n",
        /* 1 */ codeSpec.getSimpleName(),
        /* 2 */ AbstractEntityDesc.class.getName(),
        /* 3 */ entityMeta.getEntityTypeName());
    printer.print("%n");
    printer.indent();
    printer.printValidateVersionStaticInitializer();
    printFields();
    printConstructor();
    printMethods();
    printListenerHolder();
    printer.unindent();
    printer.iprint("}%n");
  }

  private void printFields() {
    printSingletonField();
    printOriginalStatesAccessorField();
    printNamingTypeField();
    printIdGeneratorField();
    printPropertyDescFields();
    printListenerSupplierField();
    printImmutableField();
    printCatalogNameField();
    printSchemaNameField();
    printTableNameField();
    printIsQuoteRequiredField();
    printNameField();
    printIdPropertyDescsField();
    printEntityPropertyDescsField();
    printEntityPropertyDescMapField();
  }

  private void printSingletonField() {
    printer.iprint(
        "private static final %1$s __singleton = new %1$s();%n", /* 1 */ codeSpec.getSimpleName());
    printer.print("%n");
  }

  private void printOriginalStatesAccessorField() {
    if (!entityMeta.isAbstract() && entityMeta.hasOriginalStatesMeta()) {
      var osm = entityMeta.getOriginalStatesMeta();
      printer.iprint(
          "private static final %1$s<%2$s> __originalStatesAccessor = new %1$s<>(%3$s.class, \"%4$s\");%n",
          /* 1 */ OriginalStatesAccessor.class.getName(),
          /* 2 */ osm.getTypeElement().getQualifiedName(),
          /* 3 */ osm.getFieldEnclosingElement().getQualifiedName(),
          /* 4 */ osm.getFieldElement().getSimpleName());
      printer.print("%n");
    }
  }

  private void printIdGeneratorField() {
    if (entityMeta.hasGeneratedIdPropertyMeta()) {
      var propertyMeta = entityMeta.getGeneratedIdPropertyMeta();
      var idGeneratorMeta = propertyMeta.getIdGeneratorMeta();
      idGeneratorMeta.accept(new IdGeneratorGenerator(), null);
      printer.print("%n");
    }
  }

  private void printPropertyDescFields() {
    for (var pm : entityMeta.getAllPropertyMetas()) {
      printer.iprint("/** the %1$s */%n", pm.getName());
      if (pm.isEmbedded()) {
        printer.iprint(
            "public final %1$s<%2$s, %3$s> %4$s = "
                + "new %1$s<>(\"%5$s\", %2$s.class, %6$s.getEmbeddablePropertyDescs"
                + "(\"%7$s\", %2$s.class, __namingType));%n",
            /* 1 */ EmbeddedPropertyDesc.class.getName(),
            /* 2 */ entityMeta.getEntityTypeName(),
            /* 3 */ pm.getTypeName(),
            /* 4 */ pm.getFieldName(),
            /* 5 */ pm.getName(),
            /* 6 */ embeddableDesc(pm.getEmbeddableCtType()),
            /* 7 */ pm.getName());
      } else {
        var args = createPropertyFormatArgs(pm);
        var className = args[0];
        if (GeneratedIdPropertyDesc.class.getName().equals(className)) {
          printer.iprint(
              "public final %1$s<%2$s, %3$s, %4$s> %5$s = "
                  + "new %1$s<>(%6$s, %7$s, %8$s, %9$s, __namingType, %10$s, __idGenerator);%n",
              args);
        } else if (AssignedIdPropertyDesc.class.getName().equals(className)) {
          printer.iprint(
              "public final %1$s<%2$s, %3$s, %4$s> %5$s = "
                  + "new %1$s<>(%6$s, %7$s, %8$s, %9$s, __namingType, %10$s);%n",
              args);
        } else if (VersionPropertyDesc.class.getName().equals(className)) {
          printer.iprint(
              "public final %1$s<%2$s, %3$s, %4$s> %5$s = "
                  + "new %1$s<>(%6$s, %7$s, %8$s, %9$s, __namingType, %10$s);%n",
              args);
        } else {
          printer.iprint(
              "public final %1$s<%2$s, %3$s, %4$s> %5$s = "
                  + "new %1$s<>(%6$s, %7$s, %8$s, %9$s, __namingType, %11$s, %12$s, %10$s);%n",
              args);
        }
      }
      printer.print("%n");
    }
  }

  private Object[] createPropertyFormatArgs(EntityPropertyMeta pm) {
    final var _1_propertyDescClass =
        iife(
            () -> {
              if (pm.isId()) {
                if (pm.getIdGeneratorMeta() != null) {
                  return GeneratedIdPropertyDesc.class.getName();
                } else {
                  return AssignedIdPropertyDesc.class.getName();
                }
              } else if (pm.isVersion()) {
                return VersionPropertyDesc.class.getName();
              } else {
                return DefaultPropertyDesc.class.getName();
              }
            });
    final var _2_ENTITY = entityMeta.getEntityTypeName();
    final var _3_BASIC = pm.getCtType().accept(new BasicTypeArgCodeBuilder(), null);
    final var _4_CONTAINER = pm.getCtType().accept(new ContainerTypeArgCodeBuilder(), false);
    final var _5_fieldName = pm.getFieldName();
    final var _6_entityClass = entityMeta.getEntityTypeName() + ".class";
    final var _7_scalarSupplier = pm.getCtType().accept(new ScalarSupplierCodeBuilder(), false);
    final var _8_name = "\"" + pm.getName() + "\"";
    final var _9_columnName = "\"" + pm.getColumnName() + "\"";
    final var _10_quoteRequired = pm.isColumnQuoteRequired();
    final var _11_insertable = pm.isColumnInsertable();
    final var _12_updatable = pm.isColumnUpdatable();

    return new Object[] {
      _1_propertyDescClass,
      _2_ENTITY,
      _3_BASIC,
      _4_CONTAINER,
      _5_fieldName,
      _6_entityClass,
      _7_scalarSupplier,
      _8_name,
      _9_columnName,
      _10_quoteRequired,
      _11_insertable,
      _12_updatable
    };
  }

  private void printListenerSupplierField() {
    if (entityMeta.hasGenericListener()) {
      printer.iprint(
          "private final java.util.function.Supplier<%1$s<%2$s>> __listenerSupplier;%n",
          /* 1 */ entityMeta.getListenerElement().getQualifiedName(),
          /* 2 */ entityMeta.getEntityTypeName());
    } else {
      printer.iprint(
          "private final java.util.function.Supplier<%1$s> __listenerSupplier;%n",
          /* 1 */ entityMeta.getListenerElement().getQualifiedName());
    }
    printer.print("%n");
  }

  private void printNamingTypeField() {
    var namingType = entityMeta.getNamingType();
    if (namingType == null) {
      printer.iprint(
          "private final %1$s __namingType = null;%n", /* 1 */ NamingType.class.getName());
    } else {
      printer.iprint(
          "private final %1$s __namingType = %1$s.%2$s;%n",
          /* 1 */ NamingType.class.getName(), /* 2 */ namingType.name());
    }
    printer.print("%n");
  }

  private void printImmutableField() {
    printer.iprint("private final boolean __immutable;%n");
    printer.print("%n");
  }

  private void printCatalogNameField() {
    printer.iprint("private final String __catalogName;%n");
    printer.print("%n");
  }

  private void printSchemaNameField() {
    printer.iprint("private final String __schemaName;%n");
    printer.print("%n");
  }

  private void printTableNameField() {
    printer.iprint("private final String __tableName;%n");
    printer.print("%n");
  }

  private void printIsQuoteRequiredField() {
    printer.iprint("private final boolean __isQuoteRequired;%n");
    printer.print("%n");
  }

  private void printNameField() {
    printer.iprint("private final String __name;%n");
    printer.print("%n");
  }

  private void printIdPropertyDescsField() {
    printer.iprint(
        "private final java.util.List<%1$s<%2$s, ?>> __idPropertyDescs;%n",
        /* 1 */ EntityPropertyDesc.class.getName(), /* 2 */ entityMeta.getEntityTypeName());
    printer.print("%n");
  }

  private void printEntityPropertyDescsField() {
    printer.iprint(
        "private final java.util.List<%1$s<%2$s, ?>> __entityPropertyDescs;%n",
        /* 1 */ EntityPropertyDesc.class.getName(), /* 2 */ entityMeta.getEntityTypeName());
    printer.print("%n");
  }

  private void printEntityPropertyDescMapField() {
    printer.iprint(
        "private final java.util.Map<String, %1$s<%2$s, ?>> __entityPropertyDescMap;%n",
        /* 1 */ EntityPropertyDesc.class.getName(), /* 2 */ entityMeta.getEntityTypeName());
    printer.print("%n");
  }

  private void printConstructor() {
    printer.iprint("private %1$s() {%n", codeSpec.getSimpleName());
    printer.iprint("    __listenerSupplier = () -> ListenerHolder.listener;%n");
    printer.iprint("    __immutable = %1$s;%n", entityMeta.isImmutable());
    printer.iprint("    __name = \"%1$s\";%n", entityMeta.getEntityName());
    printer.iprint("    __catalogName = \"%1$s\";%n", entityMeta.getCatalogName());
    printer.iprint("    __schemaName = \"%1$s\";%n", entityMeta.getSchemaName());
    printer.iprint("    __tableName = \"%1$s\";%n", entityMeta.getTableName());
    printer.iprint("    __isQuoteRequired = %1$s;%n", entityMeta.isQuoteRequired());
    printer.iprint(
        "    java.util.List<%1$s<%2$s, ?>> __idList = new java.util.ArrayList<>();%n",
        /* 1 */ EntityPropertyDesc.class.getName(), /* 2 */ entityMeta.getEntityTypeName());
    printer.iprint(
        "    java.util.List<%1$s<%2$s, ?>> __list = new java.util.ArrayList<>(%3$s);%n",
        /* 1 */ EntityPropertyDesc.class.getName(),
        /* 2 */ entityMeta.getEntityTypeName(),
        /* 3 */ entityMeta.getAllPropertyMetas().size());
    printer.iprint(
        "    java.util.Map<String, %1$s<%2$s, ?>> __map = new java.util.HashMap<>(%3$s);%n",
        /* 1 */ EntityPropertyDesc.class.getName(),
        /* 2 */ entityMeta.getEntityTypeName(),
        /* 3 */ entityMeta.getAllPropertyMetas().size());
    for (var pm : entityMeta.getAllPropertyMetas()) {
      if (pm.isEmbedded()) {
        printer.iprint(
            "    __list.addAll(%1$s.getEmbeddablePropertyDescs());%n", pm.getFieldName());
        printer.iprint(
            "    __map.putAll(%1$s.getEmbeddablePropertyDescMap());%n", pm.getFieldName());
      } else {
        if (pm.isId()) {
          printer.iprint("    __idList.add(%1$s);%n", pm.getFieldName());
        }
        printer.iprint("    __list.add(%1$s);%n", pm.getFieldName());
        printer.iprint(
            "    __map.put(\"%1$s\", %2$s);%n", /* 1 */ pm.getName(), /* 2 */ pm.getFieldName());
      }
    }
    printer.iprint("    __idPropertyDescs = java.util.Collections.unmodifiableList(__idList);%n");
    printer.iprint("    __entityPropertyDescs = java.util.Collections.unmodifiableList(__list);%n");
    printer.iprint("    __entityPropertyDescMap = java.util.Collections.unmodifiableMap(__map);%n");
    printer.iprint("}%n");
    printer.print("%n");
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
    printGetEntityPropertyDescsMethod();
    printGetEntityPropertyDescMethod();
    printGetIdPropertyDescsMethod();
    printGetGeneratedIdPropertyDescMethod();
    printGetVersionPropertyDescMethod();
    printNewEntityMethod();
    printGetEntityClassMethod();
    printGetOriginalStatesMethod();
    printSaveCurrentStatesMethod();
    printGetSingletonInternalMethod();
    printNewInstanceMethod();
  }

  private void printGetNamingTypeMethod() {
    printer.iprint("@Override%n");
    printer.iprint("public %1$s getNamingType() {%n", NamingType.class.getName());
    printer.iprint("    return __namingType;%n");
    printer.iprint("}%n");
    printer.print("%n");
  }

  private void printIsImmutableMethod() {
    printer.iprint("@Override%n");
    printer.iprint("public boolean isImmutable() {%n");
    printer.iprint("    return __immutable;%n");
    printer.iprint("}%n");
    printer.print("%n");
  }

  private void printGetNameMethod() {
    printer.iprint("@Override%n");
    printer.iprint("public String getName() {%n");
    printer.iprint("    return __name;%n");
    printer.iprint("}%n");
    printer.print("%n");
  }

  private void printGetCatalogNameMethod() {
    printer.iprint("@Override%n");
    printer.iprint("public String getCatalogName() {%n");
    printer.iprint("    return __catalogName;%n");
    printer.iprint("}%n");
    printer.print("%n");
  }

  private void printGetSchemaNameMethod() {
    printer.iprint("@Override%n");
    printer.iprint("public String getSchemaName() {%n");
    printer.iprint("    return __schemaName;%n");
    printer.iprint("}%n");
    printer.print("%n");
  }

  private void printGetTableNameMethod() {
    printer.iprint("@Override%n");
    printer.iprint(
        "public String getTableName(%1$s<%2$s, String, String> namingFunction) {%n",
        /* 1 */ BiFunction.class.getName(), /* 2 */ NamingType.class.getName());
    printer.iprint("    if (__tableName.isEmpty()) {%n");
    printer.iprint("        return namingFunction.apply(__namingType, __name);%n");
    printer.iprint("    }%n");
    printer.iprint("    return __tableName;%n");
    printer.iprint("}%n");
    printer.print("%n");
  }

  private void printIsQuoteRequiredMethod() {
    printer.iprint("@Override%n");
    printer.iprint("public boolean isQuoteRequired() {%n");
    printer.iprint("    return __isQuoteRequired;%n");
    printer.iprint("}%n");
    printer.print("%n");
  }

  private void printPreInsertMethod() {
    printer.iprint("@SuppressWarnings({\"rawtypes\", \"unchecked\"})%n");
    printer.iprint("@Override%n");
    printer.iprint(
        "public void preInsert(%1$s entity, %2$s<%1$s> context) {%n",
        /* 1 */ entityMeta.getEntityTypeName(), /* 2 */ PreInsertContext.class.getName());
    printDeclareListener();
    printer.iprint("    __listener.preInsert(entity, context);%n");
    printer.iprint("}%n");
    printer.print("%n");
  }

  private void printPreUpdateMethod() {
    printer.iprint("@SuppressWarnings({\"rawtypes\", \"unchecked\"})%n");
    printer.iprint("@Override%n");
    printer.iprint(
        "public void preUpdate(%1$s entity, %2$s<%1$s> context) {%n",
        /* 1 */ entityMeta.getEntityTypeName(), /* 2 */ PreUpdateContext.class.getName());
    printDeclareListener();
    printer.iprint("    __listener.preUpdate(entity, context);%n");
    printer.iprint("}%n");
    printer.print("%n");
  }

  private void printPreDeleteMethod() {
    printer.iprint("@SuppressWarnings({\"rawtypes\", \"unchecked\"})%n");
    printer.iprint("@Override%n");
    printer.iprint(
        "public void preDelete(%1$s entity, %2$s<%1$s> context) {%n",
        /* 1 */ entityMeta.getEntityTypeName(), /* 2 */ PreDeleteContext.class.getName());
    printDeclareListener();
    printer.iprint("    __listener.preDelete(entity, context);%n");
    printer.iprint("}%n");
    printer.print("%n");
  }

  private void printPostInsertMethod() {
    printer.iprint("@SuppressWarnings({\"rawtypes\", \"unchecked\"})%n");
    printer.iprint("@Override%n");
    printer.iprint(
        "public void postInsert(%1$s entity, %2$s<%1$s> context) {%n",
        /* 1 */ entityMeta.getEntityTypeName(), /* 2 */ PostInsertContext.class.getName());
    printDeclareListener();
    printer.iprint("    __listener.postInsert(entity, context);%n");
    printer.iprint("}%n");
    printer.print("%n");
  }

  private void printPostUpdateMethod() {
    printer.iprint("@SuppressWarnings({\"rawtypes\", \"unchecked\"})%n");
    printer.iprint("@Override%n");
    printer.iprint(
        "public void postUpdate(%1$s entity, %2$s<%1$s> context) {%n",
        /* 1 */ entityMeta.getEntityTypeName(), /* 2 */ PostUpdateContext.class.getName());
    printDeclareListener();
    printer.iprint("    __listener.postUpdate(entity, context);%n");
    printer.iprint("}%n");
    printer.print("%n");
  }

  private void printPostDeleteMethod() {
    printer.iprint("@SuppressWarnings({\"rawtypes\", \"unchecked\"})%n");
    printer.iprint("@Override%n");
    printer.iprint(
        "public void postDelete(%1$s entity, %2$s<%1$s> context) {%n",
        /* 1 */ entityMeta.getEntityTypeName(), /* 2 */ PostDeleteContext.class.getName());
    printDeclareListener();
    printer.iprint("    __listener.postDelete(entity, context);%n");
    printer.iprint("}%n");
    printer.print("%n");
  }

  private void printGetEntityPropertyDescsMethod() {
    printer.iprint("@Override%n");
    printer.iprint(
        "public java.util.List<%1$s<%2$s, ?>> getEntityPropertyDescs() {%n",
        /* 1 */ EntityPropertyDesc.class.getName(), /* 2 */ entityMeta.getEntityTypeName());
    printer.iprint("    return __entityPropertyDescs;%n");
    printer.iprint("}%n");
    printer.print("%n");
  }

  private void printGetEntityPropertyDescMethod() {
    printer.iprint("@Override%n");
    printer.iprint(
        "public %1$s<%2$s, ?> getEntityPropertyDesc(String __name) {%n",
        /* 1 */ EntityPropertyDesc.class.getName(), /* 2 */ entityMeta.getEntityTypeName());
    printer.iprint("    return __entityPropertyDescMap.get(__name);%n");
    printer.iprint("}%n");
    printer.print("%n");
  }

  private void printGetIdPropertyDescsMethod() {
    printer.iprint("@Override%n");
    printer.iprint(
        "public java.util.List<%1$s<%2$s, ?>> getIdPropertyDescs() {%n",
        /* 1 */ EntityPropertyDesc.class.getName(), /* 2 */ entityMeta.getEntityTypeName());
    printer.iprint("    return __idPropertyDescs;%n");
    printer.iprint("}%n");
    printer.print("%n");
  }

  private void printGetGeneratedIdPropertyDescMethod() {
    var idName = "null";
    if (entityMeta.hasGeneratedIdPropertyMeta()) {
      var pm = entityMeta.getGeneratedIdPropertyMeta();
      idName = pm.getFieldName();
    }
    printer.iprint("@Override%n");
    printer.iprint(
        "public %1$s<%2$s, ?, ?> getGeneratedIdPropertyDesc() {%n",
        /* 1 */ GeneratedIdPropertyDesc.class.getName(), /* 2 */ entityMeta.getEntityTypeName());
    printer.iprint("    return %1$s;%n", idName);
    printer.iprint("}%n");
    printer.print("%n");
  }

  private void printGetVersionPropertyDescMethod() {
    var versionName = "null";
    if (entityMeta.hasVersionPropertyMeta()) {
      var pm = entityMeta.getVersionPropertyMeta();
      versionName = pm.getFieldName();
    }
    printer.iprint("@Override%n");
    printer.iprint(
        "public %1$s<%2$s, ?, ?> getVersionPropertyDesc() {%n",
        /* 1 */ VersionPropertyDesc.class.getName(), /* 2 */ entityMeta.getEntityTypeName());
    printer.iprint("    return %1$s;%n", versionName);
    printer.iprint("}%n");
    printer.print("%n");
  }

  private void printNewEntityMethod() {
    if (hasGenericTypeProperty()) {
      printer.iprint("@SuppressWarnings(\"unchecked\")%n");
    }
    printer.iprint("@Override%n");
    printer.iprint(
        "public %1$s newEntity(%2$s<String, %3$s<%1$s, ?>> __args) {%n",
        /* 1 */ entityMeta.getEntityTypeName(),
        /* 2 */ Map.class.getName(),
        /* 3 */ Property.class.getName());
    if (entityMeta.isAbstract()) {
      printer.iprint("    return null;%n");
    } else {
      if (entityMeta.isImmutable()) {
        printer.iprint("    return new %1$s(%n", entityMeta.getEntityTypeName());
        for (var it = entityMeta.getAllPropertyMetas().iterator(); it.hasNext(); ) {
          var propertyMeta = it.next();
          if (propertyMeta.isEmbedded()) {
            printer.iprint(
                "        %1$s.newEmbeddable(\"%2$s\", __args)",
                /* 1 */ embeddableDesc(propertyMeta.getEmbeddableCtType()),
                /* 2 */ propertyMeta.getName());
          } else {
            printer.iprint(
                "        (%1$s)(__args.get(\"%2$s\") != null ? __args.get(\"%2$s\").get() : null)",
                /* 1 */ ctx.getTypes().boxIfPrimitive(propertyMeta.getType()),
                /* 2 */ propertyMeta.getName());
          }
          if (it.hasNext()) {
            printer.print(",%n");
          }
        }
        printer.print(");%n");
      } else {
        printer.iprint("    %1$s entity = new %1$s();%n", entityMeta.getEntityTypeName());
        for (var propertyMeta : entityMeta.getAllPropertyMetas()) {
          if (propertyMeta.isEmbedded()) {
            printer.iprint(
                "    %1$s.save(entity, %2$s.newEmbeddable(\"%3$s\", __args));%n",
                /* 1 */ propertyMeta.getFieldName(),
                /* 2 */ embeddableDesc(propertyMeta.getEmbeddableCtType()),
                /* 3 */ propertyMeta.getName());
          } else {
            printer.iprint(
                "    if (__args.get(\"%1$s\") != null) __args.get(\"%1$s\").save(entity);%n",
                /* 1 */ propertyMeta.getName());
          }
        }
        printer.iprint("    return entity;%n");
      }
    }
    printer.iprint("}%n");
    printer.print("%n");
  }

  private boolean hasGenericTypeProperty() {
    if (entityMeta.isImmutable()) {
      for (var propertyMeta : entityMeta.getAllPropertyMetas()) {
        var element = ctx.getTypes().toTypeElement(propertyMeta.getType());
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
    printer.iprint("@Override%n");
    printer.iprint("public Class<%1$s> getEntityClass() {%n", entityMeta.getEntityTypeName());
    printer.iprint("    return %1$s.class;%n", entityMeta.getEntityTypeName());
    printer.iprint("}%n");
    printer.print("%n");
  }

  private void printGetOriginalStatesMethod() {
    printer.iprint("@Override%n");
    printer.iprint(
        "public %1$s getOriginalStates(%1$s __entity) {%n", entityMeta.getEntityTypeName());
    if (!entityMeta.isAbstract() && entityMeta.hasOriginalStatesMeta()) {
      printer.iprint("    return __originalStatesAccessor.get(__entity);%n");
    } else {
      printer.iprint("    return null;%n");
    }
    printer.iprint("}%n");
    printer.print("%n");
  }

  private void printSaveCurrentStatesMethod() {
    printer.iprint("@Override%n");
    printer.iprint(
        "public void saveCurrentStates(%1$s __entity) {%n", entityMeta.getEntityTypeName());
    if (!entityMeta.isAbstract() && entityMeta.hasOriginalStatesMeta()) {
      printer.iprint("    %1$s __currentStates = new %1$s();%n", entityMeta.getEntityTypeName());
      for (var pm : entityMeta.getAllPropertyMetas()) {
        printer.iprint("    %1$s.copy(__currentStates, __entity);%n", pm.getFieldName());
      }
      printer.iprint("    __originalStatesAccessor.set(__entity, __currentStates);%n");
    }
    printer.iprint("}%n");
    printer.print("%n");
  }

  private void printGetSingletonInternalMethod() {
    printer.iprint("/**%n");
    printer.iprint(" * @return the singleton%n");
    printer.iprint(" */%n");
    printer.iprint("public static %1$s getSingletonInternal() {%n", codeSpec.getSimpleName());
    printer.iprint("    return __singleton;%n");
    printer.iprint("}%n");
    printer.print("%n");
  }

  private void printNewInstanceMethod() {
    printer.iprint("/**%n");
    printer.iprint(" * @return the new instance%n");
    printer.iprint(" */%n");
    printer.iprint("public static %1$s newInstance() {%n", codeSpec.getSimpleName());
    printer.iprint("    return new %1$s();%n", codeSpec.getSimpleName());
    printer.iprint("}%n");
    printer.print("%n");
  }

  private void printListenerHolder() {
    printer.iprint("private static class ListenerHolder {%n");
    if (entityMeta.hasGenericListener()) {
      printer.iprint(
          "    private static %1$s<%2$s> listener = new %1$s<>();%n",
          /* 1 */ entityMeta.getListenerElement().getQualifiedName(),
          /* 2 */ entityMeta.getEntityTypeName());
    } else {
      printer.iprint(
          "    private static %1$s listener = new %1$s();%n",
          entityMeta.getListenerElement().getQualifiedName());
    }
    printer.iprint("}%n");
    printer.print("%n");
  }

  private void printDeclareListener() {
    printer.iprint(
        "    Class __listenerClass = %1$s.class;%n",
        entityMeta.getListenerElement().getQualifiedName());
    printer.iprint(
        "    %1$s __listener = context.getConfig().getEntityListenerProvider().get(__listenerClass, __listenerSupplier);%n",
        entityMeta.getListenerElement().getQualifiedName());
  }

  private class IdGeneratorGenerator implements IdGeneratorMetaVisitor<Void> {

    @Override
    public void visitIdentityIdGeneratorMeta(IdentityIdGeneratorMeta m, Void p) {
      printer.iprint(
          "private final %1$s __idGenerator = new %1$s();%n", m.getIdGeneratorClassName());
    }

    @Override
    public void visitSequenceIdGeneratorMeta(SequenceIdGeneratorMeta m, Void p) {
      printer.iprint(
          "private final %1$s __idGenerator = new %1$s();%n", m.getIdGeneratorClassName());
      printer.iprint("{%n");
      printer.iprint(
          "    __idGenerator.setQualifiedSequenceName(\"%1$s\");%n", m.getQualifiedSequenceName());
      printer.iprint("    __idGenerator.setInitialValue(%1$s);%n", m.getInitialValue());
      printer.iprint("    __idGenerator.setAllocationSize(%1$s);%n", m.getAllocationSize());
      printer.iprint("    __idGenerator.initialize();%n");
      printer.iprint("}%n");
    }

    @Override
    public void visitTableIdGeneratorMeta(TableIdGeneratorMeta m, Void p) {
      printer.iprint(
          "private final %1$s __idGenerator = new %1$s();%n", m.getIdGeneratorClassName());
      printer.iprint("{%n");
      printer.iprint(
          "    __idGenerator.setQualifiedTableName(\"%1$s\");%n", m.getQualifiedTableName());
      printer.iprint("    __idGenerator.setInitialValue(%1$s);%n", m.getInitialValue());
      printer.iprint("    __idGenerator.setAllocationSize(%1$s);%n", m.getAllocationSize());
      printer.iprint("    __idGenerator.setPkColumnName(\"%1$s\");%n", m.getPkColumnName());
      printer.iprint("    __idGenerator.setPkColumnValue(\"%1$s\");%n", m.getPkColumnValue());
      printer.iprint("    __idGenerator.setValueColumnName(\"%1$s\");%n", m.getValueColumnName());
      printer.iprint("    __idGenerator.initialize();%n");
      printer.iprint("}%n");
    }
  }
}
