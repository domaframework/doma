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
import org.seasar.doma.wrapper.IntegerWrapper;

public class _JobType implements DomainType<Integer, JobType> {

    private static final _JobType singleton = new _JobType();

    private _JobType() {
    }

    @Override
    public JobType newDomain(Integer value) {
        if (value == null) {
            return new JobType(0);
        }
        return new JobType(value);
    }

    @Override
    public Class<Integer> getValueClass() {
        return Integer.class;
    }

    @Override
    public Class<JobType> getDomainClass() {
        return JobType.class;
    }

    @Override
    public Wrapper getWrapper(JobType domain) {
        return new Wrapper(domain);
    }

    public static _JobType getSingletonInternal() {
        return singleton;
    }

    static class Wrapper extends IntegerWrapper implements
            DomainWrapper<Integer, JobType> {

        private JobType domain;

        public Wrapper(JobType domain) {
            if (domain == null) {
                this.domain = new JobType(0);
            } else {
                this.domain = domain;
            }
        }

        @Override
        protected Integer doGet() {
            return domain.getValue();
        }

        @Override
        protected void doSet(Integer value) {
            domain = new JobType(value);
        }

        @Override
        public JobType getDomain() {
            return domain;
        }

    }

}
