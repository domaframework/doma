package org.seasar.doma.wrapper;

/**
 * A visitor for the {@link Wrapper} interface.
 *
 * @param <R> The result type
 * @param <P> The first parameter type
 * @param <Q> The second parameter type
 * @param <TH> The error or exception type
 */
public interface WrapperVisitor<R, P, Q, TH extends Throwable> {

  default R visitArrayWrapper(ArrayWrapper wrapper, P p, Q q) throws TH {
    return null;
  }

  default R visitBigDecimalWrapper(BigDecimalWrapper wrapper, P p, Q q) throws TH {
    return null;
  }

  default R visitBigIntegerWrapper(BigIntegerWrapper wrapper, P p, Q q) throws TH {
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

  default <E extends Enum<E>> R visitEnumWrapper(EnumWrapper<E> wrapper, P p, Q q) throws TH {
    return null;
  }

  default R visitFloatWrapper(FloatWrapper wrapper, P p, Q q) throws TH {
    return null;
  }

  default R visitIntegerWrapper(IntegerWrapper wrapper, P p, Q q) throws TH {
    return null;
  }

  default R visitLocalDateWrapper(LocalDateWrapper wrapper, P p, Q q) throws TH {
    return null;
  }

  default R visitLocalDateTimeWrapper(LocalDateTimeWrapper wrapper, P p, Q q) throws TH {
    return null;
  }

  default R visitLocalTimeWrapper(LocalTimeWrapper wrapper, P p, Q q) throws TH {
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

  default R visitPrimitiveBooleanWrapper(PrimitiveBooleanWrapper wrapper, P p, Q q) throws TH {
    return visitBooleanWrapper(wrapper, p, q);
  }

  default R visitPrimitiveByteWrapper(PrimitiveByteWrapper wrapper, P p, Q q) throws TH {
    return visitByteWrapper(wrapper, p, q);
  }

  default R visitPrimitiveDoubleWrapper(PrimitiveDoubleWrapper wrapper, P p, Q q) throws TH {
    return visitDoubleWrapper(wrapper, p, q);
  }

  default R visitPrimitiveFloatWrapper(PrimitiveFloatWrapper wrapper, P p, Q q) throws TH {
    return visitFloatWrapper(wrapper, p, q);
  }

  default R visitPrimitiveIntWrapper(PrimitiveIntWrapper wrapper, P p, Q q) throws TH {
    return visitIntegerWrapper(wrapper, p, q);
  }

  default R visitPrimitiveLongWrapper(PrimitiveLongWrapper wrapper, P p, Q q) throws TH {
    return visitLongWrapper(wrapper, p, q);
  }

  default R visitPrimitiveShortWrapper(PrimitiveShortWrapper wrapper, P p, Q q) throws TH {
    return visitShortWrapper(wrapper, p, q);
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

  default R visitTimestampWrapper(TimestampWrapper wrapper, P p, Q q) throws TH {
    return null;
  }

  default R visitTimeWrapper(TimeWrapper wrapper, P p, Q q) throws TH {
    return null;
  }

  default R visitUtilDateWrapper(UtilDateWrapper wrapper, P p, Q q) throws TH {
    return null;
  }
}
