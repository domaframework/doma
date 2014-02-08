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
package org.seasar.doma.internal.apt.entity;

import org.seasar.doma.jdbc.domain.AbstractDomainType;

/**
 * @author taedium
 * 
 */
public class _Name extends AbstractDomainType<String, Name> {

    private _Name() {
        super(() -> new org.seasar.doma.wrapper.StringWrapper());
    }

    @Override
    public Name newDomain(String value) {
        return null;
    }

    @Override
    public String getBasicValue(Name domain) {
        return null;
    }

    @Override
    public Class<String> getBasicClass() {
        return null;
    }

    @Override
    public Class<Name> getDomainClass() {
        return null;
    }

    public static _Name getSingletonInternal() {
        return null;
    }

}
