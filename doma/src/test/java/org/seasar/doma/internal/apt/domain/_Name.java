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
package org.seasar.doma.internal.apt.domain;

import org.seasar.doma.internal.domain.DomainType;
import org.seasar.doma.internal.domain.DomainTypeFactory;
import org.seasar.doma.wrapper.StringWrapper;

/**
 * @author taedium
 * 
 */
public class _Name implements DomainTypeFactory<String, Name> {

    @Override
    public DomainType<String, Name> createDomainType() {
        return new NameType();
    }

    @Override
    public DomainType<String, Name> createDomainType(Name domain) {
        return new NameType(domain);
    }

    private static class NameType implements DomainType<String, Name> {

        private final StringWrapper wrapper = new StringWrapper();

        private NameType() {
        }

        private NameType(Name domain) {
            this.wrapper.set(domain != null ? domain.getValue() : null);
        }

        @Override
        public Name getDomain() {
            return new Name(wrapper.get());
        }

        @Override
        public Class<Name> getDomainClass() {
            return Name.class;
        }

        @Override
        public StringWrapper getWrapper() {
            return wrapper;
        }

    }

}
