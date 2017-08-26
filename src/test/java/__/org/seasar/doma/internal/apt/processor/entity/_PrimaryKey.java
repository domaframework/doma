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
package __.org.seasar.doma.internal.apt.processor.entity;

import org.seasar.doma.internal.apt.processor.entity.PrimaryKey;
import org.seasar.doma.internal.apt.processor.entity.PrimaryKeyConverter;

/**
 * @author taedium
 * 
 */
public final class _PrimaryKey
        extends org.seasar.doma.jdbc.holder.AbstractHolderDesc<java.lang.Integer, PrimaryKey> {

    static {
        org.seasar.doma.internal.Artifact.validateVersion("@VERSION@");
    }

    private static final _PrimaryKey singleton = new _PrimaryKey();

    private static final PrimaryKeyConverter converter = new PrimaryKeyConverter();

    private _PrimaryKey() {
        super(() -> new org.seasar.doma.wrapper.IntegerWrapper());
    }

    @Override
    public PrimaryKey newHolder(java.lang.Integer value) {
        return converter.fromValueToHolder(value);
    }

    @Override
    public Integer getBasicValue(PrimaryKey holder) {
        if (holder == null) {
            return null;
        }
        return converter.fromHolderToValue(holder);
    }

    @Override
    public Class<Integer> getBasicClass() {
        return Integer.class;
    }

    @Override
    public Class<PrimaryKey> getHolderClass() {
        return PrimaryKey.class;
    }

    /**
     * @return the singleton
     */
    public static _PrimaryKey getSingletonInternal() {
        return singleton;
    }

}
