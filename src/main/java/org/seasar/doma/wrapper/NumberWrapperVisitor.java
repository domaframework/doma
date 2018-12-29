package org.seasar.doma.wrapper;

/**
 * @author nakamura-to
 * @since 2.0.0
 * @param <R> 戻り値
 * @param <P> 1番目のパラメータ
 * @param <Q> 2番目のパラメータ
 * @param <TH> 例外
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
