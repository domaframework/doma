package org.seasar.doma.internal.apt.generator;

import static org.seasar.doma.internal.apt.generator.CodeHelper.embeddableDesc;
import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.util.Formatter;
import java.util.Iterator;
import java.util.Map;
import java.util.function.BiFunction;

import javax.lang.model.element.TypeElement;

import org.seasar.doma.internal.apt.Context;
import org.seasar.doma.internal.apt.codespec.CodeSpec;
import org.seasar.doma.internal.apt.meta.entity.EntityMeta;
import org.seasar.doma.internal.apt.meta.entity.EntityPropertyMeta;
import org.seasar.doma.internal.apt.meta.entity.OriginalStatesMeta;
import org.seasar.doma.internal.apt.meta.id.IdGeneratorMeta;
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

/**
 * 
 * @author taedium
 * 
 */
public class EntityDescGenerator extends AbstractGenerator {

    private final EntityMeta entityMeta;

    public EntityDescGenerator(Context ctx, EntityMeta entityMeta, CodeSpec codeSpec,
            Formatter formatter) {
        super(ctx, codeSpec, formatter);
        assertNotNull(entityMeta);
        this.entityMeta = entityMeta;
    }

    @Override
    public void generate() {
        printPackage();
        printClass();
    }

    private void printClass() {
        iprint("/** */%n");
        printGenerated();
        iprint("public final class %1$s extends %2$s<%3$s> {%n",
        // @formatter:off
                /* 1 */codeSpec.getSimpleName(),
                /* 2 */AbstractEntityDesc.class.getName(),
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
        iprint("private static final %1$s __singleton = new %1$s();%n",
        // @formatter:off
                /* 1 */codeSpec.getSimpleName());
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

    private void printPropertyDescFields() {
        for (EntityPropertyMeta pm : entityMeta.getAllPropertyMetas()) {
            iprint("/** the %1$s */%n", pm.getName());
            if (pm.isEmbedded()) {
                iprint("public final %1$s<%2$s, %3$s> %4$s = "
                        + "new %1$s<>(\"%5$s\", %2$s.class, %6$s.getEmbeddablePropertyDescs"
                        + "(\"%7$s\", %2$s.class, __namingType));%n",
                // @formatter:off
                        /* 1 */EmbeddedPropertyDesc.class.getName(),
                        /* 2 */entityMeta.getEntityTypeName(), 
                        /* 3 */pm.getTypeName(),
                        /* 4 */pm.getFieldName(), 
                        /* 5 */pm.getName(),
                        /* 6 */embeddableDesc(pm.getEmbeddableCtType()), 
                        /* 7 */pm.getName());
                        // @formatter:on
            } else {
                Object[] args = createPropertyFormatArgs(pm);
                Object className = args[0];
                if (GeneratedIdPropertyDesc.class.getName().equals(className)) {
                    iprint("public final %1$s<%2$s, %3$s, %4$s> %5$s = "
                            + "new %1$s<>(%6$s, %7$s, %8$s, %9$s, __namingType, %10$s, __idGenerator);%n",
                            args);
                } else if (AssignedIdPropertyDesc.class.getName().equals(className)) {
                    iprint("public final %1$s<%2$s, %3$s, %4$s> %5$s = "
                            + "new %1$s<>(%6$s, %7$s, %8$s, %9$s, __namingType, %10$s);%n", args);
                } else if (VersionPropertyDesc.class.getName().equals(className)) {
                    iprint("public final %1$s<%2$s, %3$s, %4$s> %5$s = "
                            + "new %1$s<>(%6$s, %7$s, %8$s, %9$s, __namingType, %10$s);%n", args);
                } else {
                    iprint("public final %1$s<%2$s, %3$s, %4$s> %5$s = "
                            + "new %1$s<>(%6$s, %7$s, %8$s, %9$s, __namingType, %11$s, %12$s, %10$s);%n",
                            args);
                }
            }
            print("%n");
        }
    }

    private Object[] createPropertyFormatArgs(EntityPropertyMeta pm) {

        class Args {
            String _1_propertyDescClass;
            String _2_ENTITY;
            String _3_BASIC;
            String _4_CONTAINER;
            String _5_fieldName;
            String _6_entityClass;
            String _7_scalarSupplier;
            String _8_name;
            String _9_columnName;
            boolean _10_quoteRequired;
            boolean _11_insertable;
            boolean _12_updatable;

            Object[] toArray() {
                return new Object[] { _1_propertyDescClass, _2_ENTITY, _3_BASIC, _4_CONTAINER,
                        _5_fieldName, _6_entityClass, _7_scalarSupplier, _8_name, _9_columnName,
                        _10_quoteRequired, _11_insertable, _12_updatable };
            }
        }

        Args args = new Args();
        if (pm.isId()) {
            if (pm.getIdGeneratorMeta() != null) {
                args._1_propertyDescClass = GeneratedIdPropertyDesc.class.getName();
            } else {
                args._1_propertyDescClass = AssignedIdPropertyDesc.class.getName();
            }
        } else if (pm.isVersion()) {
            args._1_propertyDescClass = VersionPropertyDesc.class.getName();
        } else {
            args._1_propertyDescClass = DefaultPropertyDesc.class.getName();
        }
        args._2_ENTITY = entityMeta.getEntityTypeName();
        args._3_BASIC = pm.getCtType().accept(new BasicTypeArgCodeBuilder(), null);
        args._4_CONTAINER = pm.getCtType().accept(new ContainerTypeArgCodeBuilder(), false);
        args._5_fieldName = pm.getFieldName();
        args._6_entityClass = entityMeta.getEntityTypeName() + ".class";
        args._7_scalarSupplier = pm.getCtType().accept(new ScalarSupplierCodeBuilder(), false);
        args._8_name = "\"" + pm.getName() + "\"";
        args._9_columnName = "\"" + pm.getColumnName() + "\"";
        args._10_quoteRequired = pm.isColumnQuoteRequired();
        args._11_insertable = pm.isColumnInsertable();
        args._12_updatable = pm.isColumnUpdatable();
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

    private void printIdPropertyDescsField() {
        iprint("private final java.util.List<%1$s<%2$s, ?>> __idPropertyDescs;%n",
        // @formatter:off
                /* 1 */EntityPropertyDesc.class.getName(), 
                /* 2 */entityMeta.getEntityTypeName());
                // @formatter:on
        print("%n");
    }

    private void printEntityPropertyDescsField() {
        iprint("private final java.util.List<%1$s<%2$s, ?>> __entityPropertyDescs;%n",
        // @formatter:off
                /* 1 */EntityPropertyDesc.class.getName(), 
                /* 2 */entityMeta.getEntityTypeName());
                // @formatter:on
        print("%n");
    }

    private void printEntityPropertyDescMapField() {
        iprint("private final java.util.Map<String, %1$s<%2$s, ?>> __entityPropertyDescMap;%n",
        // @formatter:off
                /* 1 */EntityPropertyDesc.class.getName(),
                /* 2 */entityMeta.getEntityTypeName());
                // @formatter:on
        print("%n");
    }

    private void printConstructor() {
        iprint("private %1$s() {%n", codeSpec.getSimpleName());
        iprint("    __listenerSupplier = () -> ListenerHolder.listener;%n");
        iprint("    __immutable = %1$s;%n", entityMeta.isImmutable());
        iprint("    __name = \"%1$s\";%n", entityMeta.getEntityName());
        iprint("    __catalogName = \"%1$s\";%n", entityMeta.getCatalogName());
        iprint("    __schemaName = \"%1$s\";%n", entityMeta.getSchemaName());
        iprint("    __tableName = \"%1$s\";%n", entityMeta.getTableName());
        iprint("    __isQuoteRequired = %1$s;%n", entityMeta.isQuoteRequired());
        iprint("    java.util.List<%1$s<%2$s, ?>> __idList = new java.util.ArrayList<>();%n",
        // @formatter:off
                /* 1 */EntityPropertyDesc.class.getName(),
                /* 2 */entityMeta.getEntityTypeName());
                // @formatter:on
        iprint("    java.util.List<%1$s<%2$s, ?>> __list = new java.util.ArrayList<>(%3$s);%n",
        // @formatter:off
                /* 1 */EntityPropertyDesc.class.getName(),
                /* 2 */entityMeta.getEntityTypeName(),
                /* 3 */entityMeta.getAllPropertyMetas().size());
                // @formatter:on
        iprint("    java.util.Map<String, %1$s<%2$s, ?>> __map = new java.util.HashMap<>(%3$s);%n",
        // @formatter:off
                /* 1 */EntityPropertyDesc.class.getName(),
                /* 2 */entityMeta.getEntityTypeName(),
                /* 3 */entityMeta.getAllPropertyMetas().size());
                // @formatter:off
        for (EntityPropertyMeta pm : entityMeta.getAllPropertyMetas()) {
            if (pm.isEmbedded()) {
                iprint("    __list.addAll(%1$s.getEmbeddablePropertyDescs());%n",
                        pm.getFieldName());
                iprint("    __map.putAll(%1$s.getEmbeddablePropertyDescMap());%n",
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
        iprint("    __idPropertyDescs = java.util.Collections.unmodifiableList(__idList);%n");
        iprint("    __entityPropertyDescs = java.util.Collections.unmodifiableList(__list);%n");
        iprint("    __entityPropertyDescMap = java.util.Collections.unmodifiableMap(__map);%n");
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

    private void printGetEntityPropertyDescsMethod() {
        iprint("@Override%n");
        iprint("public java.util.List<%1$s<%2$s, ?>> getEntityPropertyDescs() {%n",
        // @formatter:off
                /* 1 */EntityPropertyDesc.class.getName(), 
                /* 2 */entityMeta.getEntityTypeName());
                // @formatter:on
        iprint("    return __entityPropertyDescs;%n");
        iprint("}%n");
        print("%n");
    }

    private void printGetEntityPropertyDescMethod() {
        iprint("@Override%n");
        iprint("public %1$s<%2$s, ?> getEntityPropertyDesc(String __name) {%n",
        // @formatter:off
                /* 1 */EntityPropertyDesc.class.getName(),
                /* 2 */entityMeta.getEntityTypeName());
                // @formatter:on
        iprint("    return __entityPropertyDescMap.get(__name);%n");
        iprint("}%n");
        print("%n");
    }

    private void printGetIdPropertyDescsMethod() {
        iprint("@Override%n");
        iprint("public java.util.List<%1$s<%2$s, ?>> getIdPropertyDescs() {%n",
        // @formatter:off
                /* 1 */EntityPropertyDesc.class.getName(),
                /* 2 */entityMeta.getEntityTypeName());
                // @formatter:on
        iprint("    return __idPropertyDescs;%n");
        iprint("}%n");
        print("%n");
    }

    private void printGetGeneratedIdPropertyDescMethod() {
        String idName = "null";
        if (entityMeta.hasGeneratedIdPropertyMeta()) {
            EntityPropertyMeta pm = entityMeta.getGeneratedIdPropertyMeta();
            idName = pm.getFieldName();
        }
        iprint("@Override%n");
        iprint("public %1$s<%2$s, ?, ?> getGeneratedIdPropertyDesc() {%n",
        // @formatter:off
                /* 1 */GeneratedIdPropertyDesc.class.getName(),
                /* 2 */entityMeta.getEntityTypeName());
                // @formatter:on
        iprint("    return %1$s;%n", idName);
        iprint("}%n");
        print("%n");
    }

    private void printGetVersionPropertyDescMethod() {
        String versionName = "null";
        if (entityMeta.hasVersionPropertyMeta()) {
            EntityPropertyMeta pm = entityMeta.getVersionPropertyMeta();
            versionName = pm.getFieldName();
        }
        iprint("@Override%n");
        iprint("public %1$s<%2$s, ?, ?> getVersionPropertyDesc() {%n",
        // @formatter:off
                /* 1 */VersionPropertyDesc.class.getName(),
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
                        iprint("        %1$s.newEmbeddable(\"%2$s\", __args)",
                        // @formatter:off
                                /* 1 */embeddableDesc(propertyMeta.getEmbeddableCtType()),
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
                        iprint("    %1$s.save(entity, %2$s.newEmbeddable(\"%3$s\", __args));%n",
                        // @formatter:off
                                /* 1 */propertyMeta.getFieldName(),
                                /* 2 */embeddableDesc(propertyMeta.getEmbeddableCtType()),
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
        iprint("public static %1$s getSingletonInternal() {%n", codeSpec.getSimpleName());
        iprint("    return __singleton;%n");
        iprint("}%n");
        print("%n");
    }

    private void printNewInstanceMethod() {
        iprint("/**%n");
        iprint(" * @return the new instance%n");
        iprint(" */%n");
        iprint("public static %1$s newInstance() {%n", codeSpec.getSimpleName());
        iprint("    return new %1$s();%n", codeSpec.getSimpleName());
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
        public Void visitIdentityIdGeneratorMeta(IdentityIdGeneratorMeta m, Void p) {
            iprint("private final %1$s __idGenerator = new %1$s();%n", m.getIdGeneratorClassName());
            return null;
        }

        @Override
        public Void visitSequenceIdGeneratorMeta(SequenceIdGeneratorMeta m, Void p) {
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
        public Void visitTableIdGeneratorMeta(TableIdGeneratorMeta m, Void p) {
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

}
