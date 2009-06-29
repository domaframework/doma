package org.seasar.doma.jdbc;

import org.seasar.doma.internal.jdbc.type.BigDecimalType;
import org.seasar.doma.internal.jdbc.type.DateType;
import org.seasar.doma.internal.jdbc.type.IntegerType;
import org.seasar.doma.internal.jdbc.type.StringType;
import org.seasar.doma.internal.jdbc.type.TimeType;
import org.seasar.doma.internal.jdbc.type.TimestampType;


/**
 * @author taedium
 * 
 */
public final class JdbcTypes {

    public static final StringType STRING = new StringType();

    public static final IntegerType INTEGER = new IntegerType();

    public static final BigDecimalType BIGDECIMAL = new BigDecimalType();

    public static final DateType DATE = new DateType();

    public static final TimeType TIME = new TimeType();

    public static final TimestampType TIMESTAMP = new TimestampType();
}
