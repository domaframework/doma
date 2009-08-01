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
public interface BuiltinDomainVisitor<R, P, TH extends Throwable> extends
        AbstractArrayDomainVisitor<R, P, TH>,
        AbstractBigDecimalDomainVisitor<R, P, TH>,
        AbstractBigIntegerDomainVisitor<R, P, TH>,
        AbstractBlobDomainVisitor<R, P, TH>,
        AbstractBooleanDomainVisitor<R, P, TH>,
        AbstractByteDomainVisitor<R, P, TH>,
        AbstractBytesDomainVisitor<R, P, TH>,
        AbstractClobDomainVisitor<R, P, TH>,
        AbstractDateDomainVisitor<R, P, TH>,
        AbstractDoubleDomainVisitor<R, P, TH>,
        AbstractFloatDomainVisitor<R, P, TH>,
        AbstractIntegerDomainVisitor<R, P, TH>,
        AbstractLongDomainVisitor<R, P, TH>,
        AbstractNClobDomainVisitor<R, P, TH>,
        AbstractShortDomainVisitor<R, P, TH>,
        AbstractStringDomainVisitor<R, P, TH>,
        AbstractTimeDomainVisitor<R, P, TH>,
        AbstractTimestampDomainVisitor<R, P, TH> {
}
