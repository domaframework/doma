package org.seasar.doma.wrapper;

/**
 * A visitor for the {@link NumberWrapper} interface.
 *
 * @param <R> The result type
 * @param <P> The first parameter type
 * @param <Q> The second parameter type
 * @param <TH> The error or exception type
 */
public interface NumberWrapperVisitor<R, P, Q, TH extends Throwable>
    extends WrapperVisitor<R, P, Q, TH> {

  default R visitBigIntegerWrapper(BigIntegerWrapper wrapper, P p, Q q) throws TH {
    return visitNumberWrapper(wrapper, p, q);
  }

  default R visitBigDecimalWrapper(BigDecimalWrapper wrapper, P p, Q q) throws TH {
    return visitNumberWrapper(wrapper, p, q);
  }

  default R visitByteWrapper(ByteWrapper wrapper, P p, Q q) throws TH {
    return visitNumberWrapper(wrapper, p, q);
  }

  default R visitDoubleWrapper(DoubleWrapper wrapper, P p, Q q) throws TH {
    return visitNumberWrapper(wrapper, p, q);
  }

  default R visitFloatWrapper(FloatWrapper wrapper, P p, Q q) throws TH {
    return visitNumberWrapper(wrapper, p, q);
  }

  default R visitIntegerWrapper(IntegerWrapper wrapper, P p, Q q) throws TH {
    return visitNumberWrapper(wrapper, p, q);
  }

  default R visitLongWrapper(LongWrapper wrapper, P p, Q q) throws TH {
    return visitNumberWrapper(wrapper, p, q);
  }

  default R visitShortWrapper(ShortWrapper wrapper, P p, Q q) throws TH {
    return visitNumberWrapper(wrapper, p, q);
  }

  <V extends Number> R visitNumberWrapper(NumberWrapper<V> wrapper, P p, Q q) throws TH;
}
