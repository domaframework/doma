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

import org.seasar.doma.internal.apt.entity.VersionNo;
import org.seasar.doma.internal.apt.entity.VersionNoConverter;

/**
 * @author taedium
 * 
 */
public final class _VersionNo
        extends
        org.seasar.doma.jdbc.domain.AbstractDomainType<java.lang.Integer, VersionNo> {

    static {
        org.seasar.doma.internal.Artifact.validateVersion("@VERSION@");
    }

    private static final _VersionNo singleton = new _VersionNo();

    private static final VersionNoConverter converter = new VersionNoConverter();

    private _VersionNo() {
        super(() -> new org.seasar.doma.wrapper.IntegerWrapper());
    }

    @Override
    public VersionNo newDomain(java.lang.Integer value) {
        return converter.fromValueToDomain(value);
    }

    @Override
    public Integer getBasicValue(VersionNo domain) {
        if (domain == null) {
            return null;
        }
        return converter.fromDomainToValue(domain);
    }

    @Override
    public Class<Integer> getBasicClass() {
        return Integer.class;
    }

    @Override
    public Class<VersionNo> getDomainClass() {
        return VersionNo.class;
    }

    /**
     * @return the singleton
     */
    public static _VersionNo getSingletonInternal() {
        return singleton;
    }

}
