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
package org.seasar.doma.domain;

/**
 * @author taedium
 * 
 */
public interface ComparableDomain<V extends Comparable<? super V>, D extends Domain<V, D> & Comparable<? super D>>
        extends Domain<V, D>, Comparable<D> {

    int compareTo(D other);

    boolean eq(V other);

    boolean eq(D other);

    boolean ge(V other);

    boolean ge(D other);

    boolean gt(V other);

    boolean gt(D other);

    boolean le(V other);

    boolean le(D other);

    boolean lt(V other);

    boolean lt(D other);

}
