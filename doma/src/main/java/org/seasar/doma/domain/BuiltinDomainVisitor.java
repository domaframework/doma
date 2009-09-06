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
 * あらかじめ用意された {@link DomainVisitor} です。
 * 
 * @author taedium
 * 
 */
public interface BuiltinDomainVisitor<R, P, TH extends Throwable> extends
        ArrayDomainVisitor<R, P, TH>,
        BigDecimalDomainVisitor<R, P, TH>,
        BigIntegerDomainVisitor<R, P, TH>,
        BlobDomainVisitor<R, P, TH>,
        BooleanDomainVisitor<R, P, TH>,
        ByteDomainVisitor<R, P, TH>,
        BytesDomainVisitor<R, P, TH>,
        ClobDomainVisitor<R, P, TH>,
        DateDomainVisitor<R, P, TH>,
        DoubleDomainVisitor<R, P, TH>,
        FloatDomainVisitor<R, P, TH>,
        IntegerDomainVisitor<R, P, TH>,
        LongDomainVisitor<R, P, TH>,
        NClobDomainVisitor<R, P, TH>,
        ShortDomainVisitor<R, P, TH>,
        StringDomainVisitor<R, P, TH>,
        TimeDomainVisitor<R, P, TH>,
        TimestampDomainVisitor<R, P, TH> {
}
