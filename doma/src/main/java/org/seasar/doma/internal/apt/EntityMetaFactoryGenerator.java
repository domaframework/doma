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

import org.seasar.doma.internal.apt.meta.ColumnMeta;
import org.seasar.doma.internal.apt.meta.EntityMeta;
import org.seasar.doma.internal.apt.meta.EntityPropertyMeta;
import org.seasar.doma.internal.apt.meta.IdGeneratorMeta;
import org.seasar.doma.internal.apt.meta.IdGeneratorMetaVisitor;
import org.seasar.doma.internal.apt.meta.IdentityIdGeneratorMeta;
import org.seasar.doma.internal.apt.meta.SequenceIdGeneratorMeta;
import org.seasar.doma.internal.apt.meta.TableIdGeneratorMeta;
import org.seasar.doma.internal.apt.meta.TableMeta;
import org.seasar.doma.jdbc.entity.AbstractEntityMeta;
import org.seasar.doma.jdbc.entity.AssignedIdPropertyMeta;
import org.seasar.doma.jdbc.entity.BasicPropertyMeta;
import org.seasar.doma.jdbc.entity.EntityMetaFactory;
import org.seasar.doma.jdbc.entity.GeneratedIdPropertyMeta;
import org.seasar.doma.jdbc.entity.VersionPropertyMeta;

/**
 * 
 * @author taedium
 * 
 */
public class EntityMetaFactoryGenerator extends AbstractGenerator {

    protected final EntityMeta entityMeta;

    public EntityMetaFactoryGenerator(ProcessingEnvironment env,
            TypeElement entityElement, EntityMeta entityMeta)
            throws IOException {
        super(env, entityElement, null, null, Options.getEntitySuffix(env));
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
        printGenerated();
        iprint("public class %1$s implements %2$s<%3$s> {%n", simpleName,
                EntityMetaFactory.class.getName(), entityMeta
                        .getEntityTypeName());
        print("%n");
        indent();
        printMethods();
        printMetaClass();
        unindent();
        iprint("}%n");
    }

    protected void printMethods() {
        iprint("@Override%n");
        iprint("public %1$s<%2$s> createEntityMeta() {%n",
                org.seasar.doma.jdbc.entity.EntityMeta.class.getName(),
                entityMeta.getEntityTypeName());
        iprint("    return new Meta();%n");
        iprint("}%n");
        print("%n");
        iprint("@Override%n");
        iprint("public %1$s<%2$s> createEntityMeta(%2$s entity) {%n",
                org.seasar.doma.jdbc.entity.EntityMeta.class.getName(),
                entityMeta.getEntityTypeName());
        iprint("    return new Meta(entity);%n");
        iprint("}%n");
        print("%n");
    }

    protected void printMetaClass() {
        iprint("public static class Meta extends %1$s<%2$s> {%n",
                AbstractEntityMeta.class.getName(), entityMeta
                        .getEntityTypeName());
        print("%n");
        indent();
        printMetaClassFields();
        printMetaClassConstructors();
        printMetaClassMethods();
        unindent();
        iprint("}%n");
        print("%n");
    }

    protected void printMetaClassFields() {
        printMetaClassGeneratedIdPropertyField();
        printMetaClassListenerField();
        printMetaClassPropertyField();
        printMetaClassEntityField();
        printMetaClassDirtyStatesField();
        printMetaClassNameField();
        printMetaClassPropertiesField();
        printMetaClassPropertyMapField();
    }

    protected void printMetaClassGeneratedIdPropertyField() {
        if (entityMeta.hasGeneratedIdPropertyMeta()) {
            EntityPropertyMeta propertyMeta = entityMeta
                    .getGeneratedIdPropertyMeta();
            IdGeneratorMeta idGeneratorMeta = propertyMeta.getIdGeneratorMeta();
            idGeneratorMeta.accept(new IdGeneratorGenerator(), null);
            print("%n");
        }
    }

    protected void printMetaClassListenerField() {
        iprint("private static final %1$s __listener = new %1$s();%n",
                entityMeta.getListenerTypeName());
        print("%n");
    }

    protected void printMetaClassEntityField() {
        iprint("private final %1$s __entity;%n", entityMeta.getEntityTypeName());
        print("%n");
    }

    protected void printMetaClassDirtyStatesField() {
        iprint("private final java.util.Set<java.lang.String> __modifiedProperties;%n");
        print("%n");
    }

