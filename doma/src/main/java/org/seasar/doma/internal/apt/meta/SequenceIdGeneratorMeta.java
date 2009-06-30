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
package org.seasar.doma.internal.apt.meta;

import static org.seasar.doma.internal.util.Assertions.*;

import org.seasar.doma.internal.jdbc.id.SequenceIdGenerator;

/**
 * @author taedium
 * 
 */
public class SequenceIdGeneratorMeta implements IdGeneratorMeta {

    protected final String qualifiedSequenceName;

    protected final int initialValue;

    protected final int allocationSize;

    public SequenceIdGeneratorMeta(String qualifiedSequenceName,
            int initialValue, int allocationSize) {
        assertNotNull(qualifiedSequenceName);
        this.qualifiedSequenceName = qualifiedSequenceName;
        this.initialValue = initialValue;
        this.allocationSize = allocationSize;
    }

    public String getQualifiedSequenceName() {
        return qualifiedSequenceName;
    }

    public int getInitialValue() {
        return initialValue;
    }

    public int getAllocationSize() {
        return allocationSize;
    }

    @Override
    public String getIdGeneratorClassName() {
        return SequenceIdGenerator.class.getName();
    }

    @Override
    public <R, P> R accept(IdGeneratorMetaVisitor<R, P> visitor, P p) {
        return visitor.visistSequenceIdGeneratorMeta(this, p);
    }
}
