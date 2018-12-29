package org.seasar.doma.jdbc.type;

/**
 * {@link JdbcType} の実装のインスタンスを提供するクラスです。
 *
 * @author taedium
 */
public final class JdbcTypes {

  public static final ArrayType ARRAY = new ArrayType();

  public static final BigDecimalType BIG_DECIMAL = new BigDecimalType();

  public static final BigIntegerType BIG_INTEGER = new BigIntegerType();

  public static final BlobType BLOB = new BlobType();

  public static final BooleanType BOOLEAN = new BooleanType();

  public static final ByteType BYTE = new ByteType();

  public static final BytesType BYTES = new BytesType();

  public static final ClobType CLOB = new ClobType();

  public static final DateType DATE = new DateType();

  public static final DoubleType DOUBLE = new DoubleType();

  public static final FloatType FLOAT = new FloatType();

  public static final IntegerAdaptiveBooleanType INTEGER_ADAPTIVE_BOOLEAN =
      new IntegerAdaptiveBooleanType();

  public static final IntegerType INTEGER = new IntegerType();

  public static final LocalDateType LOCAL_DATE = new LocalDateType();

  public static final LocalDateTimeType LOCAL_DATE_TIME = new LocalDateTimeType();

  public static final LocalTimeType LOCAL_TIME = new LocalTimeType();

  public static final LongType LONG = new LongType();

  public static final NClobType NCLOB = new NClobType();

  public static final NStringType NSTRING = new NStringType();

  public static final ObjectType OBJECT = new ObjectType();

  public static final ShortType SHORT = new ShortType();

  public static final StringType STRING = new StringType();

  public static final SQLXMLType SQLXML = new SQLXMLType();

  public static final TimeType TIME = new TimeType();

  public static final TimestampType TIMESTAMP = new TimestampType();

  public static final UtilDateType UTIL_DATE = new UtilDateType();
}
