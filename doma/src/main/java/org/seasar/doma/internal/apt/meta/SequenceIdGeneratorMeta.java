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

import org.seasar.doma.internal.apt.mirror.SequenceGeneratorMirror;

/**
 * @author taedium
 * 
 */
public class SequenceIdGeneratorMeta implements IdGeneratorMeta {

    protected final SequenceGeneratorMirror sequenceGeneratorMirror;

    public SequenceIdGeneratorMeta(
            SequenceGeneratorMirror sequenceGeneratorMirror) {
        assertNotNull(sequenceGeneratorMirror);
        this.sequenceGeneratorMirror = sequenceGeneratorMirror;
    }

    public String getQualifiedSequenceName() {
        StringBuilder buf = new StringBuilder();
        String catalogName = sequenceGeneratorMirror.getCatalogValue();
        if (!catalogName.isEmpty()) {
            buf.append(catalogName);
            buf.append(".");
        }
        String schemaName = sequenceGeneratorMirror.getCatalogValue();
        if (!schemaName.isEmpty()) {
            buf.append(schemaName);
            buf.append(".");
        }
        buf.append(sequenceGeneratorMirror.getSequenceValue());
        return buf.toString();
    }

    public long getInitialValue() {
        return sequenceGeneratorMirror.getInitialValueValue();
    }

    public long getAllocationSize() {
        return sequenceGeneratorMirror.getAllocationSizeValue();
    }

    @Override
    public String getIdGeneratorClassName() {
        return sequenceGeneratorMirror.getImplementerValue().toString();
    }

    @Override
    public <R, P> R accept(IdGeneratorMetaVisitor<R, P> visitor, P p) {
        return visitor.visistSequenceIdGeneratorMeta(this, p);
    }
}
