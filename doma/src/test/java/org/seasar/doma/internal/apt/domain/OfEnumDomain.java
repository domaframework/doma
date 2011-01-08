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
package org.seasar.doma.internal.apt.domain;

import org.seasar.doma.Domain;

/**
 * @author taedium
 * 
 */
@Domain(valueType = OfEnumDomain.JobType.class, factoryMethod = "of")
public class OfEnumDomain {

    private final JobType jobType;

    private OfEnumDomain(JobType jobType) {
        this.jobType = jobType;
    }

    public JobType getValue() {
        return jobType;
    }

    public static OfEnumDomain of(JobType value) {
        return new OfEnumDomain(value);
    }

    public static enum JobType {
        SALESMAN
    }
}
