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

import static org.seasar.doma.internal.util.AssertionUtil.*;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.TypeElement;

import org.seasar.doma.internal.Constants;
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
import org.seasar.doma.internal.apt.util.TypeMirrorUtil;
import org.seasar.doma.internal.jdbc.util.TableUtil;
import org.seasar.doma.jdbc.entity.AbstractEntityType;
import org.seasar.doma.jdbc.entity.AssignedIdPropertyType;
import org.seasar.doma.jdbc.entity.BasicPropertyType;
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
import org.seasar.doma.jdbc.entity.VersionPropertyType;

/**
 * 
 * @author taedium
 * 
 */
public class EntityTypeGenerator extends AbstractGenerator {

    protected static final String NULL = "null";

    protected final EntityMeta entityMeta;

    public EntityTypeGenerator(ProcessingEnvironment env,
            TypeElement entityElement, EntityMeta entityMeta)
            throws IOException {
        super(env, entityElement, null, null, Constants.METATYPE_PREFIX, "");
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
        iprint("public final class %1$s extends %2$s<%3$s> {%n", simpleName,
                AbstractEntityType.class.getName(),
                entityMeta.getEntityTypeName());
        print("%n");
        indent();
        printValidateVersionStaticInitializer();
        printFields();
        printConstructor();
        printMethods();
        unindent();
        iprint("}%n");
    }

