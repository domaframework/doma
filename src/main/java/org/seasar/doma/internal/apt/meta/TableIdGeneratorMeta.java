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
package org.seasar.doma.internal.apt.meta;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import org.seasar.doma.internal.apt.reflection.TableGeneratorReflection;

/**
 * @author taedium
 * 
 */
public class TableIdGeneratorMeta implements IdGeneratorMeta {

    private final TableGeneratorReflection tableGeneratorReflection;

    public TableIdGeneratorMeta(TableGeneratorReflection tableGeneratorReflection) {
        assertNotNull(tableGeneratorReflection);
        this.tableGeneratorReflection = tableGeneratorReflection;
    }

    public String getQualifiedTableName() {
        StringBuilder buf = new StringBuilder();
        String catalogName = tableGeneratorReflection.getCatalogValue();
        if (!catalogName.isEmpty()) {
            buf.append(catalogName);
            buf.append(".");
        }
        String schemaName = tableGeneratorReflection.getCatalogValue();
        if (!schemaName.isEmpty()) {
            buf.append(schemaName);
            buf.append(".");
        }
        buf.append(tableGeneratorReflection.getTableValue());
        return buf.toString();
    }

    public String getPkColumnName() {
        return tableGeneratorReflection.getPkColumnNameValue();
    }

    public String getValueColumnName() {
        return tableGeneratorReflection.getValueColumnNameValue();
    }

    public String getPkColumnValue() {
        return tableGeneratorReflection.getPkColumnValueValue();
    }

    public long getInitialValue() {
        return tableGeneratorReflection.getInitialValueValue();
    }

    public long getAllocationSize() {
        return tableGeneratorReflection.getAllocationSizeValue();
    }

    @Override
    public String getIdGeneratorClassName() {
        return tableGeneratorReflection.getImplementerValue().toString();
    }

    @Override
    public <R, P> R accept(IdGeneratorMetaVisitor<R, P> visitor, P p) {
        return visitor.visistTableIdGeneratorMeta(this, p);
    }
}
