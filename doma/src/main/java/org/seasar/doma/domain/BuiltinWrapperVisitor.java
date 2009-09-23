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
 * あらかじめ用意された {@link WrapperVisitor} です。
 * 
 * @author taedium
 * 
 */
public interface BuiltinWrapperVisitor<R, P, TH extends Throwable> extends
        ArrayWrapperVisitor<R, P, TH>,
        BigDecimalWrapperVisitor<R, P, TH>,
        BigIntegerWrapperVisitor<R, P, TH>,
        BlobWrapperVisitor<R, P, TH>,
        BooleanWrapperVisitor<R, P, TH>,
        ByteWrapperVisitor<R, P, TH>,
        BytesWrapperVisitor<R, P, TH>,
        ClobWrapperVisitor<R, P, TH>,
        DateWrapperVisitor<R, P, TH>,
        DoubleWrapperVisitor<R, P, TH>,
        FloatWrapperVisitor<R, P, TH>,
        IntegerWrapperVisitor<R, P, TH>,
        LongWrapperVisitor<R, P, TH>,
        NClobWrapperVisitor<R, P, TH>,
        ShortWrapperVisitor<R, P, TH>,
        StringWrapperVisitor<R, P, TH>,
        TimeWrapperVisitor<R, P, TH>,
        TimestampWrapperVisitor<R, P, TH> {
}