    protected void printMetaClassPropertyField() {
        for (EntityPropertyMeta pm : entityMeta.getAllPropertyMetas()) {
            if (pm.isTrnsient()) {
                continue;
            }
            ColumnMeta cm = pm.getColumnMeta();
            String columnName = cm.getName();
            String quote = "\"";
            if (columnName == null) {
                columnName = "null";
                quote = "";
            }
            if (pm.isId()) {
                if (pm.getIdGeneratorMeta() != null) {
                    iprint(
                            "private final %1$s<%2$s> %3$s = new %1$s<%2$s>(\"%3$s\", %5$s%4$s%5$s, new %2$s(), __idGenerator);%n", /* 1 */
                            GeneratedIdPropertyMeta.class.getName(), /* 2 */
                            pm.getWrapperTypeName(), /* 3 */pm.getName(), /* 4 */
                            columnName, /* 5 */quote);
                } else {
                    iprint(
                            "private final %1$s<%2$s> %3$s = new %1$s<%2$s>(\"%3$s\", %5$s%4$s%5$s, new %2$s());%n", /* 1 */
                            AssignedIdPropertyMeta.class.getName(), /* 2 */
                            pm.getWrapperTypeName(), /* 3 */pm.getName(), /* 4 */
                            columnName, /* 5 */quote);
                }
            } else if (pm.isVersion()) {
                iprint(
                        "private final %1$s<%2$s> %3$s = new %1$s<%2$s>(\"%3$s\", %5$s%4$s%5$s, new %2$s());%n", /* 1 */
                        VersionPropertyMeta.class.getName(), /* 2 */pm
                                .getWrapperTypeName(), /* 3 */pm.getName(), /* 4 */
                        columnName, /* 5 */quote);
            } else {
                iprint(
                        "private final %1$s<%2$s> %3$s = new %1$s<%2$s>(\"%3$s\", %7$s%4$s%7$s, new %2$s(), %5$s, %6$s);%n", /* 1 */
                        BasicPropertyMeta.class.getName(), /* 2 */pm
                                .getWrapperTypeName(), /* 3 */pm.getName(), /* 4 */
                        columnName, /* 5 */cm.isInsertable(), /* 6 */cm
                                .isUpdatable(), /* 7 */quote);
            }
            print("%n");
        }
    }

    protected void printMetaClassNameField() {
        iprint("private final String __name = \"%1$s\";%n", entityMeta
                .getEntityName());
        print("%n");
    }

    protected void printMetaClassPropertiesField() {
        iprint("private java.util.List<%1$s<?>> __entityProperties;%n",
                org.seasar.doma.jdbc.entity.EntityPropertyMeta.class.getName());
        print("%n");
    }

    protected void printMetaClassPropertyMapField() {
        iprint("private java.util.Map<String, %1$s<?>> __entityPropertyMap;%n",
                org.seasar.doma.jdbc.entity.EntityPropertyMeta.class.getName());
        print("%n");
    }

    protected void printMetaClassConstructors() {
        iprint("private Meta() {%n");
        iprint("    this(new %1$s());%n", entityMeta.getEntityTypeName());
        iprint("}%n");
        print("%n");
        iprint("private Meta(%1$s entity) {%n", entityMeta.getEntityTypeName());
        TableMeta tm = entityMeta.getTableMeta();
        String catalog = null;
        String catalogQuote = "";
        if (tm.getCatalog() != null) {
            catalog = tm.getCatalog();
            catalogQuote = "\"";
        }
        String schema = null;
        String schemaQuote = "";
        if (tm.getSchema() != null) {
            schema = tm.getSchema();
            schemaQuote = "\"";
        }
        String table = null;
        String tableQuote = "";
        if (tm.getName() != null) {
            table = tm.getName();
            tableQuote = "\"";
        }
        iprint("    super(%2$s%1$s%2$s, %4$s%3$s%4$s, %6$s%5$s%6$s);%n",
                catalog, catalogQuote, schema, schemaQuote, table, tableQuote);
        for (EntityPropertyMeta pm : entityMeta.getAllPropertyMetas()) {
            if (!pm.isTrnsient()) {
                iprint("    %1$s.getWrapper().set(entity.%1$s);%n", pm
                        .getName());
            }
        }
        iprint("    __entity = entity;%n");
        String modifiedPropertyFieldName = entityMeta
                .getModifiedPropertiesFieldName();
        if (modifiedPropertyFieldName != null) {
            iprint("    __modifiedProperties = entity.%1$s;%n",
                    modifiedPropertyFieldName);
        } else {
            iprint("    __modifiedProperties = null;%n");
        }
        iprint("}%n");
        print("%n");
    }