    protected void printFields() {
        printSingletonField();
        printOriginalStatesAccessorField();
        printIdGeneratorField();
        printPropertyTypeFields();
        printListenerField();
        printNamingTypeField();
        printImmutableField();
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

    protected void printOriginalStatesAccessorField() {
        if (!entityMeta.isAbstract() && entityMeta.hasOriginalStatesMeta()) {
            OriginalStatesMeta osm = entityMeta.getOriginalStatesMeta();
            iprint("private static final %1$s<%2$s> __originalStatesAccessor = new %1$s<%2$s>(%3$s.class, \"%4$s\");%n",
                    OriginalStatesAccessor.class.getName(), osm
                            .getTypeElement().getQualifiedName(), osm
                            .getFieldEnclosingElement().getQualifiedName(), osm
                            .getFieldElement().getSimpleName());
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
            String parentEntityPropertyType = "null";
            String parentEntityTypeNameAsTypeParameter = Object.class.getName();
            if (!pm.isOwnProperty()) {
                parentEntityPropertyType = pm.getEntityMetaTypeName()
                        + ".getSingletonInternal()." + pm.getFieldName();
                parentEntityTypeNameAsTypeParameter = pm.getEntityTypeName();
            }
            String domainType = "null";
            String domainTypeNameAsTypeParameter = Object.class.getName();
            if (visitor.domainType != null) {
                domainType = visitor.domainType.getInstantiationCommand();
                domainTypeNameAsTypeParameter = visitor.domainType
                        .getTypeNameAsTypeParameter();
            }
            iprint("/** the %1$s */%n", pm.getName());
            if (pm.isId()) {
                if (pm.getIdGeneratorMeta() != null) {
                    iprint("public final %1$s<%11$s, %2$s, %3$s, %9$s> %12$s = new %1$s<%11$s, %2$s, %3$s, %9$s>(%6$s.class, %3$s.class, %7$s.class, %10$s, %8$s, \"%4$s\", \"%5$s\", __idGenerator);%n",
                    /* 1 */GeneratedIdPropertyType.class.getName(),
                    /* 2 */entityMeta.getEntityTypeName(),
                    /* 3 */visitor.wrapperType.getWrappedType()
                            .getTypeNameAsTypeParameter(),
                    /* 4 */pm.getName(),
                    /* 5 */pm.getColumnName(),
                    /* 6 */entityMeta.getEntityTypeName(),
                    /* 7 */visitor.wrapperType.getQualifiedName(),
                    /* 8 */domainType,
                    /* 9 */domainTypeNameAsTypeParameter,
                    /* 10 */parentEntityPropertyType,
                    /* 11 */parentEntityTypeNameAsTypeParameter,
                    /* 12 */pm.getFieldName());
                } else {
                    iprint("public final %1$s<%11$s, %2$s, %3$s, %9$s> %12$s = new %1$s<%11$s, %2$s, %3$s, %9$s>(%6$s.class, %3$s.class, %7$s.class, %10$s, %8$s, \"%4$s\", \"%5$s\");%n",
                    /* 1 */AssignedIdPropertyType.class.getName(),
                    /* 2 */entityMeta.getEntityTypeName(),
                    /* 3 */visitor.wrapperType.getWrappedType()
                            .getTypeNameAsTypeParameter(),
                    /* 4 */pm.getName(),
                    /* 5 */pm.getColumnName(),
                    /* 6 */entityMeta.getEntityTypeName(),
                    /* 7 */visitor.wrapperType.getQualifiedName(),
                    /* 8 */domainType,
                    /* 9 */domainTypeNameAsTypeParameter,
                    /* 10 */parentEntityPropertyType,
                    /* 11 */parentEntityTypeNameAsTypeParameter,
                    /* 12 */pm.getFieldName());
                }
            } else if (pm.isVersion()) {
                iprint("public final %1$s<%11$s, %2$s, %3$s, %9$s> %12$s = new %1$s<%11$s, %2$s, %3$s, %9$s>(%6$s.class, %3$s.class, %7$s.class, %10$s, %8$s, \"%4$s\", \"%5$s\");%n",
                /* 1 */VersionPropertyType.class.getName(),
                /* 2 */entityMeta.getEntityTypeName(),
                /* 3 */visitor.wrapperType.getWrappedType()
                        .getTypeNameAsTypeParameter(),
                /* 4 */pm.getName(),
                /* 5 */pm.getColumnName(),
                /* 6 */entityMeta.getEntityTypeName(),
                /* 7 */visitor.wrapperType.getQualifiedName(),
                /* 8 */domainType,
                /* 9 */domainTypeNameAsTypeParameter,
                /* 10 */parentEntityPropertyType,
                /* 11 */parentEntityTypeNameAsTypeParameter,
                /* 12 */pm.getFieldName());
            } else {
                iprint("public final %1$s<%13$s, %2$s, %3$s, %11$s> %14$s = new %1$s<%13$s, %2$s, %3$s, %11$s>(%8$s.class, %3$s.class, %9$s.class, %12$s, %10$s, \"%4$s\", \"%5$s\", %6$s, %7$s);%n",
                /* 1 */BasicPropertyType.class.getName(),
                /* 2 */entityMeta.getEntityTypeName(),
                /* 3 */visitor.wrapperType.getWrappedType()
                        .getTypeNameAsTypeParameter(),
                /* 4 */pm.getName(),
                /* 5 */pm.getColumnName(),
                /* 6 */pm.isColumnInsertable(),
                /* 7 */pm.isColumnUpdatable(),
                /* 8 */entityMeta.getEntityTypeName(),
                /* 9 */visitor.wrapperType.getQualifiedName(),
                /* 10 */domainType,
                /* 11 */domainTypeNameAsTypeParameter,
                /* 12 */parentEntityPropertyType,
                /* 13 */parentEntityTypeNameAsTypeParameter,
                /* 14 */pm.getFieldName());
            }
            print("%n");
        }
    }

    protected void printListenerField() {
        if (entityMeta.isGenericEntityListener()) {
            iprint("private final %1$s<%2$s> __listener;%n", entityMeta
                    .getEntityListenerElement().getQualifiedName(),
                    entityMeta.getEntityTypeName());
        } else {
            iprint("private final %1$s __listener;%n", entityMeta
                    .getEntityListenerElement().getQualifiedName());
        }
        print("%n");
    }

    protected void printNamingTypeField() {
        iprint("private final %1$s __namingType;%n", NamingType.class.getName());
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

    protected void printQualifiedTableNameField() {
        iprint("private final String __qualifiedTableName;%n");
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
        if (entityMeta.isGenericEntityListener()) {
            iprint("    __listener = new %1$s<%2$s>();%n", entityMeta
                    .getEntityListenerElement().getQualifiedName(),
                    entityMeta.getEntityTypeName());
        } else {
            iprint("    __listener = new %1$s();%n", entityMeta
                    .getEntityListenerElement().getQualifiedName());
        }
        iprint("    __namingType = %1$s.%2$s;%n", NamingType.class.getName(),
                entityMeta.getNamingType().name());
        iprint("    __immutable = %1$s;%n", entityMeta.isImmutable());
        iprint("    __name = \"%1$s\";%n", entityMeta.getEntityName());
        iprint("    __catalogName = \"%1$s\";%n", entityMeta.getCatalogName());
        iprint("    __schemaName = \"%1$s\";%n", entityMeta.getSchemaName());
        iprint("    __tableName = \"%1$s\";%n", entityMeta.getTableName());
        iprint("    __qualifiedTableName = \"%1$s\";%n",
                TableUtil.getQualifiedTableName(entityMeta.getCatalogName(),
                        entityMeta.getSchemaName(), entityMeta.getTableName()));
        iprint("    java.util.List<%1$s<%2$s, ?>> __idList = new java.util.ArrayList<%1$s<%2$s, ?>>();%n",
                EntityPropertyType.class.getName(),
                entityMeta.getEntityTypeName());
        iprint("    java.util.List<%1$s<%2$s, ?>> __list = new java.util.ArrayList<%1$s<%2$s, ?>>(%3$s);%n",
                EntityPropertyType.class.getName(), entityMeta
                        .getEntityTypeName(), entityMeta.getAllPropertyMetas()
                        .size());
        iprint("    java.util.Map<String, %1$s<%2$s, ?>> __map = new java.util.HashMap<String, %1$s<%2$s, ?>>(%3$s);%n",
                EntityPropertyType.class.getName(), entityMeta
                        .getEntityTypeName(), entityMeta.getAllPropertyMetas()
                        .size());
        for (EntityPropertyMeta pm : entityMeta.getAllPropertyMetas()) {
            if (pm.isId()) {
                iprint("    __idList.add(%1$s);%n", pm.getFieldName());
            }
            iprint("    __list.add(%1$s);%n", pm.getFieldName());
            iprint("    __map.put(\"%1$s\", %2$s);%n", pm.getName(),
                    pm.getFieldName());
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
        printGetQualifiedTableNameMethod();
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
        printNewEntityWithMapMethod();
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
        iprint("public void preInsert(%1$s entity, %2$s<%1$s> context) {%n",
                entityMeta.getEntityTypeName(),
                PreInsertContext.class.getName());
        iprint("    __listener.preInsert(entity, context);%n");
        iprint("}%n");
        print("%n");
    }

    protected void printPreUpdateMethod() {
        iprint("@Override%n");
        iprint("public void preUpdate(%1$s entity, %2$s<%1$s> context) {%n",
                entityMeta.getEntityTypeName(),
                PreUpdateContext.class.getName());
        iprint("    __listener.preUpdate(entity, context);%n");
        iprint("}%n");
        print("%n");
    }

    protected void printPreDeleteMethod() {
        iprint("@Override%n");
        iprint("public void preDelete(%1$s entity, %2$s<%1$s> context) {%n",
                entityMeta.getEntityTypeName(),
                PreDeleteContext.class.getName());
        iprint("    __listener.preDelete(entity, context);%n");
        iprint("}%n");
        print("%n");
    }

    protected void printPostInsertMethod() {
        iprint("@Override%n");
        iprint("public void postInsert(%1$s entity, %2$s<%1$s> context) {%n",
                entityMeta.getEntityTypeName(),
                PostInsertContext.class.getName());
        iprint("    __listener.postInsert(entity, context);%n");
        iprint("}%n");
        print("%n");
    }

    protected void printPostUpdateMethod() {
        iprint("@Override%n");
        iprint("public void postUpdate(%1$s entity, %2$s<%1$s> context) {%n",
                entityMeta.getEntityTypeName(),
                PostUpdateContext.class.getName());
        iprint("    __listener.postUpdate(entity, context);%n");
        iprint("}%n");
        print("%n");
    }

    protected void printPostDeleteMethod() {
        iprint("@Override%n");
        iprint("public void postDelete(%1$s entity, %2$s<%1$s> context) {%n",
                entityMeta.getEntityTypeName(),
                PostDeleteContext.class.getName());
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
        String parentEntityTypeNameAsTypeParameter = Object.class.getName();
        String idName = "null";
        if (entityMeta.hasGeneratedIdPropertyMeta()) {
            EntityPropertyMeta pm = entityMeta.getGeneratedIdPropertyMeta();
            idName = pm.getFieldName();
            if (!pm.isOwnProperty()) {
                parentEntityTypeNameAsTypeParameter = pm.getEntityTypeName();
            }
        }
        iprint("@Override%n");
        iprint("public %1$s<%3$s, %2$s, ?, ?> getGeneratedIdPropertyType() {%n",
                GeneratedIdPropertyType.class.getName(),
                entityMeta.getEntityTypeName(),
                parentEntityTypeNameAsTypeParameter);
        iprint("    return %1$s;%n", idName);
        iprint("}%n");
        print("%n");
    }

    protected void printGetVersionPropertyTypeMethod() {
        String parentEntityTypeNameAsTypeParameter = Object.class.getName();
        String versionName = "null";
        if (entityMeta.hasVersionPropertyMeta()) {
            EntityPropertyMeta pm = entityMeta.getVersionPropertyMeta();
            versionName = pm.getFieldName();
            if (!pm.isOwnProperty()) {
                parentEntityTypeNameAsTypeParameter = pm.getEntityTypeName();
            }
        }
        iprint("@Override%n");
        iprint("public %1$s<%3$s, %2$s, ?, ?> getVersionPropertyType() {%n",
                VersionPropertyType.class.getName(),
                entityMeta.getEntityTypeName(),
                parentEntityTypeNameAsTypeParameter);
        iprint("    return %1$s;%n", versionName);
        iprint("}%n");
        print("%n");
    }

    protected void printNewEntityMethod() {
        iprint("@Override%n");
        iprint("public %1$s newEntity() {%n", entityMeta.getEntityTypeName());
        if (entityMeta.isImmutable() || entityMeta.isAbstract()) {
            iprint("    return null;%n");
        } else {
            iprint("    return new %1$s();%n", entityMeta.getEntityTypeName());
        }
        iprint("}%n");
        print("%n");
    }

    protected void printNewEntityWithMapMethod() {
        if (hasGenericTypeProperty()) {
            iprint("@SuppressWarnings(\"unchecked\")%n");
        }
        iprint("@Override%n");
        iprint("public %1$s newEntity(%2$s<String, Object> __args) {%n",
                entityMeta.getEntityTypeName(), Map.class.getName());
        if (entityMeta.isAbstract()) {
            iprint("    return null;%n");
        } else {
            if (entityMeta.isImmutable()) {
                iprint("    return new %1$s(%n", entityMeta.getEntityTypeName());
                for (Iterator<EntityPropertyMeta> it = entityMeta
                        .getAllPropertyMetasInCtorArgsOrder().iterator(); it
                        .hasNext();) {
                    EntityPropertyMeta propertyMeta = it.next();
                    iprint("        (%1$s)__args.get(\"%2$s\")",
                            TypeMirrorUtil.boxIfPrimitive(
                                    propertyMeta.getType(), env),
                            propertyMeta.getName());
                    if (it.hasNext()) {
                        print(",\n");
                    }
                }
                print(");%n");
            } else {
                iprint("    return new %1$s();%n",
                        entityMeta.getEntityTypeName());
            }
        }
        iprint("}%n");
        print("%n");
    }

    protected boolean hasGenericTypeProperty() {
        if (entityMeta.isImmutable()) {
            for (EntityPropertyMeta propertyMeta : entityMeta
                    .getAllPropertyMetas()) {
                TypeElement element = TypeMirrorUtil.toTypeElement(
                        propertyMeta.getType(), env);
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
                iprint("    %1$s.getWrapper(__currentStates).set(%1$s.getWrapper(__entity).getCopy());%n",
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

}
