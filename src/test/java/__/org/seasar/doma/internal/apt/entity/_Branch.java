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
package __.org.seasar.doma.internal.apt.entity;

import org.seasar.doma.internal.apt.entity.Branch;
import org.seasar.doma.internal.apt.entity.BranchConverter;

/**
 * @author taedium
 * 
 */

public final class _Branch
        extends
        org.seasar.doma.jdbc.holder.AbstractHolderType<java.lang.String, Branch> {

    static {
        org.seasar.doma.internal.Artifact.validateVersion("@VERSION@");
    }

    private static final _Branch singleton = new _Branch();

    private static final BranchConverter converter = new BranchConverter();

    private _Branch() {
        super(() -> new org.seasar.doma.wrapper.StringWrapper());
    }

    @Override
    public Branch newHolder(java.lang.String value) {
        return converter.fromValueToHolder(value);
    }

    @Override
    public String getBasicValue(Branch holder) {
        if (holder == null) {
            return null;
        }
        return converter.fromHolderToValue(holder);
    }

    @Override
    public Class<String> getBasicClass() {
        return String.class;
    }

    @Override
    public Class<Branch> getHolderClass() {
        return Branch.class;
    }

    /**
     * @return the singleton
     */
    public static _Branch getSingletonInternal() {
        return singleton;
    }

}
