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

import static org.seasar.doma.internal.util.Assertions.*;

import java.io.IOException;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.TypeElement;

import org.seasar.doma.internal.apt.meta.ColumnMeta;
import org.seasar.doma.internal.apt.meta.EntityMeta;
import org.seasar.doma.internal.apt.meta.IdGeneratorMeta;
import org.seasar.doma.internal.apt.meta.IdGeneratorMetaVisitor;
import org.seasar.doma.internal.apt.meta.IdentityIdGeneratorMeta;
import org.seasar.doma.internal.apt.meta.PropertyMeta;
import org.seasar.doma.internal.apt.meta.SequenceIdGeneratorMeta;
import org.seasar.doma.internal.apt.meta.TableIdGeneratorMeta;
import org.seasar.doma.internal.apt.meta.TableMeta;
import org.seasar.doma.internal.jdbc.AssignedIdProperty;
import org.seasar.doma.internal.jdbc.BasicProperty;
import org.seasar.doma.internal.jdbc.DomaAbstractEntity;
import org.seasar.doma.internal.jdbc.GeneratedIdProperty;
import org.seasar.doma.internal.jdbc.Property;
import org.seasar.doma.internal.jdbc.VersionProperty;
import org.seasar.doma.internal.jdbc.id.IdGenerator;

/**
 * 
 * @author taedium
 * 
 */
public class EntityGenerator extends AbstractGenerator {

    protected final EntityMeta entityMeta;

    public EntityGenerator(ProcessingEnvironment env,
            TypeElement entityElement, EntityMeta entityMeta)
            throws IOException {
        super(env, entityElement);
        assertNotNull(entityMeta);
        this.entityMeta = entityMeta;
    }

    public void generate() {
        printPackage();
        printClass();
    }

    protected void printPackage() {
        if (!packageName.isEmpty()) {
            print("package %1$s;%n", packageName);
            print("%n");
        }
    }

    protected void printClass() {
        printGenerated();
        print("public class %1$s extends %2$s<%3$s> implements %3$s {%n", simpleName, DomaAbstractEntity.class
                .getName(), entityMeta.getEntityElement().getQualifiedName());
        put("%n");
        indent();
        printFields();
        printConstructor();
        printMethods();
        unindent();
        print("}%n");
    }

    protected void printFields() {
        if (entityMeta.hasGeneratedIdPropertyMeta()) {
            PropertyMeta propertyMeta = entityMeta.getGeneratedIdPropertyMeta();
            IdGeneratorMeta idGeneratorMeta = propertyMeta.getIdGeneratorMeta();
            idGeneratorMeta.accept(new IdGeneratorGenerator(), null);
            put("%n");
        }
        print("private final %1$s __listener = new %1$s();%n", entityMeta
                .getListenerType());
        put("%n");
        for (PropertyMeta pm : entityMeta.getAllPropertyMetas()) {
            if (pm.isTrnsient()) {
                print("private final %1$s %2$s = new %1$s();%n", pm
                        .getReturnTypeName(), pm.getName());
            } else {
                Class<?> propertyClass = getPropertyClass(pm);
                ColumnMeta cm = pm.getColumnMeta();
                String columnName = cm.getName();
                String quote = "\"";
                if (columnName == null) {
                    columnName = "null";
                    quote = "";
                }
                String format = "private final %1$s<%2$s> %3$s = new %1$s<%2$s>(\"%3$s\", %7$s%4$s%7$s, new %2$s(), %5$s, %6$s);%n";
                if (propertyClass == GeneratedIdProperty.class) {
                    format = "private final %1$s<%2$s> %3$s = new %1$s<%2$s>(\"%3$s\", %7$s%4$s%7$s, new %2$s(), %5$s, %6$s, __idGenerator);%n";
                }
                print(format, /* 1 */propertyClass.getName(), /* 2 */pm
                        .getReturnTypeName(), /* 3 */pm.getName(), /* 4 */columnName, /* 5 */cm
                        .isInsertable(), /* 6 */cm.isUpdatable(), /* 7 */quote);
            }
            put("%n");
        }
        print("private final String __name = \"%1$s\";%n", entityMeta.getName());
        put("%n");
        print("private java.util.List<%1$s<?>> __properties;%n", Property.class
                .getName());
        put("%n");
        print("private java.util.Map<String, %1$s<?>> __propertyMap;%n", Property.class
                .getName());
        put("%n");
    }

    protected Class<?> getPropertyClass(PropertyMeta propertyMeta) {
        if (propertyMeta.isId()) {
            if (propertyMeta.getIdGeneratorMeta() != null) {
                return GeneratedIdProperty.class;
            }
            return AssignedIdProperty.class;
        } else if (propertyMeta.isVersion()) {
            return VersionProperty.class;
        }
        return BasicProperty.class;
    }

    protected void printConstructor() {
        print("public %1$s() {%n", simpleName);
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
        print("    super(%2$s%1$s%2$s, %4$s%3$s%4$s, %6$s%5$s%6$s);%n", catalog, catalogQuote, schema, schemaQuote, table, tableQuote);
        print("}%n");
        put("%n");
    }

