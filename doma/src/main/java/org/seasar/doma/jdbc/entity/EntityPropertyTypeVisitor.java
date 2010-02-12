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
package org.seasar.doma.jdbc.entity;

import org.seasar.doma.internal.jdbc.criteria.CriterionVisitor;

/**
 * {@link EntityPropertyType} のビジターです。
 * 
 * @author taedium
 * 
 * @param <R>
 *            戻り値の型
 * @param <P>
 *            パラメータの型
 * @param <TH>
 *            例外の型
 */
public interface EntityPropertyTypeVisitor<R, P, TH extends Throwable> extends
        CriterionVisitor<R, P, TH> {

    <E, V> R visitEntityPropertyType(EntityPropertyType<E, V> propertyType, P p)
            throws TH;
}
