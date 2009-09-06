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
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Iterator;
import java.util.Map;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.TypeElement;

import org.seasar.doma.domain.BuiltinArrayListDomain;
import org.seasar.doma.internal.apt.meta.ColumnMeta;
import org.seasar.doma.internal.apt.meta.EntityDelegateMeta;
import org.seasar.doma.internal.apt.meta.EntityMeta;
import org.seasar.doma.internal.apt.meta.EntityPropertyMeta;
import org.seasar.doma.internal.apt.meta.IdGeneratorMeta;
import org.seasar.doma.internal.apt.meta.IdGeneratorMetaVisitor;
import org.seasar.doma.internal.apt.meta.IdentityIdGeneratorMeta;
import org.seasar.doma.internal.apt.meta.SequenceIdGeneratorMeta;
import org.seasar.doma.internal.apt.meta.TableIdGeneratorMeta;
import org.seasar.doma.internal.apt.meta.TableMeta;
import org.seasar.doma.jdbc.entity.AssignedIdProperty;
import org.seasar.doma.jdbc.entity.BasicProperty;
import org.seasar.doma.jdbc.entity.DomaAbstractEntity;
import org.seasar.doma.jdbc.entity.EntityProperty;
import org.seasar.doma.jdbc.entity.GeneratedIdProperty;
import org.seasar.doma.jdbc.entity.TransientProperty;
import org.seasar.doma.jdbc.entity.VersionProperty;

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
        super(env, entityElement, createQualifiedName(env, entityElement,
                Options.getEntitySubpackage(env), Options.getEntitySuffix(env)));
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
        iprint(
                "public class %1$s extends %2$s<%3$s> implements %3$s, %4$s {%n",
                simpleName, DomaAbstractEntity.class.getName(), entityMeta
                        .getEntityElement().getQualifiedName(),
                Serializable.class.getName());
        print("%n");
        indent();
        printFields();
        printConstructor();
        printMethods();
        unindent();
        iprint("}%n");
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
        iprint("private static final long serialVersionUID = %1$sL;%n",
                entityMeta.getSerialVersionUID());
        print("%n");
    }

    protected void printGeneratedIdPropertyField() {
        if (entityMeta.hasGeneratedIdPropertyMeta()) {
            EntityPropertyMeta propertyMeta = entityMeta
                    .getGeneratedIdPropertyMeta();
            IdGeneratorMeta idGeneratorMeta = propertyMeta.getIdGeneratorMeta();
            idGeneratorMeta.accept(new IdGeneratorGenerator(), null);
            print("%n");
        }
    }

    protected void printListenerField() {
        iprint("private static final %1$s __listener = new %1$s();%n",
                entityMeta.getListenerType());
        print("%n");
    }

    protected void printPropertyField() {
        for (EntityPropertyMeta pm : entityMeta.getAllPropertyMetas()) {
            if (pm.isTrnsient()) {
                if (pm.isListReturnType()) {
                    iprint(
                            "private transient %1$s<%2$s<%3$s>> %4$s = new %1$s<%2$s<%3$s>>(\"%4$s\", new %2$s<%3$s>(new %5$s()));%n", /* 1 */
                            TransientProperty.class.getName(), /* 2 */
                            BuiltinArrayListDomain.class.getName(),/* 3 */pm
                                    .getReturnElementTypeName(), /* 4 */pm
                                    .getName(), /* 5 */pm.getReturnTypeName());
                } else {
                    iprint(
                            "private transient %1$s<%2$s> %3$s = new %1$s<%2$s>(\"%3$s\", new %2$s());%n", /* 1 */
                            TransientProperty.class.getName(), /* 2 */pm
                                    .getReturnTypeName(), /* 3 */pm.getName());
                }
            } else {
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
                                "private transient %1$s<%2$s> %3$s = new %1$s<%2$s>(\"%3$s\", %5$s%4$s%5$s, new %2$s(), __idGenerator);%n", /* 1 */
                                GeneratedIdProperty.class.getName(), /* 2 */pm
                                        .getReturnTypeName(), /* 3 */pm
                                        .getName(), /* 4 */
                                columnName, /* 5 */quote);
                    } else {
                        iprint(
                                "private transient %1$s<%2$s> %3$s = new %1$s<%2$s>(\"%3$s\", %5$s%4$s%5$s, new %2$s());%n", /* 1 */
                                AssignedIdProperty.class.getName(), /* 2 */pm
                                        .getReturnTypeName(), /* 3 */pm
                                        .getName(), /* 4 */
                                columnName, /* 5 */quote);
                    }
                } else if (pm.isVersion()) {
                    iprint(
                            "private transient %1$s<%2$s> %3$s = new %1$s<%2$s>(\"%3$s\", %5$s%4$s%5$s, new %2$s());%n", /* 1 */
                            VersionProperty.class.getName(), /* 2 */pm
                                    .getReturnTypeName(), /* 3 */pm.getName(), /* 4 */
                            columnName, /* 5 */quote);
                } else {
                    iprint(
                            "private transient %1$s<%2$s> %3$s = new %1$s<%2$s>(\"%3$s\", %7$s%4$s%7$s, new %2$s(), %5$s, %6$s);%n", /* 1 */
                            BasicProperty.class.getName(), /* 2 */pm
                                    .getReturnTypeName(), /* 3 */pm.getName(), /* 4 */
                            columnName, /* 5 */cm.isInsertable(), /* 6 */cm
                                    .isUpdatable(), /* 7 */quote);
                }
            }
            print("%n");
        }
    }

    protected void printNameField() {
        iprint("private final String __name = \"%1$s\";%n", entityMeta
                .getName());
        print("%n");
    }

    protected void printPropertiesField() {
        iprint(
                "private transient java.util.List<%1$s<?>> __entityProperties;%n",
                EntityProperty.class.getName());
        print("%n");
    }

    protected void printPropertyMapField() {
        iprint(
                "private transient java.util.Map<String, %1$s<?>> __entityPropertyMap;%n",
                EntityProperty.class.getName());
        print("%n");
    }

    protected void printConstructor() {
        iprint("public %1$s() {%n", simpleName);
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
        iprint("}%n");
        print("%n");
    }

    protected void printMethods() {
        printPropertyMethod();
        printDelegateMethod();
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
            iprint("@Override%n");
            iprint("public %1$s %2$s() {%n", pm.getReturnTypeName(), pm
                    .getName());
            if (pm.isTrnsient() && pm.isListReturnType()) {
                iprint("    return %1$s.getDomain().get();%n", pm.getName());
            } else {
                iprint("    return %1$s.getDomain();%n", pm.getName());
            }
            iprint("}%n");
            print("%n");
        }
    }

    protected void printDelegateMethod() {
        for (EntityDelegateMeta dm : entityMeta.getAllDelegateMetas()) {
            iprint("@Override%n");
            iprint("public %1$s %2$s(", dm.getReturnTypeName(), dm.getName());
            for (Iterator<Map.Entry<String, String>> it = dm
                    .getMethodParameters(); it.hasNext();) {
                Map.Entry<String, String> entry = it.next();
                String paramType = entry.getValue();
                String paramName = entry.getKey();
                print("%1$s %2$s", paramType, paramName);
                if (it.hasNext()) {
                    print(", ");
                }
            }
            print(") {%n");
            indent();
            iprint("%1$s __delegate = new %1$s(this);%n", dm.getTargetType());
            if ("void".equals(dm.getReturnTypeName())) {
                iprint("");
            } else {
                iprint("return ");
            }
            print("__delegate.%1$s(", dm.getName());
            for (Iterator<Map.Entry<String, String>> it = dm
                    .getMethodParameters(); it.hasNext();) {
                Map.Entry<String, String> entry = it.next();
                String paramName = entry.getKey();
                print("%1$s", paramName);
                if (it.hasNext()) {
                    print(", ");
                }
            }
            print(");%n");
            unindent();
            iprint("}%n");
            print("%n");
        }
    }

    protected void printGetNameMethod() {
        iprint("@Override%n");
        iprint("public String __getName() {%n");
        iprint("    return __name;%n");
        iprint("}%n");
        print("%n");
    }

    protected void printAsInterfaceMethod() {
        iprint("@Override%n");
        iprint("public %1$s __asInterface() {%n", entityMeta.getEntityElement()
                .getQualifiedName());
        iprint("    return this;%n");
        iprint("}%n");
        print("%n");
    }

    protected void printPreInsertMethod() {
        iprint("@Override%n");
        iprint("public void __preInsert() {%n");
        iprint("    __listener.preInsert(this);%n");
        iprint("}%n");
        print("%n");
    }

    protected void printPreUpdateMethod() {
        iprint("@Override%n");
        iprint("public void __preUpdate() {%n");
        iprint("    __listener.preUpdate(this);%n");
        iprint("}%n");
        print("%n");
    }

    protected void printPreDeleteMethod() {
        iprint("@Override%n");
        iprint("public void __preDelete() {%n");
        iprint("    __listener.preDelete(this);%n");
        iprint("}%n");
        print("%n");
    }

    protected void printGetEntityPropertiesMethod() {
        iprint("@Override%n");
        iprint("public java.util.List<%1$s<?>> __getEntityProperties() {%n",
                EntityProperty.class.getName());
        indent();
        iprint("if (__entityProperties == null) {%n");
        indent();
        iprint(
                "java.util.List<%1$s<?>> __list = new java.util.ArrayList<%1$s<?>>();%n",
                EntityProperty.class.getName());
        for (EntityPropertyMeta pm : entityMeta.getAllPropertyMetas()) {
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

    protected void printGetEntityPropertyMethod() {
        iprint("@Override%n");
        iprint("public %1$s<?> __getEntityProperty(String __name) {%n",
                EntityProperty.class.getName());
        indent();
        iprint("if (__entityPropertyMap == null) {%n");
        indent();
        iprint(
                "java.util.Map<String, %1$s<?>> __map = new java.util.HashMap<String, %1$s<?>>();%n",
                EntityProperty.class.getName());
        for (EntityPropertyMeta pm : entityMeta.getAllPropertyMetas()) {
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

    protected void printGetGeneratedIdProperty() {
        iprint("@Override%n");
        iprint("public %1$s<?> __getGeneratedIdProperty() {%n",
                GeneratedIdProperty.class.getName());
        String idName = "null";
        if (entityMeta.hasGeneratedIdPropertyMeta()) {
            EntityPropertyMeta pm = entityMeta.getGeneratedIdPropertyMeta();
            idName = pm.getName();
        }
        iprint("    return %1$s;%n", idName);
        iprint("}%n");
        print("%n");
    }

    protected void printToStringMethod() {
        iprint("@Override%n");
        iprint("public String toString() {%n");
        StringBuilder buf = new StringBuilder(200);
        buf.append("\"");
        buf.append(simpleName);
        buf.append(" [");
        java.util.List<EntityPropertyMeta> propertyMetas = entityMeta
                .getAllPropertyMetas();
        if (!propertyMetas.isEmpty()) {
            for (EntityPropertyMeta pm : propertyMetas) {
                buf.append(pm.getName());
                buf.append("=\" + ");
                buf.append(pm.getName());
                buf.append(" + \", ");
            }
            buf.setLength(buf.length() - 2);
        }
        buf.append("]\"");
        iprint("    return %1$s;%n", buf);
        iprint("}%n");
        print("%n");
    }

    protected void printGetVersionProperty() {
        iprint("@Override%n");
        iprint("public %1$s<?> __getVersionProperty() {%n",
                VersionProperty.class.getName());
        String versionName = "null";
        if (entityMeta.hasVersionPropertyMeta()) {
            EntityPropertyMeta pm = entityMeta.getVersionPropertyMeta();
            versionName = pm.getName();
        }
        iprint("    return %1$s;%n", versionName);
        iprint("}%n");
        print("%n");
    }

    protected void printReadObjectMethod() {
        if (entityMeta.hasParametalizedDomain()) {
            iprint("@SuppressWarnings(\"unchecked\")%n");
        }
        iprint(
                "private void readObject(%1$s inputStream) throws %2$s, %3$s {%n",
                ObjectInputStream.class.getName(), IOException.class.getName(),
                ClassNotFoundException.class.getName());
        indent();
        iprint("inputStream.defaultReadObject();%n");
        for (EntityPropertyMeta pm : entityMeta.getAllPropertyMetas()) {
            if (pm.isTrnsient()) {
                if (pm.isListReturnType()) {
                    iprint(
                            "%4$s = new %1$s<%2$s<%3$s>>(\"%4$s\", (%2$s<%3$s>)inputStream.readObject());%n", /* 1 */
                            TransientProperty.class.getName(), /* 2 */
                            BuiltinArrayListDomain.class.getName(),/* 3 */pm
                                    .getReturnElementTypeName(), /* 4 */pm
                                    .getName(), /* 5 */pm.getReturnTypeName());
                } else {
                    iprint(
                            "%3$s = new %1$s<%2$s>(\"%3$s\", (%2$s)inputStream.readObject());%n", /* 1 */
                            TransientProperty.class.getName(), /* 2 */pm
                                    .getReturnTypeName(), /* 3 */pm.getName());
                }
            } else {
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
                                "%3$s = new %1$s<%2$s>(\"%3$s\", %5$s%4$s%5$s, (%2$s)inputStream.readObject(), __idGenerator);%n", /* 1 */
                                GeneratedIdProperty.class.getName(), /* 2 */pm
                                        .getReturnTypeName(), /* 3 */pm
                                        .getName(), /* 4 */
                                columnName, /* 5 */
                                quote);
                    } else {
                        iprint(
                                "%3$s = new %1$s<%2$s>(\"%3$s\", %5$s%4$s%5$s, (%2$s)inputStream.readObject());%n", /* 1 */
                                AssignedIdProperty.class.getName(), /* 2 */
                                pm.getReturnTypeName(), /* 3 */pm.getName(), /* 4 */
                                columnName, /* 5 */
                                quote);
                    }
                } else if (pm.isVersion()) {
                    iprint(
                            "%3$s = new %1$s<%2$s>(\"%3$s\", %5$s%4$s%5$s, (%2$s)inputStream.readObject());%n", /* 1 */
                            VersionProperty.class.getName(), /* 2 */
                            pm.getReturnTypeName(), /* 3 */pm.getName(), /* 4 */
                            columnName, /* 5 */
                            quote);
                } else {
                    iprint(
                            "%3$s = new %1$s<%2$s>(\"%3$s\", %7$s%4$s%7$s, (%2$s)inputStream.readObject(), %5$s, %6$s);%n", /* 1 */
                            BasicProperty.class.getName(), /* 2 */
                            pm.getReturnTypeName(), /* 3 */pm.getName(), /* 4 */
                            columnName, /* 5 */cm.isInsertable(), /* 6 */cm
                                    .isUpdatable(), /* 7 */
                            quote);
                }
            }
        }
        unindent();
        iprint("}%n");
        print("%n");
    }

    protected void printWriteObjectMethod() {
        iprint("private void writeObject(%1$s outputStream) throws %2$s {%n",
                ObjectOutputStream.class.getName(), IOException.class.getName());
        indent();
        iprint("outputStream.defaultWriteObject();%n");
        for (EntityPropertyMeta pm : entityMeta.getAllPropertyMetas()) {
            iprint("outputStream.writeObject(%1$s.getDomain());%n", pm
                    .getName());
        }
        unindent();
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