    protected void printMetaClassMethods() {
        printMetaClassGetNameMethod();
        printMetaClassPreInsertMethod();
        printMetaClassPreUpdateMethod();
        printMetaClassPreDeleteMethod();
        printMetaClassGetPropertyMetasMethod();
        printMetaClassGetPropertyMetaMethod();
        printMetaClassGetGeneratedIdPropertyMethod();
        printMetaClassGetVersionPropertyMethod();
        printMetaClassRefreshEntityMethod();
        printMetaClassRefreshEntityInternalMethod();
        printMetaClassGetEntityMethod();
        printMetaClassGetEntityClassMethod();
        printMetaClassGetModifiedPropertiesMethod();
    }

    protected void printMetaClassGetNameMethod() {
        iprint("@Override%n");
        iprint("public String getName() {%n");
        iprint("    return __name;%n");
        iprint("}%n");
        print("%n");
    }

    protected void printMetaClassPreInsertMethod() {
        iprint("@Override%n");
        iprint("public void preInsert() {%n");
        iprint("    __listener.preInsert(__entity);%n");
        iprint("}%n");
        print("%n");
    }

    protected void printMetaClassPreUpdateMethod() {
        iprint("@Override%n");
        iprint("public void preUpdate() {%n");
        iprint("    __listener.preUpdate(__entity);%n");
        iprint("}%n");
        print("%n");
    }

    protected void printMetaClassPreDeleteMethod() {
        iprint("@Override%n");
        iprint("public void preDelete() {%n");
        iprint("    __listener.preDelete(__entity);%n");
        iprint("}%n");
        print("%n");
    }

    protected void printMetaClassGetPropertyMetasMethod() {
        iprint("@Override%n");
        iprint("public java.util.List<%1$s<?>> getPropertyMetas() {%n",
                org.seasar.doma.jdbc.entity.EntityPropertyMeta.class.getName());
        indent();
        iprint("if (__entityProperties == null) {%n");
        indent();
        iprint(
                "java.util.List<%1$s<?>> __list = new java.util.ArrayList<%1$s<?>>();%n",
                org.seasar.doma.jdbc.entity.EntityPropertyMeta.class.getName());
        for (EntityPropertyMeta pm : entityMeta.getAllPropertyMetas()) {
            if (pm.isTrnsient()) {
                continue;
            }
            iprint("__list.add(%1$s);%n", pm.getName());
        }
        iprint("__entityProperties = java.util.Collections.unmodifiableList(__list);%n");
        unindent();
        iprint("}%n");
        iprint("return __entityProperties;%n");
        unindent();
        iprint("}%n");
        print("%n");
    }

    protected void printMetaClassGetPropertyMetaMethod() {
        iprint("@Override%n");
        iprint("public %1$s<?> getPropertyMeta(String __name) {%n",
                org.seasar.doma.jdbc.entity.EntityPropertyMeta.class.getName());
        indent();
        iprint("if (__entityPropertyMap == null) {%n");
        indent();
        iprint(
                "java.util.Map<String, %1$s<?>> __map = new java.util.HashMap<String, %1$s<?>>();%n",
                org.seasar.doma.jdbc.entity.EntityPropertyMeta.class.getName());
        for (EntityPropertyMeta pm : entityMeta.getAllPropertyMetas()) {
            if (pm.isTrnsient()) {
                continue;
            }
            iprint("__map.put(\"%1$s\", %1$s);%n", pm.getName());
        }
        iprint("__entityPropertyMap = java.util.Collections.unmodifiableMap(__map);%n");
        unindent();
        iprint("}%n");
        iprint("return __entityPropertyMap.get(__name);%n");
        unindent();
        iprint("}%n");
        print("%n");
    }

    protected void printMetaClassGetGeneratedIdPropertyMethod() {
        iprint("@Override%n");
        iprint("public %1$s<?> getGeneratedIdProperty() {%n",
                GeneratedIdPropertyMeta.class.getName());
        String idName = "null";
        if (entityMeta.hasGeneratedIdPropertyMeta()) {
            EntityPropertyMeta pm = entityMeta.getGeneratedIdPropertyMeta();
            idName = pm.getName();
        }
        iprint("    return %1$s;%n", idName);
        iprint("}%n");
        print("%n");
    }

