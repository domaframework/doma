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
package example.domain;

import org.seasar.doma.jdbc.domain.DomainType;
import org.seasar.doma.jdbc.domain.DomainWrapper;
import org.seasar.doma.wrapper.StringWrapper;

public class _InternationalPhoneNumber implements
        DomainType<String, InternationalPhoneNumber> {

    private static final _InternationalPhoneNumber singleton = new _InternationalPhoneNumber();

    private _InternationalPhoneNumber() {
    }

    @Override
    public InternationalPhoneNumber newDomain(String value) {
        return new InternationalPhoneNumber(value);
    }

    @Override
    public Class<String> getValueClass() {
        return String.class;
    }

    @Override
    public Class<InternationalPhoneNumber> getDomainClass() {
        return InternationalPhoneNumber.class;
    }

    @Override
    public Wrapper getWrapper(InternationalPhoneNumber domain) {
        return new Wrapper(domain);
    }

    public static _InternationalPhoneNumber getSingletonInternal() {
        return singleton;
    }

    static class Wrapper extends StringWrapper implements
            DomainWrapper<String, InternationalPhoneNumber> {

        private InternationalPhoneNumber domain;

        public Wrapper(InternationalPhoneNumber domain) {
            if (domain == null) {
                this.domain = new InternationalPhoneNumber(null);
            } else {
                this.domain = domain;
            }
        }

        @Override
        protected String doGet() {
            return domain.getValue();
        }

        @Override
        protected void doSet(String value) {
            domain = new InternationalPhoneNumber(value);
        }

        @Override
        public InternationalPhoneNumber getDomain() {
            return domain;
        }

    }

}
