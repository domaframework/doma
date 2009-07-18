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
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.TypeElement;

import org.seasar.doma.domain.ArrayListDomain;
import org.seasar.doma.entity.AssignedIdProperty;
import org.seasar.doma.entity.BasicProperty;
import org.seasar.doma.entity.DomaAbstractEntity;
import org.seasar.doma.entity.EntityProperty;
import org.seasar.doma.entity.GeneratedIdProperty;
import org.seasar.doma.entity.TransientProperty;
import org.seasar.doma.entity.VersionProperty;
import org.seasar.doma.internal.apt.meta.ColumnMeta;
import org.seasar.doma.internal.apt.meta.EntityMeta;
import org.seasar.doma.internal.apt.meta.EntityPropertyMeta;
import org.seasar.doma.internal.apt.meta.IdGeneratorMeta;
import org.seasar.doma.internal.apt.meta.IdGeneratorMetaVisitor;
import org.seasar.doma.internal.apt.meta.IdentityIdGeneratorMeta;
import org.seasar.doma.internal.apt.meta.SequenceIdGeneratorMeta;
import org.seasar.doma.internal.apt.meta.TableIdGeneratorMeta;
import org.seasar.doma.internal.apt.meta.TableMeta;

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
        print("public class %1$s extends %2$s<%3$s> implements %3$s, %4$s {%n", simpleName, DomaAbstractEntity.class
                .getName(), entityMeta.getEntityElement().getQualifiedName(), Serializable.class
                .getName());
        put("%n");
        indent();
        printFields();
        printConstructor();
        printMethods();
        unindent();
        print("}%n");
    }

    protected void printFields() {
        printSerialVersionUIDField();
        printGeneratedIdPropertyField();
        printListenerField();
        printPropertyField();
        printNameField();
        printPropertiesField();
        printPropertyMapField();
    }

    protected void printSerialVersionUIDField() {
        print("private static final long serialVersionUID = %1$sL;%n", entityMeta
                .getSerialVersionUID());
        put("%n");
    }

    protected void printGeneratedIdPropertyField() {
        if (entityMeta.hasGeneratedIdPropertyMeta()) {
            EntityPropertyMeta propertyMeta = entityMeta
                    .getGeneratedIdPropertyMeta();
            IdGeneratorMeta idGeneratorMeta = propertyMeta.getIdGeneratorMeta();
            idGeneratorMeta.accept(new IdGeneratorGenerator(), null);
            put("%n");
        }
    }

    protected void printListenerField() {
        print("private static final %1$s __listener = new %1$s();%n", entityMeta
                .getListenerType());
        put("%n");
    }

    protected void printPropertyField() {
        for (EntityPropertyMeta pm : entityMeta.getAllPropertyMetas()) {
            if (pm.isTrnsient()) {
                if (pm.isListReturnType()) {
                    print("private transient %1$s<%2$s<%3$s>> %4$s = new %1$s<%2$s<%3$s>>(\"%4$s\", new %2$s<%3$s>(new %5$s()));%n", /* 1 */TransientProperty.class
                            .getName(), /* 2 */ArrayListDomain.class.getName(),/* 3 */pm
                            .getReturnElementTypeName(), /* 4 */pm.getName(), /* 5 */pm
                            .getReturnTypeName());
                } else {
                    print("private transient %1$s<%2$s> %3$s = new %1$s<%2$s>(\"%3$s\", new %2$s());%n", /* 1 */TransientProperty.class
                            .getName(), /* 2 */pm.getReturnTypeName(), /* 3 */pm
                            .getName());
                }
            } else {
                Class<?> propertyClass = getPropertyClass(pm);
                ColumnMeta cm = pm.getColumnMeta();
                String columnName = cm.getName();
                String quote = "\"";
                if (columnName == null) {
                    columnName = "null";
                    quote = "";
                }
                String format = "private transient %1$s<%2$s> %3$s = new %1$s<%2$s>(\"%3$s\", %7$s%4$s%7$s, new %2$s(), %5$s, %6$s);%n";
                if (propertyClass == GeneratedIdProperty.class) {
                    format = "private transient %1$s<%2$s> %3$s = new %1$s<%2$s>(\"%3$s\", %7$s%4$s%7$s, new %2$s(), %5$s, %6$s, __idGenerator);%n";
                }
                print(format, /* 1 */propertyClass.getName(), /* 2 */pm
                        .getReturnTypeName(), /* 3 */pm.getName(), /* 4 */columnName, /* 5 */cm
                        .isInsertable(), /* 6 */cm.isUpdatable(), /* 7 */quote);
            }
            put("%n");
        }
    }

    protected void printNameField() {
        print("private final String __name = \"%1$s\";%n", entityMeta.getName());
        put("%n");
    }

    protected void printPropertiesField() {
        print("private transient java.util.List<%1$s<?>> __entityProperties;%n", EntityProperty.class
                .getName());
        put("%n");
    }

    protected void printPropertyMapField() {
        print("private transient java.util.Map<String, %1$s<?>> __entityPropertyMap;%n", EntityProperty.class
                .getName());
        put("%n");
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
        printPropertyMethod();
        printGetNameMethod();
        printAsInterfaceMethod();
        printPreInsertMethod();
        printPreUpdateMethod();
        printPreDeleteMethod();
        printGetEntityPropertiesMethod();
        printGetEntityPropertyMethod();
        printGetGeneratedIdProperty();
        printGetVersionProperty();
        printToStringMethod();
        printReadObjectMethod();
        printWriteObjectMethod();
    }

    protected void printPropertyMethod() {
        for (EntityPropertyMeta pm : entityMeta.getAllPropertyMetas()) {
            print("@Override%n");
            print("public %1$s %2$s() {%n", pm.getReturnTypeName(), pm
                    .getName());
            if (pm.isTrnsient() && pm.isListReturnType()) {
                print("    return %1$s.getDomain().get();%n", pm.getName());
            } else {
                print("    return %1$s.getDomain();%n", pm.getName());
            }
            print("}%n");
            put("%n");
        }
    }

    protected void printGetNameMethod() {
        print("@Override%n");
        print("public String __getName() {%n");
        print("    return __name;%n");
        print("}%n");
        put("%n");
    }

    protected void printAsInterfaceMethod() {
        print("@Override%n");
        print("public %1$s __asInterface() {%n", entityMeta.getEntityElement()
                .getQualifiedName());
        print("    return this;%n");
        print("}%n");
        put("%n");
    }

    protected void printPreInsertMethod() {
        print("@Override%n");
        print("public void __preInsert() {%n");
        print("    __listener.preInsert(this);%n");
        print("}%n");
        put("%n");
    }

    protected void printPreUpdateMethod() {
        print("@Override%n");
        print("public void __preUpdate() {%n");
        print("    __listener.preUpdate(this);%n");
        print("}%n");
        put("%n");
    }

    protected void printPreDeleteMethod() {
        print("@Override%n");
        print("public void __preDelete() {%n");
        print("    __listener.preDelete(this);%n");
        print("}%n");
        put("%n");
    }

    protected void printGetEntityPropertiesMethod() {
        print("@Override%n");
        print("public java.util.List<%1$s<?>> __getEntityProperties() {%n", EntityProperty.class
                .getName());
        indent();
        print("if (__entityProperties == null) {%n");
        indent();
        print("java.util.List<%1$s<?>> __list = new java.util.ArrayList<%1$s<?>>();%n", EntityProperty.class
                .getName());
        for (EntityPropertyMeta pm : entityMeta.getAllPropertyMetas()) {
            print("__list.add(%1$s);%n", pm.getName());
        }
        print("__entityProperties = java.util.Collections.unmodifiableList(__list);%n");
        unindent();
        print("}%n");
        print("return __entityProperties;%n");
        unindent();
        print("}%n");
        put("%n");
    }

    protected void printGetEntityPropertyMethod() {
        print("@Override%n");
        print("public %1$s<?> __getEntityProperty(String __name) {%n", EntityProperty.class
                .getName());
        indent();
        print("if (__entityPropertyMap == null) {%n");
        indent();
        print("java.util.Map<String, %1$s<?>> __map = new java.util.HashMap<String, %1$s<?>>();%n", EntityProperty.class
                .getName());
        for (EntityPropertyMeta pm : entityMeta.getAllPropertyMetas()) {
            print("__map.put(\"%1$s\", %1$s);%n", pm.getName());
        }
        print("__entityPropertyMap = java.util.Collections.unmodifiableMap(__map);%n");
        unindent();
        print("}%n");
        print("return __entityPropertyMap.get(__name);%n");
        unindent();
        print("}%n");
        put("%n");
    }

    protected void printGetGeneratedIdProperty() {
        print("@Override%n");
        print("public %1$s<?> __getGeneratedIdProperty() {%n", GeneratedIdProperty.class
                .getName());
        String idName = "null";
        if (entityMeta.hasGeneratedIdPropertyMeta()) {
            EntityPropertyMeta pm = entityMeta.getGeneratedIdPropertyMeta();
            idName = pm.getName();
        }
        print("    return %1$s;%n", idName);
        print("}%n");
        put("%n");
    }

    protected void printToStringMethod() {
        print("@Override%n");
        print("public String toString() {%n");
        StringBuilder buf = new StringBuilder(200);
        buf.append("\"");
        buf.append(simpleName);
        buf.append(" [");
        java.util.List<EntityPropertyMeta> propertyMetas = entityMeta
                .getAllPropertyMetas();
        if (!propertyMetas.isEmpty()) {
            for (EntityPropertyMeta pm : entityMeta.getAllPropertyMetas()) {
                buf.append(pm.getName());
                buf.append("=\" + ");
                buf.append(pm.getName());
                buf.append(" + \", ");
            }
            buf.setLength(buf.length() - 2);
        }
        buf.append("]\"");
        print("    return %1$s;%n", buf);
        print("}%n");
        put("%n");
    }

    protected void printGetVersionProperty() {
        print("@Override%n");
        print("public %1$s<?> __getVersionProperty() {%n", VersionProperty.class
                .getName());
        String versionName = "null";
        if (entityMeta.hasVersionPropertyMeta()) {
            EntityPropertyMeta pm = entityMeta.getVersionPropertyMeta();
            versionName = pm.getName();
        }
        print("    return %1$s;%n", versionName);
        print("}%n");
        put("%n");
    }

    protected void printReadObjectMethod() {
        print("@SuppressWarnings(\"unchecked\")%n");
        print("private void readObject(%1$s inputStream) throws %2$s, %3$s {%n", ObjectInputStream.class
                .getName(), IOException.class.getName(), ClassNotFoundException.class
                .getName());
        indent();
        print("inputStream.defaultReadObject();%n");
        for (EntityPropertyMeta pm : entityMeta.getAllPropertyMetas()) {
            if (pm.isTrnsient()) {
                if (pm.isListReturnType()) {
                    print("%4$s = new %1$s<%2$s<%3$s>>(\"%4$s\", (%2$s<%3$s>)inputStream.readObject());%n", /* 1 */TransientProperty.class
                            .getName(), /* 2 */ArrayListDomain.class.getName(),/* 3 */pm
                            .getReturnElementTypeName(), /* 4 */pm.getName(), /* 5 */pm
                            .getReturnTypeName());
                } else {
                    print("%3$s = new %1$s<%2$s>(\"%3$s\", (%2$s)inputStream.readObject());%n", /* 1 */TransientProperty.class
                            .getName(), /* 2 */pm.getReturnTypeName(), /* 3 */pm
                            .getName());
                }
            } else {
                Class<?> propertyClass = getPropertyClass(pm);
                ColumnMeta cm = pm.getColumnMeta();
                String columnName = cm.getName();
                String quote = "\"";
                if (columnName == null) {
                    columnName = "null";
                    quote = "";
                }
                String format = "%3$s = new %1$s<%2$s>(\"%3$s\", %7$s%4$s%7$s, (%2$s)inputStream.readObject(), %5$s, %6$s);%n";
                if (propertyClass == GeneratedIdProperty.class) {
                    format = "%3$s = new %1$s<%2$s>(\"%3$s\", %7$s%4$s%7$s, (%2$s)inputStream.readObject(), %5$s, %6$s, __idGenerator);%n";
                }
                print(format, /* 1 */propertyClass.getName(), /* 2 */pm
                        .getReturnTypeName(), /* 3 */pm.getName(), /* 4 */columnName, /* 5 */cm
                        .isInsertable(), /* 6 */cm.isUpdatable(), /* 7 */quote);
            }
        }
        unindent();
        print("}%n");
        put("%n");
    }

    protected void printWriteObjectMethod() {
        print("private void writeObject(%1$s outputStream) throws %2$s {%n", ObjectOutputStream.class
                .getName(), IOException.class.getName());
        indent();
        print("outputStream.defaultWriteObject();%n");
        for (EntityPropertyMeta pm : entityMeta.getAllPropertyMetas()) {
            print("outputStream.writeObject(%1$s.getDomain());%n", pm.getName());
        }
        unindent();
        print("}%n");
        put("%n");
    }

    protected Class<?> getPropertyClass(EntityPropertyMeta propertyMeta) {
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

    protected class IdGeneratorGenerator implements
            IdGeneratorMetaVisitor<Void, Void> {

        @Override
        public Void visistIdentityIdGeneratorMeta(IdentityIdGeneratorMeta m,
                Void p) {
            print("private static final %1$s __idGenerator = new %1$s();%n", m
                    .getIdGeneratorClassName());
            return null;
        }

        @Override
        public Void visistSequenceIdGeneratorMeta(SequenceIdGeneratorMeta m,
                Void p) {
            print("private static final %1$s __idGenerator = new %1$s();%n", m
                    .getIdGeneratorClassName());
            print("static {%n");
            indent();
            print("__idGenerator.setQualifiedSequenceName(\"%1$s\");%n", m
                    .getQualifiedSequenceName());
            print("__idGenerator.setInitialValue(%1$s);%n", m.getInitialValue());
            print("__idGenerator.setAllocationSize(%1$s);%n", m
                    .getAllocationSize());
            print("__idGenerator.initialize();%n");
            unindent();
            print("}%n");
            return null;
        }

        @Override
        public Void visistTableIdGeneratorMeta(TableIdGeneratorMeta m, Void p) {
            print("private static final %1$s __idGenerator = new %1$s();%n", m
                    .getIdGeneratorClassName());
            print("static {%n");
            indent();
            print("__idGenerator.setQualifiedTableName(\"%1$s\");%n", m
                    .getQualifiedTableName());
            print("__idGenerator.setInitialValue(%1$s);%n", m.getInitialValue());
            print("__idGenerator.setAllocationSize(%1$s);%n", m
                    .getAllocationSize());
            print("__idGenerator.setPkColumnName(\"%1$s\");%n", m
                    .getPkColumnName());
            print("__idGenerator.setPkColumnValue(\"%1$s\");%n", m
                    .getPkColumnValue());
            print("__idGenerator.setValueColumnName(\"%1$s\");%n", m
                    .getValueColumnName());
            print("__idGenerator.initialize();%n");
            unindent();
            print("}%n");
            return null;
        }
    }

}
