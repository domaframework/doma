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
package org.seasar.doma.jdbc.builder;

import static org.seasar.doma.internal.util.AssertionUtil.assertNull;

import java.util.List;
import java.util.ArrayList;

/**
 * @author bakenezumi
 *
 */
class BatchParam<P> {

    final String name;

    final Class<P> paramClass;

    final List<P> params = new ArrayList<>();

    final boolean literal;

    BatchParam(Class<P> paramClass, ParamIndex index, boolean literal) {
        this.paramClass = paramClass;
        this.name = "p" + index.getValue();
        this.literal = literal;
    }

    BatchParam(BatchParam<Object> baseParam, Class<P> paramClass) {
        this.name = baseParam.name;
        this.paramClass = paramClass;
        this.literal = baseParam.literal;
        baseParam.params.forEach(e -> {
            assertNull(e);
            params.add(null);
        });
    }

    void add(P param) {
        params.add(param);
    }

}
