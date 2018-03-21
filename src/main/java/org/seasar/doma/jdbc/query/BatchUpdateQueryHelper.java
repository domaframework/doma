package org.seasar.doma.jdbc.query;

import java.util.ArrayList;
import java.util.List;

import org.seasar.doma.internal.jdbc.sql.SqlContext;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.Naming;
import org.seasar.doma.jdbc.dialect.Dialect;
import org.seasar.doma.jdbc.entity.EntityDesc;
import org.seasar.doma.jdbc.entity.EntityPropertyDesc;
import org.seasar.doma.jdbc.entity.Property;

/**
 * A helper for {@link BatchUpdateQuery}.
 */
public class BatchUpdateQueryHelper<E> {

    protected final Config config;

    protected final EntityDesc<E> entityDesc;

    protected final boolean versionIgnored;

    protected final boolean optimisticLockExceptionSuppressed;

    protected final String[] includedPropertyNames;

    protected final String[] excludedPropertyNames;

    public BatchUpdateQueryHelper(Config config, EntityDesc<E> entityDesc,
            String[] includedPropertyNames, String[] excludedPropertyNames, boolean versionIgnored,
            boolean optimisticLockExceptionSuppressed) {
        this.config = config;
        this.entityDesc = entityDesc;
        this.versionIgnored = versionIgnored;
        this.optimisticLockExceptionSuppressed = optimisticLockExceptionSuppressed;
        this.includedPropertyNames = includedPropertyNames;
        this.excludedPropertyNames = excludedPropertyNames;
    }

    public List<EntityPropertyDesc<E, ?>> getTargetPropertyDescs() {
        List<EntityPropertyDesc<E, ?>> targetPropertyDescs = new ArrayList<>(
                entityDesc.getEntityPropertyDescs().size());
        for (EntityPropertyDesc<E, ?> p : entityDesc.getEntityPropertyDescs()) {
            if (!p.isUpdatable()) {
                continue;
            }
            if (p.isId()) {
                continue;
            }
            if (p.isVersion()) {
                targetPropertyDescs.add(p);
                continue;
            }
            if (!isTargetPropertyName(p.getName())) {
                continue;
            }
            targetPropertyDescs.add(p);
        }
        return targetPropertyDescs;
    }

    protected boolean isTargetPropertyName(String name) {
        if (includedPropertyNames.length > 0) {
            for (String includedName : includedPropertyNames) {
                if (includedName.equals(name)) {
                    for (String excludedName : excludedPropertyNames) {
                        if (excludedName.equals(name)) {
                            return false;
                        }
                    }
                    return true;
                }
            }
            return false;
        }
        if (excludedPropertyNames.length > 0) {
            for (String excludedName : excludedPropertyNames) {
                if (excludedName.equals(name)) {
                    return false;
                }
            }
            return true;
        }
        return true;
    }

    public void populateValues(E entity, List<EntityPropertyDesc<E, ?>> targetPropertyDescs,
            EntityPropertyDesc<E, ?> versionPropertyDesc, SqlContext context) {
        Dialect dialect = config.getDialect();
        Naming naming = config.getNaming();
        for (EntityPropertyDesc<E, ?> propertyDesc : targetPropertyDescs) {
            Property<E, ?> property = propertyDesc.createProperty();
            property.load(entity);
            context.appendSql(propertyDesc.getColumnName(naming::apply, dialect::applyQuote));
            context.appendSql(" = ");
            context.appendParameter(property.asInParameter());
            if (propertyDesc.isVersion() && !versionIgnored) {
                context.appendSql(" + 1");
            }
            context.appendSql(", ");
        }
        context.cutBackSql(2);
    }

}
