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

public final class _Branch implements
        org.seasar.doma.jdbc.domain.DomainType<java.lang.String, Branch> {

    static {
        org.seasar.doma.internal.Artifact.validateVersion("@VERSION@");
    }

    private static final _Branch singleton = new _Branch();

    private static final BranchConverter converter = new BranchConverter();

    private _Branch() {
    }

    @Override
    public Branch newDomain(java.lang.String value) {
        return converter.fromValueToDomain(value);
    }

    @Override
    public Class<String> getValueClass() {
        return String.class;
    }

    @Override
    public Class<Branch> getDomainClass() {
        return Branch.class;
    }

    @Override
    public org.seasar.doma.jdbc.domain.DomainWrapper<java.lang.String, Branch> getWrapper(
            Branch domain) {
        return new Wrapper(domain);
    }

    /**
     * @return the singleton
     */
    public static _Branch getSingletonInternal() {
        return singleton;
    }

    private static class Wrapper extends org.seasar.doma.wrapper.StringWrapper
            implements
            org.seasar.doma.jdbc.domain.DomainWrapper<java.lang.String, Branch> {

        private Branch domain;

        private Wrapper(Branch domain) {
            this.domain = domain;
        }

        @Override
        protected java.lang.String doGet() {
            if (domain == null) {
                return null;
            }
            return converter.fromDomainToValue(domain);
        }

        @Override
        protected void doSet(java.lang.String value) {
            domain = converter.fromValueToDomain(value);
        }

        @Override
        public Branch getDomain() {
            return domain;
        }
    }
}
