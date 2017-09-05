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
package org.seasar.doma;

import java.util.stream.Collector;
import java.util.stream.Stream;

/**
 * Defines strategies for handling an object that is mapped to a result set.
 */
public enum SelectType {

    /**
     * The object is returned from a method.
     */
    RETURN,

    /**
     * The object is handled by using {@link Stream}.
     */
    STREAM,

    /**
     * The object is handled by using {@link Collector}.
     */
    COLLECT;
}
