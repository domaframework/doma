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

import static org.seasar.doma.internal.util.AssertionUtil.*;

import org.seasar.doma.internal.apt.mirror.TableGeneratorMirror;

/**
 * @author taedium
 * 
 */
public class TableIdGeneratorMeta implements IdGeneratorMeta {

    protected final TableGeneratorMirror tableGeneratorMirror;

    public TableIdGeneratorMeta(TableGeneratorMirror tableGeneratorMirror) {
        assertNotNull(tableGeneratorMirror);
        this.tableGeneratorMirror = tableGeneratorMirror;
    }

    public String getQualifiedTableName() {
        StringBuilder buf = new StringBuilder();
        String catalogName = tableGeneratorMirror.getCatalogValue();
        if (!catalogName.isEmpty()) {
            buf.append(catalogName);
            buf.append(".");
        }
        String schemaName = tableGeneratorMirror.getCatalogValue();
        if (!schemaName.isEmpty()) {
            buf.append(schemaName);
            buf.append(".");
        }
        buf.append(tableGeneratorMirror.getTableValue());
        return buf.toString();
    }

    public String getPkColumnName() {
        return tableGeneratorMirror.getPkColumnNameValue();
    }

    public String getValueColumnName() {
        return tableGeneratorMirror.getValueColumnNameValue();
    }

    public String getPkColumnValue() {
        return tableGeneratorMirror.getPkColumnValueValue();
    }

    public long getInitialValue() {
        return tableGeneratorMirror.getInitialValueValue();
    }

    public long getAllocationSize() {
        return tableGeneratorMirror.getAllocationSizeValue();
    }

    @Override
    public String getIdGeneratorClassName() {
        return tableGeneratorMirror.getImplementerValue().toString();
    }

    @Override
    public <R, P> R accept(IdGeneratorMetaVisitor<R, P> visitor, P p) {
        return visitor.visistTableIdGeneratorMeta(this, p);
    }
}
