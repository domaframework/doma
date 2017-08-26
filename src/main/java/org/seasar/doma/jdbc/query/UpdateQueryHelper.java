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
package org.seasar.doma.jdbc.query;

import java.util.ArrayList;
import java.util.List;

import org.seasar.doma.internal.jdbc.sql.SqlContext;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.Naming;
import org.seasar.doma.jdbc.dialect.Dialect;
import org.seasar.doma.jdbc.entity.EntityPropertyDesc;
import org.seasar.doma.jdbc.entity.EntityDesc;
import org.seasar.doma.jdbc.entity.Property;
import org.seasar.doma.jdbc.entity.VersionPropertyDesc;
import org.seasar.doma.wrapper.Wrapper;

/**
 * @author nakamura-to
 * @since 2.3.0
 */
public class UpdateQueryHelper<E> {

    protected final Config config;

    protected final EntityDesc<E> entityDesc;

    protected final boolean nullExcluded;

    protected final boolean versionIgnored;

    protected final boolean optimisticLockExceptionSuppressed;

    protected final boolean unchangedPropertyIncluded;

    protected final String[] includedPropertyNames;

    protected final String[] excludedPropertyNames;

    public UpdateQueryHelper(Config config, EntityDesc<E> entityDesc,
            String[] includedPropertyNames, String[] excludedPropertyNames, boolean nullExcluded,
            boolean versionIgnored, boolean optimisticLockExceptionSuppressed,
            boolean unchangedPropertyIncluded) {
        this.config = config;
        this.entityDesc = entityDesc;
        this.nullExcluded = nullExcluded;
        this.versionIgnored = versionIgnored;
        this.optimisticLockExceptionSuppressed = optimisticLockExceptionSuppressed;
        this.unchangedPropertyIncluded = unchangedPropertyIncluded;
        this.includedPropertyNames = includedPropertyNames;
        this.excludedPropertyNames = excludedPropertyNames;
    }

    public List<EntityPropertyDesc<E, ?>> getTargetPropertyDescs(E entity) {
        int capacity = entityDesc.getEntityPropertyDescs().size();
        List<EntityPropertyDesc<E, ?>> results = new ArrayList<>(capacity);
        E originalStates = entityDesc.getOriginalStates(entity);
        for (EntityPropertyDesc<E, ?> propertyDesc : entityDesc.getEntityPropertyDescs()) {
            if (!propertyDesc.isUpdatable()) {
                continue;
            }
            if (propertyDesc.isId()) {
                continue;
            }
            if (!versionIgnored && propertyDesc.isVersion()) {
                continue;
            }
            if (nullExcluded) {
                Property<E, ?> property = propertyDesc.createProperty();
                property.load(entity);
                if (property.getWrapper().get() == null) {
                    continue;
                }
            }
            if (unchangedPropertyIncluded || originalStates == null
                    || isChanged(entity, originalStates, propertyDesc)) {
                String name = propertyDesc.getName();
                if (!isTargetPropertyName(name)) {
                    continue;
                }
                results.add(propertyDesc);
            }
        }
        return results;
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

    protected boolean isChanged(E entity, E originalStates, EntityPropertyDesc<E, ?> propertyDesc) {
        Wrapper<?> wrapper = propertyDesc.createProperty().load(entity).getWrapper();
        Wrapper<?> originalWrapper = propertyDesc.createProperty()
                .load(originalStates)
                .getWrapper();
        return !wrapper.hasEqualValue(originalWrapper.get());
    }

    public void populateValues(E entity, List<EntityPropertyDesc<E, ?>> targetPropertyDescs,
            VersionPropertyDesc<E, ?, ?> versionPropertyDesc, SqlContext context) {
        Dialect dialect = config.getDialect();
        Naming naming = config.getNaming();
        for (EntityPropertyDesc<E, ?> propertyDesc : targetPropertyDescs) {
            Property<E, ?> property = propertyDesc.createProperty();
            property.load(entity);
            context.appendSql(propertyDesc.getColumnName(naming::apply, dialect::applyQuote));
            context.appendSql(" = ");
            context.appendParameter(property.asInParameter());
            context.appendSql(", ");
        }
        if (!versionIgnored && versionPropertyDesc != null) {
            Property<E, ?> property = versionPropertyDesc.createProperty();
            property.load(entity);
            context.appendSql(
                    versionPropertyDesc.getColumnName(naming::apply, dialect::applyQuote));
            context.appendSql(" = ");
            context.appendParameter(property.asInParameter());
            context.appendSql(" + 1");
        } else {
            context.cutBackSql(2);
        }
    }

}
