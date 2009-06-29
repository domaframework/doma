package org.seasar.doma.internal.apt;

import static org.seasar.doma.internal.util.Assertions.*;

import javax.annotation.Generated;
import javax.annotation.processing.ProcessingEnvironment;

import org.seasar.doma.internal.ProductInfo;
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
public class EntityGenerator implements Generator,
        IdGeneratorMetaVisitor<Void, Printer> {

    protected final ProcessingEnvironment env;

    protected final String entityImplPackageName;

    protected final String entityImplSimpleName;

    protected final EntityMeta entityMeta;

    public EntityGenerator(ProcessingEnvironment env,
            String entityQualifiedName, EntityMeta entityMeta) {
        assertNotNull(env, entityQualifiedName, entityMeta);
        this.env = env;
        int pos = entityQualifiedName.lastIndexOf('.');
        if (pos > -1) {
            this.entityImplPackageName = entityQualifiedName.substring(0, pos);
            this.entityImplSimpleName = entityQualifiedName.substring(pos + 1);
        } else {
            this.entityImplPackageName = "";
            this.entityImplSimpleName = entityQualifiedName;
        }
        this.entityMeta = entityMeta;
    }

    public void generate(Printer p) {
        doPackage(p);
        doClass(p);
    }

    protected void doPackage(Printer p) {
        if (!entityImplPackageName.isEmpty()) {
            p.p("package %s;%n", entityImplPackageName);
            p.p("%n");
        }
    }

    protected void doClass(Printer p) {
        p
                .p("@%s(value = { \"%s\", \"%s\" }, date = \"%tF %<tT\")%n", Generated.class
                        .getName(), ProductInfo.getName(), ProductInfo
                        .getVersion(), Options.getDate(env));
        p
                .p("public class %1$s extends %2$s<%3$s> implements %3$s {%n", entityImplSimpleName, DomaAbstractEntity.class
                        .getName(), entityMeta.getEntityElement()
                        .getQualifiedName());
        p.p("%n");
        p.indent();
        doFields(p);
        doConstructor(p);
        doMethods(p);
        p.unindent();
        p.p("}%n");
    }

    protected void doFields(Printer p) {
        if (entityMeta.hasGeneratedIdPropertyMeta()) {
            PropertyMeta propertyMeta = entityMeta.getGeneratedIdPropertyMeta();
            IdGeneratorMeta idGeneratorMeta = propertyMeta.getIdGeneratorMeta();
            idGeneratorMeta.accept(this, p);
            p.p("%n");
        }
        p.p("private final %1$s __listener = new %1$s();%n", entityMeta
                .getListenerType());
        p.p("%n");
        for (PropertyMeta pm : entityMeta.getAllPropertyMetas()) {
            if (pm.isTrnsient()) {
                p.p("private final %1$s %2$s = new %1$s();%n", pm
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
                p
                        .p(format, /* 1 */propertyClass.getName(), /* 2 */pm
                                .getReturnTypeName(), /* 3 */pm.getName(), /* 4 */columnName, /* 5 */cm
                                .isInsertable(), /* 6 */cm.isUpdatable(), /* 7 */quote);
            }
            p.p("%n");
        }
        p.p("private final String __name = \"%s\";%n", entityMeta.getName());
        p.p("%n");
        p.p("private java.util.List<%s<?>> __properties;%n", Property.class
                .getName());
        p.p("%n");
        p
                .p("private java.util.Map<String, %s<?>> __propertyMap;%n", Property.class
                        .getName());
        p.p("%n");
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

    protected void doConstructor(Printer p) {
        p.p("public %1$s() {%n", entityImplSimpleName);
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
        p
                .p("    super(%2$s%1$s%2$s, %4$s%3$s%4$s, %6$s%5$s%6$s);%n", catalog, catalogQuote, schema, schemaQuote, table, tableQuote);
        p.p("}%n");
        p.p("%n");
    }

    protected void doMethods(Printer p) {
        for (PropertyMeta pm : entityMeta.getAllPropertyMetas()) {
            if (pm.isTrnsient()) {
                p.p("@Override%n");
                p.p("public %s %s() {%n", pm.getReturnTypeName(), pm.getName());
                p.p("    return %s;%n", pm.getName());
                p.p("}%n");
                p.p("%n");
            } else {
                p.p("@Override%n");
                p.p("public %s %s() {%n", pm.getReturnTypeName(), pm.getName());
                p.p("    return %s.getDomain();%n", pm.getName());
                p.p("}%n");
                p.p("%n");
            }
        }
        p.p("@Override%n");
        p.p("public String __getName() {%n");
        p.p("    return __name;%n");
        p.p("}%n");
        p.p("%n");
        p.p("@Override%n");
        p.p("public %s __asInterface() {%n", entityMeta.getEntityElement()
                .getQualifiedName());
        p.p("    return this;%n");
        p.p("}%n");
        p.p("%n");
        p.p("@Override%n");
        p.p("public void __preInsert() {%n");
        p.p("    __listener.preInsert(this);%n");
        p.p("}%n");
        p.p("%n");
        p.p("@Override%n");
        p.p("public void __preUpdate() {%n");
        p.p("    __listener.preUpdate(this);%n");
        p.p("}%n");
        p.p("%n");
        p.p("@Override%n");
        p.p("public void __preDelete() {%n");
        p.p("    __listener.preDelete(this);%n");
        p.p("}%n");
        p.p("%n");
        p.p("@Override%n");
        p
                .p("public java.util.List<%s<?>> __getProperties() {%n", Property.class
                        .getName());
        p.indent();
        p.p("if (__properties == null) {%n");
        p.indent();
        p
                .p("java.util.List<%1$s<?>> list = new java.util.ArrayList<%1$s<?>>();%n", Property.class
                        .getName());
        for (PropertyMeta pm : entityMeta.getAllPropertyMetas()) {
            if (pm.isTrnsient()) {
                continue;
            }
            p.p("list.add(%1$s);%n", pm.getName());
        }
        p.p("__properties = java.util.Collections.unmodifiableList(list);%n");
        p.unindent();
        p.p("}%n");
        p.p("return __properties;%n");
        p.unindent();
        p.p("}%n");
        p.p("%n");
        p.p("@Override%n");
        p
                .p("public %s<?> __getPropertyByName(String propertyName) {%n", Property.class
                        .getName());
        p.indent();
        p.p("if (__propertyMap == null) {%n");
        p.indent();
        p
                .p("java.util.Map<String, %1$s<?>> map = new java.util.HashMap<String, %1$s<?>>();%n", Property.class
                        .getName());
        for (PropertyMeta pm : entityMeta.getAllPropertyMetas()) {
            if (pm.isTrnsient()) {
                continue;
            }
            p.p("map.put(\"%1$s\", %1$s);%n", pm.getName());
        }
        p.p("__propertyMap = java.util.Collections.unmodifiableMap(map);%n");
        p.unindent();
        p.p("}%n");
        p.p("return __propertyMap.get(propertyName);%n");
        p.unindent();
        p.p("}%n");
        p.p("%n");
        p.p("@Override%n");
        p
                .p("public %s<?> __getGeneratedIdProperty() {%n", GeneratedIdProperty.class
                        .getName());
        String idName = "null";
        if (entityMeta.hasGeneratedIdPropertyMeta()) {
            PropertyMeta pm = entityMeta.getGeneratedIdPropertyMeta();
            idName = pm.getName();
        }
        p.p("    return %1$s;%n", idName);
        p.p("}%n");
        p.p("%n");
        p.p("@Override%n");
        p.p("public %s<?> __getVersionProperty() {%n", VersionProperty.class
                .getName());
        String versionName = "null";
        if (entityMeta.hasVersionPropertyMeta()) {
            PropertyMeta pm = entityMeta.getVersionPropertyMeta();
            versionName = pm.getName();
        }
        p.p("    return %1$s;%n", versionName);
        p.p("}%n");
        p.p("%n");
    }

    @Override
    public Void visistIdentityIdGeneratorMeta(IdentityIdGeneratorMeta m,
            Printer p) {
        p
                .p("private static final %1$s __idGenerator = new %2$s();%n", IdGenerator.class
                        .getName(), m.getIdGeneratorClassName());
        return null;
    }

    @Override
    public Void visistSequenceIdGeneratorMeta(SequenceIdGeneratorMeta m,
            Printer p) {
        p
                .p("private static final %1$s __idGenerator = new %2$s(\"%3$s\", %4$s, %5$s);%n", IdGenerator.class
                        .getName(), m.getIdGeneratorClassName(), m
                        .getQualifiedSequenceName(), m.getInitialValue(), m
                        .getAllocationSize());
        return null;
    }

    @Override
    public Void visistTableIdGeneratorMeta(TableIdGeneratorMeta m, Printer p) {
        p
                .p("private static final %1$s __idGenerator = new %2$s(\"%3$s\", \"%4$s\", \"%5$s\", \"%6$s\", %7$s, %8$s);%n", IdGenerator.class
                        .getName(), m.getIdGeneratorClassName(), m
                        .getQualifiedTableName(), m.getPkColumnName(), m
                        .getValueColumnName(), m.getPkColumnValue(), m
                        .getInitialValue(), m.getAllocationSize());
        return null;
    }

}