    protected void printMethods() {
        for (PropertyMeta pm : entityMeta.getAllPropertyMetas()) {
            if (pm.isTrnsient()) {
                print("@Override%n");
                print("public %1$s %2$s() {%n", pm.getReturnTypeName(), pm
                        .getName());
                print("    return %1$s;%n", pm.getName());
                print("}%n");
                put("%n");
            } else {
                print("@Override%n");
                print("public %1$s %2$s() {%n", pm.getReturnTypeName(), pm
                        .getName());
                print("    return %1$s.getDomain();%n", pm.getName());
                print("}%n");
                put("%n");
            }
        }
        print("@Override%n");
        print("public String __getName() {%n");
        print("    return __name;%n");
        print("}%n");
        put("%n");
        print("@Override%n");
        print("public %1$s __asInterface() {%n", entityMeta.getEntityElement()
                .getQualifiedName());
        print("    return this;%n");
        print("}%n");
        put("%n");
        print("@Override%n");
        print("public void __preInsert() {%n");
        print("    __listener.preInsert(this);%n");
        print("}%n");
        put("%n");
        print("@Override%n");
        print("public void __preUpdate() {%n");
        print("    __listener.preUpdate(this);%n");
        print("}%n");
        put("%n");
        print("@Override%n");
        print("public void __preDelete() {%n");
        print("    __listener.preDelete(this);%n");
        print("}%n");
        put("%n");
        print("@Override%n");
        print("public java.util.List<%1$s<?>> __getProperties() {%n", Property.class
                .getName());
        indent();
        print("if (__properties == null) {%n");
        indent();
        print("java.util.List<%1$s<?>> list = new java.util.ArrayList<%1$s<?>>();%n", Property.class
                .getName());
        for (PropertyMeta pm : entityMeta.getAllPropertyMetas()) {
            if (pm.isTrnsient()) {
                continue;
            }
            print("list.add(%1$s);%n", pm.getName());
        }
        print("__properties = java.util.Collections.unmodifiableList(list);%n");
        unindent();
        print("}%n");
        print("return __properties;%n");
        unindent();
        print("}%n");
        put("%n");
        print("@Override%n");
        print("public %1$s<?> __getPropertyByName(String propertyName) {%n", Property.class
                .getName());
        indent();
        print("if (__propertyMap == null) {%n");
        indent();
        print("java.util.Map<String, %1$s<?>> map = new java.util.HashMap<String, %1$s<?>>();%n", Property.class
                .getName());
        for (PropertyMeta pm : entityMeta.getAllPropertyMetas()) {
            if (pm.isTrnsient()) {
                continue;
            }
            print("map.put(\"%1$s\", %1$s);%n", pm.getName());
        }
        print("__propertyMap = java.util.Collections.unmodifiableMap(map);%n");
        unindent();
        print("}%n");
        print("return __propertyMap.get(propertyName);%n");
        unindent();
        print("}%n");
        put("%n");
        print("@Override%n");
        print("public %1$s<?> __getGeneratedIdProperty() {%n", GeneratedIdProperty.class
                .getName());
        String idName = "null";
        if (entityMeta.hasGeneratedIdPropertyMeta()) {
            PropertyMeta pm = entityMeta.getGeneratedIdPropertyMeta();
            idName = pm.getName();
        }
        print("    return %1$s;%n", idName);
        print("}%n");
        put("%n");
        print("@Override%n");
        print("public %1$s<?> __getVersionProperty() {%n", VersionProperty.class
                .getName());
        String versionName = "null";
        if (entityMeta.hasVersionPropertyMeta()) {
            PropertyMeta pm = entityMeta.getVersionPropertyMeta();
            versionName = pm.getName();
        }
        print("    return %1$s;%n", versionName);
        print("}%n");
        put("%n");
    }

    protected class IdGeneratorGenerator implements
            IdGeneratorMetaVisitor<Void, Void> {

        @Override
        public Void visistIdentityIdGeneratorMeta(IdentityIdGeneratorMeta m,
                Void p) {
            print("private static final %1$s __idGenerator = new %2$s();%n", IdGenerator.class
                    .getName(), m.getIdGeneratorClassName());
            return null;
        }

        @Override
        public Void visistSequenceIdGeneratorMeta(SequenceIdGeneratorMeta m,
                Void p) {
            print("private static final %1$s __idGenerator = new %2$s(\"%3$s\", %4$s, %5$s);%n", IdGenerator.class
                    .getName(), m.getIdGeneratorClassName(), m
                    .getQualifiedSequenceName(), m.getInitialValue(), m
                    .getAllocationSize());
            return null;
        }

        @Override
        public Void visistTableIdGeneratorMeta(TableIdGeneratorMeta m, Void p) {
            print("private static final %1$s __idGenerator = new %2$s(\"%3$s\", \"%4$s\", \"%5$s\", \"%6$s\", %7$s, %8$s);%n", IdGenerator.class
                    .getName(), m.getIdGeneratorClassName(), m
                    .getQualifiedTableName(), m.getPkColumnName(), m
                    .getValueColumnName(), m.getPkColumnValue(), m
                    .getInitialValue(), m.getAllocationSize());
            return null;
        }
    }

}
