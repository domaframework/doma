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
package org.seasar.doma.wrapper;

/**
 * {@link Wrapper} のビジターです。
 * 
 * @author taedium
 * 
 * @param <R>
 *            戻り値の型
 * @param <P>
 *            1番目のパラメータの型
 * @param <Q>
 *            2番目のパラメータの型
 * @param <TH>
 *            例外の型
 */
public interface WrapperVisitor<R, P, Q, TH extends Throwable> {

    default R visitArrayWrapper(ArrayWrapper wrapper, P p, Q q) throws TH {
        return null;
    }

    default R visitBigDecimalWrapper(BigDecimalWrapper wrapper, P p, Q q)
            throws TH {
        return null;
    }

    default R visitBigIntegerWrapper(BigIntegerWrapper wrapper, P p, Q q)
            throws TH {
        return null;
    }

    default R visitBlobWrapper(BlobWrapper wrapper, P p, Q q) throws TH {
        return null;
    }

    default R visitBooleanWrapper(BooleanWrapper wrapper, P p, Q q) throws TH {
        return null;
    }

    default R visitBytesWrapper(BytesWrapper wrapper, P p, Q q) throws TH {
        return null;
    }

    default R visitByteWrapper(ByteWrapper wrapper, P p, Q q) throws TH {
        return null;
    }

    default R visitClobWrapper(ClobWrapper wrapper, P p, Q q) throws TH {
        return null;
    }

    default R visitDateWrapper(DateWrapper wrapper, P p, Q q) throws TH {
        return null;
    }

    default R visitDoubleWrapper(DoubleWrapper wrapper, P p, Q q) throws TH {
        return null;
    }

    default <E extends Enum<E>> R visitEnumWrapper(EnumWrapper<E> wrapper, P p,
            Q q) throws TH {
        return null;
    }

    default R visitFloatWrapper(FloatWrapper wrapper, P p, Q q) throws TH {
        return null;
    }

    default R visitIntegerWrapper(IntegerWrapper wrapper, P p, Q q) throws TH {
        return null;
    }

    default R visitLocalDateWrapper(LocalDateWrapper wrapper, P p, Q q)
            throws TH {
        return null;
    }

    default R visitLocalDateTimeWrapper(LocalDateTimeWrapper wrapper, P p, Q q)
            throws TH {
        return null;
    }

    default R visitLocalTimeWrapper(LocalTimeWrapper wrapper, P p, Q q)
            throws TH {
        return null;
    }

    default R visitLongWrapper(LongWrapper wrapper, P p, Q q) throws TH {
        return null;
    }

    default R visitNClobWrapper(NClobWrapper wrapper, P p, Q q) throws TH {
        return null;
    }

    default R visitObjectWrapper(ObjectWrapper wrapper, P p, Q q) throws TH {
        return null;
    }

    default R visitShortWrapper(ShortWrapper wrapper, P p, Q q) throws TH {
        return null;
    }

    default R visitStringWrapper(StringWrapper wrapper, P p, Q q) throws TH {
        return null;
    }

    default R visitSQLXMLWrapper(SQLXMLWrapper wrapper, P p, Q q) throws TH {
        return null;
    }

    default R visitTimestampWrapper(TimestampWrapper wrapper, P p, Q q)
            throws TH {
        return null;
    }

    default R visitTimeWrapper(TimeWrapper wrapper, P p, Q q) throws TH {
        return null;
    }

    default R visitUtilDateWrapper(UtilDateWrapper wrapper, P p, Q q) throws TH {
        return null;
    }
}
