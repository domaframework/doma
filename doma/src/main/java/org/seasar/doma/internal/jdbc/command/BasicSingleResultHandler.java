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
package org.seasar.doma.internal.jdbc.command;

import java.util.function.Supplier;

import org.seasar.doma.wrapper.Wrapper;

/**
 * @author taedium
 * @param <BASIC>
 */
public class BasicSingleResultHandler<BASIC> extends
        ScalarSingleResultHandler<BASIC, BASIC> {

    public BasicSingleResultHandler(Supplier<Wrapper<BASIC>> supplier,
            boolean primitive) {
        super(() -> new org.seasar.doma.internal.wrapper.BasicScalar<>(
                supplier, primitive));
    }

}
