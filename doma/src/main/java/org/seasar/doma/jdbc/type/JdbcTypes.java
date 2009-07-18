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
package org.seasar.doma.jdbc.type;

/**
 * @author taedium
 * 
 */
public final class JdbcTypes {

    public static final StringType STRING = new StringType();

    public static final IntType INT = new IntType();

    public static final BigDecimalType BIG_DECIMAL = new BigDecimalType();

    public static final DateType DATE = new DateType();

    public static final TimeType TIME = new TimeType();

    public static final TimestampType TIMESTAMP = new TimestampType();

    public static final ArrayType ARRAY = new ArrayType();

    public static final BlobType BLOB = new BlobType();

    public static final ClobType CLOB = new ClobType();

    public static final NClobType NCLOB = new NClobType();

    public static final BooleanType BOOLEAN = new BooleanType();

    public static final IntAdaptiveBooleanType INT_ADAPTIVE_BOOLEAN = new IntAdaptiveBooleanType();

    public static final BytesType BYTES = new BytesType();

    public static final DoubleType DOUBLE = new DoubleType();

    public static final ObjectType OBJECT = new ObjectType();

}
