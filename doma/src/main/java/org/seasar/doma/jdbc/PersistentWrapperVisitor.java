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
package org.seasar.doma.jdbc;

import java.sql.Wrapper;

import org.seasar.doma.wrapper.ArrayWrapperVisitor;
import org.seasar.doma.wrapper.BigDecimalWrapperVisitor;
import org.seasar.doma.wrapper.BigIntegerWrapperVisitor;
import org.seasar.doma.wrapper.BlobWrapperVisitor;
import org.seasar.doma.wrapper.BooleanWrapperVisitor;
import org.seasar.doma.wrapper.ByteWrapperVisitor;
import org.seasar.doma.wrapper.BytesWrapperVisitor;
import org.seasar.doma.wrapper.ClobWrapperVisitor;
import org.seasar.doma.wrapper.DateWrapperVisitor;
import org.seasar.doma.wrapper.DoubleWrapperVisitor;
import org.seasar.doma.wrapper.EnumWrapperVisitor;
import org.seasar.doma.wrapper.FloatWrapperVisitor;
import org.seasar.doma.wrapper.IntegerWrapperVisitor;
import org.seasar.doma.wrapper.LongWrapperVisitor;
import org.seasar.doma.wrapper.NClobWrapperVisitor;
import org.seasar.doma.wrapper.ObjectWrapperVisitor;
import org.seasar.doma.wrapper.ShortWrapperVisitor;
import org.seasar.doma.wrapper.StringWrapperVisitor;
import org.seasar.doma.wrapper.TimeWrapperVisitor;
import org.seasar.doma.wrapper.TimestampWrapperVisitor;
import org.seasar.doma.wrapper.UtilDateWrapperVisitor;
import org.seasar.doma.wrapper.WrapperVisitor;

/**
 * 永続可能な {@link Wrapper} に対する {@link WrapperVisitor} です。
 * 
 * @author taedium
 * 
 */
public interface PersistentWrapperVisitor<R, P, TH extends Throwable> extends
        ArrayWrapperVisitor<R, P, TH>, BigDecimalWrapperVisitor<R, P, TH>,
        BigIntegerWrapperVisitor<R, P, TH>, BlobWrapperVisitor<R, P, TH>,
        BooleanWrapperVisitor<R, P, TH>, ByteWrapperVisitor<R, P, TH>,
        BytesWrapperVisitor<R, P, TH>, ClobWrapperVisitor<R, P, TH>,
        DateWrapperVisitor<R, P, TH>, DoubleWrapperVisitor<R, P, TH>,
        FloatWrapperVisitor<R, P, TH>, IntegerWrapperVisitor<R, P, TH>,
        LongWrapperVisitor<R, P, TH>, NClobWrapperVisitor<R, P, TH>,
        ShortWrapperVisitor<R, P, TH>, StringWrapperVisitor<R, P, TH>,
        TimeWrapperVisitor<R, P, TH>, TimestampWrapperVisitor<R, P, TH>,
        EnumWrapperVisitor<R, P, TH>, UtilDateWrapperVisitor<R, P, TH>,
        ObjectWrapperVisitor<R, P, TH> {
}