    protected void printMetaClassGetVersionPropertyMethod() {
        iprint("@Override%n");
        iprint("public %1$s<?> getVersionProperty() {%n",
                VersionPropertyMeta.class.getName());
        String versionName = "null";
        if (entityMeta.hasVersionPropertyMeta()) {
            EntityPropertyMeta pm = entityMeta.getVersionPropertyMeta();
            versionName = pm.getName();
        }
        iprint("    return %1$s;%n", versionName);
        iprint("}%n");
        print("%n");
    }

    protected void printMetaClassRefreshEntityMethod() {
        iprint("@Override%n");
        iprint("public void refreshEntity() {%n");
        iprint("    refreshEntityInternal();%n");
        iprint("}%n");
        print("%n");
    }

    protected void printMetaClassRefreshEntityInternalMethod() {
        iprint("public void refreshEntityInternal() {%n");
        for (EntityPropertyMeta pm : entityMeta.getAllPropertyMetas()) {
            if (pm.isId() || pm.isVersion()) {
                iprint("    __entity.%1$s = %1$s.getWrapper().get();%n", pm
                        .getName());
            }
        }
        iprint("}%n");
        print("%n");
    }

    protected void printMetaClassGetEntityMethod() {
        iprint("@Override%n");
        iprint("public %1$s getEntity() {%n", entityMeta.getEntityTypeName());
        iprint("    refreshEntityInternal();%n");
        iprint("    return __entity;%n");
        iprint("}%n");
        print("%n");
    }

    protected void printMetaClassGetEntityClassMethod() {
        iprint("@Override%n");
        iprint("public Class<%1$s> getEntityClass() {%n", entityMeta
                .getEntityTypeName());
        iprint("    return %1$s.class;%n", entityMeta.getEntityTypeName());
        iprint("}%n");
        print("%n");
    }

    protected void printMetaClassGetModifiedPropertiesMethod() {
        iprint("@Override%n");
        iprint("public java.util.Set<String> getModifiedProperties() {%n");
        iprint("    return __modifiedProperties;%n");
        iprint("}%n");
        print("%n");
    }

    protected class IdGeneratorGenerator implements
            IdGeneratorMetaVisitor<Void, Void> {

        @Override
        public Void visistIdentityIdGeneratorMeta(IdentityIdGeneratorMeta m,
                Void p) {
            iprint("private static final %1$s __idGenerator = new %1$s();%n", m
                    .getIdGeneratorClassName());
            return null;
        }

        @Override
        public Void visistSequenceIdGeneratorMeta(SequenceIdGeneratorMeta m,
                Void p) {
            iprint("private static final %1$s __idGenerator = new %1$s();%n", m
                    .getIdGeneratorClassName());
            iprint("static {%n");
            indent();
            iprint("__idGenerator.setQualifiedSequenceName(\"%1$s\");%n", m
                    .getQualifiedSequenceName());
            iprint("__idGenerator.setInitialValue(%1$s);%n", m
                    .getInitialValue());
            iprint("__idGenerator.setAllocationSize(%1$s);%n", m
                    .getAllocationSize());
            iprint("__idGenerator.initialize();%n");
            unindent();
            iprint("}%n");
            return null;
        }

        @Override
        public Void visistTableIdGeneratorMeta(TableIdGeneratorMeta m, Void p) {
            iprint("private static final %1$s __idGenerator = new %1$s();%n", m
                    .getIdGeneratorClassName());
            iprint("static {%n");
            indent();
            iprint("__idGenerator.setQualifiedTableName(\"%1$s\");%n", m
                    .getQualifiedTableName());
            iprint("__idGenerator.setInitialValue(%1$s);%n", m
                    .getInitialValue());
            iprint("__idGenerator.setAllocationSize(%1$s);%n", m
                    .getAllocationSize());
            iprint("__idGenerator.setPkColumnName(\"%1$s\");%n", m
                    .getPkColumnName());
            iprint("__idGenerator.setPkColumnValue(\"%1$s\");%n", m
                    .getPkColumnValue());
            iprint("__idGenerator.setValueColumnName(\"%1$s\");%n", m
                    .getValueColumnName());
            iprint("__idGenerator.initialize();%n");
            unindent();
            iprint("}%n");
            return null;
        }
    }

}
