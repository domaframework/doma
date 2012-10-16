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

public class _PhoneNumber implements DomainType<String, PhoneNumber> {

    private static final _PhoneNumber singleton = new _PhoneNumber();

    private _PhoneNumber() {
    }

    @Override
    public PhoneNumber newDomain(String value) {
        return new PhoneNumber(value);
    }

    @Override
    public Class<String> getValueClass() {
        return String.class;
    }

    @Override
    public Class<PhoneNumber> getDomainClass() {
        return PhoneNumber.class;
    }

    @Override
    public Wrapper getWrapper(PhoneNumber domain) {
        return new Wrapper(domain);
    }

    public static _PhoneNumber getSingletonInternal() {
        return singleton;
    }

    static class Wrapper extends StringWrapper implements
            DomainWrapper<String, PhoneNumber> {

        private PhoneNumber domain;

        public Wrapper(PhoneNumber domain) {
            if (domain == null) {
                this.domain = new PhoneNumber(null);
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
            domain = new PhoneNumber(value);
        }

        @Override
        public PhoneNumber getDomain() {
            return domain;
        }

    }

}
