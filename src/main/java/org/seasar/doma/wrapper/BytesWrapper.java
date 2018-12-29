package org.seasar.doma.wrapper;

import java.util.Arrays;
import org.seasar.doma.DomaNullPointerException;

/**
 * {@code byte[]} のラッパーです。
 *
 * @author taedium
 */
public class BytesWrapper extends AbstractWrapper<byte[]> {

  /** インスタンスを構築します。 */
  public BytesWrapper() {
    super(byte[].class);
  }

  /**
   * 値を指定してインスタンスを構築します。
   *
   * @param value 値
   */
  public BytesWrapper(byte[] value) {
    super(byte[].class, value);
  }

  @Override
  protected byte[] doGetCopy() {
    byte[] original = get();
    if (original == null) {
      return null;
    }
    return Arrays.copyOf(original, original.length);
  }

  @Override
  protected boolean doHasEqualValue(Object otherValue) {
    if (otherValue instanceof byte[]) {
      return Arrays.equals(get(), (byte[]) otherValue);
    }
    return false;
  }

  @Override
  public <R, P, Q, TH extends Throwable> R accept(WrapperVisitor<R, P, Q, TH> visitor, P p, Q q)
      throws TH {
    if (visitor == null) {
      throw new DomaNullPointerException("visitor");
    }
    return visitor.visitBytesWrapper(this, p, q);
  }
}
